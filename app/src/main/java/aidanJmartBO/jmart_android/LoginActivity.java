package aidanJmartBO.jmart_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import aidanJmartBO.jmart_android.model.Account;
import aidanJmartBO.jmart_android.model.Store;
import aidanJmartBO.jmart_android.request.LoginRequest;

public class LoginActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {
    private static final Gson gson = new Gson();
    private static Account loggedAccount;

    public static Account getLoggedAccount(){
        return loggedAccount;
    }
    
    private TextView register;
    private EditText emailText;
    private EditText password;
    private Button button_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = (TextView) findViewById(R.id.RegisterNow);
        emailText = (EditText) findViewById(R.id.emailInput);
        password = (EditText) findViewById(R.id.passwordInput);
        button_Login = (Button) findViewById(R.id.Login);

        emailText.setText("aidandaffa29@gmail.com");
        password.setText("Aidan123");

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent registerPage = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerPage);
            }
        });

        button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRequest newLogin = new LoginRequest(
                        emailText.getText().toString(),
                        password.getText().toString(),
                        new Response.Listener<String>() {
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject != null) {
                                        Toast.makeText(getApplicationContext(), "Login berhasil.", Toast.LENGTH_SHORT).show();
                                        loggedAccount = gson.fromJson(jsonObject.toString(), Account.class);
                                        Intent loginSuccess = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(loginSuccess);
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(LoginActivity.this, "Login gagal.", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(),
                                        "Terdapat kesalahan pada sistem", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(newLogin);
            }
        });
    }

    
    @Override
    public void onErrorResponse(VolleyError error) {
        Intent intent = new Intent(this, MainActivity.class);
        Toast.makeText(this, "Error :(", Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    @Override
    public void onResponse(String response) {
        Intent intent = new Intent(this, MainActivity.class);
        Toast.makeText(this, "Login success!", Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    public static void reloadLoggedAccount(String response){
        loggedAccount = gson.fromJson(response, Account.class);
    }
    public static void insertLoggedAccountStore(String response){
        Store newStore = gson.fromJson(response, Store.class);
        loggedAccount.store = newStore;
    }

    private void onLoginClicked(View view){
        String email = emailText.getText().toString();
        String pass = password.getText().toString();
        Response.Listener<String> listener = item -> {

        };

        Response.ErrorListener error = item -> {

        };

        LoginRequest loginRequest = new LoginRequest(email, pass, listener, error);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(loginRequest);

    }
    
}