package com.expenselog;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.expenselog.adapterPackage.ItemData;
import com.expenselog.adapterPackage.RecyclerTouchListener;
import com.expenselog.adapterPackage.SpinnerAdapter;
import com.expenselog.adapterPackage.SpinnerCategoryAdapter;
import com.expenselog.database.DBBank;
import com.expenselog.database.DBHelperExpense;
import com.expenselog.database.DBSalary;
import com.expenselog.database.DBWallet;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by siddhant on 11/17/16.
 */
public class AddExpense extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener{


    private EditText intUserInput, stringUserInput;
    private DBHelperExpense mydb ;
    private DBSalary mydb_salary;
    private DBWallet mydb_wallet;
    private DBBank mydb_bank;

    private AlertDialog dialog;

    private TextView txtCategory, txtDate;

    private String selectItem;

    private EditText itemName, itemAmount;
    private Button saveItem, cancelButton;

    //FragmentManager fm = null;
    //SpinnerCategoryList spinnerCategoryList;

    private int pos = -1;

    private Spinner spCategory, spDate, accountSpinner;

    private Calendar calendar;
    private DateFormat dateFormat;

    private List<ItemData> list, accountList;
    private RecyclerView mRecyclerView;
    private SpinnerCategoryAdapter spinnerCategoryAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());

        //stringUserInput = (EditText) findViewById(R.id.stringUserInput);
        //intUserInput= (EditText) findViewById(R.id.intUserInput);

        itemName = (EditText) findViewById(R.id.itemName);
        itemAmount = (EditText) findViewById(R.id.amount);

        txtCategory = (TextView) findViewById(R.id.txtCategory);
        txtDate = (TextView) findViewById(R.id.txtDate);

        accountList = populateAccountSpinner();

        accountSpinner = (Spinner)findViewById(R.id.accountSpinner);
        SpinnerAdapter adapter=new SpinnerAdapter(this,
                R.layout.spinner_layout,R.id.txt, accountList);
        accountSpinner.setAdapter(adapter);

        /*
        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //String accountName = accountSpinner.getSelectedItem().toString();
                //String accountName = ((TextView) view.findViewById(R.id.txt)).getText().toString();

                ItemData selectedItem = (ItemData) accountSpinner.getSelectedItem();
                String accountName = selectedItem.getText();

                Toast.makeText(AddExpense.this,"Spinner selected Item: "+accountName,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
                                     */
        update(); //method call
        //addItem();

        //populateAccountSpinner(); //method call

        //Spinner method call
        categorySpinner();
        dateSpinner();

        txtCategory();  //method call

        getSpinnerItem();

        //Button method call
        cancelButton();
        saveButton();

    }

    private void update() {
        txtDate.setText(dateFormat.format(calendar.getTime()));
    }

    /*
    public void addItem()
    {
        Button addItem = (Button) findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    if(!intUserInput.getText().equals("") && !stringUserInput.getText().equals(""))
                    {
                        //String temp = userInput.getText().toString();
                        //int amount = Integer.parseInt(intUserInput.getText().toString());

                        Float amount = Float.valueOf(intUserInput.getText().toString());
                        String item = stringUserInput.getText().toString();

                        Intent intent = new Intent(AddExpense.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        //intent.putExtra("intItem",amount);
                        //intent.putExtra("stringItem",item);

                        mydb = new DBHelperExpense(getApplicationContext());
                        mydb.insertData(item, amount);
                        startActivity(intent);

                        finish();

                    }
                    else
                    {
                        Toast.makeText(AddExpense.this,"Please Fill required fields",Toast.LENGTH_LONG).show();
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
    }                               */

    public List<ItemData> populateAccountSpinner()
    {
        List<ItemData> list=new ArrayList<>();
        list.add(new ItemData("SALARY",R.drawable.salary));
        list.add(new ItemData("WALLET",R.drawable.wallet));
        list.add(new ItemData("BANK",R.drawable.bank));
        //accountSpinner = (Spinner)findViewById(R.id.accountSpinner);
        //SpinnerAdapter adapter=new SpinnerAdapter(this,R.layout.spinner_layout,R.id.txt,list);
        //accountSpinner.setAdapter(adapter);
        return  list;
    }

    public void getSpinnerItem()
    {

        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //String accountName = parent.getSelectedItem().toString();
                //String accountName = ((Spinner)findViewById(R.id.accountSpinner)).getSelectedItem().toString();
                ItemData selectedItem = (ItemData) accountSpinner.getSelectedItem();
                String accountName = selectedItem.getText();

                Toast.makeText(AddExpense.this,"Spinner selected Item: "+accountName,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void categorySpinner()
    {
        //List<ItemData> list=new ArrayList<>();
        //list.add(new ItemData("Food",R.drawable.accomodation));

        spCategory=(Spinner)findViewById(R.id.categorySpinner);

        spCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //isSpinnerTouched = true;

                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    list = fillData();

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddExpense.this);
                    View mView = getLayoutInflater().inflate(R.layout.spinner_category_list, null);

                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();

                    //RecyclerView
                    mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerSpinnerCategory);
                    mLayoutManager = new LinearLayoutManager(AddExpense.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);

                    //Adapter
                    spinnerCategoryAdapter = new SpinnerCategoryAdapter(list, AddExpense.this);
                    mRecyclerView.setAdapter(spinnerCategoryAdapter);

                    mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            ItemData item = list.get(position);

                            txtCategory.setText(item.getText());

                            //Toast.makeText(getApplicationContext(), item.getText() + " is selected!", Toast.LENGTH_SHORT).show();
                            //finish();
                            dialog.dismiss();

                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));

                    dialog.show();

                }
                /*
                Intent intent = new Intent(AddExpense.this, SpinnerCategoryList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);                            */

                //dialogBox();
                    /*
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    //fm = getFragmentManager();
                    spinnerCategoryList = new SpinnerCategoryList();

                    spinnerCategoryList.show(ft, "category_tag");

                    Toast.makeText(AddExpense.this,"Inside equal to null",Toast.LENGTH_SHORT).show(); */

                    //spinnerCategoryList.onTouchEvent();

                /*
                else if(spinnerCategoryList != null){
                    //spinnerCategoryList.removeFragment();
                   android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentByTag("category_tag");
                    if (fragment != null) {
                        //ft.remove(prev);
                        getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
                    }

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    //fm = getFragmentManager();
                    spinnerCategoryList = new SpinnerCategoryList();

                    spinnerCategoryList.show(ft, "tag");

                    Toast.makeText(AddExpense.this,"Inside not equal to null",Toast.LENGTH_SHORT).show();
                }   */
                return false;
            }
        });

    }

    public void txtCategoryClick(View view)
    {
        list = fillData();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddExpense.this);
        View mView = getLayoutInflater().inflate(R.layout.spinner_category_list, null);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        //RecyclerView
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerSpinnerCategory);
        mLayoutManager = new LinearLayoutManager(AddExpense.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Adapter
        spinnerCategoryAdapter = new SpinnerCategoryAdapter(list, AddExpense.this);
        mRecyclerView.setAdapter(spinnerCategoryAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ItemData item = list.get(position);

                txtCategory.setText(item.getText());

                Toast.makeText(getApplicationContext(), item.getText() + " is selected!", Toast.LENGTH_SHORT).show();
                //finish();
                dialog.dismiss();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        dialog.show();
    }


    public void txtDateClick(View view)
    {
        DatePickerDialog.newInstance(AddExpense.this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show(getFragmentManager(), "datePicker");
    }

    public List<ItemData> fillData()
    {
        List<ItemData> dataList = new ArrayList<>();

        dataList.add(new ItemData("Accommodation",R.drawable.accomodation));
        dataList.add(new ItemData("Food",R.drawable.food));
        dataList.add(new ItemData("Shopping",R.drawable.shooping));
        dataList.add(new ItemData("Groceries",R.drawable.groceries));

        return dataList;
    }

    public void dateSpinner()
    {
        spDate = (Spinner) findViewById(R.id.dateSpinner);
        spDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    DatePickerDialog.newInstance(AddExpense.this,
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                            .show(getFragmentManager(), "datePicker");
                }

                return false;
            }
        });


    }

    /*
    public void dialogBox()
    {
        List<ItemData> list;

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddExpense.this).setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK ) {
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });

        View mView = getLayoutInflater().inflate(R.layout.spinner_category_list, null);

        mBuilder.setView(mView);

        dialog = mBuilder.create();
        dialog.setCanceledOnTouchOutside(true);

        list = fillData();

        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerSpinnerCategory);

        //SET PROPERTIES
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddExpense.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        spinnerCategoryAdapter = new SpinnerCategoryAdapter(list, getApplicationContext());
        recyclerView.setAdapter(spinnerCategoryAdapter);
        spinnerCategoryAdapter.notifyDataSetChanged();

        dialog.show();
    }                       */

    public void txtCategory()
    {
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
           pos = extras.getInt("position");
        }

        switch (pos)
        {
            case 0:
                txtCategory.setText("Accommodation");
                break;

            case 1:
                txtCategory.setText("Food");
                break;

            case 2:
                txtCategory.setText("Shopping");
                break;

            case 3:
                txtCategory.setText("Groceries");
                break;

            default:
                //Toast.makeText(this,"IndexOutOfBound Error",Toast.LENGTH_SHORT).show();
        }
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
        saveItem = (Button) findViewById(R.id.saveButton);
        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    if(!itemAmount.getText().toString().equals("") && !itemName.getText().toString().equals(""))
                    {
                        ItemData selectedItem = (ItemData) accountSpinner.getSelectedItem();
                        String accountName = selectedItem.getText();

                        if(accountName.equals("SALARY"))
                        {
                            Float amount = Float.valueOf(itemAmount.getText().toString());
                            String item = itemName.getText().toString();

                            mydb_salary = new DBSalary(getApplicationContext());
                            if(mydb_salary.checkForTables())
                            {
                                double sAccountBalance = mydb_salary.getAmount();
                                double remainingBalance = sAccountBalance - amount;

                                mydb_salary.updateAmount(remainingBalance);

                                Intent intent = new Intent(AddExpense.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                mydb = new DBHelperExpense(getApplicationContext());
                                mydb.insertData(item, amount);
                                startActivity(intent);

                                finish();

                                Toast.makeText(AddExpense.this,"Account: "+accountName+" Database is updated !!",Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                Toast.makeText(AddExpense.this,"Not enough balance in account: "+accountName,Toast.LENGTH_LONG).show();
                            }
                        }

                        else if(accountName.equals("WALLET"))
                        {
                            Float amount = Float.valueOf(itemAmount.getText().toString());
                            String item = itemName.getText().toString();

                            mydb_wallet = new DBWallet(getApplicationContext());
                            if(mydb_wallet.checkForTables())
                            {
                                double wAccountBalance = mydb_wallet.getAmount();
                                double remainingBalance = wAccountBalance - amount;

                                mydb_wallet.updateAmount(remainingBalance);

                                Intent intent = new Intent(AddExpense.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                mydb = new DBHelperExpense(getApplicationContext());
                                mydb.insertData(item, amount);
                                startActivity(intent);

                                finish();

                                Toast.makeText(AddExpense.this,"Account: "+accountName+" Database is updated !!",Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                Toast.makeText(AddExpense.this,"Not enough balance in account: "+accountName,Toast.LENGTH_LONG).show();
                            }
                        }
                        else if(accountName.equals("BANK"))
                        {
                            Float amount = Float.valueOf(itemAmount.getText().toString());
                            String item = itemName.getText().toString();

                            mydb_bank = new DBBank(getApplicationContext());
                            if(mydb_bank.checkForTables())
                            {
                                double bAccountBalance = mydb_bank.getAmount();
                                double remainingBalance = bAccountBalance - amount;

                                mydb_bank.updateAmount(remainingBalance);

                                Intent intent = new Intent(AddExpense.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                mydb = new DBHelperExpense(getApplicationContext());
                                mydb.insertData(item, amount);
                                startActivity(intent);

                                finish();

                                Toast.makeText(AddExpense.this,"Account: "+accountName+" Database is updated !!",Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                Toast.makeText(AddExpense.this,"Not enough balance in account: "+accountName,Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(AddExpense.this,"Spinner selected Item: "+accountName,Toast.LENGTH_LONG).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(AddExpense.this,"Please Fill required fields",Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        update();
    }

    @Override
    public void onClick(View v) {

    }
}
