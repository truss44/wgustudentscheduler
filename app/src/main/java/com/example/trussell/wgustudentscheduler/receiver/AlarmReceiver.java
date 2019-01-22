package com.example.trussell.wgustudentscheduler.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.trussell.wgustudentscheduler.R;
import com.example.trussell.wgustudentscheduler.TermActivity;

public class AlarmReceiver extends BroadcastReceiver {

    private String CHANNEL_ID = "app_notification_channel";
    private String CHANNEL_DESC = "Student Application Notification Channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String mNotificationTitle = intent.getStringExtra("mNotificationTitle");
        String mNotificationContent = intent.getStringExtra("mNotificationContent");
        int mNotificationId = Integer.parseInt(intent.getStringExtra("mNotificationId"));

        Intent resultIntent = new Intent(context, TermActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(TermActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        mNotificationId,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID);

        mBuilder.setSmallIcon(R.drawable.ic_notification_default);
        mBuilder.setContentTitle(mNotificationTitle);
        mBuilder.setContentText(mNotificationContent);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_DESC, NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(mNotificationId, mBuilder.build());
    }
}
