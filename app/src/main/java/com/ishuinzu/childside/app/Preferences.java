package com.ishuinzu.childside.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.ishuinzu.childside.object.ParentObject;

public class Preferences {
    private static Preferences PREFERENCES;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String PREFERENCES_NAME = "APP_PREFERENCES";
    private static final String INSTRUCTIONS_SEEN = "INSTRUCTIONS_SEEN";
    private static final String LOGGED_IN = "LOGGED_IN";
    private static final String IS_DEFAULT_LAUNCHER = "IS_DEFAULT_LAUNCHER";
    private static final String PARENT_EMAIL = "PARENT_EMAIL";
    private static final String PARENT_FCM_TOKEN = "PARENT_FCM_TOKEN";
    private static final String PARENT_HAVE_CHILD = "PARENT_HAVE_CHILD";
    private static final String PARENT_ID = "PARENT_ID";
    private static final String PARENT_IMAGE_URL = "PARENT_IMAGE_URL";
    private static final String PARENT_NAME = "PARENT_NAME";
    private static final String FILE_PATH = "FILE_PATH";

    private Preferences(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized Preferences getInstance(Context context) {
        if (PREFERENCES == null) {
            PREFERENCES = new Preferences(context);
        }
        return PREFERENCES;
    }

    public void setInstructionsSeen(Boolean instructionsSeen) {
        editor = sharedPreferences.edit();
        editor.putBoolean(INSTRUCTIONS_SEEN, instructionsSeen);
        editor.apply();
    }

    public Boolean getInstructionsSeen() {
        return sharedPreferences.getBoolean(INSTRUCTIONS_SEEN, false);
    }

    public void setLoggedIn(Boolean isLoggedIn) {
        editor = sharedPreferences.edit();
        editor.putBoolean(LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public Boolean getLoggedIn() {
        return sharedPreferences.getBoolean(LOGGED_IN, false);
    }

    public void setParent(ParentObject parentObject) {
        editor = sharedPreferences.edit();

        editor.putString(PARENT_EMAIL, parentObject.getEmail());
        editor.putString(PARENT_FCM_TOKEN, parentObject.getFcm_token());
        editor.putString(PARENT_HAVE_CHILD, parentObject.getHave_child());
        editor.putString(PARENT_ID, parentObject.getId());
        editor.putString(PARENT_IMAGE_URL, parentObject.getImg_url());
        editor.putString(PARENT_NAME, parentObject.getName());
        editor.apply();
    }

    public ParentObject getParent() {
        return new ParentObject(
                sharedPreferences.getString(PARENT_EMAIL, null),
                sharedPreferences.getString(PARENT_FCM_TOKEN, null),
                sharedPreferences.getString(PARENT_HAVE_CHILD, null),
                sharedPreferences.getString(PARENT_ID, null),
                sharedPreferences.getString(PARENT_IMAGE_URL, null),
                sharedPreferences.getString(PARENT_NAME, null)
        );
    }

    public void setIsDefaultLauncher(Boolean isDefaultLauncher) {
        editor = sharedPreferences.edit();

        editor.putBoolean(IS_DEFAULT_LAUNCHER, isDefaultLauncher);
        editor.apply();
    }

    public Boolean getIsDefaultLauncher() {
        return sharedPreferences.getBoolean(IS_DEFAULT_LAUNCHER, false);
    }

    public void updateFCMToken(String fcmToken) {
        editor = sharedPreferences.edit();

        editor.putString(PARENT_FCM_TOKEN, fcmToken);
        editor.apply();
    }

    public void setFilePath(String filePath) {
        editor = sharedPreferences.edit();

        editor.putString(FILE_PATH, filePath);
        editor.apply();
    }

    public void deleteFilePath() {
        editor = sharedPreferences.edit();

        editor.putString(FILE_PATH, "");
        editor.apply();
    }

    public String getFilePath() {
        return sharedPreferences.getString(FILE_PATH, null);
    }
}
