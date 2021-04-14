package com.camdocscanner.pdfscanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.camdocscanner.pdfscanner.persistance.DocumentDatabase;
import com.camdocscanner.pdfscanner.utils.SharedPrefManager;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new SharedPrefManager(SplashScreen.this);

        DocumentDatabase.getInstance( getApplicationContext() );

        if (restorePrefData()) {

            new Handler().postDelayed(new Runnable() {


                @Override
                public void run() {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }
            }, SPLASH_TIME_OUT);


        } else {

            new Handler().postDelayed(new Runnable() {


                @Override
                public void run() {
                    Intent i = new Intent(SplashScreen.this, IntroActivity.class);
                    startActivity(i);
                    finish();

                }
            }, SPLASH_TIME_OUT);
        }
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;
    }
}
