package star.liuwen.com.cash_books.View.PieChart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by inf on 2016/11/29.
 */

public class PieChart extends View{


    /**
     * view的宽高
     */
    private int mWidth,mHeight;
    /**
     * 饼状图的半径、内部空白圆的半径
     */
    private float mRadius,mInnerRadius;
    /**
     * 饼状图的外切
     */
    private RectF mPieRect;
    /**
     * 各种画笔
     */
    private Paint mPiePaint,mBlankPaint,mLinePaint,mTextPaint;
    /**
     * 实体类集合
     */
    private List<IPieElement> mElements;
    /**
     * 各个元素的角度
     */
    private List<Float> mAngles=new ArrayList<>();
    /**
     * 元素的颜色
     */
    private List<String> mColors=new ArrayList<>();
    /**
     * 元素的占比
     */
    private List<String> mPercents=new ArrayList<>();
    /**
     * 中心文字
     */
    private String mText;
    public PieChart(Context context) {
        this(context,null);
    }

    public PieChart(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPieRect =new RectF();
        mPiePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPiePaint.setColor(Color.RED);

        mBlankPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mBlankPaint.setColor(Color.WHITE);

        mLinePaint =new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(4);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mTextPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(20);
        mTextPaint.setColor(Color.BLACK);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);

        int width=0,height=0;
        if (widthMode==MeasureSpec.AT_MOST){
            width= (int) getSize();
        }else {
            width=widthSize;
        }
        if (heightMode==MeasureSpec.AT_MOST){
            height= (int) getSize();
        }else {
            height=heightSize;
        }
        int size=Math.min(width,height);
        setMeasuredDimension(size,size);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w-getPaddingLeft()-getPaddingRight();
        mHeight=h-getPaddingTop()-getPaddingBottom();
        mRadius= (float) (Math.min(mWidth, mHeight)/2*0.6);
        mPieRect.left=-mRadius;
        mPieRect.top= -mRadius;
        mPieRect.right=mRadius;
        mPieRect.bottom=mRadius;

