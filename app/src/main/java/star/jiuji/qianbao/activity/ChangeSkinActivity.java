package star.jiuji.qianbao.activity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import star.jiuji.qianbao.adapter.BgPicGridAdapter;
import star.jiuji.qianbao.base.BaseActivity;
import star.jiuji.qianbao.base.Config;
import star.jiuji.qianbao.eventBus.C;
import star.jiuji.qianbao.eventBus.Event;
import star.jiuji.qianbao.eventBus.EventBusUtil;
import star.jiuji.qianbao.R;
import star.jiuji.qianbao.utils.BitMapUtils;
import star.jiuji.qianbao.utils.SharedPreferencesUtil;
import star.jiuji.qianbao.bean.BgPicModel;

/**
 * Created by liuwen on 2017/1/20.
 */
public class ChangeSkinActivity extends BaseActivity {
    private GridView mGridView;
    private List<BgPicModel> mList;
    private DrawerLayout mDrawerLayout;
    private BgPicGridAdapter mAdapter;

    @Override
    public int activityLayoutRes() {
        return R.layout.change_skin_activity;
    }

    @Override
    public void initView() {
        setBackView();
        setLeftImage(R.mipmap.fanhui_lan);
        setTitle(getString(R.string.theme));
        setLeftText(getString(R.string.back));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mGridView = (GridView) findViewById(R.id.change_background_grid);
        mList = new ArrayList<>();
        getDataFormAssets();
        if (SharedPreferencesUtil.getStringPreferences(this, Config.ChangeBg, null) != null) {
            Bitmap bitmap = BitMapUtils.getBitmapByPath(this, SharedPreferencesUtil.getStringPreferences(this, Config.ChangeBg, null), false);
            mDrawerLayout.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
        }
        mAdapter = new BgPicGridAdapter(this, mList);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = ((BgPicModel) mAdapter.getItem(position)).path;
                SharedPreferencesUtil.setStringPreferences(ChangeSkinActivity.this, Config.ChangeBg, path);
                Drawable drawable = Drawable.createFromPath(path);
                mDrawerLayout.setBackgroundDrawable(drawable);
                Bitmap bitmap = BitMapUtils.getBitmapByPath(ChangeSkinActivity.this, path, false);
                if (bitmap != null) {
                    mDrawerLayout.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
                }
                EventBusUtil.sendEvent(new Event(C.EventCode.UserPhoto, path));
            }
        });
        mGridView.setAdapter(mAdapter);
    }

    private void getDataFormAssets() {
        AssetManager am = getAssets();
        try {
            String[] drawableList = am.list("bkgs");
            mList = new ArrayList<>();
            for (String path : drawableList) {
                BgPicModel model = new BgPicModel();
                InputStream is = am.open("bkgs/" + path);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                model.path = path;
                model.bitmap = bitmap;
                mList.add(model);
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
