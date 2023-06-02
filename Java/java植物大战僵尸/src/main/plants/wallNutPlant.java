package main.plants;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sun.corba.se.spi.orbutil.fsm.Action;

import main.GamePanel;

public class wallNutPlant extends Plant {
	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;
	private int index=0;
	private int cost = 0;
	private BufferedImage image = null;
	private GamePanel panel=null;
	private HashMap imageMap=null;
	private int key=1;
	private boolean alive=false;
	private int maxHp=30;
	private int hp=30;
	private List zombies = new ArrayList();
	private int action = 1;
	private PlantsRect plantsRect=null;
	
	public wallNutPlant(int x,int y,int width,int height,GamePanel panel) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.panel=panel;
		this.imageMap=panel.wallNutHashMap1;
		this.image=(BufferedImage)imageMap.get("("+key+")");
	}
	@Override
	public void draw(Graphics g) {
		g.drawImage(image, x, y, width,height, null);
	}

	@Override
	void shoot() {
	}
	private void changeActionOrNot() {
		if(maxHp/3*2<hp){//如果当前血量小于总血量2/3 则切换图片
			this.imageMap=panel.wallNutHashMap2;
			action=2;
			key=1;
		}else if(maxHp/3<hp){//如果当前血量小于总血量1/3 则切换图片
			this.imageMap=panel.wallNutHashMap3;
			action=3;
			key=1;
		}
	}
	
	//摇晃动画
	@Override
	void waggle() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(alive){
					changeImage();
					try {
						Thread.sleep(110);
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
		//根据action来处理 action代表不同的状态
		if(action==1){
			if(key>16){
				key=1;
			}
		}else if(action==2){
			if(key>11){
				key=1;
			}
		}else if(action==3){
			if(key>15){
				key=1;
			}
		}
	
		this.image=(BufferedImage)imageMap.get("("+key+")");
		width = this.image.getWidth();
		height = this.image.getHeight();
	}
	
	//种下植物
	@Override
	public void plant(PlantsRect rect) {
		this.x=rect.getX()+10;
		this.y=rect.getY()+12;
		this.index=rect.getIndex();
		this.alive=true;
		plantsRect = rect;
		//摇晃的动画
		waggle();
	}
	
	@Override
	public void clear() {
		alive=false;
		//植物占的位置去除
		if(plantsRect!=null){
			plantsRect.setPlant(null);
			plantsRect=null;	
		}
		//移除植物
		panel.plants.remove(this);
	}
	//添加僵尸对象，等植物吃完要通知每一个对象
	@Override
	public void addZombie(Zombie z) {
		zombies.add(z);
	}
	//通知所有僵尸对象
	@Override
	public void noteZombie() {
		Zombie zombie = null;
		//执行通知
		for (int i = 0; i < zombies.size(); i++) {
			zombie = (Zombie)zombies.get(i);
			zombie.noteZombie();
		}
		//通知完清除这个对象
		zombies.clear();
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
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
		//判断是否要切换图片
		changeActionOrNot();
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

}
