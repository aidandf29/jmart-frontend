package aidanJmartBO.jmart_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import aidanJmartBO.jmart_android.model.Product;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    private static final Gson gson = new Gson();
    MyRecyclerViewAdapter adapter;
    private TabLayout mainTabLayout;
    //CardView Product
    private CardView cv_product;
    private Button buttonPrev;
    private Button buttonNext;
    private Button buttonGo;
    private EditText et_page;
    private int page;
    //CardView Filter
    private CardView cv_filter;
    private EditText et_productName;
    private EditText et_lowestPrice;
    private EditText et_highestPrice;
    private CheckBox cb_new;
    private CheckBox cb_used;
    private Button btnApply;
    private Button btnClear;
    private Spinner spinner_filterCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestQueue queue = Volley.newRequestQueue(this);

        mainTabLayout = findViewById(R.id.mainTabLayout);
        cv_product = findViewById(R.id.cv_product);
        cv_filter = findViewById(R.id.cv_filter);
        //Tab Selector Listener
        mainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        cv_product.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        cv_filter.setVisibility(View.VISIBLE);
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        cv_product.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        cv_filter.setVisibility(View.INVISIBLE);
                        break;
                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        List<Product> productNames = new ArrayList<>();
        page = 0;
        fetchProduct(productNames, page, queue, false);
        RecyclerView recyclerView = findViewById(R.id.rv_Products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, productNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        buttonPrev = findViewById(R.id.btnPrev);
        buttonNext = findViewById(R.id.buttonNext);
        buttonGo = findViewById(R.id.buttonGo);
        et_page = findViewById(R.id.tv_page);
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page > 0){
                    page--;
                    fetchProduct(productNames, page, queue, true);
                }
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                fetchProduct(productNames, page, queue, true);

            }
        });
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = Integer.parseInt(et_page.getText().toString());
                fetchProduct(productNames, page, queue, true);
            }
        });
        //Filter CardView
        et_productName = findViewById(R.id.et_productName);
        et_lowestPrice = findViewById(R.id.et_lowestPrice);
        et_highestPrice = findViewById(R.id.et_highestPrice);
        spinner_filterCategory = findViewById(R.id.spinner_filterCategory);
        cb_new = findViewById(R.id.cb_new);
        cb_used = findViewById(R.id.cb_used);
        String checkCondition;
        if(cb_new.isChecked()){
            checkCondition = cb_new.getText().toString();
        }
        if(cb_used.isChecked()){
            checkCondition = cb_used.getText().toString();
        }
        btnApply = findViewById(R.id.btnApply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = et_productName.getText().toString();
                String lowestPrice= et_lowestPrice.getText().toString();
                String highestPrice = et_highestPrice.getText().toString();
                String category = spinner_filterCategory.getSelectedItem().toString();
                StringRequest filterRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/product/getFiltered?pageSize=10&accountId="+LoginActivity.getLoggedAccount().id+"&search="+productName+"&minPrice="+lowestPrice+"&maxPrice="+highestPrice+"&category="+category, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonReader reader = new JsonReader(new StringReader(response)); //Use reader to read json response of filtered products
                        try {
                            productNames.clear();                                       //Clear the previous list
                            reader.beginArray();
                            while(reader.hasNext()){
                                productNames.add(gson.fromJson(reader, Product.class)); //Add the products from reader to the arraylist
                            }
                            adapter.refresh(productNames);                              //Refresh/update displaying the list
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Filter product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                        }
                        //After filtering, move back to display the product tab (set product visible, set Filter invisible)
                        cv_product.setVisibility(View.VISIBLE);
                        cv_filter.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Filtering Succesful", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Filtering error, check again.", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(filterRequest);
            }
        });
        //Clear the input fields
        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Testing Clear", Toast.LENGTH_SHORT).show();
                et_productName.setText("");
                et_lowestPrice.setText("");
                et_highestPrice.setText("");
                cb_new.setChecked(false);
                cb_used.setChecked(false);
                spinner_filterCategory.setSelection(0);
            }
        });
    }
    //Fetch Products Request Method
    public void fetchProduct(List<Product> productNames, int page, RequestQueue queue, boolean refreshAdapter){
        StringRequest fetchProductsRequest = new StringRequest(Request.Method.GET, "http://192.168.100.6:8080/product/page?page="+page+"&pageSize=10", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonReader reader = new JsonReader(new StringReader(response));
                try {
                    productNames.clear();
                    reader.beginArray();
                    while(reader.hasNext()){
                        productNames.add(gson.fromJson(reader, Product.class));
                    }
                    if(refreshAdapter){
                        adapter.refresh(productNames);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Fetch product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fetch product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(fetchProductsRequest);
    }
    //RecycleView Item ClickListener
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getApplicationContext(), "Testing click product", Toast.LENGTH_LONG).show();
    }
    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate with the menu created in menu_main
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        //Check if account has no store yet, dont display the add product menu item
        if(LoginActivity.getLoggedAccount().store == null){
            menu.getItem(1).setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();
        if(id == R.id.menu_aboutme) {
            Intent intent = new Intent(this, AboutMeActivity.class);
            this.startActivity(intent);
            return true;
        }
        else if(id == R.id.menu_add) {
            Intent intent = new Intent(this, CreateProductActivity.class);
            this.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}