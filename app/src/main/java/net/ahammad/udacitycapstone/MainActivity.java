package net.ahammad.udacitycapstone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;

import net.ahammad.udacitycapstone.fragments.AddReminderFragment;
import net.ahammad.udacitycapstone.fragments.MainFragment;
import net.ahammad.udacitycapstone.fragments.ReminderDetailsFragment;
import net.ahammad.udacitycapstone.util.BusProvider;
import net.ahammad.udacitycapstone.util.ReminderBean;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity{

    Fragment mCurrentFragment;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        changeFragment(new MainFragment());
    }

    public void changeFragment (Fragment fragment,boolean addToBackStack) {
        // add condition to hidden add button
        mCurrentFragment = fragment;
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment,fragment.getClass().getName());

        if (addToBackStack)transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
//        checkFragment(1);
    }



    @Subscribe
    public void getDetails(ReminderBean item){
        ReminderDetailsFragment reminderDetailsFragment = new ReminderDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ReminderDetailsFragment.TITLE,item.getTitle());
        bundle.putString(ReminderDetailsFragment.EX_DATE,item.getExDate());
        bundle.putString(ReminderDetailsFragment.NO_OF_TIMES,item.getNumberOfTimes());
        bundle.putString(ReminderDetailsFragment.IMAGE,item.getImagePath());
        bundle.putDouble(ReminderDetailsFragment.LON, item.getLon());
        bundle.putDouble(ReminderDetailsFragment.LAT,item.getLat());
        reminderDetailsFragment.setArguments(bundle);
        changeFragment(reminderDetailsFragment, true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add:
                changeFragment(new AddReminderFragment(),true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void changeFragment (Fragment fragment){
      changeFragment(fragment,false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }


}
