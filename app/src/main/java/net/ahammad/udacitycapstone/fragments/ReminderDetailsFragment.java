package net.ahammad.udacitycapstone.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;

import net.ahammad.udacitycapstone.MainApp;
import net.ahammad.udacitycapstone.MapsActivity;
import net.ahammad.udacitycapstone.R;
import net.ahammad.udacitycapstone.util.Database;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alahammad on 9/4/15.
 */
public class ReminderDetailsFragment extends Fragment implements View.OnClickListener{

    public static final String TITLE = "title";
    public static final String EX_DATE = "ex_date";
    public static final String NO_OF_TIMES = "no_of_times";
    public static final String IMAGE= "image";
    public static final String LAT = "lat";
    public static final String LON = "lon";

    @Bind(R.id.title)
    TextView mTitle;

    @Bind(R.id.exDate)
    TextView mExDate;

    @Bind(R.id.noTimes)
    TextView mNoTimes;

    @Bind(R.id.delete)
    Button mDelete;

    @Bind(R.id.imageView)
    ImageView mPreview;

    @Bind(R.id.btn_show_map)
    Button mShowMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApp application = (MainApp) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Reminder details");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reminder_details_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments()!=null) {
            mTitle.setText(getArguments().getString(TITLE));
            mExDate.setText(getArguments().getString(EX_DATE));
            mNoTimes.setText(getArguments().getString(NO_OF_TIMES));
            displayLocation();
            String image = getArguments().getString(IMAGE);
            previewImage(image);
            mDelete.setOnClickListener(this);
        }
    }

    private void displayLocation (){
        DecimalFormat decimalFormat  =new DecimalFormat("#.#######");
        if (getArguments().getDouble(LAT,-1)!=-1 && getArguments().getDouble(LON,-1)!=-1 )
        mShowMap.setText(decimalFormat.format(getArguments().getDouble(LAT)) + "," + decimalFormat.format(getArguments().getDouble(LON)));
    }

    @OnClick(R.id.btn_show_map)
    public void showMap (View v){
        double lat = getArguments()!=null ? getArguments().getDouble(LAT):-1;
        double lon = getArguments()!=null ? getArguments().getDouble(LON):-1;
        if (lat!=-1 && lon!=-1) {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            intent.putExtra(ReminderDetailsFragment.LAT,lat);
            intent.putExtra(ReminderDetailsFragment.LON,lon);
            startActivity(intent);
        }
    }

    private void previewImage (String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        final Bitmap bitmap = BitmapFactory.decodeFile(path,
                options);

        mPreview.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        Database.getInstance(getActivity()).deleteReminder(getArguments().getString(TITLE),getArguments().getString(EX_DATE));
        Toast.makeText(getActivity(), R.string.deleted, Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
