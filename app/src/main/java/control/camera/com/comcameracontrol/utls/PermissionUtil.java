package control.camera.com.comcameracontrol.utls;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;

/**
 * Created by Server on 2017/8/7.
 */

public class PermissionUtil {

    /**
     * 判断是否申请过权限
     */
    private static boolean hasPermission(Context context, String[] permissionList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissionList) {
                if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    /**
     * 注册权限,在APP中将会使用到某个权限的时候调用此方法来申请权限
     *
     * @param activity
     * @param permissionList
     * @param registerCode
     * @return 返回true表示已经注册过了
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean registerPermission(Activity activity, String[] permissionList, int registerCode) {

        if (hasPermission(activity, permissionList)) {
            return true;
        }

        if (shouldShowRequest(activity, permissionList)) {
            activity.requestPermissions(permissionList, registerCode);
        } else {
            activity.requestPermissions(permissionList, registerCode);
        }

        return false;
    }

    /**
     * 申请权限反馈
     * 需要在activity 或者fragment 中重写 onRequestPermissionsResult 方法，然后再调用此方法判断
     *
     *
     * @param requestCode  权限申请的code  只有当它 与 registerCode 相同的时候才是当前申请的。
     * @param permissions  申请的权限
     * @param grantResults 申请的权限 用户是否同意的返回值
     * @param registerCode 自己在申请权限是自定的的申请code
     * @return true 表示同意，false  表示拒绝
     */
    public static boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int registerCode) {

        if (requestCode == registerCode && grantResults.length > 0) {
            //PackageManager.PERMISSION_GRANTED 表示用户同意了权限
            for (int grantResultCode : grantResults) {
                if (grantResultCode != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 判断是否需要申请权限
     *
     * @param activity
     * @param permissionList 需要申请的权限列表
     * @return true 表示有部分权限没有申请过，这里需要申请权限，false 表示 所有权限都已经申请过，这里就不需要在吃申请了
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean shouldShowRequest(Activity activity, String[] permissionList) {
        for (String permission : permissionList) {
            if (!activity.shouldShowRequestPermissionRationale(permission)) {
                return true; //需要弹出权限申请界面
            }
        }
        return false; //不需要弹出权限申请界面
    }


}
