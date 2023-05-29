package main;

public class Main {
	//主类
	public static void main(String[] args) {
		GameFrame frame = new GameFrame();
		GamePanel gamePanel = new GamePanel(frame);
		frame.add(gamePanel);
		frame.setVisible(true);//设定显示
	}
}
