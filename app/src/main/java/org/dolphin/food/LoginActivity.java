package org.dolphin.food;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextView createnewAccount;
    EditText inputEmail, inputPassword;
    Button btnLogin;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Button btnGoogle;
    Button btnFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createnewAccount = findViewById(R.id.createNewAccount);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook=findViewById(R.id.btnFacebook);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        createnewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perforLogin();
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,GoogleSignInActivity.class);

                startActivity(intent);
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,FacebookSignInActivity.class);

                startActivity(intent);
            }
        });
    }

    private void perforLogin() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (email.isEmpty()) {
            inputEmail.setError("Enter the Email");
        } else if (!email.matches(emailPattern)) {
            inputEmail.setError("Enter Context Email");
        } else if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("Enter Correct Password");
        } else {
            progressDialog.setMessage("Please wait while Logging in");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT);
                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
        private void sendUserToNextActivity () {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

}