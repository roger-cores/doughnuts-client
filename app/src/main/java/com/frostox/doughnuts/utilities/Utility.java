package com.frostox.doughnuts.utilities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.frostox.doughnuts.R;
import com.frostox.doughnuts.activities.MainActivity;
import com.frostox.doughnuts.fragments.Login;
import com.frostox.doughnuts.fragments.Register;

/**
 * Created by Roger Cores on 2/8/16.
 */
public class Utility {
    public static void displayContingencyError(Activity activity){
        Snackbar snack = Snackbar.make(activity.findViewById(android.R.id.content), activity.getString(R.string.error_contingency), Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED);

        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        if(tv!=null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        if(activity instanceof MainActivity){
            Fragment fragment = ((MainActivity) activity).getSupportFragmentManager().findFragmentById(R.id.fullscreen_content);
            if(fragment instanceof Login){
                ((Login) fragment).hideProgress();
            } else if(fragment instanceof Register){
                ((Register) fragment).hideProgress();
            }
        }

        snack.show();
    }

    public static void displayError(Activity activity, int id, String... args){
        String message;
        if(args != null){
            message = String.format(activity.getString(id), args);
        } else message = activity.getString(id);

        Snackbar snack = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED);

        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        if(tv!=null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        if(activity instanceof MainActivity){
            Fragment fragment = ((MainActivity) activity).getSupportFragmentManager().findFragmentById(R.id.fullscreen_content);
            if(fragment instanceof Login){
                ((Login) fragment).hideProgress();
            } else if(fragment instanceof Register){
                ((Register) fragment).hideProgress();
            }
        }

        snack.show();
    }
}
