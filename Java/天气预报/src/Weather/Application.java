package Weather;

import com.imooc.weather.HourWeather;
import com.imooc.weather.WeatherUtils;
import com.imooc.weather.impl.WeatherUtilsImpl;

import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        System.out.println("查询最近天气预报：");
        System.out.println("输入1：查询未来24小时天气预报");
        System.out.println("输入2：查询未来3天的天气预报");
        System.out.println("输入3：查询未来7天的天气预报");
        Scanner sc = new Scanner(System.in);
        int i = sc.nextInt();
        System.out.println("用户输入数字："+i);
        if(i==1){
            System.out.print("请输入城市名称，查询未来24小时天气预报");
            String city = sc.next();
            WeatherUtils weatherUtils = new WeatherUtilsImpl();
            List<HourWeather> weatherList =weatherUtils.w24h("a72fa4abd8bb45e48045d081cddec3be",city);
            System.out.println(weatherList);
            if(weatherList.size()==0) {
                System.out.println("没有找到城市天气");
            }else{
                for(HourWeather hourWeather:weatherList){
                    String temp= "%s月%s日%s时|%-3s|%-20s|%-8s|%-4s℃";
                    String row = String.format(temp,new String[]{
                       hourWeather.getMonth(),
                       hourWeather.getDay(),
                       hourWeather.getHour(),
                       hourWeather.getWindDirection(),
                       hourWeather.getWindPower(),
                       hourWeather.getWeather(),
                       hourWeather.getTemperature()
                    });
                    System.out.println(row);
                }
            }
        }
    }
}
