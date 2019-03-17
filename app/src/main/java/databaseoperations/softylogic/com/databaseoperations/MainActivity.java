package databaseoperations.softylogic.com.databaseoperations;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity   {
    ArrayList<userDetails> list;
    ListView lv;
    CustomArrayAdapter adapter;

DbHelper db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewusers);
        lv = findViewById(R.id.usersListView);

db = new DbHelper(this);
            LayoutInflater li = LayoutInflater.from(this);
            View prompt = li.inflate(R.layout.logindialogbox, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(prompt);
            final EditText user = prompt.findViewById(R.id.login_name);
            final EditText pass =  prompt.findViewById(R.id.login_password);

            alertDialogBuilder.setTitle("Login");
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            if (db.checkUserForLogin(user.getText().toString(), pass.getText().toString())){
                                Toast.makeText(MainActivity.this , "Logged In", Toast.LENGTH_SHORT).show();
                            }
                            if (!db.checkUserForLogin(user.getText().toString(), pass.getText().toString())){
                                Toast.makeText(MainActivity.this , "Login failed", Toast.LENGTH_SHORT).show();
                                finishAffinity();
                            }

;                        }
                    });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    dialog.cancel();

                }
            });

            alertDialogBuilder.show();


    }


    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_search:
                LayoutInflater li = LayoutInflater.from(this);
                View prompt = li.inflate(R.layout.searchdialogbox, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setView(prompt);
                final EditText name = prompt.findViewById(R.id.search_name);
                final EditText email =  prompt.findViewById(R.id.search_email);
                final EditText userid =  prompt.findViewById(R.id.search_id);

                alertDialogBuilder.setTitle("Search");
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                if(email.getText().toString().length() > 0){
                                    ArrayList<userDetails> userList = new ArrayList<userDetails>();
                                    userList = db.searchUserByEmail(email.getText().toString());
                                   if(userList.size() > 0){

                                       adapter = new CustomArrayAdapter(MainActivity.this, userList);
                                       adapter.notifyDataSetChanged();
                                       lv.setAdapter(adapter);


                                   }
                                }
                                else if(email.getText().toString().length() == 0){
                                    if(name.getText().toString().length() > 0){
                                        ArrayList<userDetails> userList = new ArrayList<userDetails>();
                                        userList = db.searchUserbyname(name.getText().toString());
                                        if(userList.size() > 0){
                                            adapter = new CustomArrayAdapter(MainActivity.this, userList);
                                            adapter.notifyDataSetChanged();
                                            lv.setAdapter(adapter);

                                        }
                                    }
                                }
                                else if(email.getText().toString().length() == 0){
                                    if(name.getText().toString().length() == 0){
                                        if(userid.getText().toString().length() > 0){
                                            ArrayList<userDetails> userList = new ArrayList<userDetails>();
                                        userList = db.searchUserbyid(userid.getText().toString());
                                            if(userList.size() > 0){

                                                adapter = new CustomArrayAdapter(MainActivity.this, userList);
                                                adapter.notifyDataSetChanged();
                                                lv.setAdapter(adapter);
                                            }
                                        }
                                    }
                                }

                                                        }
                        });

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();


                    }
                });

                alertDialogBuilder.show();
                break;
            case R.id.menu_view:

                list = db.getAllUser();

                adapter = new CustomArrayAdapter(this, list);
                adapter.notifyDataSetChanged();
                lv.setAdapter(adapter);


                break;

            case R.id.menu_add_user:

                LayoutInflater lit = LayoutInflater.from(this);
                View prompt2 = lit.inflate(R.layout.adduserdialog ,null);
                AlertDialog.Builder alertDialogBuilderadd = new AlertDialog.Builder(this);
                alertDialogBuilderadd.setView(prompt2);
                final EditText username = prompt2.findViewById(R.id.addname);
                final EditText password =  prompt2.findViewById(R.id.addpassword);
                final EditText useremail =  prompt2.findViewById(R.id.addemail);
                final EditText contact =  prompt2.findViewById(R.id.addcontact);
                final EditText usertype =  prompt2.findViewById(R.id.addUserType);
                final EditText regDate = prompt2.findViewById(R.id.addregDate);

                final userDetails userAdd = new userDetails();
                regDate.setText(new CommonMethods().getDate());
                alertDialogBuilderadd.setTitle("Add user");
                alertDialogBuilderadd.setCancelable(false)

                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                userAdd.name = username.getText().toString();
                                userAdd.password = password.getText().toString();
                                userAdd.emailid = useremail.getText().toString();
                                userAdd.contact = contact.getText().toString();
                                userAdd.usertype = Integer.parseInt(usertype.getText().toString());
                                userAdd.registerationdate = new CommonMethods().getDate();
                                if(db.addUser(userAdd))
                                {
                                    Toast.makeText(MainActivity.this,"Data Entered" , Toast.LENGTH_SHORT ).show();

                                }
                                else{
                                    Toast.makeText(MainActivity.this,"Data Not Entered" , Toast.LENGTH_SHORT ).show();
                                }
                            }

                        });

                alertDialogBuilderadd.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();


                    }
                });

                alertDialogBuilderadd.show();

                break;


        }
        return super.onOptionsItemSelected(item);
    }








}
