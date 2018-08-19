package control.camera.com.comcameracontrol.frag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 视频模式
 */
import control.camera.com.comcameracontrol.R;

public class HomeVideoFrag extends Fragment {

    private View MyView;
    private static HomeVideoFrag self;

    public static HomeVideoFrag getInstance() {
        if (self == null) {
            self = new HomeVideoFrag();
        }
        return self;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        MyView=inflater.inflate(R.layout.frag_video,container,false);
        return MyView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitData();
    }

    public void InitData(){


    }






}
