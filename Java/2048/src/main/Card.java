package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import sun.font.FontDesignMetrics;

public class Card {
	private int x = 0;// x坐标
	private int y = 0;// y坐标
	private int w = 80;// 宽
	private int h = 80;// 高
	private int i = 0;//下标i
	private int j = 0;//下标j
	private int start=10;//偏移量（固定值）
	private int num=0;//显示数字
	private boolean merge=false;//当前是否被合并过，如果合并了，则不能继续合并，针对当前轮
	
	public Card(int i,int j){
		this.i=i;
		this.j=j;
	}
	//根据i j计算x y坐标
	private void cal(){
		this.x = start + j*w + (j+1)*5;
		this.y = start + i*h + (i+1)*5;
	}
	//绘制方法
	public void draw(Graphics g) {
		cal();
		//获取旧的颜色
		Color oColor = g.getColor();
		//获取要用的颜色
		Color color = getColor();
		//设置画笔颜色
		g.setColor(color);
		g.fillRoundRect(x, y, w, h, 4, 4);
		
		if(num!=0){
			//设置字的颜色
			g.setColor(new Color(125,78,51));
			Font font = new Font("思源宋体", Font.BOLD, 35);
			g.setFont(font);
			//转换成String
			String text = num+"";
			//计算该字体文本的长度
	        int wordWidth = getWordWidth(font, text);
	        //计算出字体居中位置的X坐标
	        int sx = x+(w-wordWidth)/2;
	        //绘制
			g.drawString(text, sx , y+50);
		}
		
		//恢复画笔颜色
		g.setColor(oColor);
	}
	
	//得到该字体字符串的长度  
	public static int getWordWidth(Font font, String content) {
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        int width = 0;
        for (int i = 0; i < content.length(); i++) {
            width += metrics.charWidth(content.charAt(i));
        }
        return width;
    }
	//获取color
	private Color getColor(){
		Color color=null;
		//根据num设定颜色
		switch (num) {
		case 2:
			color = new Color(238,244,234);
			break;
		case 4:
			color = new Color(222,236,200);
			break;
		case 8:
			color = new Color(174,213,130);
			break;
		case 16:
			color = new Color(142,201,75);
			break;
		case 32:
			color = new Color(111,148,48);
			break;
		case 64:
			color = new Color(76,174,124);
			break;
		case 128:
			color = new Color(60,180,144);
			break;
		case 256:
			color = new Color(45,130,120);
			break;
		case 512:
			color = new Color(9,97,26);
			break;
		case 1024:
			color = new Color(242,177,121);
			break;
		case 2048:
			color = new Color(223,185,0);
			break;

		default://默认颜色
			color = new Color(92,151,117);
			break;
		}
		
		return color;
	}
	
	//卡片向上移动
	public boolean moveTop(Card[][] cards,boolean bool) {
		//设定退出条件
		if(i==0){//已经是最上面了
			return false;
		}
		//上面一个卡片
		Card prev = cards[i-1][j];
		if(prev.getNum()==0){//上一个卡片是空
			//移动，本质就是设置数字
			if(bool){//bool为true才执行，因为flase只是用来判断能否移动
				prev.num=this.num;
				this.num=0;
				//递归操作（注意这里是要 prev 来 move了）
				prev.moveTop(cards,bool);
			}
			return true;
		}else if(prev.getNum()==num && !prev.merge){//合并操作（如果已经合并了，则不运行再次合并，针对当然轮）
			if(bool){////bool为true才执行
				prev.merge=true;
				prev.num=this.num*2;
				this.num=0;
			}
			return true;
		}else {//上一个的num与当前num不同，无法移动，并退出
			return false;
		}
	}
	//向下移动
	public boolean moveBottom(Card[][] cards,boolean bool) {
		//设定退出条件
		if(i==3){//已经是最下面了
			return false;
		}
		//上面一个卡片
		Card prev = cards[i+1][j];
		if(prev.getNum()==0){//上一个卡片是空
			//移动，本质就是设置数字
			if(bool){//bool为true才执行，因为flase只是用来判断能否移动
				prev.num=this.num;
				this.num=0;
				//递归操作（注意这里是要 prev 来 move了）
				prev.moveBottom(cards,bool);
			}
			return true;
		}else if(prev.getNum()==num && !prev.merge){//合并操作（如果已经合并了，则不运行再次合并，针对当然轮）
			if(bool){////bool为true才执行
				prev.merge=true;
				prev.num=this.num*2;
				this.num=0;
			}
			return true;
		}else {//上一个的num与当前num不同，无法移动，并退出
			return false;
		}
	
		
	}
	//向右移动
	public boolean moveRight(Card[][] cards,boolean bool) {
		//设定退出条件
		if(j==3){//已经是最右边了
			return false;
		}
		//上面一个卡片
		Card prev = cards[i][j+1];
		if(prev.getNum()==0){//上一个卡片是空
			//移动，本质就是设置数字
			if(bool){//bool为true才执行，因为flase只是用来判断能否移动
				prev.num=this.num;
				this.num=0;
				//递归操作（注意这里是要 prev 来 move了）
				prev.moveRight(cards,bool);
			}
			return true;
		}else if(prev.getNum()==num && !prev.merge){//合并操作（如果已经合并了，则不运行再次合并，针对当然轮）
			if(bool){////bool为true才执行
				prev.merge=true;
				prev.num=this.num*2;
				this.num=0;
			}
			return true;
		}else {//上一个的num与当前num不同，无法移动，并退出
			return false;
		}
	}
	//向左移动
	public boolean moveLeft(Card[][] cards,boolean bool) {
		//设定退出条件
		if(j==0){//已经是最左边了
			return false;
		}
		//上面一个卡片
		Card prev = cards[i][j-1];
		if(prev.getNum()==0){//上一个卡片是空
			//移动，本质就是设置数字
			if(bool){//bool为true才执行，因为flase只是用来判断能否移动
				prev.num=this.num;
				this.num=0;
				//递归操作（注意这里是要 prev 来 move了）
				prev.moveLeft(cards,bool);	
			}
			return true;
		}else if(prev.getNum()==num && !prev.merge){//合并操作（如果已经合并了，则不运行再次合并，针对当然轮）
			if(bool){////bool为true才执行
				prev.merge=true;
				prev.num=this.num*2;
				this.num=0;
			}
			return true;
		}else {//上一个的num与当前num不同，无法移动，并退出
			return false;
		}
	}
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public boolean isMerge() {
		return merge;
	}
	public void setMerge(boolean merge) {
		this.merge = merge;
	}
}
