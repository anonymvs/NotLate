package hu.bme.aut.notlateapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";
    public static final String REGISTRATION_COMPLETED = "REGISTRATION_COMPLETED";

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordAgain;
    private Button btnRegister;
    private Spinner sGender;

    enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    public Gender gender;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etEmail = (EditText) findViewById(R.id.emailEditText);
        etPassword = (EditText) findViewById(R.id.passwordEditText);
        etPasswordAgain = (EditText) findViewById(R.id.passwordAgainEditText);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isReadyToRegister()) {
                    makeRegistration();
                }
            }
        });

        spinnerInit();
    }

    private void makeRegistration() {
        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                if (!task.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this, "Invalid Email address or Password",
                            Toast.LENGTH_SHORT).show();
                } else {
                    sendVerificationEmail();
                    FirebaseUser user = mAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(etUsername.getText().toString()).build();
                    user.updateProfile(profileUpdates);
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    intent.putExtra(REGISTRATION_COMPLETED, true);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void sendVerificationEmail() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(RegistrationActivity.this, "Verification email sent to " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "sendEmailVerification", task.getException());
                        Toast.makeText(RegistrationActivity.this,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean isReadyToRegister() {
        if(isFilled(etFirstName) &&
                isFilled(etLastName) &&
                isFilled(etEmail) &&
                isFilled(etPassword) &&
                isFilled(etPasswordAgain) &&
                isFilled(etUsername)) {
            if(etPassword.getText().toString().equals(etPasswordAgain.getText().toString())) {
                return true;
            } else {
                Toast.makeText(RegistrationActivity.this, "Your password doesn't match", Toast.LENGTH_SHORT).show();
                etPassword.setText("");
                etPasswordAgain.setText("");
                return false;
            }
        }
        return false;
    }

    private boolean isFilled(EditText et) {
        if(et.getText().toString().trim().equals("")) {
            Toast.makeText(RegistrationActivity.this, "A required field haven't been filled", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void spinnerInit() {
        gender = Gender.MALE;
        sGender = (Spinner) findViewById(R.id.sGender);
        sGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0 :
                        gender = Gender.MALE;
                        break;
                    case 1 :
                        gender = Gender.FEMALE;
                        Toast.makeText(adapterView.getContext(), "female" + gender.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case 2 :
                        gender = Gender.OTHER;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
