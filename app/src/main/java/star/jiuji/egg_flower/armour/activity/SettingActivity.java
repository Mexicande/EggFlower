package star.jiuji.egg_flower.armour.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import star.jiuji.egg_flower.R;
import star.jiuji.egg_flower.armour.entity.LoginEvent;
import star.jiuji.egg_flower.armour.net.Contacts;
import star.jiuji.egg_flower.armour.utils.SPUtil;
import star.jiuji.egg_flower.armour.utils.StatusBarUtil;
import star.jiuji.egg_flower.armour.utils.ToastUtils;

/**
 * @author apple
 */
public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.apply)
    Button apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 90);

        initView();
    }

    private void initView() {
        toolbarBack.setVisibility(View.VISIBLE);
        toolbarTitle.setText("设置");
        String token = SPUtil.getString(this, Contacts.TOKEN);
        if (!TextUtils.isEmpty(token)) {
            apply.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.toolbar_back, R.id.about, R.id.version, R.id.apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.about:
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                break;
            case R.id.version:
                ToastUtils.showToast("已经是最近版本啦！");
                break;
            case R.id.apply:
                SPUtil.remove(SettingActivity.this, Contacts.PHONE);
                SPUtil.remove(SettingActivity.this, Contacts.TOKEN);
                startActivity(new Intent(SettingActivity.this, LoginActivity.class)
                        .putExtra("type", 1));
                EventBus.getDefault().post(new LoginEvent(null));
                finish();
                break;
            default:
                break;
        }
    }
}
