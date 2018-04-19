package com.parse;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import bolts.Continuation;
import bolts.Task;
import bolts.Task.TaskCompletionSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

class GcmRegistrar {
    private static final String ERROR_EXTRA = "error";
    private static final String FILENAME_DEVICE_TOKEN_LAST_MODIFIED = "deviceTokenLastModified";
    private static final String PARSE_SENDER_ID = "1076345567071";
    public static final String REGISTER_ACTION = "com.google.android.c2dm.intent.REGISTER";
    private static final String REGISTRATION_ID_EXTRA = "registration_id";
    private static final String SENDER_ID_EXTRA = "com.parse.push.gcm_sender_id";
    private static final String TAG = "com.parse.GcmRegistrar";
    private Context context = null;
    private long localDeviceTokenLastModified;
    private final Object localDeviceTokenLastModifiedMutex = new Object();
    private final Object lock = new Object();
    private Request request = null;

    class C00734 implements Callable<Void> {
        C00734() {
        }

        public Void call() throws Exception {
            synchronized (GcmRegistrar.this.localDeviceTokenLastModifiedMutex) {
                GcmRegistrar.this.localDeviceTokenLastModified = ManifestInfo.getLastModified();
                try {
                    ParseFileUtils.writeStringToFile(GcmRegistrar.getLocalDeviceTokenLastModifiedFile(), String.valueOf(GcmRegistrar.this.localDeviceTokenLastModified), "UTF-8");
                } catch (IOException e) {
                }
            }
            return null;
        }
    }

    class C00745 implements Callable<Long> {
        C00745() {
        }

        public Long call() throws Exception {
            Long valueOf;
            synchronized (GcmRegistrar.this.localDeviceTokenLastModifiedMutex) {
                if (GcmRegistrar.this.localDeviceTokenLastModified == 0) {
                    try {
                        GcmRegistrar.this.localDeviceTokenLastModified = Long.valueOf(ParseFileUtils.readFileToString(GcmRegistrar.getLocalDeviceTokenLastModifiedFile(), "UTF-8")).longValue();
                    } catch (IOException e) {
                        GcmRegistrar.this.localDeviceTokenLastModified = 0;
                    }
                }
                valueOf = Long.valueOf(GcmRegistrar.this.localDeviceTokenLastModified);
            }
            return valueOf;
        }
    }

    private static class Request {
        private static final int BACKOFF_INTERVAL_MS = 3000;
        private static final int MAX_RETRIES = 5;
        private static final String RETRY_ACTION = "com.parse.RetryGcmRegistration";
        private final PendingIntent appIntent = PendingIntent.getBroadcast(this.context, this.identifier, new Intent(), 0);
        private final Context context;
        private final int identifier = this.random.nextInt();
        private final Random random = new Random();
        private final PendingIntent retryIntent;
        private final BroadcastReceiver retryReceiver;
        private final String senderId;
        private final TaskCompletionSource tcs = Task.create();
        private final AtomicInteger tries = new AtomicInteger(0);

