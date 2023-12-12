package com.example.fatawa;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Spinner spinner;

    private int numbersOfSearch;


    RadioGroup radioGroup;
    RadioButton radioButton;
    EditText editText;
    Button button;
    Button bookmarks;
    ListView listView;

    ArrayList<String> questionSource;
    ArrayList<String> answerSource;


    ArrayAdapter arrayAdapter;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        spinner = findViewById(R.id.spinner_view);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.labels_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        radioGroup = findViewById(R.id.radioGroup);
        if (radioGroup.getCheckedRadioButtonId() != R.id.search_in_question || radioGroup.getCheckedRadioButtonId() != R.id.search_in_question) {
            radioGroup.check(R.id.search_in_question);
        }

            listView = findViewById(R.id.list_view);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            questionSource = new ArrayList<>();
            answerSource = new ArrayList<>();

            bookmarks = findViewById(R.id.bookmarkButton);
            bookmarks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openBookmarkActivity();
                }
            });

            databaseHelper = new DatabaseHelper(MainActivity2.this);


            if (savedInstanceState == null) {
                viewData();
            } else {
                questionSource = savedInstanceState.getStringArrayList("question");
                answerSource = savedInstanceState.getStringArrayList("answer");
            }




            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, questionSource) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    final View renderer = super.getView(position, convertView, parent);
                    convertView = findViewById(R.id.list_view);
                    listView.setSelection(position);

                    if (position == 7) {
                        // convertView.setBackgroundResource(android.R.color.white);
                        //renderer.setBackgroundResource(android.R.color.darker_gray);
                    }


                    return renderer;
                }
            };
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(MainActivity2.this, AnswerActivity.class);

                    String par1 = String.valueOf(questionSource.get(position));
                    String par2 = String.valueOf(answerSource.get(position));
                    intent.putExtra("questions", par1);
                    intent.putExtra("answers", par2);
                    startActivity(intent);


                }
            });



            editText = findViewById(R.id.editText);
            button = findViewById(R.id.buttonOfSearch);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchInput();
                    handled = true;
                }
                return handled;
            }
        });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchInput();
                }
            });
        }

        private void openBookmarkActivity () {
            Intent intent = new Intent(this, BookmarkActivity.class);
            startActivity(intent);

        }

        private void hideSoftKeyboard (){
            // Check if no view has focus:
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        private void searchInput(){
            String searched = editText.getText().toString();
            if (editText.getText().toString().equals("")) {
                Toast.makeText(MainActivity2.this, "جعبه متن خالی است", Toast.LENGTH_SHORT).show();
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.search_in_question) {
                searchedInQuestion(searched);
                arrayAdapter.notifyDataSetChanged();
                hideSoftKeyboard();
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.search_in_answer) {
                searchedInAnswer(searched);
                arrayAdapter.notifyDataSetChanged();
                hideSoftKeyboard();
            }
        }

        private void viewData () {
            questionSource = databaseHelper.getQuestion2();
            answerSource = databaseHelper.getQuestion3();
        }

        public void searchedInQuestion (String text){
            Cursor cursor = databaseHelper.searchQuestion(text);
            questionSource.clear();
            answerSource.clear();

            int i = 0;
            while (cursor.moveToNext() & i < numbersOfSearch) {
                questionSource.add(cursor.getString(1));
                answerSource.add(cursor.getString(2));
                i++;
            }
        }

        public void searchedInAnswer (String text){
            Cursor cursor = databaseHelper.searchAnswer(text);
            questionSource.clear();
            answerSource.clear();

            int i = 0;
            while (cursor.moveToNext() & i < numbersOfSearch) {
                questionSource.add(cursor.getString(1));
                answerSource.add(cursor.getString(2));
                i++;
            }
        }


        @Override
        public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
            String s = parent.getItemAtPosition(position).toString();

            switch (s) {
                case "نتایج:30":
                    numbersOfSearch = 30;
                    break;
                case "نتایج:60":
                    numbersOfSearch = 60;
                    break;
                case "نتایج:120":
                    numbersOfSearch = 120;
                    break;
                case "نتایج:240":
                    numbersOfSearch = 240;
                    break;
            }

        }


        @Override
        public void onNothingSelected (AdapterView < ? > parent){

        }

        public void setRadioButton (View view){
            radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
            Toast.makeText(this, radioButton.getText(), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onSaveInstanceState (@NonNull Bundle outState){
            super.onSaveInstanceState(outState);
            outState.putStringArrayList("question", questionSource);
            outState.putStringArrayList("answer", answerSource);
        }

        @Override
        protected void onRestoreInstanceState (@NonNull Bundle savedInstanceState){
            super.onRestoreInstanceState(savedInstanceState);

            ArrayList<String> getQuestion = savedInstanceState.getStringArrayList("question");
            ArrayList<String> getAnswer = savedInstanceState.getStringArrayList("answer");
        }

        public void getReadItems () {
            ArrayList<Integer> arrayIndex = databaseHelper.searchInRead(questionSource);
            Toast.makeText(this, "items in arraylist: "+String.valueOf(arrayIndex.size()), Toast.LENGTH_SHORT).show();
        }
    }
