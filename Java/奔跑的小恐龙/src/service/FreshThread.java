package service;

import view.GamePanel;
import view.MainFrame;
import view.ScoreDialog;

import java.awt.*;

public class FreshThread extends Thread{
    public static final int FREASH =20;//刷新时间
    GamePanel p ;//游戏面板
    public FreshThread(GamePanel p){
        this.p = p;
    }
    public void run(){
        while(!p.isFinish()){
            p.repaint();
            try {
                Thread.sleep(FREASH);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Container c = p.getParent();
        while(!(c instanceof MainFrame)) {
            c = c.getParent();
        }
        MainFrame frame =(MainFrame) c;
        new ScoreDialog(frame);
        frame.restart();
    }
}
