package service;

import java.io.*;
import java.util.Arrays;

//分数记录
public class ScoreRecorder {
    private static final String SCOREFILE = "data/source";//得分记录文件
    private static int scores[] =new int[3];//当前得分最高的前三名

    /**
     * 分数初始化
     */
    public static void init(){
        File f = new File(SCOREFILE);//创建记录文件对象
        if (!f.exists()){
            try {
                f.createNewFile();//如果文件不存在，则创建新文件
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(f);//文件字节输入流
            isr = new InputStreamReader(fis);//字节流转字符流
            br = new BufferedReader(isr);//缓冲字符流
            String value =br.readLine();//读取一行
            if(!(value == null || "".equals(value))){
                String vs[] =value.split(",");//分割字符串
                if(vs.length < 3){
                    Arrays.fill(scores,0);//数组填充0
                }else{
                    for (int i = 0; i < 3; i++) {
                        //将记录文件中的值付给当前分数数组
                        scores[i] = Integer.parseInt(vs[i]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //保存分数
    public static void saveScore(){
        //拼接得分数组
        String value = scores[0]+","+scores[1]+","+scores[2];
        FileOutputStream fos =null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(SCOREFILE);//文件字节输出流
            osw = new OutputStreamWriter(fos);//字节流转字符流
            bw = new BufferedWriter(osw);//缓冲字符流
            bw.write(value);//写入拼接后的字符串
            bw.flush();//字符流刷新
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加分数。如果新添加的分数比排行榜分数高，则会将新分数记入排行榜
     * @return score
     */
    public static void addNewScore(int score){
        //在得分组基础上创建一个长度为4的临时数组
        int[] temp=Arrays.copyOf(scores,4);
        temp[3] = score;//将新分数赋值给第四个元素
        Arrays.sort(temp);//临时数组降序排序
        scores = Arrays.copyOfRange(temp,1,4);//将后三个元素赋值给得分组
    }

    public static int[] getScores() {
        return scores;
    }
}
