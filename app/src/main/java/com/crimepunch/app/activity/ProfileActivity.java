package com.crimepunch.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.crimepunch.app.R;
import com.crimepunch.app.base.BaseActivity;
import com.crimepunch.app.datastore.Server;
import com.crimepunch.app.event.UserRegistrationEvent;
import com.crimepunch.app.helper.Session;
import com.crimepunch.app.model.User;
import de.greenrobot.event.EventBus;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user-1 on 3/10/15.
 */
public class ProfileActivity extends BaseActivity {

    @Inject
    Server server;

    @Inject
    EventBus eventBus;

    @Inject
    Session session;

    private EditText apName;
    private EditText apAddress;
    private RadioGroup apSex;
    private Button apSubmit;

    private String selectedSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        apName = (EditText) findViewById(R.id.ap_name);
        apAddress = (EditText) findViewById(R.id.ap_address);
        apSex = (RadioGroup) findViewById(R.id.ap_sex);
        apSubmit = (Button) findViewById(R.id.ap_submit);

        apSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.ap_male) {
                    selectedSex = "male";
                }
                else {
                    selectedSex = "female";
                }
            }
        });

        apSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> attributes = new HashMap<>();
                attributes.put("name", apName.getText().toString());
                attributes.put("address", apAddress.getText().toString());
                attributes.put("sex", selectedSex);

                User user = new User();
                user.setId(getIntent().getStringExtra("phoneNumber"));
                user.setAttributes(attributes);
                server.registerUser(getBaseContext(), user);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
        if(session.getUser(this) != null) {
            startHomeActivity();
        }
    }

    @Override
    protected void onStop() {
        eventBus.unregister(this);
        super.onStop();
    }

    public void onEventMainThread(UserRegistrationEvent event) {
        if(event.getSuccess()) {
            session.setUser(this, event.getUser());
            startHomeActivity();
        }
        else {
            Toast.makeText(this, "User registration failed because " + event.getError(), Toast.LENGTH_LONG).show();
        }
    }

    private void startHomeActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }
}
