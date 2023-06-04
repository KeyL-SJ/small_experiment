package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import common.GameApp;
import common.MusicPlayer;

/*
 * 画布类
 */
public class GamePanel extends JPanel implements ActionListener {
	GamePanel gamePanel=this;
	private JFrame mainFrame=null;
	JMenuBar jmb = null;
	BufferedImage bg=null;//背景图片
	
	MusicPlayer musicBg=null;
	
	private int gameHeight=0;
	private int gameWidth=0;
	
	public List tempImages = new ArrayList();//临时图片
	public BufferedImage  tempBufferedImage = null;//临时图片对象
	
	public HashMap imageMap = new HashMap();//图片Map对象
	private HashMap enemy1boomImageMap = new HashMap();//敌机爆炸图片Map对象
	private HashMap enemy2boomImageMap = new HashMap();//敌机爆炸图片Map对象
	private HashMap enemy3boomImageMap = new HashMap();//敌机爆炸图片Map对象
	private HashMap enemy4boomImageMap = new HashMap();//敌机爆炸图片Map对象
	public List enemyListMap = new ArrayList();
	
	public HashMap mypalneBoomImageMap= new HashMap();//我方飞机爆炸图片Map对象
	
	public MyPlane myPlane=null;//我的飞机
	public List enemyList = new ArrayList();
	public List bulletList = new ArrayList();
	
	public static boolean startFlag=true;
	public static boolean nextEnd=false;
	public static boolean nextWin=false;
	
	public int totalCount=1000;
	public int curCount = 0;
	
