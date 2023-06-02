package main.plants;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import sun.awt.PlatformFont;

import main.GamePanel;
import main.common.MusicPlayer;

public class GeneralZombie extends Zombie{

	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;
	private BufferedImage image = null;
	private GamePanel panel=null;
	private HashMap zombieMoveHashMap=null;
	private HashMap zombieEatHashMap=null;
	private HashMap zombieDeadHashMap=null;
	private int key=1;
	private boolean alive=false;
	private boolean moveFlag=true;
	private boolean eatFlag=false;
	private boolean deadFlag=false;

	private int speed=1;
	
	private int action = 1;//1 移动，2 吃  3 死亡
	private int index=0 ;//下标
	private int hp=0 ;//血量
	private Plant eatPlant=null;//正在吃的植物
	
	MusicPlayer musicEat=null;
	MusicPlayer musicDead=null;
	
	public GeneralZombie(int x,int y,int width,int height,GamePanel panel) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.panel=panel;
		this.zombieMoveHashMap=panel.zombieMoveHashMap;
		this.zombieEatHashMap=panel.zombieEatHashMap;
		this.zombieDeadHashMap=panel.zombieDeadHashMap;
		this.image=(BufferedImage)zombieMoveHashMap.get("("+key+")");
		
		alive = true;
		move();
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(image, x, y, width,height, null);
	}
	
	@Override
	void dead() {
		musicDead=new MusicPlayer("/music/dead.wav");
		musicDead.play();
		
		alive=false;
		
		//moveFlag =false;//停止移动动画
		
		action=3;//死亡动作
		
		key=1;//key要重置
		
		deadFlag = true;
		
		//僵尸死亡动画
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(deadFlag){
					changeImage();//这个里面会处理僵尸消失
					try {
						Thread.sleep(80);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(musicDead!=null){
					musicDead.stop();
				}
			}
		}).start();
	}

	@Override
	void eat() {
		//因为僵尸是向左移动的，判断僵尸的x 小于植物的 x+width 就表示要吃（同时还需要判断僵尸的屁股要大于植物的x，否则植物放到僵尸后面也被吃）
		List plants = panel.plants;
		Plant plant=null;
		for (int i = 0; i < plants.size(); i++) {
			plant = (Plant)plants.get(i);
			if(x<plant.getX()+plant.getWidth() && index==plant.getIndex()
				&& x+width >plant.getX()+plant.getWidth()/2){//走过了植物的一半就不让吃了
				//吃
				if(eatPlant!=null){
					continue;
				}
				if(action==3) continue ;
				
				eatPlant=plant;
				doEat(); 
				break;
			}
		}
	
	}
	
	void doEat(){
		if(eatPlant==null) return ;
		if(action==3) return ;
		
		//将僵尸对象添加到植物中，存储为一个list，当植物被吃完后，需要主动通知每一个僵尸对象，否则会发生植物吃完，仍然有僵尸执行吃的延时动作
		eatPlant.addZombie(this);
		
		action=2;
		key=1;
		moveFlag=false;
		eatFlag=true;
		//僵尸吃动画
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(alive && eatFlag){
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
	
	private void doEatPlant() {
		if(eatPlant==null) return ;
		//每一次执行线程，都让植物掉血
		int hp = eatPlant.getHp();
		hp--;
		eatPlant.setHp(hp);
		musicEat=new MusicPlayer("/music/eat.wav");
		musicEat.play();
		
		if(hp==0){//植物消失，通知僵尸恢复行走
			eatPlant.noteZombie();
		}
		
	}
	//通知僵尸恢复行走
	@Override
	void noteZombie() {
		if(moveFlag){//防止重复通知
			return ;
		}
		if(action==3) return ;
		
		if(eatPlant!=null){//把正在吃的植物对象清空
			eatPlant.clear();
			eatPlant=null;
		}
		
		moveFlag=true;
		eatFlag=false;
		action=1;
		key=1;
		//重新移动
		
		move();		
	}

	//移动
	@Override
	void move(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(alive && moveFlag && !deadFlag){
					x-=speed;
					
					changeImage();
					
					eat();
					
					if(x<100){
						System.out.println("结束了");
						panel.gameOver();
					}
					
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
		if(action==1){
			if(key>22){
				key=1;
			}
			this.image=(BufferedImage)zombieMoveHashMap.get("("+key+")");
		}else if(action==2){
			if(key>21){
				doEatPlant();
				key=1;
			}
			this.image=(BufferedImage)zombieEatHashMap.get("("+key+")");
		}else if(action==3){
			if(key>10){//僵尸死亡动画执行完毕
				deadFlag = false;
				clear();
				return ;
			}
			this.image=(BufferedImage)zombieDeadHashMap.get("("+key+")");
			//坐标稍微做写调整，因为死亡的图片更大
			x-=5;
			y+=10;
		}
		width = this.image.getWidth();
		height = this.image.getHeight();
	}
	
	//清除僵尸
	void clear(){
		this.alive=false;
		panel.zombies.remove(this);
		
		if(panel.winComputed){//最后一波后计算僵尸的数量了，如果数量为0全部消灭就赢了
			if(panel.zombies.size()==0){
				panel.gameWin();
			}
		}
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
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
}
