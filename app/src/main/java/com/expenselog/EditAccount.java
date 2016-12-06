package com.expenselog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.expenselog.database.DBBank;
import com.expenselog.database.DBSalary;
import com.expenselog.database.DBWallet;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditAccount extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView accountName, txtDate;

    private EditText startingBalance, description;

    private Button cancelButton, saveButton;
    private Calendar calendar;
    private DateFormat dateFormat;

    private Spinner spinner;

    private DBSalary mydb_salary;
    private DBWallet mydb_wallet;
    private DBBank mydb_bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account);

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

        accountName = (TextView) findViewById(R.id.accountName);
        txtDate  = (TextView) findViewById(R.id.spDate);

        startingBalance = (EditText) findViewById(R.id.startingBalance);
        description = (EditText) findViewById(R.id.txtDescription);

        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());

       // salaryUpdate();//method call
        //walletUpdate();
        //bankUpdate();
        accountNameUpdate();

        update();   //method call

        spinnerDate();  //method call

        //Button method call
        cancelButton();
        saveButton();

    }

    public void accountNameUpdate()
    {
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            /*
            String sName = extras.getString("salaryAccountName");

            String wName = extras.getString("walletAccountName");

            String bName = extras.getString("bankAccountName"); */

            int accountNumber = extras.getInt("accountName");

            try{

                /*
                if(sName == "SALARY")
                {
                    accountName.setText(sName);

                }else if( wName == "WALLET")
                {
                    accountName.setText(wName);

                }else if(bName == "BANK")
                {
                    accountName.setText(bName);

                }else
                {
                    Toast.makeText(this,"Account name not found !!",Toast.LENGTH_SHORT).show();
                }
                            */
                switch (accountNumber)
                {
                    case 1:
                        accountName.setText("SALARY");
                        break;

                    case 2:
                        accountName.setText("WALLET");
                        break;

                    case 3:
                        accountName.setText("BANK");
                        break;

                    default:
                        Toast.makeText(this,"Account name not found !!",Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }


        }

    }
    public void salaryUpdate()
    {
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            String name = extras.getString("salaryAccountName");

            accountName.setText(name);
        }
    }

    public void walletUpdate()
    {
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            String name = extras.getString("walletAccountName");

            accountName.setText(name);
        }
    }

    public void bankUpdate()
    {
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            String name = extras.getString("bankAccountName");

            accountName.setText(name);
        }
    }

    public void spinnerDate()
    {
        spinner = (Spinner) findViewById(R.id.spinnerDate);
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    DatePickerDialog.newInstance(EditAccount.this,
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                            .show(getFragmentManager(), "datePicker");
                }

                return false;
            }
        });
    }

    public void cancelButton()
    {
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void saveButton()
    {
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                    if(!startingBalance.getText().toString().equals(" ") &&
                            !description.getText().toString().equals(" ") )
                    {
                        //String temp = userInput.getText().toString();
                        //int amount = Integer.parseInt(intUserInput.getText().toString());

                        if(accountName.getText().toString().equals("SALARY")) {

                            double amount = Double.parseDouble(startingBalance.getText().toString());
                            String item = description.getText().toString();
                            String date = txtDate.getText().toString();

                            Intent intent = new Intent(EditAccount.this, Accounts.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            //intent.putExtra("amountSalary", amount);
                            intent.putExtra("accountNumber",1);
                            //intent .putExtra("accountName","salary");

                            mydb_salary = new DBSalary(getApplicationContext());
                            if(mydb_salary.checkForTables())
                            {
                                mydb_salary.updateAmount(amount);
                                Toast.makeText(EditAccount.this,"Inside update salary !!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                mydb_salary.insertData(amount, item, date);
                                Toast.makeText(EditAccount.this,"Inside insert salary !!",Toast.LENGTH_SHORT).show();
                            }

                            startActivity(intent);

                            finish();

                            Toast.makeText(EditAccount.this,"Inside salary !!",Toast.LENGTH_SHORT).show();

                        }
                        else if(accountName.getText().toString().equals("WALLET"))
                        {
                            double amount = Double.parseDouble(startingBalance.getText().toString());
                            String item = description.getText().toString();
                            String date = txtDate.getText().toString();

                            Intent intent = new Intent(EditAccount.this, Accounts.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            //intent.putExtra("amountWallet", amount);
                            intent.putExtra("accountNumber",2);
                            //intent .putExtra("accountName","wallet");

                            mydb_wallet = new DBWallet(getApplicationContext());
                            if(mydb_wallet.checkForTables())
                            {
                                mydb_wallet.updateAmount(amount);
                                Toast.makeText(EditAccount.this,"Inside update wallet !!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                mydb_wallet.insertData(amount, item, date);
                                Toast.makeText(EditAccount.this,"Inside insert wallet !!",Toast.LENGTH_SHORT).show();
                            }

                            startActivity(intent);

                            finish();
                        }
                        else if(accountName.getText().toString().equals("BANK"))
                        {
                            //accountName = BANK

                            double amount = Double.parseDouble(startingBalance.getText().toString());
                            String item = description.getText().toString();
                            String date = txtDate.getText().toString();

                            Intent intent = new Intent(EditAccount.this, Accounts.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            //intent.putExtra("amountBank", amount);
                            intent.putExtra("accountNumber",3);
                            //intent .putExtra("accountName","bank");

                            mydb_bank = new DBBank(getApplicationContext());
                            if(mydb_bank.checkForTables())
                            {
                                mydb_bank.updateAmount(amount);
                                Toast.makeText(EditAccount.this,"Inside update bank !!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                mydb_bank.insertData(amount, item, date);
                                Toast.makeText(EditAccount.this,"Inside insert bank !!",Toast.LENGTH_SHORT).show();
                            }

                            startActivity(intent);

                            finish();
                        }

                    }
                    else
                    {
                        Toast.makeText(EditAccount.this,"Please Fill required fields",Toast.LENGTH_LONG).show();
                    }
                }
                catch (StringIndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                }
                catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }


    public void update()
    {
        txtDate.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        update();
    }
}
