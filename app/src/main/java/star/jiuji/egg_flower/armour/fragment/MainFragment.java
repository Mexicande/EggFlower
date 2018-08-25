package star.jiuji.egg_flower.armour.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import star.jiuji.egg_flower.R;
import star.jiuji.egg_flower.armour.activity.BankActivity;
import star.jiuji.egg_flower.armour.activity.HtmlActivity;
import star.jiuji.egg_flower.armour.activity.LoginActivity;
import star.jiuji.egg_flower.armour.activity.MainActivity;
import star.jiuji.egg_flower.armour.activity.WelfareActivity;
import star.jiuji.egg_flower.armour.adapter.CreditAdapter;
import star.jiuji.egg_flower.armour.adapter.ProductAdapter;
import star.jiuji.egg_flower.armour.entity.CreditBean;
import star.jiuji.egg_flower.armour.entity.ProductEntity;
import star.jiuji.egg_flower.armour.net.Api;
import star.jiuji.egg_flower.armour.net.ApiService;
import star.jiuji.egg_flower.armour.net.OnRequestDataListener;
import star.jiuji.egg_flower.armour.utils.SPUtil;
import star.jiuji.egg_flower.armour.utils.ToastUtils;
import star.jiuji.egg_flower.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recylerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.Swip)
    SwipeRefreshLayout mSwip;
    Unbinder unbinder;
    private ProductAdapter mProductAdapter;
    private CreditAdapter mCreditAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    private MainActivity mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        setListener();
        getData();
        return view;
    }


    private void initView() {
        mProductAdapter = new ProductAdapter(null);
        mCreditAdapter = new CreditAdapter(null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mProductAdapter.setHeaderView(getHeader());
        mProductAdapter.setFooterView(setFooter());
        mRecyclerView.setAdapter(mProductAdapter);

        mBankRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mBankRecycler.setAdapter(mCreditAdapter);

        mCreditAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //todo tiao yin hang ka h5
                Intent intent = new Intent(getContext(), HtmlActivity.class);
                intent.putExtra("title", mCreditAdapter.getData().get(position).getName());
                intent.putExtra("html", mCreditAdapter.getData().get(position).getLink());
                startActivity(intent);
            }
        });

        mProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // TODO: 2018/8/21  跳转
                skipActivity(mProductAdapter.getData().get(position));
            }
        });
    }


    private void setListener() {
        mSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

    }

    private View getHeader() {
        View view = getLayoutInflater().inflate(R.layout.item_header, null);
        ButterKnife.findById(view, R.id.item_header_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2018/8/21  头部按钮跳转
                if (mainActivity != null) {
                    mainActivity.selectFragment(1);
                }
            }
        });

        ButterKnife.findById(view, R.id.layout_loan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2018/8/21   贷款助手
                ApiService.GET_SERVICE("http://api.anwenqianbao.com/v2/borrow/url"
                        , null, new OnRequestDataListener() {
                            @Override
                            public void requestSuccess(int code, JSONObject json) {
                                JSONObject jsonObject = json.getJSONObject("data");
                                String url = jsonObject.getString("url");
                                String name = jsonObject.getString("name");
                                Intent intent = new Intent(getContext(), HtmlActivity.class);
                                intent.putExtra("title", name);
                                intent.putExtra("html", url);
                                startActivity(intent);
                            }

                            @Override
                            public void requestFailure(int code, String msg) {
                                ToastUtils.showToast(msg);
                            }

                            @Override
                            public void onFinish() {

                            }
                        });

            }
        });
        ButterKnife.findById(view, R.id.layout_lottery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2018/8/21  更多精彩
                Intent intent = new Intent(getContext(), WelfareActivity.class);
                startActivity(intent);
            }
        });
        ButterKnife.findById(view, R.id.item_header_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2018/8/21  跳转到第二界面
                if (mainActivity != null) {
                    mainActivity.selectFragment(1);
                }
            }
        });
        return view;
    }

    private RecyclerView mBankRecycler;

    private View setFooter() {
        View footer = getLayoutInflater().inflate(R.layout.item_footer, null);
        mBankRecycler = footer.findViewById(R.id.care_Recycler);
        ButterKnife.findById(footer, R.id.item_card_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2018/8/21 跳转银行卡
                Intent intent = new Intent(getContext(), BankActivity.class);
                startActivity(intent);

            }
        });

        return footer;
    }

    private void getData() {
        ApiService.GET_SERVICE(Api.HOT_PRODUCT, null, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {

                String data = json.getString("data");
                Gson gson = new Gson();
                ProductEntity[] products = gson.fromJson(data, ProductEntity[].class);
                if (products.length != 0) {
                    List<ProductEntity> products1 = Arrays.asList(products);
                    if (products1.size() > 3) {
                        List<ProductEntity> productEntities = products1.subList(0, 3);
                        mProductAdapter.setNewData(productEntities);
                    } else {
                        mProductAdapter.setNewData(products1);

                    }
                }
            }

            @Override
            public void requestFailure(int code, String msg) {

            }

            @Override
            public void onFinish() {

                if (mSwip.isRefreshing()) {
                    mSwip.setRefreshing(false);
                }

            }

        });
        ApiService.GET_SERVICE(Api.CREDIT, null, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {
                String data = json.getString("data");
                List<CreditBean> list = new Gson().fromJson(data, new TypeToken<List<CreditBean>>() {
                }.getType());
                mCreditAdapter.setNewData(list);
            }

            @Override
            public void requestFailure(int code, String msg) {
            }

            @Override
            public void onFinish() {
            }
        });
    }

    private void skipActivity(ProductEntity product) {
        String token = SPUtil.getString(getActivity(), "token");
        ApiService.apply(product.getId(), token);
        Intent intent;
        if (TextUtils.isEmpty(token)) {
            intent = new Intent(getContext(), LoginActivity.class);
        } else {
            intent = new Intent(getContext(), HtmlActivity.class);
        }
        intent.putExtra("html", product.getUrl());
        intent.putExtra("title", product.getP_name());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
