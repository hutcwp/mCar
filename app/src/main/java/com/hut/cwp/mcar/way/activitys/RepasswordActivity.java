package com.hut.cwp.mcar.way.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.hut.cwp.mcar.R;


public class RepasswordActivity extends Activity {

    private RelativeLayout repassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__user);

        repassword= (RelativeLayout) findViewById(R.id.bu_repassword);

        repassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RepasswordActivity.this,RepasswordActivity2.class);
                //Intent intent = new Intent(RepasswordActivity.this,MainActivity.class);
                startActivity(intent);
                //finish();

            }
        });
    }
}
