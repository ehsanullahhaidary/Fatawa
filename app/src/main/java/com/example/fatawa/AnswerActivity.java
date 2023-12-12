package com.example.fatawa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class AnswerActivity extends AppCompatActivity {

    AlertDialog alertDialog;
    String questionSource;
    String answerSource;

    TextView myQuestionText;
    TextView myAnswerText;
    String share_Copy;

    DatabaseHelper databaseHelper;


    Button share;
    Button copy;
    Button fontSize;
    ImageButton bookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        final CharSequence[] values = {"15sp", "18sp", "22sp"};

        share = findViewById(R.id.share);
        copy = findViewById(R.id.copyToClipboard);
        fontSize = findViewById(R.id.fontSize);
        bookmark = findViewById(R.id.bookmarkShape);

        questionSource = getIntent().getExtras().get("questions").toString();
        answerSource = getIntent().getExtras().get("answers").toString();

        databaseHelper = new DatabaseHelper(this);


        int isMark = databaseHelper.getWordFromBookmark(questionSource);
        bookmark.setTag(isMark);
        int icon = isMark == 0? R.drawable.ic_baseline_bookmark_border_24:R.drawable.ic_baseline_bookmark_fill_24;
        bookmark.setImageResource(icon);


        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int)bookmark.getTag();
                if (i == 0){
                    bookmark.setImageResource(R.drawable.ic_baseline_bookmark_fill_24);
                    bookmark.setTag(1);
                    insertToBookmark(questionSource, answerSource);
                    Toast.makeText(AnswerActivity.this, "سوال مورد نظر به برگزیده ها اظافه شد.", Toast.LENGTH_SHORT).show();
                }else if (i == 1){
                    bookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                    bookmark.setTag(0);
                    databaseHelper.deleteFromBookmark(questionSource);
                }

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, share_Copy+"\n"+" برگرفته از برنامه فتاوای اهل سنت ");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(shareIntent, "اشتراک گذاری"));
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(" برگرفته از برنامه فتاوای اهل سنت ", share_Copy);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(AnswerActivity.this, "متن به clipboard کپی شد.", Toast.LENGTH_SHORT).show();

            }
        });

        fontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(AnswerActivity.this);
                dlgAlert.setTitle("انداره متن را انتخاب کنید.");
                dlgAlert.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                myAnswerText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                break;
                            case 1:
                                myAnswerText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                break;
                            case 2:
                                myAnswerText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                                break;
                        }
                       alertDialog.dismiss();
                    }
                });
                alertDialog = dlgAlert.create();
                alertDialog.show();
            }
        });


        myQuestionText = findViewById(R.id.questionText);
        myAnswerText = findViewById(R.id.answerText);

        myQuestionText.append(questionSource);
        myAnswerText.append(answerSource);

        share_Copy = questionSource + answerSource;
    }

    private void insertToBookmark(String questionSourceString, String answerSourceString) {
        databaseHelper.insertToBookmark(questionSourceString, answerSourceString);
    }

    

}
