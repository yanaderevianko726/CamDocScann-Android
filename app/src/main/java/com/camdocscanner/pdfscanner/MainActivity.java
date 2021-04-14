package com.camdocscanner.pdfscanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.camdocscanner.pdfscanner.dialogs.SelectOptionDialog;
import com.camdocscanner.pdfscanner.fileView.FLAdapter;
import com.camdocscanner.pdfscanner.persistance.Document;
import com.camdocscanner.pdfscanner.persistance.DocumentViewModel;
import com.camdocscanner.pdfscanner.utils.DialogUtil;
import com.camdocscanner.pdfscanner.utils.DialogUtilCallback;
import com.camdocscanner.pdfscanner.utils.FileIOUtils;
import com.camdocscanner.pdfscanner.utils.FileWritingCallback;
import com.camdocscanner.pdfscanner.utils.PDFWriterUtil;
import com.camdocscanner.pdfscanner.utils.PermissionUtil;
import com.camdocscanner.pdfscanner.utils.SharedPrefManager;
import com.camdocscanner.pdfscanner.utils.UIUtil;
import com.camdocscanner.pdfscanner.utils.variables;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FLAdapter fileAdapter;

    private final Context c = this;

    private List<Uri> scannedBitmaps = new ArrayList<>();

    private DocumentViewModel viewModel;
    private LinearLayout emptyLayout;

    private FrameLayout frg_banner_admob;
    private AdView bannerAdView;

    private PopupMenu popup;

    LiveData<List<Document>> liveData;

    public MainActivity() {
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rw);

        UIUtil.setLightNavigationBar( recyclerView, this );
        PermissionUtil.ask(this);

        final String baseStorageDirectory =  getApplicationContext().getString( R.string.base_storage_path);
        FileIOUtils.mkdir( baseStorageDirectory );

        final String baseStagingDirectory =  getApplicationContext().getString( R.string.base_staging_path);
        FileIOUtils.mkdir( baseStagingDirectory );

        final String scanningTmpDirectory =  getApplicationContext().getString( R.string.base_scantmp_path);
        FileIOUtils.mkdir( scanningTmpDirectory );

        this.emptyLayout = findViewById(R.id.empty_list);

        viewModel = ViewModelProviders.of(this).get(DocumentViewModel.class);

        fileAdapter = new FLAdapter( viewModel, this);
        recyclerView.setAdapter( fileAdapter );

        liveData = viewModel.getAllDocuments();
        liveData.observe(this, new Observer<List<Document>>() {
                    @Override
                    public void onChanged(@Nullable List<Document> documents) {

                        if( documents.size() > 0 ){
                            emptyLayout.setVisibility(View.GONE);

                        } else {
                            emptyLayout.setVisibility(View.VISIBLE);
                        }

                        fileAdapter.setData(documents);
                    }
                });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        final ImageView imgMore = findViewById(R.id.img_main_more);
        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(imgMore);
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        frg_banner_admob = findViewById(R.id.adView_container);
        bannerAdView = new AdView(this);
        frg_banner_admob.addView(bannerAdView);
        bannerAdView.setAdUnitId(getString(R.string.admob_banner_id));
        loadBanner();
    }

    private void loadBanner() {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        AdSize adSize = getAdSize();
        bannerAdView.setAdSize(adSize);
        bannerAdView.loadAd(adRequest);
        frg_banner_admob.setVisibility(View.VISIBLE);
    }

    private AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    private void showPopup(final ImageView view) {
        final String[] titles = {"Search", "Settings"};
        final int[] menuIcons = {
                R.drawable.ic_search,
                R.drawable.setting,
        };
        popup = new PopupMenu(MainActivity.this, view);
        for(int i = 0; i < 2; i++) {
            popup.getMenu().add(0, i, 1, menuIconWithText(getResources().getDrawable(menuIcons[i]), titles[i]));
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == 0){
                        goToSearch();
                    } else if(item.getItemId() == 1){
                        goToPreferences();
                    }
                    return true;
                }
            });
        }
        popup.show();
    }

    private CharSequence menuIconWithText(Drawable r, String title) {
        int ratio = r.getIntrinsicWidth()/40;
        int width = r.getIntrinsicWidth()/ratio;
        r.setBounds(0, 0, width, width);
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

    public void goToSearch(){
        Intent intent = new Intent(this, SearchableActivity.class);
        startActivityForResult(intent, 0);
    }


    public void goToPreferences(){
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    public void selectMethod(View view) {
        SelectOptionDialog dialog = new SelectOptionDialog(this, new SelectOptionDialog.SelectOptionDlgCallback() {
            @Override
            public void selectedMedia() {
                openGallery();
            }

            @Override
            public void selectedCamera() {
                openCamera();
            }
        });
        dialog.show();
    }

    public void openCamera(){
        scannedBitmaps.clear();

        String stagingDirPath = getApplicationContext().getString( R.string.base_staging_path );
        String scanningTmpDirectory =  getApplicationContext().getString( R.string.base_scantmp_path);

        FileIOUtils.clearDirectory( stagingDirPath );
        FileIOUtils.clearDirectory( scanningTmpDirectory );


        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, ScanConstants.OPEN_CAMERA);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
        startActivityForResult(intent, ScanConstants.START_CAMERA_REQUEST_CODE, options.toBundle());
    }

    public void openGallery(){
        scannedBitmaps.clear();

        String stagingDirPath = getApplicationContext().getString( R.string.base_staging_path );
        FileIOUtils.clearDirectory( stagingDirPath );

        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, ScanConstants.OPEN_MEDIA);
        startActivityForResult(intent, ScanConstants.PICKFILE_REQUEST_CODE);
    }

    private void saveBitmap( final Bitmap bitmap, final boolean saveMode ){

        final String baseDirectory =  getApplicationContext().getString(R.string.base_storage_path);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss");
        final String timestamp = simpleDateFormat.format( new Date() );

        DialogUtil.askUserFilaname( c, null, null, new DialogUtilCallback() {

            @Override
            public void onSave(String textValue, String category) {

                String stagingDirPath = getApplicationContext().getString( R.string.base_staging_path );
                String filename = "SCANNED_" + timestamp;
                final PDFWriterUtil pdfWriter = new PDFWriterUtil();

                try {
                    if(saveMode){

                        List<File> stagingFiles = FileIOUtils.getAllFiles( stagingDirPath );
                        for ( File stagedFile : stagingFiles ) {
                            pdfWriter.addFile( stagedFile );
                        }
                        pdfWriter.addBitmap(bitmap);
                        filename = filename + ".pdf";
                    }else{
                        filename = filename + ".png";
                    }

                    FileIOUtils.writeFile( baseDirectory, filename, new FileWritingCallback() {
                        @Override
                        public void write(FileOutputStream out) {
                            if(saveMode){
                                try {
                                    pdfWriter.write(out);

                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                            }else{
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                            }
                        }
                    });

                    fileAdapter.notifyDataSetChanged();

                    FileIOUtils.clearDirectory( stagingDirPath );

                    SimpleDateFormat simpleDateFormatView = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                    final String timestampView = simpleDateFormatView.format(new Date());

                    Document newDocument = new Document();
                    newDocument.setName( textValue );
                    newDocument.setCategory( category );
                    newDocument.setPath( filename );
                    newDocument.setScanned( timestampView );
                    newDocument.setPageCount( pdfWriter.getPageCount() );
                    viewModel.saveDocument(newDocument);

                    pdfWriter.close();

                    bitmap.recycle();
                    System.gc();

                }catch(IOException ioe){
                    ioe.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( ( requestCode == ScanConstants.PICKFILE_REQUEST_CODE || requestCode == ScanConstants.START_CAMERA_REQUEST_CODE ) &&
                resultCode == Activity.RESULT_OK) {

            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            boolean saveMode = data.getExtras().getBoolean(ScanConstants.SAVE_PDF);

            final File sd = Environment.getExternalStorageDirectory();
            File src = new File(sd, uri.getPath());
            Bitmap bitmap = BitmapFactory.decodeFile(src.getAbsolutePath());

            saveBitmap(bitmap, saveMode);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
