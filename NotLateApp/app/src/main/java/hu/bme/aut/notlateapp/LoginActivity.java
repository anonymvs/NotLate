package hu.bme.aut.notlateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.R.attr.required;
import static java.lang.Boolean.FALSE;

public class LoginActivity extends AppCompatActivity {
    private static final String MY_AUTH_INSTANCE = "MY_AUTH_INSTANCE";
    private static final String TAG = "LoginAcitvity";
    private EditText emailEditText;
    private EditText passwordEditText;

    private boolean canThrowVerificationToast = true;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        if(getIntent().getBooleanExtra(RegistrationActivity.REGISTRATION_COMPLETED, false)) {
            canThrowVerificationToast = false;
        }

        setSharedPreferences();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if(user.isEmailVerified()) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        if(canThrowVerificationToast) {
                            Toast.makeText(LoginActivity.this, "Email address is not verified", Toast.LENGTH_SHORT).show();
                        }
                        canThrowVerificationToast = true;
                    }
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isReadyToLogin()) {
                    canThrowVerificationToast = true;
                    makeLogin();
                }
            }
        });

        TextView tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void makeLogin() {
        mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if(!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                    Toast.makeText(LoginActivity.this, "authentication failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isReadyToLogin() {
        if(!emailEditText.getText().toString().trim().equals("") &&
                !passwordEditText.getText().toString().trim().equals("")) {
            return true;
        } else {
            Toast.makeText(LoginActivity.this, "You have to fill all the required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void setSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean logged_in = sharedPreferences.getBoolean("logged_in", true);
        if(!logged_in) {
            FirebaseAuth.getInstance().signOut();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
