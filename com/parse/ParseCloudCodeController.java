package com.parse;

import bolts.Continuation;
import bolts.Task;
import java.util.Map;
import org.json.JSONObject;

class ParseCloudCodeController {
    final ParseHttpClient restClient;

    class C02331 implements Continuation<JSONObject, T> {
        C02331() {
        }

        public T then(Task<JSONObject> task) throws Exception {
            return ParseCloudCodeController.this.convertCloudResponse(task.getResult());
        }
    }

    public ParseCloudCodeController(ParseHttpClient restClient) {
        this.restClient = restClient;
    }

    public <T> Task<T> callFunctionInBackground(String name, Map<String, ?> params, String sessionToken) {
        return ParseRESTCloudCommand.callFunctionCommand(name, params, sessionToken).executeAsync(this.restClient).onSuccess(new C02331());
    }

    Object convertCloudResponse(Object result) {
        if (result instanceof JSONObject) {
            result = ((JSONObject) result).opt("result");
        }
        Object finalResult = ParseDecoder.get().decode(result);
        return finalResult != null ? finalResult : result;
    }
}
