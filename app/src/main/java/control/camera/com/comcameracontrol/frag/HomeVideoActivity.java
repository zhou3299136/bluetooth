package control.camera.com.comcameracontrol.frag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import control.camera.com.comcameracontrol.App;
import control.camera.com.comcameracontrol.R;
import control.camera.com.comcameracontrol.utls.AppUtis;
import control.camera.com.comcameracontrol.utls.ContextUtil;

/**
 * 视频模式
 */
public class HomeVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private View MyView;
    private static HomeVideoActivity self;

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

    public TextView main_frame_video;
    public TextView main_frame_delay;

    public boolean IsabsSelected = false;
    public boolean IsStartSelected = false;
    public boolean IsbasSelected = false;
    public boolean IsShutterSelected = false;
    public boolean IsCourseSelected = false;

    private String smsg = "";    //显示用数据缓存
    boolean bRun = true;
    boolean bThread = false;
    private String fmsg = "";    //保存用数据缓存
    public boolean IsSpeed = false;
    public String speed = "";
    public boolean direction = false;
    private InputStream HomeVideoio;    //输入流，用来接收蓝牙数据

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_video);
        initView();
        InitData();
        onSendButtonClicked(ContextUtil.video);
    }


    public void initView() {
        frag_video_quantity = findViewById(R.id.frag_video_quantity);
        frag_video_quantity_progress = findViewById(R.id.frag_video_quantity_progress);

        video_speed = findViewById(R.id.video_speed);
        frag_video_quantity_speed = findViewById(R.id.frag_video_quantity_speed);

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

        main_frame_video = findViewById(R.id.main_frame_video);
        main_frame_delay = findViewById(R.id.main_frame_delay);

        video_ab.setOnClickListener(this);
        video_start.setOnClickListener(this);
        video_ba.setOnClickListener(this);
        video_shutter.setOnClickListener(this);
        video_course.setOnClickListener(this);
        main_frame_delay.setOnClickListener(this);
    }

    public void InitData() {
        main_frame_video.setSelected(true);
        //打开接收线程
        try {
            HomeVideoio = App.getApp().get_socket().getInputStream();
        } catch (IOException e) {
            Toast.makeText(this, "接收数据失败！", Toast.LENGTH_SHORT).show();
            return;
        }


        frag_video_quantity_speed.setMax(100);
        frag_video_quantity_speed.setProgress(50);

        frag_video_quantity_progress.setMax(100);
        frag_video_quantity_progress.setProgress(100);

        video_speed.setFocusable(true);
        video_speed.setFocusableInTouchMode(true);
        video_speed.requestFocus();

        video_speed.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否是“完成”键
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    speed = v.getText().toString();
                    if (TextUtils.isEmpty(speed)) {
                        Toast.makeText(HomeVideoActivity.this, "速度不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Integer.valueOf(speed) > 100) {
                            Toast.makeText(HomeVideoActivity.this, "最大速度为100，请重新输入", Toast.LENGTH_SHORT).show();
                        } else {
                            IsSpeed = true;
                            onSendButtonClicked(ContextUtil.speed + AppUtis.speedTime(speed) + "#");
                            frag_video_quantity_speed.setProgress(Integer.valueOf(speed));
                            //隐藏软键盘
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (imm.isActive()) {
                                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
        });

    }

    //接收数据线程
    //接收数据线程
    Thread readThread = null;

    //消息处理队列
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("HomeVideoFrag----", "" + smsg);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        readThread = new Thread() {
            public void run() {
                if(this.isInterrupted()){
                    return;
                }
                int num = 0;
                byte[] buffer = new byte[1024];
                byte[] buffer_new = new byte[1024];
                int i = 0;
                int n = 0;
                bRun = true;
                //接收线程
                while (true) {
                    try {
                        while (HomeVideoio.available() == 0) {
                            while (bRun == false) {
                            }
                        }
                        while (true) {
                            if (!bThread)//跳出循环
                                return;
                            num = HomeVideoio.read(buffer);         //读入数据
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
                            smsg = s;   //写入接收缓存
                            Log.e("HomeVideoFrag===", "" + smsg);
                            if (HomeVideoio.available() == 0) break;  //短时间没有数据才跳出进行显示
                        }
                        //发送显示消息，进行显示刷新
                        handler.sendMessage(handler.obtainMessage());
                    } catch (IOException e) {
                    }
                }
            }
        };

        readThread.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        readThread.interrupt();
        handler.removeCallbacks(readThread);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onSendButtonClicked(ContextUtil.speed + "050#");
    }

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
                    direction = true;
                    onSendButtonClicked(ContextUtil.FXAB);
                }
                IsbasSelected = false;
                video_ba_im.setSelected(false);
                break;
            case R.id.video_start:
                if (direction) {
                    if (IsStartSelected) {
                        IsStartSelected = false;
                        video_start_im.setSelected(false);
                        video_start_tv.setText("开始");
                        onSendButtonClicked(ContextUtil.SPTZ);
                    } else {
                        IsStartSelected = true;
                        video_start_im.setSelected(true);
                        video_start_tv.setText("暂停");
                        onSendButtonClicked(ContextUtil.SPQD);
                    }
                } else {
                    Toast.makeText(this, "请选择运动方向", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.video_ba:
                if (IsbasSelected) {
                    IsbasSelected = false;
                    video_ba_im.setSelected(false);
                } else {
                    IsbasSelected = true;
                    video_ba_im.setSelected(true);
                    direction = true;
                    onSendButtonClicked(ContextUtil.FXBA);
                }
                IsabsSelected = false;
                video_ab_im.setSelected(false);
                break;
            case R.id.video_shutter:
                if (IsShutterSelected) {
                    IsShutterSelected = false;
                    video_shutter_im.setSelected(false);
                    onSendButtonClicked(ContextUtil.SPK0);
                    Toast.makeText(this, "快门关闭", Toast.LENGTH_SHORT).show();
                } else {
                    IsShutterSelected = true;
                    video_shutter_im.setSelected(true);
                    onSendButtonClicked(ContextUtil.SPK1);
                    Toast.makeText(this, "快门开启", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.video_course:
                if (IsCourseSelected) {
                    IsCourseSelected = false;
                    video_course_course.setSelected(false);
                    onSendButtonClicked(ContextUtil.SPSD);
                    Toast.makeText(this, "自动返航关", Toast.LENGTH_SHORT).show();
                } else {
                    IsCourseSelected = true;
                    video_course_course.setSelected(true);
                    onSendButtonClicked(ContextUtil.SPZD);
                    Toast.makeText(this, "自动返航开", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.main_frame_delay:
                startActivity(new Intent(this, HomeDelayActivity.class));
                finish();
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
