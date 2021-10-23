package com.helloworld.goodpoint.ui.myService;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.JsonObject;
import com.helloworld.goodpoint.App;
import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.pojo.NotificationItem;
import com.helloworld.goodpoint.pojo.User;
import com.helloworld.goodpoint.retrofit.ApiClient;
import com.helloworld.goodpoint.retrofit.ApiInterface;
import com.helloworld.goodpoint.retrofit.Decode;
import com.helloworld.goodpoint.ui.NotificationActivity;
import com.helloworld.goodpoint.ui.PrefManager;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationBroadcast extends BroadcastReceiver {

    String user_id;

    //@SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        String refresh = new PrefManager(context).isLoginned();
        Log.e("TAG", "onReceive: Token: "+refresh);
        newNotification(context);
        if(refresh.isEmpty()) {
            if(User.getUser() != null && User.getUser().getId() != null && !User.getUser().getId().isEmpty())
                user_id = User.getUser().getId();
            else return;
        }else{
            try {
                String bodyToken = Decode.decoded(refresh);
                JSONObject jsonObject = new JSONObject(bodyToken);
                user_id = jsonObject.getString("user_id");
            } catch (Exception e) {
                Log.e("TAG", "onReceive: "+e.getMessage());
            }
        }

        ApiInterface apiInterface = ApiClient.getApiClient(new PrefManager(context).getNGROKLink()).create(ApiInterface.class);
        Call<List<NotificationItem>> call = apiInterface.getNewNotification(user_id);
        call.enqueue(new Callback<List<NotificationItem>>() {
            @Override
            public void onResponse(Call<List<NotificationItem>> call, Response<List<NotificationItem>> response) {
                if(response.body()==null || response.body().isEmpty())
                    return;
                for(NotificationItem item: response.body()){
                    showNotification(context,item.getId(),item.getTitle(),item.getDescription(),item.getType());
                    Call<JsonObject> sentCall = apiInterface.updateSent(item.getId(),true);
                    sentCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Log.e("TAG", "onResponse: Success");
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.e("TAG", "onFailure: "+t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<NotificationItem>> call, Throwable t) {
                Log.e("TAG", "onFailure: "+t.getMessage());
            }
        });
    }

    private void newNotification(Context context) {
        Intent i = new Intent(App.getInstance(),NotificationBroadcast.class);
        i.setAction("com.helloworld.goodpoint.ui.myService.NotificationBroadcast");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(App.getInstance(),10,i,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(System.currentTimeMillis()+20000,pendingIntent),pendingIntent);
    }

    private void createNotification(Context context, String id, String title, String description, int type) {
        Log.d("Good Point Service", "createNotification: "+id);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(context, NotificationActivity.class);
        PendingIntent pintent = PendingIntent.getActivity(context,type,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel nc = new NotificationChannel("Good Point",title, NotificationManager.IMPORTANCE_HIGH);
            nc.setDescription(description);
            nm.createNotificationChannel(nc);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"Good Point");
            builder.setContentTitle(title).setContentText(description);
            builder.setSmallIcon(R.drawable.application_icon2).setStyle(new NotificationCompat.BigTextStyle().bigText(description));
            builder.setContentIntent(pintent).setAutoCancel(true);
            NotificationManagerCompat nmc = NotificationManagerCompat.from(context);
            nmc.notify(Integer.parseInt(id),builder.build());
        }else{
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle(title).setContentText(description);
            builder.setSmallIcon(R.drawable.application_icon2).setStyle(new Notification.BigTextStyle().bigText(description));
            builder.setContentIntent(pintent);
            nm.notify(Integer.parseInt(id),builder.build());
        }

    }

    private void showNotification(Context context, String id, String title, String description, int type) {
        Log.e("TAG", "showNotification: "+id);
        Log.e("TAG", "showNotification: "+title);
        Log.e("TAG", "showNotification: "+description);
        Log.e("TAG", "showNotification: "+type);
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wl.acquire(10);

        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Uri ringtoneUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.consequence);
        Ringtone r = RingtoneManager.getRingtone(context, ringtoneUri);
        r.play();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "Good Point");
        NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        context.grantUriPermission("com.android.systemui", ringtoneUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent notifyIntent = new Intent(context, NotificationActivity.class);
        notifyIntent.putExtra("ID",id);
        PendingIntent pintent = PendingIntent.getActivity(context,type,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            NotificationChannel mChannel = new NotificationChannel("Good Point", title, NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setSound(ringtoneUri, att);
            mChannel.setBypassDnd(true);
            mChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            mChannel.setShowBadge(true);

            if (mNotifyManager != null) {
                mNotifyManager.createNotificationChannel(mChannel);
            }

            notificationBuilder
                    .setSmallIcon(R.drawable.application_icon2)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setCategory(NotificationCompat.CATEGORY_EVENT)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(description))
                    .setSound(ringtoneUri)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setContentIntent(pintent)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        }else {
            notificationBuilder.setContentTitle(title)
                    .setSmallIcon(R.drawable.application_icon2)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setCategory(NotificationCompat.CATEGORY_EVENT)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(description))
                    .setSound(ringtoneUri)
                    .setAutoCancel(true)
                    .setContentIntent(pintent)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        }

        if (mNotifyManager != null) {
            mNotifyManager.notify(Integer.parseInt(id), notificationBuilder.build());
            Log.e("TAG", "showNotification: Success");
        }
        else
            Log.e("TAG", "showNotification: Fail");
    }

}