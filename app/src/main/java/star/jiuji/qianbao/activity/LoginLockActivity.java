package star.jiuji.qianbao.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import star.jiuji.qianbao.eventBus.Event;
import star.jiuji.qianbao.base.BaseActivity;
import star.jiuji.qianbao.base.Config;
import star.jiuji.qianbao.eventBus.C;
import star.jiuji.qianbao.graphicLock.GraphicLockView;
import star.jiuji.qianbao.R;
import star.jiuji.qianbao.utils.SharedPreferencesUtil;
import star.jiuji.qianbao.utils.ToastUtils;
import star.jiuji.qianbao.view.CircleImageView;

/**
 * Created by liuwen on 2017/2/10.
 */
public class LoginLockActivity extends BaseActivity implements GraphicLockView.OnGraphicLockListener {
    private CircleImageView imgUser;
    private TextView txtForgetPassword;
    private GraphicLockView mLockView;

    @Override
    public void initView() {
        imgUser = (CircleImageView) findViewById(R.id.user_info_url);
        txtForgetPassword = (TextView) findViewById(R.id.text_forget_password);
        mLockView = (GraphicLockView) findViewById(R.id.agl_gl_lock);

        Bitmap bt = BitmapFactory.decodeFile(Config.RootPath + "head.jpg");
        if (bt != null) {
            imgUser.setImageBitmap(bt);
        }
        mLockView.setOnGraphicLockListener(this);
        txtForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginLockActivity.this, Login1Activity.class);
                intent.putExtra(Config.TxtForgetGesturePassword, true);
                startActivity(intent);
            }
        });
    }


    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case C.EventCode.UserUrl:
                imgUser.setImageBitmap((Bitmap) event.getData());
                break;
            case C.EventCode.UserForgetGesturePassword:
                finish();
                break;
        }
    }

    @Override
    public void setPwdSuccess(String password) {
        if (SharedPreferencesUtil.getStringPreferences(this, Config.LockPassword, "").equals(password)) {
            ToastUtils.showToast(this, getString(R.string.lock_login_success));
            startActivity(new Intent(LoginLockActivity.this, Login1Activity.class));
            finish();
        } else {
            ToastUtils.showToast(this, getString(R.string.lock_login_error));
        }
    }

    @Override
    public void setPwdFailure() {
        Toast.makeText(this, getString(R.string.lock_login_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int activityLayoutRes() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.login_lock_activity;
    }


}
