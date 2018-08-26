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
public class HomeVideoActivity extends AppCompatActivity implements View.OnClickListener, App.SockeMsg {


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
    public boolean IsSpeed = false;
    public String speed = "";
    public boolean direction = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_video);
        App.getApp().setMonMesgIstener(this);
        initView();
        InitData();
        App.getApp().onSendButtonClicked(ContextUtil.video);
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
                            App.getApp().onSendButtonClicked(ContextUtil.speed + AppUtis.speedTime(speed) + "#");
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


    @Override
    protected void onResume() {
        super.onResume();
//        App.getApp().onSendButtonClicked(ContextUtil.speed + "050#");
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
                    App.getApp().onSendButtonClicked(ContextUtil.FXAB);
                }
                IsbasSelected = false;
                video_ba_im.setSelected(false);
                break;
            case R.id.video_start:
                if (!direction) {
                    Toast.makeText(this, "请选择运动方向", Toast.LENGTH_SHORT).show();
                }else if (!IsSpeed){
                    Toast.makeText(this, "请设置速度", Toast.LENGTH_SHORT).show();
                } else {
                    if (IsStartSelected) {
                        IsStartSelected = false;
                        video_start_im.setSelected(false);
                        video_start_tv.setText("开始");
                        App.getApp().onSendButtonClicked(ContextUtil.SPTZ);
                    } else {
                        IsStartSelected = true;
                        video_start_im.setSelected(true);
                        video_start_tv.setText("暂停");
                        App.getApp().onSendButtonClicked(ContextUtil.SPQD);
                    }
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
                    App.getApp().onSendButtonClicked(ContextUtil.FXBA);
                }
                IsabsSelected = false;
                video_ab_im.setSelected(false);
                break;
            case R.id.video_shutter:
                if (IsShutterSelected) {
                    IsShutterSelected = false;
                    video_shutter_im.setSelected(false);
                    App.getApp().onSendButtonClicked(ContextUtil.SPK0);
                    Toast.makeText(this, "快门关闭", Toast.LENGTH_SHORT).show();
                } else {
                    IsShutterSelected = true;
                    video_shutter_im.setSelected(true);
                    App.getApp().onSendButtonClicked(ContextUtil.SPK1);
                    Toast.makeText(this, "快门开启", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.video_course:
                if (IsCourseSelected) {
                    IsCourseSelected = false;
                    video_course_course.setSelected(false);
                    App.getApp().onSendButtonClicked(ContextUtil.SPSD);
                    Toast.makeText(this, "自动返航关", Toast.LENGTH_SHORT).show();
                } else {
                    IsCourseSelected = true;
                    video_course_course.setSelected(true);
                    App.getApp().onSendButtonClicked(ContextUtil.SPZD);
                    Toast.makeText(this, "自动返航开", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.main_frame_delay:
                startActivity(new Intent(this, HomeDelayActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onMessAge(String message) {
        Log.e("HomeVideoActivity", "" + message);
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
            return;
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
