package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.sun.org.apache.xml.internal.security.Init;
import common.MusicPlayer;
//敌机
public class EnemyPlane extends Plane {

	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;
	private BufferedImage image = null;
	private GamePanel panel=null;
	private HashMap imageMap=null;
	private boolean alive=true;
	private boolean canMove=false;
	private int speed=5;
	private int key=1;
	private int index = 0;
	private HashMap boomImageMap = null;
	private int count=10;
	
	public EnemyPlane(GamePanel panel) {
		this.panel=panel;
		this.imageMap=panel.imageMap;
		index = new Random().nextInt(4)+1;
		
		this.image=(BufferedImage)imageMap.get("enemy"+index);
		
		boomImageMap = (HashMap)panel.enemyListMap.get(index-1);
		
		init();
		
		move();
	}
	//敌机初始化
	private void init() {
		//取到飞机的宽高
		height = this.image.getHeight();
		width = this.image.getWidth();
		//飞机从窗口外飞入
		y = - height;
		//随机x 
		x = new Random().nextInt(520-width);
	}
	//绘制
	public void draw(Graphics g) {
		g.drawImage(image, x, y, width,height, null);
	}
	
	//清除本身
	@Override
	public void clear() {
		alive=false;
		panel.enemyList.remove(this);
	}

	//移动
	@Override
	public void move() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(alive){
					y+=speed;
					hitMyPlane();
					
					if(y>panel.getHeight()){
						clear();
					}
					
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	
	}
	
	//飞机爆炸
	@Override
	public void boom() {
		new MusicPlayer("/music/boom.wav").play();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(alive){
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
			clear();//清除飞机
			return;
		}
		image=(BufferedImage)boomImageMap.get("enemy"+index+"boom"+key);
		if(image!=null){
			width = image.getWidth();
			height = image.getHeight();
		}
		
	}
	
	//判断碰撞我机
	protected void hitMyPlane() {
		if(!alive) return ;
		
		MyPlane myPlane = panel.myPlane;
		if(myPlane==null){
			return ;
		}
		if(myPlane.isHitFlag()){//如果飞机已经碰撞了，则不能计算再次
			return ;
		}
		if(isPoint(myPlane)){
			myPlane.setHitFlag(true);
			//当前敌机爆炸
			boom();
			
			//我飞机爆炸
			myPlane.boom();
		}
	}
	
	
	//判断飞机与子弹是否碰撞
	private boolean isPoint(MyPlane plane) {
		/*
		 * 
		 * 两种情况
		 * 1.需要判断敌机的4个点是否在飞机范围内，如果有则表示碰撞了
		 * 2.如果步骤1不成立，则反过来，判断我机的4个点是否在敌机的范围内，如果是标志碰撞了
		*/
		
		//方式1
		
		//左上角
		int x1 = x;
		int y1 = y;
		//右上角
		int x2 = x+width;
		int y2 = y;
		//右下角
		int x3 = x+width;
		int y3 = y+height;
		//左下角
		int x4 = x;
		int y4 = y+height;
		//只要有一个点在范围内，则判断为碰撞
		if(comparePointMyPlane(x1,y1,plane)|| comparePointMyPlane(x2,y2,plane)||comparePointMyPlane(x3,y3,plane)||comparePointMyPlane(x4,y4,plane) ){
			return true;
		}
		
		//方式1没成立则用方式2判断
		
		//方式2
		x1 = plane.getX();
		y1 = plane.getY();
		//右上角
		x2 = plane.getX()+plane.getWidth();
		y2 = plane.getY();
		//右下角
		x3 = plane.getX()+plane.getWidth();
		y3 =plane.getY()+plane.getHeight();
		//左下角
		x4 = plane.getX();
		y4 = plane.getY()+plane.getHeight();
		if(comparePoint(x1,y1)|| comparePoint(x2,y2)||comparePoint(x3,y3)||comparePoint(x4,y4) ){
			return true;
		}
		return false;
	}
	//用敌机的坐标来判断
	private boolean comparePointMyPlane(int x,int y,MyPlane plane){
		//大于左上角，小于右下角的坐标则肯定在范围内
		if(x>plane.getX() && y >plane.getY()
			&& x<plane.getX()+plane.getWidth() && y <plane.getY()+plane.getHeight()	){
			return  true;
		}
		return false;
	}
	//用我机的坐标来判断
	private boolean comparePoint(int x,int y){
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
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
