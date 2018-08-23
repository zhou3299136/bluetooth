package control.camera.com.comcameracontrol.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import control.camera.com.comcameracontrol.R;

/**
 * 延时拍照
 */
public class HomeDelayFrag extends AppCompatActivity implements View.OnClickListener{

public TextView main_frame_delay;
public TextView main_frame_video;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_delay);
        initView();
        initData();
    }


    public void initView(){
        main_frame_delay=findViewById(R.id.main_frame_delay);
        main_frame_delay.setSelected(true);
        main_frame_video=findViewById(R.id.main_frame_video);
        main_frame_video.setOnClickListener(this);
    }


    public void initData() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_frame_video:
                startActivity(new Intent(this,HomeVideoFrag.class));
                finish();
                break;

        }
    }
}
