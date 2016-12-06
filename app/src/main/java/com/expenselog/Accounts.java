package com.expenselog;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.expenselog.database.DBBank;
import com.expenselog.database.DBSalary;
import com.expenselog.database.DBWallet;

public class Accounts extends AppCompatActivity {

    private RelativeLayout salaryLayout;
    private RelativeLayout walletLayout;
    private RelativeLayout bankLayout;

    private TextView amountSalary, amountWallet, amountBank, totalBalance;

    private LinearLayout viewTransactionLayout, editAccountLayout, addTransactionLayout;

    public static final String SALARY_PREFERENCE = "salaryPrefs";
    public static final String SALARY_KEY = "salaryKey";

    public static final String WALLET_PREFERENCE = "walletPrefs";
    public static final String WALLET_KEY = "walletKey";

    public static final String BANK_PREFERENCE = "bankPrefs";
    public static final String BANK_KEY = "bankKey";

    SharedPreferences sharedPreferences ;
    SharedPreferences sp;

    double amount, retrieveAmount, tBalance = 0.00;

    private DrawerLayout drawer;

    private DBSalary mydb_salary;
    private DBWallet mydb_wallet;
    private DBBank mydb_bank;

    private String rs = "Rs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        NavigationView leftNavigationView = (NavigationView) findViewById(R.id.nav_left_view);
        leftNavigationView.setItemIconTintList(null);
        leftNavigationView.getMenu().getItem(0).setChecked(true);
        leftNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle Left navigation view item clicks here.

                int id = item.getItemId();

