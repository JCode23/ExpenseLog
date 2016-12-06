package com.expenselog.adapterPackage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.expenselog.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by siddhant on 11/18/16.
 */
public class SpinnerCategoryAdapter  extends RecyclerView.Adapter<SpinnerCategoryAdapter.View_Holder>{

    List<ItemData> list = Collections.emptyList();
    Context ctx;

    public SpinnerCategoryAdapter(List<ItemData> list, Context ctx)
    {
        this.list = list;
        this.ctx = ctx;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_category, parent, false);
        View_Holder holder = new View_Holder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView

        ItemData itemData = list.get(position);
        holder.image.setImageResource(list.get(position).getImageId());
        holder.text.setText(list.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView text;
        //List<ItemData> list = Collections.emptyList();
        //Context ctx;

        public View_Holder(View itemView) {
            super(itemView);

            //this.ctx = ctx;
            //this.list = list;

            //itemView.setOnClickListener(this);

            image = (ImageView) itemView.findViewById(R.id.spinnerCategoryImage);
            text = (TextView) itemView.findViewById(R.id.spinnerCategoryText);
        }

        /*
        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Intent intent = new Intent(this.ctx, AddExpense.class);
            intent.putExtra("position", position);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            Toast.makeText(this.ctx,"clicked : "+position,Toast.LENGTH_SHORT).show();

            this.ctx.startActivity(intent);
        }                                          */

    }

}
