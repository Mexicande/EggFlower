package star.jiuji.egg_flower.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import star.jiuji.egg_flower.R;
import star.jiuji.egg_flower.utils.DateTimeUtil;
import star.jiuji.egg_flower.base.BaseActivity;
import star.jiuji.egg_flower.base.Config;
import star.jiuji.egg_flower.utils.SharedPreferencesUtil;

/**
 * Created by liuwen on 2017/2/7.
 */
public class HuoBiActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout reHuoBi;
    private TextView txtHuoBiCh, txtHuoBiEn, txtHuoBiUpdate;


    @Override
    public int activityLayoutRes() {
        return R.layout.huo_bi_activity;
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.huobi_setting));
        setLeftImage(R.mipmap.fanhui_lan);
        setLeftText(getString(R.string.back));
        setBackView();

        reHuoBi = (RelativeLayout) findViewById(R.id.re_huobi);
        txtHuoBiCh = (TextView) findViewById(R.id.txt_huobi_ch);
        txtHuoBiEn = (TextView) findViewById(R.id.txt_huobi_en);
        txtHuoBiUpdate = (TextView) findViewById(R.id.huobi_update);

        reHuoBi.setOnClickListener(this);
        txtHuoBiUpdate.setText("汇率更新与"+ DateTimeUtil.getCurrentTime_Today());
        txtHuoBiCh.setText(SharedPreferencesUtil.getStringPreferences(this, Config.HuoBICh, "").isEmpty() ? getString(R.string.no_setting) : SharedPreferencesUtil.getStringPreferences(this, Config.HuoBICh, ""));
        txtHuoBiEn.setText(SharedPreferencesUtil.getStringPreferences(this, Config.HuoBIEn, "").isEmpty() ? getString(R.string.no_setting) : SharedPreferencesUtil.getStringPreferences(this, Config.HuoBIEn, ""));
    }

    private static final int HuoBICode = 101;

    @Override
    public void onClick(View v) {
        if (v == reHuoBi) {
            startActivityForResult(new Intent(this, HuoBiSettingActivity.class), HuoBICode);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case HuoBICode:
                txtHuoBiCh.setText(data.getExtras().getString(Config.HuoBICh));
                txtHuoBiEn.setText(data.getExtras().getString(Config.HuoBIEn));
                SharedPreferencesUtil.setStringPreferences(this, Config.HuoBICh, data.getExtras().getString(Config.HuoBICh));
                SharedPreferencesUtil.setStringPreferences(this, Config.HuoBIEn, data.getExtras().getString(Config.HuoBIEn));
                break;
        }
    }
}
