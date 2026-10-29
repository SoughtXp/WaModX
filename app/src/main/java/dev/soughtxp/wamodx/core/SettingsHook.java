package dev.soughtxp.wamodx.core;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.soughtxp.wamodx.App;
import dev.soughtxp.wamodx.ui.SettingsActivity;
import dev.soughtxp.wamodx.util.ResUtils;

public class SettingsHook {

    private SettingsHook() {
    }

    public static void inject(ViewGroup container) {
        if (container == null) return;

        try {
            android.content.Context context = container.getContext();
            if (context == null) return;

            int layoutId = ResUtils.LAYOUT.get("wamodx_privacy_row");
            LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(layoutId, container, false);
            if (view == null) return;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openSettings();
                }
            });

            int privacyId = ResUtils.ID.get("privacy_preference");
            View privacyView = container.findViewById(privacyId);

            if (privacyView != null) {
                ViewGroup parent = (privacyView.getParent() instanceof ViewGroup)
                        ? (ViewGroup) privacyView.getParent() : null;
                if (parent != null) {
                    int index = parent.indexOfChild(privacyView);
                    parent.addView(view, index + 1);
                } else {
                    container.addView(view);
                }
            } else {
                container.addView(view);
            }
        } catch (Exception e) {
        }
    }

    public static void openSettings() {
        try {
            android.content.Context context = App.getContext();
            if (context == null) return;
            Intent intent = new Intent(context, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }
}
