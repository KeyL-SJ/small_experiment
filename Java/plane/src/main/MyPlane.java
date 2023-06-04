package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import common.MusicPlayer;

public class MyPlane {

	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;
	private BufferedImage image = null;
	private GamePanel panel=null;
	private HashMap imageMap=null;
	private boolean alive=true;
	private boolean canMove=false;
	private int key=1;
	private HashMap boomImageMap=null;
	private boolean hitFlag=false;//正在碰撞
	
	public MyPlane(int x,int y,int width,int height,GamePanel panel) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.panel=panel;
		this.imageMap=panel.imageMap;
		this.image=(BufferedImage)imageMap.get("myplane1");
		this.boomImageMap=panel.mypalneBoomImageMap;
		
		shoot();
	}
	//绘制
	public void draw(Graphics g) {
		g.drawImage(image, x, y, width,height, null);
	}
	public void clear() {
		alive=false;
		panel.myPlane=null;
	}
	
	//飞机跟随鼠标移动
	public void move(int x,int y) {
		if(hitFlag) return ;
		
		//判断范围，当横向移动在窗口范围内
		if(x-width/2>=0 && x<=panel.getWidth()-width/2){
			this.x=x-width/2;
		}
		//判断范围，当纵向移动在窗口范围内
		if(y-height/2>=0 && y<=panel.getHeight()-height/2){
			this.y=y-height/2;
		}
	}
	//射击
	void shoot() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(alive && !panel.nextEnd  && !panel.nextWin){
					//创建子弹
					createBullet();
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			private void createBullet() {
				Bullet bullet = new Bullet(x+width/2-10, y, 20, 30, panel);
				panel.bulletList.add(bullet);
				new MusicPlayer("/music/shoot.wav").play();
			}
		}).start();
	}

	//飞机爆炸
	public void boom() {
		new MusicPlayer("/music/boom.wav").play();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(alive && panel.startFlag){
					changeImage();
					try {
						Thread.sleep(80);
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
		if(key>boomImageMap.size()){
			canMove=false;
			clear();//清除飞机
			panel.gameOver();
			return;
		}
		this.image=(BufferedImage)boomImageMap.get("myplane1boom"+key);
		width = image.getWidth();
		height = image.getHeight();
	}
	
	//判断鼠标是否在飞机范围内，在才可以移动
	boolean isPoint(int x,int y){
		//大于左上角，小于右下角的坐标则肯定在范围内
		if(x>this.x && y >this.y
			&& x<this.x+this.width && y <this.y+this.height){
			return  true;
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
	public boolean isHitFlag() {
		return hitFlag;
	}
	public void setHitFlag(boolean hitFlag) {
		this.hitFlag = hitFlag;
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	public boolean isCanMove() {
		return canMove;
	}
	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}
}
