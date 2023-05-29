package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import common.ImageValue;

public class Qizi {

	private int x = 0;
	private int y = 0;
	private int r = 36;
	private GamePanel panel = null;
	private Color color = null;
	private int type = 1;// 棋子类型 1：白棋 2：黑棋
	private boolean last=false;//标示最后下的那个棋子

	public Qizi(int x, int y, int type, GamePanel panel) {
		this.x = x;
		this.y = y;
		this.panel = panel;
		this.type=type;
	}

	// 绘制
	void draw(Graphics g) {
		Color oColor = g.getColor();
		if (type == 1) {// 白色
			g.drawImage(ImageValue.whiteQiziImage, x - r / 2, y - r / 2,r,r, null);
		} else {// 黑色
			g.drawImage(ImageValue.blackQiziImage, x - r / 2, y - r / 2,r,r, null);
		}

		if (color != null) {// 用完设置回去颜色
			g.setColor(oColor);
		}
		
		if(last){
			Graphics2D g2 = (Graphics2D)g;  //g是Graphics对象
			g2.setStroke(new BasicStroke(1.6f));
			g2.setColor(new Color(255,0,0));
			g2.drawRect(x-3, y-3, 6, 6);
			g2.setColor(oColor);
		}
	}
	
	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
