package star.jiuji.egg_flower.base;

import android.os.Environment;

/**
 * Created by liuwen on 2017/1/10.
 */
public class Config {

    public static final String RootPath = getRootPath() + "/Cash_Books/"; //上传经过压缩好的相册
    public static final String SHARED_PREFERENCES_FILE_NAME = "Jzb";

    public static final String PhotoPath = getRootPath() + "/cashPhoto"; //存放拍好的相册

    private static String getRootPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }


    public static String[] reportsColor = new String[]{"#EF427B", "#5EBF93", "#3FA7D6", "#80AF1D", "#BE7777", "#6767AA", "#ED9342", "#CC6705",
            "#899275", "#E05C29", "#D1B95E", "#6C84B7", "#DF3D3D", "#076E4F", "#FE473E", "#DD6133", "#FD567E", "#D53163",
            "#975AAE", "#0BACE9", "#B42914", "#4A79D0", "#5D9C8D", "#4D7003", "#CF0E13", "#E0A904", "#FCB578",
            "#105B3B", "#3C5A99", "#B9975F", "#E96297", "#D18B38", "#80AF1D", "#344860", "#9DD2E6",
            "#AD254C", "#A688C4", "#936651", "#936651", "#E0A904", "#FE673E"};


    public final static String isRemindPush = "isRemindPush";
    public final static String isOpenCodedLock = "isOpenCodedLock";
    public final static String isBudgetPush = "isBudgetPush";
    public final static String ChangeBg = "ChangeBg";


    public final static String InviteAReward = "https://www.qeeniao.com/news/27";
    public final static String NewPersonGift = "https://www.qeeniao.com/news/29";
    public final static String EveryDaySignIn = "https://www.qeeniao.com/activity";
    public final static String SafetyGuarantee = "https://www.qeeniao.com/news/21";


    public final static String ZHI_CHU = "支出";
    public final static String SHOU_RU = "收入";
    public final static String CASH = "现金";
    public final static String CXK = "储蓄卡";
    public final static String XYK = "信用卡";
    public final static String ZFB = "支付宝";
    public final static String JC = "借出";
    public final static String JR = "借入";
    public final static String OTHER = "OTHER";
    public final static String WEIXIN = "微信";
    public final static String CZK = "储值卡";
    public final static String TOUZI = "投资账户";
    public final static String INTENTACCOUNT = "网络账户";
    public final static String HUOQI = "随存随取";
    public final static String DINGQI = "封闭期";

    public final static String ChoiceAccount = "ChoiceAccount";
    public final static String AccountModel = "AccountModel";
    public final static String PayShowDetailModel = "PayShowDetailModel";


    public final static String userNickName = "userNickName";
    public final static String userSignature = "userSignature";
    public final static String userSex = "userSix";
    public static final String UserName = "UserName";
    public static final String UserPassWord = "UserPassWord";
    public static final String UserLocation = "UserLocation";


    public final static String HuoBICh = "HuoBICh";
    public final static String HuoBIEn = "HuoBIEn";

    public static final String SHARE_LOGO = RootPath + "/share/logo/";


    public static final String LockPassword = "LockPassword";
    public static final String PlanSaveMoneyModel = "PlanSaveMoneyModel";

    public static final String FistStar = "FirstStar";


    public static final String PlanMoney = "PlanMoney";
    public static final String PlanFinishTime = "PlanFinishTime";
    public static final String PlanRemark = "PlanRemark";
    public static final String PlanIsPut = "PlanIsPut";

    public static final String SaveAPenPlatform = "SaveAPenPlatform";
    public static final String TextInPut = "TextInPut";

    public static final String TxtChoiceAccountPop = "TxtChoiceAccountPop";
    public static final String TxtMoney = "TxtMoney";
    public static final String TxtPercent = "TxtPercent";
    public static final String TxtRemark = "TxtRemark";
    public static final String TxtAccountName = "TxtAccountName";
    public static final String TxtAccountMoney = "TxtAccountMoney";
    public static final String TxtCreditLimit = "TxtCreditLimit";
    public static final String TxtDebt = "TxtDebt";
    public static final String TxtYuEr = "TxtYuer";
    public static final String TxtAccount = "TxtAccount";
    public static final String TxtPlanMoney = "TxtPlanMoney";
    public static final String TxtPlanRemark = "TxtPlanRemark";
    public static final String TxtNickName = "TxtNickName";
    public static final String TxtSigNature = "TxtSigNature";
    public static final String TxtChoiceAccount = "TxtChoiceAccount";
    public static final String TxtChoiceAccountDate = "TxtChoiceAccountDate";
    public static final String TxtForgetGesturePassword = "TxtForgetGesturePassword";
    public static final String TxtZhiChuId = "TxtZhiChuId";
    public static final String TxtSearchResult = "TxtSearchResult";
    public static final String TxtIssuingBank = "TxtIssuingBank";
    public static final String TxtBudgetYuSuan = "TxtBudgetYuSuan";

    public static final String IntentTag_ToWebView = "IntentTag_ToWebView";

    public static final String ModelWallet = "ModelWallet";
    public static final String ModelChoiceAccount = "ModelChoiceAccount";
    public static final String ModelBudget = "ModelBudget";

    public static final int STATUS_FORCE_KILLED = -1;//程序被crash
    public static final int STATUS_NORMAL = 1; //正常状态
    public static final int STATUS_KICK_OUT = 2;//TOKEN失效或者被踢下线
    public static final String KEY_HOME_ACTION = "key_home_action";//返回到主页面
    public static final int ACTION_BACK_TO_HOME = 6; //默认值
    public static final int ACTION_RESTART_APP = 9;//被强杀
    public static final int ACTION_KICK_OUT = 10;//被踢出
}
