package com.parse;

import bolts.Continuation;
import bolts.Task;
import org.json.JSONObject;

class NetworkSessionController implements ParseSessionController {
    private final ParseHttpClient client;
    private final ParseObjectCoder coder = ParseObjectCoder.get();

    class C01661 implements Continuation<JSONObject, State> {
        C01661() {
        }

        public State then(Task<JSONObject> task) throws Exception {
            return ((Builder) ((Builder) NetworkSessionController.this.coder.decode(new Builder("_Session"), (JSONObject) task.getResult(), ParseDecoder.get())).isComplete(true)).build();
        }
    }

    class C01672 implements Continuation<JSONObject, State> {
        C01672() {
        }

        public State then(Task<JSONObject> task) throws Exception {
            return ((Builder) ((Builder) NetworkSessionController.this.coder.decode(new Builder("_Session"), (JSONObject) task.getResult(), ParseDecoder.get())).isComplete(true)).build();
        }
    }

    public NetworkSessionController(ParseHttpClient client) {
        this.client = client;
    }

    public Task<State> getSessionAsync(String sessionToken) {
        return ParseRESTSessionCommand.getCurrentSessionCommand(sessionToken).executeAsync(this.client).onSuccess(new C01661());
    }

    public Task<Void> revokeAsync(String sessionToken) {
        return ParseRESTSessionCommand.revoke(sessionToken).executeAsync(this.client).makeVoid();
    }

    public Task<State> upgradeToRevocable(String sessionToken) {
        return ParseRESTSessionCommand.upgradeToRevocableSessionCommand(sessionToken).executeAsync(this.client).onSuccess(new C01672());
    }
}
