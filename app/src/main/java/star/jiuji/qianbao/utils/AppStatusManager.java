package star.jiuji.qianbao.utils;

import star.jiuji.qianbao.base.Config;

/**
 * Created by liuwen on 2017/5/5.
 */
public class AppStatusManager {

    public int appStatus = Config.STATUS_FORCE_KILLED;
    public static AppStatusManager appStatusManager;

    private AppStatusManager() {

    }

    public static AppStatusManager getInstance() {
        if (appStatusManager == null) {
            synchronized (AppStatusManager.class) {
                if (appStatusManager == null) {
                    appStatusManager = new AppStatusManager();
                }
            }
        }
        return appStatusManager;
    }


    public int getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(int appStatus) {
        this.appStatus = appStatus;
    }
}
