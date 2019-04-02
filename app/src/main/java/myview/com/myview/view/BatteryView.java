package myview.com.myview.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import myview.com.myview.R;

/**
 * Crate by E470PD on 2019/4/1
 */
public class BatteryView extends View {
    /*view的宽度和高度*/
    private int mWidth;
    private int mHeight;
    /*画笔*/
    private Paint mPaint;
    //item 个数
    private int mItemCount;
    //间隔线宽度 占 mItemWidth的是十分之一
    private float mSplitWidth;
    //item 高度 目前计算出来宽度的一般
    private float mItemHeight;
    //item 宽度 根据itemcont计算出来
    private float mItemWidth;
    //圆角半径
//    private float mItemRadios;
    //线的宽度
    private float mStorkeWidth;
    //渐变开始颜色
    private int mStartColor;
    //结束渐变颜色
    private int mEndColor;
    //默认背景色
    private int mDefaultColor;
    //动画时长
    private int mAnimDuration;
    //进度值
    private int mProgress;
    /*渐变色百分比*/
    private float franch;
    /*(进度平均值)每一条所占的进度　用来判断进度所占的块数*/
    private float averageProgress;
    /**
     * 公有控件高度属性  用于计算 控件高度={@link #mItemHeight}*{@link #publicViewHeight}
     * 默认是宽度的2.8倍 3倍略显高...根据需求自己设置
     */
    private float publicViewHeight = 2.8f;
    /**
     * 公有文字距离Y轴中心点向下偏移属性
     * 用于绘制文字时 baseLineY 基准线的位置向下的偏移量 以{@link #mItemHeight} 为基础
     * 下面是是计算方式以及中心点注释
     * {@link #drawText(RectF, String, Canvas)}
     * int baseLineY = (int) (rectF.centerY() - top / 2 - bottom / 2 + (mItemHeight / publicextPosition)); 中心点向下便宜
     * (int) (rectF.centerY() - top / 2 - bottom / 2）中心点Y轴 控件的中心点 也是将要绘制的文字中心点
     * (mItemHeight / {@link #publicTextPosition}) 既是向下偏移量
     * 默认为2  也就是二分之一{@link #mItemHeight} 的高度 可以根据显示效果自己调整
     */
    private float publicTextPosition = 2;
    //设置文字所占块的比例 默认0.5
    private float textSizeProportion = 0.5f;

