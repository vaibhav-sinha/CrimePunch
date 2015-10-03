package com.crimepunch.app.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.crimepunch.app.application.CrimePunchApplication;

/**
 * Created by user-1 on 3/10/15.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CrimePunchApplication)getActivity().getApplication()).inject(this);
    }

}
