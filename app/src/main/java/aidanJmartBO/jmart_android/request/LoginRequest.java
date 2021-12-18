package aidanJmartBO.jmart_android.request;

import java.util.HashMap;
import java.util.Map;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Class CreatePaymentRequest - connect into backend
 *
 * @author Muh. Aidan Daffa J
 * @version (version number or date here)
 */

public class LoginRequest extends StringRequest {
    private static final String URL = "http://192.168.10.119:8080/account/login";
    private final Map<String, String> params;

    public LoginRequest(String email, String password, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
    }
    public Map<String, String> getParams(){
        return params;
    }

}
