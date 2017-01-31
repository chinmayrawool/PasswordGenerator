package com.mad.passwordgenerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GeneratedPasswords extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_passwords);

        ArrayList<String> ls = (ArrayList<String>) getIntent().getExtras().get(MainActivity.pwd1);

        ArrayList<String> ls1 = (ArrayList<String>) getIntent().getExtras().get(MainActivity.pwd2);


        Toast.makeText(getApplicationContext(),ls.toString(),Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),ls1.toString(),Toast.LENGTH_SHORT).show();

        LinearLayout l1 = (LinearLayout) findViewById(R.id.ln1);
        LinearLayout l2 = (LinearLayout) findViewById(R.id.ln2);
        l1.removeAllViews();
        l2.removeAllViews();
        for(int i=0;i<ls.size();i++) {
            TextView t1 = new TextView(this);
            t1.setText(ls.get(i));

            l2.addView(t1);
        }
        for (int i=0;i<ls1.size();i++)
        {
            TextView t1 = new TextView(this);
            t1.setText(ls1.get(i));

            l1.addView(t1);
        }



        Button f = (Button) findViewById(R.id.finish);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });


    }
}
