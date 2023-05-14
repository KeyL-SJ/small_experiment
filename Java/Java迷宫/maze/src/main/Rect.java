package main;

import java.awt.Color;
import java.awt.Graphics;

//开始结束方块
public class Rect {
	private int i=0;//二维数组的下标i
	private int j=0;//二维数组的下标j
	private int x=0;//x坐标
	private int y=0;//y坐标
	private int h=0;//宽高
	private int start=6;//偏移像素
	private String type="";//start end 
	
	public Rect(int i,int j,int h,String type){
		this.i=i;
		this.j=j;
		this.h=h;
		this.type=type;
		
	}
	//初始化
	private void init() {
		this.x=start+j*h+2;
		this.y=start+i*h+2;
	}
	//绘制
	void draw(Graphics g){
		//计算x、y坐标
		init();
		
		Color oColor = g.getColor();
		if("start".equals(type)){//红色
			g.setColor(Color.red);
		}else{
			g.setColor(Color.blue);
		}
		g.fillRect(x, y, h-3, h-3);
		g.setColor(oColor);
	}
	
	//移动
	public void move(int type, Block[][] blocks,GamePanel panel) {
		//根据当前start方形，获得对应的迷宫单元
		Block cur = blocks[this.i][this.j];
		
		boolean wall = cur.walls[type];//得到对应的那面墙	
		if(!wall){
			//得到移动方块对应的单元
			Block next = cur.getNeighbor(type,true);
			if(next!=null){
				this.i = next.getI();
				this.j = next.getJ();
				panel.repaint();
				//判断如果i,j等于终点的，则表示获得成功
				if(this.i==panel.end.i && this.j==panel.end.j){
					panel.gameWin();
				}
			}
		}
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
