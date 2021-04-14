package com.camdocscanner.pdfscanner.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.camdocscanner.pdfscanner.R;
import com.camdocscanner.pdfscanner.utils.variables;

import java.util.List;

public class SupportDialog extends Dialog {

    private Context context;

    private LinearLayout llt_ok;
    private TextView txt_help_title_1, txt_content_1;
    private TextView txt_help_title_2, txt_content_2;
    private TextView txt_help_title_3, txt_content_3;
    private TextView txt_help_title_4, txt_content_4;
    private TextView txt_help_title_5, txt_content_5;
    private TextView txt_help_title_6, txt_content_6;
    private TextView txt_help_title_7, txt_content_7;
    private TextView txt_help_title_8, txt_content_8, txt_content_9;

    private SupportDialogCallback dialogCallback;


    private void initEvent() {
        llt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SupportDialog.this.dismiss();
                dialogCallback.onClickOkBth();
            }
        });
    }

    public SupportDialog(@NonNull Context _context, SupportDialogCallback callback) {
        super(_context);

        context = _context;
        dialogCallback = callback;

        setContentView(R.layout.dialog_users);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setTitle(null);
        setCanceledOnTouchOutside(true);

        initUIDialog();
    }

    private void initUIDialog() {

        llt_ok = findViewById(R.id.btn_dialog_help_ok);

        txt_help_title_1 = findViewById(R.id.txt_dlg_help_title1);
        txt_content_1 = findViewById(R.id.txt_dlg_help_content_1);

        txt_help_title_2 = findViewById(R.id.txt_dlg_help_title2);
        txt_content_2 = findViewById(R.id.txt_dlg_help_content_2);

        txt_help_title_3 = findViewById(R.id.txt_dlg_help_title3);
        txt_content_3 = findViewById(R.id.txt_dlg_help_content_3);

        txt_help_title_4 = findViewById(R.id.txt_dlg_help_title4);
        txt_content_4 = findViewById(R.id.txt_dlg_help_content_4);

        txt_help_title_5 = findViewById(R.id.txt_dlg_help_title5);
        txt_content_5 = findViewById(R.id.txt_dlg_help_content_5);

        txt_help_title_6 = findViewById(R.id.txt_dlg_help_title6);
        txt_content_6 = findViewById(R.id.txt_dlg_help_content_6);

        txt_help_title_7 = findViewById(R.id.txt_dlg_help_title7);
        txt_content_7 = findViewById(R.id.txt_dlg_help_content_7);

        txt_help_title_8 = findViewById(R.id.txt_dlg_help_title8);
        txt_content_8 = findViewById(R.id.txt_dlg_help_content_8);
        txt_content_9 = findViewById(R.id.txt_dlg_help_content_9);

        initEvent();
        insertGuide();
    }

    private void insertGuide() {

        txt_help_title_1.setText("A simple, smart and efficient document scanner for Android\n");
        txt_content_1.setText("Cam Doc Scanner is a mobile document scanner, easily digitize your paper documents into pdfs. With tons of features, document scanning has never been easier.\n");

        txt_help_title_2.setText("Enhanced image quality\n");
        txt_content_2.setText("No more low light or bad photos, it can auto-enhance the image quality for best performances.\n");

        txt_help_title_3.setText("Auto-Edge Cropping\n");
        txt_content_3.setText("Automatically detects edges from the image, so you don't have to.\n");

        txt_help_title_4.setText("Extract text from Images\n");
        txt_content_4.setText("With OCR Support, No more typing, it can automatically extract text from images.\n");

        txt_help_title_5.setText("Share your scans\n");
        txt_content_5.setText("Share with your friends via email, whatsapp, messenger or and via link to other social networks.\n");

        txt_help_title_6.setText("Documents to PDF\n");
        txt_content_6.setText("Easily convert your paper documents into digitized into a PDF files, looking sharp and clean.\n");

        txt_help_title_7.setText("Developer Name: Web Developers World\n");
        txt_content_7.setPaintFlags(txt_content_7.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_content_7.setText(variables.url_developers + "\n");

        txt_content_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://" + variables.url_developers;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });

        txt_help_title_8.setText("Support\n");
        txt_content_8.setPaintFlags(txt_content_8.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_content_8.setText(variables.url_support1 );
        txt_content_9.setPaintFlags(txt_content_9.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_content_9.setText(variables.url_support2 + "\n");
    }

    public interface SupportDialogCallback {
        void onClickOkBth();
    }

}
