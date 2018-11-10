package myview.com.myview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import myview.com.myview.R;

/**
 * {@link #getText()} 获取文本
 * {@link #setLeftDrawable(int, int, int)} 设置左侧图标大小，高亮以及默认图标
 * {@link #setHiit(int)} 设置显示hiit 资源文本
 * {@link #setHiit(String)} 设置显示hiit文本
 * {@link #setInputType(int, boolean)} 设置输入类型以及是否pin码
 * {@link #setMaxLength(int)} 设置支持最大长度
 */
public class ClearEditTextPwd extends LinearLayout {
    /*edittext密码输入框*/
    private ClearEditTextPwdBase mCetPwd;
    /*右侧小眼睛*/
    private ImageView mIvShowPwd;
    /*右侧删除*/
    private ImageView mIvDelete;
    /*是否显示密码*/
    private boolean pwdIsVisible = false;
    /*是否持有焦点*/
    public boolean isFocus;

    public ClearEditTextPwd(Context context) {
        super(context);
        initView(context);
    }

    public ClearEditTextPwd(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ClearEditTextPwd(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.edittext_layout, this, true);
        mCetPwd = view.findViewById(R.id.cet_pwd);
        mCetPwd.setSingleLine();
        mIvShowPwd = view.findViewById(R.id.ed_iv_right_show_pwd);
        mIvDelete = view.findViewById(R.id.ed_iv_right_delete);
        isShown(false);
        mCetPwd.addTextChangedListener(textWatcher);
        mCetPwd.setOnFocusChangeListener(fousListener);
        mIvShowPwd.setOnClickListener(clickListener);
        mIvDelete.setOnClickListener(clickListener);
        setPwdIsVisible(pwdIsVisible);
    }

    OnFocusChangeListener fousListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            isFocus = hasFocus;
            upRightView(isFocus);
        }
    };

    /**
     * 是否持有焦点 true  yes  false no
     * 持有焦点且eidttext 内容不为null则显示
     *
     * @param isVisible
     */
    private void upRightView(boolean isVisible) {
        if (isVisible) {
            isShown(!mCetPwd.getText().toString().isEmpty());
        } else {
            isShown(false);
        }
    }

    OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ed_iv_right_show_pwd:
                    if (pwdIsVisible) {
                        pwdIsVisible = false;
                        setPwdIsVisible(pwdIsVisible);
                    } else {
                        pwdIsVisible = true;
                        setPwdIsVisible(pwdIsVisible);
                    }
                    break;
                case R.id.ed_iv_right_delete:
                    mCetPwd.setText("");
                    break;
            }
        }
    };
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            /*文字为空则不显示右侧按钮*/
            upRightView(isFocus);
        }
    };

    /**
     * 是否显示右侧可见密码以及删除按钮
     *
     * @param isShow false 表示不显示 true 表示显示
     */
    private void isShown(boolean isShow) {
        if (isShow) {
            mIvShowPwd.setVisibility(View.VISIBLE);
            mIvDelete.setVisibility(View.VISIBLE);
        } else {
            mIvShowPwd.setVisibility(View.INVISIBLE);
            mIvDelete.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 是否显示密码 默认为不显示 false　不显示　　true　显示
     *
     * @param isPwdVisible
     */
    private void setPwdIsVisible(boolean isPwdVisible) {
        if (isPwdVisible) {
            //设置密码为明文，并更改眼睛图标
            mCetPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mIvShowPwd.setImageResource(R.drawable.pwd_hide);
        } else {
            //设置密码为暗文，并更改眼睛图标
            mCetPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mIvShowPwd.setImageResource(R.drawable.pwd_show);
        }
        //设置光标位置的代码需放在设置明暗文的代码后面
        mCetPwd.setSelection(mCetPwd.getText().toString().length());
    }

    /**
     * 获取文本
     *
     * @return
     */
    public String getText() {
        return mCetPwd.getText().toString().trim();
    }

    /**
     * @param drawableSize  图标大小 最好把dp转成px
     * @param defaultID     默认图标id
     * @param heightlightId 高亮图标id
     */
    public void setLeftDrawable(int drawableSize, int defaultID, int heightlightId) {
        mCetPwd.setLeftDrawable(drawableSize, defaultID, heightlightId);
    }

    /**
     * 添加监听事件
     *
     * @param textWatcher
     */
    public void addTextChangedListener(TextWatcher textWatcher) {
        mCetPwd.addTextChangedListener(textWatcher);
    }

    /**
     * 设置默认显示内容
     *
     * @param hiit
     */
    public void setHiit(String hiit) {
        mCetPwd.setHint(hiit);
    }

    /**
     * 设置默认显示内容
     *
     * @param hiit
     */
    public void setHiit(int hiit) {
        mCetPwd.setHint(hiit);
    }

    /**
     * InputType.TYPE_NUMBER_VARIATION_PASSWORD  表示数字型密码
     * InputType.TYPE_CLASS_NUMBER 一起输入显示隐藏
     *
     * @param type
     * @param isPin 是否pin 码   true 表示是  默认隐藏不显示明文
     */
    public void setInputType(int type, boolean isPin) {
        mCetPwd.setInputType(type);
        if (isPin) {
            mCetPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    /**
     * 设置最大长度
     *
     * @param length
     */
    public void setMaxLength(int length) {
        InputFilter[] filters = {new InputFilter.LengthFilter(length)};
        mCetPwd.setFilters(filters);
    }

}
