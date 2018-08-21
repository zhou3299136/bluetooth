package control.camera.com.comcameracontrol.frag;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 视频模式
 */
import java.io.IOException;
import java.io.OutputStream;
import control.camera.com.comcameracontrol.App;
import control.camera.com.comcameracontrol.R;
import control.camera.com.comcameracontrol.utls.ContextUtil;

public class HomeVideoFrag extends AppCompatActivity implements View.OnClickListener {

    private View MyView;
    private static HomeVideoFrag self;

    public TextView frag_video_quantity;
    public ProgressBar frag_video_quantity_progress;

    public EditText video_speed;
    public ProgressBar frag_video_quantity_speed;

    public LinearLayout video_ab;
    public ImageView video_ab_im;

    public LinearLayout video_start;
    public ImageView video_start_im;
    public TextView video_start_tv;

    public LinearLayout video_ba;
    public ImageView video_ba_im;

    public LinearLayout video_shutter;
    public ImageView video_shutter_im;

    public LinearLayout video_course;
    public ImageView video_course_course;

    public boolean IsabsSelected = false;
    public boolean IsStartSelected = false;
    public boolean IsbasSelected = false;
    public boolean IsShutterSelected = false;
    public boolean IsCourseSelected = false;

    private String smsg = "";    //显示用数据缓存
    boolean bRun = true;
    boolean bThread = false;
    private String fmsg = "";    //保存用数据缓存

//    public static HomeVideoFrag getInstance() {
//        if (self == null) {
//            self = new HomeVideoFrag();
//        }
//        return self;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_video);
        initView();
        InitData();
        onSendButtonClicked(ContextUtil.handshake);
//        onSendButtonClicked(ContextUtil.handshake);
    }

    //    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        MyView = inflater.inflate(R.layout.frag_video, container, false);
//        return MyView;
//    }

//    @Override
////    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
////        super.onViewCreated(view, savedInstanceState);
////        initView();
////        InitData();
//////        onSendButtonClicked(ContextUtil.handshake);
////    }



    public void initView() {
        frag_video_quantity = findViewById(R.id.frag_video_quantity);
        frag_video_quantity_progress = findViewById(R.id.frag_video_quantity_progress);

        video_speed = findViewById(R.id.video_speed);
        frag_video_quantity_speed =findViewById(R.id.frag_video_quantity_speed);

        video_ab = findViewById(R.id.video_ab);
        video_ab_im = findViewById(R.id.video_ab_im);

        video_start = findViewById(R.id.video_start);
        video_start_im = findViewById(R.id.video_start_im);
        video_start_tv = findViewById(R.id.video_start_tv);

        video_ba = findViewById(R.id.video_ba);
        video_ba_im = findViewById(R.id.video_ba_im);

        video_shutter = findViewById(R.id.video_shutter);
        video_shutter_im = findViewById(R.id.video_shutter_im);

        video_course = findViewById(R.id.video_course);
        video_course_course = findViewById(R.id.video_course_course);

        video_ab.setOnClickListener(this);
        video_start.setOnClickListener(this);
        video_ba.setOnClickListener(this);
        video_shutter.setOnClickListener(this);
        video_course.setOnClickListener(this);

    }

    public void InitData() {

        if (bThread == false) {
            readThread.start();
            bThread = true;
        } else {
            bRun = true;
        }


    }

    //接收数据线程
    //接收数据线程
    Thread readThread = new Thread() {

        public void run() {
            int num = 0;
            byte[] buffer = new byte[1024];
            byte[] buffer_new = new byte[1024];
            int i = 0;
            int n = 0;
            bRun = true;
            //接收线程
            while (true) {
                try {
//                    while (is.available() == 0) {
//                        while (bRun == false) {
//                        }
//                    }
                    while (App.getApp().getIsInStre().available() == 0) {
                        while (bRun == false) {
                        }
                    }
                    while (true) {
                        if (!bThread)//跳出循环
                            return;
//                        num = is.read(buffer);         //读入数据
                        num = App.getApp().getIsInStre().read(buffer);
                        n = 0;
                        String s0 = new String(buffer, 0, num);
                        fmsg += s0;    //保存收到数据
                        for (i = 0; i < num; i++) {
                            if ((buffer[i] == 0x0d) && (buffer[i + 1] == 0x0a)) {
                                buffer_new[n] = 0x0a;
                                i++;
                            } else {
                                buffer_new[n] = buffer[i];
                            }
                            n++;
                        }
                        String s = new String(buffer_new, 0, n);
                        smsg += s;   //写入接收缓存
//                        if (is.available() == 0) break;  //短时间没有数据才跳出进行显示
                        if (App.getApp().getIsInStre().available() == 0) break;  //短时间没有数据才跳出进行显示
                    }
                    //发送显示消息，进行显示刷新
                    handler.sendMessage(handler.obtainMessage());
                } catch (IOException e) {
                }
            }
        }
    };

    //消息处理队列
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("this",""+smsg);
        }
    };



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_ab:
                if (IsabsSelected) {
                    video_ab_im.setSelected(false);
                    IsabsSelected = false;
                } else {
                    video_ab_im.setSelected(true);
                    IsabsSelected = true;
                    onSendButtonClicked(ContextUtil.ADW);
                }
                IsbasSelected = false;
                video_ba_im.setSelected(false);
                break;
            case R.id.video_start:
                if (IsStartSelected) {
                    IsStartSelected = false;
                    video_start_im.setSelected(false);
                    video_start_tv.setText("开始");
                } else {
                    IsStartSelected = true;
                    video_start_im.setSelected(true);
                    video_start_tv.setText("暂停");
                }
                break;
            case R.id.video_ba:
                if (IsbasSelected) {
                    IsbasSelected = false;
                    video_ba_im.setSelected(false);
                } else {
                    IsbasSelected = true;
                    video_ba_im.setSelected(true);
                    onSendButtonClicked(ContextUtil.BDW);
                }
                IsabsSelected = false;
                video_ab_im.setSelected(false);
                break;
            case R.id.video_shutter:
                if (IsShutterSelected) {
                    IsShutterSelected = false;
                    video_shutter_im.setSelected(false);
                } else {
                    IsShutterSelected = true;
                    video_shutter_im.setSelected(true);
                }
                break;
            case R.id.video_course:
                if (IsCourseSelected) {
                    IsCourseSelected = false;
                    video_course_course.setSelected(false);
                } else {
                    IsCourseSelected = true;
                    video_course_course.setSelected(true);
                }
                break;
        }
    }

    public void onSendButtonClicked(String type) {
        int i = 0;
        int n = 0;
        try {
            OutputStream os = App.getApp().get_socket().getOutputStream();   //蓝牙连接输出流
            byte[] bos = type.getBytes();
            for (i = 0; i < bos.length; i++) {
                if (bos[i] == 0x0a) n++;
            }
            byte[] bos_new = new byte[bos.length + n];
            n = 0;
            for (i = 0; i < bos.length; i++) { //手机中换行为0a,将其改为0d 0a后再发送
                if (bos[i] == 0x0a) {
                    bos_new[n] = 0x0d;
                    n++;
                    bos_new[n] = 0x0a;
                } else {
                    bos_new[n] = bos[i];
                }
                n++;
            }
            os.write(bos_new);
        } catch (IOException e) {
        }
    }

}
