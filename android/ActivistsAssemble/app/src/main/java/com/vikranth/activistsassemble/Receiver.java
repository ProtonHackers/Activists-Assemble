package com.vikranth.activistsassemble;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
/**
 * Created by vikranth on 1/27/2017.
 */
public class Receiver extends BroadcastReceiver {
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;
    private static String ACTION_DISMISS_NOTIFICATION = "dismiss";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
//        NotificationItem model = (NotificationItem) extras.getSerializable("notification");
//        if (model == null) return;
//        Time item = model.getModel();
//        boolean isNotification =
//                PreferenceUtils.getString(context, "plan")
//                        .equalsIgnoreCase(item.name);
        if (true) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            Intent intent1 = new Intent(context, ListDateActivity.class);
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//            stackBuilder.addParentStack(ListDateActivity.class);
//            stackBuilder.addNextIntent(intent1);
//            PendingIntent alarmIntent = stackBuilder.getPendingIntent(model.getId() + 1, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle("Protest Occuring")
                    .setContentText("Sign Up")
//                    .setContentText(sessionDetails + "\n")
                    .setSmallIcon(R.drawable.calendar_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.calendar_icon))
//                    .addAction(ignoreReminderAction(context))
                    .build();

            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(0, notification);
        }
    }

//    private static Action ignoreReminderAction(Context context) {
//        Intent ignoreReminderIntent = new Intent(context, ListDateActivity.class);
//        ignoreReminderIntent.setAction(ACTION_DISMISS_NOTIFICATION);
//        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
//                context,
//                ACTION_IGNORE_PENDING_INTENT_ID,
//                ignoreReminderIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        Action ignoreReminderAction = new Action(R.drawable.ic_cancel_black_24px,
//                "No, thanks.",
//                ignoreReminderPendingIntent);
//        return ignoreReminderAction;
//    }
}
