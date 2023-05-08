package main;

import java.awt.Color;
import javax.swing.JFrame;
/**
 *窗体类
 */
public class GameFrame extends JFrame {
	//构造方法
	public GameFrame(){
		setTitle("2048");//设置标题
		setSize(370, 420);//设置窗体大小
		getContentPane().setBackground(new Color(66,136,83));//加上背景
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭后进程退出
		setLocationRelativeTo(null);//居中
		setResizable(false);//不允许变大
	}
}
