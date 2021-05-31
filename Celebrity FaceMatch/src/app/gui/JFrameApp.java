package app.gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

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
    }
    
    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == UploadButton || e.getSource() == ChangeButton) {
			try {
				// Comprobar extensiones
				JFileChooser fileChooser = new JFileChooser();
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
			try {
				agent.sendMessage(file);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}