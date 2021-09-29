package gui;


import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;
import java.awt.Button;
import javax.swing.JScrollPane;


import compiler.Compiler;
import javax.swing.JTextField;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static JTextArea outputText;
	private static String text = "";
	private static JTextField pathLabel;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Button loadFileBtn = new Button("Load");
		loadFileBtn.setActionCommand("Load");
		loadFileBtn.setBounds(290, 11, 55, 26);	
		loadFileBtn.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				try {
					FileFrame frame = new FileFrame();
					frame.setVisible(true);
					frame.dispose();
				} catch (Exception error) {
					error.printStackTrace();
				}
			} 
		});
		contentPane.add(loadFileBtn);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 48, 390, 202);
		contentPane.add(scrollPane);
		
		outputText = new JTextArea();
		outputText.setEditable(false);
		scrollPane.setViewportView(outputText);
		
		Button playButton = new Button("Play");
		playButton.setBounds(355, 11, 55, 26);
		playButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				MainFrame.resetOutput();
				Compiler compiler = new Compiler();
				String path = pathLabel.getText();
				if(path != null)
					if(path.length() > 3)
						if(path.subSequence(path.length()-3, path.length()).equals("txt"))
							compiler.compile(pathLabel.getText());
						else
							MainFrame.printOutput("File must be a txt");
					else
						MainFrame.printOutput("Not a valid file");
				else
					MainFrame.printOutput("First select a file");
			} 
		});
		
		contentPane.add(playButton);
		
		pathLabel = new JTextField();
		pathLabel.setBounds(20, 11, 264, 26);
		contentPane.add(pathLabel);
		pathLabel.setColumns(10);
	}
	
	
	public static void printOutput(String newText) {
		text = text + newText + "\n";
		outputText.setText(text);
	}
	
	public static void resetOutput() {
		text = "";
		outputText.setText(text);
	}

	public static void setPath(String filename) {
		pathLabel.setText(filename);
	}
}
