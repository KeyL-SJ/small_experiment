package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import main.card.Card;
import main.common.GameApp;
import main.common.MusicPlayer;
import main.plants.GeneralZombie;
import main.plants.Plant;
import main.plants.PlantsRect;
import main.plants.Sun;
import main.plants.Wandou;
import main.plants.WandouPlant;
import main.plants.Zombie;
import main.tool.Shovel;
import main.tool.ShovelCard;

/*
 * 画布类
 */
public class GamePanel extends JPanel implements ActionListener {
	GamePanel gamePanel=this;
	private JFrame mainFrame=null;
	JMenuBar jmb = null;
	BufferedImage bg=null;//背景图片
	
	MusicPlayer musicBg=null;
	MusicPlayer musicWin=null;
	MusicPlayer musicLost=null;
	MusicPlayer musicPlant=null;
	
	public int gameWidth=0;
	public int gameHeight=0;
	public List plantsRect = new ArrayList();//植物种植背景方块
	public List plantsCard = new ArrayList();//植物卡牌
	public List cardRects = new ArrayList();//卡牌遮罩
	public List plants = new ArrayList();//植物
	public List suns = new ArrayList();//太阳光
	public List wandous = new ArrayList();//豌豆
	public List zombies = new ArrayList();//僵尸
	
	public List tempImages = new ArrayList();//临时图片
	public BufferedImage  tempBufferedImage = null;//临时图片对象
	
	public Plant curPlant = null;//当前种植的植物
	public Shovel shovel = null;//当前铲子对象
	public ShovelCard shovelCard = null;//铲子卡牌对象
	
	public HashMap imageMap = new HashMap();//图片Map对象
	public HashMap sunPlantImageMap = new HashMap();//太阳花图片Map对象
	public HashMap sunImageMap = new HashMap();//太阳图片Map对象
	public HashMap wandouPlantHashMap = new HashMap();//豌豆植物图片Map对象
	
	public HashMap zombieMoveHashMap = new HashMap();//僵尸移动图片Map对象
	public HashMap zombieEatHashMap = new HashMap();//僵尸吃图片Map对象
	public HashMap zombieDeadHashMap = new HashMap();//僵尸死亡图片Map对象
	
	public HashMap wallNutHashMap1 = new HashMap();//胡桃图片Map对象
	public HashMap wallNutHashMap2 = new HashMap();//胡桃图片Map对象
	public HashMap wallNutHashMap3 = new HashMap();//胡桃图片Map对象
	
	private int last=6;//自动创建5波后将会是最后一波
	private int cur=0;//僵尸波数计数器
	
	public int sunCount=600;//太阳数值
	public int winCount=300;//300分结束游戏，没一个僵尸10分，打死30个僵尸获得胜利
	public int curCount=0;//积分
	private int timeCount=0;//创建僵尸的计数器
	private int sunTimeCount=0;//创建太阳计数器
	
	private static boolean startFlag=true;
	private static boolean nextEnd=false;
	private static boolean nextWin=false;
	private static boolean lastFlag=false;
	public static boolean winComputed=false;
	
	//构造里面初始化相关参数
	public GamePanel(JFrame frame){
		this.setLayout(null);
		mainFrame = frame;
		//创建按钮
		initMenu();
		//初始化图片
		initImage();
		//创建田种植物的背景方块
		initPlantsRect();
		//初始化卡牌
		initCard();
		//创建卡牌遮罩
		initCardRect();
		//创建铲子卡牌
		createShovelCard();
		//添加鼠标事件监控
		createMouseListener();
		
		//开启背景音乐
		musicBg=new MusicPlayer("/music/bg.wav");
		musicBg.loop(-1);
		
		mainFrame.setVisible(true);
		 //主线程启动
		new Thread(new RefreshThread()).start();
		
		ready();
	}
	//
	private void createShovelCard() {
		shovelCard = new ShovelCard(454, 4,68,28,(BufferedImage)imageMap.get("16"), this);	
	}

	private void createZombies() {
		Random random = new Random();
		int count = random.nextInt(5)+1;//随机数量的僵尸
		
		for(int i=0;i<count;i++){
			createZombie(random);
		}
	
		cur++;
		if(cur==last){
			lastFlag = true;
			//最后一波
			last();
		}
	}
	
