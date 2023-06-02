package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.card.Card;

public class Main {
	//主类
	public static void main(String[] args) {
		GameFrame frame = new GameFrame();
		GamePanel panel = new GamePanel(frame);
		frame.add(panel);
		frame.setVisible(true);//设定显示
	}
}

