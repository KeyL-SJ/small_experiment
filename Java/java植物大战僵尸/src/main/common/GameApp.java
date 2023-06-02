package main.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class GameApp {
    public static BufferedImage getImg(String path){
        //用try方法捕获异常
        try {
            //io流，输送数据的管道
            BufferedImage img = ImageIO.read(GameApp.class.getResource(path));
            return img;
        }
        //异常处理，打印异常
        catch (IOException e) {
            e.printStackTrace();
        }
        //没找到则返回空
        return null;
    }

    public static ImageIcon getImg2(String path){
        
        //用try方法捕获异常
        try {
        	BufferedImage img = ImageIO.read(GameApp.class.getResource(path));
            return new ImageIcon(img);
        }
        //异常处理，打印异常
        catch (IOException e) {
            e.printStackTrace();
        }
        //没找到则返回空
        return null;
    }
    
    //将指定的图片加载成map的形式返回(BufferedImage)
    public static HashMap getImageMapByIcon(String path,List nameList) {
    	HashMap target = new HashMap();
    	BufferedImage img= null;
    	String name="";
    	String key="";
    		for(int i=0;i<nameList.size();i++){
    			name = (String)nameList.get(i);
    			key = name.split("\\.")[0];
    			img = GameApp.getImg(path+nameList.get(i));
    			target.put(key, img);
    	}
    	return target;
	}
    
    //将指定的图片加载成map的形式返回(ImageIcon)
    public static HashMap getImageMapByIcon2(String path,List nameList) {
    	HashMap target = new HashMap();
    	ImageIcon img= null;
    	String name="";
    	String key="";
    		for(int i=0;i<nameList.size();i++){
    			name = (String)nameList.get(i);
    			key = name.split("\\.")[0];
    			img = GameApp.getImg2(path+nameList.get(i));
    			target.put(key, img);
    	}
    	return target;
	}
}
