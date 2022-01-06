package com.huong.bpnhmnh.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.huong.bpnhmnh.MainActivity;
import com.huong.bpnhmnh.R;
import com.huong.bpnhmnh.utils.Perf;

import java.util.Random;



public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        pushBedTimeReminder(context);
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        assert drawable != null;
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public void pushBedTimeReminder(Context context) {
        long previousTime = Perf.getLong("lastTimeDailyNoti", context, 0);
        if (System.currentTimeMillis() - previousTime < 12 * 60 * 60 * 1000) {//12 hours
            return;
        }
        Perf.setLong("lastTimeDailyNoti", System.currentTimeMillis(), context);

        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        int flag = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flag = PendingIntent.FLAG_IMMUTABLE;

        }
        Random random = new Random();
        boolean randomTitle = random.nextBoolean();
        String title = "Bếp nhà mình";
        String message = "Cùng học nấu ăn với Bếp nhà mình nào";

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, flag);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alone_reminder")
                .setSmallIcon(R.mipmap.logo)
                .setLargeIcon(getBitmapFromVectorDrawable(context, R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setDefaults(Notification.DEFAULT_ALL);

        createChanel(mNotifyMgr);

        Notification noti = builder.build();
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        mNotifyMgr.notify(111, noti);
    }

    private void createChanel(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("alone_reminder", "Reminder", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
    }
}
