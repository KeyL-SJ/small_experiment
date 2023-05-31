package service;

import java.io.FileNotFoundException;

public class Sound {
    static final String DIR ="music/";//音乐文件夹
    static final String BACKGROUND = "background.wav";//背景音乐
    static final String JUMP = "jump.wav";//跳跃音效
    static final String HIT = "hit.wav";//撞击音效

    //播放跳跃音效
    public static void Jump(){
        play(DIR+JUMP,false);//播放一次跳跃音效
    }

    //播放撞击音效
    public static void hit(){
        play(DIR+HIT,false);//播放一次撞击音效
    }

    //播放背景音乐
    public static void background(){
        play(DIR+BACKGROUND,true);
    }

    /**
     * 播放
     * file 音乐文件完整名称
     * circulate 是否循环播放
     */
    private static void play(String file, boolean circulate){
        //创建播放器
        MusicPlayer player = null;
        try {
            player = new MusicPlayer(file,circulate);
            player.play();//播放器开始播放
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
