package com.parse;

import bolts.Continuation;
import bolts.Task;
import java.util.Arrays;
import java.util.Map;

class CachedCurrentUserController implements ParseCurrentUserController {
    ParseUser currentUser;
    boolean currentUserMatchesDisk = false;
    private final Object mutex = new Object();
    private final ParseObjectStore<ParseUser> store;
    private final TaskQueue taskQueue = new TaskQueue();

    class C01452 implements Continuation<Void, Task<Boolean>> {

        class C01441 implements Continuation<Void, Task<Boolean>> {
            C01441() {
            }

            public Task<Boolean> then(Task<Void> task) throws Exception {
                return CachedCurrentUserController.this.store.existsAsync();
            }
        }

        C01452() {
        }

        public Task<Boolean> then(Task<Void> toAwait) throws Exception {
            return toAwait.continueWithTask(new C01441());
        }
    }

    class C01463 implements Continuation<ParseUser, String> {
        C01463() {
        }

        public String then(Task<ParseUser> task) throws Exception {
            ParseUser user = (ParseUser) task.getResult();
            return user != null ? user.getSessionToken() : null;
        }
    }

    class C01504 implements Continuation<Void, Task<Void>> {
        C01504() {
        }

        public Task<Void> then(Task<Void> toAwait) throws Exception {
            final Task<ParseUser> userTask = CachedCurrentUserController.this.getAsync(false);
            return Task.whenAll(Arrays.asList(new Task[]{userTask, toAwait})).continueWithTask(new Continuation<Void, Task<Void>>() {

                class C01471 implements Continuation<ParseUser, Task<Void>> {
                    C01471() {
                    }

                    public Task<Void> then(Task<ParseUser> task) throws Exception {
                        ParseUser user = (ParseUser) task.getResult();
                        if (user == null) {
                            return task.cast();
                        }
                        return user.logOutAsync();
                    }
                }

                class C01482 implements Continuation<Void, Void> {
                    C01482() {
                    }

                    public Void then(Task<Void> task) throws Exception {
                        boolean deleted = !task.isFaulted();
                        synchronized (CachedCurrentUserController.this.mutex) {
                            CachedCurrentUserController.this.currentUserMatchesDisk = deleted;
                            CachedCurrentUserController.this.currentUser = null;
                        }
                        return null;
                    }
                }

                public Task<Void> then(Task<Void> task) throws Exception {
                    Task<Void> logOutTask = userTask.onSuccessTask(new C01471());
                    Task<Void> diskTask = CachedCurrentUserController.this.store.deleteAsync().continueWith(new C01482());
                    return Task.whenAll(Arrays.asList(new Task[]{logOutTask, diskTask}));
                }
            });
        }
    }

    public CachedCurrentUserController(ParseObjectStore<ParseUser> store) {
        this.store = store;
    }

    public Task<Void> setAsync(final ParseUser user) {
        return this.taskQueue.enqueue(new Continuation<Void, Task<Void>>() {

            class C01421 implements Continuation<Void, Task<Void>> {

                class C01411 implements Continuation<Void, Void> {
                    C01411() {
                    }

                    public Void then(Task<Void> task) throws Exception {
                        synchronized (CachedCurrentUserController.this.mutex) {
                            CachedCurrentUserController.this.currentUserMatchesDisk = !task.isFaulted();
                            CachedCurrentUserController.this.currentUser = user;
                        }
                        return null;
                    }
                }

                C01421() {
                }

                public Task<Void> then(Task<Void> task) throws Exception {
                    synchronized (CachedCurrentUserController.this.mutex) {
                        ParseUser oldCurrentUser = CachedCurrentUserController.this.currentUser;
                    }
                    if (!(oldCurrentUser == null || oldCurrentUser == user)) {
                        oldCurrentUser.logOutInternal();
                    }
                    synchronized (user.mutex) {
                        user.setIsCurrentUser(true);
                        user.synchronizeAllAuthData();
                    }
                    return CachedCurrentUserController.this.store.setAsync(user).continueWith(new C01411());
                }
            }

            public Task<Void> then(Task<Void> toAwait) throws Exception {
                return toAwait.continueWithTask(new C01421());
            }
        });
    }

    public Task<Void> setIfNeededAsync(ParseUser user) {
        synchronized (this.mutex) {
            if (!user.isCurrentUser() || this.currentUserMatchesDisk) {
                Task<Void> forResult = Task.forResult(null);
                return forResult;
            }
            return setAsync(user);
        }
    }

