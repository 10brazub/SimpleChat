package com.example.simplechat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ChatActivity extends AppCompatActivity {

    public String TAG = "Chat Activity";
    EditText etMessage;
    ImageButton ibSend;

    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (ParseUser.getCurrentUser() != null) { // Start with existing user
            startWithCurrentUser(); //TODO: We will build out this method in the next step
        } else { // If not logged in, login as a new anonymous user
            login();
        }
    }

    // Create an anonymous user using ParseAnonymousUtils and set sUserId
    void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Anonymous login failed: ", e);
                } else {
                    startWithCurrentUser();
                }
            }
        });
    }

    void startWithCurrentUser() {
        setupMessagePosting();
    }

    public void setupMessagePosting (){

        ibSend = findViewById(R.id.ibSend);
        etMessage = findViewById(R.id.etMessage);

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                ParseObject message =  ParseObject.create("Message");
                message.put(USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());
                message.put(BODY_KEY, data);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Failed to save message", e);
                        }
                    }
                });
                etMessage.setText(null);
            }
        });
    }
}