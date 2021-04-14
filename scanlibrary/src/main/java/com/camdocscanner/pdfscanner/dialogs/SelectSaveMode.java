package com.camdocscanner.pdfscanner.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.scanlibrary.R;

public class SelectSaveMode extends Dialog {

    private Context context;

    private ImageView img_pdf, img_png;
    private TextView txt_cancel;

    private SaveMethodDialogCallback dlgCallback;


    private void initEvent() {

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectSaveMode.this.dismiss();
            }
        });

        img_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgCallback.onClickPdf();
                SelectSaveMode.this.dismiss();
            }
        });

        img_png.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgCallback.onClickPng();
                SelectSaveMode.this.dismiss();
            }
        });
    }

    public SelectSaveMode(@NonNull Context _context, SaveMethodDialogCallback callback) {
        super(_context);

        context = _context;
        dlgCallback = callback;

        setContentView(R.layout.dialog_save_method);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setTitle(null);
        setCanceledOnTouchOutside(true);

        initUIDialog();
    }

    private void initUIDialog() {

        img_pdf = findViewById(R.id.img_dialog_pdf);
        img_png = findViewById(R.id.img_dialog_png);

        txt_cancel = findViewById(R.id.txt_dlg_save_cancel);

        initEvent();
    }

    public interface SaveMethodDialogCallback {
        void onClickPdf();
        void onClickPng();
    }

}
