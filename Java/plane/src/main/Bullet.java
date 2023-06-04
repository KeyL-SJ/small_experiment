package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

public class Bullet {

	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;
	private BufferedImage image = null;
	private GamePanel panel=null;
	private HashMap imageMap=null;
	private boolean alive=true;

	private boolean canMove=false;
	private int speed=20;
	
	public Bullet(int x,int y,int width,int height,GamePanel panel) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.panel=panel;
		this.imageMap=panel.imageMap;
		this.image=(BufferedImage)imageMap.get("bullet");
		
		move();
	}
	
	public void draw(Graphics g) {
		g.drawImage(image, x, y, width,height, null);
	}
	
	public void clear() {
		alive=false;
		panel.bulletList.remove(this);
	}
	//移动
	void move(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(alive && !panel.nextEnd  && !panel.nextWin){
					y-=speed;
					if(y<=0){
						clear();
					}
					hitEnemy();
					
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	
	//判断击中敌机
	protected void hitEnemy() {
		EnemyPlane enemyPlane=null;
		List enemys = panel.enemyList;
		for (int i = 0; i < enemys.size(); i++) {
			try {
				enemyPlane = (EnemyPlane)enemys.get(i);
			} catch (Exception e) {
			}
			if(enemyPlane==null) continue;
			if(this.isPoint(enemyPlane)){
				
				panel.curCount+=enemyPlane.getCount();
				//删除当前子弹
				clear();
				
				//飞机爆炸
				enemyPlane.boom();
				
				if(panel.curCount>=panel.totalCount){
					panel.myPlane.setCanMove(false);
					panel.gameWin();
				}
			}
		}
	}

	//判断飞机与子弹是否碰撞
	private boolean isPoint(EnemyPlane plane) {
		//因为子弹比飞机小，所以只需要判断子弹的4个点是否在飞机范围内，如果有则表示碰撞了
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
		if(comparePoint(x1,y1,plane)|| comparePoint(x2,y2,plane)||comparePoint(x3,y3,plane)||comparePoint(x4,y4,plane) ){
			return true;
		}
		return false;
	}
	
	private boolean comparePoint(int x,int y,EnemyPlane plane){
		//大于左上角，小于右下角的坐标则肯定在范围内
		if(x>plane.getX() && y >plane.getY()
			&& x<plane.getX()+plane.getWidth() && y <plane.getY()+plane.getHeight()	){
			return  true;
		}
		return false;
	}
	
	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
}
