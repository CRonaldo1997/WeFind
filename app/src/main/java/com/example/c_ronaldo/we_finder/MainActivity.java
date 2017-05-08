package com.example.c_ronaldo.we_finder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText)this.findViewById(R.id.mEmail);
        password = (EditText)this.findViewById(R.id.mPassword);

    }

    public void onSignUpClicked(View button){
        Intent goToSignUpView = new Intent(this,SignUpActivity.class);
        startActivity(goToSignUpView);
    }

}
