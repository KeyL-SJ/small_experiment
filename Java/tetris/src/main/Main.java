package main;


public class Main {
	//主类
	public static void main(String[] args) {
		GameFrame frame = new GameFrame();
		BackPanel panel = new BackPanel(frame);
		frame.add(panel);
		GamePanel gamePanel = new GamePanel(frame);
		panel.setGamePanel(gamePanel);
		frame.add(gamePanel);
		frame.setVisible(true);//设定显示
	}
}
