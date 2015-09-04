package net.ahammad.udacitycapstone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.squareup.otto.Subscribe;

import net.ahammad.udacitycapstone.fragments.AddReminderFragment;
import net.ahammad.udacitycapstone.fragments.MainFragment;
import net.ahammad.udacitycapstone.fragments.ReminderDetailsFragment;
import net.ahammad.udacitycapstone.util.BusProvider;
import net.ahammad.udacitycapstone.util.ReminderBean;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Fragment mCurrentFragment;

    @Bind(R.id.delete)
    Button mAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAdd.setOnClickListener(this);
        changeFragment(new MainFragment());
    }

    public void changeFragment (Fragment fragment,boolean addToBackStack) {
        // add condition to hidden add button
        mCurrentFragment = fragment;
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (addToBackStack)transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
//        checkFragment(1);
    }


    private void checkFragment (int number){
        if (!(mCurrentFragment instanceof MainFragment)) mAdd.setVisibility(View.GONE);
    }


    @Subscribe
    public void getDetails(ReminderBean item){
        ReminderDetailsFragment reminderDetailsFragment = new ReminderDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ReminderDetailsFragment.TITLE,item.getTitle());
        bundle.putString(ReminderDetailsFragment.EX_DATE,item.getExDate());
        bundle.putString(ReminderDetailsFragment.NO_OF_TIMES,item.getNumberOfTimes());
        bundle.putString(ReminderDetailsFragment.IMAGE,item.getImagePath());
        reminderDetailsFragment.setArguments(bundle);
        changeFragment(reminderDetailsFragment, true);
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

    @Override
    public void onClick(View v) {
        changeFragment(new AddReminderFragment(),true);
    }
}
