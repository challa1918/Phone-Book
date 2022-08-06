package com.example.database;

import androidx.annotation.RequiresApi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.database.Data.DatabaseHandler;
import com.example.database.Data.Util;
import com.example.database.Model.Contact;
import com.example.database.Model.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.database.R.layout.contactrow;

public class MainActivity extends AppCompatActivity {

    DatabaseHandler databaseHandler;
    public ArrayList<Contact> list;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    FloatingActionButton floatingActionButton;
    EditText name,phn;
    SearchView searchView;
    Button addc,ui;
        ImageButton delb,editb;
    Dialog dialog;
    View view,card;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
ActionBar actionBar=getSupportActionBar();
actionBar.setDisplayShowHomeEnabled(true);
actionBar.setTitle("  Phone Book");
        actionBar.setIcon(R.mipmap.ic_launcher);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            int permission=1;
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},permission);

        }

        view= getLayoutInflater().inflate(R.layout.popup,null);
        card=getLayoutInflater().inflate(contactrow,null);
        delb= card.findViewById(R.id.del);
        editb=card.findViewById(R.id.edit);
        name=view.findViewById(R.id.popname);
        phn=view.findViewById(R.id.popphnno);
        addc=view.findViewById(R.id.popaddb);
        dialog=new Dialog(MainActivity.this);
        imageView=findViewById(R.id.imageView);
        recyclerView=findViewById(R.id.Recycle);
        floatingActionButton=findViewById(R.id.floatingActionButton2);
        searchView=findViewById(R.id.searchView);


        databaseHandler=new DatabaseHandler(this);

        list=databaseHandler.GetContacts();
        isEmpty();
        recyclerViewAdapter=new RecyclerViewAdapter(list,MainActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter.notifyDataSetChanged();

        recyclerViewAdapter.notifyItemChanged(0);
        recyclerView.setAdapter(recyclerViewAdapter);
        isEmpty();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(list.contains(query)){

                }else{
                    Toast.makeText(getApplicationContext(), "No Match found",Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerViewAdapter.getFilter().filter(newText);

                return false;
            }
        });


          addc.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              if(name.getText().toString().equals("")) {
                  name.setError("Name cannot be empty");
                  name.requestFocus();

              }
              else if(phn.getText().toString().equals("")){
                  phn.setError("Phone number cannot be empty");
                  phn.requestFocus();
              }else{
                  Contact contact=new Contact();
                  contact.setName(name.getText().toString());
                  contact.setPhnno(phn.getText().toString());
                  databaseHandler.AddContacts(contact);
                  recyclerViewAdapter.notifyDataSetChanged();

                  name.setText("");
                  phn.setText("");
                  dialog.cancel();

                  imageView.setVisibility(View.INVISIBLE);
              }
              list=databaseHandler.GetContacts();
              recyclerViewAdapter=new RecyclerViewAdapter(list,MainActivity.this);
              recyclerView.setAdapter(recyclerViewAdapter);

          }
      });

floatingActionButton.setOnClickListener(new View.OnClickListener() {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {


        dialog.setContentView(view);
        dialog.show();

    }
});

    }

    public void isEmpty(){
        if(list.size()==0){
            imageView.setVisibility(View.VISIBLE);
        }
        else{
            imageView.setVisibility(View.INVISIBLE);
        }
    }






}
