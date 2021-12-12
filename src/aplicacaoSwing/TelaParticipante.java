package aplicacaoSwing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import fachada.Fachada;
import modelo.Convidado;
import modelo.Participante;
import modelo.Reuniao;

public class TelaParticipante {
	private JFrame frame;
	private JTable table;
	private JScrollPane scrollPane;
	private JLabel label;
	private JLabel label_3;
	private JLabel label_1;
	private JLabel label_2;
	private JTextField textField;
	private JTextField textField_1;
	private JButton button_1;

	private Timer timer;
	private JButton button;
	private JButton button_2;
	private JLabel label_4;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	//	public static void main(String[] args) {
	//		EventQueue.invokeLater(new Runnable() {
	//			public void run() {
	//				try {
	//					TelaReuniao window = new TelaReuniao();
	//					window.frame.setVisible(true);
	//				} catch (Exception e) {
	//					e.printStackTrace();
	//				}
	//			}
	//		});
	//	}

	/**
	 * Create the application.
	 */
	public TelaParticipante() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Participante");
		frame.setBounds(100, 100, 586, 368);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				listagem();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				timer.stop();
			}
		});

		scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 11, 431, 207);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		table.setGridColor(Color.BLACK);
		table.setRequestFocusEnabled(false);
		table.setFocusable(false);
		table.setBackground(Color.WHITE);
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(true);
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		scrollPane.setViewportView(table);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(
				new Object[][] {},
				new String[] {"nome", "email", "reuniao"}
				));
		table.setShowGrid(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		label = new JLabel("");
		label.setForeground(Color.RED);
		label.setBounds(21, 302, 539, 14);
		frame.getContentPane().add(label);

		label_3 = new JLabel("selecione");
		label_3.setBounds(21, 216, 431, 14);
		frame.getContentPane().add(label_3);

		label_1 = new JLabel("nome:");
		label_1.setHorizontalAlignment(SwingConstants.LEFT);
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_1.setBounds(21, 244, 71, 14);
		frame.getContentPane().add(label_1);

		textField = new JTextField();
		textField.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField.setColumns(10);
		textField.setBackground(Color.WHITE);
		textField.setBounds(76, 241, 134, 20);
		frame.getContentPane().add(textField);

		label_2 = new JLabel("email:");
		label_2.setHorizontalAlignment(SwingConstants.LEFT);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_2.setBounds(21, 277, 71, 14);
		frame.getContentPane().add(label_2);

		textField_1 = new JTextField();
		textField_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField_1.setColumns(10);
		textField_1.setBounds(76, 271, 195, 20);
		frame.getContentPane().add(textField_1);


		button = new JButton("Apagar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (table.getSelectedRow() >= 0){
						String nome = (String) table.getValueAt( table.getSelectedRow(), 0);

						Object[] options = { "Confirmar", "Cancelar" };
						int escolha = JOptionPane.showOptionDialog(null, "Confirma exclusão de participante "+ nome, "Alerta",
								JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
						if(escolha == 0) {
							Fachada.apagarParticipante(nome);
							label.setText("apagou "+nome);
							listagem();
						}
					}
					else
						label.setText("selecione uma linha");
				}
				catch(Exception erro) {
					label.setText(erro.getMessage());
				}
			}
		});
		button.setBounds(462, 105, 89, 23);
		frame.getContentPane().add(button);

		button_1 = new JButton("Criar Participante");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(textField.getText().isEmpty() || textField_1.getText().isEmpty()) {
						label.setText("campo vazio");
						return;
					}
					String nome = textField.getText();
					String email = textField_1.getText();
					Fachada.criarParticipante(nome, email);
					label.setText("participante criado");
					listagem();
				}
				catch(Exception ex) {
					label.setText(ex.getMessage());
				}
			}
		});
		button_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		button_1.setBounds(263, 241, 134, 21);
		frame.getContentPane().add(button_1);

		button_2 = new JButton("Criar Convidado");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(textField.getText().isEmpty() || textField_1.getText().isEmpty()
							|| textField_2.getText().isEmpty()) {
						label.setText("campo vazio");
						return;
					}
					String nome = textField.getText();
					String email = textField_1.getText();
					String empresa = textField_2.getText();
					Fachada.criarConvidado(nome, email, empresa);
					label.setText("convidado criado");
					listagem();
				}
				catch(Exception ex) {
					label.setText(ex.getMessage());
				}
			}
		});
		button_2.setFont(new Font("Tahoma", Font.PLAIN, 11));
		button_2.setBounds(403, 241, 134, 21);
		frame.getContentPane().add(button_2);

		label_4 = new JLabel("empresa:");
		label_4.setHorizontalAlignment(SwingConstants.LEFT);
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_4.setBounds(281, 274, 71, 14);
		frame.getContentPane().add(label_4);

		textField_2 = new JTextField();
		textField_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField_2.setColumns(10);
		textField_2.setBounds(342, 271, 195, 20);
		frame.getContentPane().add(textField_2);

		frame.setVisible(true);

		//----------timer----------------
		timer = new Timer(0, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listagem();
			}
		});
		timer.setRepeats(true);
		timer.setDelay(10000);	//10segundos
		timer.start();			//inicia o temporizador
	}

	public void listagem() {
		try{
			List<Participante> lista = Fachada.listarParticipantes();

			//objeto model contem todas as linhas e colunas da tabela
			DefaultTableModel model = new DefaultTableModel();

			//criar as colunas (0,1,2) da tabela
			model.addColumn("nome");
			model.addColumn("email");
			model.addColumn("empresa");
			model.addColumn("reuniao");

			//criar as linhas da tabela
			String texto;
			String empresa;
			
			for(Participante p : lista) {
				//empresa so existe para convidado
				if(p instanceof Convidado conv) { //conv é a mesma referencia de p
					empresa = conv.getEmpresa();
				} 
				else
					empresa = "";
				
				if(p.getReunioes().isEmpty())
					model.addRow(new Object[]{p.getNome(), p.getEmail(),empresa, "sem reunioes"});
				else
					for(Reuniao r : p.getReunioes()) {
						texto = r.getDatahora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +" - "+r.getAssunto() ;
						model.addRow(new Object[]{p.getNome(), p.getEmail(), empresa, texto});
					}
			}
			table.setModel(model);
			label_3.setText("resultados: "+lista.size()+ " participantes   - selecione uma linha");
		}
		catch(Exception erro){
			label.setText(erro.getMessage());
		}
	}
}
