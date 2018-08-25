package star.jiuji.egg_flower.armour.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import star.jiuji.egg_flower.R;
import star.jiuji.egg_flower.armour.adapter.WelfareAdapter;
import star.jiuji.egg_flower.armour.entity.WelfareBean;
import star.jiuji.egg_flower.armour.net.Api;
import star.jiuji.egg_flower.armour.net.ApiService;
import star.jiuji.egg_flower.armour.net.OnRequestDataListener;
import star.jiuji.egg_flower.armour.utils.ToastUtils;
import star.jiuji.egg_flower.armour.view.RecycleViewDivider;

/**
 * @author yanshihao
 */
public class WelfareActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.recylerview)
    RecyclerView mRecylerview;
    @BindView(R.id.Swip)
    SwipeRefreshLayout mSwip;

    private WelfareAdapter mWelfareAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.colorPrimary),50);
        ButterKnife.bind(this);
        initView();
        ininData();
    }

    private void ininData() {
        ApiService.GET_SERVICE(Api.WELFARE, null, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {

                String data = json.getString("data");
                Gson gson = new Gson();
                List<WelfareBean> list = gson.fromJson(data,
                        new TypeToken<List<WelfareBean>>() {
                        }.getType());

                mWelfareAdapter.setNewData(list);
            }

            @Override
            public void requestFailure(int code, String msg) {
                ToastUtils.showToast(msg);
            }

            @Override
            public void onFinish() {

                if (mSwip.isRefreshing()) {
                    mSwip.setRefreshing(false);
                }

            }

        });
    }

    private void initView() {
        mTitle.setText("福利");
        mRecylerview.setLayoutManager(new LinearLayoutManager(this));
        mWelfareAdapter = new WelfareAdapter(null);
        mRecylerview.addItemDecoration(new RecycleViewDivider(this,
                LinearLayout.HORIZONTAL, 10, ContextCompat.getColor(this, R.color.divider)));
        mRecylerview.setAdapter(mWelfareAdapter);
        mSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ininData();
            }
        });
    }
}
