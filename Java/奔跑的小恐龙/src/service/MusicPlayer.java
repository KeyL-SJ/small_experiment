package service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MusicPlayer implements Runnable{
    File soundFile;//音乐文件
    Thread thread;//父线程
    boolean circulate;//是否循环播放

    /**
     * 构造方法，默认不循环播放
     * filepath:音乐文件的完整名称
     *
     */
    public MusicPlayer(String filepath) throws FileNotFoundException{
        this(filepath,false);
    }

    /**
     * 构造方法
     * @param filepath 音乐文件完整名称
     * @param circulate 是否循环播放
     */
    public MusicPlayer(String filepath, boolean circulate) throws FileNotFoundException{
        this.circulate = circulate;
        soundFile = new File(filepath);
        if(!soundFile.exists()){
            throw new FileNotFoundException(filepath+"未找到");
        }
    }
    /**
     * 播放
     */
    public void play(){
        thread = new Thread(this);//创建线程对象
        thread.start();//开启线程
    }

    /**
     * 停止播放
     */
    public void stop(){
        thread.stop();//强制关闭线程
    }

    /**
     * 重写线程执行方法
     */
    @Override
    public void run() {
        byte[] auBuffer = new byte[1024 * 128];//创建129k缓冲区
        do {
            AudioInputStream audioInputStream = null;
            SourceDataLine auline = null;//混频器源数据行
            try {
                audioInputStream = AudioSystem.getAudioInputStream(soundFile);
                AudioFormat format = audioInputStream.getFormat();//获取音频格式
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                //利用音频系统类获得与指定Line.Info对象中的描述匹配的行，并转换为源数据行对象
                auline = (SourceDataLine) AudioSystem.getLine(info);
                auline.open(format);//按照指定的格式打开源数据行
                auline.start();//源数据行开启读写活动
                int byteCount = 0;//记录音频输入流读出的字节书
                while (byteCount != -1) {//如果音频输入流中读取的字节数不为-1
                    //从音频数据流中读出128k的数据
                    byteCount = audioInputStream.read(auBuffer, 0, auBuffer.length);
                    if (byteCount >= 0) {//如果读出有效数据
                        auline.write(auBuffer, 0, byteCount);//将有效数据写入数据行中
                    }
                }
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } finally {
                auline.drain();//清空数据行
                auline.close();//关闭数据行
            }
        } while (circulate);//根据循环标志判断是否循环播放
    }
}