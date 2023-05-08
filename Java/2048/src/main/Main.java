package main;



//Main类
public class Main {

	public static void main(String[] args) {
		GameFrame frame = new GameFrame();
		GamePanel panel = new GamePanel(frame);
		frame.add(panel);
		frame.setVisible(true);
	}
}
