package main.tool;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class ShovelCard {

	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;
	private BufferedImage image = null;
	private GamePanel panel=null;

	public ShovelCard(int x, int y, int width, int height, BufferedImage image,GamePanel panel) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.image = image;
		this.panel=panel;
	}

	public void draw(Graphics g) {
		g.drawImage(image, x, y, width, height, null);
	}
	
	//检查坐标是否图形范围内
	public boolean isPoint(int x,int y){
		if(x>this.x && y>this.y && x<this.x+this.width && y < this.y + this.height){
			return true;
		}
		return false;
	}

	public Shovel createShovel(int x2, int y2) {
		BufferedImage image = (BufferedImage)panel.imageMap.get("11");
		Shovel shovel = new Shovel(x2-40, y2-40, 80, 80, image, panel);
		return shovel;
	}

}
