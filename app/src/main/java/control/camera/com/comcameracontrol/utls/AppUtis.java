package control.camera.com.comcameracontrol.utls;

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


}
