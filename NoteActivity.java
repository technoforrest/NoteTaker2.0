package com.example.schwartz.pa5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {

    //initializations
    private long id;
    private String title;
    private String category;
    private String content;
    private int imageResourceId;
    private String type[] = {"Category","Personal","School","Work","Other"};
    private String TAG = "NoteActivity";
    private Integer images[] = {0, R.drawable.personal, R.drawable.school, R.drawable.work, R.drawable.other};


    /**
     * creates parts of the program
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializations
        final String type[] = {"Category","Personal","School","Work","Other"};
        final Spinner spinner = new Spinner(this);
        final EditText titleTxt = new EditText(this);
        final EditText contentTxt = new EditText(this);
        Button doneButton = new Button(this);


        //creates the grid layout
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(2);
        gridLayout.setRowCount(5);
        Intent intent = getIntent();

        if (intent != null) {
            id = intent.getLongExtra("Id", -1);
            title = intent.getStringExtra("Title");
            category = intent.getStringExtra("Category");
            content = intent.getStringExtra("Content");
            imageResourceId = intent.getIntExtra("ImageResourceId", 0);

            //spinner.setAdapter(new SpinnerAdapter(NoteActivity.this, R.layout.activity_note, type));
            ArrayAdapter<String> adapter = new SpinnerAdapter(NoteActivity.this, R.layout.activity_note, type);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                /**
                 * gets and saves the category of the note in the drop down menu
                 * @param adapterView
                 * @param view
                 * @param i
                 * @param l
                 */
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String item = (String) adapterView.getItemAtPosition(i);
                    Toast.makeText(adapterView.getContext(), "Item selected: " + item, Toast.LENGTH_LONG).show();
                }

                /**
                 * if nothing is selected
                 * @param adapterView
                 */
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        //creates the title editText
        titleTxt.setText(title);
        titleTxt.setHint("Title");
        titleTxt.setTag(1);
        GridLayout.Spec titleRow = GridLayout.spec(0);
        GridLayout.Spec titleCol = GridLayout.spec(0, 1, 3);
        GridLayout.LayoutParams titleParams = new GridLayout.LayoutParams(titleRow, titleCol);
        titleTxt.setLayoutParams(titleParams);

        //creates the content editText
        contentTxt.setText(content);
        contentTxt.setHint("Content");
        contentTxt.setTag(1);
        GridLayout.Spec contentRow = GridLayout.spec(1);
        GridLayout.Spec contentCol = GridLayout.spec(0, 2);
        GridLayout.LayoutParams contentParams = new GridLayout.LayoutParams(contentRow, contentCol);
        contentTxt.setLayoutParams(contentParams);

        //creates the done button
        doneButton.setHint("Done");
        doneButton.setTag(1);
        GridLayout.Spec doneButtonRow = GridLayout.spec(4);
        GridLayout.Spec doneButtonCol = GridLayout.spec(0, 2);
        GridLayout.LayoutParams doneButtonParams = new GridLayout.LayoutParams(doneButtonRow, doneButtonCol);
        doneButton.setLayoutParams(doneButtonParams);
        doneButton.setWidth(800);

        //adds title to grid layout
        gridLayout.addView(titleTxt);

        //adds spinner to grid layout
        gridLayout.addView(spinner);

        //adds content to grid layout
        gridLayout.addView(contentTxt);

        //adds done button to grid layout
        gridLayout.addView(doneButton);

        doneButton.setOnClickListener(new View.OnClickListener(){
            /**
             * implements the done button to go back to MainActivity screen
             * @param view
             */
            @Override
            public void onClick(View view){
                if(!titleTxt.getText().toString().isEmpty())
                {
                    title = titleTxt.getText().toString();
                    content = contentTxt.getText().toString();
                    Intent intent = new Intent(NoteActivity.this, MainActivity.class);
                    intent.putExtra("Id", id);
                    intent.putExtra("Title", title);
                    intent.putExtra("Category", category);
                    intent.putExtra("Content", content);
                    intent.putExtra("ImageRousourceId", 0);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter A Title.", Toast.LENGTH_SHORT).show();
                }
                    /*NoteDBHelper databaseHelper = new NoteDBHelper(NoteActivity.this);
                    title = titleTxt.getText().toString();
                    content = contentTxt.getText().toString();
                    databaseHelper.insertNote(new Note(id, title, category, content, imageResource));
                    finish();*/

            }
        });
        setContentView(gridLayout);
    }

    public class SpinnerAdapter extends ArrayAdapter<String> {
        public SpinnerAdapter(Context context, int textViewResourceId,
                              String[] objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d(TAG, "getView: " + position);
            LayoutInflater inflater=getLayoutInflater();
            View item = inflater.inflate(R.layout.activity_note, parent, false);
            TextView label = (TextView) item.findViewById(R.id.tvLanguage);
            ImageView icon=(ImageView)item.findViewById(R.id.imgLanguage);
            if(type[1].toString()== category){
                label.setText(type[1]);
                icon.setImageResource(images[1]);
            }else{
                label.setText(type[position]);
                icon.setImageResource(images[position]);
            }

            return item;
        }
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            Log.d(TAG, "getDropDownView: " + position);
            // TODO Auto-generated method stub

            return getView(position, convertView, parent);

        }


    }
}