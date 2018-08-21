package star.jiuji.egg_flower.adapter;

import android.support.v7.widget.RecyclerView;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import star.jiuji.egg_flower.bean.ZhiChuModel;
import star.jiuji.egg_flower.R;

/**
 * Created by liuwen on 2017/1/18.
 */
public class ZhiChuAdapter extends BGARecyclerViewAdapter<ZhiChuModel> {

    public ZhiChuAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_zhichu_fragment);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, ZhiChuModel model) {
        helper.setImageResource(R.id.item_imag, model.getUrl());
        helper.setText(R.id.item_name, model.getNames());
    }

}
