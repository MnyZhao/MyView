package myview.com.myview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import myview.com.myview.R;


/**
 *
 */

public class ClearEditTextHighlight extends EditText implements View.OnFocusChangeListener, TextWatcher {
    /**
     * 画线
     */
    private Paint mPaint;
    /**
     * 右边可以清除的小图标
     */
    private Drawable mClearDrawable;
    /**
     * 标记
     */
    private static final int DRAWABLE_LEFT = 0; //左边图标
    private static final int DRAWABLE_TOP = 1;
    private static final int DRAWABLE_RIGHT = 2;
    private static final int DRAWABLE_BOTTOM = 3;
    /**
     * 控件是否有焦点
     */
    private boolean hasFocus = true;
    //底部线的颜色
    private int defaultBottomLineColor;
    //是否展示底部线
    private boolean showBottomLine;
    //底部线的宽度
    private float bottomLineWidth;
    //清除功能是否可用
    private boolean disableClean;
    //右边图标的大小
    private float rightDrawableSize;

    //默认图标
    private Drawable defaultLeftDrawable;
    //高亮图标
    private Drawable highlightLeftDrawable;
    //默认图标id
    private int defaultLeftDrawableId;
    //高亮图标id
    private int highlightLeftDrawableId;
    //左侧图标大小
    private int leftDrawableSize;

    public ClearEditTextHighlight(Context context) {
        this(context, null);
    }

    public ClearEditTextHighlight(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditTextHighlight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomAttrs(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {

        //设置对其方式:center_vertical
        setGravity(Gravity.CENTER_VERTICAL);

        //初始化画笔等
        initDrawLine();
        //设置图标
        setDrawable();
        //设置监听
        this.setOnFocusChangeListener(this);
        this.addTextChangedListener(this);
        //默认状态
        updateDrawable(true);
    }

    /**
     * 获取自定义属性
     */
    private void initCustomAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        //获取自定义属性。
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClearEditTextHighlight, defStyleAttr, 0);
        //是否展示底部线
        showBottomLine = ta.getBoolean(R.styleable.ClearEditTextHighlight_mClearEditText_showBottomLine, false);
        //底部线的颜色
        defaultBottomLineColor = ta.getColor(R.styleable.ClearEditTextHighlight_mClearEditText_bottomLineColor, Color.parseColor("#E6E6E6"));
        //底部线的宽度dp
        bottomLineWidth = ta.getDimension(R.styleable.ClearEditTextHighlight_mClearEditText_bottomLineWidth, dip2px(context, 1));
        //清除功能是否可用
        disableClean = ta.getBoolean(R.styleable.ClearEditTextHighlight_mClearEditText_disableClear, true);
        //右边按钮的大小dp
        rightDrawableSize = ta.getDimension(R.styleable.ClearEditTextHighlight_mClearEditText_rightDrawableSize, dip2px(context, 18));
        //左侧默认图片id
        defaultLeftDrawableId = ta.getResourceId(R.styleable.ClearEditTextHighlight_mClearEdittext_leftDrawable_default, R.drawable.login_phone_default);
        //左侧高亮图片id
        highlightLeftDrawableId = ta.getResourceId(R.styleable.ClearEditTextHighlight_mClearEdittext_leftDrawable_Highlight, R.drawable.login_phone);
        leftDrawableSize = (int) ta.getDimension(R.styleable.ClearEditTextHighlight_mClearEditText_leftDrawableSize, dip2px(context,24));
        ta.recycle();
    }

