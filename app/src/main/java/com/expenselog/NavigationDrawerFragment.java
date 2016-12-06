package com.expenselog;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements NavigationAdapter.ClickListener {

    private RecyclerView recyclerView;
    private NavigationAdapter adapter;

    public static final String PREF_FILE_NAME = "textpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private boolean mUserLearnedDrawer;
    private View containerView;
    private boolean mFromSavedInstanceState;

    private LinearLayout linearLayout;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));

        if(savedInstanceState == null)
        {
            mFromSavedInstanceState = true;
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        linearLayout = (LinearLayout) layout.findViewById(R.id.overViewLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);
            }
        });
        recyclerView = (RecyclerView) layout.findViewById(R.id.left_drawerList);

        adapter = new NavigationAdapter(getActivity(), getData());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }


    public static List<NavigationItems> getData()
    {
        List<NavigationItems> data = new ArrayList<>();
        int[] icons = {R.drawable.account, R.drawable.report, R.drawable.bill_remainder, R.drawable.categories_icon};
        String[] titles = {"Accounts", "Reports", "Bill Reminders", "Categories"};

        for(int i=0; i<icons.length && i<titles.length; i++)
        {
            NavigationItems current = new NavigationItems();
            current.iconId = icons[i];
            current.title = titles[i];

            data.add(current);
        }

        return data;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {

        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
                if(!mUserLearnedDrawer)
                {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer+"");
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };

        if(!mUserLearnedDrawer)
        {
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PREF_FILE_NAMe",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PREF_FILE_NAMe",Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }


    @Override
    public void itemClicked(View view, int position) {

        switch (position)
        {
            case 0:
                startActivity(new Intent(getActivity(), Accounts.class));
                break;

            case 1:
                startActivity(new Intent(getActivity(), Reports.class));
                break;

            case 2:
                startActivity(new Intent(getActivity(), BillReminders.class));
                break;

            case 3:
                startActivity(new Intent(getActivity(), Categories.class));
                break;
        }

    }
}

