package star.jiuji.qianbao.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import star.jiuji.qianbao.activity.AboutMeActivity;
import star.jiuji.qianbao.activity.ChangeSkinActivity;
import star.jiuji.qianbao.activity.RemindActivity;
import star.jiuji.qianbao.activity.SaveMoneyActivity;
import star.jiuji.qianbao.activity.SettingActivity;
import star.jiuji.qianbao.activity.ShowSaveMoneyPlanActivity;
import star.jiuji.qianbao.activity.UserInfoActivity;
import star.jiuji.qianbao.base.BaseFragment;
import star.jiuji.qianbao.base.Config;
import star.jiuji.qianbao.eventBus.C;
import star.jiuji.qianbao.eventBus.Event;
import star.jiuji.qianbao.utils.BitMapUtils;
import star.jiuji.qianbao.utils.SharedPreferencesUtil;
import star.jiuji.qianbao.view.CircleImageView;
import star.jiuji.qianbao.bean.PlanSaveMoneyModel;
import star.jiuji.qianbao.R;
import star.jiuji.qianbao.utils.ToastUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout reUserInfo, reJq, reCq, reHf, reZd, reDaoData, reTx, reSuggest, reSetting, reAbout;
    private DrawerLayout mDrawerLayout;
    private CircleImageView mImageUrl;
    private TextView txtUserNickName, txtSignature;
    private PlanSaveMoneyModel model;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_my);
        setTitle(getString(R.string.my_more));
        initView();
        return getContentView();
    }

    private void initView() {
        reUserInfo = (RelativeLayout) getContentView().findViewById(R.id.re_f_my_userInfo);
        //reJq = (RelativeLayout) getContentView().findViewById(R.id.re_f_my_jieqian);
        reCq = (RelativeLayout) getContentView().findViewById(R.id.re_f_my_cunqian);
        reHf = (RelativeLayout) getContentView().findViewById(R.id.re_f_my_huanfu);
        reZd = (RelativeLayout) getContentView().findViewById(R.id.re_f_my_zhangdan);
        reDaoData = (RelativeLayout) getContentView().findViewById(R.id.re_f_my_daochushuju);
        reTx = (RelativeLayout) getContentView().findViewById(R.id.re_f_my_tixing);
        reSuggest = (RelativeLayout) getContentView().findViewById(R.id.re_f_my_suggest);
        reSetting = (RelativeLayout) getContentView().findViewById(R.id.re_f_my_setting);
        reAbout = (RelativeLayout) getContentView().findViewById(R.id.re_f_my_about_us);

        txtUserNickName = (TextView) getContentView().findViewById(R.id.f_my_userName);
        txtSignature = (TextView) getContentView().findViewById(R.id.f_my_message);

        mImageUrl = (CircleImageView) getContentView().findViewById(R.id.f_my_touxiang);
        mDrawerLayout = (DrawerLayout) getContentView().findViewById(R.id.drawer_layout);

        reUserInfo.setOnClickListener(this);
//        reJq.setOnClickListener(this);
        reCq.setOnClickListener(this);
        reHf.setOnClickListener(this);
        reZd.setOnClickListener(this);
        reDaoData.setOnClickListener(this);
        reTx.setOnClickListener(this);
        reSuggest.setOnClickListener(this);
        reSetting.setOnClickListener(this);
        reAbout.setOnClickListener(this);

        if (SharedPreferencesUtil.getStringPreferences(getActivity(), Config.ChangeBg, null) != null) {
            Bitmap bitmap = BitMapUtils.getBitmapByPath(getActivity(), SharedPreferencesUtil.getStringPreferences(getActivity(), Config.ChangeBg, null), false);
            mDrawerLayout.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
        }


        Bitmap bt = BitmapFactory.decodeFile(Config.RootPath + "head.jpg");
        if (bt != null) {
            mImageUrl.setImageBitmap(bt);
        }
        txtUserNickName.setText(SharedPreferencesUtil.getStringPreferences(getActivity(), Config.userNickName, "").isEmpty() ? SharedPreferencesUtil.getStringPreferences(getActivity(), Config.UserName, "").isEmpty() ? getString(R.string.no_setting) : SharedPreferencesUtil.getStringPreferences(getActivity(), Config.UserName, "") : SharedPreferencesUtil.getStringPreferences(getActivity(), Config.userNickName, ""));
        txtSignature.setText(SharedPreferencesUtil.getStringPreferences(getActivity(), Config.userSignature, "").isEmpty() ? getString(R.string.no_setting) : SharedPreferencesUtil.getStringPreferences(getActivity(), Config.userSignature, ""));

    }


    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case C.EventCode.UserInFoToMyFragment:
                //设置昵称
                txtUserNickName.setText(getString(R.string.no_setting));
                break;
            case C.EventCode.UserPhoto:
                //设置背景
                Bitmap bitmap = BitMapUtils.getBitmapByPath(getActivity(), event.getData().toString(), false);
                mDrawerLayout.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
                break;
            case C.EventCode.UserUrl:
                //设置头像
                mImageUrl.setImageBitmap((Bitmap) event.getData());
                break;
            case C.EventCode.UserSignature:
                //设置签名
                txtSignature.setText((CharSequence) event.getData());
                break;
            case C.EventCode.UserNickName:
                //设置昵称
                txtUserNickName.setText((CharSequence) event.getData());
                break;
            case C.EventCode.NewSaveMoneyPlanActivityToMyFragment:
                model = (PlanSaveMoneyModel) event.getData();
                break;

        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), SaveMoneyActivity.class);
        if (v == reUserInfo) {
            startActivity(new Intent(getActivity(), UserInfoActivity.class));
        } else if (v == reJq) {

        } else if (v == reCq) {
            intent.putExtra("plan", "cunqian");
            if (SharedPreferencesUtil.getBooleanPreferences(getActivity(), Config.PlanIsPut, false)) {
                Intent intentPlan = new Intent(new Intent(getActivity(), ShowSaveMoneyPlanActivity.class));
                intentPlan.putExtra(Config.PlanSaveMoneyModel, model);
                startActivity(intentPlan);
            } else {
                startActivity(intent);
            }
        } else if (v == reHf) {
            startActivity(new Intent(getActivity(), ChangeSkinActivity.class));
        } else if (v == reZd) {
            intent.putExtra("plan", "zhangdan");
            startActivity(intent);
        } else if (v == reDaoData) {
            ToastUtils.showToast(getActivity(), "功能开发中，敬请期待");
        } else if (v == reTx) {
            startActivity(new Intent(getActivity(), RemindActivity.class));
        } else if (v == reSuggest) {
            ToastUtils.showToast(getActivity(), "功能开发中,敬请期待");
        } else if (v == reSetting) {
            startActivity(new Intent(getActivity(), SettingActivity.class));
        } else if (v == reAbout) {
            startActivity(new Intent(getActivity(), AboutMeActivity.class));
        }
    }

}