        mInnerRadius= (float) (mRadius*0.6);

    }

    private Path mPath=new Path();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth/2,mHeight/2);
        canvas.drawArc(mPieRect,0,90,true,mPiePaint);
        canvas.drawArc(mPieRect,90,1,true,mBlankPaint);
        canvas.drawArc(mPieRect,91,45,true,mPiePaint);
        canvas.drawArc(mPieRect,136,1,true,mBlankPaint);
        canvas.drawArc(mPieRect,137,50,true,mPiePaint);
        //从12点方向开始画
        float sweepedAngle=-90;
        mTextPaint.setTextSize(20);
        for(int i=0;mElements!=null&&mElements.size()>i;i++){
            //设置扇形的颜色
            mPiePaint.setColor(Color.parseColor(mColors.get(i)));
          //  mLinePaint.setColor(Color.parseColor(mColors.get(i)));
            //画扇形
            canvas.drawArc(mPieRect,sweepedAngle,mAngles.get(i),true,mPiePaint);
            //扫过的角度++
            sweepedAngle+=mAngles.get(i);

            String percentText=mPercents.get(i)+"%";
            //画分割线
            canvas.drawArc(mPieRect,sweepedAngle,1,true,mBlankPaint);
            sweepedAngle+=1;
            float x=getXCoordinate(mAngles.get(i),sweepedAngle);
            float y=getYCoordinate(mAngles.get(i),sweepedAngle);
            mPath.reset();
            mPath.moveTo(x,y);
            mPath.lineTo((float)(x*1.2),(float)(y*1.2));
           // canvas.drawPath(mPath,mLinePaint);
            mPath.reset();
            mPath.moveTo((float)(x*1.2),(float)(y*1.2));

            //水平线的长度设置为文字长度的1.3倍
            float horizontalLineLength= (float) (getTextWidth(mTextPaint, percentText)*1.3);
            //当线的起点在第三、四象限时，先把path移动到终点位置，然后向起点画线，使后面画文字时，文字方向是正确的
            if (x<0){
                horizontalLineLength=-horizontalLineLength;
                mPath.moveTo((float)(x*1.2)+horizontalLineLength,(float)(y*1.2));
                mPath.lineTo((float)(x*1.2),(float)(y*1.2));
            }else {
                mPath.lineTo((float)(x*1.2)+horizontalLineLength,(float)(y*1.2));

            }
            mPath.close();
           // canvas.drawPath(mPath,mLinePaint);
            //垂直方向的偏移量，画文字时，文字显示在path的下方，为了让文字显示在上方，设置一个文字高度的垂直偏移量
            float offsetV=-getTextHeight(mTextPaint,percentText);
           // canvas.drawTextOnPath(percentText,mPath,0,offsetV,mTextPaint);


        }

        //这里开始画中心空白部分以及文字，空白部分半径设置为整个圆半径的0.6倍

        canvas.drawCircle(0, 0, mInnerRadius, mBlankPaint);
        double index=3;
        String longerText=mText.substring(0, (int) index);
        calculateTextPaint(mText);
        mTextPaint.setTextSize(30);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(longerText,0,0,mTextPaint);
        canvas.drawText(mText.substring((int) (index)), 0, getTextHeight(mTextPaint,mText)+13, mTextPaint);
    }

    /**
     * 把文字分两行，并画在圆内接正方形内，依此计算画笔的textSize
     * @param text
     */
    private void calculateTextPaint(String text){
        if (!TextUtils.isEmpty(text)){
            measureText(text,100);
        }
    }

    /**
     * 递归调用，计算testSize
     * @param text
     * @param textSize
     */
    private void measureText(String text,int textSize){
        mTextPaint.setTextSize(textSize);
        float width=getTextWidth(mTextPaint,text);
        float height=getTextHeight(mTextPaint,text);
        if (width>mInnerRadius*1.41421){
            textSize--;
            measureText(text,textSize);
            return;
        }
        if (height*2.5>mInnerRadius*1.41421){
            textSize--;
            measureText(text,textSize);
        }
    }
    private float getTextHeight(Paint paint,String text){
        Rect rect=new Rect();
        paint.getTextBounds(text,0,text.length(),rect);
        return rect.height();
    }
    private float getTextWidth(Paint paint,String text){
        Rect rect=new Rect();
        paint.getTextBounds(text,0,text.length(),rect);
        return rect.width();
    }

    private float getXCoordinate(float angle,float sweepedAngle){
        float x= (float) (mRadius*Math.cos(Math.toRadians(sweepedAngle-angle/2)));
        return x;

    }
    private float getYCoordinate(float angle,float sweepedAngle){
        float y= (float) (mRadius*Math.sin(Math.toRadians(sweepedAngle-angle/2)));
        return y;

    }

    private float getSize(){
        Display display= ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        float widht=display.getWidth();
        float height=display.getHeight();
        return Math.min(widht,height);
    }

    public void setData(List<IPieElement> elements){
        mElements=elements;
        setValuesAndColors();
        invalidate();
    }

    /**
     * 设置中心文字
     * @param text
     */
    public void setTitleText(String text){
        mText=text;
    }

    /**
     * 计算角度值和各个值的占比
     */
    private void setValuesAndColors(){
        float sum=0;
        if (mElements!=null&&mElements.size()>0){
            for(IPieElement ele:mElements){
                sum+=ele.getValue();
                mColors.add(ele.getColor());
            }
            BigDecimal totleAngel=BigDecimal.valueOf(360-mElements.size());
            for(int i=0;i<mElements.size();i++){
                IPieElement ele=mElements.get(i);
                BigDecimal bigDecimal=new BigDecimal(String.valueOf(ele.getValue()));
                BigDecimal sumBigDecimal=BigDecimal.valueOf(sum);
                BigDecimal res=bigDecimal.divide(sumBigDecimal,5,BigDecimal.ROUND_HALF_UP);
                //计算角度
                BigDecimal angle=res.multiply(totleAngel);
                mAngles.add(angle.floatValue());
                //计算百分比保留两位小数并保存
                mPercents.add(bigDecimal.multiply(new BigDecimal(100)).divide(sumBigDecimal,2,BigDecimal.ROUND_HALF_UP).toPlainString());
            }
        }

    }
}
