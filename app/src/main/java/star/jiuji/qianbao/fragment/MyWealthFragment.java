package star.jiuji.qianbao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import star.jiuji.qianbao.base.BaseFragment;
import star.jiuji.qianbao.R;

/**
 * Created by liuwen on 2017/5/9.
 * 我的财富页面
 */
public class MyWealthFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_my_wealth);
        initView();
        initData();
        return getContentView();
    }

    private void initView() {
    }


    private void initData() {

    }
}
