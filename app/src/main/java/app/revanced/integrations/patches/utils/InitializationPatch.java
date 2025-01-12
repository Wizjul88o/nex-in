package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.utils.ReVancedHelper.isAdditionalSettingsEnabled;
import static app.revanced.integrations.utils.ReVancedUtils.runOnMainThreadDelayed;
import static app.revanced.integrations.utils.StringRef.str;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedHelper;

public class InitializationPatch {

    private static void buildDialog(@NonNull Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage(str("revanced_reboot_first_run"))
                .setPositiveButton(str("in_app_update_restart_button"), (dialog, id) ->
                        runOnMainThreadDelayed(() -> {
                                    activity.finishAffinity();
                                    activity.startActivity(activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName()));
                                    System.exit(0);
                                }, 500
                        )
                )
                .setNegativeButton(str("sign_in_cancel"), null)
                .setCancelable(false)
                .show();
    }

    /**
     * The new layout is not loaded when the app is first installed.
     * (Also reproduced on unPatched YouTube)
     * <p>
     * Side effects when new layout is not loaded:
     * - 8X zoom not working in fullscreen
     * <p>
     * To fix this, show the reboot dialog when the app is installed for the first time.
     * <p>
     * The version of the current integrations is saved to YouTube's SharedPreferences to identify if the app was first installed.
     */
    public static void initializeReVancedSettings(@NonNull Context context) {
        SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_ADDITIONAL_SETTINGS.saveValue(isAdditionalSettingsEnabled());
        if (SettingsEnum.INITIALIZED.getBoolean())
            return;

        // show dialog
        if (!(context instanceof Activity mActivity))
            return;

        runOnMainThreadDelayed(() -> buildDialog(mActivity), 500);

        runOnMainThreadDelayed(() ->
                {
                    // set initialize value
                    SettingsEnum.INITIALIZED.saveValue(true);

                    // set spoof player parameter default value
                    SettingsEnum.SPOOF_PLAYER_PARAMETER.saveValue(!mActivity.getPackageName().equals("com.google.android.youtube"));

                    // set save playback speed default value
                    SettingsEnum.ENABLE_SAVE_PLAYBACK_SPEED.saveValue(PatchStatus.DefaultPlaybackSpeed());
                }, 1000
        );
    }

    public static void setDeviceInformation(@NonNull Context context) {
        ReVancedHelper.setApplicationLabel(context);
        ReVancedHelper.setIsTablet(context);
        ReVancedHelper.setPackageName(context);
        ReVancedHelper.setVersionCode(context);
        ReVancedHelper.setVersionName(context);
    }
}