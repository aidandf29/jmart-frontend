package aidanJmartBO.jmart_android.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Class RegisterStoreRequest - connect into backend
 *
 * @author Muh. Aidan Daffa J
 * @version (version number or date here)
 */

//Class for register new store request
public class RegisterStoreRequest extends StringRequest{
    private Map<String, String> params;

    public RegisterStoreRequest(int id, String name, String address, String phoneNumber, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, "http://192.168.10.119:8080/account/"+id+"/registerStore", listener, errorListener);
        params = new HashMap<>();
        params.put("name", name);
        params.put("address", address);
        params.put("phoneNumber", phoneNumber);
    }

    public Map<String, String> getParams(){
        return params;
    }
}