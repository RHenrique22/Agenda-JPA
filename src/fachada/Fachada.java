package fachada;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import dao.DAO;
import dao.DAOConvidado;
import dao.DAOParticipante;
import dao.DAOReuniao;
import modelo.Convidado;
import modelo.Participante;
import modelo.Reuniao;

public class Fachada {

//	private static String emailOrigem;
//	private static String senhaOrigem;
//	private static boolean emailDesabilitado;
	private static DAOParticipante daoParticipante = new DAOParticipante();
	private static DAOReuniao daoReuniao = new DAOReuniao();
	private static DAOConvidado daoConvidado = new DAOConvidado();

//	public static void setEmailSenha(String email, String senha) {
//		emailOrigem = email;
//		senhaOrigem = senha;
//	}
//	public static void desabilitarEmail(boolean status) {
//		emailDesabilitado = status;
//	}

	public static void inicializar()  {
		DAO.open();
	}

	public static void	finalizar() {
		DAO.close();
	}


	public static Participante criarParticipante(String nome, String email) throws Exception {
		nome = nome.trim();
		email = email.trim();

		//inicio da transacao
		DAO.begin();

		//Verificar se o participande existe
		Participante p =  daoParticipante.read(nome);

		if (p != null) {
			DAO.rollback();
			throw new Exception("Participante " + nome + " ja cadastrado(a)");
		}
		else {
			p = new Participante (nome, email);
		}

		//persistir novo participante
		daoParticipante.create(p);

		//fim da transacao
		DAO.commit();

		return p;
	}

	public static Convidado criarConvidado(String nome, String email, String empresa) throws Exception {
		nome = nome.trim();
		email = email.trim();
		empresa = empresa.trim();

		//inicio da transacao
		DAO.begin();

		//Verificar se o convidado existe
		Convidado c =  daoConvidado.read(nome);

		if (c != null) {
			DAO.rollback();
			throw new Exception("Convidado " + nome + " ja cadastrado(a)");
		}

		//Cadastrar participante na reunião
		c = new Convidado(nome, email, empresa);

		//persistir novo convidado no banco
		daoConvidado.create(c);

		//fim da transacao
		DAO.commit();

		return c;
	}

	public static Reuniao criarReuniao(String datahora, String assunto, ArrayList<String> nomes) throws Exception {
		assunto = assunto.trim();
		String exceptionH = "";
		String exceptionNotExist = "";

		//inicio da transacao
		DAO.begin();

		LocalDateTime dth;

		try {
			DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			dth  = LocalDateTime.parse(datahora, parser);
		}
		catch (DateTimeParseException e) {
			DAO.rollback();
			throw new Exception ("Reuniao com formato de data invalido");
		}

		//Verificar o tamanho da lista de participantes se é > 2
		if (nomes.size() < 2) {
			DAO.rollback();
			throw new Exception("Reunião sem quórum mínimo de dois participantes");
		}

		ArrayList<Participante> participantes = new ArrayList<Participante>();

		for (String n : nomes) {
			//Verificar se o participante existe
			Participante p =  daoParticipante.read(n);

			if (p == null) {
				throw new Exception("Participante " + n + " inexistente");
//				exceptionNotExist += String.format("Participante %s inexistente%n", n);
			}

			if (p.TotalDeReunioes() == 0) {
				participantes.add(p);
			}
			else {
				boolean teste = false;
				//Verificar se o participante já está em outra reunião no mesmo horário
				for (Reuniao r1 : p.getReunioes()) 	{
					Duration duracao = Duration.between(r1.getDatahora(), dth); //(d - hinicio)
					long horas = duracao.toHours();
					if (Math.abs(horas) < 2) {
						throw new Exception("Participante " + p.getNome() + " já está em outra reunião nesse horário");
//						exceptionH += String.format("Participante %s já está em outra reunião nesse horário", p.getNome());
//						teste = false;
//						break;
					}
					else {
						teste = true;
					}
				}

				if (teste) {
					participantes.add(p);
				}

			}
		}

		if (participantes.size() < 2) {
			DAO.rollback();
			throw new Exception (exceptionNotExist + exceptionH + "Reunião sem quórum mínimo de dois participantes");
		}

		Reuniao r = new Reuniao(dth.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) ,assunto);

		//relacionar participante e reuniao
		for (Participante p : participantes) {
			r.adicionar(p);
			p.adicionar(r);
		}

		//persistir nova reuniao no banco
		daoReuniao.create(r);

		//fim da transacao
		DAO.commit();

		// enviar email para participantes
		// for(Participante p : participantes)	{
		// 	enviarEmail(p.getEmail(), "nova reunião",
		// 	 "Você foi agendado para a reunião na data: " + r.getDatahora() + " e assunto: " + assunto);
		// }

