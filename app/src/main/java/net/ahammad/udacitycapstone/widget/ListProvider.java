package net.ahammad.udacitycapstone.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import net.ahammad.udacitycapstone.R;
import net.ahammad.udacitycapstone.util.ReminderBean;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Ala Hammad on 9/14/2015.
 */
public class ListProvider  implements RemoteViewsFactory  {
    private RealmResults<ReminderBean> mData;
    private Context context = null;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem(context);
    }

    public RealmResults<ReminderBean> getReminders(){
        // i must get new instance from realm
        // exception : Realm objects can only be accessed on the thread they were created.
        RealmResults<ReminderBean> list = Realm.getInstance(context).where(ReminderBean.class).findAll();
        return list;
    }


    private void populateListItem(Context context) {
       mData = getReminders();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        populateListItem(context);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /*
    *Similar to getView of Adapter where instead of View
    *we return RemoteViews
    *
    */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.reminder_row);
        ReminderBean reminderBean =getReminders().get(position);
        remoteView.setTextViewText(R.id.title, reminderBean.getTitle());
        remoteView.setTextViewText(R.id.ex_date,reminderBean.getExDate());
        remoteView.setTextViewText(R.id.numoftimes,context.getString(R.string.times,reminderBean.getNumberOfTimes()));

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}
