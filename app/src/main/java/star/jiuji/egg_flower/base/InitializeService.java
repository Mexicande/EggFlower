package star.jiuji.egg_flower.base;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.avos.avoscloud.AVOSCloud;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.meituan.android.walle.WalleChannelReader;
import com.umeng.commonsdk.UMConfigure;

import star.jiuji.egg_flower.R;
import star.jiuji.egg_flower.armour.net.Contacts;

/**
 *
 * @author apple
 * @date 2018/8/8
 */

public class InitializeService extends IntentService {

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_INIT_WHEN_APP_CREATE = "com.guesslive.caixiangji.service.action.app.create";
    public static final String EXTRA_PARAM = "com.guesslive.caixiangji.service.extra.PARAM";

    public InitializeService( ) {
        super("InitializeService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT_WHEN_APP_CREATE.equals(action)) {
                performInit(EXTRA_PARAM);
            }
        }
    }

    /** * 启动调用 * @param context */
    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    /** * 启动初始化操作 */
    private void performInit(String param) {
        String channel = WalleChannelReader.getChannel(this.getApplicationContext());
        initUmeng(channel);
        initOkgo(channel);
    }

    private void initOkgo(String channel) {
        HttpParams params=new HttpParams();
        String name = getString(R.string.appName);
        params.put("market",channel);
        params.put("name", name);
        OkGo.getInstance().init(App.getApplication())
                .addCommonParams(params);
    }

    private void initUmeng(String channel) {

        UMConfigure.init(this, Contacts.UMENG_KEY, channel, UMConfigure.DEVICE_TYPE_PHONE, null);
    }
}
