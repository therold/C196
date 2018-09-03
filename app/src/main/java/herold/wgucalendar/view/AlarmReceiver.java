package herold.wgucalendar.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    private final int NOTIFICATION_ID = 1004;
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("Title");
        NotificationManager notificationMgr = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(android.R.drawable.star_on)
                .setAutoCancel(false)
                .build();
        notification.defaults|= Notification.DEFAULT_SOUND;
        notification.defaults|= Notification.DEFAULT_LIGHTS;
        notification.defaults|= Notification.DEFAULT_VIBRATE;
        notification.flags |= Notification.FLAG_INSISTENT;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationMgr.notify(NOTIFICATION_ID, notification);
    }
}
