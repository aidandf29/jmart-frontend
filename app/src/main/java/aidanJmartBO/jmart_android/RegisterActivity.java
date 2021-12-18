package aidanJmartBO.jmart_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import aidanJmartBO.jmart_android.request.RegisterRequest;

/**
 * Class RegisterActivity - to control activity_Register page
 *
 * @author Muh. Aidan Daffa J
 * @version (version number or date here)
 */

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText RegisterName = findViewById(R.id.editTextTextPersonName);
        EditText RegisterEmail = findViewById(R.id.editTextTextEmailAddress);
        EditText RegisterPassword = findViewById(R.id.editTextTextPassword2);
        Button RegisterButton = findViewById(R.id.button);

        RegisterButton.setOnClickListener(v -> {
            Response.Listener<String> listener = response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject != null) {
                        Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException err){
                    Toast.makeText(RegisterActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };
            Response.ErrorListener errorListener = response -> {
                Toast.makeText(RegisterActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
            };
            String name = RegisterName.getText().toString();
            String email = RegisterEmail.getText().toString();
            String password = RegisterPassword.getText().toString();
            RegisterRequest registerRequest = new RegisterRequest(name, email, password, listener, errorListener);
            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
            requestQueue.add(registerRequest);

        });
    }
}