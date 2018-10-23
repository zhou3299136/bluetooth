package control.camera.com.comcameracontrol.utls;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class AppUtis {


    public static String speedTime(String speedTime)
    {
        int time=Integer.valueOf(speedTime);
        String speed="";
        if (time<100&&time>10){
            speed="0"+speedTime;
        } else if (time<10){
            speed="00"+speedTime;
        }else {
            speed=""+speedTime;
        }
        return speed;
    }

    public static String SykDelaynNum(String speedTime){
        int time=Integer.valueOf(speedTime);
        String speed = "";
        if (time<100&&time>=10){
            speed="000"+speedTime;
        }else if (time<10){
            speed="0000"+speedTime;
        }else {
            speed="00"+speedTime;
        }
        return speed;
    }

    public static String shutterTime(String speedTime){
        double time=Double.valueOf(speedTime);
        String speed="";
        if (time<100&&time>10){
            speed="0"+speedTime;
        } else if (time<10){
            speed="00"+speedTime;
        }else {
            speed=""+speedTime;
        }
        return speed;
    }

    public static String  formatTo￥_Ceiling(double floatNo) {
        DecimalFormat format = new DecimalFormat("##0.0000");
        format.setRoundingMode(RoundingMode.CEILING);
        DecimalFormat format2 = new DecimalFormat("##0.00");
        return format2.format(Double.valueOf(format.format(floatNo)));
    }

    public static String  formatTo￥(double floatNo) {
        DecimalFormat format = new DecimalFormat("##0");
        format.setRoundingMode(RoundingMode.CEILING);
        DecimalFormat format2 = new DecimalFormat("##0");
        return format2.format(Double.valueOf(format.format(floatNo)));
    }



}
