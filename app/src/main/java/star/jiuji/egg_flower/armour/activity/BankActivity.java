package star.jiuji.egg_flower.armour.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import star.jiuji.egg_flower.R;
import star.jiuji.egg_flower.armour.adapter.BankAdapter;
import star.jiuji.egg_flower.armour.entity.CreditBean;
import star.jiuji.egg_flower.armour.net.Api;
import star.jiuji.egg_flower.armour.net.ApiService;
import star.jiuji.egg_flower.armour.net.OnRequestDataListener;
import star.jiuji.egg_flower.armour.utils.SPUtil;
import star.jiuji.egg_flower.armour.utils.StatusBarUtil;
import star.jiuji.egg_flower.armour.utils.ToastUtils;
import star.jiuji.egg_flower.armour.view.RecycleViewDivider;

/**
 * @author yanshihao
 */
public class BankActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.recylerview)
    RecyclerView mRecylerview;
    @BindView(R.id.Swip)
    SwipeRefreshLayout mSwip;

    private BankAdapter mBankAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 90);

        mTitle.setText("银行卡");
        initView();
    }

    private void initView() {
        mBankAdapter = new BankAdapter(null);
        mRecylerview.setLayoutManager(new LinearLayoutManager(this));
        mRecylerview.addItemDecoration(new RecycleViewDivider(this,
                LinearLayout.HORIZONTAL, 10, ContextCompat.getColor(this, R.color.divider)));
        mRecylerview.setAdapter(mBankAdapter);
        getData();
        mSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        mBankAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                skipActivity(mBankAdapter.getData().get(position));
            }
        });
    }

    private void skipActivity(CreditBean creditBean) {
        String token = SPUtil.getString(this, "token");
        Intent intent;
        if (TextUtils.isEmpty(token)) {
            intent = new Intent(this, LoginActivity.class);
        } else {
            intent = new Intent(this, HtmlActivity.class);
        }
        intent.putExtra("html", creditBean.getLink());
        intent.putExtra("title", creditBean.getName());
        startActivity(intent);
    }

    public void getData() {
        ApiService.GET_SERVICE(Api.CREDIT, null, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {
                String data = json.getString("data");
                List<CreditBean> list = new Gson().fromJson(data, new TypeToken<List<CreditBean>>() {
                }.getType());
                mBankAdapter.setNewData(list);
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
}
