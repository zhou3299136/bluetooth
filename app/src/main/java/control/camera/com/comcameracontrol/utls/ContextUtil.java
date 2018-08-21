package control.camera.com.comcameracontrol.utls;


public class ContextUtil {

    /**
     * 通用串口命令
     */
    public static String handshake = "$WSReady#";//握手
    public static String ADW = "$ADW#";//A点定位
    public static String BDW = "$BDW#";//B点定位
    public static String DL = "$DL#";//电量
    public static String speed="$SPSP";//速度;速度设置
    public static  String FXAB="$FXAB#";//A到B
    public  static String FXBA="$FXBA#";//B到A

    /**
     * 视频模式
     */
    public static String video="$MSV#";
    public static String SPZD="$SPZD#";//视频自动往返
    public static  String SPSD="$SPSD#";//视频手动返航
    public static  String SPK1="$SPK1#";//快门开启
    public static  String SPK0="$SPK0#";//快门关闭
    public static  String SPQD="$SPQD#";//视频启动
    public static  String SPTZ="$SPTZ#";//视频停止


    /**
     * 延时拍摄
     */
    public static  String MSP="$MSP#";//延时摄影模式
    public static  String SYQD="$SYQD#";//视频启动
    public static  String SYTZ="$SYTZ#";//视频停止
    public static String SYBJ="$SYBJ#";//摄影步距值
    public static  String SYJG="$SYJG#";//摄影间隔值
    public static  String SYZS="$SYZS#";//拍摄张数值
    public static  String SYKS="$SYKS#";//快门时间数值

    public static String ADDRESS="";
    public final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP服务UUID号



}
