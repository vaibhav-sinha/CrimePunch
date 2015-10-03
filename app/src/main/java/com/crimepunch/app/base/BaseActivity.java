package com.crimepunch.app.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.crimepunch.app.application.CrimePunchApplication;
import com.crimepunch.app.helper.WindowAnimationHelper;

/**
 * Created by user-1 on 3/10/15.
 */
public class BaseActivity extends FragmentActivity {

    protected Boolean dontUseAnimation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CrimePunchApplication)getApplication()).inject(this);
    }


    @Override
    public void finish() {
        super.finish();
        if(!dontUseAnimation) {
            WindowAnimationHelper.finish(this);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if(dontUseAnimation) {
            super.startActivityForResult(intent, requestCode);
        }
        else {
            WindowAnimationHelper.startActivityForResultWithSlideFromRight(this, intent, requestCode);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if(dontUseAnimation) {
            super.startActivity(intent);
        }
        else {
            WindowAnimationHelper.startActivityWithSlideFromRight(this, intent);
        }
    }
}
