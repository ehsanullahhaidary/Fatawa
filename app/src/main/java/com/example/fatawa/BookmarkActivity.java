package com.example.fatawa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class BookmarkActivity extends AppCompatActivity {

    ArrayList<String> bookmarkArrayList;
    DatabaseHelper databaseHelper;

    AlertDialog alertDialog;

    Button cleanAll;
    ListView listView;

    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        databaseHelper = new DatabaseHelper(this);

        bookmarkArrayList = new ArrayList<>();
        cleanAll = findViewById(R.id.clear);

        getAllWordFromBookmark();

        listView = findViewById(R.id.bookmarkListView);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookmarkArrayList);
        listView.setAdapter(arrayAdapter);

        cleanAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(BookmarkActivity.this);
                builder.setTitle("هشدار");
                builder.setMessage("آیا میخواهید همه برگزیذه ها را حذف کنید؟");
                builder.setCancelable(true);

                builder.setPositiveButton("بلی", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.cleanAllFromBookmark();
                        Toast.makeText(BookmarkActivity.this, "همه برکزیذه ها حذف شدند.", Toast.LENGTH_SHORT).show();
                        bookmarkArrayList.clear();
                        arrayAdapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(BookmarkActivity.this);
                builder.setTitle("هشدار");
                builder.setMessage("آیا میخواهید سوال مورد نظر را از برگزیده ها حذف کنید؟");
                builder.setCancelable(true);

                builder.setPositiveButton("بلی", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cleanFromBookmark(bookmarkArrayList.get(position));

                        bookmarkArrayList.clear();
                        getAllWordFromBookmark();
                        arrayAdapter.notifyDataSetChanged();

                    }
                });

                builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String value = bookmarkArrayList.get(position);

                Intent intent = new Intent(BookmarkActivity.this, AnswerActivity.class);

                String par1 = String.valueOf(bookmarkArrayList.get(position));
                String par2 = databaseHelper.SearchInBookmarkAnswers(value);

                intent.putExtra("questions", par1);
                intent.putExtra("answers", par2);
                startActivity(intent);

            }
        });

    }

    private void cleanFromBookmark(String s) {
        databaseHelper.deleteFromBookmark(s);
    }

    public void getAllWordFromBookmark() {
        Cursor cursor = databaseHelper.getAllFromBookmark();
        while (cursor.moveToNext()) {
            bookmarkArrayList.add(cursor.getString(0));
        }
        Collections.sort(bookmarkArrayList);
    }

}