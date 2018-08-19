package control.camera.com.comcameracontrol.frag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import control.camera.com.comcameracontrol.R;

/**
 * 延时拍照
 */
public class HomeDelayFrag extends Fragment {

    private View MyView;

    private static HomeDelayFrag self;

    public static HomeDelayFrag getInstance() {
        if (self == null) {
            self = new HomeDelayFrag();
        }
        return self;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MyView = inflater.inflate(R.layout.frag_delay, container, false);
        return MyView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    public void initData() {

    }


}
