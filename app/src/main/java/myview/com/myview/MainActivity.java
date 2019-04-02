package myview.com.myview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;

import myview.com.myview.view.BatteryView;
import myview.com.myview.view.ChargeView;
import myview.com.myview.view.ClearEditTextHighlight;
import myview.com.myview.view.ClearEditTextPwd;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ChargeView mCvView;
    private ClearEditTextHighlight mCetPhone;
    private ClearEditTextPwd mCetsPwd;
    /**
     * 展示充电动画
     */
    private Button mBtnStart;
    /**
     * 停止充电动画
     */
    private Button mBtnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    BatteryView mBv;

    private void initView() {
        mCvView = findViewById(R.id.cv_view);
        mCetPhone = (ClearEditTextHighlight) findViewById(R.id.cet_phone);
        mCetPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
        mCetsPwd = (ClearEditTextPwd) findViewById(R.id.cets_pwd);
        mCetsPwd.setInputType(InputType.TYPE_CLASS_NUMBER, false);
        mBtnStart = (Button) findViewById(R.id.btnStart);
        mBtnStart.setOnClickListener(this);
        mBtnStop = (Button) findViewById(R.id.btnStop);
        mBtnStop.setOnClickListener(this);
        mBv = findViewById(R.id.bv);
        findViewById(R.id.btn_set).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btnStart:
                mCvView.setProgress(90);
                mCvView.setAnimation();
                break;
            case R.id.btnStop:
                mCvView.closeAnimation(100);
                break;
            case R.id.btn_set:
                mBv.setCurrentProgress(100);
                mBv.setAnimation();
                break;
        }
    }
}
