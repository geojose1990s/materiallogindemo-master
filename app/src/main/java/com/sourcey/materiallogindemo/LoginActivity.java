package com.sourcey.materiallogindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity implements PostResponseListener {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;
    private ProgressDialog progressDialog = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }
                String email = _emailText.getText().toString();
                String password = _passwordText.getText().toString();
                NetworkManager.getInstance().makePOSTRequest("http://avs.aptdeal.in/login.php", email, password, LoginActivity.this);
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty()) {
            _emailText.setError("enter a valid username address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onRequestStarted() {
        _loginButton.setEnabled(false);
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }

    @Override
    public void onPOSTRequestCompleted(String result) {
        Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    public void onPOSTRequestEndedWithError(VolleyError error) {
        Toast.makeText(LoginActivity.this, "onPOSTRequestEndedWithError", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    public void onRequestTimeOutError() {
        Toast.makeText(LoginActivity.this, "onRequestTimeOutError", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

}
