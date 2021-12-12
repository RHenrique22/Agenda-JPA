package aplicacaoSwing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
import modelo.Participante;
import modelo.Reuniao;

public class TelaReuniao {
	private JFrame frame;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton button_1;
	private JButton button;
	private JButton button_2;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JLabel label;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_5;
	private JLabel label_3;
	private JLabel label_4;
	private JLabel label_8;

	private JButton button_3;
	private JLabel label_6;
	private JTextField textField_3;
	private JLabel label_7;
	
	private Timer timer;

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
	public TelaReuniao() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 12));
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
		frame.setTitle("Reuni\u00E3o");
		frame.setBounds(100, 100, 715, 347);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(26, 11, 478, 135);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		table.setGridColor(Color.BLACK);
		table.setRequestFocusEnabled(false);
		table.setFocusable(false);
		table.setBackground(Color.WHITE);
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(true);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane.setViewportView(table);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(
				new Object[][] {},
				new String[] {"id", "datahora", "assunto", "paricipante"}
				));
		table.setShowGrid(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);


		button = new JButton("Criar reuni\u00E3o");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(textField.getText().isEmpty() || textField_1.getText().isEmpty() ||
							textField_2.getText().isEmpty() || textField_3.getText().isEmpty()) {
						label.setText("campo vazio");
						return;
					}
					String data = textField.getText();
					String hora = textField_1.getText();
					String assunto = textField_2.getText();
					String nomes[] = textField_3.getText().trim().split(",");
					Fachada.criarReuniao(data+" "+hora, assunto,  new ArrayList<>(Arrays.asList(nomes)));
					label.setText("reunião criada");
					listagem();
				}
				catch(Exception ex) {
					label.setText(ex.getMessage());
				}
			}
		});

		button_2 = new JButton("Inserir participante");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (table.getSelectedRow() >= 0){
						String id = (String) table.getValueAt( table.getSelectedRow(), 0);
						String nome = JOptionPane.showInputDialog("nome do participante");
						Fachada.adicionarParticipanteReuniao(nome, Integer.parseInt(id));
						label.setText("inseriu na reuniao " + id + " o participante "+ nome);
						listagem();
					}
					else
						label.setText("selecione uma linha");
				}
				catch(Exception erro) {
					label.setText(erro.getMessage());
				}
			}
		});
		button_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button_2.setBounds(514, 63, 169, 23);
		frame.getContentPane().add(button_2);
		button.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button.setBounds(340, 194, 125, 23);
		frame.getContentPane().add(button);

		button_1 = new JButton("Cancelar reuni\u00E3o");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (table.getSelectedRow() >= 0){
						String id = (String) table.getValueAt( table.getSelectedRow(), 0);

						Object[] options = { "Confirmar", "Cancelar" };
						int escolha = JOptionPane.showOptionDialog(null, "Confirma exclusão da reunião "+id, "Alerta",
								JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
						if(escolha == 0) {
							Fachada.cancelarReuniao(Integer.parseInt(id));
							label.setText("reunião cancelada "+id);
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
		button_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button_1.setBounds(514, 39, 169, 23);
		frame.getContentPane().add(button_1);

		label = new JLabel("");
		label.setBackground(Color.RED);
		label.setBounds(26, 286, 650, 14);
		frame.getContentPane().add(label);

		label_1 = new JLabel("data");
		label_1.setHorizontalAlignment(SwingConstants.LEFT);
		label_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		label_1.setBounds(26, 174, 71, 14);
		frame.getContentPane().add(label_1);

		label_2 = new JLabel("horario");
		label_2.setHorizontalAlignment(SwingConstants.LEFT);
		label_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		label_2.setBounds(26, 198, 58, 14);
		frame.getContentPane().add(label_2);

		label_5 = new JLabel("assunto");
		label_5.setHorizontalAlignment(SwingConstants.LEFT);
		label_5.setFont(new Font("Dialog", Font.PLAIN, 12));
		label_5.setBounds(26, 223, 71, 14);
		frame.getContentPane().add(label_5);

		textField = new JTextField();
		textField.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField.setColumns(10);
		textField.setBounds(74, 171, 69, 20);
		frame.getContentPane().add(textField);

		textField_1 = new JTextField();
		textField_1.setText("08:00");
		textField_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField_1.setColumns(10);
		textField_1.setBounds(74, 195, 69, 20);
		frame.getContentPane().add(textField_1);

		textField_2 = new JTextField();
		textField_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField_2.setColumns(10);
		textField_2.setBounds(74, 223, 169, 20);
		frame.getContentPane().add(textField_2);

		label_3 = new JLabel("dd/mm/aaaa");
		label_3.setBounds(153, 175, 71, 14);
		frame.getContentPane().add(label_3);

		label_4 = new JLabel("hh:mm");
		label_4.setBounds(153, 198, 71, 14);
		frame.getContentPane().add(label_4);

		label_8 = new JLabel("selecione");
		label_8.setBounds(26, 146, 367, 14);
		frame.getContentPane().add(label_8);

		button_3 = new JButton("Remover participante");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (table.getSelectedRow() >= 0){
						String id = (String) table.getValueAt( table.getSelectedRow(), 0);
						String nome = (String) table.getValueAt( table.getSelectedRow(), 3);
						Fachada.removerParticipanteReuniao(nome, Integer.parseInt(id));
						label.setText("removeu da reuniao " + id + " o participante "+ nome);
						listagem();
					}
					else
						label.setText("selecione uma linha");
				}
				catch(Exception erro) {
					label.setText(erro.getMessage());
				}
			}
		});
		button_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button_3.setBounds(514, 88, 169, 23);
		frame.getContentPane().add(button_3);

		label_6 = new JLabel("nomes:");
		label_6.setHorizontalAlignment(SwingConstants.LEFT);
		label_6.setFont(new Font("Dialog", Font.PLAIN, 12));
		label_6.setBounds(26, 251, 71, 14);
		frame.getContentPane().add(label_6);

		textField_3 = new JTextField();
		textField_3.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField_3.setColumns(10);
		textField_3.setBounds(74, 248, 391, 20);
		frame.getContentPane().add(textField_3);

		label_7 = new JLabel("separador: \",\"");
		label_7.setBounds(471, 252, 113, 14);
		frame.getContentPane().add(label_7);

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

	//*****************************
	public void listagem () {
		try {
			List<Reuniao> lista = Fachada.listarReunioes();

			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("id");
			model.addColumn("dataHora");
			model.addColumn("assunto");
			model.addColumn("participante");
			String datastr;
			for(Reuniao r : lista) {
				for(Participante p : r.getParticipantes())	{
					datastr = r.getDatahora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
					model.addRow(new String[]{r.getId()+"", datastr, r.getAssunto(), p.getNome()});
				}
			}
			table.setModel(model);
			label_8.setText("resultados: "+lista.size()+ " reunioes  - selecione uma linha");
		}
		catch(Exception erro){
			label.setText(erro.getMessage());
		}

	}
}
