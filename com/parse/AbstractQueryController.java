package com.parse;

import bolts.Continuation;
import bolts.Task;
import java.util.List;

abstract class AbstractQueryController implements ParseQueryController {

    class C01281 implements Continuation<List<T>, T> {
        C01281() {
        }

        public T then(Task<List<T>> task) throws Exception {
            if (task.isFaulted()) {
                throw task.getError();
            } else if (task.getResult() != null && ((List) task.getResult()).size() > 0) {
                return (ParseObject) ((List) task.getResult()).get(0);
            } else {
                throw new ParseException(ParseException.OBJECT_NOT_FOUND, "no results found for query");
            }
        }
    }

    AbstractQueryController() {
    }

    public <T extends ParseObject> Task<T> getFirstAsync(State<T> state, ParseUser user, Task<Void> cancellationToken) {
        return findAsync(state, user, cancellationToken).continueWith(new C01281());
    }
}