		return r;
	}

	public static void 	adicionarParticipanteReuniao(String nome, int id) throws Exception 	{
		nome = nome.trim();

		//inicio da transacao
		DAO.begin();

		//Verificar se o participante existe
		Participante p = daoParticipante.read(nome);

		if (p == null) {
			DAO.rollback();
			throw new Exception("Participante " + nome + " não consta no cadastro");
		}

		//Verificar de a reuniaão existe no repositório
		Reuniao r = daoReuniao.read(id);

		if (r == null) {
			DAO.rollback();
			throw new Exception("Reuniao " + id + " não cadastrada");
		}

		//Verificar se o participante já participa desta reunião
		if (r.localizarParticipante(nome) != null) {
			DAO.rollback();
			throw new Exception("Participante " + nome + " já cadastrado na reunião " + id);
		}

		//Verificar se o participante já está em outra reunião no mesmo horário
		for (Reuniao r1 : p.getReunioes()) 	{
			LocalDateTime hinicio = r1.getDatahora();
			Duration duracao = Duration.between(r1.getDatahora(), r.getDatahora());
			long horas = duracao.toHours();

			if (Math.abs(horas) < 2) {
				DAO.rollback();
				throw new Exception("Participante já está em outra reunião nesse horário");
			}
		}

		r.adicionar(p);
		daoReuniao.update(r);
		p.adicionar(r);
		daoParticipante.update(p);

		DAO.commit();

		// enviarEmail(p.getEmail(), "novo participante",
		//  "Você foi adicionado a reunião na data:" + r.getDatahora() + " e assunto:" + r.getAssunto());

	}

	public static void 	removerParticipanteReuniao(String nome, int id) throws Exception {
		nome = nome.trim();

		//inicio da transacao
		DAO.begin();

		//Verificar se o participante existe
		Participante p = daoParticipante.read(nome);

		if (p == null) {
			DAO.rollback();
			throw new Exception("Participante " + nome + " não consta no cadastro");
		}

		//Verificar se a reunião está cadastrada
		Reuniao r = daoReuniao.read(id);

		if (r == null) {
			DAO.rollback();
			throw new Exception("Reuniao " + id + " não cadastrada");
		}

		r.remover(p);
		daoReuniao.update(r);

		p.remover(r);
		daoParticipante.update(p);
		
		DAO.commit();

		// enviarEmail(p.getEmail(), "participante removido",
		//  "Você foi removido da reunião na data:" + r.getDatahora() + " e assunto:" + r.getAssunto());

		if (r.TotalDeParticipantes() < 2) {
			cancelarReuniao(id);
		}
			
	}

	public static void	cancelarReuniao(int id) throws Exception {
		//inicio da transacao
		DAO.begin();

		//Verificar se a reunião está cadastrada
		Reuniao r = daoReuniao.read(id);

		if (r == null) {
			throw new Exception("Reuniao " + id + " não cadastrada");
		}

		//Remover participante da reunião
		for (Participante p : r.getParticipantes()) {
			p.remover(r);
			daoParticipante.update(p);
		}

		//apagar reunião no banco
		daoReuniao.delete(r);

		//fim da transacao
		DAO.commit();

		//enviar email para todos os participantes
		// for (Participante p : r.getParticipantes()) {
		// 	enviarEmail(p.getEmail(), "reuniao cancelada",
		// 	 "data: " + r.getDatahora() + " e assunto:" + r.getAssunto());
		// }

	}

	public static void apagarParticipante(String nome) throws Exception {
		nome = nome.trim();
		ArrayList<Reuniao> cancelarR = new ArrayList<Reuniao>();


		//inicio da transacao
		DAO.begin();

		//Verificar se o participande existe
		Participante p = daoParticipante.read(nome);

		if (p == null) {
			DAO.rollback();
			throw new Exception("Participante " + nome + " nao cadastrado(a)");
		}

		//remover o participante das reunioes que participa
		for (Reuniao r : p.getReunioes()) {
			r.remover(p);
			daoReuniao.update(r);

			if (r.TotalDeParticipantes() < 2) {
				cancelarR.add(r);
			}
		}

		daoParticipante.delete(p);

		DAO.commit();

		if (cancelarR.size() > 0) {
			for (Reuniao r : cancelarR) {
				cancelarReuniao(r.getId());
			}
		}

		// enviarEmail(p.getEmail()," descadastro ",  "vc foi excluido da agenda");

	}

	public static List<Participante> listarParticipantes() {
		return daoParticipante.readAll();
	}

	public static List<Convidado> listarConvidados() {
		return daoConvidado.readAll();
	}

	public static List<Reuniao> listarReunioes() 	{
		return daoReuniao.readAll();
	}

	public static List<Participante> consulta(String nome, int mes) {
		return daoParticipante.consulta(nome, mes);
	}

	public static List<Reuniao> consulta() {
		return daoReuniao.consulta();
	}


	/*
	 * ********************************************************
	 * Obs: lembrar de desligar antivirus e
	 * de ativar "Acesso a App menos seguro" na conta do gmail
	 *
	 * biblioteca java.mail 1.6.2
	 * ********************************************************
	 */

	// public static void enviarEmail(String emaildestino, String assunto, String mensagem) {
	// 	try {
	// 		if (Fachada.emailDesabilitado)
	// 			return;

	// 		String emailorigem = Fachada.emailOrigem;
	// 		String senhaorigem = Fachada.senhaOrigem;

	// 		//configurar email de origem
	// 		Properties props = new Properties();
	// 		props.put("mail.smtp.starttls.enable", "true");
	// 		props.put("mail.smtp.host", "smtp.gmail.com");
	// 		props.put("mail.smtp.port", "587");
	// 		props.put("mail.smtp.auth", "true");
	// 		Session session;
	// 		session = Session.getInstance(props,
	// 				new javax.mail.Authenticator() 	{
	// 			protected PasswordAuthentication getPasswordAuthentication() 	{
	// 				return new PasswordAuthentication(emailorigem, senhaorigem);
	// 			}
	// 		});

	// 		//criar e enviar email
	// 		MimeMessage message = new MimeMessage(session);
	// 		message.setSubject(assunto);
	// 		message.setFrom(new InternetAddress(emailorigem));
	// 		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emaildestino));
	// 		message.setText(mensagem);   // usar "\n" para quebrar linhas
	// 		// Transport.send(message);
	// 	}
	// 	catch (MessagingException e) {
	// 		System.out.println(e.getMessage());
	// 	}
	// 	catch (Exception e) {
	// 		System.out.println(e.getMessage());
	// 	}
	// }
}
