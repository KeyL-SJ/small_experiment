package main;

import java.awt.Graphics;

public class Block {

	private int x = 0;
	private int y = 0;
	private int mX = 0;//在模型中处于的X位置
	private int mY = 0;//在模型中处于的Y位置
	private GamePanel panel = null;

	public Block(int x, int y, int mX, int mY, GamePanel panel) {
		this.x = x;
		this.y = y;
		this.panel = panel;
		this.mX = mX;
		this.mY = mY;
	}

	//绘制
	void draw(Graphics g) {
		g.fillRect(12 + (mX + x) * 20, 12 + (mY + y) * 20, 20, 20);
	}

	//绘制下一个，位置固定
	void drawNext(Graphics g) {
		g.fillRect(380 + mX * 20 + 1, 170 + mY * 20 + 1, 19, 19);
	}

	//向左
	void move(boolean xDir, int step) {
		if (xDir) {//X方向的移动，step 正数向右 负数向左
			x += step;
		} else {
			y += step;
		}
		panel.repaint();
	}

	//边界处理
	public boolean outside(boolean xDir, int step) {
		if (xDir) {//横向
			int x = (this.x + mX) + step;
			if (x < 0 || x > 14) {//不能移动
				return true;
			}
		} else {
			int y = (this.y + mY) + step;
			if (y > 19) {//不能移动
				return true;
			}
		}
		return false;
	}

	//触底判断
	public boolean bottom() {
		if (this.y + mY == 19) {//触到底部
			return true;
		}
		return false;
	}

	//跟其他方块有无碰撞
	public boolean hitBlock(boolean xDir, int step) {
		Block[][] blockStack = panel.blockStack;
		Block bott = null;
		int x = 0;
		int y = 0;
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 20; j++) {
				bott = (Block) blockStack[i][j];
				if (bott != null) {
					if (xDir) {//横向
						x = (this.x + mX) + step;
						y = (this.y + mY);
					} else {
						x = (this.x + mX);
						y = (this.y + mY) + step;
					}
					if (x == bott.x + bott.mX && y == bott.y + bott.mY) {
						return true;
					}
				}
			}
		}
		return false;
	}

	//判断消除情况 -- 某行
	public boolean clear() {
		Block[][] blockStack = panel.blockStack;
		Block bott = null;
		for (int i = 0; i < 15; i++) {
			bott = (Block) blockStack[i][y + mY];
			if (bott == null) {//这行中，有一个是null这不能消除
				return false;
			}
		}
		return true;
	}

	//预变形
	public boolean preRotate() {
		//旋转万能公式 x=-y y=x
		int x = -mY + this.x;
		int y = mX + this.y;
		if (x < 0 || x > 14) {//不能变形
			return false;
		}
		if (y >= 19) {//不能变形
			return false;
		}
		return true;
	}

	//变形
	public void rotate() {
		//旋转万能公式 x=-y y=x
		int x = mX;
		mX = -mY;
		mY = x;
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

	public int getmX() {
		return mX;
	}

	public void setmX(int mX) {
		this.mX = mX;
	}

	public int getmY() {
		return mY;
	}

	public void setmY(int mY) {
		this.mY = mY;
	}
}