        class C00751 extends BroadcastReceiver {
            C00751() {
            }

            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getIntExtra("random", 0) == Request.this.identifier) {
                    Request.this.send();
                }
            }
        }

        public static Request createAndSend(Context context, String senderId) {
            Request request = new Request(context, senderId);
            request.send();
            return request;
        }

        private Request(Context context, String senderId) {
            this.context = context;
            this.senderId = senderId;
            String packageName = this.context.getPackageName();
            Intent intent = new Intent(RETRY_ACTION).setPackage(packageName);
            intent.addCategory(packageName);
            intent.putExtra("random", this.identifier);
            this.retryIntent = PendingIntent.getBroadcast(this.context, this.identifier, intent, 0);
            this.retryReceiver = new C00751();
            IntentFilter filter = new IntentFilter();
            filter.addAction(RETRY_ACTION);
            filter.addCategory(packageName);
            context.registerReceiver(this.retryReceiver, filter);
        }

        public Task<String> getTask() {
            return this.tcs.getTask();
        }

        private void send() {
            Intent intent = new Intent(GcmRegistrar.REGISTER_ACTION);
            intent.setPackage("com.google.android.gsf");
            intent.putExtra("sender", this.senderId);
            intent.putExtra("app", this.appIntent);
            ComponentName name = null;
            try {
                name = this.context.startService(intent);
            } catch (SecurityException e) {
            }
            if (name == null) {
                finish(null, "GSF_PACKAGE_NOT_AVAILABLE");
            }
            this.tries.incrementAndGet();
            PLog.m6v(GcmRegistrar.TAG, "Sending GCM registration intent");
        }

        public void onReceiveResponseIntent(Intent intent) {
            String registrationId = intent.getStringExtra(GcmRegistrar.REGISTRATION_ID_EXTRA);
            String error = intent.getStringExtra(GcmRegistrar.ERROR_EXTRA);
            if (registrationId == null && error == null) {
                PLog.m2e(GcmRegistrar.TAG, "Got no registration info in GCM onReceiveResponseIntent");
            } else if (!"SERVICE_NOT_AVAILABLE".equals(error) || this.tries.get() >= 5) {
                finish(registrationId, error);
            } else {
                ((AlarmManager) this.context.getSystemService("alarm")).set(2, SystemClock.elapsedRealtime() + ((long) (((1 << this.tries.get()) * BACKOFF_INTERVAL_MS) + this.random.nextInt(BACKOFF_INTERVAL_MS))), this.retryIntent);
            }
        }

        private void finish(String registrationId, String error) {
            boolean didSetResult;
            if (registrationId != null) {
                didSetResult = this.tcs.trySetResult(registrationId);
            } else {
                didSetResult = this.tcs.trySetError(new Exception("GCM registration error: " + error));
            }
            if (didSetResult) {
                this.appIntent.cancel();
                this.retryIntent.cancel();
                this.context.unregisterReceiver(this.retryReceiver);
            }
        }
    }

    private static class Singleton {
        public static final GcmRegistrar INSTANCE = new GcmRegistrar(Parse.getApplicationContext());

        private Singleton() {
        }
    }

    class C01582 implements Continuation<String, Void> {
        C01582() {
        }

        public Void then(Task<String> task) {
            Exception e = task.getError();
            if (e != null) {
                PLog.m3e(GcmRegistrar.TAG, "Got error when trying to register for GCM push", e);
            }
            synchronized (GcmRegistrar.this.lock) {
                GcmRegistrar.this.request = null;
            }
            return null;
        }
    }

    class C01593 implements Continuation<Long, Task<Boolean>> {
        C01593() {
        }

        public Task<Boolean> then(Task<Long> task) throws Exception {
            return Task.forResult(Boolean.valueOf(((Long) task.getResult()).longValue() != ManifestInfo.getLastModified()));
        }
    }

    public static GcmRegistrar getInstance() {
        return Singleton.INSTANCE;
    }

    private static String actualSenderIDFromExtra(Object senderIDExtra) {
        if (!(senderIDExtra instanceof String)) {
            return null;
        }
        String senderID = (String) senderIDExtra;
        if (senderID.startsWith("id:")) {
            return senderID.substring(3);
        }
        return null;
    }

    GcmRegistrar(Context context) {
        this.context = context;
    }

    public Task<Void> registerAsync() {
        if (ManifestInfo.getPushType() != PushType.GCM) {
            return Task.forResult(null);
        }
        Task<Void> onSuccessTask;
        synchronized (this.lock) {
            final ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            onSuccessTask = (installation.getDeviceToken() == null ? Task.forResult(Boolean.valueOf(true)) : isLocalDeviceTokenStaleAsync()).onSuccessTask(new Continuation<Boolean, Task<Void>>() {
                public Task<Void> then(Task<Boolean> task) throws Exception {
                    if (!((Boolean) task.getResult()).booleanValue()) {
                        return Task.forResult(null);
                    }
                    Task<Void> saveTask;
                    if (installation.getPushType() != PushType.GCM) {
                        installation.setPushType(PushType.GCM);
                        saveTask = installation.saveInBackground();
                    } else {
                        saveTask = Task.forResult(null);
                    }
                    GcmRegistrar.this.sendRegistrationRequestAsync();
                    return saveTask;
                }
            });
        }
        return onSuccessTask;
    }

    private Task<Void> sendRegistrationRequestAsync() {
        Task<Void> forResult;
        synchronized (this.lock) {
            if (this.request != null) {
                forResult = Task.forResult(null);
            } else {
                Bundle metaData = ManifestInfo.getApplicationMetadata(this.context);
                String senderIDs = PARSE_SENDER_ID;
                if (metaData != null) {
                    Object senderIDExtra = metaData.get(SENDER_ID_EXTRA);
                    if (senderIDExtra != null) {
                        String senderID = actualSenderIDFromExtra(senderIDExtra);
                        if (senderID != null) {
                            senderIDs = senderIDs + "," + senderID;
                        } else {
                            PLog.m2e(TAG, "Found com.parse.push.gcm_sender_id <meta-data> element with value \"" + senderIDExtra.toString() + "\", but the value is missing the expected \"id:\" " + "prefix.");
                        }
                    }
                }
                this.request = Request.createAndSend(this.context, senderIDs);
                forResult = this.request.getTask().continueWith(new C01582());
            }
        }
        return forResult;
    }

    public Task<Void> handleRegistrationIntentAsync(Intent intent) {
        List<Task<Void>> tasks = new ArrayList();
        String registrationId = intent.getStringExtra(REGISTRATION_ID_EXTRA);
        if (registrationId != null && registrationId.length() > 0) {
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            if (!registrationId.equals(installation.getDeviceToken())) {
                installation.setPushType(PushType.GCM);
                installation.setDeviceToken(registrationId);
                tasks.add(installation.saveInBackground());
            }
            tasks.add(updateLocalDeviceTokenLastModifiedAsync());
        }
        synchronized (this.lock) {
            if (this.request != null) {
                this.request.onReceiveResponseIntent(intent);
            }
        }
        return Task.whenAll(tasks);
    }

    int getRequestIdentifier() {
        int access$300;
        synchronized (this.lock) {
            access$300 = this.request != null ? this.request.identifier : 0;
        }
        return access$300;
    }

    Task<Boolean> isLocalDeviceTokenStaleAsync() {
        return getLocalDeviceTokenLastModifiedAsync().onSuccessTask(new C01593());
    }

    Task<Void> updateLocalDeviceTokenLastModifiedAsync() {
        return Task.call(new C00734(), Task.BACKGROUND_EXECUTOR);
    }

    private Task<Long> getLocalDeviceTokenLastModifiedAsync() {
        return Task.call(new C00745(), Task.BACKGROUND_EXECUTOR);
    }

    static File getLocalDeviceTokenLastModifiedFile() {
        return new File(Parse.getParseCacheDir("GCMRegistrar"), FILENAME_DEVICE_TOKEN_LAST_MODIFIED);
    }

    static void deleteLocalDeviceTokenLastModifiedFile() {
        ParseFileUtils.deleteQuietly(getLocalDeviceTokenLastModifiedFile());
    }
}
