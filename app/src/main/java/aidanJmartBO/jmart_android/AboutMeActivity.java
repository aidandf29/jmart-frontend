package aidanJmartBO.jmart_android;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import aidanJmartBO.jmart_android.request.RegisterStoreRequest;

/**
 * Class AboutMeActivity - to control activity_about_me page
 *
 * @author Muh. Aidan Daffa J
 * @version (version number or date here)
 */

public class AboutMeActivity extends AppCompatActivity {
    //field
    private TextView tv_userName;
    private TextView tv_userEmail;
    private TextView tv_userBalance;
    private Button buttonTopUp;
    private Button buttonRegisterStore;
    private Button btnInvoiceHistory;
    private EditText et_topUpAmount;
    private CardView cv_storeExists;

    private CardView cv_registerStore;
    private EditText et_storeName;
    private EditText et_storeAddress;
    private EditText et_storePhoneNumber;
    private Button buttonRegisterStoreCancel;
    private Button buttonRegisterStoreConfirm;

    private TextView tv_storeNameF;
    private TextView tv_storeAddressF;
    private TextView tv_storePhoneNumberF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        RequestQueue queue = Volley.newRequestQueue(this);

        tv_userName = findViewById(R.id.tv_userName);
        tv_userEmail = findViewById(R.id.tv_userEmail);
        tv_userBalance = findViewById(R.id.tv_userBalance);
        et_topUpAmount = findViewById(R.id.tv_topUpAmount);
        tv_userName.setText(LoginActivity.getLoggedAccount().name);
        tv_userEmail.setText(LoginActivity.getLoggedAccount().email);
        tv_userBalance.setText(String.valueOf(LoginActivity.getLoggedAccount().balance));

        //to make button that redirect into invoice history
        btnInvoiceHistory = findViewById(R.id.buttonInvoiceHistory);
        btnInvoiceHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InvoiceHistoryActivity.class);
                startActivity(intent);
            }
        });

        buttonTopUp = findViewById(R.id.buttonTopUp);

        buttonRegisterStore = findViewById(R.id.buttonRegisterStore);
        cv_registerStore = findViewById(R.id.cv_registerStore);
        cv_storeExists = findViewById(R.id.cv_storeExists);
        et_storeName = findViewById(R.id.et_storeName);
        et_storeAddress = findViewById(R.id.et_storeAddress);
        et_storePhoneNumber = findViewById(R.id.et_storePhoneNumber);
        buttonRegisterStoreCancel = findViewById(R.id.buttonRegisterStoreCancel);
        buttonRegisterStoreConfirm = findViewById(R.id.buttonRegisterStoreConfirm);
        if(LoginActivity.getLoggedAccount().store != null){
            buttonRegisterStore.setVisibility(View.GONE);
            cv_storeExists.setVisibility(View.VISIBLE);

            tv_storeNameF = findViewById(R.id.tv_storeNameF);
            tv_storeAddressF = findViewById(R.id.tv_storeAddressF);
            tv_storePhoneNumberF = findViewById(R.id.tv_storePhoneNumberF);
            tv_storeNameF.setText(LoginActivity.getLoggedAccount().store.name);
            tv_storeAddressF.setText(LoginActivity.getLoggedAccount().store.address);
            tv_storePhoneNumberF.setText(LoginActivity.getLoggedAccount().store.phoneNumber);
        }

        buttonRegisterStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonRegisterStore.setVisibility(View.INVISIBLE);
                cv_registerStore.setVisibility(View.VISIBLE);
            }
        });


        //handler for topup button
        buttonTopUp = findViewById(R.id.buttonTopUp);
        buttonTopUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String balance = et_topUpAmount.getText().toString();
                String URL = "http://192.168.10.119:8080/account/"+LoginActivity.getLoggedAccount().id+"/topUp";
                StringRequest topUpRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LoginActivity.reloadLoggedAccount(response);
                        try {
                            Toast.makeText(getApplicationContext(), "Top Up success", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(getIntent());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Top Up unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Top Up unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("balance", balance);
                        return params;
                    }
                };
                queue.add(topUpRequest);
            }
        });

        buttonRegisterStoreCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                buttonRegisterStore.setVisibility(View.VISIBLE);
                cv_registerStore.setVisibility(View.INVISIBLE);
            }
        });
        buttonRegisterStoreConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_storeName.getText().toString();
                String address = et_storeAddress.getText().toString();
                String phoneNumber = et_storePhoneNumber.getText().toString();
                RegisterStoreRequest registerStoreRequest = new RegisterStoreRequest(LoginActivity.getLoggedAccount().id, name, address ,phoneNumber, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LoginActivity.insertLoggedAccountStore(response);
                        try {
                            Toast.makeText(getApplicationContext(), "Register Store success", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Register Store unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Register Store unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(registerStoreRequest);
            }
        });
    }
}