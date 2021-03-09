package com.example.message;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;

import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;



public class MainActivity extends AppCompatActivity {

    Calendar dateAndTime=Calendar.getInstance();
    List<UserModel> users = new ArrayList<UserModel>();

    UserAdapter adapter_2 ;
    NotificationChannel channel ;
    NotificationManager notificationManager;
    SharedPreferences settings;
    SharedPreferences.Editor prefEditor;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences("test", MODE_PRIVATE);


        if(settings.getString("1","")!="")
        {
            users=JSONHelper.importFromJSON(settings.getString("1",""));

        }
        prefEditor = settings.edit();


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        UserAdapter adapter = new UserAdapter(this, users);
        adapter_2=adapter;
        channel=new NotificationChannel("test", "test", NotificationManager.IMPORTANCE_HIGH);
         notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        EditText text=(EditText) findViewById(R.id.test);
        text.setText(settings.getString("2","Almaty"));



        recyclerView.setAdapter(adapter_2);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {



               while (true)
               {
                   List<UserModel> users_2= new ArrayList<UserModel>() ;
                   users_2.addAll(users);

                   Bitmap icon = BitmapFactory.decodeResource(getResources(),
                           R.drawable.some_pic);

                   for (UserModel userModel: users_2){
                       Calendar dateAndTime_2=Calendar.getInstance();
                       MyAsyncTask task= new MyAsyncTask();

                       if (userModel.time.getMinutes()==dateAndTime_2.getTime().getMinutes()&&userModel.time.getHours()==dateAndTime_2.getTime().getHours()&&userModel.Active==1)
                       {
                      userModel.Active=0;
                           NotificationCompat.Builder builder = null;
                           try {
                               builder = new NotificationCompat.Builder(MainActivity.this, "test")
                                       .setSmallIcon(R.drawable.ic_action_edit)
                                       .setLargeIcon(icon)


                                       .setContentTitle("Heeey")
                                       .setContentText(userModel.time.getHours()+":" + userModel.time.getMinutes() + " temperature in " +userModel.city+" :"+ task.execute("http://api.openweathermap.org/data/2.5/weather?q="+userModel.city+"&appid=fc535f4b14c6927ff9aee363d5c6c55e").get() )
                                       .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                           } catch (ExecutionException e) {
                               e.printStackTrace();
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                           notificationManager.notify(2, builder.build());
                           Log.println(Log.ASSERT,"t","re");

                       }
                       else if(userModel.time.getMinutes()!=dateAndTime_2.getTime().getMinutes()||userModel.time.getHours()!=dateAndTime_2.getTime().getHours()){

                          userModel.Active=1;
                       }
                   }
               }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();



        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.miCompose) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);


            View.OnClickListener oclBtnOk = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Меняем текст в TextView (tvOut)

                }
            };



            builder.setView(R.layout.dialog);
            builder.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {



                            UserModel userModel= new UserModel();
                            userModel.time= dateAndTime.getTime();
                            MyAsyncTask task= new MyAsyncTask();
                            EditText text=(EditText) findViewById(R.id.test);
                            userModel.city= text.getText().toString() ;
                            prefEditor.putString("2",userModel.city);

                            userModel.Active=1;


                            users.add(userModel);
                            adapter_2.notifyDataSetChanged();

                            String str= JSONHelper.exportToJSON(MainActivity.this,users);


                            prefEditor.putString("1",str );
                            prefEditor.apply();




                            // устанавливаем для списка адаптер





                        }

                    });


            // Create the AlertDialog object and return it
            builder.create();
            builder.show();
        }


        return super.onOptionsItemSelected(item);
    }
    public void Del(View view) {
        Intent intent = getIntent();
        users.clear();
        prefEditor.clear();
        prefEditor.apply();
        adapter_2.notifyDataSetChanged();
    }

    public void setTime(View v) {
        new TimePickerDialog(MainActivity.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);

        }
    };

    private  class  MyAsyncTask extends AsyncTask<String,Void , String> {
        @Override
        protected String doInBackground(String... parameter) {
            URL url = null;
            try {

                url = new URL(parameter[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer json = new StringBuffer(1024);
                String tmp = "";

                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();
                JSONObject json_2 = new JSONObject(json.toString());
               Double temp= (json_2.getJSONObject("main").getDouble("temp")-273);
                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.CEILING);


               String str= df.format(temp);

                return str;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "test";


        }
    }


}




