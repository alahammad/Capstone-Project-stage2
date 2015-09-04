package net.ahammad.udacitycapstone.fragments;

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


import net.ahammad.udacitycapstone.R;
import net.ahammad.udacitycapstone.util.Database;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alahammad on 9/4/15.
 */
public class ReminderDetailsFragment extends Fragment implements View.OnClickListener{

    public static final String TITLE = "title";
    public static final String EX_DATE = "ex_date";
    public static final String NO_OF_TIMES = "no_of_times";
    public static final String IMAGE= "image";


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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reminder_details_fragment,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments()!=null) {
            mTitle.setText(getArguments().getString(TITLE));
            mExDate.setText(getArguments().getString(EX_DATE));
            mNoTimes.setText(getArguments().getString(NO_OF_TIMES));
            String image = getArguments().getString(IMAGE);
            previewImage(image);
            mDelete.setOnClickListener(this);
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
