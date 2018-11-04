import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class WindowListener implements ActionListener {

	private SpritePanel m_sp;
	private JFrame m_frame;
	
	public WindowListener(SpritePanel sp, JFrame frame){
		m_sp = sp;
		m_frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getActionCommand().equals("Open")){
			try {
				JFileChooser jfc = new JFileChooser();
				jfc.setMultiSelectionEnabled(false);
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = jfc.showOpenDialog(m_frame);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	m_sp.loadPictures(jfc.getSelectedFile().getPath());
			    }
				
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		else if(e.getActionCommand().equals("Save")){
			try {
				m_sp.savePictures();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		else if(e.getActionCommand().equals("Convert to XML")) {
			try{
				Runtime run = Runtime.getRuntime();
				String path = m_sp.getAnimationPath();
				path = path.replace('\\', '/');
				String[] stab = path.split("/");
				final Process process = run.exec(new String[] { "AnimationCreator", path, stab[stab.length-1]});
				
				// Consommation de la sortie standard de l'application externe dans un Thread separe
				new Thread() {
					public void run() {
						try {
							BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
							String line = "";
							try {
								while((line = reader.readLine()) != null) {
									// Traitement du flux de sortie de l'application si besoin est
								}
							} finally {
								reader.close();
							}
						} catch(IOException ioe) {
							ioe.printStackTrace();
							JOptionPane.showMessageDialog(m_frame, ioe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}.start();

				// Consommation de la sortie d'erreur de l'application externe dans un Thread separe
				new Thread() {
					public void run() {
						try {
							BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
							String line = "";
							try {
								String msg = "";
								while((line = reader.readLine()) != null) {
									msg += line;
								}
								
								JOptionPane.showMessageDialog(m_frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
							} finally {
								reader.close();
							}
						} catch(IOException ioe) {
							ioe.printStackTrace();
							JOptionPane.showMessageDialog(m_frame, ioe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}.start();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		else if(e.getActionCommand().equals("Quit")){
			System.exit(0);
		}
	}

}
