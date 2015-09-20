package net.ahammad.udacitycapstone.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import net.ahammad.udacitycapstone.MainActivity;
import net.ahammad.udacitycapstone.R;

/**
 * Created by Ala Hammad on 9/20/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_DATA = "NOTIFICATION_DATA";

    private Context ctx;

    @Override
    public void onReceive(Context context, Intent intent) {
        ctx=context;
        createNotification(0, "I'm running");
    }


    private void createNotification(long when,String data){
        String notificationContent =ctx.getString(R.string.notification_content);
        String notificationTitle =ctx.getString(R.string.notification_title);

        //large icon for notification,normally use App icon
        Bitmap largeIcon = BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher);
        int smalIcon = R.mipmap.ic_launcher;
        String notificationData="This is data : "+data;

		/*create intent for show notification details when user clicks notification*/
        Intent intent =new Intent(ctx, MainActivity.class);
//        intent.putExtra(NOTIFICATION_DATA, notificationData);

		/*create unique this intent from  other intent using setData */
//        intent.setData(Uri.parse("content://" + when));
		/*create new task for each notification with pending intent so we set Intent.FLAG_ACTIVITY_NEW_TASK */
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);

		/*get the system service that manage notification NotificationManager*/
        NotificationManager notificationManager =(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

		/*build the notification*/
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                ctx)
//                .setWhen(when)
                .setContentText(notificationContent)
                .setContentTitle(notificationTitle)
                .setSmallIcon(smalIcon)
                .setAutoCancel(true)
                .setTicker(notificationTitle)
                .setLargeIcon(largeIcon)
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE| Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent);

		/*Create notification with builder*/
        Notification notification=notificationBuilder.build();

		/*sending notification to system.Here we use unique id (when)for making different each notification
		 * if we use same id,then first notification replace by the last notification*/
        notificationManager.notify((int) when, notification);
    }
}