	//准备
	private void ready() {
		tempBufferedImage = (BufferedImage)imageMap.get("10");
		tempImages.add(tempBufferedImage);
		
		//2秒后关闭图片
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				tempImages.remove(tempBufferedImage);
			}
		}).start();
	}
	
	//最后一波
	private void last() {
		//图片 最后一波
		tempBufferedImage = (BufferedImage)imageMap.get("15");
		tempImages.add(tempBufferedImage);
		
		//2秒后关闭图片
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				tempImages.remove(tempBufferedImage);
			}
		}).start();
		
		//创建一大波僵尸  5秒后
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//创建最后一波僵尸
				createLastZombie();
				
				//僵尸全部创建完成后，要开始计算胜利了
				winComputed=true;
			}
		}).start();
	}
	//创建最后一波僵尸
	int n=0;
	private void createLastZombie(){
		Random random = new Random();
		final int count = random.nextInt(10)+20;//随机数量的僵尸 20-30
		//分三批
		n=0;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(n<3){
					n++;
					for(int i=0;i<=count/3;i++){
						createZombie(new Random());
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	
	private void createZombie(Random random){
		int x=805;
		int y=0;
		int index = random.nextInt(5)+1;//随机获取1\2\3\4\5 行数
		if(index==1){
			y=60;
		}else if(index==2){
			y=160;
		}else if(index==3){
			y=260;
		}else if(index==4){
			y=355;
		}else if(index==5){
			y=460;
		}
		
		int x1 = random.nextInt(90)+20;//让出来的X有个范围，这样就不会挤在一起
		
		Zombie zombie = new GeneralZombie(x+x1, y, 75,119, this);
		zombie.setIndex(index);//设置是第几行的僵尸
		zombie.setHp(10);//设置血量
		zombies.add(zombie);
	}

	private void initCard() {
		int x=0,y=0;
		Card card=null;
		card = new Card(76,5,50,68,(BufferedImage)imageMap.get("3"),"sun",50,1,this);//向日葵卡牌
		plantsCard.add(card);
		
		card = new Card(127,4,50,70,(BufferedImage)imageMap.get("4"),"wandou",100,2,this);//豌豆射手卡牌
		plantsCard.add(card);
		
		card = new Card(177,4,50,70,(BufferedImage)imageMap.get("5"),"wallNut",50,3,this);//胡桃卡牌
		plantsCard.add(card);
	}
	//创建卡牌遮罩
	private void initCardRect() {
		int x=0,y=0,width=0,height=0;
		//这里也用PlantsRect类
		
		x=76;
		y=5;
		width=50;
		height=68;
		PlantsRect pRec1 = new PlantsRect(x,y,width,height,0);
		cardRects.add(pRec1);
		
		x=127;
		y=4;
		width=50;
		height=70;
		PlantsRect pRec2 = new PlantsRect(x,y,width,height,0);
		cardRects.add(pRec2);
		
		x=177;
		y=4;
		width=50;
		height=70;
		PlantsRect pRec3 = new PlantsRect(x,y,width,height,0);
		cardRects.add(pRec3);
		
		cardCanUse();
	}
	//处理卡牌是否可用
	public void cardCanUse() {
		PlantsRect rect=null;
		Card card = null;
		for (int i = 0; i < plantsCard.size(); i++) {
			card= (Card)plantsCard.get(i);
			rect = (PlantsRect)cardRects.get(i);
			if(sunCount>=card.getCost()){//遮罩不显示
				rect.setColor(new Color(192,192,192,0));
			}else{//遮罩显示
				rect.setColor(new Color(192,192,192,155));
			}
		}
	}
	//鼠标右键点击
	void mousetRightClick(){
		if(curPlant!=null){//当前正在种植物
			/* 撤销植物种植操作
			 * 1.退还阳光花费
			 * 2.清除当前植物
			 * 3.curPlant清空
			 * 4.处理田的背景方块
			 * 5.欢原阳光花费后，处理卡牌是否可用
			 */
			sunCount += curPlant.getCost();
			curPlant.clear();
			curPlant=null;
			cardCanUse();
			
			musicPlant = new MusicPlayer("/music/plant.wav");
			musicPlant.play();
			
			PlantsRect pRect = null;
			for (int i = 0; i < plantsRect.size(); i++) {
				pRect = (PlantsRect)plantsRect.get(i);
				pRect.setColor(new Color(0,250,154, 0));//都设置为不显示
			}
		}
		//清除铲子对象
		if(shovel!=null){
			shovel=null;
			musicPlant = new MusicPlayer("/music/plant.wav");
			musicPlant.play();
		}
	}
	//鼠标左键点击
	public void mousetLeftClick(MouseEvent e){
		int x = e.getX();
		int y = e.getY();
		boolean isCatch=false;
		
		if(curPlant!=null || shovel!=null){//当前正在种植物 或者铲去植物
			PlantsRect pRect = null;
			for (int i = 0; i < plantsRect.size(); i++) {
				pRect = (PlantsRect)plantsRect.get(i);
				if(pRect.isPoint(x, y)){
					if(curPlant!=null){//种植
						if(pRect.getPlant()!=null){//已经有植物了，不能再种植
							break;
						}
						//执行种植操作
						curPlant.plant(pRect);
						pRect.setPlant(curPlant);
						curPlant = null;
						
						musicPlant = new MusicPlayer("/music/plant.wav");
						musicPlant.play();
						
					}else if(shovel!=null){//铲去植物
						Plant plant = pRect.getPlant();
						if(plant!=null){
							//通知在吃的僵尸
							plant.noteZombie();
							//植物清除
							plant.clear();
							plant=null;
							//铲子清除
							shovel= null;
							
							musicPlant = new MusicPlayer("/music/plant.wav");
							musicPlant.play();
						}else {
							break;
						}
					}
					
					//种植背景消失
					pRect.setColor(new Color(0,250,154, 0));
					isCatch =true;
					
					break;
				}
			}
		}
		
		if(isCatch) return ;//已经被捕获了，后面的就没必要执行了
		
		if(curPlant==null && shovel==null ){//如果当前没有正在种植物，才可以进行选择卡牌
			Card card=null;
			for (int i = 0; i < plantsCard.size(); i++) {
				card = (Card)plantsCard.get(i);
				if(card.isPoint(x, y)){
					if(sunCount<card.getCost()){//阳光不足，则不能创建
						continue;
					}
					musicPlant = new MusicPlayer("/music/plant.wav");
					musicPlant.play();
					
					isCatch=true;
					//在鼠标坐标处创建对应的植物
					card.createPlant(x, y,card.getCost());
					//扣除对应的阳光
					sunCount-=card.getCost();
					//处理遮罩是否显示
					cardCanUse();
					break;
				}
			}
		}
		if(isCatch) return ;//已经被捕获了，后面的就没必要执行了
		
		Sun sun=null;
		for (int i = 0; i < suns.size(); i++) {
			sun = (Sun)suns.get(i);
			if(sun.isPoint(x, y)){
				isCatch=true;
				//阳光被点击
				sun.click();
				break;
			}
		}
		
		if(curPlant==null){//当前不是在种植物才创建
			if(shovel==null){//当前没有铲子才创建
				if(shovelCard.isPoint(x, y)){
					isCatch=true;
					//创建跟随鼠标的铲子
					shovel = shovelCard.createShovel(x,y);
				}
			}
		}
	}
	
	//鼠标事件的创建
	private void createMouseListener() {
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int key = e.getModifiers();
				if(key==4){//鼠标右键点击 
					mousetRightClick();
				}else if(key==16){//鼠标左键点击 
					mousetLeftClick(e);
				}
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if(curPlant!=null || shovel!=null){//当前正在种植物 或者准备铲植物
					
					if(curPlant!=null){
						//让植物跟随鼠标
						curPlant.setX(x-curPlant.getWidth()/2);
						curPlant.setY(y-curPlant.getHeight()/2);
					}else if(shovel!=null){
						//铲子跟随鼠标
						shovel.setX(x-shovel.getWidth()/2);
						shovel.setY(y-shovel.getHeight()/2);
					}
					
					PlantsRect pRect = null;
					for (int i = 0; i < plantsRect.size(); i++) {
						pRect = (PlantsRect)plantsRect.get(i);
						if(pRect.isPoint(x, y)){
							if(pRect.getPlant()!=null){//如果已经有植物了 
								if(curPlant!=null){//不能种植
									pRect.setColor(new Color(255,23,13, 190));
								}else if(shovel!=null){//可以铲除
									pRect.setColor(new Color(128,128,128, 100));
								}
							}else{
								if(curPlant!=null){//可以种植
									pRect.setColor(new Color(0,250,154, 120));
								}else if(shovel!=null){//不能铲除
									pRect.setColor(new Color(255,23,13, 140));
								}
							}
						}else{
							pRect.setColor(new Color(0,250,154, 0));
						}
					}
				}
				
				
			}
		};
		addMouseMotionListener(mouseAdapter);
		addMouseListener(mouseAdapter);
	}
	
	private void initPlantsRect() {
		int x=0,y=0;
		PlantsRect pRect=null;
		for(int i=1;i<=5;i++){//5行
			y = 75+(i-1)*100;
			for(int j=1;j<=9;j++){//9列
				x = 105+(j-1)*80;
				pRect = new PlantsRect(x,y,80,100,i);
				plantsRect.add(pRect);
			}
		}
		
	}
	//创建太阳
	private void createSun() {
		Random random = new Random();
		int x = random.nextInt(400)+300;//300-700
		Sun sun = new Sun(x, 0, 45, 44, gamePanel);
		sun.setDownMax(400);//自动创建的设置移动400
		suns.add(sun);
	}

	//游戏结束
	public void gameOver() {
		nextEnd = true;//表示下一步执行线程后要全部停止
		tempBufferedImage = (BufferedImage)imageMap.get("13");
		tempImages.add(tempBufferedImage);
	}
	public void gameWin(){
		nextWin = true;//表示下一步执行线程后要全部停止
		tempBufferedImage = (BufferedImage)imageMap.get("14");
		tempImages.add(tempBufferedImage);
	}
	//游戏结束
	public void realGameEnd(int type) {
		startFlag=false;//停止线程
		
		musicBg.stop();
		
		PlantsRect pRect = null;
		for (int i = 0; i < plantsRect.size(); i++) {
			pRect = (PlantsRect)plantsRect.get(i);
			pRect.setPlant(null);
		}
		
		//清除植物
		Plant plant=null;
		for (int i = 0; i < plants.size(); i++) {
			plant = (Plant)plants.get(i);
			plant.setAlive(false);
			plant=null;
		}
		//除太阳光
		Sun sun=null;
		for (int i = 0; i < suns.size(); i++) {
			sun = (Sun)suns.get(i);
			sun.setAlive(false);
			sun=null;
		}
		//除豌豆
		Wandou wandou=null;
		for (int i = 0; i < wandous.size(); i++) {
			wandou = (Wandou)wandous.get(i);
			wandou.setAlive(false);
			wandou=null;
		}
		//清除僵尸
		Zombie zombie=null;
		for (int i = 0; i < zombies.size(); i++) {
			zombie = (Zombie)zombies.get(i);
			zombie.setAlive(false);
			zombie=null;
		}
		
		//type=1 则不需要提示
		if(type!=1){
			if(nextWin){
				musicWin=new MusicPlayer("/music/win.wav");
				musicWin.play();
			}else {
				musicLost=new MusicPlayer("/music/lost.wav");
				musicLost.play();
			}
		}
	}
	
	//重新开始游戏
	private void restart() {
		realGameEnd(1);
		
		//参数重置
		startFlag=true;
		nextEnd=false;
		nextWin=false;
		lastFlag=false;
		winComputed=false;
		
		plants.clear();//植物
		suns.clear();//太阳光
		wandous.clear();//豌豆
		zombies.clear();//僵尸
		tempImages.clear();//临时图片清除
		
		shovel = null;
		curPlant = null;//当前种植的植物
		tempBufferedImage=null;//临时图片的清空
		sunCount=600;//太阳数值
		curCount=0;//积分
		timeCount=0;//创建僵尸的计数器
		sunTimeCount=0;//创建太阳计数器
		cur=0;//僵尸波数计数器重置
		
		new Thread(new RefreshThread()).start(); // 线程启动
		cardCanUse();
		ready();
		
		//开启背景音乐
		musicBg=new MusicPlayer("/music/bg.wav");
		musicBg.loop(-1);
	}
	
	//初始图片
	private void initImage(){
		List commonList = new ArrayList();
		commonList.add("1.png");
		commonList.add("2.png");
		commonList.add("3.png");
		commonList.add("4.png");
		commonList.add("5.gif");
		commonList.add("6.png");
		commonList.add("7.png");
		commonList.add("8.png");
		commonList.add("9.png");
		commonList.add("10.png");
		commonList.add("11.png");
		commonList.add("12.png");
		commonList.add("13.png");
		commonList.add("14.png");
		commonList.add("15.gif");
		commonList.add("16.png");
		commonList.add("17.png");
		imageMap = GameApp.getImageMapByIcon("/images/",commonList);//加载普通图片
		
		List sunPlantList = new ArrayList();
		for (int i = 1; i <= 18; i++) {
			sunPlantList.add("("+i+").png");
		}
		sunPlantImageMap = GameApp.getImageMapByIcon("/images/sunPlant/",sunPlantList);//加载太阳花的图片
		
		
		List sunList = new ArrayList();
		for (int i = 1; i <= 22; i++) {
			sunList.add("("+i+").png");
		}
		sunImageMap = GameApp.getImageMapByIcon("/images/sun/",sunList);//加载太阳的图片
		
		List wandouList = new ArrayList();
		for (int i = 1; i <= 13; i++) {
			wandouList.add("("+i+").png");
		}
		wandouPlantHashMap = GameApp.getImageMapByIcon("/images/wandou/",wandouList);//加载豌豆植物的图片
		
		
		List zombieMoveList = new ArrayList();
		for (int i = 1; i <= 22; i++) {
			zombieMoveList.add("("+i+").png");
		}
		zombieMoveHashMap = GameApp.getImageMapByIcon("/images/zombieRun/",zombieMoveList);//加载僵尸移动的图片
		
		List zombieEatList = new ArrayList();
		for (int i = 1; i <= 21; i++) {
			zombieEatList.add("("+i+").png");
		}
		zombieEatHashMap = GameApp.getImageMapByIcon("/images/zombieEat/",zombieEatList);//加载僵尸吃的图片
		
		List zombieDeadList = new ArrayList();
		for (int i = 1; i <= 10; i++) {
			zombieDeadList.add("("+i+").png");
		}
		zombieDeadHashMap = GameApp.getImageMapByIcon("/images/zombieDead/",zombieDeadList);//加载僵尸死亡的图片
		
		List wallNutList1 = new ArrayList();
		for (int i = 1; i <= 16; i++) {
			wallNutList1.add("("+i+").png");
		}
		wallNutHashMap1 = GameApp.getImageMapByIcon("/images/wallNut/1/",wallNutList1);//胡桃的图片
		
		List wallNutList2 = new ArrayList();
		for (int i = 1; i <= 11; i++) {
			wallNutList2.add("("+i+").png");
		}
		wallNutHashMap2 = GameApp.getImageMapByIcon("/images/wallNut/2/",wallNutList2);//胡桃的图片
		
		List wallNutList3 = new ArrayList();
		for (int i = 1; i <= 15; i++) {
			wallNutList3.add("("+i+").png");
		}
		wallNutHashMap3 = GameApp.getImageMapByIcon("/images/wallNut/3/",wallNutList3);//胡桃的图片
	}
	
	private void  initMenu(){
		// 创建菜单及菜单选项
		jmb = new JMenuBar();
		JMenu jm1 = new JMenu("游戏");
		jm1.setFont(new Font("微软雅黑", Font.BOLD, 15));// 设置菜单显示的字体
		JMenu jm2 = new JMenu("帮助");
		jm2.setFont(new Font("微软雅黑", Font.BOLD, 15));// 设置菜单显示的字体
		
		JMenuItem jmi1 = new JMenuItem("开始新游戏");
		JMenuItem jmi2 = new JMenuItem("退出");
		jmi1.setFont(new Font("微软雅黑", Font.BOLD, 15));
		jmi2.setFont(new Font("微软雅黑", Font.BOLD, 15));
		
		JMenuItem jmi3 = new JMenuItem("操作说明");
		jmi3.setFont(new Font("微软雅黑", Font.BOLD, 15));
		JMenuItem jmi4 = new JMenuItem("胜利条件");
		jmi4.setFont(new Font("微软雅黑", Font.BOLD, 15));
		
		jm1.add(jmi1);
		jm1.add(jmi2);
		
		jm2.add(jmi3);
		jm2.add(jmi4);
		
		jmb.add(jm1);
		jmb.add(jm2);
		mainFrame.setJMenuBar(jmb);// 菜单Bar放到JFrame上
		jmi1.addActionListener(this);
		jmi1.setActionCommand("Restart");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("Exit");
		
		jmi3.addActionListener(this);
		jmi3.setActionCommand("help");
		jmi4.addActionListener(this);
		jmi4.setActionCommand("win");
	}
	
	//刷新线程，用来重新绘制页面
	private class RefreshThread implements Runnable {
		@Override
		public void run() {
			while (startFlag) {
				repaint();
				if(nextEnd||nextWin){
					realGameEnd(0);
					return ;
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				timeCount++;
				if(timeCount>=300 && !lastFlag){//15秒创建一批僵尸
					createZombies();
					timeCount=0;
				}
				
				sunTimeCount++;
				if(sunTimeCount>=100){//5秒创建一个阳光
					createSun();
					sunTimeCount=0;
				}
			}
		}
	}
	

	
	@Override
	public void paint(Graphics g) {
		gameHeight = this.getHeight();
		gameWidth = this.getWidth();
	
		//绘制背景
		g.drawImage((BufferedImage)imageMap.get("1"), -150, 0, null);
		
		//方形卡片盘
		g.drawImage((BufferedImage)imageMap.get("2"), 0, 0,446,80, null);
		
		//铲子背景
		g.drawImage((BufferedImage)imageMap.get("17"), 446, 2,80,35, null);
	
		//积分盘
		g.drawImage((BufferedImage)imageMap.get("12"), 530, -4,128,40, null);
		
		//绘制卡牌
		Card card=null;
		for (int i = 0; i < plantsCard.size(); i++) {
			card = (Card)plantsCard.get(i);
			card.draw(g);
		}
		
		//铲子卡牌
		if(shovelCard!=null){
			shovelCard.draw(g);
		}
		
		//绘制植物田的小块方形
		PlantsRect pRect = null;
		for (int i = 0; i < plantsRect.size(); i++) {
			pRect = (PlantsRect)plantsRect.get(i);
			pRect.draw(g);
		}
		//绘制卡牌遮罩
		PlantsRect rect=null;
		for (int i = 0; i < cardRects.size(); i++) {
			rect = (PlantsRect)cardRects.get(i);
			rect.draw(g);
		}
		
		//绘制植物
		Plant plant=null;
		for (int i = 0; i < plants.size(); i++) {
			plant = (Plant)plants.get(i);
			plant.draw(g);
		}
		//绘制僵尸
		Zombie zombie=null;
		for (int i = 0; i < zombies.size(); i++) {
			zombie = (Zombie)zombies.get(i);
			zombie.draw(g);
		}
		
		//绘制太阳光
		Sun sun=null;
		for (int i = 0; i < suns.size(); i++) {
			sun = (Sun)suns.get(i);
			sun.draw(g);
		}
		//绘制豌豆
		Wandou wandou=null;
		for (int i = 0; i < wandous.size(); i++) {
			wandou = (Wandou)wandous.get(i);
			if(wandou.isFade()){
				wandou.clear();
				continue;
			}
			wandou.draw(g);
		}
		
		//临时图片
		BufferedImage tempImage = null;
		int x =0;
		int y =0;
		for (int i = 0; i < tempImages.size(); i++) {
			tempImage = (BufferedImage)tempImages.get(i);
			x = gameWidth/2-tempImage.getWidth()/2;
			y = gameHeight/2-tempImage.getHeight()/2;
			g.drawImage(tempImage,x,y, null);
		}
		
		//铲子
		if(shovel!=null){
			shovel.draw(g);
		}
		
		
		//阳光能量
		Color oColor = g.getColor();
		g.setColor(Color.blue);
		g.setFont(new Font("微软雅黑", Font.BOLD, 16));
		g.drawString(sunCount+"", 16, 73);
		g.setColor(oColor);
		//得分
		oColor = g.getColor();
		g.setColor(Color.white);
		g.setFont(new Font("微软雅黑", Font.BOLD, 16));
		g.drawString(curCount+"",570, 24);
		g.setColor(oColor);
	}
	
	//各种事件的触发方法
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("宋体", Font.ITALIC, 18)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("宋体", Font.ITALIC, 18)));
		if ("Exit".equals(command)) {
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "您确认要退出吗", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				System.exit(0);
			} 
		}else if("Restart".equals(command)){
			if(startFlag){
				Object[] options = { "确定", "取消" };
				int response = JOptionPane.showOptionDialog(this, "游戏中，您确认要重新开始吗", "",
						JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						options, options[0]);
				if (response == 0) {
					//需要先结束游戏
					realGameEnd(1);
					restart();
				} 
			}else{
				restart();
			}
		}else if("help".equals(command)){
			JOptionPane.showMessageDialog(null, "鼠标点击收集阳光，选择卡牌种植到合适的位置！",
					"提示！", JOptionPane.INFORMATION_MESSAGE);
		}else if("win".equals(command)){
			JOptionPane.showMessageDialog(null, "守住最后一波，获得胜利！",
					"提示！", JOptionPane.INFORMATION_MESSAGE);
		}
		
		
	}

}
