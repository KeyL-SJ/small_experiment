package main.plants;

import java.awt.Color;
import java.awt.Graphics;

import main.common.RandomColor;

public class PlantsRect {
	private int x=0;
	private int y=0;
	private int width=0;
	private int height=0;
	private int index=0;
	private Color color=null;
	private Plant plant=null;

	public PlantsRect(int x,int y,int width,int height,int index) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.index=index;
		//this.color = RandomColor.getRandomColor();;//随机颜色
		this.color = new Color(0,250,154, 0);//绘制成透明的颜色
	}
	
	public void draw(Graphics g) {
		Color oColor = g.getColor();
		g.setColor(color);
		g.fillRect(x, y, width, height);
		g.setColor(oColor);
	}
	//检查坐标是否图形范围内
	public boolean isPoint(int x,int y){
		if(x>this.x && y>this.y && x<this.x+this.width && y < this.y + this.height){
			return true;
		}
		return false;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
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

	public Plant getPlant() {
		return plant;
	}

	public void setPlant(Plant plant) {
		this.plant = plant;
	}
}
