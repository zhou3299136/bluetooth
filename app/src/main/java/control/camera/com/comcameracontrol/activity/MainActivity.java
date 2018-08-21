//package control.camera.com.comcameracontrol.activity;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.TextView;
//
//import control.camera.com.comcameracontrol.R;
//import control.camera.com.comcameracontrol.frag.HomeDelayFrag;
//import control.camera.com.comcameracontrol.frag.HomeVideoFrag;
//
//public class MainActivity extends BaseActivity implements View.OnClickListener{
//
//    private HomeVideoFrag videoFrag;
//    private HomeDelayFrag delayFrag;
//    private TextView main_frame_video;
//    private TextView main_frame_delay;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initData();
//    }
//
//    @Override
//    public int getLayout() {
//        return R.layout.activity_main;
//    }
//
//
//    private void initData() {
//        main_frame_video=findViewById(R.id.main_frame_video);
//        main_frame_delay=findViewById(R.id.main_frame_delay);
//        main_frame_video.setOnClickListener(this);
//        main_frame_delay.setOnClickListener(this);
//        checkFrag(R.id.main_frame_video);
//        main_frame_video.setSelected(true);
//        main_frame_delay.setSelected(false);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.main_frame_video:
//                main_frame_video.setSelected(true);
//                main_frame_delay.setSelected(false);
//                checkFrag(R.id.main_frame_video);
//                break;
//            case R.id.main_frame_delay:
//                main_frame_delay.setSelected(true);
//                main_frame_video.setSelected(false);
//                checkFrag(R.id.main_frame_delay);
//                break;
//        }
//    }
//
//    private void checkFrag(int id) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        if (fragmentManager.findFragmentByTag(HomeVideoFrag.class.getSimpleName()) != null) {
//            transaction.hide(fragmentManager.findFragmentByTag(HomeVideoFrag.class.getSimpleName()));
//        }
//        if (fragmentManager.findFragmentByTag(HomeDelayFrag.class.getSimpleName()) != null) {
//            transaction.hide(fragmentManager.findFragmentByTag(HomeDelayFrag.class.getSimpleName()));
//        }
//        switch (id){
//
//            case R.id.main_frame_video:
//                videoFrag = (HomeVideoFrag) getSupportFragmentManager().findFragmentByTag(HomeVideoFrag.class.getSimpleName());
//                if (videoFrag != null && videoFrag.isAdded()) {
//                    transaction.show(videoFrag);
//                } else {
//                    videoFrag = HomeVideoFrag.getInstance();
//                    transaction.add(R.id.main_frame_layout, videoFrag, HomeVideoFrag.class.getSimpleName());
//                }
//                transaction.commitAllowingStateLoss();
//                break;
//
//            case R.id.main_frame_delay:
//                delayFrag = (HomeDelayFrag) getSupportFragmentManager().findFragmentByTag(HomeDelayFrag.class.getSimpleName());
//                if (delayFrag != null && delayFrag.isAdded()) {
//                    transaction.show(delayFrag);
//                } else {
//                    delayFrag = HomeDelayFrag.getInstance();
//                    transaction.add(R.id.main_frame_layout, delayFrag, HomeDelayFrag.class.getSimpleName());
//                }
//                transaction.commitAllowingStateLoss();
//                break;
//
//        }
//
//
//    }
//
//
//}
