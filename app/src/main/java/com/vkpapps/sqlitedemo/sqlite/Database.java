package com.vkpapps.sqlitedemo.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import com.vkpapps.sqlitedemo.model.Profile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static Database database;

    private Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static Database getDatabase(Context context) {
        if (database==null){
            database = new Database(
                    context,"profiles",null,1
            );
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table profiles(id INTEGER PRIMARY KEY AUTOINCREMENT , name TEXT, picture BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Profile> getProfiles(Context context){
        ArrayList<Profile> profiles = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * from profiles", null);
        while (cursor.moveToNext()){
            Profile profile = new Profile();
            profile.setId(Integer.parseInt(cursor.getString(0)));
            profile.setName(cursor.getString(1));
            byte[] blob = cursor.getBlob(2);
            try {
                File file = new File(context.getDir("profilesFetched", Context.MODE_PRIVATE),profile.getId()+".png");
                if (!file.exists()&&file.createNewFile()){
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(blob);
                    fos.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            profiles.add(profile);
        }
        cursor.close();

        return profiles;
    }
    public void addProfile(Profile profile,Context context){
        String sql = "INSERT INTO profiles (name,picture) VALUES(?,?)";
        SQLiteStatement insertStmt = database.getWritableDatabase().compileStatement(sql);
        insertStmt.clearBindings();
        insertStmt.bindString(1,profile.getName());
        try {
            File file = new File(context.getDir("profiles", Context.MODE_PRIVATE), String.valueOf(profile.getId()));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            if (file.exists()){
                FileInputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int read;
                while ((read=inputStream.read(bytes))>0){
                    bos.write(bytes,0,read);
                }
                bos.close();
                insertStmt.bindBlob(2,bos.toByteArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        insertStmt.executeInsert();
    }
}
