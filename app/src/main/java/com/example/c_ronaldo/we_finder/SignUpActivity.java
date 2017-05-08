package com.example.c_ronaldo.we_finder;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        yearSpinner = (Spinner)this.findViewById(R.id.mSpinnerYear);
        signSpinner = (Spinner)this.findViewById(R.id.mSpinnerSign);
        genderSpinner = (Spinner)this.findViewById(R.id.mSpinnerGender);
        userName = (EditText)this.findViewById(R.id.mUsername);
        signUpEmail = (EditText)this.findViewById(R.id.mEmailSU);
        signUpPswd = (EditText)this.findViewById(R.id.mPassword);
        repeatPswd = (EditText)this.findViewById(R.id.mPasswordRepeat);
        portrait = (ImageView)this.findViewById(R.id.mPhotoImageView);

        generateYearList();
        yearSpinnerAdapter();
        signSpinnerAdapter();
        genderSpinnerAdapter();
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
}
