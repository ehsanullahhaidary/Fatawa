package com.example.fatawa;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.lang.reflect.Array;


public class DatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;

    private String DATABASE_LOCATION = "";
    private String DATABASE_FULL_PATH = "";
    public SQLiteDatabase mDB;

    private static final String DATABASE_NAME = "islampp.db";
    private static final String TABLE_NAME = "data";

    private static final String COL_ID = "id";
    private static final String COL_QUESTION = "question";
    private static final String COL_ANSWER = "answer";


    private final String TBL_BOOKMARK = "bookmark";

    private final String COL_KEY = "key";
    private final String COL_VALUE = "value";

    ArrayList<String> questions;
    ArrayList<String> answers;


    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

        mContext = context;
        DATABASE_LOCATION = "data/data/com.example.fatawa/databases/";
        DATABASE_FULL_PATH = DATABASE_LOCATION + DATABASE_NAME;

        if (!isExistingDB()) {
            try {
                File dbLocation = new File(DATABASE_LOCATION);
                dbLocation.mkdirs();

                extractAssetToDatabaseDictionary(DATABASE_NAME);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_FULL_PATH, null);
    }


    boolean isExistingDB() {
        File file = new File(DATABASE_FULL_PATH);
        return file.exists();
    }


    public void extractAssetToDatabaseDictionary(String fileName)
            throws IOException { // copy the database
        int length;
        InputStream input = this.mContext.getAssets().open(fileName);

        String outFileName = DATABASE_FULL_PATH;
        OutputStream destination = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        while ((length = input.read(buffer)) > 0) {
            destination.write(buffer, 0, length);
        }

        input.close();
        destination.flush();
        destination.close();
    }


    public ArrayList<String> getQuestion2() {

        questions = new ArrayList<>();
        answers = new ArrayList<>();

        int counter = 0;
        while (counter < 50) {
            int random = (int) (Math.random() * 12000);
            String q = "SELECT * FROM " + TABLE_NAME + " WHERE id = " + random;
            Cursor result = mDB.rawQuery(q, null);
            if (result.moveToFirst()) {
                questions.add(result.getString(1));
                answers.add(result.getString(2));
            }

            counter++;
        }
        return questions;
    }
    public ArrayList<String> getQuestion3(){
        return answers;
    }


    public Cursor getQuestion(int random) {


            String q = "SELECT * FROM " + TABLE_NAME + " WHERE id = " + random;
            Cursor result = mDB.rawQuery(q, null);

         return result;
    }

    public Cursor getAllFromBookmark(){

        String q = "SELECT * FROM bookmark";
        Cursor result = mDB.rawQuery(q, null);

        return result;
    }

    public Cursor searchQuestion(String text){
        String q = "SELECT * FROM "+TABLE_NAME+" WHERE "+COL_QUESTION+" LIKE '%"+text+"%'";
        Cursor cursor = mDB.rawQuery(q, null);
        return cursor;
    }

    public ArrayList<Integer> searchInRead(ArrayList<String> source){
        int index = 0;
        int arrayIndex = 0;
        ArrayList array = new ArrayList();
        while (index < source.size()){
            String q = "SELECT * FROM custom WHERE readdata LIKE '"+source.get(index)+"'";
            Cursor cursor = mDB.rawQuery(q, null);
            if (cursor.getCount() == 1){
                array.add(Integer.valueOf(index));
            }
            if (cursor != null){
                cursor.close();
            }
            index++;
        }
        return array;
    }

    public Cursor searchAnswer(String text){
        String q = "SELECT * FROM "+TABLE_NAME+" WHERE "+COL_ANSWER+" LIKE '%"+text+"%'";
        Cursor cursor = mDB.rawQuery(q, null);
        return cursor;
    }

    public String SearchInBookmarkAnswers(String text){
        String q = "SELECT * FROM bookmark WHERE key = '"+text+"'";
        Cursor cursor = mDB.rawQuery(q, null);

        ArrayList<String> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            result.add(cursor.getString(1));
        }

        return result.get(0);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int getWordFromBookmark(String key) {
        String q = "SELECT * FROM bookmark WHERE key = '"+key+"'";
        Cursor result = mDB.rawQuery(q, null);

        if (result.getCount() == 1){
            return 1;
        }
        return 0;
    }


    public void deleteFromBookmark(String s) {
        String q = "DELETE FROM bookmark WHERE key = '"+s+"'";
        mDB.execSQL(q);
        Toast.makeText(mContext, "سوال مورد نظر از برگزیده ها حذف شد.", Toast.LENGTH_SHORT).show();
    }

    public void insertToBookmark(String questionSourceString, String answerSourceString) {
        String q = "INSERT INTO bookmark (key, value) VALUES ('"+questionSourceString+"', '"+answerSourceString+"')";

        try {
            mDB.execSQL(q);
        }catch (Exception e){
        }


    }

    public void cleanAllFromBookmark() {
        String q = "DELETE FROM bookmark";
        mDB.execSQL(q);
    }
}
