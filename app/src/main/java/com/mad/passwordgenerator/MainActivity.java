/*
Assignment 3 in class
Group 47
Chinmay Rawool
Nishanth Dilly
*/



package com.mad.passwordgenerator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextSwitcher;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    Executor threadPool;
    int x;
    Handler handler;
    boolean flag;

    int countThread,lengthThread,countAsync=0, lengthAsync, totalCount;
    static  int clt;
    SeekBar s3,s4;
    TextView r3,r4;
    ProgressDialog progressDialog;
    ArrayList<String> password;
    static String pwd1 ="PASSWORD";
    static String pwd2 = "PASSWORDS";
    ArrayList<String> passwords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        final Intent intent = new Intent(MainActivity.this,GeneratedPasswords.class);
        SeekBar sb_thread_count= (SeekBar) findViewById(R.id.seekBarThreadCount);
        SeekBar sb_thread_length= (SeekBar) findViewById(R.id.seekBarThreadLength);
        flag = true;
        s3 = (SeekBar) findViewById(R.id.seekBar3);
        s4 = (SeekBar) findViewById(R.id.seekBar4);
        r3 = (TextView)findViewById(R.id.pg_textViewARC);

        r4  = (TextView)findViewById(R.id.pg_textViewARL);
        passwords = new ArrayList<String>();

        password = new ArrayList<String>();

        ((TextView)findViewById(R.id.textViewThreadCountOutput)).setText((sb_thread_count.getProgress()+1)+"");
        ((TextView)findViewById(R.id.textViewThreadLengthOutput)).setText((sb_thread_length.getProgress()+7)+"");
        ((TextView)findViewById(R.id.pg_textViewARC)).setText((sb_thread_count.getProgress()+1)+"");
        ((TextView)findViewById(R.id.pg_textViewARL)).setText((sb_thread_length.getProgress()+7)+"");
        sb_thread_count.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progress = progress+1;
                ((TextView) findViewById(R.id.textViewThreadCountOutput)).setText(progress+"");
                countThread=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_thread_length.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progress = progress+7;
                ((TextView) findViewById(R.id.textViewThreadLengthOutput)).setText(progress+"");
                lengthThread=progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        s3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                r3.setText((1+progress)+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        s4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                r4.setText((7+progress)+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.d("handle", msg.what+"");
                switch (msg.what) {
                    case ThreadPassGenerate.STATUS_DONE: {

                        //progressDialog.dismiss();
                        passwords = msg.getData().getStringArrayList("PASSWORDS");
                        int i=0;
                        for(String pass:passwords){
                            Log.d("demo",passwords.get(i++).toString()) ;
                        }

                        //Bundle allpassword = new Bundle();
                        //allpassword.putStringArrayList("PASSWORDS", passwords);



                        break;
                    }
                    case ThreadPassGenerate.STATUS_START:
                        Log.d("demo","message start");
                        //progressDialog.show();
                        break;
                    case ThreadPassGenerate.STATUS_STEP:
                        Log.d("demo","message step");
                        progressDialog.setProgress(msg.getData().getInt("PROGRESS"));

                        break;
                }



                return false;
            }
        });
        threadPool = Executors.newFixedThreadPool(2);

        findViewById(R.id.buttonGenerate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Generating Passwords");
                progressDialog.setMax(100);
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                progressDialog.setProgress(0);

                countThread = Integer.parseInt(((TextView)findViewById(R.id.textViewThreadCountOutput)).getText().toString());
                lengthThread = Integer.parseInt(((TextView)findViewById(R.id.textViewThreadLengthOutput)).getText().toString());
                Log.d("demo","button clicked and thread created");
                countAsync =Integer.parseInt(r3.getText().toString());
                lengthAsync = Integer.parseInt(r4.getText().toString());
                totalCount=countThread+countAsync;
                threadPool.execute(new ThreadPassGenerate(countThread,lengthThread,totalCount));

                /*while(flag)
                {

                }*/


                AsyncTPC b = new AsyncTPC(countAsync,lengthAsync,totalCount);
                b.execute();
            }
        });


    }

    class ThreadPassGenerate implements Runnable{
        int count,length,totalcount;
        public ThreadPassGenerate(int count,int length,int totalcount){
            this.count=count;
            this.length=length;
            this.totalcount=totalcount;
        }
        static final int STATUS_START=0x00;
        static final int STATUS_STEP=0x01;
        static final int STATUS_DONE=0x02;
        @Override
        public void run() {

            Message msg = new Message();
            msg.what = STATUS_START;
            handler.sendMessage(msg);
            Log.d("demo","count="+count+" "+length );
            Util util = new Util();

            for (int i = 1; i <= count; i++) {
                passwords.add(util.getPassword(length));
                Log.d("demo","i="+i+" "+"progress="+100*i/totalcount+"total="+totalCount);
                msg = new Message();
                msg.what = STATUS_STEP;
                Bundle progress = new Bundle();
                x+=(100 * i )/ (totalcount);
                Log.d("demo","x="+x);
                progress.putInt("PROGRESS", x) ;
                msg.setData(progress);
                handler.sendMessage(msg);

            }

            msg = new Message();
            msg.what = STATUS_DONE;
            Bundle allpassword = new Bundle();
            allpassword.putStringArrayList("PASSWORDS", passwords);

            msg.setData(allpassword);
            Log.d("demo","data send to handler");
            handler.sendMessage(msg);
            clt=progressDialog.getProgress();
            flag = false;

        }


    }


    class AsyncTPC extends AsyncTask<Void,Integer,Void> {

        int count, length,totallength;
        AsyncTPC(int count,int length,int totallength){
            this.count = count;
            this.length = length;
            this.totallength = totallength;
        }

        @Override
        protected Void doInBackground(Void... params) {
            x=0;
            //int y= clt;
            int y=0 ;//((totallength - count)*100)/totallength;

            //y=50;
           progressDialog.setProgress(y);
            Log.d("demo","y="+y);
            Log.d("demo","total count="+totallength);
            for(int i=1;i<=count;i++){
                String s = Util.getPassword(length);
                password.add(s);
                x=x+(100*(i)/totallength);

                publishProgress(x);
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            Intent i = new Intent(MainActivity.this,GeneratedPasswords.class);
            i.putExtra(MainActivity.pwd1,password);
            i.putExtra(MainActivity.pwd2,passwords);
            startActivity(i);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);

        }


    }
}