                if (id == R.id.nav_overview) {
                    Toast.makeText(Accounts.this, "Left Drawer - Overview", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Accounts.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else if (id == R.id.nav_account) {
                    Toast.makeText(Accounts.this, "Left Drawer - Accounts", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Accounts.this, Accounts.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else if (id == R.id.nav_report) {
                    Toast.makeText(Accounts.this, "Left Drawer - Reports", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Accounts.this, Reports.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else if (id == R.id.nav_bill_remainders) {
                    Toast.makeText(Accounts.this, "Left Drawer - Bill Reminders", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Accounts.this, BillReminders.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else if (id == R.id.nav_categories) {
                    Toast.makeText(Accounts.this, "Left Drawer - Categories", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Accounts.this, Categories.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        /*
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!amountSalary.getText().toString().equals(" ") && !amountSalary.getText().toString().equals("0.00"))
                {
                    String sAmount = amountSalary.getText().toString();
                    Intent intent = new Intent(Accounts.this, MainActivity.class);

                    intent.putExtra("salaryAmount", sAmount);
                    startActivity(intent);
                }

                finish();
            }
        });                        */

        totalBalance = (TextView) findViewById(R.id.accountTotalBalance);

        amountSalary = (TextView) findViewById(R.id.amountSalary);

        amountWallet = (TextView) findViewById(R.id.amountWallet);

        amountBank = (TextView) findViewById(R.id.amountBank);

        salaryLayout = (RelativeLayout) findViewById(R.id.salaryLayout);

        salaryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Accounts.this);
                View mView = getLayoutInflater().inflate(R.layout.option_dialog,null);

                addTransactionLayout = (LinearLayout) mView.findViewById(R.id.addTransactionLayout);
                addTransactionLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(Accounts.this,"Add Transaction Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                editAccountLayout = (LinearLayout) mView.findViewById(R.id.editAccountLayout);
                editAccountLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(Accounts.this,"Edit Account Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                viewTransactionLayout = (LinearLayout) mView.findViewById(R.id.viewTransactionLayout);
                viewTransactionLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(Accounts.this,"View Transaction Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });


        walletLayout = (RelativeLayout) findViewById(R.id.walletLayout);

        walletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Accounts.this);
                View mView = getLayoutInflater().inflate(R.layout.option_dialog, null);

                addTransactionLayout = (LinearLayout) mView.findViewById(R.id.addTransactionLayout);
                addTransactionLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(Accounts.this,"Add Transaction Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                editAccountLayout = (LinearLayout) mView.findViewById(R.id.editAccountLayout);
                editAccountLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(Accounts.this,"Edit Account Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                viewTransactionLayout = (LinearLayout) mView.findViewById(R.id.viewTransactionLayout);
                viewTransactionLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(Accounts.this,"View Transaction Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });

        bankLayout = (RelativeLayout) findViewById(R.id.bankLayout);

        bankLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Accounts.this);
                View mView = getLayoutInflater().inflate(R.layout.option_dialog, null);

                addTransactionLayout = (LinearLayout) mView.findViewById(R.id.addTransactionLayout);
                addTransactionLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(Accounts.this,"Add Transaction Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                editAccountLayout = (LinearLayout) mView.findViewById(R.id.editAccountLayout);
                editAccountLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(Accounts.this,"Edit Account Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                viewTransactionLayout = (LinearLayout) mView.findViewById(R.id.viewTransactionLayout);
                viewTransactionLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(Accounts.this,"View Transaction Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });


        //updateAccounts();
        //updateSalary();//method call
        //updateAccounts();

        totalBalance();//method call

        //updateWallet();//method call

        //updateBank();//method call

        updateSalaryFromDB();

        updateWalletFromDB();

        updateBankFromDB();



    }

    public void updateAccounts()
    {
        mydb_salary = new DBSalary(getApplicationContext());
        mydb_wallet = new DBWallet(getApplicationContext());
        mydb_bank = new DBBank(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            int accountNumber = extras.getInt("accountNumber");

            try{

                switch (accountNumber)
                {
                    case 1:
                        updateSalaryFromDB();
                        //double sAmount = mydb_salary.getAmount();
                        //amountSalary.setText(rs+String.valueOf(String.format("%.2f", sAmount)));
                        break;

                    case 2:
                        updateWalletFromDB();
                        //double wAmount = mydb_wallet.getAmount();
                        //amountWallet.setText(rs+String.valueOf(String.format("%.2f", wAmount)));
                        break;

                    case 3:
                        updateBankFromDB();
                        //double bAmount = mydb_bank.getAmount();
                        //amountWallet.setText(rs+String.valueOf(String.format("%.2f", bAmount)));
                        break;

                    default:
                        Toast.makeText(this,"Balance not found !!",Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }


    }
    public void updateSalaryFromDB()
    {
        mydb_salary = new DBSalary(getApplicationContext());

        try {

            if(!String.valueOf(mydb_salary.getAmount()).equals(" ")) {
                double sAmount = mydb_salary.getAmount();
                amountSalary.setText(rs+String.valueOf(String.format("%.2f", sAmount)));
            }
            else
            {
                Toast.makeText(this, "Database is empty !!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateWalletFromDB()
    {
        mydb_wallet = new DBWallet(getApplicationContext());

        try {

            if(!String.valueOf(mydb_wallet.getAmount()).equals(" ")) {
                double wAmount = mydb_wallet.getAmount();
                amountWallet.setText(rs+String.valueOf(String.format("%.2f", wAmount)));
            }
            else
            {
                Toast.makeText(this, "Database is empty !!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateBankFromDB()
    {
        mydb_bank= new DBBank(getApplicationContext());

        try {

            if(!String.valueOf(mydb_bank.getAmount()).equals(" ")) {
                double bAmount = mydb_bank.getAmount();
                amountBank.setText(rs+String.valueOf(String.format("%.2f", bAmount)));
            }
            else
            {
                Toast.makeText(this, "Database is empty !!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void updateSalary()
    {
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            amount = extras.getDouble("amountSalary");
            amountSalary.setText(String.valueOf(String.format("%.2f", amount)));

            /******* Create SharedPreferences *******/
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            sharedPreferences = getApplicationContext().getSharedPreferences(SALARY_PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            /**************** Storing data as KEY/VALUE pair *******************/
            editor.putLong(SALARY_KEY, Double.doubleToRawLongBits(amount));

            // Save the changes in SharedPreferences
            editor.apply();

            Toast.makeText(this, "second Time UpdateSalary", Toast.LENGTH_SHORT).show();
        }
        else if(extras == null)
        {
            sharedPreferences = getApplicationContext().getSharedPreferences(SALARY_PREFERENCE, MODE_PRIVATE);

            if(sharedPreferences.contains(SALARY_KEY)) {
                double retrieveAmount = getDouble(sharedPreferences, SALARY_KEY, amount);

                amountSalary.setText(String.valueOf(String.format("%.2f", retrieveAmount)));
            }
            else
            {
                Toast.makeText(this, "No Key Found !!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "second Time UpdateSalary", Toast.LENGTH_SHORT).show();
        }
    }

    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {

        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
     }

    public void updateWallet()
    {
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            amount = extras.getDouble("amountWallet");
            amountWallet.setText(String.valueOf(String.format("%.2f", amount)));

            /******* Create SharedPreferences *******/
            sp = getApplicationContext().getSharedPreferences(WALLET_PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            /**************** Storing data as KEY/VALUE pair *******************/
            editor.putLong(WALLET_KEY, Double.doubleToRawLongBits(amount));

            // Save the changes in SharedPreferences
            editor.apply();
        }
        else if(extras == null)
        {
            sp = getApplicationContext().getSharedPreferences(WALLET_PREFERENCE, MODE_PRIVATE);

            if(sp.contains(WALLET_KEY)) {
                double retrieveAmount = getDouble(sp, WALLET_KEY, amount);

                amountSalary.setText(String.valueOf(String.format("%.2f", retrieveAmount)));
            }
            else
            {
                Toast.makeText(this, "No Key Found !!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateBank()
    {
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            amount = extras.getDouble("amountWallet");
            amountBank.setText(String.valueOf(String.format("%.2f", amount)));

            /******* Create SharedPreferences *******/
            sharedPreferences = getApplicationContext().getSharedPreferences(BANK_PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            /**************** Storing data as KEY/VALUE pair *******************/
            editor.putLong(BANK_KEY, Double.doubleToRawLongBits(amount));

            // Save the changes in SharedPreferences
            editor.apply();
        }
        else if(extras == null)
        {
            sharedPreferences = getApplicationContext().getSharedPreferences(BANK_PREFERENCE, MODE_PRIVATE);

            if(sharedPreferences.contains(BANK_KEY)) {
                double retrieveAmount = getDouble(sharedPreferences, BANK_KEY, amount);

                amountSalary.setText(String.valueOf(String.format("%.2f", retrieveAmount)));
            }
            else
            {
                Toast.makeText(this, "No Key Found !!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
    public double getData()
    {
        sharedPreferences = getApplicationContext().getSharedPreferences(MyPREFERENCE, MODE_PRIVATE);

        if(sharedPreferences.contains(Amount)) {

            retrieveAmount = getDouble(sharedPreferences, Amount, amount);

            //amountSalary.setText(String.valueOf(String.format("%.2f", retrieveAmount)));
        }
        else
        {
            Toast.makeText(this, "No Key Found !!", Toast.LENGTH_SHORT).show();
        }
        return retrieveAmount;
    }                       */

    public void totalBalance()
    {
        mydb_salary = new DBSalary(getApplicationContext());
        mydb_wallet = new DBWallet(getApplicationContext());
        mydb_bank = new DBBank(getApplicationContext());

        try{
            if(mydb_salary.checkForTables())
            {
                double salaryAmount = mydb_salary.getAmount();
                tBalance = tBalance+salaryAmount;
                totalBalance.setText(rs+String.valueOf(String.format("%.2f", tBalance)));
            }
            if(mydb_wallet.checkForTables())
            {
                double walletAmount = mydb_wallet.getAmount();
                tBalance = tBalance+walletAmount;
                totalBalance.setText(rs+String.valueOf(String.format("%.2f", tBalance)));

            }
            if(mydb_bank.checkForTables())
            {
                double bankAmount = mydb_bank.getAmount();
                tBalance = tBalance+bankAmount;
                totalBalance.setText(rs+String.valueOf(String.format("%.2f", tBalance)));
            }

            /*
            if(mydb_salary.checkForTables() && mydb_wallet.checkForTables())
            {
                double salaryAmount = mydb_salary.getAmount();
                double walletAmount = mydb_wallet.getAmount();
                tBalance = tBalance+salaryAmount+walletAmount;
                totalBalance.setText(rs+String.valueOf(String.format("%.2f", tBalance)));

            }
            if(mydb_salary.checkForTables() && mydb_bank.checkForTables())
            {
                double salaryAmount = mydb_salary.getAmount();
                double bankAmount = mydb_bank.getAmount();
                tBalance = tBalance+salaryAmount+bankAmount;
                totalBalance.setText(rs+String.valueOf(String.format("%.2f", tBalance)));

            }
            if(mydb_wallet.checkForTables() && mydb_bank.checkForTables())
            {
                double walletAmount = mydb_wallet.getAmount();
                double bankAmount = mydb_bank.getAmount();
                tBalance = tBalance+walletAmount+bankAmount;
                totalBalance.setText(rs+String.valueOf(String.format("%.2f", tBalance)));

            }
            if(mydb_salary.checkForTables() && mydb_wallet.checkForTables() && mydb_bank.checkForTables())
            {
                double salaryAmount = mydb_salary.getAmount();
                double walletAmount = mydb_wallet.getAmount();
                double bankAmount = mydb_bank.getAmount();
                tBalance = tBalance+salaryAmount+walletAmount+bankAmount;
                totalBalance.setText(rs+String.valueOf(String.format("%.2f", tBalance)));
            }

                        */
        /*
        sharedPreferences = getApplicationContext().getSharedPreferences(SALARY_PREFERENCE, MODE_PRIVATE);

        if(sharedPreferences.contains(SALARY_KEY)) {

            double salaryAmount = getDouble(sharedPreferences, SALARY_KEY, amount);

            tBalance = tBalance + salaryAmount;

            totalBalance.setText(String.valueOf(String.format("%.2f", tBalance)));

        } */
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /*

    public void updateAccounts()
    {
        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            String accountName = extras.getString("accountName");

            if(accountName.equals("salary"))
            {
                updateSalary();
            }
            else if(accountName.equals("wallet"))
            {
                updateWallet();
            }
            else if(accountName.equals("bank"))
            {
                updateBank();
            }
            else
            {
                Toast.makeText(this, "Inside updateAccounts", Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            Toast.makeText(this, "Inside updateAccounts", Toast.LENGTH_SHORT).show();
        }
    }
                              */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {  /*Closes the Appropriate  */
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
            //System.exit(0);
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
