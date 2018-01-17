package com.example.den.a18_01_15_loginhttpok;

import android.util.Log;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Den on 1/15/2018.
 */

public class HttpProvider {
    private static  final HttpProvider ourInstanse = new HttpProvider();
    private static final String BASE_URL = "https://telranstudentsproject.appspot.com/_ah/api/contactsApi/v1";

    private OkHttpClient client;
    private MediaType JSON;
    private Gson gson;

    public static HttpProvider getInstance(){
        return ourInstanse;
    }

    private HttpProvider(){
        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");
        gson = new Gson();
    }


    public String registration(String email,String password) throws Exception{
        Auth auth = new Auth(email,password);
        String jsonRequest = gson.toJson(auth);

        RequestBody body = RequestBody.create(JSON, jsonRequest);
        Request request = new Request.Builder()
                .url(BASE_URL+"/registration")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();

        if (response.code() < 400){
            String responseJson = response.body().string();
            AuthToken token = gson.fromJson(responseJson,AuthToken.class);
            return token.getToken();
        }else if (response.code() == 409){
            throw new Exception("User alresdy exist!");
        }else{
            String error = response.body().string();
            Log.d("MY_TAG","registration error:" + error);
            throw new Exception("Server error! Call to support!");
        }
    }
    public String login(String email, String password) throws  Exception{
        Auth auth = new Auth(email,password);
        String jsonRequest = gson.toJson(auth);

        RequestBody body = RequestBody.create(JSON, jsonRequest);
        Request request = new Request.Builder()
                .url(BASE_URL+"/login")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();

        if (response.code() < 400) {
            String responseJson = response.body().string();
            AuthToken token = gson.fromJson(responseJson, AuthToken.class);
            return token.getToken();
        }else if (response.code() == 401){
            throw new Exception("Wrong email or password");
        }else{
            String error = response.body().string();
            Log.d("MY_TAG","login error:" + error);
            throw new Exception("Server error! Call to support!");
        }
    }
}
