package com.expenselog;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by siddhant on 10/17/16.
 */
public class ExpenseByCategory extends AppCompatActivity {


    PieChart mChart;
    // we're going to display pie chart for Expense Log
    private int[] yValues = {10};
    private String[] xValues = {"Total", "Remaining", "Running", "Inflow"};


    final ArrayList<Integer> newItems = new ArrayList<>();
    EditText userInput;

    // colors for different sections in pieChart
    public static  final int[] MY_COLORS = {
            Color.rgb(42,98,253), Color.rgb(255,65,132), Color.rgb(233,246,55),
            Color.rgb(80,175,83), Color.rgb(215,60,55)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expensebycategory_card);

        mChart = (PieChart) findViewById(R.id.chart1);

        //   mChart.setUsePercentValues(true);
        mChart.setDescription("");

        mChart.setRotationEnabled(true);

        mChart.setCenterText(generateCenterSpannableText());

        //progressBar = (ProgressBar) findViewById(R.id.custom_progress_bar);
        //progressBar.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

        userInput= (EditText) findViewById(R.id.userInput);




        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                Toast.makeText(ExpenseByCategory.this,
                        xValues[e.getXIndex()] + " is " + e.getVal() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // setting sample Data for Pie Chart
        //setDataForPieChart();
        addItem();


    }

    public void addItem()
    {

        Button addItem = (Button) findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                try{
                    if(!userInput.getText().equals(""))
                    {
                        //String temp = userInput.getText().toString();
                        int item = Integer.parseInt(userInput.getText().toString());
                        newItems.add(item);
                    }
                }
                catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }

                ArrayList<Entry> yVals1 = new ArrayList<Entry>();

                /* for (int i = 0; i < yValues.length; i++)
                    yVals1.add(new Entry(yValues[i], i)); */

                for (int i = 0; i < newItems.size(); i++)
                    yVals1.add(new Entry(newItems.get(i), i));

                ArrayList<String> xVals = new ArrayList<String>();

                for (int i = 0; i < xValues.length; i++)
                    xVals.add(xValues[i]);

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

    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("OverView");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 8, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 8, s.length() , 0);
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
}
