package app.gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import app.agents.PerceptionAgent;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class JFrameApp extends JFrame implements ActionListener {
    
	public static final long serialVersionUID = 1L;
    
    private JPanel contentPane;
	private JLabel imageUploaded;
	private JButton UploadButton;
	private File file;
	private JButton ConfirmButton;
	private JButton ChangeButton;
	private JLabel title;
	private JLabel text;
	public JLabel calculando;
	public JLabel resultado1;
	public JLabel resultado2;
	public JLabel resultado3;
	public JLabel resultados;

    protected PerceptionAgent agent;
    
    public JFrameApp(String nombre, PerceptionAgent agent) {
    	super();
    	this.agent = agent;
        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 755, 526);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		imageUploaded = new JLabel((String) null);
		imageUploaded.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		imageUploaded.setBounds(59, 150, 245, 266);
		contentPane.add(imageUploaded);

		UploadButton = new JButton("Upload image");
		UploadButton.addActionListener(this);
		UploadButton.setBounds(59, 427, 245, 23);
		contentPane.add(UploadButton);
		
		ConfirmButton = new JButton("Confirm image");
		ConfirmButton.addActionListener(this);
		ConfirmButton.setBounds(183, 427, 121, 23);
		contentPane.add(ConfirmButton);
		ConfirmButton.setVisible(false);
		
		ChangeButton = new JButton("Change image");
		ChangeButton.addActionListener(this);
		ChangeButton.setBounds(59, 427, 121, 23);
		contentPane.add(ChangeButton);
		
		title = new JLabel("Welcome to Celebrity FaceMatch!!");
		title.setFont(new Font("Tahoma", Font.BOLD, 26));
		title.setBounds(150, 25, 460, 62);
		contentPane.add(title);
		
		text = new JLabel("Upload a photo of yourseft and find out which celebrity you look like");
		text.setFont(new Font("Tahoma", Font.PLAIN, 18));
		text.setBounds(101, 84, 558, 30);
		contentPane.add(text);
		
		calculando = new JLabel("");
		calculando.setIcon(new ImageIcon("images/gif_carga.gif"));
		calculando.setBounds(350, 175, 300, 200);
		calculando.setFont(new Font("Tahoma", Font.BOLD, 15));
		calculando.setVisible(false);
		calculando.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(calculando);
		
		resultados = new JLabel("");
		resultados.setBounds(350, 175, 300, 50);
		resultados.setFont(new Font("Tahoma", Font.BOLD, 15));
		resultados.setVisible(true);
		resultados.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(resultados);
		
		resultado1 = new JLabel("");
		resultado1.setBounds(350, 225, 300, 50);
		resultado1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		resultado1.setVisible(true);
		resultado1.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(resultado1);
        
		resultado2 = new JLabel("");
		resultado2.setBounds(350, 275, 300, 50);
		resultado2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		resultado2.setVisible(true);
		resultado2.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(resultado2);
        
		resultado3 = new JLabel("");
		resultado3.setBounds(350, 325, 300, 50);
		resultado3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		resultado3.setVisible(true);
		resultado3.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(resultado3);
    }
    
    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == UploadButton || e.getSource() == ChangeButton) {
			try {
				resultados.setVisible(false);
				resultado1.setVisible(false);
				resultado2.setVisible(false);
				resultado3.setVisible(false);
				
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "gif", "png", "jpeg");
				fileChooser.setFileFilter(filter);
				
				int selection = fileChooser.showOpenDialog(this);
				if (selection == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile();
					imageUploaded.setIcon(new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath()));
					
					ImageIcon i = new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath());
					Image img = i.getImage();
					Image imgScaled = img.getScaledInstance(imageUploaded.getWidth(), imageUploaded.getHeight(), Image.SCALE_SMOOTH);
		            ImageIcon scaledIcon = new ImageIcon(imgScaled);
		            imageUploaded.setIcon(scaledIcon);
					
					UploadButton.setVisible(false);
					ConfirmButton.setVisible(true);
					ChangeButton.setVisible(true);
				}

			} catch (Exception exception) {
				System.err.println("Error al adjuntar la imagen");
			}
			
		}
		
		if (e.getSource() == ConfirmButton) {
			calculando.setVisible(true);
			
			try {
				agent.sendMessage(file);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
    
    public void ShowResults(String name1, String name2, String name3) {
    	calculando.setVisible(false);
		resultados.setVisible(true);
		resultado1.setVisible(true);
		resultado2.setVisible(true);
		resultado3.setVisible(true);
		
    	resultados.setText("The celebrities you look like the most...");
    	resultado1.setText("1.- " + name1);
		resultado2.setText("2.- " + name2);
		resultado3.setText("3.- " + name3);
    }

}