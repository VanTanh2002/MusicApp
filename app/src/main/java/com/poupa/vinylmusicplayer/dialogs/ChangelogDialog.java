package com.poupa.vinylmusicplayer.dialogs;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import com.poupa.vinylmusicplayer.util.PreferenceUtil;

public class ChangelogDialog {
    public static void setChangelogRead(@NonNull Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int currentVersion = pInfo.versionCode;
            PreferenceUtil.getInstance().setLastChangeLogVersion(currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
