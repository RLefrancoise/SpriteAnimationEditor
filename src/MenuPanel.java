import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import info.clearthought.layout.*;

public class MenuPanel extends JPanel {

	private static final long serialVersionUID = -4589449748090192906L;
	
	private JTextField m_xHotPoint;
	private JTextField m_yHotPoint;
	private JTextField m_frameTime;
	private JCheckBox m_centerToHotPoint;
	
	public MenuPanel(SpritePanel sp){
		super();
		
		setOpaque(false);
        double[][] tab = {{0.05,0.4,0.1,0.4,0.05}, {0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05}};
        TableLayout tl = new TableLayout(tab);
        setLayout(tl);
        
        MenuListener ml = new MenuListener(this, sp);
        
        JButton addDefenceBox = new JButton("Add Defence Box");
        addDefenceBox.addActionListener(ml);
        
        JButton addOffenseBox = new JButton("Add Offense Box");
        addOffenseBox.addActionListener(ml);
        
        JButton nextFrame = new JButton("Next Frame");
        nextFrame.addActionListener(ml);
        
        JButton prevFrame = new JButton("Prev. Frame");
        prevFrame.addActionListener(ml);
        
        JButton addHotPoint = new JButton("Add Hot Point");
        addHotPoint.addActionListener(ml);
        m_xHotPoint = new JTextField();
        m_yHotPoint = new JTextField();
        
        JButton copyPrevHotPoint = new JButton("Copy Prev. Hot Point");
        copyPrevHotPoint.addActionListener(ml);
        
        m_centerToHotPoint = new JCheckBox("Center to hot point");
        m_centerToHotPoint.setSelected(false);
        m_centerToHotPoint.addItemListener(ml);
        
        m_frameTime = new JTextField();
        JPanel timePanel = new JPanel();
        double[][] timeTab = { {0.05, 0.2, 0.05, 0.2, 0.1, 0.3, 0.1}, {TableLayout.FILL} };
        JButton timeButton = new JButton("Set Time");
        timeButton.addActionListener(ml);
        
        timePanel.setLayout(new TableLayout(timeTab));
        timePanel.add(new JLabel("Time"), "1,0");
        timePanel.add(m_frameTime, "3,0");
        timePanel.add(timeButton, "5,0");
        
        JButton copyPrevDefenseBoxes = new JButton("Prev. DB");
        copyPrevDefenseBoxes.addActionListener(ml);
        
        JButton copyPrevOffenseBoxes = new JButton("Prev. OB");
        copyPrevOffenseBoxes.addActionListener(ml);
        
        JButton clearBoxes = new JButton("Clear Boxes");
        clearBoxes.addActionListener(ml);
        
        JPanel boxesPanel = new JPanel();
        double[][] boxesTab = { {0.3, 0.05, 0.3, 0.05, 0.3}, {TableLayout.FILL} };
        boxesPanel.setLayout(new TableLayout(boxesTab));
        boxesPanel.add(copyPrevDefenseBoxes, "0,0");
        boxesPanel.add(copyPrevOffenseBoxes, "2,0");
        boxesPanel.add(clearBoxes, "4,0");
        
        add(addDefenceBox, "1,1, 3,1");
        add(addOffenseBox, "1,3, 3,3");
        add(nextFrame,"3,5");
        add(prevFrame,"1,5");
        add(m_xHotPoint, "1,7");
        add(m_yHotPoint, "3,7");
        add(addHotPoint, "1,9, 3,9");
        add(copyPrevHotPoint, "1,11, 3,11");
        add(m_centerToHotPoint, "1, 13, 3, 13");
        add(timePanel, "0,15, 4,15");
        add(boxesPanel, "1,17, 3,17");
	}
	
	public int getXHotPoint(){
		return Integer.parseInt(m_xHotPoint.getText());
	}
	
	public int getYHotPoint(){
		return Integer.parseInt(m_yHotPoint.getText());
	}
	
	public int getFrameTime(){
		return Integer.parseInt(m_frameTime.getText());
	}
	
	public void setHotPoint(Point p){
		m_xHotPoint.setText("" + p.x);
		m_yHotPoint.setText("" + p.y);
	}
	
	public void setFrameTime(int time){
		m_frameTime.setText("" + time);
	}
	
	public JCheckBox getCenterToHotPointComboBox(){
		return m_centerToHotPoint;
	}
}
