package main.java.com.yt.app.util.copy;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class GsonUtil {
	private static Gson gson = new Gson();

	public static List<?> jsonToList(String jsonstr, Class<?> cls) {
		List<Object> list = new ArrayList<>();
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(jsonstr);
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		for (JsonElement jsonelement : jsonArray) {
			String jsons = jsonelement.toString();
			Object obj = gson.fromJson(jsons, cls);
			list.add(obj);
		}
		return list;
	}

}
