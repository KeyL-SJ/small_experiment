package main.plants;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import main.GamePanel;
import main.common.MusicPlayer;

public class Wandou {
	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;
	private int index=0;
	private BufferedImage image = null;
	private GamePanel panel=null;
	private HashMap imageMap=null;
	//是否存活
	private boolean alive=true;
	//移动的量
	private int speed=10;
	private boolean fade=false;

	MusicPlayer musicHit= null;
	
	public Wandou(int x,int y,int width,int height,GamePanel panel,int index) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.index=index;
		this.panel=panel;
		this.imageMap=panel.imageMap;
		this.image=(BufferedImage)imageMap.get("6");
		
		move();//默认向右移动
	}
	
	public void draw(Graphics g) {
		g.drawImage(image, x, y, width,height, null);
	}
	
	//移动
	void move(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(alive){
					x+=speed;
					//超过范围，豌豆消失
					if(x>=panel.gameWidth){
						clear();
					}
					
					if(alive){
						//判断击中
						hit();
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
	//击中
	private void hit() {
		//因为豌豆是向右飞行的，所以只需判断豌豆的 x_width  大于僵尸的x 就表示击中
		List zombies = panel.zombies;
		Zombie zombie=null;
		for (int i = 0; i < zombies.size(); i++) {
			zombie = (Zombie)zombies.get(i);
			if(zombie==null) continue;
			if(zombie.isAlive()){//僵尸存活状态才处理
				if(x+width>zombie.getX() && index==zombie.getIndex()){//同一行，且子弹的x+width大于僵尸的X坐标，判断我击中
					//僵尸掉血
					 hitZombie(zombie); 
					 break;
				}
			}
		}
	}
	
	
	
	private void hitZombie(Zombie zombie) {
		musicHit=new MusicPlayer("/music/hit.wav");
		musicHit.play();
		//子弹爆炸
		boom();
		
		//僵尸血量减少
		int hp = zombie.getHp();
		hp--;

		zombie.setHp(hp);
		if(hp==0){
			//执行僵尸死亡动画
			zombie.dead();
			zombie=null;
			panel.curCount+=10;
			
			/*if(panel.curCount>=panel.winCount){
				panel.gameWin();
			}*/
		}
	}
	//爆炸
	private void boom() {
		this.alive=false;
		this.image=(BufferedImage)imageMap.get("7");
		width=image.getWidth();
		height=image.getHeight();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				fade=true;
				//子弹消失
				clear();
			}
		}).start();
	}

	//清除豌豆
	public void clear(){
		this.alive=false;
		panel.wandous.remove(this);
	}
	
	//检查坐标是否图形范围内
	public boolean isPoint(int x,int y){
		if(x>this.x && y>this.y && x<this.x+this.width && y < this.y + this.height){
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
	public boolean isFade() {
		return fade;
	}

	public void setFade(boolean fade) {
		this.fade = fade;
	}
}
