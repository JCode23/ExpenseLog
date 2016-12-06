package com.expenselog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.expenselog.database.DBHelperExpense;

import java.io.File;
import java.io.FileWriter;

import au.com.bytecode.opencsv.CSVWriter;

public class DatabaseTools extends AppCompatActivity {

    private Button exportCSV;
    private DBHelperExpense mydb_expense;
    private File exportDir;
    private AsyncTaskRunner myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_tools);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        exportCSV = (Button) findViewById(R.id.exportCSV);

    }

    public void exportCSV(View view)
    {
        mydb_expense = new DBHelperExpense(getApplicationContext());
        if(mydb_expense.checkForTables())
        {
            exportDB();

            myAsyncTask = new AsyncTaskRunner();
            myAsyncTask.execute();

            dialogBox();
        }
        else
        {
            Toast.makeText(this,"Database is empty !!",Toast.LENGTH_LONG).show();
        }
    }

    private void exportDB()
    {
        //File dbFile=getDatabasePath("ChartData.db");
        DBHelperExpense dbhelper = new DBHelperExpense(getApplicationContext());

        String folderName = "ExpenseLog";
        exportDir = new File(Environment.getExternalStorageDirectory(), folderName);
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "Expense.csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM data",null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to export
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("DatabaseTools", sqlEx.getMessage(), sqlEx);
        }

    }

    public void dialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Backup Successful!");
        alertDialogBuilder.setMessage(exportDir.toString() );
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        boolean running;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            int i = 10;
            while(running){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(i-- == 0){
                    running = false;
                }

                publishProgress(String.valueOf(i));

            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            //finalResult.setText(result);
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(DatabaseTools.this,
                    "ProgressDialog",
                    "Loading...");
        }


    }
}
