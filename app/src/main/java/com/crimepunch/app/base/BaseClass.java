package com.crimepunch.app.base;

import com.crimepunch.app.application.CrimePunchApplication;

/**
 * Created by user-1 on 3/10/15.
 */
public class BaseClass {
    public BaseClass() {
        CrimePunchApplication.getInstance().inject(this);
    }
}
