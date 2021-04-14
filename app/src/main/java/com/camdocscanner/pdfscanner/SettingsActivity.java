package com.camdocscanner.pdfscanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.camdocscanner.pdfscanner.dialogs.SupportDialog;

public class SettingsActivity extends AppCompatActivity {

    private LinearLayout llt_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        llt_help = findViewById(R.id.llt_setting_help);
        llt_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupportDialog dlg = new SupportDialog(SettingsActivity.this, new SupportDialog.SupportDialogCallback() {
                    @Override
                    public void onClickOkBth() {

                    }
                });
                dlg.show();
            }
        });
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
//                moveTaskToBack(true);
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}