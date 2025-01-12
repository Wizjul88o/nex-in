package com.google.android.apps.youtube.app.settings.videoquality;

import static app.revanced.integrations.utils.ReVancedUtils.getChildView;
import static app.revanced.integrations.utils.ResourceUtils.identifier;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.Objects;

import app.revanced.integrations.settingsmenu.ReVancedSettingsFragment;
import app.revanced.integrations.settingsmenu.ReturnYouTubeDislikeSettingsFragment;
import app.revanced.integrations.settingsmenu.SponsorBlockSettingsFragment;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ResourceHelper;
import app.revanced.integrations.utils.ResourceType;
import app.revanced.integrations.utils.ThemeHelper;

/**
 * @noinspection ALL
 */
public class VideoQualitySettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            setTheme(ThemeHelper.getSettingTheme());
            setContentView(identifier("revanced_settings_with_toolbar", ResourceType.LAYOUT));

            final int fragmentId = identifier("revanced_settings_fragments", ResourceType.ID);
            final ViewGroup toolBar = Objects.requireNonNull(findViewById(identifier("revanced_toolbar", ResourceType.ID)));

            setBackButton(toolBar);

            PreferenceFragment fragment;
            String toolbarTitleResourceName;
            String dataString = getIntent().getDataString();
            switch (dataString) {
                case "sponsorblock_settings" -> {
                    fragment = new SponsorBlockSettingsFragment();
                    toolbarTitleResourceName = "revanced_sponsorblock_settings_title";
                    break;
                }
                case "ryd_settings" -> {
                    fragment = new ReturnYouTubeDislikeSettingsFragment();
                    toolbarTitleResourceName = "revanced_ryd_settings_title";
                    break;
                }
                case "extended_settings" -> {
                    fragment = new ReVancedSettingsFragment();
                    toolbarTitleResourceName = "revanced_extended_settings_title";
                    break;
                }
                default -> {
                    LogHelper.printException(() -> "Unknown setting: " + dataString);
                    return;
                }
            }

            setToolbarTitle(toolBar, toolbarTitleResourceName);
            getFragmentManager()
                    .beginTransaction()
                    .replace(fragmentId, fragment)
                    .commit();
        } catch (Exception ex) {
            LogHelper.printException(() -> "onCreate failure", ex);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setBackButton(ViewGroup toolBar) {
        ImageButton imageButton = Objects.requireNonNull(getChildView(toolBar, view -> view instanceof ImageButton));
        imageButton.setOnClickListener(view -> VideoQualitySettingsActivity.this.onBackPressed());
        imageButton.setImageDrawable(getResources().getDrawable(ResourceHelper.getArrow()));
    }

    private void setToolbarTitle(ViewGroup toolBar, String toolbarTitleResourceName) {
        TextView toolbarTextView = Objects.requireNonNull(getChildView(toolBar, view -> view instanceof TextView));
        toolbarTextView.setText(identifier(toolbarTitleResourceName, ResourceType.STRING));
    }
}