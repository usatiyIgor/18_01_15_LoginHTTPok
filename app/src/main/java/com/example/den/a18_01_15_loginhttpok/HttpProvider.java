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

    public Persons getAllContacts(String token) throws Exception {

        Request request = new Request.Builder()
                .header("Authorization", token)
                .url(BASE_URL + "/contactsarray")
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() == 200){
            //normal answer
            String responseJson = response.body().string();//return Json,mo*et bit' null
            return gson.fromJson(responseJson, Persons.class);
        }else if(response.code() == 401){
            throw new Exception("Wrong authorization! empty token");
        }else{
            String error = response.body().string();
            Log.d("MY_TAG", "get all contacts: error: " + error);
            throw new Exception("Server error! Call to support!");
        }
    }

    public String addContact(String token, String address, String description,
                             String email, String name, String phone) throws Exception {
        PersonWithoutId person = new PersonWithoutId(address, description,email, name, phone);
        String jsonRequest = gson.toJson(person);
        RequestBody body = RequestBody.create(JSON,jsonRequest);
        Request request = new Request.Builder()
                .url(BASE_URL + "/setContact")
                .header("Authorization", token)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() == 200){
            String responseJson = response.body().string();
            if(responseJson != null) {
                return "Contact added";
            }else{
                throw new Exception("Empty response");
            }
        }else if(response.code() == 401){
            throw new Exception("Wrong authorization! empty token");
        }else{
            String error = response.body().string();
            Log.d("MY_TAG", "add contact: error: " + error);
            throw new Exception("Server error! Call to support!");
        }
    }
    public String updateContact(Long contactId, String token, String address, String description,
                                String email, String name, String phone) throws Exception {
        Person person = new Person(contactId, address, description,email, name, phone);
        String jsonRequest = gson.toJson(person);
        RequestBody body = RequestBody.create(JSON,jsonRequest);
        Request request = new Request.Builder()
                .url(BASE_URL + "/setContact")
                .header("Authorization", token)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() == 200){
            String responseJson = response.body().string();
            if(responseJson != null) {
                return "Contact updated";
            }else{
                throw new Exception("Empty response");
            }
        }else if(response.code() == 401){
            throw new Exception("Wrong authorization! empty token");
        }else{
            String error = response.body().string();
            Log.d("MY_TAG", "update contact: error: " + error);
            throw new Exception("Server error! Call to support!");
        }
    }
    public String deleteContact(Long contactId, String token) throws Exception {
        PersonsForDelete idField = new PersonsForDelete(contactId);
        String jsonRequest = gson.toJson(idField);
        RequestBody body = RequestBody.create(JSON,jsonRequest);
        Request request = new Request.Builder()
                .url(BASE_URL + "/contact")
                .header("Authorization", token)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() == 200){
            String responseJson = response.body().string();
            if(responseJson != null) {
                return "Contact was delete";
            }else{
                throw new Exception("Empty response");
            }
        }else if(response.code() == 401){
            throw new Exception("Wrong authorization! empty token / " +
                    "Invalid token! please login or registration");
        }else if (response.code() == 409) {
            throw new Exception ("You are not owner of contact / Wrong contact id!");
        }else{
            String error = response.body().string();
            Log.d("MY_TAG", "delete contact: error: " + error);
            throw new Exception("Server error! Call to support!");
        }
    }
    public String clearContactList(String token) throws Exception {
        //String jsonRequest = gson.toJson();
        RequestBody body = RequestBody.create(JSON,"");
        Request request = new Request.Builder()
                .url(BASE_URL + "/clearContactsList")
                .header("Authorization", token)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() == 200){
            String responseJson = response.body().string();
            if(responseJson != null) {
                return "Contacts list was clear";
            }else{
                throw new Exception("Empty response");
            }
        }else if(response.code() == 401){
            throw new Exception("Wrong authorization! empty token / " +
                    "Invalid token! please login or registration");
        }else{
            String error = response.body().string();
            Log.d("MY_TAG", "clear contact list: error: " + error);
            throw new Exception("Server error! Call to support!");
        }
    }
}
