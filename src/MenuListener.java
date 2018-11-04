import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class MenuListener implements ActionListener, ItemListener {

	private SpritePanel m_sp;
	private MenuPanel m_mp;
	
	public MenuListener(MenuPanel mp, SpritePanel sp){
		m_mp = mp;
		m_sp = sp;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("Add Defence Box")){
			
			m_sp.enableDefenceBoxDrawing();
			
		}else if(e.getActionCommand().equals("Add Offense Box")){
			
			m_sp.enableOffenseBoxDrawing();
			
		}else if(e.getActionCommand().equals("Next Frame")){
			
			m_sp.nextPic();
			m_mp.setHotPoint(m_sp.currentHotPoint());
			m_mp.setFrameTime(m_sp.currentFrameTime());
			
		}
		else if(e.getActionCommand().equals("Prev. Frame")){
			
			m_sp.prevPic();
			m_mp.setHotPoint(m_sp.currentHotPoint());
			m_mp.setFrameTime(m_sp.currentFrameTime());
		}
		else if(e.getActionCommand().equals("Add Hot Point")){
			m_sp.setHotPoint(new Point(m_mp.getXHotPoint(), m_mp.getYHotPoint()));
			
		}
		else if(e.getActionCommand().equals("Copy Prev. Hot Point")){
			m_sp.copyPreviousHotPoint();
			
		}
		else if(e.getActionCommand().equals("Set Time")){
			m_sp.setFrameTime(m_mp.getFrameTime());
			
		}
		else if(e.getActionCommand().equals("Prev. DB")){
			m_sp.copyPreviousBoxes(CollisionBox.CollisionType.DEFENSE);
		}
		else if(e.getActionCommand().equals("Prev. OB")){
			m_sp.copyPreviousBoxes(CollisionBox.CollisionType.OFFENSE);
		}
		else if(e.getActionCommand().equals("Clear Boxes")){
			m_sp.clearBoxes();
		}

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		
		Object source = e.getItemSelectable();
		
		if(source == m_mp.getCenterToHotPointComboBox()){
			if(m_mp.getCenterToHotPointComboBox().isSelected()){
				m_sp.setCenterToHotPoint(true);
			}
			else if(!m_mp.getCenterToHotPointComboBox().isSelected()){
				m_sp.setCenterToHotPoint(false);
			}
			
			m_sp.repaint();
		}
		
	}

}
