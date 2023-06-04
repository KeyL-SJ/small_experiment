package main;

import java.awt.Graphics;

public abstract class Plane {
	public abstract void move();
	public abstract void draw(Graphics g);
	public abstract void boom();
	public abstract void clear();
	
	abstract int getX();
	abstract int getY();
	abstract int getWidth();
	abstract int getHeight();
}
