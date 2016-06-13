package com.creacc.ccbox.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonUtils {
	
	public static JSONObject asJsonObject(String json) {
		try {
			return (JSONObject) new JSONTokener(json).nextValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONArray asJsonArray(String json) {
		try {
			return (JSONArray) new JSONTokener(json).nextValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String getStringValue(String json, String key, String defaultValue) {
		return JsonUtils.getStringValue(JsonUtils.asJsonObject(json), key, defaultValue);
	}
	
	public static int getIntValue(String json, String key, int defaultValue) {
		return JsonUtils.getIntValue(JsonUtils.asJsonObject(json), key, defaultValue);
	}

	public static boolean getBooleanValue(String json, String key, boolean defaultValue) {
		return JsonUtils.getBooleanValue(JsonUtils.asJsonObject(json), key, defaultValue);
	}
	
	public static double getDoubleValue(String json, String key, double defaultValue) {
		return JsonUtils.getDoubleValue(JsonUtils.asJsonObject(json), key, defaultValue);
	}
	
	public static long getLongValue(String json, String key, long defaultValue) {
		return JsonUtils.getLongValue(JsonUtils.asJsonObject(json), key, defaultValue);
	}
	
	public static Object getValue(String json, String key, Object defaultValue) {
		return JsonUtils.getValue(JsonUtils.asJsonObject(json), key, defaultValue);
	}
	
	public static JSONObject getJsonObject(String json, String key, JSONObject defaultValue) {
		return JsonUtils.getJsonObject(JsonUtils.asJsonObject(json), key, defaultValue);
	}
	
	public static JSONArray getJsonArray(String json, String key, JSONArray defaultValue) {
		return JsonUtils.getJsonArray(JsonUtils.asJsonObject(json), key, defaultValue);
	}
	
	public static String getStringValue(JSONObject jsonObject, String key, String defaultValue) {
		if (JsonUtils.hasKey(jsonObject, key)) {
			try {
				return jsonObject.getString(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}
	
	public static int getIntValue(JSONObject jsonObject, String key, int defaultValue) {
		if (JsonUtils.hasKey(jsonObject, key)) {
			try {
				return jsonObject.getInt(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}

	public static boolean getBooleanValue(JSONObject jsonObject, String key, boolean defaultValue) {
		if (JsonUtils.hasKey(jsonObject, key)) {
			try {
				return jsonObject.getBoolean(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}
	
	public static double getDoubleValue(JSONObject jsonObject, String key, double defaultValue) {
		if (JsonUtils.hasKey(jsonObject, key)) {
			try {
				return jsonObject.getDouble(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}
	
	public static long getLongValue(JSONObject jsonObject, String key, long defaultValue) {
		if (JsonUtils.hasKey(jsonObject, key)) {
			try {
				return jsonObject.getLong(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}
	
	public static Object getValue(JSONObject jsonObject, String key, Object defaultValue) {
		if (JsonUtils.hasKey(jsonObject, key)) {
			try {
				return jsonObject.get(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}
	
	public static JSONObject getJsonObject(JSONObject jsonObject, String key, JSONObject defaultValue) {
		if (JsonUtils.hasKey(jsonObject, key)) {
			try {
				return jsonObject.getJSONObject(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}
	
	public static JSONArray getJsonArray(JSONObject jsonObject, String key, JSONArray defaultValue) {
		if (JsonUtils.hasKey(jsonObject, key)) {
			try {
				return jsonObject.getJSONArray(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}
	
	public static JSONObject getJsonObject(JSONArray jsonArray, int index, JSONObject defaultValue) {
		try {
			return jsonArray.getJSONObject(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}
	
	public static Object get(JSONArray jsonArray, int index, Object defaultValue) {
		try {
			return jsonArray.get(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}
	
	public static boolean hasKey(JSONObject jsonObject, String key) {
		if (jsonObject != null && key != null && !"".equals(key)) {
			return jsonObject.has(key) && !jsonObject.isNull(key);
		}
		return false;
	}
}
