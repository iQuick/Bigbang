package com.forfan.bigbang.component.base;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Html;


import com.forfan.bigbang.R;
import com.forfan.bigbang.util.EasyPermissions;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;


/*
 * @创建者     Jrking
 * @创建时间   2016/4/15 16:18
 * @描述	      ${Activity基类 }
 * @更新描述   ${适配6.0权限问题}
 */
public class PermissionActivity extends RxAppCompatActivity implements
        EasyPermissions.PermissionCallbacks {

    protected static final int RC_PERM = 123;

    protected static int reSting = R.string.ask_again;//默认提示语句

    /**
     * 权限回调接口
     */
    private CheckPermListener mListener;

    public interface CheckPermListener {
        //权限通过后的回调方法
        void grantPermission();
        void denyPermission();
    }

    public void checkPermission(CheckPermListener listener, int resString, String... mPerms) {
        mListener = listener;
        if (EasyPermissions.hasPermissions(this, mPerms)) {
            if (mListener != null)
                mListener.grantPermission();
        } else {
            CharSequence text= Html.fromHtml("<font color=\"#000000\">"+getString(resString)+"</font>");
            EasyPermissions.requestPermissions(this, text,
                    RC_PERM, mPerms);
        }
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EasyPermissions.SETTINGS_REQ_CODE) {
            //设置返回
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
       //同意了某些权限可能不是全部
        if (mListener != null)
            mListener.denyPermission();
    }

    @Override
    public void onPermissionsAllGranted() {
        if (mListener != null)
            mListener.grantPermission();//同意了全部权限的回调
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if(!EasyPermissions.checkDeniedPermissionsNeverAskAgain(this,
                getString(R.string.perm_tip),
                R.string.setting, R.string.cancel, null, perms)){
            if (mListener != null)
                mListener.denyPermission();
        }
    }



}
