package star.jiuji.qianbao.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGADivider;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import star.jiuji.qianbao.Dao.DaoAccount;
import star.jiuji.qianbao.Dao.DaoAccountBalance;
import star.jiuji.qianbao.Dao.DaoChoiceAccount;
import star.jiuji.qianbao.eventBus.Event;
import star.jiuji.qianbao.eventBus.EventBusUtil;
import star.jiuji.qianbao.utils.DateTimeUtil;
import star.jiuji.qianbao.utils.RxUtil;
import star.jiuji.qianbao.utils.StatusBarUtils;
import star.jiuji.qianbao.bean.AccountModel;
import star.jiuji.qianbao.bean.ChoiceAccount;
import star.jiuji.qianbao.base.BaseActivity;
import star.jiuji.qianbao.base.Config;
import star.jiuji.qianbao.eventBus.C;
import star.jiuji.qianbao.R;
import star.jiuji.qianbao.view.NumberAnimTextView;
import star.jiuji.qianbao.bean.BaseModel;

/**
 * Created by liuwen on 2017/2/17.
 */
public class PayShowActivity extends BaseActivity implements BGAOnRVItemClickListener {
    private NumberAnimTextView tvAccount;
    private TextView txtMonth, txtLiuChu, txtLiuRu;
    private RelativeLayout ryBg;
    private TimePickerView pvTime;
    private ChoiceAccount model;
    private RecyclerView mRecyclerView;
    private List<AccountModel> mList = new ArrayList<>();
    private List<BaseModel> updateChoiceList = new ArrayList<>();//修改余额的list
    private List<BaseModel> baseList = new ArrayList<>();
    private PaySHowAdapter mAdapter;
    private View headView;
    private double zhiChu, liuRu;
    private ViewStub mViewStub;
    private double tvAccountValue;
    private long payShowId;
    private String payShowDate;

    @Override
    public int activityLayoutRes() {
        return R.layout.pay_show_activity;
    }

