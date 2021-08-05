package com.myapplicationdev.android.ourndpsongs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import sg.edu.rp.c346.id20014241.ourndpsongs.R;

public class ThirdActivity extends AppCompatActivity {

    EditText etID, etTitle, etSingers, etYear;
    Button btnCancel, btnUpdate, btnDelete;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        setTitle(getTitle().toString() + " ~ " + getResources().getText(R.string.title_activity_third));

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        etID = (EditText) findViewById(R.id.etID);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etSingers = (EditText) findViewById(R.id.etSingers);
        etYear = (EditText) findViewById(R.id.etYear);
        ratingBar = findViewById(R.id.ratingbarStars);

        Intent i = getIntent();
        final Song currentSong = (Song) i.getSerializableExtra("song");

        etID.setText(currentSong.getId()+"");
        etTitle.setText(currentSong.getTitle());
        etSingers.setText(currentSong.getSingers());
        etYear.setText(currentSong.getYearReleased()+"");

        ratingBar.setRating(currentSong.getStars());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(ThirdActivity.this);
                currentSong.setTitle(etTitle.getText().toString().trim());
                currentSong.setSingers(etSingers.getText().toString().trim());
                int year = 0;
                try {
                    year = Integer.valueOf(etYear.getText().toString().trim());
                } catch (Exception e){
                    Toast.makeText(ThirdActivity.this, "Invalid year", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentSong.setYearReleased(year);

                currentSong.setStars((int) ratingBar.getRating());
                int result = dbh.updateSong(currentSong);
                if (result>0){
                    Toast.makeText(ThirdActivity.this, "Song updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ThirdActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DBHelper dbh = new DBHelper(ThirdActivity.this);

                AlertDialog.Builder myBuilder = new AlertDialog.Builder(ThirdActivity.this);
                myBuilder.setTitle("DANGER");
                myBuilder.setMessage("Are you sure you want to delete the island \n" + etTitle.getText().toString());
                myBuilder.setCancelable(false);

                myBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int result = dbh.deleteSong(currentSong.getId());
                        dbh.deleteSong(result);
                        finish();
                    }
                });

                myBuilder.setNeutralButton("Cancel", null);
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder myBuilder = new AlertDialog.Builder(ThirdActivity.this);
                myBuilder.setTitle("Danger");
                myBuilder.setMessage("Are you sure you want to discard the changes");
                myBuilder.setCancelable(false);

                myBuilder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

                myBuilder.setNegativeButton("Do Not Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();

            }
        });

    }


}