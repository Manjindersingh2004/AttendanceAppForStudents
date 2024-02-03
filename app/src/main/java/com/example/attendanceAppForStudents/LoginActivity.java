package com.example.attendanceAppForStudents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    Button login,signup;
    EditText emailEdt,passEdit;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=findViewById(R.id.Login_btn);
        signup=findViewById(R.id.Signup_btn);
        emailEdt=findViewById(R.id.emailEdtLogin);
        passEdit=findViewById(R.id.passEdtLogin);
        progressBar=findViewById(R.id.progressBarLogin);

        signup.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SignupActivity.class));
        });

        login.setOnClickListener(v -> {

            String email,password;
            email=emailEdt.getText().toString().trim();
            password=passEdit.getText().toString().trim();
            if(email.isEmpty()){
                emailEdt.setError("Email is empty");
                emailEdt.requestFocus();
            }
            else if(!Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$").matcher(email).matches()){
                emailEdt.setError("Invalid Email");
                emailEdt.requestFocus();
                emailEdt.setText(null);
            }
            else if(password.isEmpty()){
                passEdit.setError("Password is empty");
                passEdit.requestFocus();
            }
            else{
                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                                startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ));
                            }
                            else{
                                showAlertDialog();
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(LoginActivity.this, "Email not verified", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            try{
                                throw task.getException();
                            }catch(FirebaseAuthInvalidUserException e){
                                Toast.makeText(LoginActivity.this, "User Do not exists", Toast.LENGTH_SHORT).show();
                            }
                            catch(FirebaseAuthInvalidCredentialsException e){
                                Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                                emailEdt.setError("check and re-enter");
                                emailEdt.requestFocus();
                            }catch (Exception e){
                                Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ));
            }
            else{
//                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(LoginActivity.this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//
//                        }
//                        else{
//                            Toast.makeText(LoginActivity.this, "Verification Email failed", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
                FirebaseAuth.getInstance().signOut();
                showAlertDialog();
            }
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Email not verified")
                .setMessage("Email verification is required to sigin in your account. Check your email app to verify.")
                .setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_EMAIL).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        dialog.dismiss();
                    }
                })
                .show();
    }
}