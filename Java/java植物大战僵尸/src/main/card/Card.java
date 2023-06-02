package main.card;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.common.MusicPlayer;
import main.plants.Plant;
import main.plants.SunPlant;
import main.plants.WandouPlant;
import main.plants.wallNutPlant;

public class Card {
	private int x = 0;//x坐标
	private int y = 0;//y坐标
	private int width = 0;//宽
	private int height = 0;//高
	private BufferedImage image = null;//图片对象
	private int cost = 0;//阳光花费
	private String type = "";//类型,可以通过类型来判断创建什么植物
	private GamePanel panel=null;
	private int index=0;

	public Card(int x, int y, int width, int height, BufferedImage image,String type,int cost, int index ,GamePanel panel) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.image = image;
		this.type=type;
		this.cost=cost;
		this.index=index;
		this.panel=panel;
	}

	public void draw(Graphics g) {
		g.drawImage(image, x, y, width, height, null);
	}
	
	//检查坐标是否图形范围内
	public boolean isPoint(int x,int y){
		if(x>this.x && y>this.y && x<this.x+this.width && y < this.y + this.height){
			return true;
		}
		return false;
	}

	//在对应的坐标出创建对应的植物
	public void createPlant(int x, int y,int cost) {
		Plant plant = null;
		if("sun".equals(type)){//创建太阳植物
			plant = new SunPlant(x-31, y-36, 62,72, panel);
		}else  if("wandou".equals(type)){//创建豌豆植物
			plant = new WandouPlant(x-31, y-35, 63,70, panel);
		}else  if("wallNut".equals(type)){//创建胡桃植物
			plant = new wallNutPlant(x-31, y-35, 61,71, panel);
		}
		plant.setCost(cost);
		panel.curPlant=plant;
		panel.plants.add(plant);
	}
	
	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
}