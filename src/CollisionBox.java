
public class CollisionBox {

	public enum CollisionType{
		DEFENSE,
		OFFENSE
	}
	
	private int m_width;
	private int m_height;
	private int m_x;
	private int m_y;
	private CollisionType m_type;
	
	public CollisionBox(){
		m_width = 0;
		m_height = 0;
		m_x = 0;
		m_y = 0;
		m_type = CollisionType.DEFENSE;
	}
	
	public CollisionBox(int x, int y, int w, int h, CollisionType type){
		m_width = w;
		if(m_width < 0) m_width = 0;
		m_height = h;
		if(m_height < 0) m_height = 0;
		m_x = x;
		m_y = y;
		m_type = type;
	}
	
	public int getX(){
		return m_x;
	}
	
	public int getY(){
		return m_y;
	}
	
	public int getWidth(){
		return m_width;
	}
	
	public int getHeight(){
		return m_height;
	}
	
	public CollisionType getType(){
		return m_type;
	}
	
	public String toString(){
		return m_type + " " + m_x + "," + m_y + "," + m_width + "," + m_height;
	}
	
}
