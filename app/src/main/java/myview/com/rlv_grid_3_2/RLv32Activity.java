package myview.com.rlv_grid_3_2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import myview.com.myview.R;

/**
 * 关于 GridLayoutManager 的 SpanSizeLookup item 占不同的列数
 */
public class RLv32Activity extends AppCompatActivity {
    RecyclerView rlv32;
    Rlv32Adapter rlv32Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rlv32);
        rlv32 = findViewById(R.id.rlv_32);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("---" + i);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6, GridLayoutManager.VERTICAL, false);
        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //5个时为前两个为3列、后三个为2列
                if (position > 2) {
                    return 3;
                } else {
                    return 2;
                }
            }
        };
        gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setAutoMeasureEnabled(true);
        rlv32.setHasFixedSize(true);
        rlv32.setNestedScrollingEnabled(false);
        rlv32.setLayoutManager(gridLayoutManager);
        rlv32Adapter = new Rlv32Adapter(this, list);
        rlv32.setAdapter(rlv32Adapter);
    }
}
