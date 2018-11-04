import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class SpritePanel extends JPanel implements MouseListener, MouseMotionListener{

	
	private static final long serialVersionUID = -2911170291573842461L;

	private String m_picPath;
	
	private ArrayList<BufferedImage> m_picTab = new ArrayList<BufferedImage>();
	private int m_currentPic = 0;
	
	private HashMap<Integer,ArrayList<CollisionBox>> m_collisionMap = new HashMap<Integer,ArrayList<CollisionBox>>();
	private HashMap<Integer,Point> m_hotPointMap = new HashMap<Integer,Point>();
	private HashMap<Integer,Integer> m_frameTimeMap = new HashMap<Integer,Integer>();
	
	private boolean m_drawDefenceBox = false;
	private boolean m_drawOffenseBox = false;
	private boolean m_centerToHotPoint = false;
	
	private Point m_mouseStartLocation = new Point();
	private Point m_mouseCurrentLocation = new Point();
	
	private boolean m_isDrawingBoxes = false;
	
	public SpritePanel(){
		super();
		
		setBackground(Color.BLACK);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public int currentPic(){
		return m_currentPic;
	}
	
	public Point currentHotPoint(){
		return m_hotPointMap.get(m_currentPic);
	}
	
	public int currentFrameTime(){
		return m_frameTimeMap.get(m_currentPic);
	}
	
	public void loadPictures(String path) throws IOException{
		
		File f = new File(path);
		
		if(!f.isDirectory()) throw new IllegalArgumentException("le chemin ne désigne pas un dossier");
		
		m_picTab.clear();
		m_hotPointMap.clear();
		m_frameTimeMap.clear();
		
		m_picPath = path;
		
		boolean found = true;
		ArrayList<File> _pictures = new ArrayList<File>();
		
		for(int i = 1 ; found ; i++)
		{
			File pic = new File(path + "/" + i + ".png");
			if(!pic.exists()) found = false;
			else _pictures.add(pic);
		}
		
		int maxPicIndex = _pictures.size();
		
		int i = 0;
		
		for(i = 0 ; i < maxPicIndex ; i++){
			if(_pictures.get(i).getName().endsWith(".png"))
				m_picTab.add(ImageIO.read(_pictures.get(i)));
				m_hotPointMap.put(i, new Point());
				m_frameTimeMap.put(i, 0);
		}
		
		//load collisions & hot points
		for(int j = 1 ; j <= maxPicIndex ; j++){
			loadCollisions(j);
			loadHotPoint(j);
		}
		
		//load frames time
		loadFrameTime();
		
		repaint();
	}
	
	private void loadCollisions(int frameNb) throws IOException{
		
		m_collisionMap.clear();
		
		File f = new File(m_picPath + "/collisions.txt");
		if(f.exists()){
			
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			String s;
			boolean frameDefFound = false;
			
			while(!frameDefFound &&  (s = br.readLine()) != null){
				
				if(s.startsWith("Frame " + frameNb)){
					frameDefFound = true;
					while( ((s = br.readLine()) != null) && !s.startsWith("Frame")){
						
						if(s.startsWith("DEFENSE")) {
							StringTokenizer st = new StringTokenizer(s, " ");
							st.nextToken();
							String data = st.nextToken();
							st = new StringTokenizer(data, ",");
							
							int[] tab = new int[st.countTokens()];
							int i = 0;
							
							while(i < tab.length) {
								tab[i] = Integer.parseInt(st.nextToken());
								i++;
							}
							
							if(!m_collisionMap.containsKey(frameNb - 1)){
								m_collisionMap.put(frameNb - 1, new ArrayList<CollisionBox>());
							}
							CollisionBox cb = new CollisionBox(tab[0], tab[1], tab[2], tab[3], CollisionBox.CollisionType.DEFENSE);
							m_collisionMap.get(frameNb - 1).add(cb);
							
							System.out.println("Frame " + (frameNb - 1) + " DEFENSE BOX x: " + tab[0] + " y: " + tab[1] + " w: " + tab[2] + " h: " + tab[3]);
						}
						
						else if(s.startsWith("OFFENSE")) {
							StringTokenizer st = new StringTokenizer(s, " ");
							st.nextToken();
							String data = st.nextToken();
							st = new StringTokenizer(data, ",");
							
							int[] tab = new int[st.countTokens()];
							int i = 0;
							
							while(i < tab.length) {
								tab[i] = Integer.parseInt(st.nextToken());
								i++;
							}
							
							if(!m_collisionMap.containsKey(frameNb - 1)){
								m_collisionMap.put(frameNb - 1, new ArrayList<CollisionBox>());
							}
							CollisionBox cb = new CollisionBox(tab[0], tab[1], tab[2], tab[3], CollisionBox.CollisionType.OFFENSE);
							m_collisionMap.get(frameNb - 1).add(cb);
							
						}
					}
				}
			}
			
			br.close();
		}
	}
	
	private void loadHotPoint(int frameNb) throws IOException{
		
		File f = new File(m_picPath + "/collisions.txt");
		if(f.exists()){
			
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			String s;
			boolean frameDefFound = false;
			
			while(!frameDefFound &&  (s = br.readLine()) != null){
				
				if(s.startsWith("Frame " + frameNb)){
					frameDefFound = true;
					while( ((s = br.readLine()) != null) && !s.startsWith("Frame")){
						
						if(s.startsWith("hotPoint")){
							
							StringTokenizer st = new StringTokenizer(s, " ");
							int[] tab = new int[2];
							int j = 0;
							
							st.nextToken();
							
							while(j < tab.length && st.hasMoreTokens()){
								tab[j] = Integer.parseInt(st.nextToken());
								System.out.println(tab[j]);
								j++;
							}
							
							m_hotPointMap.put(frameNb - 1, new Point(tab[0], tab[1]));
						}
					}
				}
			}
			
			br.close();
		}
	}
	
	private void loadFrameTime() throws IOException{
		
		File f = new File(m_picPath + "/anim.txt");
		if(f.exists()){
			
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			String s;
			
			while((s = br.readLine()) != null){
				
				if(s.startsWith("delays=")){
		
					StringTokenizer st = new StringTokenizer(s, "=");
					st.nextToken();
					
					String delays = st.nextToken();
					StringTokenizer delaysTokenizer = new StringTokenizer(delays, ",");
					
					for(int i = 0 ; delaysTokenizer.hasMoreTokens() ; i++){
						 m_frameTimeMap.put(i, Integer.parseInt(delaysTokenizer.nextToken()));
					}
						
				}
			}
			
			br.close();
		}
	}
	public void savePictures() throws IOException{
		File f = new File(m_picPath + "/collisions.txt" );
		PrintWriter pw = new PrintWriter(f);
		
		//Set<Integer> s = m_hotPointMap.keySet();
		
		for(int i = 0 ; i < m_hotPointMap.size() ; ++i){
		//for(int i : s){
			
			pw.println("Frame " + (i+1));
			
			ArrayList<CollisionBox> cbTab = m_collisionMap.get(i);
			if(cbTab != null){
				for(CollisionBox cb : cbTab){
					pw.println(cb.toString());
				}
			}
				
			pw.println("hotPoint " + m_hotPointMap.get(i).x + " " + m_hotPointMap.get(i).y);
		}
		
		pw.close();
		
		saveAnimInfo();
	}
	
	
	private void saveAnimInfo() throws IOException{
		
		File f = new File(m_picPath + "/anim.txt" );
		PrintWriter pw = new PrintWriter(f);
		
		pw.println("sprites=" + m_picTab.size());
		
		pw.print("delays=");
		
		//Set<Integer> s = m_frameTimeMap.keySet();
		
		for(int i = 0 ; i < m_frameTimeMap.size() ; ++i){
		//for(int i : s){
			
			pw.print(m_frameTimeMap.get(i));
			if(i < m_picTab.size() - 1) pw.print(",");
		}
		
		pw.close();
	}
	
	public void prevPic(){
		m_currentPic--;
		if(m_currentPic < 0) m_currentPic++;
		
		repaint();
	}
	
	public void nextPic(){
		m_currentPic++;
		if(m_currentPic >= m_picTab.size()) m_currentPic--;
		
		repaint();
	}
	
	public void setFrameTime(int time){
		m_frameTimeMap.put(m_currentPic, time);
		repaint();
	}
	
	public void setHotPoint(Point hp){
		if(hp.x < 0) hp.x = 0;
		if(hp.x > m_picTab.get(m_currentPic).getWidth()) hp.x = m_picTab.get(m_currentPic).getWidth();
		if(hp.y < 0) hp.y = 0;
		if(hp.y > m_picTab.get(m_currentPic).getHeight()) hp.y = m_picTab.get(m_currentPic).getHeight();
		
		m_hotPointMap.put(m_currentPic, hp);
		repaint();
	}

	public void copyPreviousHotPoint(){
		if(m_currentPic == 0) return;
		
		m_hotPointMap.put(m_currentPic, m_hotPointMap.get(m_currentPic - 1));
		repaint();
	}
	
	public void copyPreviousBoxes(CollisionBox.CollisionType type) {
		if(m_currentPic == 0) return;
		
		m_collisionMap.put(m_currentPic, m_collisionMap.get(m_currentPic - 1));
		
		for(CollisionBox box : m_collisionMap.get(m_currentPic)) {
			if(box.getType() != type) {
				m_collisionMap.get(m_currentPic).remove(box);
			}
		}
		
		repaint();
	}
	
	public void clearBoxes() {
		m_collisionMap.get(m_currentPic).clear();
		
		repaint();
	}
	
	public void enableDefenceBoxDrawing(){
		m_drawDefenceBox = true;
		m_drawOffenseBox = false;
		repaint();
	}
	
	public void enableOffenseBoxDrawing(){
		m_drawDefenceBox = false;
		m_drawOffenseBox = true;
		repaint();
	}
	
	public void setCenterToHotPoint(boolean b){
		m_centerToHotPoint = b;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(!m_centerToHotPoint){
			
			//draw picture
			if(m_currentPic < m_picTab.size())
				g.drawImage(m_picTab.get(m_currentPic)
						, (this.getWidth() - m_picTab.get(m_currentPic).getWidth()) / 2
						, (this.getHeight() - m_picTab.get(m_currentPic).getHeight()) / 2
						, m_picTab.get(m_currentPic).getWidth(), m_picTab.get(m_currentPic).getHeight(), this);
			
			//draw collision boxes
			if(m_collisionMap.containsKey(m_currentPic)){
				ArrayList<CollisionBox> cbTab = m_collisionMap.get(m_currentPic);
				
				for(CollisionBox cb : cbTab){
					
					if(cb.getType() == CollisionBox.CollisionType.DEFENSE) g.setColor(Color.BLUE);
					else if(cb.getType() == CollisionBox.CollisionType.OFFENSE) g.setColor(Color.RED);
					else throw new RuntimeException("Une collision comporte un type illégal.");
					
					g.drawRect((this.getWidth() - m_picTab.get(m_currentPic).getWidth()) / 2 + cb.getX()
							, (this.getHeight() - m_picTab.get(m_currentPic).getHeight()) / 2 + cb.getY()
							, cb.getWidth()
							, cb.getHeight());
				}
			}
			
			//draw current box drawing
			if(m_isDrawingBoxes){
				if(m_drawDefenceBox) g.setColor(Color.BLUE);
				if(m_drawOffenseBox) g.setColor(Color.RED);
				
				g.drawRect(m_mouseStartLocation.x, m_mouseStartLocation.y, m_mouseCurrentLocation.x - m_mouseStartLocation.x, m_mouseCurrentLocation.y - m_mouseStartLocation.y);
			}
			
			//draw hot point
			if(m_hotPointMap.containsKey(m_currentPic)){
				g.setColor(Color.WHITE);
				int x = (this.getWidth() - m_picTab.get(m_currentPic).getWidth()) / 2 + m_hotPointMap.get(m_currentPic).x;
				int y = (this.getHeight() - m_picTab.get(m_currentPic).getHeight()) / 2 + m_hotPointMap.get(m_currentPic).y;
				
				g.drawLine(x - 2, y, x + 2, y);
				g.drawLine(x, y - 2, x, y + 2);
			}
			
		}
		else{
			
			int x = this.getWidth()  / 2;
			int y = this.getHeight() / 2;
			
			//draw picture
			if(m_currentPic < m_picTab.size())
				g.drawImage(m_picTab.get(m_currentPic)
						, x - m_hotPointMap.get(m_currentPic).x
						, y - m_hotPointMap.get(m_currentPic).y
						, m_picTab.get(m_currentPic).getWidth(), m_picTab.get(m_currentPic).getHeight(), this);
			
			//draw collision boxes
			if(m_collisionMap.containsKey(m_currentPic)){
				ArrayList<CollisionBox> cbTab = m_collisionMap.get(m_currentPic);
				
				for(CollisionBox cb : cbTab){
					
					if(cb.getType() == CollisionBox.CollisionType.DEFENSE) g.setColor(Color.BLUE);
					else if(cb.getType() == CollisionBox.CollisionType.OFFENSE) g.setColor(Color.RED);
					else throw new RuntimeException("Une collision comporte un type illégal.");
					
					g.drawRect((this.getWidth() - m_picTab.get(m_currentPic).getWidth()) / 2 - m_hotPointMap.get(m_currentPic).x + cb.getX()
							, (this.getHeight() - m_picTab.get(m_currentPic).getHeight()) / 2 - m_hotPointMap.get(m_currentPic).y + cb.getY()
							, cb.getWidth()
							, cb.getHeight());
				}
			}
			
			//draw hot point
			if(m_hotPointMap.containsKey(m_currentPic)){
				g.setColor(Color.WHITE);
				
				g.drawLine(x - 2, y, x + 2, y);
				g.drawLine(x, y - 2, x, y + 2);
			}
			
		}
		
		//draw information
		g.setColor(Color.WHITE);
		g.drawString(new String("Frame " + (m_currentPic + 1)), 5, 15);
		
		//hot point
		if(m_hotPointMap.containsKey(m_currentPic))
			g.drawString(new String("Hot Point | x : " + m_hotPointMap.get(m_currentPic).x + " y : " + m_hotPointMap.get(m_currentPic).y), 5, 30);
		
		//boxes drawing
		if(m_drawDefenceBox){
			g.setColor(Color.BLUE);
			g.drawString(new String("Defence Box Drawing "), getWidth() - 150, 15);
		}else if(m_drawOffenseBox){
			g.setColor(Color.RED);
			g.drawString(new String("Offense Box Drawing "), getWidth() - 150, 15);
		}
		
		//frame time
		if(m_frameTimeMap.size() > 0){
			g.setColor(Color.WHITE);
			g.drawString("Frame Time:" + m_frameTimeMap.get(m_currentPic), 5, 45);
		}
			
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		if( (e.getButton() == MouseEvent.BUTTON1) && (m_drawDefenceBox || m_drawOffenseBox) ){
			
				m_mouseStartLocation.x = e.getX();
				m_mouseStartLocation.y = e.getY();
				m_isDrawingBoxes = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		if(m_drawDefenceBox){
			CollisionBox cb = new CollisionBox(m_mouseStartLocation.x - (this.getWidth() - m_picTab.get(m_currentPic).getWidth()) / 2
					, m_mouseStartLocation.y - (this.getHeight() - m_picTab.get(m_currentPic).getHeight()) / 2
					, m_mouseCurrentLocation.x - m_mouseStartLocation.x
					, m_mouseCurrentLocation.y - m_mouseStartLocation.y
					, CollisionBox.CollisionType.DEFENSE);
			
			if(!m_collisionMap.containsKey(m_currentPic)){
				m_collisionMap.put(m_currentPic, new ArrayList<CollisionBox>());
			}
			m_collisionMap.get(m_currentPic).add(cb);
		}
		else if(m_drawOffenseBox){
			CollisionBox cb = new CollisionBox(m_mouseStartLocation.x - (this.getWidth() - m_picTab.get(m_currentPic).getWidth()) / 2
					, m_mouseStartLocation.y - (this.getHeight() - m_picTab.get(m_currentPic).getHeight()) / 2
					, m_mouseCurrentLocation.x - m_mouseStartLocation.x
					, m_mouseCurrentLocation.y - m_mouseStartLocation.y
					, CollisionBox.CollisionType.OFFENSE);
			
			if(!m_collisionMap.containsKey(m_currentPic)){
				m_collisionMap.put(m_currentPic, new ArrayList<CollisionBox>());
			}
			m_collisionMap.get(m_currentPic).add(cb);
		}
		
		
		if(m_drawDefenceBox || m_drawOffenseBox) m_isDrawingBoxes = false;
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
		if(m_drawDefenceBox || m_drawOffenseBox){
			m_mouseCurrentLocation.x = e.getX();
			m_mouseCurrentLocation.y = e.getY();
			repaint();
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		
	}
	
	public String getAnimationPath() {
		return m_picPath;
	}
}
