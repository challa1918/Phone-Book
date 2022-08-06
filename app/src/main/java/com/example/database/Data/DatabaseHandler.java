package com.example.database.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.database.Model.Contact;
import com.example.database.R;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context) {
        super(context, Util.Database_Name, null, Util.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + Util.TABLE_NAME + "(" + Util.KEY_ID + " Integer PRIMARY KEY," + Util.KEY_NAME + " Text," + Util.KEY_PHNNO + " Text" + ")";
        db.execSQL(sql);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqldrop = String.valueOf(R.string.Drop);
        db.execSQL(sqldrop, new String[]{Util.Database_Name});
        onCreate(db);


    }


    public void AddContacts(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, contact.getName());
        values.put(Util.KEY_PHNNO, contact.getPhnno());
        db.insert(Util.TABLE_NAME, null, values);
        db.close();


    }

    public ArrayList<Contact> GetContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Contact> ContactList = new ArrayList<>();
        String selectAll = "Select * from " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);
        if (cursor.moveToNext()) {
            do {


                Contact c = new Contact();
                c.setid(Integer.parseInt(cursor.getString(0)));
                 c.setName(cursor.getString(1));
                c.setPhnno(cursor.getString(2));
                ContactList.add(c);
             System.out.println(c.toString());


            } while (cursor.moveToNext());

        }
        return ContactList;


    }
    public void delete(Contact contact)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        db.delete(Util.TABLE_NAME,Util.KEY_ID+"=?",new String[]{String.valueOf(contact.getid())});

    }
    public void update(Contact contact)
    {
        SQLiteDatabase database= this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(Util.KEY_NAME,contact.getName());
        values.put(Util.KEY_PHNNO,contact.getPhnno());
        database.update(Util.TABLE_NAME,values,Util.KEY_ID+"=?",new String[]{String.valueOf(contact.getid())});


    }
}