    public BatteryView(Context context) {
        super(context);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BatteryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BatteryView);
        /*item 个数*/
        mItemCount = array.getInt(R.styleable.BatteryView_b_item_count, 15);
        /*圆角半径*/
//        mItemRadios = array.getDimension(R.styleable.BatteryView_b_border_cornor_radius, dp2px(50));
        mStorkeWidth = array.getDimension(R.styleable.BatteryView_b_storke_width, 1f);
        /*充电进度中的开始颜色 -14909263(色值)*/
        mStartColor = array.getInt(R.styleable.BatteryView_b_item_start_color, Color.parseColor("#0E74A7"));
        /*充电完成结束的颜色 -5249793(色值)*/
        mEndColor = array.getInt(R.styleable.BatteryView_b_item_end_color, Color.parseColor("#95D8FF"));
        /*未充电的颜色*/
        mDefaultColor = array.getInt(R.styleable.BatteryView_b_item_default_color, 0xffF2F2F2);
        /*动画时间*/
        mAnimDuration = array.getInt(R.styleable.BatteryView_b_anim_duration, 0);
        mProgress = array.getInt(R.styleable.BatteryView_b_progress, 0);
        array.recycle();
        franch = 1f / mItemCount;
        /*进度均值*/
        averageProgress = 100f / mItemCount;
        initPaint();
    }

    /**
     * 初始化画笔  填充 抗锯齿
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(mStorkeWidth);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    /**
     * 在onMesure中计算过了 所以这个方法在onDraw中就不调用了
     * 计算item 宽高 split 宽
     */
    private void calculate() {
        int width = getWidth();
        float itemAndSpacWidth = width / mItemCount;
        mSplitWidth = itemAndSpacWidth * (0.1f);
        mItemWidth = itemAndSpacWidth - mSplitWidth + itemAndSpacWidth * (0.1f) / 14;
        mItemHeight = mItemWidth / 2;
    }

    /**
     * 绘制所有的默认进度
     *
     * @param canvas
     */
    private void drawDefault(Canvas canvas) {
        mPaint.setColor(mDefaultColor);
        for (int i = 0; i < mItemCount; i++) {
            RectF rectF = new RectF(i * mItemWidth + i * mSplitWidth,
                    0,
                    (i + 1) * mItemWidth + i * mSplitWidth,
                    mItemHeight);
            canvas.drawRect(rectF, mPaint);
        }
    }

    /**
     * 绘制当前进度
     *
     * @param canvas
     */
    private void drawProgress(Canvas canvas) {
        /*当前进度所占进度块个数*/
        int currentProNum = (int) (mProgress / averageProgress);
        for (int i = 0; i < currentProNum; i++) {
            RectF rectF = new RectF(i * mItemWidth + i * mSplitWidth,
                    0,
                    (i + 1) * mItemWidth + i * mSplitWidth,
                    mItemHeight);
            mPaint.setColor(caculateColor(mStartColor, mEndColor, i * franch));
            canvas.drawRect(rectF, mPaint);
            if (isSet) {
                if (i == setProNum - 1) {
                    drawRoundRect(i, canvas, mProgress);
                }
            } else {
                if (i == currentProNum - 1) {
                    drawRoundRect(i, canvas, setCountProgress);
                }
            }
        }
    }


    /**
     * 绘制 最后一个椭圆
     *
     * @param position 当前进度所占最后一块 currentItemCoun
     * @param canvas
     * @param progress 总进度
     */
    private void drawRoundRect(int position, Canvas canvas, int progress) {
        RectF rectRoundF = new RectF(position * mItemWidth + position * mSplitWidth,
                0,
                (position + 1) * mItemWidth + position * mSplitWidth,
                mItemHeight * publicViewHeight);
        canvas.drawRoundRect(rectRoundF, 50, 50, mPaint);
        drawText(rectRoundF, String.valueOf(mProgress), canvas);
    }

    /**
     * 绘制文字
     *
     * @param rectF
     * @param msg
     * @param canvas
     */
    private void drawText(RectF rectF, String msg, Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(24f);
        textPaint.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);//抗锯齿
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离
        //文字基准是基于文字左下角靠近的一个位置 为了向下移动一点 所以这里根据rectF的高度动态设置一下
        int baseLineY = (int) (rectF.centerY() - top / 2 - bottom / 2 + (mItemHeight / publicTextPosition));//基线中间点的y轴计算公式
        //根据文字的初始大小以及绘制宽度 获取宽度与大小的比例 在根据rectf的宽度计算文字的大小
        float width = Math.abs(fontMetrics.ascent) + Math.abs(fontMetrics.descent);
        float size = (width / 24f) * (rectF.width() * textSizeProportion);
        textPaint.setTextSize(size);
        canvas.drawText(msg, rectF.centerX(), baseLineY, textPaint);
    }

    private ObjectAnimator anim;

    /**
     * 开启属性动画 当在充电状态中的时候开启
     */
    public void setAnimation() {
        anim = ObjectAnimator.ofInt(this, "progress", 0, getProgress());
        anim.setDuration(mAnimDuration * 1000);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }

    /**
     * 关闭属性动画
     *
     * @param progress 总体进度 因为this.progress会随着属性动画变化所以直接把真是进度传递进来
     */
    public void closeAnimation(int progress) {
        setProgress(progress);
        if (anim != null) {
            anim.cancel();
        }
    }

    /**
     * 设置进度
     * <p>
     * {@link #setProNum} 属性用于区分属性动画的
     * (既({@link #drawProgress(Canvas)中的currentProNum}))设置的进度应该是第多少块
     * {@link #setCountProgress} 属性用去区分{@link #mProgress} 设置的进度
     * 因为属性动画关联了{@link #mProgress} 所以如果不用另外的属性记录 第多少块和总进度
     * 就会出现一种诡异的现象。。下面你可能卡不明白 但是你可以换成(i==currentProNum-1) 运行试试。。。
     * 因为需要在第n块的时候绘制下半部分 如果直接用{@link #mProgress} 因为关联属性动画的进度
     * 动画开启后调用 invalidate();重绘 {@link #mProgress}会从0 一直加到当前设置的进度
     * {@link #drawProgress(Canvas)}原来是(i==currentProNum-1)现在是(i=={@link #setProNum}-1)
     * 多次原{@link #drawProgress(Canvas)})中的(i=currentProNum-1) 就会出现每一个进度块都会绘制下半部分
     * 因为currentProNum每次都在变化
     * 所以用属性{@link #setProNum}单独记录是第多少块绘制下半部分
     * 同理 {@link #mProgress} 因为属性动画关联也会从0 一直增加到设置的进度值
     * 所以此处用属性{@link #setCountProgress} 单独记录设置的进度
     */
    int setProNum;
    int setCountProgress;
    //是否通过setCurrentProgress 用来区分是否xml 设置进度 走不同逻辑分别显示最后一块的下半部分
    boolean isSet;

    public void setCurrentProgress(int progress) {
        isSet = true;
        setCountProgress = progress;
        setProNum = (int) (progress / averageProgress);
        setProgress(progress);
    }

    /**
     * 设置进度 用于属性动画
     *
     * @param progress
     */
    private void setProgress(int progress) {
        this.mProgress = progress;
        System.out.println("BatteryView.setProgress");
        invalidate();
    }

    private int getProgress() {
        return this.mProgress;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //calculate();
        drawDefault(canvas);
        drawProgress(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //计算item 宽高 split 宽 并设置控件宽高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        float itemAndSpacWidth = width / mItemCount;
        mSplitWidth = itemAndSpacWidth * (0.1f);
        mItemWidth = itemAndSpacWidth - mSplitWidth + itemAndSpacWidth * (0.1f) / 14;
        mItemHeight = mItemWidth / 2;
        setMeasuredDimension(width, (int) (mItemHeight * publicViewHeight));
    }

    /**
     * dp转化为px`
     *
     * @param dp
     * @return
     */
    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }

    /**
     * 计算从startColor过度到endColor过程中百分比为franch时的颜色值
     *
     * @param startColor 起始颜色 int类型
     * @param endColor   结束颜色 int类型
     * @param franch     franch 百分比0.5
     * @return 返回int格式的color
     */
    public static int caculateColor(int startColor, int endColor, float franch) {
        String strStartColor = "#" + Integer.toHexString(startColor);
        String strEndColor = "#" + Integer.toHexString(endColor);
        return Color.parseColor(caculateColor(strStartColor, strEndColor, franch));
    }

    /**
     * 计算从startColor过度到endColor过程中百分比为franch时的颜色值
     *
     * @param startColor 起始颜色 （格式#FFFFFFFF）
     * @param endColor   结束颜色 （格式#FFFFFFFF）
     * @param franch     百分比0.5
     * @return 返回String格式的color（格式#FFFFFFFF）
     */
    public static String caculateColor(String startColor, String endColor, float franch) {

        int startAlpha = Integer.parseInt(startColor.substring(1, 3), 16);
        int startRed = Integer.parseInt(startColor.substring(3, 5), 16);
        int startGreen = Integer.parseInt(startColor.substring(5, 7), 16);
        int startBlue = Integer.parseInt(startColor.substring(7), 16);

        int endAlpha = Integer.parseInt(endColor.substring(1, 3), 16);
        int endRed = Integer.parseInt(endColor.substring(3, 5), 16);
        int endGreen = Integer.parseInt(endColor.substring(5, 7), 16);
        int endBlue = Integer.parseInt(endColor.substring(7), 16);

        int currentAlpha = (int) ((endAlpha - startAlpha) * franch + startAlpha);
        int currentRed = (int) ((endRed - startRed) * franch + startRed);
        int currentGreen = (int) ((endGreen - startGreen) * franch + startGreen);
        int currentBlue = (int) ((endBlue - startBlue) * franch + startBlue);

        return "#" + getHexString(currentAlpha) + getHexString(currentRed)
                + getHexString(currentGreen) + getHexString(currentBlue);

    }

    /**
     * 将10进制颜色值转换成16进制。
     */
    public static String getHexString(int value) {
        String hexString = Integer.toHexString(value);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }
}
