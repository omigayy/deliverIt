package com.example.omigayy.comp590final;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

public class Level0 extends AppCompatActivity implements SensorEventListener {

    private static MediaPlayer mediaPlayer = null;
    private SensorManager sm;
    private Sensor s1;
    int maxX,maxY;
    float mvx=0,mvy=0;
    float xf=0,yf=0;
    ImageView img_animation;
    ImageView obj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level0);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // call pop-up
        ViewDialog tutorial = new ViewDialog();
        tutorial.showDialog(Level0.this, "", R.layout.tutorial);

        //media set up
        mediaPlayer =  MediaPlayer.create(this, R.raw.level);
        mediaPlayer.start();

        //sensor set up
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if(sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)!=null){
            Log.v("pt", " rotate");
        }
        s1=sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        onResume();

        //animation set up
        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        maxX = mdispSize.x;
        maxY = mdispSize.y;
        Log.v("setup", "width " + maxX + " height " + maxY);
        img_animation=(ImageView) findViewById(R.id.imageView);
        obj=(ImageView) findViewById(R.id.imageView2);
    }

    public void settings(View v){
        ViewDialog setting = new ViewDialog();
        setting.showDialog(Level0.this, "",R.layout.setting);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mediaPlayer.release();
        mediaPlayer = null;
        Log.v("button", "paused");
        sm.unregisterListener(this, s1);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float x,y;
        if(sensor.getType()==Sensor.TYPE_ROTATION_VECTOR){
            x=event.values[0];
            y=event.values[1];
            if(Math.abs(x-.01)<.05 && Math.abs(y-.01)<.05){//flat
                mvx=0;mvy=0;
            }else if(x>= 0.08 && y>= 0.02){
                mvx=5;mvy=0;
            } else if (x <= -.01 && y <= -0.10){
                mvx=-5;mvy=0;
            } else if(x>= 0.2 && y<= -0.05){
                mvx=0;
                mvy=2;
            } else if(x<= -0.05 && y>= -.02){
                mvx=0;
                mvy=-2;
            }else{
            }
            if(!won) {
                animate(xf, mvx + xf, yf, mvy + yf);
            }
            xf=xf+mvx;
            yf=yf+mvy;
            //Log.v("pt", " spot x " + x);
            //  Log.v("pt", " spot y " + y);
        }
    }
    Boolean won=false;
    public void animate(float xf, float xt, float yf, float yt){
        int[] locationimg= new int[2];
        img_animation.getLocationOnScreen(locationimg);

        int[] locationobj= new int[2];
        obj.getLocationOnScreen(locationobj);

        if(Math.abs(xf-locationobj[0])<250 &&Math.abs(yf+296-locationobj[1])<80){
            Log.v("ani", "HIT Goal");
            won=true;
            // call pop-up
            ViewDialog win = new ViewDialog();
            win.showDialog(Level0.this, "",R.layout.win);
        }
       // Log.v("ani", "spot x" + xf + "spot y" +yf);
       // Log.v("ani", "goal x" + locationobj[0] + "goal y" + locationobj[1]);

        if(xt>maxX){
            Log.v("ani", xt+ " maxx is"+maxX);
            xt = maxX-250;
        }
        if(xt<-120){
            Log.v("ani", xt+ " minx");
            xt = 10;
        }
        if(yt>280){
            Log.v("ani", yt+ "maxy is"+maxY);
            yt=260;
        }
        if(yt<-240){
            Log.v("ani", yt+ "miny");
            yt=-200;
        }
        //Log.v("ani", "2 movemnt" + xt + "location y" + yt);
        TranslateAnimation animation = new TranslateAnimation(xf, xt,yf, yt);
        this.xf=xt;
        this.yf=yt;

        animation.setDuration(5000000);
        animation.setRepeatCount(0);
        img_animation.startAnimation(animation);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume(){
        Log.v("button", "resumed");
        super.onResume();
        sm.registerListener(this, s1, 10000);//.01sec
    }

    public class ViewDialog {

        public void showDialog(Activity activity, String msg, Integer viewid ){
            Log.v("ani","in showdialog");
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.setContentView(viewid);
            dialog.getWindow().setLayout(800,493);

//            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
//            text.setText(msg);

            Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            View decorView = dialog.getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

            dialog.show();
        }
    }

    public void onRadioButtonClicked1(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.on:
                if (checked) {
                    if (mediaPlayer ==null) {
                        mediaPlayer = MediaPlayer.create(this, R.raw.level);
                        mediaPlayer.start();
                    }
                }
                break;
            case R.id.off:
                if (checked) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                break;
        }
    }

    public void mainMenu(View v){
        Intent intent = new Intent(Level0.this, MainActivity.class);
        startActivity(intent);
    }

    public void nextLevel(View v){
        Intent intent = new Intent(Level0.this, Level1.class);
        startActivity(intent);
    }

}
