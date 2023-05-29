package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

//指示器类
public class Pointer {
	private int i=0;//二维下标i
	private int j=0;//二维下标j
	private int x=0;//坐标X
	private int y=0;//坐标Y
	private GamePanel panel=null;
	private Color color=null;
	private int h=40;//指示的大小
	private boolean isShow=false;//是否展示
	private int qizi = 0 ;//棋子类型 0：无  1：白棋  2：黑棋
	
	public Pointer(int x,int y,int i,int j,Color color,GamePanel panel){
		this.x=x;
		this.y=y;
		this.i=i;
		this.j=j;
		this.panel=panel;
		this.color=color;
	}
	//绘制
	void draw(Graphics g){
		Color oColor = g.getColor();
		if(color!=null){
			g.setColor(color);
		}
		//g.drawRect(x-h/2, y-h/2, h, h);
		
		if(isShow){
			//绘制指示器
			drawPointer(g);
		}
		
		if(color!=null){//用完设置回去颜色
			g.setColor(oColor);
		}
	}
	
	private void drawPointer(Graphics g) {
		
		Graphics2D g2 = (Graphics2D)g;  //g是Graphics对象
		g2.setStroke(new BasicStroke(2.0f));

		/*
		 * 1.先计算4个顶点
		 * 2.依次从每个顶点绘制横竖两条线
		 */
		
		//左上角
		int x1 = x-h/2;
		int y1 = y-h/2;
		//向右画线
		int x2 = x1+1*h/4;
		int y2 = y1;
		g2.drawLine(x1, y1, x2, y2);
		
		//向下画线
		x2 = x1;
		y2 = y1+1*h/4;
		g2.drawLine(x1, y1, x2, y2);
		
		//右上角
		x1 = x+h/2;
		y1 = y-h/2;
		//向左画线
		x2 = x1-1*h/4;
		y2 = y1;
		g2.drawLine(x1, y1, x2, y2);
		
		//向下画线
		x2 = x1;
		y2 = y1+1*h/4;
		g2.drawLine(x1, y1, x2, y2);
		
		//右下角
		x1 = x+h/2;
		y1 = y+h/2;
		//向左画线
		x2 = x1-1*h/4;
		y2 = y1;
		g2.drawLine(x1, y1, x2, y2);
		
		//向上画线
		x2 = x1;
		y2 = y1-1*h/4;
		g2.drawLine(x1, y1, x2, y2);
		
		//左下角
		x1 = x-h/2;
		y1 = y+h/2;
		//向右画线
		x2 = x1+1*h/4;
		y2 = y1;
		g2.drawLine(x1, y1, x2, y2);
		
		//向上画线
		x2 = x1;
		y2 = y1-1*h/4;
		g2.drawLine(x1, y1, x2, y2);
	}
	
	//判断鼠标是否在指针范围内
	boolean isPoint(int x,int y){
		//大于左上角，小于右下角的坐标则肯定在范围内
		if(x>this.x-h/2 && y >this.y-h/2
			&& x<this.x+h/2 && y <this.y+h/2){
			return  true;
		}
		return false;
	}
	
	public boolean isShow() {
		return isShow;
	}
	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}
	public int getQizi() {
		return qizi;
	}
	public void setQizi(int qizi) {
		this.qizi = qizi;
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
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public int getJ() {
		return j;
	}
	public void setJ(int j) {
		this.j = j;
	}
}