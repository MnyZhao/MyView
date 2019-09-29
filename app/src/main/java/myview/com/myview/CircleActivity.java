package myview.com.myview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import myview.com.myview.view.RoundProgress;

public class CircleActivity extends AppCompatActivity {
    RoundProgress roundProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        roundProgress = findViewById(R.id.rp_view);
        Button btnSet = findViewById(R.id.btn_set);
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundProgress.setProgress(50);
            }
        });

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, CircleActivity.class);
        context.startActivity(starter);
    }
}
