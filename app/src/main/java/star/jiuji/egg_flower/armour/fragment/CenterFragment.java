package star.jiuji.egg_flower.armour.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import star.jiuji.egg_flower.R;
import star.jiuji.egg_flower.armour.activity.EmptyActivity;
import star.jiuji.egg_flower.armour.activity.FeedbackActivity;
import star.jiuji.egg_flower.armour.activity.HtmlActivity;
import star.jiuji.egg_flower.armour.activity.LoginActivity;
import star.jiuji.egg_flower.armour.activity.SettingActivity;
import star.jiuji.egg_flower.armour.entity.LoginEvent;
import star.jiuji.egg_flower.armour.net.Contacts;
import star.jiuji.egg_flower.armour.utils.SPUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class CenterFragment extends Fragment {


    @BindView(R.id.login)
    TextView login;
    private final int LOGIN_REQUESTION = 10000;
    private final int LOAN_REQUESTION = 20000;
    private final int FREE_REQUESTION = 30000;
    private final int RESULT_CODE = 200;

    private String token;

    public CenterFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_center, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLogin(LoginEvent event) {
        if (event.msg != null) {
            login.setText(event.msg);
        } else {
            login.setText("未登录");
        }
        token = SPUtil.getString(getContext(), Contacts.TOKEN);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void initView() {
        token = SPUtil.getString(getContext(), Contacts.TOKEN);
        String phone = SPUtil.getString(getContext(), Contacts.PHONE);
        if (!TextUtils.isEmpty(phone)) {
            login.setText(phone);
        }

    }

    @OnClick({R.id.header, R.id.login, R.id.setting, R.id.loan, R.id.free, R.id.safety, R.id.feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header:
            case R.id.login:
                token = SPUtil.getString(getContext(), Contacts.TOKEN);
                if (TextUtils.isEmpty(token)) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, LOGIN_REQUESTION);
                }
                break;
            case R.id.setting:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.loan:
                token = SPUtil.getString(getContext(), Contacts.TOKEN);

                Intent intent = new Intent(getActivity(), EmptyActivity.class);
                intent.putExtra("title", "贷款进度");
                startActivity(intent);

                break;
            case R.id.free:
                token = SPUtil.getString(getContext(), Contacts.TOKEN);

                Intent intent1 = new Intent(getActivity(), EmptyActivity.class);
                intent1.putExtra("title", "我的免息券");
                startActivity(intent1);

                break;
            case R.id.safety:
                Intent intent2 = new Intent(getActivity(), HtmlActivity.class);
                intent2.putExtra("title", "安全小贴士");
                intent2.putExtra("html", "http://m.anwenqianbao.com/#/minTips");
                startActivity(intent2);
                break;
            case R.id.feedback:
                startActivity(new Intent(getContext(), FeedbackActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_REQUESTION:
                if (resultCode == RESULT_CODE) {
                    String phone = data.getStringExtra("phone");
                    login.setText(phone);
                    token = SPUtil.getString(getContext(), Contacts.TOKEN);
                }
                break;
            case LOAN_REQUESTION:
                if (resultCode == RESULT_CODE) {
                    token = SPUtil.getString(getContext(), Contacts.TOKEN);
                    Intent intent = new Intent(getActivity(), EmptyActivity.class);
                    intent.putExtra("title", "贷款进度");
                    startActivity(intent);
                }
                break;
            case FREE_REQUESTION:
                if (resultCode == RESULT_CODE) {
                    token = SPUtil.getString(getContext(), Contacts.TOKEN);
                    Intent intent = new Intent(getActivity(), EmptyActivity.class);
                    intent.putExtra("title", "我的免息券");
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
}
