package com.example.c_ronaldo.we_finder;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private final String TITLE = "Sign Up";

    Spinner yearSpinner;
    Spinner signSpinner;
    Spinner genderSpinner;
    EditText userName;
    EditText signUpEmail;
    EditText signUpPswd;
    EditText repeatPswd;
    ImageView portrait;
    String zodiacSign;
    String gender;
    String year;
    ArrayAdapter<String> adapterYear;
    ArrayAdapter<String> adapterSign;
    ArrayAdapter<String> adapterGender;
    List<String> yearList = new ArrayList<>();
    String[] signList = {"Aries", "Taurus", "Gemini","Cancer","Leo","Pisces","Aquarius","Libra","Sagittarius", "Scorpio", "Capricorn","Virgo"};
    String[] genderList = {"Male","Female"};
    private static final int PICK_IMAGE  = 666;
    Uri imageUri;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle(TITLE);

        yearSpinner = (Spinner)this.findViewById(R.id.mSpinnerYear);
        signSpinner = (Spinner)this.findViewById(R.id.mSpinnerSign);
        genderSpinner = (Spinner)this.findViewById(R.id.mSpinnerGender);
        userName = (EditText)this.findViewById(R.id.mUsername);
        signUpEmail = (EditText)this.findViewById(R.id.mEmailSU);
        signUpPswd = (EditText)this.findViewById(R.id.mPasswordSU);
        repeatPswd = (EditText)this.findViewById(R.id.mPasswordRepeat);
        portrait = (ImageView)this.findViewById(R.id.mPhotoImageView);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        generateYearList();
        yearSpinnerAdapter();
        signSpinnerAdapter();
        genderSpinnerAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.saveuser_menu, menu);
        return true;
    }

    public void saveUserMenuClicked(MenuItem selectedMenu){
        Log.i("menuLog","saveMenuClicked!");
//        createAccount(signUpEmail.getText().toString(), signUpPswd.getText().toString());
        String username = userName.getText().toString();
        String email = signUpEmail.getText().toString();
        if (username.isEmpty() || email.isEmpty() || year.isEmpty() || zodiacSign.isEmpty() ||
                gender.isEmpty()) {
            Log.d(TAG, "Some fields missing\nNot creating new user");
            String msg = "Please complete the required fields";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            return;
        }
        String password = signUpPswd.getText().toString();
        if (!createAccount(email, password)) {
            Log.d(TAG, "Create account failed.");
        } else {
            Log.d(TAG, "Create account succeeded");
            Intent toPassBack = getIntent();
            toPassBack.putExtra("email", email);
            toPassBack.putExtra("password", password);
            setResult(RESULT_OK, toPassBack);

            Log.d(TAG, "Adding current user info to Firebase Database");
            upLoadToFirebase(username, email, year, zodiacSign, gender);
            Toast.makeText(this, "User Created on Firebase. Please sign in.",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void onUploadPortraitClicked(View button){
        openGallery();
    }

    public void openGallery(){
        Intent goToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(goToGallery,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            Log.i("forImage","whereIsImage");
            imageUri = data.getData();
            Log.i("forImage",imageUri.toString());
            portrait.setImageURI(imageUri);
        }
    }

    public void generateYearList(){
        yearList.clear();
        for(int i=1900;i<=2017;i++){
            yearList.add(Integer.toString(i));
        }
    }

    public void yearSpinnerAdapter(){
        adapterYear = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, yearList);
        yearSpinner.setAdapter(adapterYear);
        yearSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                year = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //do nothing
            }});
    }

    public void signSpinnerAdapter(){
        adapterSign = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, signList);
        signSpinner.setAdapter(adapterSign);
        signSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                zodiacSign = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //do nothing
            }});
    }

    public void genderSpinnerAdapter(){
        adapterGender = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, genderList);
        genderSpinner.setAdapter(adapterGender);
        genderSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //do nothing
            }});
    }

    private boolean createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return false;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Firebase Authentication Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return true;
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = signUpEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            signUpEmail.setError("Email Address Required.");
            valid = false;
        } else {
            signUpEmail.setError(null);
        }

        String password = signUpPswd.getText().toString();
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            signUpPswd.setError("Password must have at least 6 characters.");
            valid = false;
        } else {
            signUpPswd.setError(null);
        }

        String rePassword = repeatPswd.getText().toString();
        if (TextUtils.isEmpty(rePassword) || rePassword.length() < 6 ||
                !rePassword.equals(password)) {
            repeatPswd.setError("Password doesn't not match");
            valid = false;
        } else {
            signUpPswd.setError(null);
        }
        return valid;
    }

    private void upLoadToFirebase(String username, String email, String year, String zodiac,
                                  String gender) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);   // Enable offline writing
        DatabaseReference userTable = database.getReference("users");
        User currentUser = new User(username, email, year, zodiac, gender);
//        String userKey = userTable.push().getKey();
//        userTable.child(userKey).setValue(currentUser);
        userTable.child(username).setValue(currentUser);
    }
}
