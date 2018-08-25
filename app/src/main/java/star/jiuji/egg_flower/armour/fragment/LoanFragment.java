package star.jiuji.egg_flower.armour.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import star.jiuji.egg_flower.R;
import star.jiuji.egg_flower.armour.activity.HtmlActivity;
import star.jiuji.egg_flower.armour.activity.LoginActivity;
import star.jiuji.egg_flower.armour.adapter.ProductAdapter;
import star.jiuji.egg_flower.armour.entity.ProductEntity;
import star.jiuji.egg_flower.armour.net.Api;
import star.jiuji.egg_flower.armour.net.ApiService;
import star.jiuji.egg_flower.armour.net.OnRequestDataListener;
import star.jiuji.egg_flower.armour.utils.SPUtil;
import star.jiuji.egg_flower.armour.view.RecycleViewDivider;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author yanshihao
 */
public class LoanFragment extends Fragment {


    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.recylerview)
    RecyclerView mRecylerview;
    @BindView(R.id.Swip)
    SwipeRefreshLayout mSwip;
    Unbinder unbinder;

    public LoanFragment() {
    }

    private ProductAdapter mProductAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        mProductAdapter = new ProductAdapter(null);
        mRecylerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecylerview.addItemDecoration(new RecycleViewDivider(getContext(),
                LinearLayout.HORIZONTAL, 5, ContextCompat.getColor(getContext(), R.color.divider)));
        mRecylerview.setAdapter(mProductAdapter);
        getData();
        mSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        mProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                skipActivity(mProductAdapter.getData().get(position));
            }
        });
    }

    private void getData() {
        ApiService.GET_SERVICE(Api.PRODUCT_LSIT, null, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {

                String data = json.getString("data");
                Gson gson = new Gson();
                ProductEntity[] products = gson.fromJson(data, ProductEntity[].class);
                if (products.length != 0) {
                    List<ProductEntity> products1 = Arrays.asList(products);
                    mProductAdapter.setNewData(products1);
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
