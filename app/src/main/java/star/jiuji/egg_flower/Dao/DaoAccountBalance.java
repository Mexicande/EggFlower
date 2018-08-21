package star.jiuji.egg_flower.Dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import star.jiuji.egg_flower.base.App;
import star.jiuji.egg_flower.bean.BaseModel;
import star.jiuji.egg_flower.bean.BaseModelDao;

/**
 * Created by liuwen on 2017/4/7.
 */
public class DaoAccountBalance {

    /**
     * 插入对象数据
     * 插入对象为BaseModel
     *
     * @param model
     */
    public static void insert(BaseModel model) {
        App.getDaoInstant().getBaseModelDao().insert(model);
    }

    /**
     * 插入数据为List<BaseModel></>
     *
     * @param list
     */
    public static void insertList(List<BaseModel> list) {
        App.getDaoInstant().getBaseModelDao().insert((BaseModel) list);
    }


    /**
     * 查询 BaseModel集合对象
     * 都可以按timeMinSecond来排序
     *
     * @return
     */
    public static List<BaseModel> query() {
        List<BaseModel> list = new ArrayList<>();
        list = App.getDaoInstant().getBaseModelDao().queryBuilder().list();
        Collections.sort(list, new Comparator<BaseModel>() {
            @Override
            public int compare(BaseModel model1, BaseModel model2) {
                return model2.getTimeMinSec().compareTo(model1.getTimeMinSec());
            }
        });
        return list;
    }

    /**
     * 根据唯一性标识 choiceAccountId 来查询
     * @param id
     * @return
     */
    public static List<BaseModel> queryById(long id) {
        List<BaseModel> list = new ArrayList<>();
        list = App.getDaoInstant().getBaseModelDao().queryBuilder().where(BaseModelDao.Properties.ChoiceAccountId.eq(id)).build().list();
        Collections.sort(list, new Comparator<BaseModel>() {
            @Override
            public int compare(BaseModel model1, BaseModel model2) {
                return model2.getTimeMinSec().compareTo(model1.getTimeMinSec());
            }
        });
        return list;
    }


    /**
     * 根据两个日期之间来查询数据
     *
     * @param startData
     * @param endData
     * @return
     */
    public static List<BaseModel> queryByIDAndDate(long id, StringBuilder startData, StringBuilder endData) {
        List<BaseModel> list = new ArrayList<>();
        list = App.getDaoInstant().getBaseModelDao().queryBuilder().where(BaseModelDao.Properties.ChoiceAccountId.eq(id), BaseModelDao.Properties.Date.between(startData, endData)).build().list();
        Collections.sort(list, new Comparator<BaseModel>() {
            @Override
            public int compare(BaseModel model1, BaseModel model2) {
                return model2.getTimeMinSec().compareTo(model1.getTimeMinSec());
            }
        });
        return list;
    }


    /**
     * 删除所有
     */
    public static void deleteAllData() {
        App.getDaoInstant().getBaseModelDao().deleteAll();
    }

    /**
     * 按照id来删除
     *
     * @param id
     */
    public static void deleteById(long id) {
        App.getDaoInstant().getBaseModelDao().deleteByKey(id);
    }

    /**
     * 根据model来删除
     *
     * @param model
     */
    public static void deleteByModel(BaseModel model) {
        App.getDaoInstant().getBaseModelDao().delete(model);
    }


    /**
     * 获取总数
     * SaveMoneyPlanModel的总数
     *
     * @return
     */
    public static long getCount() {
        return App.getDaoInstant().getBaseModelDao().count();
    }
}
