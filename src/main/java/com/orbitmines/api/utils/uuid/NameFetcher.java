package com.orbitmines.api.utils.uuid;

import com.orbitmines.api.utils.DateUtils;
import org.json.fang.JSONArray;
import org.json.fang.JSONObject;
import org.json.fang.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.Callable;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class NameFetcher implements Callable<Map<UUID, List<String>>> {

    private static Map<UUID, List<String>> cache = new HashMap<>();

    public NameFetcher(UUID uuid) {
        cache.put(uuid, getHistory(uuid));
    }

    private List<String> getHistory(UUID uuid) {
        if (cache.containsKey(uuid)) return cache.get(uuid);
        URLConnection connection;

        try {
            connection = new URL("https://api.mojang.com/user/profiles/" + uuid.toString().replace("-", "").toLowerCase() + "/names").openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String string = in.readLine();
            in.close();
            JSONParser parser = new JSONParser();
            List<String> names = new ArrayList<>();
            try {
                JSONArray array = (JSONArray) parser.parse(string);

                if (array.size() != 1) {
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject obj = (JSONObject) array.get(i);
                        if (i == 0) {
                            names.add(obj.get("name") + "");
                        } else if (i == array.size()) {
                            String date = DateUtils.FORMAT.format(new Date((long) obj.get("changedToAt")));

                            names.add(obj.get("name") + " " + date);
                        } else {
                            String date = DateUtils.FORMAT.format(new Date((long) obj.get("changedToAt")));

                            names.add(obj.get("name") + " " + date);
                        }
                    }
                } else {
                    names.add(((JSONObject) array.get(0)).get("name") + "");
                }
            } catch (Exception e) {
                names = null;
            }

            cache.put(uuid, names);

            return names;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void invalidateCache() {
        cache.clear();
    }

    @Override
    public Map<UUID, List<String>> call() throws Exception {
        return cache;
    }
}
