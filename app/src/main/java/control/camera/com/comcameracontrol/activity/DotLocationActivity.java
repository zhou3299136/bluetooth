package control.camera.com.comcameracontrol.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import control.camera.com.comcameracontrol.App;
import control.camera.com.comcameracontrol.R;
import control.camera.com.comcameracontrol.frag.HomeVideoActivity;
import control.camera.com.comcameracontrol.utls.ContextUtil;

public class DotLocationActivity extends Activity implements View.OnClickListener, App.SockeMsg {
    public TextView do_location_A;
    public TextView do_location_A_ok;

    public TextView do_location_B;
    public TextView do_location_B_ok;

    public TextView do_location_ok;


    public boolean IsADW = false;
    public boolean IsADWOK = false;
    public boolean IsBDW = false;
    public boolean IsBDWOK = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_location);
        initView();
        initData();
    }

    public void initView() {
        do_location_A = findViewById(R.id.do_location_A);
        do_location_A_ok = findViewById(R.id.do_location_A_ok);
        do_location_B = findViewById(R.id.do_location_B);
        do_location_B_ok = findViewById(R.id.do_location_B_ok);
        do_location_ok = findViewById(R.id.do_location_ok);
        do_location_A.setOnClickListener(this);
        do_location_A_ok.setOnClickListener(this);
        do_location_B.setOnClickListener(this);
        do_location_B_ok.setOnClickListener(this);
        do_location_ok.setOnClickListener(this);
    }

    public void initData() {
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.do_location_A:
                IsADW = true;
                App.getApp().onSendButtonClicked(ContextUtil.ADW);
                do_location_A.setSelected(true);
                break;
            case R.id.do_location_A_ok:
                if (IsADW) {
                    IsADWOK = true;
                    App.getApp().onSendButtonClicked(ContextUtil.ADWOK);
                    do_location_A_ok.setSelected(true);
                } else {
                    Toast.makeText(this, "请先定位A点", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.do_location_B:
                if (IsADW && IsADWOK) {
                    IsBDW = true;
                    App.getApp().onSendButtonClicked(ContextUtil.BDW);
                    do_location_B.setSelected(true);
                } else {
                    Toast.makeText(this, "请先定位A点", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.do_location_B_ok:
                if (IsBDW) {
                    IsBDWOK = true;
                    App.getApp().onSendButtonClicked(ContextUtil.BDWOK);
                    do_location_B_ok.setSelected(true);
                } else {
                    Toast.makeText(this, "请先定位B点", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.do_location_ok:
                if (IsADWOK && IsBDWOK) {
                    startActivity(new Intent(this, HomeVideoActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "请先完成AB点定位", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        App.getApp().setMonMesgIstener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onMessAge(String message) {
        Log.e("DotLocationActivity", "" + message);
        Log.e("this",""+App.getApp().getISdunakai());
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
