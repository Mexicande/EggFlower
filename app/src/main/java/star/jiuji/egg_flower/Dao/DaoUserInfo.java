package star.jiuji.egg_flower.Dao;

import java.util.List;

import star.jiuji.egg_flower.base.App;
import star.jiuji.egg_flower.bean.UserInfoModel;
import star.jiuji.egg_flower.bean.UserInfoModelDao;
import star.jiuji.egg_flower.bean.ZhiChuModel;

/**
 * Created by liuwen on 2017/4/13.
 */
public class DaoUserInfo {


    /**
     * 插入对象数据
     * 插入对象为UserInfoModel
     *
     * @param model
     */
    public static void insert(UserInfoModel model) {
        App.getDaoInstant().getUserInfoModelDao().insert(model);
    }

    /**
     * 删除指定的id的数据
     *
     * @param id
     */
    public static void deleteById(long id) {
        App.getDaoInstant().getUserInfoModelDao().deleteByKey(id);

    }

    /**
     * 删除指定的model的数据
     *
     * @param model
     */
    public static void deleteByModel(UserInfoModel model) {
        App.getDaoInstant().getUserInfoModelDao().delete(model);
    }


    /**
     * 删除所有
     */
    public static void deleteAllData() {
        App.getDaoInstant().getUserInfoModelDao().deleteAll();
    }


    /**
     * 更新数据
     *
     * @param model
     */
    public static void update(ZhiChuModel model) {
        App.getDaoInstant().getZhiChuModelDao().update(model);
    }

    /**
     * 查询 UserInfoModel的集合对象
     *
     * @return
     */
    public static List<UserInfoModel> query() {
        return App.getDaoInstant().getUserInfoModelDao().queryBuilder().list();
    }


    //根據Id來查詢用戶的集合對象
    public static List<UserInfoModel> queryById(long id) {
        return App.getDaoInstant().getUserInfoModelDao().queryBuilder().where(UserInfoModelDao.Properties.Id.eq(id)).list();
    }


    //根據用戶名來查詢用戶的集合對象
    public static List<UserInfoModel> queryByUserName(String name) {
        return App.getDaoInstant().getUserInfoModelDao().queryBuilder().where(UserInfoModelDao.Properties.UserName.eq(name)).list();
    }


    /**
     * 获取总数
     * UserInfoModel的总数
     *
     * @return
     */
    public static long getCount() {
        return App.getDaoInstant().getUserInfoModelDao().count();
    }

}