    private void initDrawLine() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(bottomLineWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBottomLine) {
            //画线
            mPaint.setColor(defaultBottomLineColor);
            canvas.drawLine(dip2px(getContext(), 2), this.getHeight() - 1, this.getWidth() - dip2px(getContext(), 2),
                    this.getHeight() - 1, mPaint);
        }
    }

    private void setDrawable() {
        //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片:左上右下（0123）
        mClearDrawable = getCompoundDrawables()[DRAWABLE_RIGHT];
        if (mClearDrawable == null) {
            //获取默认图标
            mClearDrawable = getResources().getDrawable(R.drawable.icon_edit_clear);
        }
        mClearDrawable.setBounds(0, 0, (int) rightDrawableSize, (int) rightDrawableSize);
        setLeftDrawable();
    }


    // 更新删除图片状态: 当内容不为空，而且获得焦点，才显示右侧删除按钮
    private void updateDrawable(boolean hasFocus) {
        if (length() > 0 && hasFocus) {
            if (disableClean) {
                setCompoundDrawables(getCompoundDrawables()[DRAWABLE_LEFT], getCompoundDrawables()[DRAWABLE_TOP], mClearDrawable, getCompoundDrawables()[DRAWABLE_BOTTOM]);
            } else {
                setCompoundDrawables(getCompoundDrawables()[DRAWABLE_LEFT], getCompoundDrawables()[DRAWABLE_TOP], null, getCompoundDrawables()[DRAWABLE_BOTTOM]);
            }
        } else {
            setCompoundDrawables(getCompoundDrawables()[DRAWABLE_LEFT], getCompoundDrawables()[DRAWABLE_TOP], null, getCompoundDrawables()[DRAWABLE_BOTTOM]);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //可以获得上下左右四个drawable，右侧排第二。图标没有设置则为空。
        Drawable rightIcon = getCompoundDrawables()[DRAWABLE_RIGHT];
        if (rightIcon != null && event.getAction() == MotionEvent.ACTION_UP) {
            //检查点击的位置是否是右侧的删除图标
            //注意，使用getRwwX()是获取相对屏幕的位置，getX()可能获取相对父组件的位置
            int leftEdgeOfRightDrawable = getRight() - getPaddingRight()
                    - rightIcon.getBounds().width();
            if (event.getRawX() >= leftEdgeOfRightDrawable) {
                setText("");
                if (mOnClearClickListener != null) {
                    mOnClearClickListener.onClick();
                }
                return true;
            }

        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        updateDrawable(this.hasFocus);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        //更新状态
        if (s.toString().isEmpty()) {
            changeDrawable(true);
        } else {
            changeDrawable(false);
        }
        updateDrawable(hasFocus);
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mClearDrawable = null;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    //右边按钮的点击事件
    public interface OnClearClickListener {
        void onClick();
    }

    private OnClearClickListener mOnClearClickListener;

    //设置监听
    public void setOnClearClickListener(OnClearClickListener mOnClearClickListener) {
        this.mOnClearClickListener = mOnClearClickListener;
    }

    /**
     * 一定要马上调用
     *
     * @param drawableSize  图标大小 最好把dp转成px
     * @param defaultID     默认图标id
     * @param heightlightId 高亮图标id
     */
    public void setLeftDrawable(int drawableSize, int defaultID, int heightlightId) {
        leftDrawableSize = dip2px(getContext(), drawableSize);
        defaultLeftDrawable = getResources().getDrawable(defaultID);
        defaultLeftDrawable.setBounds(0, 0, leftDrawableSize, leftDrawableSize);
        setCompoundDrawables(defaultLeftDrawable, getCompoundDrawables()[DRAWABLE_TOP], null, getCompoundDrawables()[DRAWABLE_BOTTOM]);
        highlightLeftDrawable = getResources().getDrawable(heightlightId);
        highlightLeftDrawable.setBounds(0, 0, leftDrawableSize, leftDrawableSize);
    }

    /**
     * 设置左边的图标
     */
    private void setLeftDrawable() {
        defaultLeftDrawable = getResources().getDrawable(defaultLeftDrawableId);
        defaultLeftDrawable.setBounds(0, 0, leftDrawableSize, leftDrawableSize);
        setCompoundDrawables(defaultLeftDrawable, getCompoundDrawables()[DRAWABLE_TOP], null, getCompoundDrawables()[DRAWABLE_BOTTOM]);
        highlightLeftDrawable = getResources().getDrawable(highlightLeftDrawableId);
        highlightLeftDrawable.setBounds(0, 0, leftDrawableSize, leftDrawableSize);
    }

    /**
     * 当前输入框 是否为空
     *
     * @param isNUll true 是 显示默认  false 否 表示高亮
     */
    private void changeDrawable(boolean isNUll) {
        if (isNUll) {
            setCompoundDrawables(defaultLeftDrawable, getCompoundDrawables()[DRAWABLE_TOP], mClearDrawable, getCompoundDrawables()[DRAWABLE_BOTTOM]);
        } else {
            setCompoundDrawables(highlightLeftDrawable, getCompoundDrawables()[DRAWABLE_TOP], mClearDrawable, getCompoundDrawables()[DRAWABLE_BOTTOM]);
        }
    }
}

