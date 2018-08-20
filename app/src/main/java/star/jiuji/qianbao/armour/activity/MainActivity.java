package star.jiuji.qianbao.armour.activity;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jaeger.library.StatusBarUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;
import star.jiuji.qianbao.R;
import star.jiuji.qianbao.armour.adapter.MyViewPagerAdapter;
import star.jiuji.qianbao.armour.adapter.NoTouchViewPager;
import star.jiuji.qianbao.armour.fragment.CenterFragment;
import star.jiuji.qianbao.armour.fragment.LoanFragment;
import star.jiuji.qianbao.armour.fragment.MainFragment;
import star.jiuji.qianbao.armour.utils.ToastUtils;
import star.jiuji.qianbao.base.BaseDefaultActivity;

/**
 * @author yanshihao
 */
public class MainActivity extends BaseDefaultActivity {


    @BindView(R.id.tab)
    PageNavigationView tab;
    @BindView(R.id.viewPager)
    NoTouchViewPager viewPager;
    public static NavigationController navigationController;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        StatusBarUtil.setTranslucentForImageViewInFragment(this, null);
        AndPermission.with(this)
                .requestCode(200)
                .permission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(permListener)
                .start();
    }


    private void initView() {
        ArrayList<Fragment> mFragments= new ArrayList<>();
        mFragments.add(new MainFragment());
        mFragments.add(new LoanFragment());
        mFragments.add(new CenterFragment());
        navigationController = tab.custom()
                .addItem(newItem(R.mipmap.icon_home, R.mipmap.icon_select_home, "首页"))
                .addItem(newItem(R.mipmap.icon_loan, R.mipmap.icon_select_loan, "贷款大全"))
                .addItem(newItem(R.mipmap.icon_center, R.mipmap.icon_select_center, "我的"))
                .build();

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(myViewPagerAdapter);
        navigationController.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(mFragments.size());
    }

    //创建一个Item
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text) {
        NormalItemView normalItemView = new NormalItemView(this);
        normalItemView.initialize(drawable, checkedDrawable, text);
        normalItemView.setTextDefaultColor(Color.GRAY);
        normalItemView.setTextCheckedColor(getResources().getColor(R.color.colorPrimary));
        return normalItemView;
    }


    private long mLastBackTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mLastBackTime) < 1000) {
            finish();
        } else {
            mLastBackTime = System.currentTimeMillis();
            ToastUtils.showToast( "再按一次退出");
        }
    }

    private PermissionListener permListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if (requestCode == 200) {
                // updateDiy();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            ToastUtils.showToast( "为了您的账号安全,请打开设备权限");
            if (requestCode == 200) {
                if ((AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions))) {
                    AndPermission.defaultSettingDialog(MainActivity.this, 500).show();
                }
            }
        }
    };

}
