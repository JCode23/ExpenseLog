package com.expenselog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.expenselog.adapterPackage.ItemData;
import com.expenselog.adapterPackage.RecyclerTouchListener;
import com.expenselog.adapterPackage.SpinnerCategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class SpinnerCategoryList extends AppCompatActivity {

    private List<ItemData> list;
    private RecyclerView mRecyclerView;
    private SpinnerCategoryAdapter spinnerCategoryAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private int pos = -1;
    private String selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.spinner_category_list);

        list = fillData();

        //RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerSpinnerCategory);
        mLayoutManager = new LinearLayoutManager(SpinnerCategoryList.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Adapter
        spinnerCategoryAdapter = new SpinnerCategoryAdapter(list, this);
        mRecyclerView.setAdapter(spinnerCategoryAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ItemData item = list.get(position);
                Toast.makeText(getApplicationContext(), item.getText() + " is selected!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //selectedItem();
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

    public void selectedItem()
    {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pos = extras.getInt("position");
        }

        if(pos >= 0) {
            String selectedItem = list.get(pos).getText().toString();
            Intent intent = new Intent(this, AddExpense.class);
            intent.putExtra("selectedItem", selectedItem);
            startActivity(intent);

            Toast.makeText(this,"Inside SelectItem",Toast.LENGTH_SHORT).show();
        }

    }
}
