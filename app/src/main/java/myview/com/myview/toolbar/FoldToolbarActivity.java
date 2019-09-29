package myview.com.myview.toolbar;

import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import myview.com.myview.R;

/**
 * 折叠toolbat
 */
public class FoldToolbarActivity extends AppCompatActivity {
    CollapsingToolbarLayout clt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fold_toolbar);
        clt = (CollapsingToolbarLayout) findViewById(R.id.clt);
        clt.setTitle("向上滑动折叠");//必须将显示的title设置在CollapsingToolbarLayout上
        int color = Color.parseColor("#5EFFFF");
        clt.setCollapsedTitleTextColor(color);
    }
}
