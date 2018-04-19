package com.parse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ParseFieldOperation */
final class ParseFieldOperations {
    private static Map<String, ParseFieldOperationFactory> opDecoderMap = new HashMap();

    /* compiled from: ParseFieldOperation */
    private interface ParseFieldOperationFactory {
        ParseFieldOperation decode(JSONObject jSONObject, ParseDecoder parseDecoder) throws JSONException;
    }

    /* compiled from: ParseFieldOperation */
    static class C02421 implements ParseFieldOperationFactory {
        C02421() {
        }

        public ParseFieldOperation decode(JSONObject object, ParseDecoder decoder) throws JSONException {
            ParseFieldOperation op = null;
            JSONArray ops = object.getJSONArray("ops");
            for (int i = 0; i < ops.length(); i++) {
                op = ParseFieldOperations.decode(ops.getJSONObject(i), decoder).mergeWithPrevious(op);
            }
            return op;
        }
    }

    /* compiled from: ParseFieldOperation */
    static class C02432 implements ParseFieldOperationFactory {
        C02432() {
        }

        public ParseFieldOperation decode(JSONObject object, ParseDecoder decoder) throws JSONException {
            return ParseDeleteOperation.getInstance();
        }
    }

    /* compiled from: ParseFieldOperation */
    static class C02443 implements ParseFieldOperationFactory {
        C02443() {
        }

        public ParseFieldOperation decode(JSONObject object, ParseDecoder decoder) throws JSONException {
            return new ParseIncrementOperation((Number) decoder.decode(object.opt("amount")));
        }
    }

    /* compiled from: ParseFieldOperation */
    static class C02454 implements ParseFieldOperationFactory {
        C02454() {
        }

        public ParseFieldOperation decode(JSONObject object, ParseDecoder decoder) throws JSONException {
            return new ParseAddOperation((Collection) decoder.decode(object.opt("objects")));
        }
    }

    /* compiled from: ParseFieldOperation */
    static class C02465 implements ParseFieldOperationFactory {
        C02465() {
        }

        public ParseFieldOperation decode(JSONObject object, ParseDecoder decoder) throws JSONException {
            return new ParseAddUniqueOperation((Collection) decoder.decode(object.opt("objects")));
        }
    }

    /* compiled from: ParseFieldOperation */
    static class C02476 implements ParseFieldOperationFactory {
        C02476() {
        }

        public ParseFieldOperation decode(JSONObject object, ParseDecoder decoder) throws JSONException {
            return new ParseRemoveOperation((Collection) decoder.decode(object.opt("objects")));
        }
    }

    /* compiled from: ParseFieldOperation */
    static class C02487 implements ParseFieldOperationFactory {
        C02487() {
        }

        public ParseFieldOperation decode(JSONObject object, ParseDecoder decoder) throws JSONException {
            return new ParseRelationOperation(new HashSet((List) decoder.decode(object.optJSONArray("objects"))), null);
        }
    }

    /* compiled from: ParseFieldOperation */
    static class C02498 implements ParseFieldOperationFactory {
        C02498() {
        }

        public ParseFieldOperation decode(JSONObject object, ParseDecoder decoder) throws JSONException {
            return new ParseRelationOperation(null, new HashSet((List) decoder.decode(object.optJSONArray("objects"))));
        }
    }

    private ParseFieldOperations() {
    }

    private static void registerDecoder(String opName, ParseFieldOperationFactory factory) {
        opDecoderMap.put(opName, factory);
    }

    static void registerDefaultDecoders() {
        registerDecoder("Batch", new C02421());
        registerDecoder("Delete", new C02432());
        registerDecoder("Increment", new C02443());
        registerDecoder("Add", new C02454());
        registerDecoder("AddUnique", new C02465());
        registerDecoder("Remove", new C02476());
        registerDecoder("AddRelation", new C02487());
        registerDecoder("RemoveRelation", new C02498());
    }

    static ParseFieldOperation decode(JSONObject encoded, ParseDecoder decoder) throws JSONException {
        String op = encoded.optString("__op");
        ParseFieldOperationFactory factory = (ParseFieldOperationFactory) opDecoderMap.get(op);
        if (factory != null) {
            return factory.decode(encoded, decoder);
        }
        throw new RuntimeException("Unable to decode operation of type " + op);
    }

    static ArrayList<Object> jsonArrayAsArrayList(JSONArray array) {
        ArrayList<Object> result = new ArrayList(array.length());
        int i = 0;
        while (i < array.length()) {
            try {
                result.add(array.get(i));
                i++;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
