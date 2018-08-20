package star.jiuji.qianbao.armour.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import star.jiuji.qianbao.R;
import star.jiuji.qianbao.armour.fragment.VerificationFragment;
import star.jiuji.qianbao.armour.net.Api;
import star.jiuji.qianbao.armour.net.ApiService;
import star.jiuji.qianbao.armour.net.Contacts;
import star.jiuji.qianbao.armour.net.OnRequestDataListener;
import star.jiuji.qianbao.armour.net.VerListener;
import star.jiuji.qianbao.armour.utils.AppUtils;
import star.jiuji.qianbao.armour.utils.CaptchaTimeCount;
import star.jiuji.qianbao.armour.utils.CommonUtil;
import star.jiuji.qianbao.armour.utils.SPUtil;
import star.jiuji.qianbao.armour.utils.StatusBarUtil;
import star.jiuji.qianbao.armour.utils.ToastUtils;
import star.jiuji.qianbao.armour.view.SlideView;


/**
 * @author apple
 */
public class LoginActivity extends AppCompatActivity implements VerListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.bt_code)
    Button btCode;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.login_phone_r2)
    RelativeLayout loginPhoneR2;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.layout_code)
    RelativeLayout layoutCode;
    @BindView(R.id.slideview)
    SlideView slideview;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_card)
    TextView tvCard;
    @BindView(R.id.et_card)
    EditText etCard;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.layout_name)
    LinearLayout layoutName;
    private CaptchaTimeCount captchaTimeCount;
    private int oldNew = 0;
    private KProgressHUD hud;
    private String phone;
    private int isolduser;
    private String mHtml;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.theme_color), 40);
        ButterKnife.bind(this);
        captchaTimeCount = new CaptchaTimeCount(Contacts.Times.MILLIS_IN_TOTAL, Contacts.Times.COUNT_DOWN_INTERVAL, btCode, this);
        mHtml = getIntent().getStringExtra("html");
        mId = getIntent().getStringExtra("id");
        initView();

    }

    private void initView() {
        slideview.addSlideListener(new SlideView.OnSlideListener() {
            @Override
            public void onSlideSuccess() {
                if (layoutCode.getVisibility() == View.VISIBLE) {
                    String code = etCode.getText().toString();
                    if (!TextUtils.isEmpty(code) && code.length() == 4) {
                        verCode(code);
                        slideview.reset();
                    } else {
                        slideview.reset();
                        ToastUtils.showToast("验证码错误");
                    }
                } else {
                    SPUtil.putString(LoginActivity.this, Contacts.PHONE, phone);
                    if (isolduser == 0) {   //老用户
                        apply(mId, SPUtil.getString(LoginActivity.this, "token"));
                        startActivity(new Intent(LoginActivity.this, HtmlActivity.class).putExtra("html", mHtml));
                        finish();
                    } else {
                        loginPhoneR2.setVisibility(View.GONE);
                        layoutName.setVisibility(View.VISIBLE);
                        slideview.setVisibility(View.GONE);
                        slideview.reset();
                    }
                }
            }
        });

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

    }

    /**
     * 验证码效验
     */

    private void verCode(String code) {
        hud.show();
        Map<String, String> map = new HashMap<>();
        map.put("userphone", phone);
        map.put("code", code);

        ApiService.GET_SERVICE(Api.LOGIN.CHECKCODE, map, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                hud.dismiss();
                try {
                    JSONObject date = data.getJSONObject("data");
                    String msg = date.getString("msg");
                    String isSucess = date.getString("isSuccess");
                    String token = date.getString("token");
                    if ("1".equals(isSucess)) {
                        SPUtil.putString(LoginActivity.this, Contacts.PHONE, phone);
                        SPUtil.putString(LoginActivity.this, Contacts.TOKEN, token);
                        if (isolduser == 0) {
                            apply(mId, token);
                            startActivity(new Intent(LoginActivity.this, HtmlActivity.class).putExtra("html", mHtml));
                            finish();
                        } else {
                            layoutName.setVisibility(View.VISIBLE);
                            slideview.setVisibility(View.GONE);
                            layoutCode.setVisibility(View.GONE);
                            loginPhoneR2.setVisibility(View.GONE);
                        }
                    } else {
                        slideview.reset();
                    }
                    ToastUtils.showToast(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                slideview.reset();
                hud.dismiss();
                ToastUtils.showToast(msg);
            }
        });

    }

    @Override
    public void success() {
        isOldUser();
    }

    /**
     * isOldUser
     * 新老用户
     */
    private void isOldUser() {
        Map<String, String> map = new HashMap<>();
        map.put("userphone", phone);

        ApiService.GET_SERVICE(Api.LOGIN.isOldUser, map, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                try {
                    JSONObject date = data.getJSONObject("data");
                    oldNew = date.getIntValue("isolduser");
                    isolduser = date.getIntValue("iden_status");
                    if (oldNew == 1) {
                        String token = date.getString("token");
                        SPUtil.putString(LoginActivity.this, Contacts.TOKEN, token);
                    }
                    fillData(oldNew);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                ToastUtils.showToast(msg);
            }
        });
    }

    /**
     * 新老用户
     *
     * @param isolduser 1：老用户 0：新用户
     */
    private void fillData(int isolduser) {
        if (isolduser == 1) {
            if (layoutCode.getVisibility() == View.VISIBLE) {
                layoutCode.setVisibility(View.GONE);
            }
            slideview.setVisibility(View.VISIBLE);
        } else {
            layoutCode.setVisibility(View.VISIBLE);
            layoutName.setVisibility(View.GONE);
            getCode();
        }

    }

    /**
     * 验证码获取
     */
    private void getCode() {
        captchaTimeCount.start();

        Map<String, String> map = new HashMap<>();
        map.put("userphone", phone);
        ApiService.GET_SERVICE(Api.LOGIN.CODE, map, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                try {
                    JSONObject date = data.getJSONObject("data");
                    String msg = date.getString("msg");
                    String isSucess = date.getString("isSuccess");
                    if ("1".equals(isSucess)) {
                        slideview.setVisibility(View.VISIBLE);
                    }
                    ToastUtils.showToast(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void requestFailure(int code, String msg) {
                ToastUtils.showToast(msg);
            }
        });

    }


    @OnClick({R.id.back, R.id.bt_code, R.id.bt_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.bt_code:
                verPhone();
                break;
            case R.id.bt_login:
                break;
            default:
                break;
        }
    }


    private void verPhone() {
        phone = etPhone.getText().toString();
        boolean b = CommonUtil.checkPhone(phone, true);
        if (b) {
            VerificationFragment verification = new VerificationFragment();
            verification.show(getSupportFragmentManager(), "ver");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.setFocusable(false);
                v.setFocusableInTouchMode(true);
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null) {
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private void apply(String productId, String token) {
        OkGo.<String>post(Api.APPLY)
                .params("id", productId)
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("apply", "onSuccess: "+response.body() );
                    }
                });
    }
}
