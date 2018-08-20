package control.camera.com.comcameracontrol.frag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 视频模式
 */
import control.camera.com.comcameracontrol.R;

public class HomeVideoFrag extends Fragment implements View.OnClickListener {

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


    public static HomeVideoFrag getInstance() {
        if (self == null) {
            self = new HomeVideoFrag();
        }
        return self;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        MyView = inflater.inflate(R.layout.frag_video, container, false);
        return MyView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        InitData();
    }


    public void initView() {
        frag_video_quantity = MyView.findViewById(R.id.frag_video_quantity);
        frag_video_quantity_progress = MyView.findViewById(R.id.frag_video_quantity_progress);

        video_speed = MyView.findViewById(R.id.video_speed);
        frag_video_quantity_speed = MyView.findViewById(R.id.frag_video_quantity_speed);

        video_ab = MyView.findViewById(R.id.video_ab);
        video_ab_im = MyView.findViewById(R.id.video_ab_im);

        video_start = MyView.findViewById(R.id.video_start);
        video_start_im = MyView.findViewById(R.id.video_start_im);
        video_start_tv = MyView.findViewById(R.id.video_start_tv);

        video_ba = MyView.findViewById(R.id.video_ba);
        video_ba_im = MyView.findViewById(R.id.video_ba_im);

        video_shutter = MyView.findViewById(R.id.video_shutter);
        video_shutter_im = MyView.findViewById(R.id.video_shutter_im);

        video_course = MyView.findViewById(R.id.video_course);
        video_course_course = MyView.findViewById(R.id.video_course_course);

        video_ab.setOnClickListener(this);
        video_start.setOnClickListener(this);
        video_ba.setOnClickListener(this);
        video_shutter.setOnClickListener(this);
        video_course.setOnClickListener(this);

    }

    public void InitData() {


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
}
