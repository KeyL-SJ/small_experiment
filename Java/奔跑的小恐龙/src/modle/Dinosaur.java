package modle;

import service.FreshThread;
import service.Sound;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 恐龙类
 *
 */
public class Dinosaur {
    public BufferedImage image;//主图片
    private BufferedImage image1,image2,image3;//恐龙跑步
    public int x,y;//坐标
    private int jumpValue = 0;//跳跃的增变量
    private boolean jumpState = false;//跳跃状态，为true则跳
    private int stepTimer = 0;//踏步计时器
    private final int JUMP_HIGHT = 100;//跳起最大高度
    private final int LOWEST_Y = 120;//落地最低坐标
    private final int FREASH = FreshThread.FREASH;//刷新时间

    public Dinosaur(){
        x=50;
        y=LOWEST_Y;
        try {
            image1 = ImageIO.read(new File("image/恐龙1.png"));
            image2 = ImageIO.read(new File("image/恐龙2.png"));
            image3 = ImageIO.read(new File("image/恐龙3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void move(){
        step();//不断踏步
        if(jumpState){//如果正在跳跃
            if(y>=LOWEST_Y) {//如果纵坐标大于等于最低点
                jumpValue = -4;
            }
            if(y<=LOWEST_Y - JUMP_HIGHT){//如果跳过最高点
                jumpValue = 4;//增量变为正值
            }
            y+=jumpValue;//纵坐标发生变化
            if(y>=LOWEST_Y){
                jumpState = false;//停止跳跃
            }
        }
    }

    public void jump() {
        if (!jumpState) {
            Sound.Jump();
        }
        jumpState = true;
    }


    private void step() {
        //每过250毫秒，更换一张图片。因为共有3图片，所以除以3取余，轮流展示这三张
        int temp = stepTimer / 250 % 3;
        switch (temp) {
            case 1:
                image = image1;
                break;
            case 2:
                image = image2;
                break;
            default:
                image = image3;
        }
        stepTimer += FREASH;//计时器递增
    }

    /**
     * 足部的边界
     */
    public Rectangle getFootBounds(){
        return new Rectangle(x+30,y+59,29,18);
    }

    /**
     * 头部边界区域
     */
    public Rectangle getHeadBounds(){
        return new Rectangle(x+66,y+25,32,22);
    }
}
