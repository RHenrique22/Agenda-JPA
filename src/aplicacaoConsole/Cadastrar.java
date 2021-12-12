package aplicacaoConsole;

import fachada.*;
import modelo.*;

import java.util.ArrayList;

public class Cadastrar {
    public Cadastrar() {
		try {
			Fachada.inicializar();
//			Fachada.desabilitarEmail(true);

			Participante p;
			Convidado c;
			Reuniao r;
			p = Fachada.criarParticipante("joao", "joao@gmail.com");
			p = Fachada.criarParticipante("maria", "maria@gmail.com");
			c = Fachada.criarConvidado("jose", "jose@gmail.com", "microsoft");
			c = Fachada.criarConvidado("ana", "ana@gmail.com", "ibm");
			c = Fachada.criarConvidado("paulo", "paulo@gmail.com", "oracle");

			ArrayList<String> nomes;
			nomes = new ArrayList<>();
			nomes.add("joao");
			nomes.add("maria");
			r = Fachada.criarReuniao("01/11/2021 08:00", "assunto1", nomes);

			nomes = new ArrayList<>();
			nomes.add("joao");
			nomes.add("maria");
			r = Fachada.criarReuniao("01/11/2021 10:00", "assunto2", nomes);
			
			nomes = new ArrayList<>();
			nomes.add("jose");
			nomes.add("ana"); // Ana
			r = Fachada.criarReuniao("01/12/2021 08:00", "assunto3", nomes);

			nomes = new ArrayList<>();
			nomes.add("jose");
			nomes.add("ana");
			r = Fachada.criarReuniao("01/12/2021 10:00", "assunto4", nomes);
			
			nomes = new ArrayList<>();
			nomes.add("jose");
			nomes.add("paulo");
			r = Fachada.criarReuniao("02/12/2021 10:00", "assunto5", nomes);
			
			nomes = new ArrayList<>();
			nomes.add("joao");
			nomes.add("maria");
			r = Fachada.criarReuniao("02/12/2021 08:00", "assunto6", nomes);
			
			nomes = new ArrayList<>();
			nomes.add("joao");
			nomes.add("maria");
			nomes.add("jose");
			nomes.add("ana");
			nomes.add("paulo");
			r = Fachada.criarReuniao("02/12/2021 10:00", "assunto7", nomes);
		} 
		catch (Exception e) {
			System.out.println("---> " + e.getMessage());
		}	
		Fachada.finalizar();
	}

	public static void main (String[] args) {
		Cadastrar app = new Cadastrar();
	}
}