	//构造里面初始化相关参数
	public GamePanel(JFrame frame){
		this.setLayout(null);
		mainFrame = frame;
		//创建按钮
		initMenu();
		//初始化图片
		initImage();
		//初始化自己的飞机
		initMyPlane();
		//初始化敌机
		initEnemyPlane();
		//添加鼠标事件监控
		createMouseListener();
		
		mainFrame.setVisible(true);
		 //主线程启动
		new Thread(new RefreshThread()).start();
		
		//开启背景音乐
		musicBg=new MusicPlayer("/music/bg.wav");
		musicBg.loop(-1);
		
		ready();
	}
	//定时创建敌机
	private void initEnemyPlane() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (startFlag) {
					createEnemyPlane();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	//创建敌机
	private void createEnemyPlane() {
		EnemyPlane enemyPlane = new  EnemyPlane(this);
		enemyList.add(enemyPlane);
	}
	//创建自己飞机
	private void initMyPlane() {
		myPlane = new MyPlane(300, 530, 132, 86, this);
	}

	//准备
	private void ready() {
		tempBufferedImage = (BufferedImage)imageMap.get("ready");
		tempImages.add(tempBufferedImage);
		
		//1秒后关闭图片
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				tempImages.remove(tempBufferedImage);
			}
		}).start();
	}
	
	
	
	//鼠标事件的创建
	private void createMouseListener() {
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if(myPlane==null) return ;
				
				if(myPlane.isCanMove()){
					myPlane.move(x,y);
					return;
				}
				//
				if(myPlane.isPoint(x,y)){
					myPlane.setCanMove(true);
				}
				
				
			}
		};
		addMouseMotionListener(mouseAdapter);
		addMouseListener(mouseAdapter);
	}
	

	//游戏结束
	public void gameOver() {
		nextEnd = true;//表示下一步执行线程后要全部停止
		tempBufferedImage = (BufferedImage)imageMap.get("lost");
		tempImages.add(tempBufferedImage);
	}
	public void gameWin(){
		nextWin = true;//表示下一步执行线程后要全部停止
		tempBufferedImage = (BufferedImage)imageMap.get("win");
		tempImages.add(tempBufferedImage);
	}
	//游戏结束
	public void realGameEnd(int type) {
		startFlag=false;//停止线程
		
		musicBg.stop();
		
		EnemyPlane enemyPlane=null;
		for (int i = 0; i < enemyList.size(); i++) {
			enemyPlane = (EnemyPlane)enemyList.get(i);
			if(enemyPlane!=null){
				enemyPlane.setAlive(false);
				enemyPlane=null;
			}
		}
		enemyList.clear();
		
		Bullet bullet=null;
		for (int i = 0; i < bulletList.size(); i++) {
			bullet = (Bullet)bulletList.get(i);
			if(bullet!=null){
				bullet.setAlive(false);
				bullet=null;
			}
		}
		
		
		//type=1 则不需要提示
	/*	if(type!=1){
			if(nextWin){
				musicWin=new MusicPlayer("/music/win.wav");
				musicWin.play();
			}else {
				musicLost=new MusicPlayer("/music/lost.wav");
				musicLost.play();
			}
		}*/
	}
	
	//重新开始游戏
	private void restart() {
		realGameEnd(1);
		
		//参数重置
		startFlag=true;
		nextEnd=false;
		nextWin=false;
		
		curCount=0;
		
		if(myPlane!=null){
			myPlane.clear();
			myPlane=null;//我的飞机
		}
		EnemyPlane enemyPlane=null;
		for (int i = 0; i < enemyList.size(); i++) {
			enemyPlane = (EnemyPlane)enemyList.get(i);
			if(enemyPlane!=null){
				enemyPlane.clear();
			}
		}
		enemyList.clear();
		
		Bullet bullet=null;
		for (int i = 0; i < bulletList.size(); i++) {
			bullet = (Bullet)bulletList.get(i);
			if(bullet!=null){
				bullet.clear();
			}
		}
		bulletList.clear();
		
		tempImages.clear();//临时图片清除
		tempBufferedImage=null;//临时图片的清空
		
		//初始化自己的飞机
		initMyPlane();
		//初始化敌机
		initEnemyPlane();
		
		new Thread(new RefreshThread()).start(); // 线程启动
		
		//开启背景音乐
		musicBg=new MusicPlayer("/music/bg.wav");
		musicBg.loop(-1);
		
		ready();
	}
	
	//初始图片
	private void initImage(){
		List commonList = new ArrayList();
		commonList.add("bg.jpg");
		commonList.add("myplane1.png");
		commonList.add("enemy1.png");
		commonList.add("enemy2.png");
		commonList.add("enemy3.png");
		commonList.add("enemy4.png");
		commonList.add("bullet.png");
		commonList.add("ready.png");
		commonList.add("win.png");
		commonList.add("lost.png");
		
		imageMap = GameApp.getImageMapByIcon("/images/",commonList);//加载普通图片
		
		List enemy1List = new ArrayList();
		for (int i = 1; i <= 6; i++) {
			enemy1List.add("enemy1boom"+i+".png");
		}
		enemy1boomImageMap = GameApp.getImageMapByIcon("/images/enemy1boom/",enemy1List);//加载飞机爆炸的图片
		enemyListMap.add(enemy1boomImageMap);
		
		List enemy2List = new ArrayList();
		for (int i = 1; i <= 6; i++) {
			enemy2List.add("enemy2boom"+i+".png");
		}
		enemy2boomImageMap = GameApp.getImageMapByIcon("/images/enemy2boom/",enemy2List);//加载飞机爆炸的图片
		enemyListMap.add(enemy2boomImageMap);
		
		List enemy3List = new ArrayList();
		for (int i = 1; i <= 6; i++) {
			enemy3List.add("enemy3boom"+i+".png");
		}
		enemy3boomImageMap = GameApp.getImageMapByIcon("/images/enemy3boom/",enemy3List);//加载飞机爆炸的图片
		enemyListMap.add(enemy3boomImageMap);
		
		List enemy4List = new ArrayList();
		for (int i = 1; i <= 6; i++) {
			enemy4List.add("enemy4boom"+i+".png");
		}
		enemy4boomImageMap = GameApp.getImageMapByIcon("/images/enemy4boom/",enemy4List);//加载飞机爆炸的图片
		
		enemyListMap.add(enemy4boomImageMap);
		
		
		List mypalneBoomList = new ArrayList();
		for (int i = 1; i <= 6; i++) {
			mypalneBoomList.add("myplane1boom"+i+".png");
		}
		mypalneBoomImageMap = GameApp.getImageMapByIcon("/images/myplane1boom/",mypalneBoomList);//加载我方飞机爆炸的图片
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
			}
		}
	}
	

	
	@Override
	public void paint(Graphics g) {
		gameHeight = this.getHeight();
		gameWidth = this.getWidth();
		//绘制背景
		g.drawImage((BufferedImage)imageMap.get("bg"), 0, -150, null);
		
		//绘制飞机
		if(myPlane!=null){
			myPlane.draw(g);
		}
		//绘制敌机
		EnemyPlane enemyPlane=null;
		for (int i = 0; i < enemyList.size(); i++) {
			enemyPlane = (EnemyPlane)enemyList.get(i);
			enemyPlane.draw(g);
		}
		//绘制子弹
		Bullet bullet=null;
		for (int i = 0; i < bulletList.size(); i++) {
			bullet = (Bullet)bulletList.get(i);
			bullet.draw(g);
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
		
		//得分
		Color oColor = g.getColor();
		oColor = g.getColor();
		g.setColor(Color.white);
		g.setFont(new Font("微软雅黑", Font.BOLD, 16));
		g.drawString("得分："+curCount+"",400, 24);
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
			JOptionPane.showMessageDialog(null, "游戏开始后，要先动鼠标到飞机处，触发移动效果，然后飞机就会跟随鼠标移动！",
					"提示！", JOptionPane.INFORMATION_MESSAGE);
		}else if("win".equals(command)){
			JOptionPane.showMessageDialog(null, "得分1000，获得胜利！",
					"提示！", JOptionPane.INFORMATION_MESSAGE);
		}
		
		
	}

}
