package star.jiuji.qianbao.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import star.jiuji.qianbao.base.BaseActivity;
import star.jiuji.qianbao.base.Config;
import star.jiuji.qianbao.R;
import star.jiuji.qianbao.utils.SharedPreferencesUtil;
import star.jiuji.qianbao.base.App;

/**
 * Created by liuwen on 2017/2/7.
 */
public class CodedLockActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout reSettingCodeClock;
    private ImageView mImageView;

    @Override
    public int activityLayoutRes() {
        return R.layout.coded_lock_activity;
    }

    @Override
    public void initView() {
        setLeftImage(R.mipmap.fanhui_lan);
        setTitle(getString(R.string.setting_save_account));
        setLeftText(getString(R.string.back));
        setBackView();
        mImageView = (ImageView) findViewById(R.id.remind_push);
        reSettingCodeClock = (RelativeLayout) findViewById(R.id.reSetting_code_clock);
        boolean isOpen = SharedPreferencesUtil.getBooleanPreferences(this, Config.isOpenCodedLock, false);
        mImageView.setImageResource(isOpen ? R.mipmap.more_push_on : R.mipmap.more_push_off);
        SharedPreferencesUtil.cleanSharePreferences(this, isOpen ? "" : Config.isOpenCodedLock);
        reSettingCodeClock.setVisibility(isOpen ? View.VISIBLE : View.GONE);

        reSettingCodeClock.setOnClickListener(this);

    }


    public void toSwitchPush(View view) {
        if (!SharedPreferencesUtil.getBooleanPreferences(this, Config.isOpenCodedLock, false)) {
            Intent intent = new Intent(this, GraphicLockActivity.class);
            startActivityForResult(intent, isOpenLock);
        } else {
            mImageView.setImageResource(R.mipmap.more_push_off);
            reSettingCodeClock.setVisibility(View.GONE);
            App.isOpenCodedLock = !App.isOpenCodedLock;
            SharedPreferencesUtil.setBooleanPreferences(CodedLockActivity.this, Config.isOpenCodedLock, App.isOpenCodedLock);
        }

    }

    private static final int isOpenLock = 101;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case isOpenLock:
                boolean isOpen = data.getExtras().getBoolean(Config.isOpenCodedLock);
                mImageView.setImageResource(isOpen ? R.mipmap.more_push_on : R.mipmap.more_push_off);
                reSettingCodeClock.setVisibility(isOpen ? View.VISIBLE : View.GONE);
                App.isOpenCodedLock = isOpen;
                SharedPreferencesUtil.setBooleanPreferences(CodedLockActivity.this, Config.isOpenCodedLock, isOpen);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == reSettingCodeClock) {
            Intent intent = new Intent(this, GraphicLockActivity.class);
            startActivityForResult(intent, isOpenLock);
        }
    }
}
