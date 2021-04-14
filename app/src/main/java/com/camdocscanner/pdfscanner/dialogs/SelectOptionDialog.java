package com.camdocscanner.pdfscanner.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.camdocscanner.pdfscanner.R;

public class SelectOptionDialog extends Dialog {

    private SelectOptionDlgCallback callback;

    public SelectOptionDialog(@NonNull Context context, SelectOptionDlgCallback _callback) {
        super(context);
        callback = _callback;

        setContentView(R.layout.dialog_agreement);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setTitle(null);
        setCanceledOnTouchOutside(false);

        initUI();
    }

    private void initUI() {
        ImageView btnGallery = findViewById(R.id.img_dialog_gallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.selectedMedia();
                SelectOptionDialog.this.dismiss();
            }
        });

        ImageView btnCamera = findViewById(R.id.img_dialog_camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.selectedCamera();
                SelectOptionDialog.this.dismiss();
            }
        });

        TextView txtDismiss = findViewById(R.id.txt_dlg_cancel);
        txtDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectOptionDialog.this.dismiss();
            }
        });
    }

    public interface SelectOptionDlgCallback {
        void selectedMedia();
        void selectedCamera();
    }
}
