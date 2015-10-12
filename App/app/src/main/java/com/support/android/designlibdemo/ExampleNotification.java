package com.support.android.designlibdemo;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;

public class ExampleNotification {
        /**
         * A numeric value that identifies the notification that we'll be sending.
         * This value needs to be unique within this app, but it doesn't need to be
         * unique system-wide.
         */
        public static final int NOTIFICATION_ID = 1;
        private static final String NOMBRE_APP = "Red De Mascotas";
        private String texto;
        private Resources resources;
        private NotificationManager notificationManager;
        private Activity activity;

        public ExampleNotification(Resources resources, NotificationManager notificationManager, Activity activity, String texto){
            this.resources = resources;
            this.notificationManager = notificationManager; //TODO: (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            this.activity = activity;
            this.texto = texto;
        }

        /**
         * Send a sample notification using the NotificationCompat API.
         */
        public void sendNotification() {

            // BEGIN_INCLUDE(build_action)
            /** Create an intent that will be fired when the user clicks the notification.
             * The intent needs to be packaged into a {@link android.app.PendingIntent} so that the
             * notification service can fire it on our behalf.
             */

//            Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://developer.android.com/reference/android/app/Notification.html"));
            PendingIntent pendingIntent = PendingIntent.getActivity(this.activity, 0, intent, 0);
            // END_INCLUDE(build_action)

            // BEGIN_INCLUDE (build_notification)
            /**
             * Use NotificationCompat.Builder to set up our notification.
             */
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this.activity);

            /** Set the icon that will appear in the notification bar. This icon also appears
             * in the lower right hand corner of the notification itself.
             */
            builder.setSmallIcon(R.drawable.footprint3);

            // Set the intent that will fire when the user taps the notification.
            builder.setContentIntent(pendingIntent);

            // Set the notification to auto-cancel. This means that the notification will disappear
            // after the user taps it, rather than remaining until it's explicitly dismissed.
            builder.setAutoCancel(true);

            /**
             *Build the notification's appearance.
             * Set the large icon, which appears on the left of the notification. In this
             * sample we'll set the large icon to be the same as our app icon. The app icon is a
             * reasonable default if you don't have anything more compelling to use as an icon.
             */
            builder.setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.footprint1));

            /**
             * Set the text of the notification. This sample sets the three most commononly used
             * text areas:
             * 1. The content title, which appears in large type at the top of the notification
             * 2. The content text, which appears in smaller text below the title
             * 3. The subtext, which appears under the text on newer devices. Devices running
             *    versions of Android prior to 4.2 will ignore this field, so don't use it for
             *    anything vital!
             */
            builder.setContentTitle(NOMBRE_APP);
            builder.setContentText("Hay nuevas publicaciones similares a tu búsqueda");
            builder.setSubText("");

            // END_INCLUDE (build_notification)

            // BEGIN_INCLUDE(send_notification)
            /**
             * Send the notification. This will immediately display the notification icon in the
             * notification bar.
             */
            this.notificationManager.notify(NOTIFICATION_ID, builder.build());
            // END_INCLUDE(send_notification)
        }
    }
