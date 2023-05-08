package main;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/*
 * 画布类
 */
public class GamePanel extends JPanel implements ActionListener{
	private JMenuBar jmb = null;
	private GameFrame mainFrame = null;
	private GamePanel panel = null;
	private String gameFlag = "start";//游戏状态
	
	private final int COLS=4;//列
	private final int ROWS=4;//行
	
	private Card cards[][] = new Card[ROWS][COLS];
	//构造方法
	public GamePanel(GameFrame mainFrame){
		this.setLayout(null);
		this.setOpaque(false);
		this.mainFrame=mainFrame;
		this.panel =this;
		
		//创建菜单
		createMenu();
		
		//初始化
		init();
		
		//随机创建一个数字
		createRandomNumber();
		//添加键盘事件
		createKeyListener();
	}
	
	private Font createFont(){
		return new Font("思源宋体",Font.BOLD,18);
	}
	
	//创建菜单
	private void createMenu() {
		//创建JMenuBar
		jmb = new JMenuBar();
		//取得字体
		Font tFont = createFont(); 
		//创建游戏选项
		JMenu jMenu1 = new JMenu("游戏");
		jMenu1.setFont(tFont);
		//创建帮助选项
		JMenu jMenu2 = new JMenu("帮助");
		jMenu2.setFont(tFont);
		
		JMenuItem jmi1 = new JMenuItem("新游戏");
		jmi1.setFont(tFont);
		JMenuItem jmi2 = new JMenuItem("退出");
		jmi2.setFont(tFont);
		//jmi1 jmi2添加到菜单项“游戏”中
		jMenu1.add(jmi1);
		jMenu1.add(jmi2);
		
		JMenuItem jmi3 = new JMenuItem("操作帮助");
		jmi3.setFont(tFont);
		JMenuItem jmi4 = new JMenuItem("胜利条件");
		jmi4.setFont(tFont);
		//jmi13 jmi4添加到菜单项“游戏”中
		jMenu2.add(jmi3);
		jMenu2.add(jmi4);
		
		jmb.add(jMenu1);
		jmb.add(jMenu2);
		
		mainFrame.setJMenuBar(jmb);
		
		//添加监听
		jmi1.addActionListener(this);
		jmi2.addActionListener(this);
		jmi3.addActionListener(this);
		jmi4.addActionListener(this);
		//设置指令
		jmi1.setActionCommand("restart");
		jmi2.setActionCommand("exit");
		jmi3.setActionCommand("help");
		jmi4.setActionCommand("win");
	}
	
