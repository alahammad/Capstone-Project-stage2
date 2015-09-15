package net.ahammad.udacitycapstone.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;

import net.ahammad.udacitycapstone.MainApp;
import net.ahammad.udacitycapstone.MapsActivity;
import net.ahammad.udacitycapstone.R;
import net.ahammad.udacitycapstone.util.Database;
import net.ahammad.udacitycapstone.util.ReminderBean;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alahammad on 9/4/15.
 */
public class AddReminderFragment extends Fragment implements View.OnClickListener{
    public static final int MAP_REQUEST = 200;
    // camera
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "MedicineReminder";

    @Bind(R.id.title)
    EditText mTitle;

    @Bind(R.id.exDate)
    Button mExDate;

    @Bind(R.id.noTimes)
    EditText mNoOfTimes;

    @Bind(R.id.add)
    Button mAdd;

    @Bind(R.id.add_photo)
    Button mAddPhoto;

    @Bind(R.id.imageView)
    ImageView mPreview;

    @Bind(R.id.btn_map)
    Button mMap;

    private DatePickerDialog mDatePicker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApp application = (MainApp) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Add Reminder");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.add_reminder_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdd.setOnClickListener(this);
        mAddPhoto.setOnClickListener(this);
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getActivity(),
                    R.string.camera_error,
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
        }
        mExDate.setOnClickListener(this);

        setDatePicker();
    }

    @Override
    public void onResume() {
        super.onResume();
        double lat = MainApp.lat;
        double lon = MainApp.lon;
        if (lat!=0 && lon!=0) {
            DecimalFormat decimalFormat  =new DecimalFormat("#.#######");
            mMap.setText(decimalFormat.format(lat) + "," + decimalFormat.format(lon));
        }
        Log.d("Hammad", "Lat " + lat);
    }

    private boolean isDeviceSupportCamera() {
        if (getActivity().getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @OnClick(R.id.btn_map)
    public void showMap(View view){
        Intent intent =new Intent(getActivity(), MapsActivity.class);
        startActivityForResult(intent, MAP_REQUEST);
    }


    @Override
    public void onClick(View v) {
        if (v==mAdd) {
            if (checkEmpty(mTitle) && !TextUtils.isEmpty(mExDate.getText().toString()) && checkEmpty(mNoOfTimes)) {
                String imagePath = fileUri!=null ? fileUri.getPath() :"";
                ReminderBean bean = new ReminderBean( mTitle.getText().toString(), mExDate.getText().toString(),mNoOfTimes.getText().toString(),imagePath,MainApp.lat,MainApp.lon);
                Database.getInstance(getActivity()).insertReminder(bean);
                Toast.makeText(getActivity(), R.string.add_success, Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }else{
                Toast.makeText(getActivity(),R.string.empty,Toast.LENGTH_LONG).show();

            }
        }else if (v==mExDate){
            mDatePicker.show();
        }
        else{ // camera
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // successfully captured the image
                // display it in image view

                previewCapturedImage();
            }
        }
    }

    private void previewCapturedImage(){

        try {
            // hide video preview
            mPreview.setVisibility(View.VISIBLE);
            mAddPhoto.setVisibility(View.GONE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            mPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean checkEmpty (EditText editText){
        if (!TextUtils.isEmpty(editText.getText())) return true;
        return false;
    }

    // camera
    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }else {
            return null;
        }

        return mediaFile;
    }

    // date picker

    private void setDatePicker(){
        Calendar newCalendar = Calendar.getInstance();
        final  DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
       mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mExDate.setText(dateFormat.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

}
