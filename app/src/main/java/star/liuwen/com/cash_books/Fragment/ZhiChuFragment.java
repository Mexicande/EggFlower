package star.liuwen.com.cash_books.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemChildTouchListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemLongClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewHolder;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import star.liuwen.com.cash_books.Activity.EditIncomeAndCostActivity;
import star.liuwen.com.cash_books.Adapter.PopWindowAdapter;
import star.liuwen.com.cash_books.Base.BaseFragment;
import star.liuwen.com.cash_books.Base.Config;
import star.liuwen.com.cash_books.Dao.DaoChoiceAccount;
import star.liuwen.com.cash_books.Dao.DaoZhiChuModel;
import star.liuwen.com.cash_books.MainActivity;
import star.liuwen.com.cash_books.R;
import star.liuwen.com.cash_books.RxBus.RxBus;
import star.liuwen.com.cash_books.RxBus.RxBusResult;
import star.liuwen.com.cash_books.Utils.DateTimeUtil;
import star.liuwen.com.cash_books.Utils.SharedPreferencesUtil;
import star.liuwen.com.cash_books.Utils.ToastUtils;
import star.liuwen.com.cash_books.Utils.VibratorUtil;
import star.liuwen.com.cash_books.bean.AccountModel;
import star.liuwen.com.cash_books.bean.ChoiceAccount;
import star.liuwen.com.cash_books.bean.ZhiChuModel;

/**
 * Created by liuwen on 2017/1/5.
 */
public class ZhiChuFragment extends BaseFragment implements View.OnClickListener, BGAOnItemChildClickListener, BGAOnRVItemClickListener, BGAOnRVItemLongClickListener {

    private List<ZhiChuModel> mList;
    private RecyclerView mRecyclerView;
    private ZhiChuAdapter mAdapter;
    private EditText edName;
    private ImageView imageName;
    private TextView txtName, tvData, tvZhanghu, tvSure;
    private List<AccountModel> homListData;
    private Integer AccountUrl;

    private String AccountType, AccountData, AccountConsumeType, choiceAccount, choiceAccountDate, mEdName;
    private PopupWindow window;
    private ListView mListView;
    private PopWindowAdapter mPopWindowAdapter;
    private TimePickerView pvTime;
    private boolean isShowDelete = false;
    private ChoiceAccount model;

