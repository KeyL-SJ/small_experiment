package main.plants;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import main.GamePanel;
import main.common.MusicPlayer;

public class Sun {
	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;
	private BufferedImage image = null;
	private GamePanel panel=null;
	private HashMap imageMap=null;
	private int key=1;
	//是否存活
	private boolean alive=true;
	//向下移动的量
	private boolean moveDownFlag = false;
	private int moveDown = 0;
	//正在向目标移动标示
	private boolean moveTarget = false;
	//向目标移动的x、y速度
	private double mx=0;
	private double my=0;
	
	private int downMax = 100;//向下移动的距离

	private int count=20;//收集一个得到的分数
	
	MusicPlayer musicPoints=null;
	MusicPlayer musicMoneyfalls=null;
	
	public Sun(int x,int y,int width,int height,GamePanel panel) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.panel=panel;
		this.imageMap=panel.sunImageMap;
		this.image=(BufferedImage)imageMap.get("("+key+")");
		
		//执行动画
		waggle();
		
		move();//默认向下移动
	}
	
	public void draw(Graphics g) {
		g.drawImage(image, x, y, width,height, null);
	}
	
	//摇晃动画
	void waggle() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(alive){
					changeImage();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	//更换图片
	void changeImage(){
		key++;
		if(key>22){
			key=1;
		}
		this.image=(BufferedImage)imageMap.get("("+key+")");
	}
	//移动
	void move(){//往下移动100
		moveDownFlag=true;
		new Thread(new Runnable() {
			int speed=4;
			@Override
			public void run() {
				while(moveDownFlag){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					moveDown+=speed;
					y+=speed;
					
					//阳光静止10秒
					if(moveDown>downMax){
						moveDownFlag=false;
						stop();
					}
				}
			
			}
		}).start();
	}
	
	//停止8秒
	void stop(){//
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(8000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//阳光消失
				
				clear();
			}
		}).start();
	}
	//阳光清除
	void clear(){
		this.alive=false;
		panel.suns.remove(this);
	}
	
	//被点击
	public void click(){
		musicPoints=new MusicPlayer("/music/points.wav");
		musicPoints.play();
		//向左上角移动
		int cx=20,
			cy=20;//收集点的X\Y坐标
		
		double angle = Math.atan2((y-cy), (x-cx));  //弧度 
		//计算出X\Y每一帧移动的距离
	    mx = Math.cos(angle)*20;
	    my = Math.sin(angle)*20;
		
		moveTarget = true ;
		moveDown=downMax+1;//设定不再向下运动
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(moveTarget){
					x-=mx;
					y-=my;
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					//停止移动，到达目标位置
					if(y<=20||x<=20){
					    musicMoneyfalls=new MusicPlayer("/music/moneyfalls.wav");
						musicMoneyfalls.play();
						moveTarget=false;
						panel.sunCount+=count;
						panel.cardCanUse();
						clear();
					}
				}
			}
		}).start();
	}
	
	//检查坐标是否图形范围内
	public boolean isPoint(int x,int y){
		//范围放大些,方便点击
		if(x>=this.x-5 && y>=this.y-5 && x<=this.x+this.width+5 && y <= this.y + this.height+5){
			return true;
		}
		return false;
	}
	
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public int getDownMax() {
		return downMax;
	}

	public void setDownMax(int downMax) {
		this.downMax = downMax;
	}
}
