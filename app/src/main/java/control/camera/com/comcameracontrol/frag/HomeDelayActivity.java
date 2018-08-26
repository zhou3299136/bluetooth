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
import android.view.WindowManager;
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
 * 延时拍照
 */
public class HomeDelayActivity extends AppCompatActivity implements View.OnClickListener ,App.SockeMsg{
    private String smsg = "";    //显示用数据缓存
    boolean bRun = true;
    boolean bThread = false;
    private String fmsg = "";    //保存用数据缓存

    public TextView main_frame_delay;
    public TextView main_frame_video;

    public EditText frag_delay_time_ed;//间隔时间
    public EditText frag_delay_num_ed;//拍摄数量

    public LinearLayout delay_ab;
    public ImageView delay_ab_im;

    public LinearLayout delay_start;
    public ImageView delay_start_im;
    public TextView delay_start_tv;


    public LinearLayout delay_ba;
    public ImageView delay_ba_im;

    public EditText delay_shutter_time;//快门时间
    public EditText frag_delay_distance_ed;

    public ProgressBar frag_delay_quantity_progress;
    public ProgressBar frag_delay_distance_progress;

    public TextView frag_delay_complete_num;
    public boolean IsabsSelected = false;
    public boolean IsStartSelected = false;
    public boolean IsbasSelected = false;
    public boolean direction = false;


    public String delayTime="";
    public String delaynNum="";
    public String distance="";
    public String shutterTime="";

    public boolean isDelayTime=false;
    public boolean isDelaynNum=false;
    public boolean isDistance=false;
    public boolean isShutterTime=false;

