package star.jiuji.egg_flower.armour.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import star.jiuji.egg_flower.R;
import star.jiuji.egg_flower.armour.entity.CreditBean;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/10 下午5:53
 * - @Email whynightcode@gmail.com
 */
public class CreditAdapter extends BaseQuickAdapter<CreditBean, BaseViewHolder> {

    private final RequestOptions mRequestOptions =
            new RequestOptions()
                    .centerInside()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

    public CreditAdapter(@Nullable List<CreditBean> data) {
        super(R.layout.card_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, CreditBean item) {
        helper.setText(R.id.bank_name, item.getName())
                ;
        Glide.with(mContext).load(item.getPicture())
                .apply(mRequestOptions)
                .into((ImageView) helper.getView(R.id.bank_logo));


    }
}
