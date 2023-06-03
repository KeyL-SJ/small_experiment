package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/*
 * 背景画布类
 */
public class BackPanel extends JPanel implements ActionListener {
	BackPanel panel = this;
	GamePanel gamePanel = null;
	JMenuBar jmb = null;

	private JFrame mainFrame = null;
	private static final int ROWS = 20;
	private static final int COLS = 15;

	//开始/停止按钮 
	private JButton btnStart = null;
	//暂停/继续按钮 
	private JButton btnPause = null;

	//构造里面初始化相关参数
	public BackPanel(JFrame frame) {
		this.setLayout(null);
		this.setOpaque(false);
		this.mainFrame = frame;
		//初始化
		init();
		//创建按钮
		initMenu();

		mainFrame.setVisible(true);
	}

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		gamePanel.backPanel = this;
	}

	//初始化
	private void init() {
		// 开始/停止按钮
		btnStart = new JButton();
		btnStart.setFont(new Font("黑体", Font.PLAIN, 18));
		btnStart.setFocusPainted(false);
		btnStart.setText("暂停");
		btnStart.setBounds(360, 300, 80, 43);
		btnStart.setBorder(BorderFactory.createRaisedBevelBorder());
		this.add(btnStart);
		btnStart.addActionListener(this);
		btnStart.setActionCommand("start");
	}

	private void initMenu() {
		// 创建菜单及菜单选项
		jmb = new JMenuBar();
		JMenu jm1 = new JMenu("游戏");
		jm1.setFont(new Font("仿宋", Font.BOLD, 15));// 设置菜单显示的字体
		JMenu jm2 = new JMenu("帮助");
		jm2.setFont(new Font("仿宋", Font.BOLD, 15));// 设置菜单显示的字体

		JMenuItem jmi1 = new JMenuItem("开始新游戏");
		JMenuItem jmi2 = new JMenuItem("退出");
		jmi1.setFont(new Font("仿宋", Font.BOLD, 15));
		jmi2.setFont(new Font("仿宋", Font.BOLD, 15));

		JMenuItem jmi3 = new JMenuItem("操作说明");
		jmi3.setFont(new Font("仿宋", Font.BOLD, 15));
		JMenuItem jmi4 = new JMenuItem("失败判定");
		jmi4.setFont(new Font("仿宋", Font.BOLD, 15));

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
		jmi4.setActionCommand("lost");
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		//绘制网格
		drawGrid(g);
		//绘制边框
		drawBorder(g);
		//右边辅助区域
		drawBorderRight(g);
		//绘制积分区域
		drawCount(g);
		//绘制下一个区域
		drawNext(g);
	}

	//绘制积分区域
	private void drawCount(Graphics g) {
		BasicStroke bs_2 = new BasicStroke(2L, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
		Graphics2D g_2d = (Graphics2D) g;
		g_2d.setColor(new Color(0, 0, 0));
		g_2d.setStroke(bs_2);
		g_2d.drawRect(350, 17, 110, 80);

		//得分
		g.setFont(new Font("宋体", Font.BOLD, 20));
		g.drawString("得分：", 380, 40);
	}

	//绘制下一个区域
	private void drawNext(Graphics g) {
		BasicStroke bs_2 = new BasicStroke(2L, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
		Graphics2D g_2d = (Graphics2D) g;
		g_2d.setColor(new Color(0, 0, 0));
		g_2d.setStroke(bs_2);
		g_2d.drawRect(350, 120, 110, 120);

		//得分
		g.setFont(new Font("宋体", Font.BOLD, 20));
		g.drawString("下一个：", 360, 140);
	}

	//绘制边框
	private void drawBorder(Graphics g) {
		BasicStroke bs_2 = new BasicStroke(12L, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
		Graphics2D g_2d = (Graphics2D) g;
		g_2d.setColor(new Color(128, 128, 128));
		g_2d.setStroke(bs_2);

		RoundRectangle2D.Double rect = new RoundRectangle2D.Double(6, 6, 313 - 1, 413 - 1, 2, 2);
		g_2d.draw(rect);
	}

	//绘制右边区域边框
	private void drawBorderRight(Graphics g) {
		BasicStroke bs_2 = new BasicStroke(12L, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
		Graphics2D g_2d = (Graphics2D) g;
		g_2d.setColor(new Color(128, 128, 128));
		g_2d.setStroke(bs_2);

		RoundRectangle2D.Double rect = new RoundRectangle2D.Double(336, 6, 140 - 1, 413 - 1, 2, 2);
		g_2d.draw(rect);
		//g_2d.drawRect(336, 6, 140, 413);
	}

	//绘制网格
	private void drawGrid(Graphics g) {
		Graphics2D g_2d = (Graphics2D) g;
		g_2d.setColor(new Color(255, 255, 255, 150));
		int x1 = 12;
		int y1 = 20;
		int x2 = 312;
		int y2 = 20;
		for (int i = 0; i <= ROWS; i++) {
			y1 = 12 + 20 * i;
			y2 = 12 + 20 * i;
			g_2d.drawLine(x1, y1, x2, y2);
		}

		y1 = 12;
		y2 = 412;
		for (int i = 0; i <= COLS; i++) {
			x1 = 12 + 20 * i;
			x2 = 12 + 20 * i;
			g_2d.drawLine(x1, y1, x2, y2);
		}
	}

	public void end() {
		btnStart.setText("重开");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		System.out.println(command);
		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("宋体", Font.ITALIC, 18)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("宋体", Font.ITALIC, 18)));
		if ("Exit".equals(command)) {
			Object[] options = {"确定", "取消"};
			int response = JOptionPane.showOptionDialog(this, "您确认要退出吗", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				System.exit(0);
			}
		} else if ("Restart".equals(command)) {
			if (!"end".equals(gamePanel.gameFlag)) {
				JOptionPane.showMessageDialog(null, "正在游戏中无法重新开始！",
						"提示！", JOptionPane.INFORMATION_MESSAGE);
			} else {
				if (gamePanel != null) {
					gamePanel.restart();
				}
			}
		} else if ("help".equals(command)) {
			JOptionPane.showMessageDialog(null, "[下][S]、[左][A]、[右][D]移动，[上][W][空格]旋转变形",
					"提示！", JOptionPane.INFORMATION_MESSAGE);
		} else if ("lost".equals(command)) {
			JOptionPane.showMessageDialog(null, "方块触顶则失败！",
					"提示！", JOptionPane.INFORMATION_MESSAGE);
		} else if ("start".equals(command)) {
			System.out.println("开始");
			mainFrame.requestFocus();
			if ("start".equals(gamePanel.gameFlag)) {//进入暂停
				gamePanel.pause();
				btnStart.setText("继续");
			} else if ("pause".equals(gamePanel.gameFlag)) {//进入继续
				gamePanel.goOn();
				btnStart.setText("暂停");
			} else if ("end".equals(gamePanel.gameFlag)) {//进入重开
				gamePanel.restart();
				btnStart.setText("暂停");
			}
		}

	}
}