    public Task<ParseUser> getAsync() {
        return getAsync(ParseUser.isAutomaticUserEnabled());
    }

    public Task<Boolean> existsAsync() {
        synchronized (this.mutex) {
            if (this.currentUser != null) {
                Task<Boolean> forResult = Task.forResult(Boolean.valueOf(true));
                return forResult;
            }
            return this.taskQueue.enqueue(new C01452());
        }
    }

    public boolean isCurrent(ParseUser user) {
        boolean z;
        synchronized (this.mutex) {
            z = this.currentUser == user;
        }
        return z;
    }

    public void clearFromMemory() {
        synchronized (this.mutex) {
            this.currentUser = null;
            this.currentUserMatchesDisk = false;
        }
    }

    public void clearFromDisk() {
        synchronized (this.mutex) {
            this.currentUser = null;
            this.currentUserMatchesDisk = false;
        }
        try {
            ParseTaskUtils.wait(this.store.deleteAsync());
        } catch (ParseException e) {
        }
    }

    public Task<String> getCurrentSessionTokenAsync() {
        return getAsync(false).onSuccess(new C01463());
    }

    public Task<Void> logoutAsync() {
        return this.taskQueue.enqueue(new C01504());
    }

    public Task<ParseUser> getAsync(final boolean shouldAutoCreateUser) {
        synchronized (this.mutex) {
            if (this.currentUser != null) {
                Task<ParseUser> forResult = Task.forResult(this.currentUser);
                return forResult;
            }
            return this.taskQueue.enqueue(new Continuation<Void, Task<ParseUser>>() {

                class C01521 implements Continuation<Void, Task<ParseUser>> {

                    class C01511 implements Continuation<ParseUser, ParseUser> {
                        C01511() {
                        }

                        public ParseUser then(Task<ParseUser> task) throws Exception {
                            boolean matchesDisk;
                            boolean z = true;
                            ParseUser current = (ParseUser) task.getResult();
                            if (task.isFaulted()) {
                                matchesDisk = false;
                            } else {
                                matchesDisk = true;
                            }
                            synchronized (CachedCurrentUserController.this.mutex) {
                                CachedCurrentUserController.this.currentUser = current;
                                CachedCurrentUserController.this.currentUserMatchesDisk = matchesDisk;
                            }
                            if (current != null) {
                                synchronized (current.mutex) {
                                    current.setIsCurrentUser(true);
                                    if (!(current.getObjectId() == null && ParseAnonymousUtils.isLinked(current))) {
                                        z = false;
                                    }
                                    current.isLazy = z;
                                }
                                return current;
                            } else if (shouldAutoCreateUser) {
                                return CachedCurrentUserController.this.lazyLogIn();
                            } else {
                                return null;
                            }
                        }
                    }

                    C01521() {
                    }

                    public Task<ParseUser> then(Task<Void> task) throws Exception {
                        synchronized (CachedCurrentUserController.this.mutex) {
                            ParseUser current = CachedCurrentUserController.this.currentUser;
                            boolean matchesDisk = CachedCurrentUserController.this.currentUserMatchesDisk;
                        }
                        if (current != null) {
                            return Task.forResult(current);
                        }
                        if (!matchesDisk) {
                            return CachedCurrentUserController.this.store.getAsync().continueWith(new C01511());
                        }
                        if (shouldAutoCreateUser) {
                            return Task.forResult(CachedCurrentUserController.this.lazyLogIn());
                        }
                        return null;
                    }
                }

                public Task<ParseUser> then(Task<Void> toAwait) throws Exception {
                    return toAwait.continueWithTask(new C01521());
                }
            });
        }
    }

    private ParseUser lazyLogIn() {
        AnonymousAuthenticationProvider provider = ParseAnonymousUtils.getProvider();
        return lazyLogIn(provider.getAuthType(), provider.getAuthData());
    }

    ParseUser lazyLogIn(String authType, Map<String, String> authData) {
        ParseUser user = (ParseUser) ParseObject.create(ParseUser.class);
        synchronized (user.mutex) {
            user.setIsCurrentUser(true);
            user.isLazy = true;
            user.putAuthData(authType, authData);
        }
        synchronized (this.mutex) {
            this.currentUserMatchesDisk = false;
            this.currentUser = user;
        }
        return user;
    }
}
