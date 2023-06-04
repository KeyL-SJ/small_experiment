package common;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MusicPlayer {
	private Clip clip ;
	
	public MusicPlayer(String path){
		AudioInputStream audio;
		try {
			URL url = MusicPlayer.class.getResource(path);
			audio = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audio);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	//type  -1循环播放  0播放一次  正整数播放输入值 +1 次，比如传如2 则播放3次 
	public void loop(int type) {
		clip.loop(type);
	}
	//播放一次
	public void play() {
		clip.start();
	}
	//停止
	public void stop() {
		clip.stop();
	}
}
