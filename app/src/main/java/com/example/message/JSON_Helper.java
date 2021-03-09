package com.example.message;

import android.content.Context;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

class JSONHelper {

    private static final String FILE_NAME = "data.json";

    static String exportToJSON(Context context, List<UserModel> dataList) {

        Gson gson = new Gson();
        DataItems dataItems = new DataItems();
        dataItems.setUsers(dataList);
        String jsonString = gson.toJson(dataItems);

       return jsonString;
    }

    static List<UserModel> importFromJSON(String json) {



            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(json, DataItems.class);
            return  dataItems.getUsers();



    }

    private static class DataItems {
        private List<UserModel> users;

        List<UserModel> getUsers() {
            return users;
        }
        void setUsers(List<UserModel> users) {
            this.users = users;
        }
    }
}