    public int amount=0;
    public int completion=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_delay);
        App.getApp().setMonMesgIstener(this);
        initView();
        initData();
        App.getApp().onSendButtonClicked(ContextUtil.MSP);
    }


    public void initView() {
        main_frame_delay = findViewById(R.id.main_frame_delay);
        main_frame_delay.setSelected(true);
        main_frame_video = findViewById(R.id.main_frame_video);
        main_frame_video.setOnClickListener(this);

        frag_delay_time_ed = findViewById(R.id.frag_delay_time_ed);
        frag_delay_num_ed = findViewById(R.id.frag_delay_num_ed);

        delay_ab = findViewById(R.id.delay_ab);
        delay_ab_im = findViewById(R.id.delay_ab_im);

        delay_start = findViewById(R.id.delay_start);
        delay_start_im = findViewById(R.id.delay_start_im);
        delay_start_tv = findViewById(R.id.delay_start_tv);

        delay_ba = findViewById(R.id.delay_ba);
        delay_ba_im = findViewById(R.id.delay_ba_im);

        delay_shutter_time = findViewById(R.id.delay_shutter_time);
        frag_delay_quantity_progress=findViewById(R.id.frag_delay_quantity_progress);
        frag_delay_distance_progress=findViewById(R.id.frag_delay_distance_progress);
        frag_delay_distance_ed=findViewById(R.id.frag_delay_distance_ed);

        frag_delay_complete_num=findViewById(R.id.frag_delay_complete_num);


        delay_ab.setOnClickListener(this);
        delay_start.setOnClickListener(this);
        delay_ba.setOnClickListener(this);


        frag_delay_quantity_progress.setMax(100);
        frag_delay_quantity_progress.setProgress(100);
        frag_delay_distance_progress.setMax(100);
    }


    public void initData() {


        frag_delay_distance_ed.setFocusable(true);
        frag_delay_distance_ed.setFocusableInTouchMode(true);
        frag_delay_distance_ed.requestFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        /**
         * 步距离
         */
        frag_delay_distance_ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否是“完成”键
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    distance = v.getText().toString();
                    if (TextUtils.isEmpty(distance)){
                        Toast.makeText(HomeDelayActivity.this, "步距离不能为空", Toast.LENGTH_SHORT).show();
                    }else {
                        isDistance=true;
                        App.getApp().onSendButtonClicked(ContextUtil.SYBJ + distance + "#");
                        frag_delay_time_ed.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });

        /**
         * 间隔时间
         */
        frag_delay_time_ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否是“完成”键
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    delayTime = v.getText().toString();
                    if (TextUtils.isEmpty(delayTime)){
                        Toast.makeText(HomeDelayActivity.this, "间隔时间不能为空", Toast.LENGTH_SHORT).show();
                    }else {
                        isDelayTime=true;
                        App.getApp().onSendButtonClicked(ContextUtil.SYJG + AppUtis.speedTime(delayTime) + "#");
                        frag_delay_num_ed.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });

        /**
         * 拍摄数量
         */
        frag_delay_num_ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否是“完成”键
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    delaynNum = v.getText().toString();
                    if (TextUtils.isEmpty(delaynNum)){
                        Toast.makeText(HomeDelayActivity.this, "拍摄数量不能为空", Toast.LENGTH_SHORT).show();
                    }else {
                        isDelaynNum=true;
                        amount=Integer.valueOf(delaynNum);
                        App.getApp().onSendButtonClicked(ContextUtil.SYZS + AppUtis.SykDelaynNum(delaynNum) + "#");
                        delay_shutter_time.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });


        /**
         * 快门时间
         */
        delay_shutter_time.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否是“完成”键
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    shutterTime = v.getText().toString();
                    if (TextUtils.isEmpty(shutterTime)){
                        Toast.makeText(HomeDelayActivity.this, "快门时间不能为空", Toast.LENGTH_SHORT).show();
                    }else {
                        isShutterTime=true;
                        App.getApp().onSendButtonClicked(ContextUtil.SYKS + AppUtis.shutterTime(shutterTime) + "#");
                        //隐藏软键盘
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm.isActive()) {
                            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                        }
                        return true;
                    }
                }
                return false;
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_frame_video:
                startActivity(new Intent(this, HomeVideoActivity.class));
                finish();
                break;
            case R.id.delay_ab:
                if (IsabsSelected) {
                    delay_ab_im.setSelected(false);
                    IsabsSelected = false;
                } else {
                    delay_ab_im.setSelected(true);
                    IsabsSelected = true;
                    direction = true;
                    App.getApp().onSendButtonClicked(ContextUtil.FXAB);
                }
                IsbasSelected = false;
                delay_ba_im.setSelected(false);
                break;
            case R.id.delay_start:

                if (!direction) {
                        Toast.makeText(this, "请选择运动方向", Toast.LENGTH_SHORT).show();
                }else if (!isDistance){
                    Toast.makeText(this, "请输入步距值", Toast.LENGTH_SHORT).show();
                }else if (!isDelayTime){
                    Toast.makeText(this, "请输入间隔时间", Toast.LENGTH_SHORT).show();
                }else if (!isDelaynNum){
                    Toast.makeText(this, "请输入拍摄张数", Toast.LENGTH_SHORT).show();
                }else if (!isShutterTime){
                    Toast.makeText(this, "请输入按下快门时间", Toast.LENGTH_SHORT).show();
                }else {
                    if (IsStartSelected) {
                        IsStartSelected = false;
                        delay_start_im.setSelected(false);
                        delay_start_tv.setText("开始");
                        App.getApp().onSendButtonClicked(ContextUtil.SYTZ);
                    } else {
                        IsStartSelected = true;
                        delay_start_im.setSelected(true);
                        delay_start_tv.setText("暂停");
                        App.getApp().onSendButtonClicked(ContextUtil.SYQD);
                    }
                }
                break;
            case R.id.delay_ba:
                if (IsbasSelected) {
                    IsbasSelected = false;
                    delay_ba_im.setSelected(false);
                } else {
                    IsbasSelected = true;
                    delay_ba_im.setSelected(true);
                    direction = true;
                    App.getApp().onSendButtonClicked(ContextUtil.FXBA);
                }
                IsabsSelected = false;
                delay_ab_im.setSelected(false);
                break;

        }
    }


    @Override
    public void onMessAge(String message) {
        if (message.contains("SYWC")){
            String num=message.substring(4,message.length());
            num.replace("#","");
            frag_delay_complete_num.setText(""+num);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApp();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private long mExitTime = 0;
    private void exitApp() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            try {
                App.getApp().getIsInStre().close();
                App.getApp().get_socket().close();
                App.getApp().set_socket(null);
                App.getApp().setMonMesgIstener(null);
            } catch (IOException e) {
            }
            finish();
            System.exit(0);
        }
    }
}
