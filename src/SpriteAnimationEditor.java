import java.awt.Window;
import java.awt.event.KeyEvent;

import javax.swing.*;

import info.clearthought.layout.*;

public class SpriteAnimationEditor {

	public static void main(String[] args) {
		
		//Frame
		JFrame frame = new JFrame("Sprite Animation Editor");
		frame.setSize(800,600);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		//Panel
        JPanel framePanel = new JPanel();
        framePanel.setOpaque(false);
        double[][] tab = {{0.5, 0.5}, {TableLayout.FILL}};
        TableLayout tl = new TableLayout(tab);
        framePanel.setLayout(tl);
        
        SpritePanel sp = new SpritePanel();
		framePanel.add(sp, "1,0");
		
		MenuPanel mp = new MenuPanel(sp);
        framePanel.add(mp, "0,0");
        
        // Listener de la fenêtre
        WindowListener wl = new WindowListener(sp, frame);
        
        //Menu Bar
        // ---file---
        JMenu fileMenu = new JMenu("File");
        // ---file---
        //		|> Open
		JMenuItem fileMenu_open = new JMenuItem("Open");
		fileMenu_open.addActionListener(wl);
		fileMenu_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, 
			 	KeyEvent.CTRL_DOWN_MASK));
		// ---file---
        //		|> Save
		JMenuItem fileMenu_save = new JMenuItem("Save");
		fileMenu_save.addActionListener(wl);
		fileMenu_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 
			 	KeyEvent.CTRL_DOWN_MASK));
		// ---file---
        //		|> Convert to XML
		JMenuItem fileMenu_toxml = new JMenuItem("Convert to XML");
		fileMenu_toxml.addActionListener(wl);
		fileMenu_toxml.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, 
			 	KeyEvent.CTRL_DOWN_MASK));
		// ---file---
        //		|> Quit
		JMenuItem fileMenu_quit = new JMenuItem("Quit");
		fileMenu_quit.addActionListener(wl);
		fileMenu_quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 
			 	KeyEvent.CTRL_DOWN_MASK));
		
		fileMenu.add(fileMenu_open);
		fileMenu.add(fileMenu_save);
		fileMenu.add(fileMenu_toxml);
		fileMenu.add(fileMenu_quit);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		
		frame.setJMenuBar(menuBar);
		frame.setContentPane(framePanel);
	}

}