	//初始化
	private void init() {
		Card card;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				card = new Card(i,j);
				cards[i][j]=card;
			}
		}
	}
	//在随机的空卡片创建数字2或者4
	private void createRandomNumber() {
		int num = 0;
		Random random = new Random();
		int index = random.nextInt(5)+1;//这样取出来的就是1-5 之间的随机数
		//因为2和4出现的概率是1比4，所以如果index是1，则创建数字4，否则创建数字2(1被随机出来的概率就是1/5，而其他就是4/5 就是1：4的关系)
		
		if(index==1){
			num = 4;
		}else {
			num = 2;
		}
		//判断如果格子已经满了，则不再获取，退出
		if(cardFull()){
			return ;
		}
		//获取随机卡片，不为空的
		Card card = getRandomCard(random);
		//给card对象设置数字
		if(card!=null){
			card.setNum(num);
		}
	}
	//获取随机卡片，不为空的
	private Card getRandomCard(Random random) {
		int i = random.nextInt(ROWS);
		int j = random.nextInt(COLS);
		Card card = cards[i][j];
		if(card.getNum()==0){//如果是空白的卡片，则找到了，直接返回
			return card;
		}
		//没找到空白的，就递归，继续寻找
		return getRandomCard(random);
	}
	//判断格子满了
	private boolean cardFull() {
		Card card;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				card = cards[i][j];
				if(card.getNum()==0){//有一个为空，则没满
					return false;
				}
			}
		}		
		return true;
	}

	//添加键盘监听
	private void createKeyListener() {
		KeyAdapter l = new KeyAdapter() {
			//按下
			@Override
			public void keyPressed(KeyEvent e) {
				if(!"start".equals(gameFlag)) return ;
				int key = e.getKeyCode();
				switch (key) {
					//向上
					case KeyEvent.VK_UP:
					case KeyEvent.VK_W:
						moveCard(1);//向上
						break;
						
					//向右	
					case KeyEvent.VK_RIGHT:
					case KeyEvent.VK_D:
						moveCard(2);//向右
						break;
						
					//向下
					case KeyEvent.VK_DOWN:
					case KeyEvent.VK_S:
						moveCard(3);//向上下
						break;
						
					//向左
					case KeyEvent.VK_LEFT:
					case KeyEvent.VK_A:
						moveCard(4);//向左
						break;
				}
			
			}
			//松开
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
		};
		//给主frame添加键盘监听
		mainFrame.addKeyListener(l);
	}
	//卡片移动的方法
	protected void moveCard(int dir) {
		//将卡片清理一遍，因为每轮移动会设定合并标记，需重置
		clearCard();
		
		if(dir==1){//向上移动
			moveCardTop(true);
		}else if(dir==2){//向右移动
			moveCardRight(true);
		}else if(dir==3){//向下移动
			moveCardBottom(true);
		}else if(dir==4){//向左移动
			moveCardLeft(true);
		}
		//移动后要创建新的卡片
		createRandomNumber();
		//重绘
		repaint();
		//判断游戏是否结束
		gameOverOrNot();
	}
	//判断游戏是否结束
	private void gameOverOrNot() {
		/* 结束条件：
		 * 1.位置已满
		 * 2.4个方向都没有可以合并的卡片
		 */
		if(isWin()){//胜利
			gameWin();
		}else if(cardFull()){//位置已满
			if(moveCardTop(false)||
				moveCardRight(false)||
				moveCardBottom(false)||
				moveCardLeft(false)){//只要有一个方向可以移动或者合并，就表没结束
				return ;
			}else{//游戏失败
				gameOver();
			}
		}
	}
	//是否获得了胜利
	private boolean isWin(){
		Card card;
		for (int i = 0; i < ROWS; i++) {//i从1开始，因为i=0不需要移动
			for (int j = 0; j < COLS; j++) {
				card = cards[i][j];
				if(card.getNum()==2048){//胜利了
					return true;
				}
			}
		}
		return false;
	}
	//将卡片清理一遍，因为每轮移动会设定合并标记，需重置
	private void clearCard() {
		Card card;
		for (int i = 0; i < ROWS; i++) {//i从1开始，因为i=0不需要移动
			for (int j = 0; j < COLS; j++) {
				card = cards[i][j];
				card.setMerge(false);
			}
		}
	}

	//向上移动
	private boolean moveCardTop(boolean bool) {
		boolean res = false;
		Card card;
		for (int i = 1; i < ROWS; i++) {//i从1开始，因为i=0不需要移动
			for (int j = 0; j < COLS; j++) {
				card = cards[i][j];
				if(card.getNum()!=0){//只要卡片不为空，要移动
					if(card.moveTop(cards,bool)){//向上移动
						res = true;//有一个为移动或者合并了，则res为true
					}
				}
			}
		}
		return res;
	}
	
	//向右移动
	private boolean moveCardRight(boolean bool) {
		boolean res = false;
		Card card;
		for (int i = 0; i < ROWS; i++) {
			for (int j = COLS-1; j >=0 ; j--) {//j从COLS-1开始，从最右边开始移动递减
				card = cards[i][j];
				if(card.getNum()!=0){//只要卡片不为空，要移动
					if(card.moveRight(cards,bool)){//向右移动
						res = true;//有一个为移动或者合并了，则res为true
					}
				}
			}
		}
		return res;
	}
	
	//向下移动
	private boolean moveCardBottom(boolean bool) {
		boolean res = false;
		Card card;
		for (int i = ROWS-1; i >=0; i--) {//i从ROWS-1开始，往下递减移动
			for (int j = 0; j < COLS; j++) {
				card = cards[i][j];
				if(card.getNum()!=0){//只要卡片不为空，要移动
					if(card.moveBottom(cards,bool)){//下移动
						res = true;//有一个为移动或者合并了，则res为true
					}
				}
			}
		}
		return res;
	}
	
	//向左移动
	private boolean moveCardLeft(boolean bool) {
		boolean res = false;
		Card card;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 1; j < COLS ; j++) {//j从1开始，从最左边开始移动
				card = cards[i][j];
				if(card.getNum()!=0){//只要卡片不为空，要移动
					if(card.moveLeft(cards,bool)){//向左移动
						res = true;//有一个为移动或者合并了，则res为true
					}
				}
			}
		}
		return res;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		//绘制卡片
		drawCard(g);
	}
	//绘制卡片
	private void drawCard(Graphics g) {
		Card card;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				card = cards[i][j];
				card.draw(g);
			}
		}		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("思源宋体", Font.ITALIC, 18)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("思源宋体", Font.ITALIC, 18)));
		if ("exit".equals(command)) {
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "您确认要退出吗", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				System.exit(0);
			} 
		}else if("restart".equals(command)){
			restart();
		}else if("help".equals(command)){
			JOptionPane.showMessageDialog(null, "通过键盘的上下左右来移动，相同数字会合并！",
					"提示！", JOptionPane.INFORMATION_MESSAGE);
		}else if("win".equals(command)){
			JOptionPane.showMessageDialog(null, "得到数字2048获得胜利，当没有空卡片则失败！",
					"提示！", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	//重新开始
	void restart() {
		gameFlag = "start";
		Card card;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				card = cards[i][j];
				card.setNum(0);
				card.setMerge(false);
			}
		}
		//随机创建一个数字
		createRandomNumber();
		//重新绘制
		repaint();
	}
	//游戏胜利
	public void gameWin() {
		gameFlag = "end";
		//弹出结束提示
		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("思源宋体", Font.ITALIC, 18)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("思源宋体", Font.ITALIC, 18)));
	    JOptionPane.showMessageDialog(mainFrame, "你成功了,太棒了!");
	}
	
	//游戏结束
	public void gameOver() {
		gameFlag = "end";
		//弹出结束提示
		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("思源宋体", Font.ITALIC, 18)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("思源宋体", Font.ITALIC, 18)));
	    JOptionPane.showMessageDialog(mainFrame, "你失败了,请再接再厉!");
	}
}
