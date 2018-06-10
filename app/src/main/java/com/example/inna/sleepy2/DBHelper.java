package com.example.inna.sleepy2;


import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/sdcard0/download/";
    private static String DB_NAME = "contactsdb";
    public static String TB_CONTACTS = "contacts";

    private SQLiteDatabase myDataBase;
    private final Context myContext;

    private static final String KEY_ID="_id";
    public static final String KEY_NAME="name";
    public static final String KEY_MAIL="mail";
    public static final String KEY_PASSWORD="password";

    /**     * Constructor     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.     * @param context     */
    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        //openDataBase();

    }
    /**     * Creates a empty database on the system and rewrites it with your own database.     * */
    public void createDataBase() throws IOException
    {
        boolean dbExist = checkDataBase();
        if(dbExist){
            //do nothing - database already exist
        }else{
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            myDataBase.execSQL("create table Contacts("+KEY_NAME+" text primary key,"+KEY_MAIL+" text,"+KEY_PASSWORD+" text"+")");
        }
    }
    /**     * Check if the database already exist to avoid re-copying the file each time you open the application.     * @return true if it exists, false if it doesn't     */
    private boolean checkDataBase()
    {       SQLiteDatabase checkDB = null;
        try
        {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e)
        {
            //database does't exist yet.
        }
        if(checkDB != null)
        {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    public void openDataBase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        try{
        myDataBase = SQLiteDatabase.openOrCreateDatabase(myPath, null);}
        catch (Exception e){
          String s =  e.getMessage();
        }
    }
    public void insertData(String tableName, String nullColumnHack, ContentValues values){
        openDataBase();
        myDataBase.insert(tableName,nullColumnHack,values);
        close();
    }
    @Override
    public synchronized void close()
    {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try {
            createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  }
    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.
}
