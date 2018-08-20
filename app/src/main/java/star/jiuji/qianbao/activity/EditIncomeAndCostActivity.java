package star.jiuji.qianbao.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import star.jiuji.qianbao.enage.DataEnige;
import star.jiuji.qianbao.eventBus.Event;
import star.jiuji.qianbao.eventBus.EventBusUtil;
import star.jiuji.qianbao.R;
import star.jiuji.qianbao.bean.ShouRuModel;
import star.jiuji.qianbao.bean.ZhiChuModel;
import star.jiuji.qianbao.base.BaseActivity;
import star.jiuji.qianbao.base.Config;
import star.jiuji.qianbao.Dao.DaoShouRuModel;
import star.jiuji.qianbao.Dao.DaoZhiChuModel;
import star.jiuji.qianbao.eventBus.C;
import star.jiuji.qianbao.utils.RxUtil;
import star.jiuji.qianbao.utils.ToastUtils;

/**
 * Created by liuwen on 2017/3/8.
 */
public class EditIncomeAndCostActivity extends BaseActivity implements BGAOnRVItemClickListener {
    private RecyclerView mRecyclerView;
    private EditText editName;
    private ImageView imageName;
    private EditCostAdapter mAdapter;
    private List<ZhiChuModel> mList;
    private String type;
    private int url;

    @Override
    public int activityLayoutRes() {
        return R.layout.edit_income_and_cost_activity;
    }

    @Override
    public void initView() {
        setBackView();
        setLeftImage(R.mipmap.fanhui_lan);
        setTitle(getString(R.string.edit_category));
        setLeftText(getString(R.string.back));
        setRightText(getString(R.string.sure), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSure(type);
            }
        });
        type = this.getIntent().getStringExtra(Config.OTHER);
        mRecyclerView = (RecyclerView) findViewById(R.id.edit_recyclerView);
        editName = (EditText) findViewById(R.id.edit_name);
        imageName = (ImageView) findViewById(R.id.imag_name);
        mAdapter = new EditCostAdapter(mRecyclerView);
        final GridLayoutManager manager = new GridLayoutManager(EditIncomeAndCostActivity.this, 6, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mList = new ArrayList<>();
        mList = DataEnige.getZhiChuData();
        mAdapter.setData(mList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnRVItemClickListener(this);
    }

    private void onSure(String type) {
        String editTypeName = editName.getText().toString();
        if (TextUtils.isEmpty(editTypeName.trim())) {
            ToastUtils.showToast(EditIncomeAndCostActivity.this, "名称不能为空");
            return;
        }
        if (type.equals(Config.ZHI_CHU)) {
            final ZhiChuModel model = new ZhiChuModel();
            //解决了id的唯一性产生的bug 可以测试一下 当没有这个随机数
            //删除一个item的时候 在添加一个item的时候 会报id唯一性的错误
            //这是因为你删除一个item的时候 增加一个item生成的id是相同的 因为你插入的是相同的 所以
            //加入了随机数
            int y = 1 + (int) (Math.random() * 10000000);
            model.setId(DaoZhiChuModel.getCount() + y);
            model.setUrl(url);
            model.setNames(editTypeName);
            Observable.create(new Observable.OnSubscribe<ZhiChuModel>() {
                @Override
                public void call(Subscriber<? super ZhiChuModel> subscriber) {
                    DaoZhiChuModel.insertZhiChu(model);
                    subscriber.onNext(model);
                }
            }).compose(RxUtil.<ZhiChuModel>applySchedulers()).subscribe(new Action1<ZhiChuModel>() {
                @Override
                public void call(ZhiChuModel zhichuModel) {
                    EventBusUtil.sendEvent(new Event(C.EventCode.EditIncomeAndCostActivityToZhiChuFragment, zhichuModel));
                    finish();
                }
            });
        } else if (type.equals(Config.SHOU_RU)) {
            final ShouRuModel model = new ShouRuModel();
            int y = 1 + (int) (Math.random() * 10000000);
            model.setId(DaoShouRuModel.getCount() + y);
            model.setUrl(url);
            model.setName(editTypeName);

            Observable.create(new Observable.OnSubscribe<ShouRuModel>() {
                @Override
                public void call(Subscriber<? super ShouRuModel> subscriber) {
                    DaoShouRuModel.insertShouRu(model);
                    subscriber.onNext(model);
                }
            }).compose(RxUtil.<ShouRuModel>applySchedulers()).subscribe(new Action1<ShouRuModel>() {
                @Override
                public void call(ShouRuModel shouRuModel) {
                    EventBusUtil.sendEvent(new Event(C.EventCode.EditIncomeAndCostActivityToShouRuFragment, shouRuModel));
                    finish();
                }
            });
        }
    }


    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        editName.setText(mList.get(position).getNames());
        imageName.setImageResource(mList.get(position).getUrl());
        url = mList.get(position).getUrl();
    }


    private class EditCostAdapter extends BGARecyclerViewAdapter<ZhiChuModel> {

        public EditCostAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_zhichu_fragment);
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, ZhiChuModel model) {
            helper.setImageResource(R.id.item_imag, model.getUrl());
            helper.setText(R.id.item_name, model.getNames());
        }
    }
}
