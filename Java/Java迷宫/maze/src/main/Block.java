package main;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
/*
 * 迷宫单元类
 */
public class Block {
	private GamePanel panel = null;
	private int i=0;//二维数组的下标i
	private int j=0;//二维数组的下标j
	private int h=0;//宽高
	private int start=6;//偏移像素
	//4个顶点坐标
	private int x1=0;//x1坐标
	private int y1=0;//y1坐标
	private int x2=0;//x2坐标
	private int y2=0;//y2坐标
	private int x3=0;//x3坐标
	private int y3=0;//y3坐标
	private int x4=0;//x4坐标
	private int y4=0;//y4坐标
	//上下左右4个墙是否显示,true：显示，false：隐藏
	boolean[] walls=new boolean[4];
	
	private boolean visited=false;//是否被访问
	//构造
	public Block(int i,int j,int h,GamePanel panel){
		this.i=i;
		this.j=j;
		this.h=h;
		this.panel=panel;
		//计算4个顶点的坐标
		init();
	}
	
	//计算4个顶点的坐标
	private void init() {
		//i代表行 j代表列
		//左上角坐标
		this.x1=start+j*h;
		this.y1=start+i*h;
		//右上角坐标
		this.x2=start+(j+1)*h;
		this.y2=start+i*h;
		//右下角坐标
		this.x3=start+(j+1)*h;
		this.y3=start+(i+1)*h;
		//左下角坐标
		this.x4=start+j*h;
		this.y4=start+(i+1)*h;	
		//默认上下左右4个墙都显示
		walls[0]=true;
		walls[1]=true;
		walls[2]=true;
		walls[3]=true;
	}

	//绘制指示器的方法
	public void draw(Graphics g) {
		//绘制迷宫块
		drawBlock(g);
	}
	//绘制迷宫块
	private void drawBlock(Graphics g) {
		//判断上、右、下、左 的墙，true的话墙就会有，否则墙就没有
		boolean top    = walls[0];
		boolean right  = walls[1];
		boolean bottom = walls[2];
		boolean left   = walls[3];
		if(top){//绘制上墙
			g.drawLine(x1, y1, x2, y2);
		}
		if(right){//绘制右墙
			g.drawLine(x2, y2, x3, y3);	
		}
		if(bottom){//绘制下墙
			g.drawLine(x3, y3, x4, y4);	
		}
		if(left){//绘制左墙
			g.drawLine(x4, y4, x1, y1);	
		}
	}
	
	//查找当前单元是否有未被访问的邻居单元
	public List<Block> findNeighbors() {
		//邻居分为上下左右
		List<Block> res= new ArrayList<Block>();//返回的数组
		
		Block top    = this.getNeighbor(0,false);
		Block right  = this.getNeighbor(1,false);
		Block bottom = this.getNeighbor(2,false);
		Block left   = this.getNeighbor(3,false);
		
		if(top!=null){
			res.add(top);
		}
		if(right!=null){
			res.add(right);
		}
		if(bottom!=null){
			res.add(bottom);
		}
		if(left!=null){
			res.add(left);
		}
		return res;//返回邻居数组
	}
	//根据方向，获得邻居
	public Block getNeighbor(int type,boolean lose_visited) {
		Block neighbor;
		int ti=0,tj=0;
		if(type==0){
			ti = this.i-1;
			tj = this.j;
		}else if(type==1){
			ti = this.i;
			tj = this.j+1;
		}else if(type==2){
			ti = this.i+1;
			tj = this.j;
		}else if(type==3){
			ti = this.i;
			tj = this.j-1;
		}
		
		Block[][] blocks = panel.blocks;
		
		if(ti<0 || tj<0 || ti>=panel.ROWS || tj>=panel.COLS){//超出边界了
			neighbor = null;
		}else{
			//首先找到这个邻居
			neighbor = blocks[ti][tj];
			//判断是否被访问，如果被访问了返回null
			if(neighbor.visited && !lose_visited){//lose_visited等于true表示忽略访问
				neighbor = null;
			}
		}
		return neighbor;
	}
	
	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
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