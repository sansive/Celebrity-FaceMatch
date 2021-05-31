package appInterface;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.border.BevelBorder;
import javax.swing.ImageIcon;

public class appInterface extends JFrame implements ActionListener {

	private JPanel contentPane;
	private static appInterface frame;
	private JLabel imageUploaded;
	private JButton UploadButton;
	private Icon image;
	private JButton ConfirmButton;
	private JButton ChangeButton;
	private JLabel image_label;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new appInterface();
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
	public appInterface() {
		setTitle("Celebrity FaceMatch");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 755, 526);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		imageUploaded = new JLabel((String) null);
		imageUploaded.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		imageUploaded.setBounds(59, 98, 245, 266);
		contentPane.add(imageUploaded);

		UploadButton = new JButton("Upload image");
		UploadButton.addActionListener(this);
		UploadButton.setBounds(59, 390, 245, 23);
		contentPane.add(UploadButton);
		
		ConfirmButton = new JButton("Confirm image");
		ConfirmButton.addActionListener(this);
		ConfirmButton.setBounds(183, 390, 121, 23);
		contentPane.add(ConfirmButton);
		ConfirmButton.setVisible(false);
		
		ChangeButton = new JButton("Change image");
		ChangeButton.addActionListener(this);
		ChangeButton.setBounds(59, 390, 121, 23);
		contentPane.add(ChangeButton);
		
		image_label = new JLabel("");
		image_label.setBounds(425, 144, 207, 198);
		contentPane.add(image_label);
		ChangeButton.setVisible(false);
	}

	/**
	 * Create event handler
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == UploadButton || e.getSource() == ChangeButton) {
			try {
				// Comprobar extensiones
				JFileChooser fileChooser = new JFileChooser();
				int selection = fileChooser.showOpenDialog(frame);
				if (selection == JFileChooser.APPROVE_OPTION) {
					image = fileChooser.getIcon(fileChooser.getSelectedFile());
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
			/*ImageIcon i = new ImageIcon("images/gif_carga.gif");
			Image img = i.getImage();
			Image imgScaled = img.getScaledInstance(image_label.getWidth(), image_label.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(imgScaled);
            image_label.setIcon(scaledIcon);*/
		}
	}
}