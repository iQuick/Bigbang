package com.forfan.bigbang.component.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.forfan.bigbang.R;
import com.forfan.bigbang.component.activity.screen.CaptureResultActivity;
import com.forfan.bigbang.component.base.BaseActivity;
import com.forfan.bigbang.cropper.BitmapUtil;
import com.forfan.bigbang.cropper.CropHandler;
import com.forfan.bigbang.cropper.CropHelper;
import com.forfan.bigbang.cropper.CropParams;
import com.forfan.bigbang.cropper.ImageUriUtil;
import com.forfan.bigbang.cropper.handler.CropImage;
import com.forfan.bigbang.entity.ImageUpload;
import com.forfan.bigbang.util.ConstantUtil;
import com.forfan.bigbang.util.OcrAnalsyser;
import com.forfan.bigbang.util.SnackBarUtil;
import com.forfan.bigbang.util.ToastUtil;
import com.forfan.bigbang.util.UrlCountUtil;
import com.forfan.bigbang.view.DialogFragment;
import com.forfan.bigbang.view.SimpleDialog;
import com.microsoft.projectoxford.vision.contract.Line;
import com.microsoft.projectoxford.vision.contract.OCR;
import com.microsoft.projectoxford.vision.contract.Region;
import com.microsoft.projectoxford.vision.contract.Word;
import com.shang.commonjar.contentProvider.SPHelper;
import com.shang.utils.StatusBarCompat;

import static com.forfan.bigbang.component.activity.screen.CaptureResultActivity.HTTP_IMAGE_BAIDU_COM;


/**
 * Created by wangyan-pd on 2016/11/9.
 */

public class OcrActivity extends BaseActivity implements View.OnClickListener, CropHandler {
    private static final String TAG = OcrActivity.class.getName();
    private CropParams mCropParams;
    private ImageView mImageView;
    private AppCompatEditText editText;
    private Button mPicReOcr;
    private Uri mCurrentUri;

   // private boolean shouldShowDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orc);
        StatusBarCompat.setupStatusBarView(this, (ViewGroup) getWindow().getDecorView(), true, R.color.colorPrimary);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.ocr_picture);


        mCropParams = new CropParams(this);
        mImageView = (ImageView) findViewById(R.id.image);
        editText = (AppCompatEditText) findViewById(R.id.result);
        mPicReOcr = (Button) findViewById(R.id.re_ocr);
        findViewById(R.id.take_pic).setOnClickListener(this);
        findViewById(R.id.select_pic).setOnClickListener(this);
        findViewById(R.id.re_ocr).setOnClickListener(this);
        parseIntent(getIntent());

        findViewById(R.id.hint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrlCountUtil.onEvent(UrlCountUtil.CLICK_OCR_TO_BIGBANG_ACTIVITY);
                Intent intent = new Intent(OcrActivity.this, BigBangActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(BigBangActivity.TO_SPLIT_STR, editText.getText().toString());
                startActivity(intent);
            }
        });
        // editText.setOnTouchListener(forceTouchListener);
    }

