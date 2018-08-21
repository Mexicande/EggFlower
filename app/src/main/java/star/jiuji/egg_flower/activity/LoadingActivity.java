package star.jiuji.egg_flower.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import java.util.List;

import star.jiuji.egg_flower.MainActivity;
import star.jiuji.egg_flower.R;
import star.jiuji.egg_flower.utils.AppStatusManager;
import star.jiuji.egg_flower.utils.SharedPreferencesUtil;
import star.jiuji.egg_flower.base.Config;

/**
 * Created by liuwen on 2017/2/14.
 */
public class LoadingActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<View> mViews;
    private boolean mIsFirst;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppStatusManager.getInstance().setAppStatus(Config.STATUS_NORMAL);//进入应用初始化设置未登录
        super.onCreate(savedInstanceState);
       // mIsFirst = SharedPreferencesUtil.getBooleanPreferences(this, Config.FistStar, true);
       setContentView(R.layout.lauach_activity);
        initView();
    }

    private void initView() {

        handler.postDelayed(runnable, 0);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boolean isFirstStar = SharedPreferencesUtil.getBooleanPreferences(LoadingActivity.this, Config.FistStar, false);
            String paw = SharedPreferencesUtil.getStringPreferences(LoadingActivity.this, Config.UserPassWord, "");
            Intent intent = null;
            if (isFirstStar && !TextUtils.isEmpty(paw)) {
                //判断是否开启了锁
                if (SharedPreferencesUtil.getBooleanPreferences(LoadingActivity.this, Config.isOpenCodedLock, false)) {
                    intent = new Intent(LoadingActivity.this, LoginLockActivity.class);
                } else {
                    intent = new Intent(LoadingActivity.this, MainActivity.class);
                }
            } else {
                //判断是否开启了锁
                if (SharedPreferencesUtil.getBooleanPreferences(LoadingActivity.this, Config.isOpenCodedLock, false)) {
                    intent = new Intent(LoadingActivity.this, LoginLockActivity.class);
                } else {
                    intent = new Intent(LoadingActivity.this, Login1Activity.class);
                }
            }
            startActivity(intent);
            LoadingActivity.this.finish();
        }
    };
}
