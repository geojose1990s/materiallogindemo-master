package com.sourcey.materiallogindemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.sourcey.materiallogindemo.network.NetworkManager;
import com.sourcey.materiallogindemo.network.PostResponseListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity implements PostResponseListener {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_fname)
    EditText _firstNameText;
    @Bind(R.id.input_lname)
    EditText _lastnameText;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.input_repassword)
    EditText _rePasswordText;
    @Bind(R.id.input_country)
    EditText _countryText;
    @Bind(R.id.input_locality)
    EditText _localityText;
    @Bind(R.id.input_phone)
    EditText _phoneText;

    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;

    private ProgressDialog progressDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _firstNameText.setText("Geo");
        _lastnameText.setText("Jose");
        _emailText.setText("geojose1990@gmail.com");
        _passwordText.setText("1234567890");
        _rePasswordText.setText("1234567890");
        _countryText.setText("India");
        _localityText.setText("Thrissur");
        _phoneText.setText("8089545503");

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = _firstNameText.getText().toString();
                String lastName = _lastnameText.getText().toString();
                String email = _emailText.getText().toString();
                String password = _passwordText.getText().toString();
                String rePassword = _rePasswordText.getText().toString();
                String country = _countryText.getText().toString();
                String locality = _localityText.getText().toString();
                String phone = _phoneText.getText().toString();

                if (!validate(firstName, lastName, email, password, rePassword, country, locality, phone)) {
                    return;
                }
                NetworkManager.getInstance().makePOSTRequest("http://avs.aptdeal.in/registration.php", firstName, lastName,
                        email, password, country, locality, phone, SignupActivity.this);

            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    private boolean validate(String firstName, String lastName, String email, String password,
                             String rePassword, String country, String locality, String phone) {
        boolean valid = true;

        // First Name Validation
        if (firstName.isEmpty() || firstName.length() < 3) {
            _firstNameText.setError("at least 3 characters");
            valid = false;
        } else {
            _firstNameText.setError(null);
        }

        // Last Name Validation
        if (lastName.isEmpty() || lastName.length() < 3) {
            _lastnameText.setError("at least 3 characters");
            valid = false;
        } else {
            _lastnameText.setError(null);
        }

        // Email Validation
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        // Password Validation
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        // Password Re entry Validation
        if (rePassword.isEmpty() || rePassword.length() < 4 || rePassword.length() > 10) {
            _rePasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else if (!password.equals(rePassword)) {
            _rePasswordText.setError("password must match");
            valid = false;
        } else {
            _rePasswordText.setError(null);
        }

        // Country Validation
        if (country.isEmpty() || country.length() < 3) {
            _countryText.setError("enter a valid country name");
            valid = false;
        } else {
            _countryText.setError(null);
        }

        // Locality Validation
        if (locality.isEmpty()) {
            _localityText.setError("locality can not be empty");
            valid = false;
        } else {
            _localityText.setError(null);
        }

        // Phone Validation
        if (phone.isEmpty() || phone.length() < 3) {
            _phoneText.setError("enter a valid phone number");
            valid = false;
        } else {
            _phoneText.setError(null);
        }

        return valid;
    }

    @Override
    public void onRequestStarted() {
        _signupButton.setEnabled(false);
        progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
    }

    @Override
    public void onPOSTRequestCompleted(String result) {
        Toast.makeText(SignupActivity.this, result, Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
        if (progressDialog != null) {
            progressDialog.hide();
        }
        // TODO: 21/09/16 send data back as intent
        setResult(RESULT_OK, null);
    }

    @Override
    public void onPOSTRequestEndedWithError(VolleyError error) {
        Toast.makeText(SignupActivity.this, "onPOSTRequestEndedWithError", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    public void onRequestTimeOutError() {
        Toast.makeText(SignupActivity.this, "onRequestTimeOutError", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }
}