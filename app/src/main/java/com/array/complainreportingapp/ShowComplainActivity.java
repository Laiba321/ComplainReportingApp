package com.array.complainreportingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class ShowComplainActivity extends AppCompatActivity {
    private TextView CTextView;
    private TextView STextView;
    private TextView DTextView;
    private ImageView SImageView;
    private PersonDBHelper dbHelper;
    private long receivedPersonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_complain);

        CTextView = (TextView)findViewById(R.id.show_category);
        STextView = (TextView)findViewById(R.id.show_severity);
        DTextView = (TextView)findViewById(R.id.show_description);
        SImageView = (ImageView)findViewById(R.id.show_image);

        dbHelper = new PersonDBHelper(this);

        try {
            //get intent to get person id
            receivedPersonId = getIntent().getLongExtra("USER_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Person queriedPerson = dbHelper.getPerson(receivedPersonId);
        //set field to this user data
        CTextView.setText("Category: " +queriedPerson.getCategory());
        STextView.setText("Severity: " +queriedPerson.getSeverity());
        DTextView.setText("Description: " +queriedPerson.getDescription());
        SImageView.setImageBitmap(queriedPerson.getImage());





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_complain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.show_c:
                actionCancel();
                break;


        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        actionCancel();
    }

    private void actionCancel() {
        startActivity(new Intent(this, MainActivity.class));
    }

}