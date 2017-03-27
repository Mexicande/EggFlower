package star.liuwen.com.cash_books.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import star.liuwen.com.cash_books.Base.BaseActivity;
import star.liuwen.com.cash_books.Base.Config;
import star.liuwen.com.cash_books.Dao.DaoChoiceAccount;
import star.liuwen.com.cash_books.MainActivity;
import star.liuwen.com.cash_books.R;
import star.liuwen.com.cash_books.RxBus.RxBus;
import star.liuwen.com.cash_books.Utils.DateTimeUtil;
import star.liuwen.com.cash_books.Utils.SharedPreferencesUtil;
import star.liuwen.com.cash_books.View.DatePickerDialog;
import star.liuwen.com.cash_books.bean.ChoiceAccount;

/**
 * Created by liuwen on 2017/2/17.
 */
public class PaySettingsActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout reBank, reAccount, reType, reMoney, reCreditLimit, reDebt, reDebtData;
    private TextView txtBank, txtAccount, txtType, txtMoney, txtCreditLimit, txtDebt, txtDebtData;
    private ChoiceAccount model;
    private DatePickerDialog dialog;
    private int position;
    private HashMap<Integer, ChoiceAccount> mHashMap = new HashMap<>();

    @Override
    public int activityLayoutRes() {
        return R.layout.pay_settings_activity;
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.pay_setting));
        setBackView();
        setLeftText(getString(R.string.back));
        setLeftImage(R.mipmap.fanhui_lan);
        setRightImage(R.mipmap.account_add_shanchu, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHashMap.put(position, model);
                RxBus.getInstance().post(Config.RxPaySettingToWalletFragment, mHashMap);
                Intent intent = new Intent(PaySettingsActivity.this, MainActivity.class);
                intent.putExtra("id", 2);
                startActivity(intent);

            }
        });

        reBank = (RelativeLayout) findViewById(R.id.re_setting_bank);
        reAccount = (RelativeLayout) findViewById(R.id.re_setting_account);
        reType = (RelativeLayout) findViewById(R.id.re_setting_tyoe);
        reMoney = (RelativeLayout) findViewById(R.id.re_setting_money);
        reCreditLimit = (RelativeLayout) findViewById(R.id.re_setting_Credit_limit);
        reDebt = (RelativeLayout) findViewById(R.id.re_setting_debt);
        reDebtData = (RelativeLayout) findViewById(R.id.re_setting_debt_date);

        txtBank = (TextView) findViewById(R.id.setting_txt_bank);
        txtAccount = (TextView) findViewById(R.id.setting_txt_account);
        txtType = (TextView) findViewById(R.id.setting_txt_type);
        txtMoney = (TextView) findViewById(R.id.setting_txt_money);
        txtCreditLimit = (TextView) findViewById(R.id.setting_txt_Credit_limit);
        txtDebt = (TextView) findViewById(R.id.setting_txt_debt);
        txtDebtData = (TextView) findViewById(R.id.setting_txt_debt_date);

        dialog = new DatePickerDialog(this);
        dialog.setCallback(new DatePickerDialog.OnClickCallback() {
            @Override
            public void onCancel() {
                dialog.dismiss();
            }

            @Override
            public void onSure(String data) {
                txtDebtData.setText(data);
                dialog.dismiss();
            }
        });

        reBank.setOnClickListener(this);
        reAccount.setOnClickListener(this);
        reType.setOnClickListener(this);
        reMoney.setOnClickListener(this);
        reCreditLimit.setOnClickListener(this);
        reDebt.setOnClickListener(this);
        reDebtData.setOnClickListener(this);
        position = getIntent().getIntExtra(Config.Position, 0);
        model = (ChoiceAccount) getIntent().getExtras().getSerializable(Config.ModelWallet);
        if (model != null) {
            txtType.setText(model.getMAccountType());
            txtMoney.setText(model.getMoney() + "");
            if (model.mAccountType.equals(Config.XYK)) {
                reMoney.setVisibility(View.GONE);
                txtAccount.setText(SharedPreferencesUtil.getStringPreferences(this, Config.TxtAccountMoney, "").isEmpty() ? model.getAccountName() : SharedPreferencesUtil.getStringPreferences(this, Config.TxtAccountName, ""));
                txtBank.setText(SharedPreferencesUtil.getStringPreferences(this, Config.TxtAccountMoney, "").isEmpty() ? getString(R.string.empty_string) : SharedPreferencesUtil.getStringPreferences(this, Config.TxtAccountName, ""));
                txtCreditLimit.setText(SharedPreferencesUtil.getStringPreferences(this, Config.TxtCreditLimit, "").isEmpty() ? getString(R.string.ling) : SharedPreferencesUtil.getStringPreferences(this, Config.TxtCreditLimit, ""));
                txtDebt.setText(SharedPreferencesUtil.getStringPreferences(this, Config.TxtDebt, "").isEmpty() ? getString(R.string.ling) : SharedPreferencesUtil.getStringPreferences(this, Config.TxtDebt, ""));
            } else if (model.mAccountType.equals(Config.CXK)) {
                txtAccount.setText(SharedPreferencesUtil.getStringPreferences(this, Config.TxtAccountMoney, "").isEmpty() ? model.getAccountName() : SharedPreferencesUtil.getStringPreferences(this, Config.TxtAccountName, ""));
                txtBank.setText(SharedPreferencesUtil.getStringPreferences(this, Config.TxtAccountMoney, "").isEmpty() ? getString(R.string.empty_string) : SharedPreferencesUtil.getStringPreferences(this, Config.TxtAccountName, ""));
                setAccountVisible();
            } else if (model.mAccountType.equals(Config.ZFB)) {
                reBank.setVisibility(View.GONE);
                txtAccount.setText(SharedPreferencesUtil.getStringPreferences(this, Config.TxtAccountMoney, "").isEmpty() ? model.getAccountName() : SharedPreferencesUtil.getStringPreferences(this, Config.TxtAccountName, ""));
                setAccountVisible();
            } else if (model.mAccountType.equals(Config.CASH)) {
                txtAccount.setText(SharedPreferencesUtil.getStringPreferences(this, Config.TxtAccountMoney, "").isEmpty() ? model.getAccountName() : SharedPreferencesUtil.getStringPreferences(this, Config.TxtAccountName, ""));
                reBank.setVisibility(View.GONE);
                setAccountVisible();
            } else if (model.mAccountType.equals(Config.JC)) {
                reBank.setVisibility(View.GONE);
                setAccountVisible();
            } else if (model.mAccountType.equals(Config.JR)) {
                reBank.setVisibility(View.GONE);
                setAccountVisible();
            }
        }
    }

    /**
     * 通用的隐藏
     */
    public void setAccountVisible() {
        reCreditLimit.setVisibility(View.GONE);
        reDebt.setVisibility(View.GONE);
        reDebtData.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(PaySettingsActivity.this, UpdateCommonKeyBoardActivity.class);
        if (v == reBank) {
            startActivityForResult(new Intent(PaySettingsActivity.this, ChoiceIssuingBankActivity.class), ChoiceIssuingBank);
        } else if (v == reAccount) {
            intent.putExtra(Config.SaveAPenPlatform, "AccountName");
            startActivityForResult(intent, ACCOUNT);
        } else if (v == reMoney) {
            intent.putExtra(Config.SaveAPenPlatform, "AccountMoney");
            startActivityForResult(intent, MONEY);
        } else if (v == reCreditLimit) {
            intent.putExtra(Config.SaveAPenPlatform, "CreditLimit");
            startActivityForResult(intent, CreditLimit);
        } else if (v == reDebt) {
            intent.putExtra(Config.SaveAPenPlatform, "Debt");
            startActivityForResult(intent, Debt);
        } else if (v == reDebtData) {
            dialog.show();
        }
    }

    private static final int ACCOUNT = 101;
    private static final int MONEY = 102;
    private static final int CreditLimit = 103;
    private static final int Debt = 104;
    private static final int ChoiceIssuingBank = 105;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case ACCOUNT:
                txtAccount.setText(data.getExtras().getString(Config.TextInPut));
                if (model.getMAccountType().equals(Config.CASH)) {
                    ChoiceAccount model = DaoChoiceAccount.query().get(0);
                    model.setAccountName(data.getExtras().getString(Config.TextInPut));
                    DaoChoiceAccount.updateAccount(model);
                    RxBus.getInstance().post(Config.CASH, true);
                } else if (model.getMAccountType().equals(Config.CXK)) {
                    ChoiceAccount model = DaoChoiceAccount.query().get(1);
                    model.setAccountName(data.getExtras().getString(Config.TextInPut));
                    DaoChoiceAccount.updateAccount(model);
                    RxBus.getInstance().post(Config.CASH, true);
                } else if (model.getMAccountType().equals(Config.XYK)) {
                    ChoiceAccount model = DaoChoiceAccount.query().get(2);
                    model.setAccountName(data.getExtras().getString(Config.TextInPut));
                    DaoChoiceAccount.updateAccount(model);
                    RxBus.getInstance().post(Config.CASH, true);
                } else if (model.getMAccountType().equals(Config.ZFB)) {
                    ChoiceAccount model = DaoChoiceAccount.query().get(3);
                    model.setAccountName(data.getExtras().getString(Config.TextInPut));
                    DaoChoiceAccount.updateAccount(model);
                    RxBus.getInstance().post(Config.CASH, true);
                } else if (model.getMAccountType().equals(Config.JC)) {
                    ChoiceAccount model = DaoChoiceAccount.query().get(4);
                    model.setAccountName(data.getExtras().getString(Config.TextInPut));
                    DaoChoiceAccount.updateAccount(model);
                    RxBus.getInstance().post(Config.CASH, true);
                } else if (model.getMAccountType().equals(Config.JR)) {
                    ChoiceAccount model = DaoChoiceAccount.query().get(5);
                    model.setAccountName(data.getExtras().getString(Config.TextInPut));
                    DaoChoiceAccount.updateAccount(model);
                    RxBus.getInstance().post(Config.CASH, true);
                }
                break;
            case MONEY:
                txtMoney.setText(data.getExtras().getString(Config.TextInPut));
                break;
            case CreditLimit:
                txtCreditLimit.setText(data.getExtras().getString(Config.TextInPut));
                break;
            case Debt:
                txtDebt.setText(data.getExtras().getString(Config.TextInPut));
                break;
            case ChoiceIssuingBank:
                txtBank.setText(data.getExtras().getString("bank"));
                break;
        }
    }
}
