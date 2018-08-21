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
import star.jiuji.egg_flower.armour.entity.ProductEntity;
import star.jiuji.egg_flower.armour.utils.GlideCircleTransform;
import star.jiuji.egg_flower.fragment.HomeFragment;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/5/23 下午6:10
 * - @Email whynightcode@gmail.com
 */
public class ProductAdapter extends BaseQuickAdapter<ProductEntity, BaseViewHolder> {

    public ProductAdapter(@Nullable List<ProductEntity> data) {
        super(R.layout.item_product, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductEntity item) {
        helper.setVisible(R.id.item_product_arrow,true);
        String maximumAmount = item.getMaximum_amount();
        if (maximumAmount.length() > 4) {
            String substring = maximumAmount.substring(0, maximumAmount.length() - 4);
            maximumAmount = substring + "万";
        }else {
            maximumAmount=item.getMaximum_amount();
        }

        int interestAlgorithm = item.getInterest_algorithm();
        helper.setText(R.id.tv_month,
                interestAlgorithm == 0 ?
                        "日利率: "
                        : "月利率: ");
        helper.setText(R.id.name, item.getP_name())
                .setText(R.id.maximum_amount, maximumAmount)
                .setText(R.id.tv_rate, item.getMin_algorithm() + "%");
        LinearLayout linearLayout = helper.getView(R.id.credit_tag);

        linearLayout.removeAllViews();

        List<ProductEntity.LabelsBean> labels = item.getLabels();

        for(int i=0;i<labels.size();i++){
            ProductEntity.LabelsBean labelsBean = item.getLabels().get(i);
            addView(linearLayout, labelsBean.getName(), labelsBean.getFont());
            if(i==2){
                break;
            }
        }
    }



    private void addView(LinearLayout linearLayout, String name, String color) {
        if (!TextUtils.isEmpty(color)) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(mContext);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setStroke(1, Color.parseColor(color));
            textView.setBackgroundDrawable(drawable);
            textView.setTextColor(Color.parseColor(color));
            textView.setTextSize(11);
            textView.setText(name);
            textView.setPadding(10,2,10,2);
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            textView.setLayoutParams(layoutParams);
            linearLayout.addView(textView);
        }
    }
}
