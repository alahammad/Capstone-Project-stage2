package net.ahammad.udacitycapstone.fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.ahammad.udacitycapstone.R;
import net.ahammad.udacitycapstone.util.BusProvider;
import net.ahammad.udacitycapstone.util.Database;
import net.ahammad.udacitycapstone.util.ReminderBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    @Bind(R.id.empty_data)
    TextView mEmptyData;

    @Bind(R.id.view)
    RecyclerView mRecycleView;

    private  ReminderAdapter mAdapter;

    private RealmResults<ReminderBean> mData;

    public MainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mData = Database.getInstance(getActivity()).getReminders();
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(llm);
        mAdapter = new ReminderAdapter();
        mRecycleView.setAdapter(mAdapter);
        showEmptyView();
    }



    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int itemPosition = mRecycleView.getChildLayoutPosition(v);
            BusProvider.getInstance().post(mData.get(itemPosition));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mData = Database.getInstance(getActivity()).getReminders();
        mAdapter.notifyDataSetChanged();
    }


    private void showEmptyView (){
        int visibility = mAdapter.getItemCount() ==0 ? View.VISIBLE : View.GONE;
        mEmptyData.setVisibility(visibility);
    }

    class ReminderAdapter extends RecyclerView.Adapter<ReminderHolder>{



        @Override
        public ReminderHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reminder_row, viewGroup, false);
            v.setOnClickListener(new MyOnClickListener());
            ReminderHolder reminderHolder = new ReminderHolder(v);
            return reminderHolder;
        }

        @Override
        public void onBindViewHolder(ReminderHolder holder, int position) {
            int color = (position%2==0) ? Color.parseColor("#009688") : Color.parseColor("#80CBC4");
            holder.bg.setBackgroundColor(color);
            holder.title.setText(mData.get(position).getTitle());
            holder.exDate.setText(mData.get(position).getExDate());
            holder.numberoftimes.setText(mData.get(position).getNumberOfTimes());
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }


    }





    class ReminderHolder extends RecyclerView.ViewHolder{
        TextView title,numberoftimes,exDate;
        RelativeLayout bg;
        public ReminderHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            numberoftimes = (TextView)itemView.findViewById(R.id.numoftimes);
            exDate = (TextView)itemView.findViewById(R.id.ex_date);
            bg= (RelativeLayout)itemView.findViewById(R.id.reminder_bg);
        }
    }
}
