package com.parse;

import bolts.Continuation;
import bolts.Task;
import java.util.Arrays;
import java.util.List;

class OfflineObjectStore<T extends ParseObject> implements ParseObjectStore<T> {
    private final String className;
    private final ParseObjectStore<T> legacy;
    private final String pinName;

    class C01763 implements Continuation<T, Task<T>> {
        C01763() {
        }

        public Task<T> then(Task<T> task) throws Exception {
            return ((ParseObject) task.getResult()) != null ? task : OfflineObjectStore.migrate(OfflineObjectStore.this.legacy, OfflineObjectStore.this).cast();
        }
    }

    class C01774 implements Continuation<List<T>, Task<T>> {
        C01774() {
        }

        public Task<T> then(Task<List<T>> task) throws Exception {
            List<T> results = (List) task.getResult();
            if (results == null) {
                return Task.forResult(null);
            }
            if (results.size() == 1) {
                return Task.forResult(results.get(0));
            }
            return ParseObject.unpinAllInBackground(OfflineObjectStore.this.pinName).cast();
        }
    }

    class C01785 implements Continuation<Integer, Task<Boolean>> {
        C01785() {
        }

        public Task<Boolean> then(Task<Integer> task) throws Exception {
            if (((Integer) task.getResult()).intValue() == 1) {
                return Task.forResult(Boolean.valueOf(true));
            }
            return OfflineObjectStore.this.legacy.existsAsync();
        }
    }

    private static <T extends ParseObject> Task<T> migrate(final ParseObjectStore<T> from, final ParseObjectStore<T> to) {
        return from.getAsync().onSuccessTask(new Continuation<T, Task<T>>() {
            public Task<T> then(Task<T> task) throws Exception {
                final ParseObject object = (ParseObject) task.getResult();
                if (object == null) {
                    return task;
                }
                return Task.whenAll(Arrays.asList(new Task[]{from.deleteAsync(), to.setAsync(object)})).continueWith(new Continuation<Void, T>() {
                    public T then(Task<Void> task) throws Exception {
                        return object;
                    }
                });
            }
        });
    }

    public OfflineObjectStore(Class<T> clazz, String pinName, ParseObjectStore<T> legacy) {
        this(ParseObject.getClassName(clazz), pinName, (ParseObjectStore) legacy);
    }

    public OfflineObjectStore(String className, String pinName, ParseObjectStore<T> legacy) {
        this.className = className;
        this.pinName = pinName;
        this.legacy = legacy;
    }

    public Task<Void> setAsync(final T object) {
        return ParseObject.unpinAllInBackground(this.pinName).continueWithTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                return object.pinInBackground(OfflineObjectStore.this.pinName, false);
            }
        });
    }

    public Task<T> getAsync() {
        return ParseQuery.getQuery(this.className).fromPin(this.pinName).ignoreACLs().findInBackground().onSuccessTask(new C01774()).onSuccessTask(new C01763());
    }

    public Task<Boolean> existsAsync() {
        return ParseQuery.getQuery(this.className).fromPin(this.pinName).ignoreACLs().countInBackground().onSuccessTask(new C01785());
    }

    public Task<Void> deleteAsync() {
        final Task<Void> ldsTask = ParseObject.unpinAllInBackground(this.pinName);
        return Task.whenAll(Arrays.asList(new Task[]{this.legacy.deleteAsync(), ldsTask})).continueWithTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                return ldsTask;
            }
        });
    }
}
