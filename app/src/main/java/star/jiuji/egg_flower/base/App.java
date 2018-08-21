package star.jiuji.egg_flower.base;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.avos.avoscloud.AVOSCloud;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.meituan.android.walle.WalleChannelReader;
import com.umeng.commonsdk.UMConfigure;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import star.jiuji.egg_flower.Dao.DaoChoiceAccount;
import star.jiuji.egg_flower.R;
import star.jiuji.egg_flower.armour.net.Contacts;
import star.jiuji.egg_flower.bean.DaoMaster;
import star.jiuji.egg_flower.bean.DaoSession;
import star.jiuji.egg_flower.enage.DataEnige;
import star.jiuji.egg_flower.utils.SharedPreferencesUtil;
import star.jiuji.egg_flower.utils.ToastUtils;
import star.jiuji.egg_flower.netState.NetChangeObserver;
import star.jiuji.egg_flower.netState.NetStateReceiver;
import star.jiuji.egg_flower.netState.NetworkUtils;

/**
 * Created by liuwen on 2017/1/6.
 */
public class App extends Application {
    private static App mContext;

    public static int position;//账户position;
    public static boolean isRemindPush = false;
    public static boolean isOpenCodedLock = false;
    public static boolean isOpenBudget = false;
    public static String cycleData = "";
    public static String cycleTime = "";
    public static String UserUrl = Config.RootPath + "head.jpg";


    private static DaoSession mDaoSession;
    public NetworkUtils.NetworkType mNetType;
    private NetStateReceiver netStateReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化内存检测工具
       // mRefWatcher = LeakCanary.install(this);
        //插入greenDao数据
        setUpDataBase();
        //初始化网络监察工具
        initNetChangeReceiver();
        mContext=this;
        initOkGo();

    }

  /*  // 检测内存工具
    public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context
                .getApplicationContext();
        return application.mRefWatcher;
    }*/


    private void setUpDataBase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "Cash_book", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
        if (SharedPreferencesUtil.getBooleanPreferences(this, Config.FistStar, true)) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //这里判断个数==0的时候才插入 不然不插入
                    if (DaoChoiceAccount.getCount() == 0) {
                        DataEnige.InsertAccountData();
                        DataEnige.InsertShouRuData();
                        DataEnige.InsertZHiCHuData();
                        DataEnige.InsertBudgetData();
                    }
                }
            });
        }
    }

    public static DaoSession getDaoInstant() {
        return mDaoSession;
    }


    private void initNetChangeReceiver() {
        //获取当前网络类型
        mNetType = NetworkUtils.getNetworkType(this);

        //定义网络状态的广播接受者
        netStateReceiver = NetStateReceiver.getReceiver();

        //给广播接受者注册一个观察者
        netStateReceiver.registerObserver(netChangeObserver);

        //注册网络变化广播
        NetworkUtils.registerNetStateReceiver(this, netStateReceiver);
    }

    private NetChangeObserver netChangeObserver = new NetChangeObserver() {

        @Override
        public void onConnect(NetworkUtils.NetworkType type) {
            App.this.onNetConnect(type);
        }

        @Override
        public void onDisConnect() {
            App.this.onNetDisConnect();
        }
    };

    protected void onNetDisConnect() {
        ToastUtils.showToast(this, "网络已断开,请检查网络设置");
        mNetType = NetworkUtils.NetworkType.NETWORK_NONE;
    }

    protected void onNetConnect(NetworkUtils.NetworkType type) {
        if (type == mNetType) return; //net not change
        switch (type) {
            case NETWORK_WIFI:
                ToastUtils.showToast(this, "已切换到 WIFI 网络");
                break;
            case NETWORK_MOBILE:
                ToastUtils.showToast(this, "已切换到 2G/3G/4G 网络");
                break;
        }
        mNetType = type;
    }

    //释放广播接受者(建议在 最后一个 Activity 退出前调用)
    public void destroyReceiver() {
        //移除里面的观察者
        netStateReceiver.removeObserver(netChangeObserver);
        //解注册广播接受者,
        NetworkUtils.unRegisterNetStateReceiver(this, netStateReceiver);
    }

    public static App getApplication() {
        return mContext;
    }

    private void initOkGo() {

        AVOSCloud.initialize(this, Contacts.LEAN_ID, Contacts.LEAN_KEY);
        String channel = WalleChannelReader.getChannel(this.getApplicationContext());
        UMConfigure.init(this, Contacts.UMENG_KEY, channel,
                UMConfigure.DEVICE_TYPE_PHONE, "");
        HttpParams params = new HttpParams();
        String name = getString(R.string.appName);
        params.put("market", channel);
        params.put("name", name);
        OkGo.getInstance().init(this)
                .addCommonParams(params);
    }
}
