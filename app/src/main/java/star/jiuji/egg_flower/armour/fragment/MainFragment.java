package star.jiuji.egg_flower.armour.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import star.jiuji.egg_flower.armour.activity.MainActivity;
import star.jiuji.egg_flower.armour.adapter.CreditAdapter;
import star.jiuji.egg_flower.armour.adapter.ProductAdapter;
import star.jiuji.egg_flower.armour.entity.CreditBean;
import star.jiuji.egg_flower.armour.entity.ProductEntity;
import star.jiuji.egg_flower.armour.net.Api;
import star.jiuji.egg_flower.armour.net.ApiService;
import star.jiuji.egg_flower.armour.net.OnRequestDataListener;
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
        mainActivity=(MainActivity) context;
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
        mProductAdapter=new ProductAdapter(null);
        mCreditAdapter=new CreditAdapter(null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       // mRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.recycler_divider));
        mProductAdapter.setHeaderView(getHeader());
        mProductAdapter.setFooterView(setFooter());
        mRecyclerView.setAdapter(mProductAdapter);

        mBankRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        mBankRecycler.setAdapter(mCreditAdapter);

        mCreditAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //todo tiao yin hang ka h5
            }
        });

        mProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // TODO: 2018/8/21  跳转
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
        ButterKnife.findById(view,R.id.item_header_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2018/8/21  头部按钮跳转
            }
        });

        ButterKnife.findById(view,R.id.layout_loan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2018/8/21   贷款助手
            }
        });
        ButterKnife.findById(view,R.id.layout_lottery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2018/8/21  更多精彩
            }
        });
        ButterKnife.findById(view,R.id.item_header_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2018/8/21  跳转到第二界面
                if (mainActivity!=null){
                    mainActivity.selectFragment(1);
                }
            }
        });
        return view;
    }
    private RecyclerView mBankRecycler;
    private View setFooter() {
        View footer = getLayoutInflater().inflate(R.layout.item_footer, null);
        mBankRecycler=footer.findViewById(R.id.care_Recycler);
        ButterKnife.findById(footer,R.id.item_card_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2018/8/21 跳转银行卡
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
                        if(products1.size()>3){
                            List<ProductEntity> productEntities = products1.subList(0, 3);
                            mProductAdapter.setNewData(productEntities);
                        }else {
                            mProductAdapter.setNewData(products1);

                        }
                    }
            }

            @Override
            public void requestFailure(int code, String msg) {

            }

            @Override
            public void onFinish() {

                if(mSwip.isRefreshing()){
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
