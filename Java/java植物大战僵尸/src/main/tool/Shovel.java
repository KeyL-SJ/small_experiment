package main.tool;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Shovel {

	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;
	private BufferedImage image = null;
	private GamePanel panel=null;

	public Shovel(int x, int y, int width, int height, BufferedImage image,GamePanel panel) {
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

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
