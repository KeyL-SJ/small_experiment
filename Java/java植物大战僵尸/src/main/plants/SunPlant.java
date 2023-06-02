package main.plants;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.GamePanel;

public class SunPlant extends Plant {
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
	private int hp=6;
	private List zombies = new ArrayList();
	private PlantsRect plantsRect=null;
	
	public SunPlant(int x,int y,int width,int height,GamePanel panel) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.panel=panel;
		this.imageMap=panel.sunPlantImageMap;
		this.image=(BufferedImage)imageMap.get("("+key+")");
	}
	@Override
	public void draw(Graphics g) {
		g.drawImage(image, x, y, width,height, null);
	}

	@Override
	void shoot() {
		//Sun
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(alive){
					try {
						//每6秒发射一个阳光
						Thread.sleep(6000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(alive){//防止被吃了还能发射一次阳光
						createSun();
					}
				}
			}

			private void createSun() {
				Sun sun = new Sun(x+width/2-22, y-44, 45, 44, panel);
				panel.suns.add(sun);
			}
		}).start();
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
		if(key>18){
			key=1;
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
		//植物摇晃的动画
		waggle();
		
		shoot();//定时发射阳光
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