    private double accountMoney;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_zhichu);
        initView();
        initData();
        return getContentView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) getContentView().findViewById(R.id.f_zhichu_recycler);
        tvData = (TextView) getContentView().findViewById(R.id.f_zhichu_data);
        tvZhanghu = (TextView) getContentView().findViewById(R.id.f_zhichu_zhanghu);
        tvSure = (TextView) getContentView().findViewById(R.id.f_zhichu_sure);

        tvData.setOnClickListener(this);
        tvZhanghu.setOnClickListener(this);
        tvSure.setOnClickListener(this);

        choiceAccount = SharedPreferencesUtil.getStringPreferences(getActivity(), Config.TxtChoiceAccount, "");
        choiceAccountDate = SharedPreferencesUtil.getStringPreferences(getActivity(), Config.TxtChoiceAccountDate, "");
        tvZhanghu.setText(choiceAccount.isEmpty() ? "账户" : choiceAccount);
        tvData.setText(choiceAccountDate.isEmpty() ? DateTimeUtil.getCurrentYear() : choiceAccountDate);

        View headView = View.inflate(getActivity(), R.layout.zhichu_shouru_head, null);
        edName = (EditText) headView.findViewById(R.id.zhichu_name);
        imageName = (ImageView) headView.findViewById(R.id.imag_name);
        txtName = (TextView) headView.findViewById(R.id.txt_name);
        txtName.setText(getString(R.string.yiban));
        mAdapter = new ZhiChuAdapter(mRecyclerView);
        mAdapter.addHeaderView(headView);
        final GridLayoutManager manager = new GridLayoutManager(getActivity(), 5, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mList = new ArrayList<>();
        model = new ChoiceAccount();
        if (DaoZhiChuModel.query().size() != 0) {
            mList = DaoZhiChuModel.query();
            mAdapter.setData(mList);
            mRecyclerView.setAdapter(mAdapter.getHeaderAndFooterAdapter());
        }
        mAdapter.addLastItem(new ZhiChuModel(DaoZhiChuModel.getCount(), R.mipmap.icon_add, "编辑"));

        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnRVItemLongClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        if (childView.getId() == R.id.item_cha) {
            mAdapter.removeItem(position);
            DaoZhiChuModel.deleteZhiChuByModel(DaoZhiChuModel.query().get(position));
        }
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        if (mAdapter.getItemCount() - 1 == position) {
            Intent intent = new Intent(getActivity(), EditIncomeAndCostActivity.class);
            intent.putExtra(Config.OTHER, Config.ZHI_CHU);
            startActivity(intent);
        } else {
            txtName.setText(mList.get(position).getNames());
            imageName.setImageResource(mList.get(position).getUrl());
            AccountConsumeType = mList.get(position).getNames();
            AccountUrl = mList.get(position).getUrl();
        }
    }

    @Override
    public boolean onRVItemLongClick(ViewGroup parent, View itemView, int position) {
        if (isShowDelete) {
            isShowDelete = false;
        } else {
            isShowDelete = true;
        }
        VibratorUtil.Vibrate(getActivity(), 70);
        mAdapter.setShowDelete(isShowDelete);
        mAdapter.setClickItemIndex(position);
        return true;
    }

    private void initData() {
        RxBus.getInstance().toObserverableOnMainThread(Config.RxToZhiChuFragment, new RxBusResult() {
            @Override
            public void onRxBusResult(Object o) {
                ZhiChuModel model = (ZhiChuModel) o;
                mAdapter.addItem(mList.size() - 1, model);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().removeObserverable(Config.RxToZhiChuFragment);
        RxBus.getInstance().removeObserverable("AccountModel");
        ToastUtils.removeToast();
    }

    @Override
    public void onClick(View v) {
        if (v == edName) {

        } else if (v == tvZhanghu) {
            showZhanghu();
            if (window.isShowing()) {
                window.dismiss();
            } else {
                window.showAtLocation(tvZhanghu, Gravity.BOTTOM, 0, 0);
                backgroundAlpha(0.5f);
            }
        } else if (v == tvData) {
            showData();
        } else if (v == tvSure) {
            doSure();
        }
    }

    private void doSure() {
        homListData = new ArrayList<>();
        mEdName = edName.getText().toString();
        if (TextUtils.isEmpty(mEdName.trim())) {
            ToastUtils.showToast(getActivity(), "请输入金额");
            return;
        }
        if (tvZhanghu.getText().toString().equals("账户")) {
            ToastUtils.showToast(getActivity(), "请选择账户");
            return;
        }

        if (tvData.getText().toString().equals("日期")) {
            ToastUtils.showToast(getActivity(), "请选择日期");
        }
        homListData.add(new AccountModel(TextUtils.isEmpty(AccountType) ? (choiceAccount.isEmpty() ? "账户" : choiceAccount) : AccountType
                , TextUtils.isEmpty(AccountData) ? (choiceAccountDate.isEmpty() ? DateTimeUtil.getCurrentYear() : choiceAccountDate) : choiceAccountDate,
                Double.parseDouble(mEdName), AccountConsumeType == null ? getString(R.string.yiban) : AccountConsumeType, AccountUrl == null ? R.mipmap.icon_shouru_type_qita :
                AccountUrl, DateTimeUtil.getCurrentTime_Today(), Config.ZHI_CHU));
        RxBus.getInstance().post("AccountModel", homListData);
     // updateChoiceAccountYuer();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("id", 1);
        startActivity(intent);
        getActivity().finish();

    }

    private void updateChoiceAccountYuer() {
        model.setMoney(accountMoney - Double.parseDouble(mEdName));
        DaoChoiceAccount.updateAccount(model);
    }


    private void showZhanghu() {
        View popView = View.inflate(getActivity(), R.layout.pop_zhanghu_dialog, null);
        mListView = (ListView) popView.findViewById(R.id.lv_popup_list);
        window = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mPopWindowAdapter = new PopWindowAdapter(getActivity(), R.layout.item_pop_account);
        if (DaoChoiceAccount.query().size() != 0) {
            mPopWindowAdapter.setData(DaoChoiceAccount.query());
        }
        mListView.setAdapter(mPopWindowAdapter);
        for (int i = 0; i < DaoChoiceAccount.query().size(); i++) {
            model = DaoChoiceAccount.query().get(i);
            if (model.getMAccountType().equals(Config.JC)) {
                mPopWindowAdapter.removeItem(model);
            } else if (model.getMAccountType().equals(Config.JR)) {
                mPopWindowAdapter.removeItem(model);
            }
        }
        window.setFocusable(true);
        window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        window.setAnimationStyle(R.style.AnimBottom);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                window.dismiss();
                AccountType = mPopWindowAdapter.getItem(position).getAccountName();
                tvZhanghu.setText(AccountType);
                //选择账户余额，方便计算
                choiceAccountYuer(AccountType);
                SharedPreferencesUtil.setStringPreferences(getActivity(), Config.TxtChoiceAccount, AccountType);
            }
        });

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    //选择账户余额
    private void choiceAccountYuer(String type) {
        for (int i = 0; i < DaoChoiceAccount.queryByAccountType(type).size(); i++) {
            accountMoney = DaoChoiceAccount.queryByAccountType(type).get(i).getMoney();
        }

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }


    public void showData() {
        //时间选择器
        pvTime = new TimePickerView(getActivity(), TimePickerView.Type.YEAR_MONTH_DAY);
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
                tvData.setText(DateTimeUtil.getYearMonthDay_(date));
                AccountData = DateTimeUtil.getYearMonthDay_(date);
                SharedPreferencesUtil.setStringPreferences(getActivity(), Config.TxtChoiceAccountDate, AccountData);
            }
        });
        //显示
        pvTime.show();
    }


    private class ZhiChuAdapter extends BGARecyclerViewAdapter<ZhiChuModel> {
        private boolean isShowDelete;//根据这个变量来判断是否显示删除图标，true是显示，false是不显示
        private int clickItemIndex = -1;//根据这个变量来辨识选中的current值

        public ZhiChuAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_zhichu_fragment);
        }

        protected void setShowDelete(boolean isShowDelete) {
            this.isShowDelete = isShowDelete;
            mAdapter.notifyDataSetChangedWrapper();
        }

        protected boolean getShowDelete() {
            return isShowDelete;
        }

        protected void setClickItemIndex(int postion) {
            this.clickItemIndex = postion;
        }

        @Override
        protected void setItemChildListener(BGAViewHolderHelper helper, int viewType) {
            super.setItemChildListener(helper, viewType);
            helper.setItemChildClickListener(R.id.item_cha);
            helper.setRVItemChildTouchListener(R.id.item_imag);
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, ZhiChuModel model) {
            TranslateAnimation animation = new TranslateAnimation(1, 4, 1, 2);
            animation.setInterpolator(new OvershootInterpolator());
            animation.setDuration(100);
            animation.setRepeatCount(Animation.INFINITE);
            animation.setRepeatMode(Animation.REVERSE);
            helper.setImageResource(R.id.item_imag, model.getUrl());
            helper.setText(R.id.item_name, model.getNames());
            if (position == mList.size() - 1) {
                helper.setVisibility(R.id.item_cha, View.GONE);
            } else {
                helper.setVisibility(R.id.item_cha, isShowDelete ? View.VISIBLE : View.GONE);
                if (isShowDelete) {
                    helper.getImageView(R.id.item_imag).startAnimation(animation);
                }
            }
        }
    }


//    private void showData() {
//        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
//        dialog.show();
//        DatePicker picker = new DatePicker(getActivity());
//        picker.setDate(Integer.parseInt(DateTimeUtil.getShowCurrentTime()[0]), Integer.parseInt(DateTimeUtil.getShowCurrentTime()[1]));
//        picker.setMode(DPMode.SINGLE);
//        picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
//            @Override
//            public void onDatePicked(String date) {
//                tvData.setText(date);
//                AccountData = date;
//                dialog.dismiss();
//            }
//        });
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setContentView(picker, params);
//        dialog.getWindow().setGravity(Gravity.CENTER);
//    }

}

