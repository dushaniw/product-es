package org.wso2.carbon.social;

import org.json.JSONException;
import org.json.JSONStringer;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

public class JSONUtil {
    public static String SimpleNativeObjectToJson(NativeObject obj) throws JSONException {
        JSONStringer json = new JSONStringer();
        simpleNativeObjectToJson(obj, json);
        return json.toString();
    }

    private static void simpleNativeObjectToJson(NativeObject obj, JSONStringer json) throws JSONException {
        json.object();

        Object[] ids = obj.getIds();
        for (Object id : ids) {
            String key = id.toString();
            json.key(key);

            Object value = obj.get(key, obj);
            valueToJson(value, json);
        }

        json.endObject();
    }

    private static void valueToJson(Object value, JSONStringer json) throws JSONException {
        if (value instanceof ScriptableObject
                && ((ScriptableObject) value).getClassName().equals("Date")
                || value instanceof NativeJavaObject) {
            throw new JSONException("Object has Complex values.");
        } else if (value instanceof NativeArray) {
            arrayToJson((NativeArray) value, json);
        } else if (value instanceof NativeObject) {
            simpleNativeObjectToJson((NativeObject) value, json);
        } else {
            json.value(value);
        }
    }

    private static void arrayToJson(NativeArray nativeArray, JSONStringer json) throws JSONException {
        Object[] propIds = nativeArray.getIds();
        if (isArray(propIds)) {
            json.array();

            for (Object propId : propIds) {
                Object value = nativeArray.get((Integer) propId, nativeArray);
                valueToJson(value, json);
            }

            json.endArray();
        } else {
            json.object();

            for (Object propId : propIds) {
                Object value = nativeArray.get(propId.toString(), nativeArray);
                json.key(propId.toString());
                valueToJson(value, json);
            }

            json.endObject();
        }
    }

    private static boolean isArray(Object[] ids) {
        boolean result = true;
        for (Object id : ids) {
            if (!(id instanceof Integer)) {
                result = false;
                break;
            }
        }
        return result;
    }
}