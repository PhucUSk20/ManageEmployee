package com.hcmus.manage_employee.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hcmus.manage_employee.R;

public class ExitActivity extends Activity {

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(android.os.Build.VERSION.SDK_INT >= 21)
        {
            finishAndRemoveTask();
        }
        else
        {
            finish();
        }
    }

    public static void exitApplicationAndRemoveFromRecent(Context context)
    {
        Intent intent = new Intent(context, ExitActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);

        context.startActivity(intent);
    }
}
