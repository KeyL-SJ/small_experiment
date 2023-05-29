package common;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageValue {
    public static BufferedImage whiteQiziImage  ;
    
    public static BufferedImage blackQiziImage  ;
    //路径
    public static String ImagePath = "/images/";
    //将图片初始化
    public static void init(){
    	String whitePath = ImagePath +"white.png";
    	String blackPath = ImagePath +"black.png";
    	try {
			whiteQiziImage = ImageIO.read(ImageValue.class.getResource(whitePath));
			blackQiziImage = ImageIO.read(ImageValue.class.getResource(blackPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
