package com.hedspi.hoangviet.eslrecom.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hedspi.hoangviet.eslrecom.commons.Preference;
import com.hedspi.hoangviet.eslrecom.models.Book;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hoangviet on 1/8/16.
 */
public class DatabaseManager {
    private static DatabaseManager mInstance = null;
    private Context mContext;
    private DatabaseHelper dbHelper;

    private static DatabaseReference database = null;
    private static long booksCount;
    private static long bookProfilesCount;
    private static Preference preference;

    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private static final String DB_NAME = "esldatabase";
    private static final String DB_FILE = "demo.sqlite";
    private static final int DB_VERSION = 1;

    private DatabaseManager(Context context){
        mContext = context;
        dbHelper = new DatabaseHelper(mContext);
    }

    public static DatabaseReference getDatabase() {
        if(database == null){
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }

    public static long getBooksCount() {
        return booksCount;
    }

    public static void setBooksCount(long booksCount) {
        DatabaseManager.booksCount = booksCount;
    }

    public static Preference getPreference() {
        return preference;
    }

    public static void setPreference(Preference preference) {
        DatabaseManager.preference = preference;
    }

    public static long getBookProfilesCount() {
        return bookProfilesCount;
    }

    public static void setBookProfilesCount(long bookProfilesCount) {
        DatabaseManager.bookProfilesCount = bookProfilesCount;
    }

    public static DatabaseManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new DatabaseManager(context);
        }
        return mInstance;
    }

    public ArrayList<Book> getListLevelTest(){
        ArrayList<Book> list = new ArrayList<Book>();
        try {
            list = (ArrayList) dbHelper.getBookDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }


    //***************** HELPER ***********************//

    class DatabaseHelper extends OrmLiteSqliteOpenHelper{
        private Dao<Book, Integer> bookDao;

        public DatabaseHelper(Context context){
            super(context, DB_NAME, null, DB_VERSION);

            checkDatabase();

            /*File dbFile = mContext.getDatabasePath(DB_NAME);
            File out = new File(mContext.getExternalFilesDir(null)+"/output");
            Log.d("DB", out.getAbsolutePath());
            copyFile(dbFile, out, false);
            dataEncryptHelper = AESCodeManager.getInstance();*/
        }

        public void copyFile(File srcFile, File destFile, boolean append) {
            InputStream in = null;
            FileOutputStream out = null;

            if (!srcFile.exists()) return;

            try {
                in = new FileInputStream(srcFile);

                File parentFile = destFile.getParentFile();
                if (!destFile.exists()) {
                    if (!parentFile.exists()) parentFile.mkdirs();
                    destFile.createNewFile();
                }

                out = new FileOutputStream(destFile, append);
                byte data[] = new byte[1024];
                int length = -1;
                while ((length = in.read(data)) != -1) {
                    out.write(data, 0, length);
                }
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onCreate(SQLiteDatabase database, ConnectionSource source){
            /*try {
                TableUtils.createTable(source, UserCurrentPosition.class);

                //encryptDatabase(database);

            }catch (SQLException e){
                e.printStackTrace();
            }*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, ConnectionSource source, int oldVersion, int newVersion){
            /*try {
                TableUtils.dropTable(source, UserCurrentPosition.class, true);
                TableUtils.dropTable(source, UserCurrentLevels.class, true);
                TableUtils.dropTable(source, UserDayProgress.class, true);
                TableUtils.dropTable(source, WordProgress.class, true);
            }catch (SQLException e){
                e.printStackTrace();
            }*/
            copyDatabase();
            onCreate(database, source);
            Log.d("LOG","Database updated!");
        }

        public Dao<Book, Integer> getBookDao() throws SQLException{
            if(bookDao == null)
                bookDao = getDao(Book.class);
            return bookDao;
        }

        @Override
        public void close(){
            super.close();
            bookDao = null;
        }

        public void checkDatabase(){
            File dbFile = mContext.getDatabasePath(DB_NAME);
            if(!dbFile.exists()){
                copyDatabase();
                Log.d("LOG:","DB path: "+dbFile.toString());
                Log.d("LOG:", "successfully init database");
            }
            else{
                Log.d("LOG:", "already exist database");
            }
        }

        public void copyDatabase(){
            try {
                InputStream input = mContext.getAssets().open(DB_FILE);
                File destFile = mContext.getDatabasePath(DB_NAME);

                File parentFile = destFile.getParentFile();
                if (!destFile.exists()) {
                    if (!parentFile.exists()) parentFile.mkdirs();
                    destFile.createNewFile();
                }

                FileOutputStream output = new FileOutputStream(destFile);

                Log.d("LOG:","parentDir: "+parentFile.toString()+" - "+parentFile.exists());
                Log.d("LOG:","destDir: "+destFile.toString() + " - "+ destFile.exists());

                byte[] mBuffer = new byte[1024];
                int mLength;
                while ((mLength = input.read(mBuffer))>0)
                {
                    output.write(mBuffer, 0, mLength);
                }
                output.flush();
                input.close();
                output.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

}