//    final ForceTouchListener forceTouchListener = new ForceTouchListener(this, 70, 0.27f, true, true, new Callback() {
//        @Override
//        public void onForceTouch() {
//            //functionToInvokeOnForceTouch();
//            UrlCountUtil.onEvent(UrlCountUtil.CLICK_OCR_TO_BIGBANG_ACTIVITY);
//            Intent intent = new Intent(OcrActivity.this, BigBangActivity.class);
//            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra(BigBangActivity.TO_SPLIT_STR, editText.getText().toString());
//            startActivity(intent);
//        }
//
//        @Override
//        public void onNormalTouch() {
//            //functionToInvokeOnNormalTouch();
////            UrlCountUtil.onEvent(UrlCountUtil.CLICK_OCR_TO_BIGBANG_ACTIVITY);
////            Intent intent = new Intent(OcrActivity.this, BigBangActivity.class);
////            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
////            intent.putExtra(BigBangActivity.TO_SPLIT_STR, editText.getText());
////            startActivity(intent);
//        }
//    });

    private void parseIntent(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (intent.getClipData() != null && intent.getClipData().getItemAt(0) != null && intent.getClipData().getItemAt(0).getUri() != null) {
                Uri uri = intent.getClipData().getItemAt(0).getUri();
                showBitmapandOcr(uri);
                UrlCountUtil.onEvent(UrlCountUtil.CLICK_OCR_FROM_SHARE);
            }
        } else if (intent.getData() != null) {
            Uri uri = intent.getData();
            showBitmapandOcr(uri);
            UrlCountUtil.onEvent(UrlCountUtil.CLICK_OCR_FROM_SHARE);
        }

    }

    private void showBitmapandOcr(Uri uri) {
        mImageView.setVisibility(View.VISIBLE);
        mImageView.setImageBitmap(BitmapUtil.decodeUriAsBitmap(this, uri));
        uploadImage4Ocr(uri);
        mCurrentUri = uri;
        showSearchOcr(mCurrentUri);
    }

    private void showSearchOcr(Uri uri) {
        String img_path = ImageUriUtil.getImageAbsolutePath(this, uri);
        findViewById(R.id.search).setVisibility(View.VISIBLE);
        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(R.string.upload_img);
                OcrAnalsyser.getInstance().uploadImage(OcrActivity.this, img_path, new OcrAnalsyser.ImageUploadCallBack() {
                    @Override
                    public void onSucess(ImageUpload imageUpload) {
                        if(imageUpload != null &&
                                imageUpload.getData() != null &&
                                !TextUtils.isEmpty(imageUpload.getData().getUrl())){

                            String url = HTTP_IMAGE_BAIDU_COM +
                                    "queryImageUrl=" +imageUpload.getData().getUrl()+
                                    "&querySign=4074500770,3618317556&fromProduct= ";
                            Intent intent = new Intent();
                            intent.putExtra("url",url);
                            intent.setClass(OcrActivity.this,WebActivity.class);
                            startActivity(intent);
                        }else {
                            ToastUtil.show(R.string.upload_img_fail);
                        }

                    }

                    @Override
                    public void onFail(Throwable throwable) {
                        ToastUtil.show(throwable.getMessage());
                    }
                });
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseIntent(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (shouldShowDialog) {
//            showBeyondQuoteDialog();
//        }
    }

    @Override
    public void onClick(View v) {
        mCropParams.refreshUri();

        switch (v.getId()) {
            case R.id.take_pic:
                try {
                    UrlCountUtil.onEvent(UrlCountUtil.CLICK_OCR_TAKEPICTURE);
                    mCropParams.enable = true;
                    mCropParams.compress = false;
                    Intent intent = CropHelper.buildCameraIntent(mCropParams);
                    startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
                    mPicReOcr.setVisibility(View.GONE);
                } catch (Throwable e) {
                }

                break;
            case R.id.select_pic:

                try {
                    UrlCountUtil.onEvent(UrlCountUtil.CLICK_OCR_PICK_FROM_GALLERY);
                    mCropParams.enable = false;
                    mCropParams.compress = false;
                    Intent intent1 = CropHelper.buildGalleryIntent(mCropParams);
                    startActivityForResult(intent1, CropHelper.REQUEST_CROP);
                    mPicReOcr.setVisibility(View.GONE);
                } catch (Throwable e) {
                }
                break;
            case R.id.re_ocr:
                UrlCountUtil.onEvent(UrlCountUtil.CLICK_OCR_REOCR);
                if (mCurrentUri != null)
                    uploadImage4Ocr(mCurrentUri);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == -1) {
            CropImage.ActivityResult result = data.getExtras().getParcelable(CropImage.CROP_IMAGE_EXTRA_RESULT);
            mCurrentUri = result.getUri();
            showBitmapandOcr(mCurrentUri);

        } else {

            CropHelper.handleResult(this, requestCode, resultCode, data);
        }
        if (requestCode == 1) {
            Log.e(TAG, "");
        }
    }

    @Override
    protected void onDestroy() {
        CropHelper.clearCacheDir();
        super.onDestroy();
    }

    @Override
    public CropParams getCropParams() {
        return mCropParams;
    }

    @Override
    public void onPhotoCropped(Uri uri) {
        // Original or Cropped uri
        Log.d(TAG, "Crop Uri in path: " + uri.getPath());
//
//        if (!mCropParams.compress) {
//            showBitmapandOcr(uri);
//
//        }
        CropImage.activity(uri)
                .start(OcrActivity.this);

    }

    private void showBeyondQuoteDialog() {
        SimpleDialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                // 这里是保持开启
                super.onPositiveActionClicked(fragment);
                Intent intent = new Intent();
                intent.setClass(OcrActivity.this, DiyOcrKeyActivity.class);
                startActivity(intent);
            }

            @Override
            public void onDismiss(DialogInterface dialog) {
                super.onCancel(dialog);
              //  shouldShowDialog = false;
            }
        };
        builder.message(this.getString(R.string.ocr_quote_beyond_time))
                .positiveAction(this.getString(R.string.free_use));
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    private void uploadImage4Ocr(Uri uri) {
        String img_path = ImageUriUtil.getImageAbsolutePath(this, uri);
        // VisionServiceRestClient client = new VisionServiceRestClient("00b0e581e4124a2583ea7dba57aaf281");
        findViewById(R.id.hint).setVisibility(View.VISIBLE);
        if (SPHelper.getInt(ConstantUtil.OCR_TIME, 0) == ConstantUtil.OCR_TIME_TO_ALERT) {
//            showBeyondQuoteDialog();
           // shouldShowDialog = true;
            int time = SPHelper.getInt(ConstantUtil.OCR_TIME, 0) + 1;
            SPHelper.save(ConstantUtil.OCR_TIME, time);
            return;
        }
        editText.setText(R.string.recognize);
        OcrAnalsyser.getInstance().analyse(this, img_path, true, new OcrAnalsyser.CallBack() {
            @Override
            public void onSucess(OCR ocr) {
                editText.setText(OcrAnalsyser.getInstance().getPasedMiscSoftText(ocr));
            }

            @Override
            public void onFail(Throwable throwable) {

                if (SPHelper.getString(ConstantUtil.DIY_OCR_KEY, "").equals("")) {
                    ToastUtil.show(getResources().getString(R.string.ocr_useup_toast));
                }
                editText.setText(R.string.sorry_for_parse_fail);
                mPicReOcr.setVisibility(View.VISIBLE);
            }
        });
//        RequestBody requestBody =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        RetrofitHelper.getOcrService()
//                .uploadImage("e02e6b613488957", descriptionString, requestBody)
//                .compose(this.bindToLifecycle())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(recommendInfo -> {
//                    LogUtil.d(recommendInfo.toString());
//                    editText.setText(getPasedText(recommendInfo));
//                }, throwable -> {
//                    LogUtil.d(throwable.toString());
//                    editText.setText(R.string.sorry_for_parse_fail);
//                    mPicReOcr.setVisibility(View.VISIBLE);
//                });

    }

    private String getPasedMiscSoftText(OCR r) {
        String result = "";
        for (Region reg : r.regions) {
            for (Line line : reg.lines) {
                for (Word word : line.words) {
                    result += word.text + " ";
                }
                result += "\n";
            }
            result += "\n\n";
        }
        return result;
    }

    private void showBigBang(String result) {
        Intent intent = new Intent(this, BigBangActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(BigBangActivity.TO_SPLIT_STR, result);
        startActivity(intent);
    }

    @Override
    public void onCompressed(Uri uri) {
        // Compressed uri
        mImageView.setImageBitmap(BitmapUtil.decodeUriAsBitmap(this, uri));

    }

    @Override
    public void onCancel() {
        SnackBarUtil.show(editText, "Crop canceled!");
    }

    @Override
    public void onFailed(String message) {
        SnackBarUtil.show(editText, "Crop failed: " + message);
    }

    @Override
    public void handleIntent(Intent intent, int requestCode) {
        try {
            startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
