package aplicacaoSwing;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import fachada.Fachada;

public class TelaPrincipal {
	private JFrame frame;
	private JMenu mnParticipante;
	private JMenu mnReuniao;
	private JLabel label;
	private Timer timer;
	private JMenu mnEmail;
	private JMenu mnConsulta;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaPrincipal window = new TelaPrincipal();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TelaPrincipal() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				try {
					Fachada.inicializar();
					definirEmailSenha();
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(frame, e.getMessage());
				}
			}
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					Fachada.finalizar();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, e1.getMessage());
				}
				timer.stop();
				//	JOptionPane.showMessageDialog(frame, "até breve !");

			}
		});
		frame.setTitle("Agenda de reuni\u00E3o");
		frame.setBounds(100, 100, 450, 363);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		label = new JLabel("");
		label.setFont(new Font("Tahoma", Font.PLAIN, 26));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setText("Inicializando...");
		label.setBounds(0, 0, 450, 313);
		ImageIcon imagem = new ImageIcon(getClass().getResource("/imagens/imagem.png"));
		imagem = new ImageIcon(imagem.getImage().getScaledInstance(label.getWidth(),label.getHeight(), Image.SCALE_DEFAULT));//		label.setIcon(fotos);
		label.setIcon(imagem);
		frame.getContentPane().add(label);
		frame.setResizable(false);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		mnParticipante = new JMenu("Participante");
		mnParticipante.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TelaParticipante tela = new TelaParticipante();
			}
		});
		menuBar.add(mnParticipante);

		mnReuniao = new JMenu("Reuniao");
		mnReuniao.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TelaReuniao tela = new TelaReuniao();
			}
		});
		menuBar.add(mnReuniao);

		mnEmail = new JMenu("Emails");
		mnEmail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				definirEmailSenha();
			}
		});
		menuBar.add(mnEmail);

		mnConsulta = new JMenu("Consultas");
		mnConsulta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//	TelaConsulta tela = new TelaConsulta();
			}
		});
		menuBar.add(mnConsulta);

		frame.setVisible(true);


		//----------timer----------------
		timer = new Timer(0, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String dt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
				frame.setTitle("Agenda de reuniao - "+ dt);
			}
		});
		timer.setRepeats(true);
		timer.setDelay(1000);	//1segundos
		timer.start();			//inicia o temporizador
	}

	public static void definirEmailSenha() {
		JTextField text = new JTextField(20);
		JPasswordField password = new JPasswordField(10);
		password.setEchoChar('*'); 
		JCheckBox check = new JCheckBox("Desabilitar envio de emails:");
		check.setSelected(true);

		JPanel painel = new JPanel();
		painel.setLayout(new BoxLayout(painel, BoxLayout.PAGE_AXIS));
		painel.add(new JLabel("email de origem:"));
		painel.add(text);
		painel.add(new JLabel("senha:"));
		painel.add(password);
		painel.add(check);

		JOptionPane.showMessageDialog(null, painel, "Dados para envio de email", JOptionPane.PLAIN_MESSAGE);
		String email = text.getText().trim();	
		String senha = new String(password.getPassword()).trim();
//		Fachada.setEmailSenha(email, senha);
//		Fachada.desabilitarEmail(check.isSelected());
	}
}