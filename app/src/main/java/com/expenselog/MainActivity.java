package com.expenselog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.expenselog.adapterPackage.OverViewCardAdapter;
import com.expenselog.database.DBBank;
import com.expenselog.database.DBHelperExpense;
import com.expenselog.database.DBSalary;
import com.expenselog.database.DBWallet;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DBHelperExpense mydb;
    private DBSalary mydb_salary;
    private DBWallet mydb_wallet;
    private DBBank mydb_bank;

    private RelativeLayout salaryLayout;
    private RelativeLayout walletLayout;
    private RelativeLayout bankLayout;

    private LinearLayout mRevealView, viewTransactionLayout, editAccountLayout, addTransactionLayout;

    private OverViewCardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] mDataset = {"Total Spent"};
    private int mDatasetTypes[] = {0}; //view types

    private boolean hidden = true;
    private boolean reveal = true;

    private static final String TAG_ADD_EXPENSE = "addExpense";
    private static final String TAG_ADD_INCOME = "addIncome";
    private static final String TAG_ADD_REMINDER = "addReminder";

    private FloatingActionButton actionButton;
    private FloatingActionMenu actionMenu;

    private TextView title, amountSalary, amountWallet, amountBank, mainTotalBalance;
    private double amount, tBalance = 0.00;
    private String rs = "Rs";

    //private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;

    private Toolbar toolbar;

    private DrawerLayout drawer;

    //Pie Chart start
    private PieChart mChart;
    // we're going to display pie chart for Expense Log
    private int[] yValues = {10};
    private String[] xValues = {"Total"};

    private boolean state = false;

    final List<Float> intItems = new ArrayList<>();
    final List<String> stringItems = new ArrayList<>();
    //EditText intUserInput, stringUserInput;

    // colors for different sections in pieChart
    public static final int[] MY_COLORS = {
            Color.rgb(42, 98, 253), Color.rgb(255, 65, 132), Color.rgb(233, 246, 55),
            Color.rgb(80, 175, 83), Color.rgb(215, 60, 55)
    };


    public static final String SALARY_PREFERENCE = "salaryPrefs";
    public static final String SALARY_KEY = "salaryKey";

    SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mChart = (PieChart) findViewById(R.id.chart1);

        //   mChart.setUsePercentValues(true);
        mChart.setDescription("");

        mChart.setRotationEnabled(true);

        mChart.setCenterText(generateCenterSpannableText());

        //progressBar = (ProgressBar) findViewById(R.id.custom_progress_bar);
        //progressBar.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

        //stringUserInput = (EditText) findViewById(R.id.stringUserInput);
        //intUserInput= (EditText) findViewById(R.id.intUserInput);


        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                // Toast.makeText(MainActivity.this,xValues[e.getXIndex()] + " is " + (e.getVal()) + "", Toast.LENGTH_SHORT).show();\
                Toast.makeText(MainActivity.this, stringItems.get(e.getXIndex()) + " is " + (e.getVal()) + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        //addItem();
        pieChart();


        amountSalary = (TextView) findViewById(R.id.amountSalary);

        amountWallet = (TextView) findViewById(R.id.amountWallet);

        amountBank = (TextView) findViewById(R.id.amountBank);

        mainTotalBalance = (TextView) findViewById(R.id.mainTotalBalance);

        relativeLayout = (RelativeLayout) findViewById(R.id.totalBalanceLayout);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, Accounts.class);
                startActivity(i);
            }
        });

        salaryLayout = (RelativeLayout) findViewById(R.id.salaryLayout);

        salaryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.option_dialog,null);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                addTransactionLayout = (LinearLayout) mView.findViewById(R.id.addTransactionLayout);
                addTransactionLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(MainActivity.this,"Add Transaction Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                editAccountLayout = (LinearLayout) mView.findViewById(R.id.editAccountLayout);
                editAccountLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MainActivity.this, EditAccount.class);
                        //intent.putExtra("salaryAccountName","SALARY");
                        intent.putExtra("accountName",1);
                        startActivity(intent);

                        dialog.dismiss();
                        Toast.makeText(MainActivity.this,"Edit Account Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                viewTransactionLayout = (LinearLayout) mView.findViewById(R.id.viewTransactionLayout);
                viewTransactionLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(MainActivity.this,"View Transaction Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();

            }
        });


        walletLayout = (RelativeLayout) findViewById(R.id.walletLayout);

        walletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.option_dialog, null);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                addTransactionLayout = (LinearLayout) mView.findViewById(R.id.addTransactionLayout);
                addTransactionLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(MainActivity.this,"Add Transaction Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                editAccountLayout = (LinearLayout) mView.findViewById(R.id.editAccountLayout);
                editAccountLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MainActivity.this, EditAccount.class);
                        //intent.putExtra("walletAccountName","WALLET");
                        intent.putExtra("accountName",2);
                        startActivity(intent);

                        dialog.dismiss();
                        Toast.makeText(MainActivity.this,"Edit Account Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                viewTransactionLayout = (LinearLayout) mView.findViewById(R.id.viewTransactionLayout);
                viewTransactionLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(MainActivity.this,"View Transaction Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();

            }
        });

        bankLayout = (RelativeLayout) findViewById(R.id.bankLayout);

        bankLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.option_dialog, null);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                addTransactionLayout = (LinearLayout) mView.findViewById(R.id.addTransactionLayout);
                addTransactionLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(MainActivity.this,"Add Transaction Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                editAccountLayout = (LinearLayout) mView.findViewById(R.id.editAccountLayout);
                editAccountLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MainActivity.this, EditAccount.class);
                        //intent.putExtra("bankAccountName","BANK");
                        intent.putExtra("accountName",3);
                        startActivity(intent);

                        dialog.dismiss();

                        Toast.makeText(MainActivity.this,"Edit Account Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                viewTransactionLayout = (LinearLayout) mView.findViewById(R.id.viewTransactionLayout);
                viewTransactionLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(MainActivity.this,"View Transaction Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();

            }
        });

        title = (TextView) findViewById(R.id.textAccount);

        mRevealView = (LinearLayout) findViewById(R.id.reveal_items);
        mRevealView.setVisibility(View.GONE);

        /**
         FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
         fab.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {

        int cx = (mRevealView.getLeft() + mRevealView.getRight());
        int cy = mRevealView.getTop();
        int radius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());

        try {
        //Below Android LOLIPOP Version
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

        SupportAnimator animator =
        ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(700);

        SupportAnimator animator_reverse = animator.reverse();

        if (hidden) {
        mRevealView.setVisibility(View.VISIBLE);
        animator.start();
        hidden = false;
        } else {
        animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
        @Override public void onAnimationStart() {

        }

        @Override public void onAnimationEnd() {
        mRevealView.setVisibility(View.INVISIBLE);
        hidden = true;

        }

        @Override public void onAnimationCancel() {

        }

        @Override public void onAnimationRepeat() {

        }
        });
        animator_reverse.start();
        }
        }
        // Android LOLIPOP And ABOVE Version

        else {
        if (hidden) {

        Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
        mRevealView.setVisibility(View.VISIBLE);
        anim.start();

        hidden = false;
        } else {
        Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, radius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
        @Override public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        mRevealView.setVisibility(View.INVISIBLE);
        hidden = true;
        }
        });
        anim.start();

        }
        }

        }catch (Exception e)
        {
        synchronized (e) {
        e.notify();
        }
        //e.notify();
        }
        }
        });

         **/


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
                    Toast.makeText(MainActivity.this, "Left Drawer - Overview", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_account) {
                    Toast.makeText(MainActivity.this, "Left Drawer - Accounts", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, Accounts.class);
                    startActivity(intent);

                } else if (id == R.id.nav_report) {
                    Toast.makeText(MainActivity.this, "Left Drawer - Reports", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, Reports.class);
                    startActivity(intent);

                } else if (id == R.id.nav_bill_remainders) {
                    Toast.makeText(MainActivity.this, "Left Drawer - Bill Reminders", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, BillReminders.class);
                    startActivity(intent);

                } else if (id == R.id.nav_categories) {
                    Toast.makeText(MainActivity.this, "Left Drawer - Categories", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, Categories.class);
                    startActivity(intent);
                }

                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });


        NavigationView rightNavigationView = (NavigationView) findViewById(R.id.nav_right_view);
        rightNavigationView.setItemIconTintList(null);
        rightNavigationView.getMenu().getItem(0).setChecked(true);
        rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle Right navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_settings) {
                    Toast.makeText(MainActivity.this, "Right Drawer - Settings", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, Settings.class);
                    startActivity(intent);

                } else if (id == R.id.nav_alert) {
                    Toast.makeText(MainActivity.this, "Right Drawer - Set Alerts", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, SetAlert.class);
                    startActivity(intent);

                } else if (id == R.id.nav_cloudSync) {
                    Toast.makeText(MainActivity.this, "Right Drawer - Cloud Sync", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, CloudSync.class);
                    startActivity(intent);

                } else if (id == R.id.nav_database) {
                    Toast.makeText(MainActivity.this, "Right Drawer - Database Tools", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, DatabaseTools.class);
                    startActivity(intent);

                }

                drawer.closeDrawer(GravityCompat.END);
                return true;
            }
        });


        /**  mRecyclerView = (RecyclerView) findViewById(R.id.recyclerOverView);
         mLayoutManager = new LinearLayoutManager(MainActivity.this);
         mRecyclerView.setLayoutManager(mLayoutManager);
         //Adapter is created in the last step
         mAdapter = new OverViewCardAdapter(mDataset, mDatasetTypes);

         //accountAnimation();
         //show(linearLayout);

         mRecyclerView.setAdapter(mAdapter);  **/

        if (reveal) {

            int cx = (mRevealView.getLeft() + mRevealView.getRight());
            int cy = mRevealView.getTop();
            int radius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());

            try {
                //Below Android LOLIPOP Version
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    SupportAnimator animator =
                            ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(700);

                    SupportAnimator animator_reverse = animator.reverse();

                    if (hidden) {
                        mRevealView.setVisibility(View.VISIBLE);
                        animator.start();
                        hidden = false;
                    } else {
                        animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                            @Override
                            public void onAnimationStart() {

                            }

                            @Override
                            public void onAnimationEnd() {
                                mRevealView.setVisibility(View.INVISIBLE);
                                hidden = true;

                            }

                            @Override
                            public void onAnimationCancel() {

                            }

                            @Override
                            public void onAnimationRepeat() {

                            }
                        });
                        animator_reverse.start();
                    }
                }
                // Android LOLIPOP And ABOVE Version

                else {
                    if (hidden) {

                        mRevealView.setVisibility(View.VISIBLE);
                        Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
                        //mRevealView.setVisibility(View.VISIBLE);
                        anim.setDuration(700);
                        anim.start();

                        hidden = false;
                    } else {
                        Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, radius, 0);
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mRevealView.setVisibility(View.INVISIBLE);
                                hidden = true;
                            }
                        });
                        anim.start();

                    }
                }

            } catch (Exception e) {
                synchronized (e) {
                    e.notify();
                }
                //e.notify();
            }

        }

        buildFAB();

        updateSalary();

        updateWallet();

        updateBank();

        updateTotal();
    }

    private void buildFAB() {

        ImageView iconActionButton = new ImageView(this);
        //iconActionButton.setImageResource(R.drawable.overview);

        //FloatingActionButton.LayoutParams paramsMain = new FloatingActionButton.LayoutParams(60, 60);
        actionButton = new FloatingActionButton.Builder(this)
                .setContentView(iconActionButton)
                //.setLayoutParams(paramsMain)
                .setBackgroundDrawable(R.drawable.overview)
                .build();

        ImageView imageViewItem01 = new ImageView(this);
        imageViewItem01.setImageResource(R.drawable.add_expanse);

        ImageView imageViewItem02 = new ImageView(this);
        imageViewItem02.setImageResource(R.drawable.add_income);

        ImageView imageViewItem03 = new ImageView(this);
        imageViewItem03.setImageResource(R.drawable.bill_remainder);

        //set the background for all the sub buttons
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        FloatingActionButton.LayoutParams paramsSubItems = new FloatingActionButton.LayoutParams(100, 100);
        itemBuilder.setLayoutParams(paramsSubItems);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_sub_button));

        //build the sub buttons
        SubActionButton buttonAddExpense = itemBuilder.setContentView(imageViewItem01).build();
        SubActionButton buttonAddIncome = itemBuilder.setContentView(imageViewItem02).build();
        SubActionButton buttonAddReminder = itemBuilder.setContentView(imageViewItem03).build();

        //set tag to sub buttons
        buttonAddExpense.setTag(TAG_ADD_EXPENSE);
        buttonAddIncome.setTag(TAG_ADD_INCOME);
        buttonAddReminder.setTag(TAG_ADD_REMINDER);

        //set onClickListener to sub buttons
        buttonAddExpense.setOnClickListener(this);
        buttonAddIncome.setOnClickListener(this);
        buttonAddReminder.setOnClickListener(this);

        //add the sub buttons to the main floating action button
        actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonAddExpense)
                .addSubActionView(buttonAddIncome)
                .addSubActionView(buttonAddReminder)
                .attachTo(actionButton)
                .build();

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {  /*Closes the Appropriate  */
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
            System.exit(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_openRight) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                drawer.openDrawer(GravityCompat.END);
            } else if (!drawer.isDrawerOpen(GravityCompat.END)) {  /*Closes the Appropriate Drawer*/
                drawer.openDrawer(GravityCompat.END);
            } else if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if (v.getTag().equals(TAG_ADD_EXPENSE)) {
            //Toast.makeText(this,"Add Expense Was Clicked !!",Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, AddExpense.class);
            startActivity(i);
            actionMenu.close(true);
        }
        if (v.getTag().equals(TAG_ADD_INCOME)) {
            //Toast.makeText(this,"Add Income Was Clicked !!",Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, AddIncome.class);
            startActivity(i);
            actionMenu.close(true);
        }
        if (v.getTag().equals(TAG_ADD_REMINDER)) {
            //Toast.makeText(this,"Add Reminder Was Clicked !!",Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, AddReminder.class);
            startActivity(i);
            actionMenu.close(true);
        }
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
                        int item = Integer.parseInt(intUserInput.getText().toString());
                        String stringItem = stringUserInput.getText().toString();

                        stringItems.add(stringItem);
                        intItems.add(item);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"Please Fill required fields",Toast.LENGTH_LONG).show();
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

                ArrayList<Entry> yVals1 = new ArrayList<Entry>();



                for (int i = 0; i < intItems.size(); i++)
                    yVals1.add(new Entry(intItems.get(i), i));

                ArrayList<String> xVals = new ArrayList<String>();

                for (int i = 0; i < stringItems.size(); i++)
                    xVals.add(stringItems.get(i));



                // create pieDataSet
                PieDataSet dataSet = new PieDataSet(yVals1, "");
                dataSet.setSliceSpace(3);
                dataSet.setSelectionShift(5);

                // adding colors
                ArrayList<Integer> colors = new ArrayList<Integer>();

                // Added My Own colors
                for (int c : MY_COLORS)
                    colors.add(c);


                dataSet.setColors(colors);

                //  create pie data object and set xValues and yValues and set it to the pieChart
                PieData data = new PieData(xVals, dataSet);
                //   data.setValueFormatter(new DefaultValueFormatter());
                //   data.setValueFormatter(new PercentFormatter());

                data.setValueFormatter(new MyValueFormatter());
                data.setValueTextSize(10f);
                data.setValueTextColor(Color.WHITE);

                mChart.setData(data);

                // undo all highlights
                mChart.highlightValues(null);

                // refresh/update pie chart
                mChart.invalidate();

                // animate piechart
                mChart.animateXY(1400, 1400);


                // Legends to show on bottom of the graph
                Legend l = mChart.getLegend();
                l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
                l.setXEntrySpace(7);
                l.setYEntrySpace(5);
            }
        });

    }           */

    public void pieChart() {

        mydb = new DBHelperExpense(this);

        try {

            /*
            if (!stringItems.isEmpty() && !intItems.isEmpty()) {

                stringItems.addAll(mydb.getAllItem());
                intItems.addAll(mydb.getAllAmount());

            } */
            if (mydb.checkForTables()) {
                stringItems.addAll(mydb.getAllItem());
                intItems.addAll(mydb.getAllAmount());
            } else {
                Toast.makeText(this, "Data table is empty.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }


        /**
         Bundle extras = getIntent().getExtras();
         if (extras != null) {

         stringItems.add(extras.getString("stringItem"));
         intItems.add(extras.getInt("intItem"));

         }     **/

        List<Entry> yVals1 = new ArrayList<Entry>();

                /* for (int i = 0; i < yValues.length; i++)
                    yVals1.add(new Entry(yValues[i], i)); */

        if(intItems.size() > 0)
        {
            for (int i = 0; i < intItems.size(); i++)
                yVals1.add(new Entry(intItems.get(i), i));
        }
        else {
            Toast.makeText(this,"intItem is empty !!",Toast.LENGTH_LONG).show();
        }
        /*
        for (int i = 0; i < intItems.size(); i++)
            yVals1.add(new Entry(intItems.get(i), i));  */

        List<String> xVals = new ArrayList<String>();

        if(stringItems.size() > 0)
        {
            for (int i = 0; i < stringItems.size(); i++)
                xVals.add(stringItems.get(i));
        }
        else {
            Toast.makeText(this,"stringItem is empty !!",Toast.LENGTH_LONG).show();
        }
        /*
        for (int i = 0; i < stringItems.size(); i++)
            xVals.add(stringItems.get(i));            */

               /* for (int i = 0; i < xValues.length; i++)
                    xVals.add(xValues[i]); */

        // create pieDataSet
        PieDataSet dataSet = new PieDataSet(yVals1, " ");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // adding colors
        List<Integer> colors = new ArrayList<Integer>();

        // Added My Own colors
        for (int c : MY_COLORS)
            colors.add(c);


        dataSet.setColors(colors);

        //  create pie data object and set xValues and yValues and set it to the pieChart
        PieData data = new PieData(xVals, dataSet);
        //   data.setValueFormatter(new DefaultValueFormatter());
        //   data.setValueFormatter(new PercentFormatter());

        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // refresh/update pie chart
        mChart.invalidate();

        // animate piechart
        mChart.animateXY(1400, 1400);


        // Legends to show on bottom of the graph
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Overview");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 8, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 8, s.length(), 0);
        //s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        //s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        //s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        //s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }


    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + ""; // e.g. append a dollar-sign
        }
    }

    public void updateSalary()
    {
        mydb_salary = new DBSalary(getApplicationContext());
        try
        {
            if(mydb_salary.checkForTables()) //check for table in database
            {
                double sAmount = mydb_salary.getAmount();
                amountSalary.setText(rs+String.valueOf(String.format("%.2f", sAmount)));
            }
            else
            {
                Toast.makeText(this,"No data in salary database",Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void updateWallet()
    {
        mydb_wallet= new DBWallet(getApplicationContext());
        try
        {
            if(mydb_wallet.checkForTables()) //check for table in database
            {
                double wAmount = mydb_wallet.getAmount();
                amountWallet.setText(rs+String.valueOf(String.format("%.2f", wAmount)));
            }
            else
            {
                Toast.makeText(this,"No data in salary database",Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void updateBank()
    {
        mydb_bank = new DBBank(getApplicationContext());
        try
        {
            if(mydb_bank.checkForTables()) //check for table in database
            {
                double bAmount = mydb_bank.getAmount();
                amountBank.setText(rs+String.valueOf(String.format("%.2f", bAmount)));
            }
            else
            {
                Toast.makeText(this,"No data in salary database",Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }



    public void updateTotal()
    {
        mydb_salary = new DBSalary(getApplicationContext());
        mydb_wallet = new DBWallet(getApplicationContext());
        mydb_bank = new DBBank(getApplicationContext());

        try{
            if(mydb_salary.checkForTables())
            {
                double salaryAmount = mydb_salary.getAmount();
                tBalance = tBalance+salaryAmount;
                mainTotalBalance.setText(rs+String.valueOf(String.format("%.2f", tBalance)));
            }
            if(mydb_wallet.checkForTables())
            {
                double walletAmount = mydb_wallet.getAmount();
                tBalance = tBalance+walletAmount;
                mainTotalBalance.setText(rs+String.valueOf(String.format("%.2f", tBalance)));

            }
            if(mydb_bank.checkForTables())
            {
                double bankAmount = mydb_bank.getAmount();
                tBalance = tBalance+bankAmount;
                mainTotalBalance.setText(rs+String.valueOf(String.format("%.2f", tBalance)));
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
            else
            {
                //totalBalance.setText(String.valueOf(String.format("%.2f", tBalance)));
                Toast.makeText(this, "No data found in database!!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*
    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {

        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }   */
/**

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_openRight) {


                    return true;
        }

        return super.onOptionsItemSelected(item);
    }

 /**
    public void accountAnimation()
    {
        int cx = (mRevealView.getLeft() + mRevealView.getRight());
        int cy = mRevealView.getTop();
        int radius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());

        try {
            //Below Android LOLIPOP Version
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                SupportAnimator animator =
                        ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(700);

                SupportAnimator animator_reverse = animator.reverse();

                if (hidden) {
                    mRevealView.setVisibility(View.VISIBLE);
                    animator.start();
                    hidden = false;
                } else {
                    animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                        @Override
                        public void onAnimationStart() {

                        }

                        @Override
                        public void onAnimationEnd() {
                            mRevealView.setVisibility(View.INVISIBLE);
                            hidden = true;

                        }

                        @Override
                        public void onAnimationCancel() {

                        }

                        @Override
                        public void onAnimationRepeat() {

                        }
                    });
                    animator_reverse.start();
                }
            }
            // Android LOLIPOP And ABOVE Version
            /**
            else {
                if (hidden) {

                    Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
                    mRevealView.setVisibility(View.VISIBLE);
                    anim.start();

                    hidden = false;
                } else {
                    Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mRevealView.setVisibility(View.INVISIBLE);
                            hidden = true;
                        }
                    });
                    anim.start();

                }
            }

            // create the animator for this view (the start radius is zero)
            Animator anim = null;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy,
                        0, radius);
            }
            anim.setDuration(1000);

            // make the view visible and start the animation
            mRevealView.setVisibility(View.VISIBLE);
            anim.start();

        }catch (Exception e)
        {
            synchronized (e) {
                e.notify();
            }
            //e.notify();
        }
    }

    private void show(final View view) {
        // get the center for the clipping circle
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = android.view.ViewAnimationUtils.createCircularReveal(view, cx, cy,
                    0, finalRadius);
        }
        anim.setDuration(1000);

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

  **/

}



