package main.plants;

import java.awt.Graphics;

public abstract class Zombie {

	public abstract void draw(Graphics g);
	abstract void move();
	abstract void eat();
	abstract void dead();
	abstract void noteZombie();
	
	public abstract int getIndex();
	public abstract void setIndex(int index);
	public abstract int getHp() ;
	public abstract void setHp(int hp);
	public abstract int getX() ;
	public abstract void setAlive(boolean alive) ;
	public abstract boolean isAlive();
}
