package main.plants;

import java.awt.Graphics;

public abstract class Plant {
	public abstract void draw(Graphics g);
	abstract void waggle();
	abstract void shoot();
	public abstract void clear();
	public abstract void plant(PlantsRect rect);
	public abstract void addZombie(Zombie z);
	
	public abstract void setX(int x);
	public abstract int getX();
	public abstract void setY(int y);
	public abstract int getIndex();
	public abstract int getWidth() ;
	public abstract int getHeight();
	public abstract void setHp(int hp);
	public abstract int getHp();
	public abstract void setAlive(boolean bool);
	public abstract int getCost() ;
	public abstract void setCost(int cost);
	public abstract void noteZombie();
}


