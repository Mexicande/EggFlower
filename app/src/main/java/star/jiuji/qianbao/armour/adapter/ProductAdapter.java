package star.jiuji.qianbao.armour.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import star.jiuji.qianbao.R;
import star.jiuji.qianbao.armour.entity.ProductEntity;
import star.jiuji.qianbao.armour.utils.GlideCircleTransform;
import star.jiuji.qianbao.fragment.HomeFragment;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/5/23 下午6:10
 * - @Email whynightcode@gmail.com
 */
public class ProductAdapter extends BaseQuickAdapter<ProductEntity, BaseViewHolder> {

    public ProductAdapter(@Nullable List<ProductEntity> data) {
        super(R.layout.item_product, data);
        openLoadAnimation();
        //多次执行动画
        isFirstOnly(false);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductEntity item) {
        String maximumAmount = item.getMaximum_amount();
        String minimum_amount = item.getMinimum_amount();
        if (minimum_amount.length()>4){
            String substring = minimum_amount.substring(0, minimum_amount.length() - 4);
            minimum_amount = substring + "万";
        }
        if (maximumAmount.length() > 4) {
            String substring = maximumAmount.substring(0, maximumAmount.length() - 4);
            maximumAmount = substring + "万";
        }
        String money = "预计可借" + minimum_amount + "-" + maximumAmount;

        int interestAlgorithm = item.getInterest_algorithm();
        helper.setText(R.id.item_rate,
                interestAlgorithm == 0 ?
                        "日利率: "
                        : "月利率: ");
        helper.setText(R.id.item_name, item.getP_name())
                .setText(R.id.item_money, money)
                .setText(R.id.item_rate_number, item.getMin_algorithm() + "%")
                .setText(R.id.item_fastest_time_number, item.getFastest_time())
                .setText(R.id.item_person_number, item.getApply())
        ;
        if(item.getLabels().size()==0){
            helper.setVisible(R.id.item_tag,false);
        }else {
            helper.setVisible(R.id.item_tag,true);

            helper.setText(R.id.item_tag, item.getLabels().get(0).getName());
        }
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .transform(new GlideCircleTransform())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mContext)
                .load(item.getP_logo())
                .apply(options)
                .into((ImageView) helper.getView(R.id.item_logo));
        helper.addOnClickListener(R.id.item_btn_apply);
    }
}