    @Override
    public void initView() {
        setBackView();
        setLeftText(getString(R.string.back));
        setLeftTextColor(this.getResources().getColor(R.color.white));
        setRightTxtColor(this.getResources().getColor(R.color.white));
        setTitlesColor(this.getResources().getColor(R.color.white));
        setRightTxtColor(this.getResources().getColor(R.color.white));
        setRightText(getString(R.string.setting), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayShowActivity.this, PaySettingsActivity.class);
                intent.putExtra(Config.ModelWallet, model);
                startActivity(intent);
            }
        });

        headView = View.inflate(this, R.layout.head_pay_show, null);
        tvAccount = (NumberAnimTextView) headView.findViewById(R.id.pay_show_txt_account);
        txtMonth = (TextView) headView.findViewById(R.id.pay_show_txt_month);
        txtLiuChu = (TextView) headView.findViewById(R.id.pay_show_txt_liuchu);
        txtLiuRu = (TextView) headView.findViewById(R.id.pay_show_txt_liuru);
        ryBg = (RelativeLayout) headView.findViewById(R.id.pay_ry);

        txtMonth.setText(DateTimeUtil.getCurrentYearMonth());

        mRecyclerView = (RecyclerView) findViewById(R.id.pay_show_recycler_view);
        mViewStub = (ViewStub) findViewById(R.id.view_stub);
        mViewStub.inflate();
        mViewStub.setVisibility(View.GONE);
        model = (ChoiceAccount) getIntent().getExtras().getSerializable(Config.ModelWallet);

        StringBuilder sbEndTime = new StringBuilder();
        StringBuilder sbStartTime = new StringBuilder();
        sbStartTime = sbStartTime.append(DateTimeUtil.getCurrentYearMonth()).append("-01").append("-00-").append("00-").append("00");
        sbEndTime = sbEndTime.append(DateTimeUtil.getCurrentYearMonth()).append("-31").append("-23-").append("59-").append("59");

        mAdapter = new PaySHowAdapter(mRecyclerView);
        mAdapter.addHeaderView(headView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (model != null) {
            payShowId = model.getId();
            payShowDate = model.getTimeMinSec();
            if (DaoAccount.queryByAccountType(model.getMAccountType()).size() != 0 || DaoChoiceAccount.query().size() != 0) {
                PayShowList(payShowId, sbStartTime, sbEndTime);
            } else {
                mAdapter.setData(baseList);
                mRecyclerView.setAdapter(mAdapter.getHeaderAndFooterAdapter());
            }
            mRecyclerView.addItemDecoration(BGADivider.newShapeDivider());
            tvAccount.setText(String.format("%.2f", model.getMoney()));
            ryBg.setBackgroundResource(model.getColor());
            StatusBarUtils.setWindowStatusBarColor(this, model.getColor());
            setTitleBg(model.getColor());
            if (model.getMAccountType().equals(Config.XYK) || model.getMAccountType().equals(Config.CXK)) {
                if (model.getIssuingBank().equals("")) {
                    setTitle(model.getAccountName());
                } else {
                    setTitle(model.getIssuingBank());
                }
            } else {
                setTitle(model.getAccountName());
            }
        }
        mAdapter.setOnRVItemClickListener(this);
    }


    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case C.EventCode.PaySettingToPayShowActivityAndWalletFragment:
                final long id = (long) event.getData();
                Observable.create(new Observable.OnSubscribe<ChoiceAccount>() {
                    @Override
                    public void call(Subscriber<? super ChoiceAccount> subscriber) {
                        model = DaoChoiceAccount.queryByAccountId(id).get(0);
                        subscriber.onNext(model);
                    }
                }).compose(RxUtil.<ChoiceAccount>applySchedulers()).subscribe(new Action1<ChoiceAccount>() {
                    @Override
                    public void call(ChoiceAccount account) {
                        if (account.getMAccountType().equals(Config.XYK) || account.getMAccountType().equals(Config.CXK)) {
                            if (account.getIssuingBank().equals("")) {
                                setTitle(account.getAccountName());
                            } else {
                                setTitle(account.getIssuingBank());
                            }
                        } else {
                            setTitle(account.getAccountName());
                        }
                    }
                });
                break;
            case C.EventCode.ChoiceColorToPaySettingAndPayShowAndWalletFragment:
                int color = (int) event.getData();
                ryBg.setBackgroundResource(color);
                StatusBarUtils.setWindowStatusBarColor(PayShowActivity.this, color);
                setTitleBg(color);
                break;

        }
    }


    private void PayShowList(long id, StringBuilder startTime, StringBuilder endTime) {
        baseList.clear();
        mList = DaoAccount.queryByIdAndDate(id, startTime, endTime);
        //账户的支出和消费记录list

        for (int i = 0; i < mList.size(); i++) {
            BaseModel baseModel = new BaseModel();
            int y = 1 + (int) (Math.random() * 1000);
            baseModel.setId(mList.get(i).getChoiceAccountId());
            baseModel.setAccountType(mList.get(i).getAccountType());
            baseModel.setUrl(mList.get(i).getUrl());
            baseModel.setMoney(mList.get(i).getMoney());
            baseModel.setName(mList.get(i).getConsumeType());
            baseModel.setType(Config.AccountModel);
            baseModel.setZhiChuShouRuType(mList.get(i).getZhiChuShouRuType());
            baseModel.setTimeMinSec(mList.get(i).getTimeMinSec());
            baseModel.setDate(mList.get(i).getData());
            baseList.add(baseModel);
        }
        //账户的修改余额的list
        updateChoiceList = DaoAccountBalance.queryByIDAndDate(id, startTime, endTime);
        for (int i = 0; i < updateChoiceList.size(); i++) {
            baseList.add(updateChoiceList.get(i));
            tvAccountValue = updateChoiceList.get(0).getMoney();
        }

        Collections.sort(baseList, new Comparator<BaseModel>() {
            @Override
            public int compare(BaseModel model1, BaseModel model2) {
                return model2.getTimeMinSec().compareTo(model1.getTimeMinSec());
            }
        });
        if (baseList.size() == 0) {
            mViewStub.setVisibility(View.VISIBLE);
            txtLiuChu.setText(getString(R.string.ling));
            txtLiuRu.setText(getString(R.string.ling));
            tvAccount.setText(getString(R.string.ling));
        } else {
            tvAccount.setText(String.format("%.2f", model.getMoney()));//设置总金额不变
            mViewStub.setVisibility(View.GONE);
            mAdapter.setData(baseList);
        }
        //避免数据再次重复添加
        zhiChu = 0;
        liuRu = 0;
        for (BaseModel model : baseList) {
            if (model.getZhiChuShouRuType().equals(Config.ZHI_CHU)) {
                zhiChu += model.getMoney();
                txtLiuChu.setText(String.format("%.2f", zhiChu));
            } else {
                liuRu += model.getMoney();
                txtLiuRu.setText(String.format("%.2f", liuRu));
            }
        }
        mRecyclerView.setAdapter(mAdapter.getHeaderAndFooterAdapter());
    }


    public void toData(View view) {
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH);
        //设置标题
        pvTime.setTitle("选择日期");
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR) + 30);
        pvTime.setTime(new Date());
        //设置是否循环
        pvTime.setCyclic(false);
        //设置是否可以关闭
        pvTime.setCancelable(true);
        //设置选择监听
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                txtMonth.setText(DateTimeUtil.getTime(date));
                PayShowList(payShowId, new StringBuilder().append(DateTimeUtil.getTime(date)).append("-01-").append("00-").append("00-").append("00"),
                        new StringBuilder().append(DateTimeUtil.getTime(date)).append("-31").append("-23-").append("59-").append("59"));
            }
        });
        //显示
        pvTime.show();
    }

    public void toYuer(View view) {
        Intent intent = new Intent(PayShowActivity.this, UpdateCommonKeyBoardActivity.class);
        intent.putExtra(Config.SaveAPenPlatform, "YuER");
        startActivityForResult(intent, YuER);
    }

    private static final int YuER = 101;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case YuER:
                    tvAccount.setNumberString(String.format("%.2f", Double.parseDouble(data.getExtras().getString(Config.TextInPut))));
                    if (model.getMAccountType().equals(Config.CASH)) {
                        updateAccountYuer(data.getExtras().getString(Config.TextInPut), Config.CASH);
                    } else if (model.getMAccountType().equals(Config.CXK)) {
                        updateAccountYuer(data.getExtras().getString(Config.TextInPut), Config.CXK);
                    } else if (model.getMAccountType().equals(Config.XYK)) {
                        updateAccountYuer(data.getExtras().getString(Config.TextInPut), Config.XYK);
                    } else if (model.getMAccountType().equals(Config.ZFB)) {
                        updateAccountYuer(data.getExtras().getString(Config.TextInPut), Config.ZFB);
                    } else if (model.getMAccountType().equals(Config.JC)) {
                        updateAccountYuer(data.getExtras().getString(Config.TextInPut), Config.JC);
                    } else if (model.getMAccountType().equals(Config.JR)) {
                        updateAccountYuer(data.getExtras().getString(Config.TextInPut), Config.JR);
                    } else if (model.getMAccountType().equals(Config.WEIXIN)) {
                        updateAccountYuer(data.getExtras().getString(Config.TextInPut), Config.WEIXIN);
                    } else if (model.getMAccountType().equals(Config.CZK)) {
                        updateAccountYuer(data.getExtras().getString(Config.TextInPut), Config.CZK);
                    } else if (model.getMAccountType().equals(Config.TOUZI)) {
                        updateAccountYuer(data.getExtras().getString(Config.TextInPut), Config.TOUZI);
                    } else if (model.getMAccountType().equals(Config.INTENTACCOUNT)) {
                        updateAccountYuer(data.getExtras().getString(Config.TextInPut), Config.INTENTACCOUNT);
                    }
                    break;
            }
        }
    }

    private void updateAccountYuer(String data, String accountType) {
        model.setMoney(Double.parseDouble(data));
        DaoChoiceAccount.updateAccount(model);
        //通知钱包页面数据发送改变
        EventBusUtil.sendEvent(new Event(C.EventCode.PayShowActivityToWalletFragment, true));

        //循环baseList的第一个数据来和插入的数据来比较
        for (int i = 0; i < baseList.size(); i++) {
            tvAccountValue = baseList.get(0).getMoney();
        }
        int s = 1 + (int) (Math.random() * 100);
        final BaseModel baseModel = new BaseModel(DaoChoiceAccount.getCount() + s, R.mipmap.yuebiangeng
                , getString(R.string.Balance_change), getString(R.string.pingzhang), Double.parseDouble(data),
                Config.ChoiceAccount,
                Double.parseDouble(data) > tvAccountValue ? Config.SHOU_RU : Config.ZHI_CHU,
                DateTimeUtil.getCurrentTime_Today(), accountType, payShowId, payShowDate);
        Observable.create(new Observable.OnSubscribe<BaseModel>() {
            @Override
            public void call(Subscriber<? super BaseModel> subscriber) {
                DaoAccountBalance.insert(baseModel);
                subscriber.onNext(baseModel);
            }
        }).compose(RxUtil.<BaseModel>applySchedulers()).subscribe(new Action1<BaseModel>() {
            @Override
            public void call(BaseModel model) {
                //如果不插入首位 则会在末端显示数据
                baseList.add(0, model);
                mAdapter.setData(baseList);
                mRecyclerView.setAdapter(mAdapter.getHeaderAndFooterAdapter());
                mViewStub.setVisibility(View.GONE);
                //更新页面数据 避免数据重复添加
                liuRu = 0;
                zhiChu = 0;
                for (BaseModel bm : baseList) {
                    if (bm.getZhiChuShouRuType().equals(Config.ZHI_CHU)) {
                        zhiChu += bm.getMoney();
                        txtLiuChu.setText(String.format("%.2f", zhiChu));
                    } else {
                        liuRu += bm.getMoney();
                        txtLiuRu.setText(String.format("%.2f", liuRu));
                    }
                }
            }
        });
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(PayShowActivity.this, PayShowDetailActivity.class);
        intent.putExtra(Config.PayShowDetailModel, baseList.get(position));
        startActivity(intent);

    }


    private class PaySHowAdapter extends BGARecyclerViewAdapter<BaseModel> {
        public PaySHowAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_pay_show);
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, BaseModel model) {
            if (needTitle(position)) {
                helper.setVisibility(R.id.item_data, View.VISIBLE);
                String currentData = DateTimeUtil.getCurrentYear();
                String[] timeMinSecs = model.getTimeMinSec().split("-");
                String timeMinSec = timeMinSecs[0] + "-" + timeMinSecs[1] + "-" + timeMinSecs[2];
                if (currentData.equals(timeMinSec)) {
                    helper.setText(R.id.item_data, "今天");
                } else {
                    helper.setText(R.id.item_data, timeMinSec);
                }
            } else {
                helper.setVisibility(R.id.item_data, View.GONE);
            }
            if (model.getZhiChuShouRuType().equals(Config.SHOU_RU)) {
                helper.setText(R.id.item_rd_money, String.format("+%.2f", model.getMoney()));
                helper.setTextColor(R.id.item_rd_money, getResources().getColor(R.color.blue));
            } else {
                helper.setText(R.id.item_rd_money, String.format("-%.2f", model.getMoney()));
            }
            if (model.getType().equals(Config.AccountModel)) {
                helper.setVisibility(R.id.item_rd_txtType, View.VISIBLE);
                helper.setVisibility(R.id.item_rd_txtName, View.GONE);
                helper.setVisibility(R.id.item_rd_txtpingzhang, View.GONE);
            } else {
                helper.setVisibility(R.id.item_rd_txtName, View.VISIBLE);
                helper.setVisibility(R.id.item_rd_txtpingzhang, View.VISIBLE);
                helper.setVisibility(R.id.item_rd_txtType, View.GONE);
            }
            helper.setImageResource(R.id.item_rd_image, model.getUrl());
            helper.setText(R.id.item_rd_txtType, model.getName());

        }

        private boolean needTitle(int position) {
            if (position == 0) {
                return true;
            }
            if (position < 0) {
                return false;

            }
            BaseModel currentModel = getItem(position);
            BaseModel previousModel = getItem(position - 1);
            if (currentModel == null || previousModel == null) {
                return false;
            }
            String currentDatas[] = currentModel.getTimeMinSec().split("-");
            String currentData = currentDatas[0] + "-" + currentDatas[1] + "-" + currentDatas[2];
            String previousDatas[] = previousModel.getTimeMinSec().split("-");
            String previousData = previousDatas[0] + "-" + previousDatas[1] + "-" + previousDatas[2];
            if (currentData.equals(previousData)) {
                return false;
            }
            return true;
        }
    }
}
