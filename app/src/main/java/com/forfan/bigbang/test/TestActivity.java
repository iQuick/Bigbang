package com.forfan.bigbang.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.forfan.bigbang.R;
import com.forfan.bigbang.component.base.BaseActivity;
import com.forfan.bigbang.network.RetrofitHelper;
import com.forfan.bigbang.network.UploadUtil;
import com.forfan.bigbang.util.IOUtil;
import com.forfan.bigbang.util.LogUtil;

import com.forfan.bigbang.util.UpdateUtil;
import com.google.gson.stream.JsonReader;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.LanguageCodes;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TestActivity extends BaseActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        new PngConverter().convertToJpg("/storage/emulated/0/share.png", "/storage/emulated/0/share.jpeg");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    public void onRetrofit(View view) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("你好啊");
        arrayList.add("你好啊");
        arrayList.add("你好啊");
        RetrofitHelper.getWordSegmentService()
                .getWordSegsList("BigBang 是您的快捷助手，您只需在设置中开启BigBang的辅助功能\n，便可以在任意app中对文字进行进行编辑，包括分词，翻译，复制以及动态调整，希望您能在日常生活中获得便利")
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recommendInfo -> {
                    LogUtil.d(recommendInfo.toString());
                }, throwable -> {
                    LogUtil.d(throwable.toString());
                });
    }

    public void onTranslate(View view) {

        RetrofitHelper.getTranslationService()
                .getTranslationItem("我好累哦 我想休息一下下 怎么办么")
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recommendInfo -> {
                    LogUtil.d(recommendInfo.toString());
                }, throwable -> {
                    LogUtil.d(throwable.toString());
                });
    }

    public void onOcr(View view) {
        VisionServiceRestClient client = new VisionServiceRestClient("56c87e179c084cfaae9b70a2f58fa8d3");
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File("/storage/emulated/0/share.jpeg");
                try {
                    byte[] data = IOUtil.getBytes("/storage/emulated/0/share.jpeg");
                    String ocr = client.recognizeText(data, LanguageCodes.AutoDetect, true);
                    LogUtil.e(ocr);
                } catch (VisionServiceException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        String descriptionString = "hello, this is description speaking";

////        RequestBody requestBody =
////                RequestBody.create(MediaType.parse("image/*"), file);
//        File file = new File("/storage/emulated/0/4.jpg");
//        RequestBody requestFile =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file);
//
//        MultipartBody.Part body = MultipartBody.Part.createFormData("data", file.getName(), requestFile);

//        UploadBody uploadBody = new UploadBody();
//        uploadBody.data = IOUtil.getBytes("/storage/emulated/0/share.jpeg");
//        RetrofitHelper.getMicsoftOcrService()
//                .uploadImage4recognize(uploadBody)
//                .compose(this.bindToLifecycle())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(recommendInfo -> {
//                    LogUtil.d(recommendInfo.toString());
//                }, throwable -> {
//                    LogUtil.d(throwable.toString());
//                });
//                .enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        LogUtil.d(response.body());
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        LogUtil.d(t.getMessage());
//                    }
//                });

    }
//    public void onUpload1(View view) throws IOException{
//        File file = new File("/storage/emulated/0/sharw.png");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                UploadUtil.uploadFile(file,"http://up.imgapi.com/");
//            }
//        }).start();
//    }
    public void onUpload(View view) throws IOException {
        File file = new File("/storage/emulated/0/sharw.png");

        //String token ="BCBB0F5480391E6D7E4ACB9F2FA16E8924019E83:Ay7-eh7ad01W1nLuaz9TYUI6luc=:eyJkZWFkbGluZSI6MTQ4NDU2MTA2MywidWlkIjoiNTgzMjEyIiwiYWxidW0iOiIxMjcxODE1In0=";
       // String token = "b12ac98d75cfc99e9aa5d14582a82d1e3c8f4fab:z_8GuiEyV-GFtm-pcsxjWjleup8=:eyJkZWFkbGluZSI6MTQ4NDU2OTE5MCwiYWN0aW9uIjoiZ2V0IiwidWlkIjoiNTgzMjEyIiwiYWlkIjoiMTI3MzUyNCIsImZyb20iOiJmaWxlIn0=";
        String token = "c601d5d96c7438b7a6801f9715820bc6e561db4b:eDYtR2NnVlVpWEdRX2pDM1hJV2lwTjAwSndJPQ==:eyJkZWFkbGluZSI6MTQ0Njk3MjkxOSwiYWN0aW9uIjoiZ2V0IiwidWlkIjoiNTIzMDYwIiwiYWlkIjoiMTE1MTg3NiIsImZyb20iOiJmaWxlIn0=";
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)

              //   .addFormDataPart("deadline",System.currentTimeMillis()+50+"")
                //.addFormDataPart("lastModifiedDate",System.currentTimeMillis()+50+"")
               // .addFormDataPart("album",1273524+"")
               // .addFormDataPart("id","WU_FILE_0")
              //  .addFormDataPart("Token",token)
               .addFormDataPart("smfile", file.getName(), RequestBody.create(MediaType.parse("image/png"), file))
                .addFormDataPart("file_id","0")
                .build();
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/*"), file);
//
//        HashMap<String,Object> map = new HashMap<>();
//        map.put("deadline",System.currentTimeMillis()+50);
//        map.put("aid",1271815);
//        map.put("from","file");
//        map.put("Token","524ed80313c4618c44f7aeb2f23666e543d11fa5:esotyeuZRmbWY_idMluKw4mq7vU=:eyJkZWFkbGluZSI6MTQ4NDEzMzQ5MCwiYWN0aW9uIjoiZ2V0IiwidWlkIjoiNTgzMjEyIiwiYWlkIjoiMTI3MTgxNSIsImZyb20iOiJmaWxlIn0=");
        //String token ="524ed80313c4618c44f7aeb2f23666e543d11fa5:XDgG4BjIs2yO8ZlGNqVWyhKw1Mc=:eyJkZWFkbGluZSI6MTQ4NDU1NjMwNywiYWN0aW9uIjoiZ2V0IiwidWlkIjoiNTgzMjEyIiwiYWlkIjoiMTI3MTgxNSIsImZyb20iOiJmaWxlIn0=";
        //String token ="524ed80313c4618c44f7aeb2f23666e543d11fa5:pAx2PkPzpCpYdtLT379Ek6IfVX8=:eyJkZWFkbGluZSI6MTQ4NDU1OTMzOCwiYWN0aW9uIjoiZ2V0IiwidWlkIjoiNTgzMjEyIiwiYWlkIjoiMTI3MTgxNSIsImZyb20iOiJmaWxlIn0=";
//        RequestBody requestFile =
//                RequestBody.create(MediaType.parse("text/plain"), file);
//
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("aFile", file.getName(), requestFile);
        FileInputStream in = new FileInputStream(file);
        String bytes = IOUtil.readString(file);
        RetrofitHelper.getImageUploadService().uploadImage4search(requestBody)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recommendInfo -> {
                    LogUtil.d(recommendInfo.toString());
                }, throwable -> {
                    LogUtil.d(throwable.toString());
                });
    }

    private String createToken() {
        return null;
    }


    public class PngConverter {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void convertToJpg(String pngFilePath, String jpgFilePath) {
            Bitmap bitmap = BitmapFactory.decodeFile(pngFilePath);
            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(jpgFilePath))) {
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)) {
                    bos.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //    public void onOcr(View view){
//        String descriptionString = "hello, this is description speaking";
//        File file = new File("/storage/emulated/0/share.png");
//        RequestBody requestBody =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        RetrofitHelper.getOcrService()
//                .uploadImage("e02e6b613488957",descriptionString,requestBody)
//                .compose(this.bindToLifecycle())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(recommendInfo -> {
//                    LogUtil.d(recommendInfo.toString());
//                }, throwable -> {
//                    LogUtil.d(throwable.toString());
//                });
//
//    }
    public static void upload(String path) {

//        String descriptionString = "hello, this is description speaking";
//
//        String[] m = new String[2];
//        m[0]= "share.png";
//        m[1]=  "Screenshot_20160128-140709.png";
//        File[]  ssssss= new  File[2];
//        File file1 = new File("/storage/emulated/0/sc/share.png");
//        File file = new File("/storage/emulated/0/Pictures/ScreenShots/Screenshot_20160128-140709.png");
//        ssssss[0]=file;
//        ssssss[0]=file1;
//        RequestBody requestBody[] = new RequestBody[3];
//        RequestBody requestBody1 =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        RequestBody requestBody2 =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file1);
//        requestBody[0]=requestBody1;
//        requestBody[1]=requestBody2;
//        Call<String> call = apiManager.uploadImage( m[0],requestBody1,requestBody2,null);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Response<String> response, Retrofit retrofit) {
//                Log.v("Upload", response.message());
//                Log.v("Upload", "success");
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.e("Upload", t.toString());
//            }
//        });

    }
}
