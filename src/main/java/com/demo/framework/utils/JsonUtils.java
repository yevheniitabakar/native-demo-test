package com.demo.framework.utils;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Utility class for JSON operations
 */
public class JsonUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private JsonUtils() {
    }

    /**
     * Convert object to JSON string
     */
    public static String toJson(Object object) {
        LOG.debug("Converting object to JSON");
        return GSON.toJson(object);
    }

    /**
     * Parse JSON string to map
     */
    public static Map<String, Object> parseJson(String json) {
        LOG.debug("Parsing JSON string");
        return GSON.fromJson(json, Map.class);
    }

    /**
     * Parse JSON string to specific class
     */
    public static <T> T parseJson(String json, Class<T> clazz) {
        LOG.debug("Parsing JSON string to class: {}", clazz.getName());
        return GSON.fromJson(json, clazz);
    }

    /**
     * Get value from JSON by path (dot notation)
     */
    public static Object getValueByPath(String json, String path) {
        LOG.debug("Getting value from JSON by path: {}", path);

        JsonElement element = JsonParser.parseString(json);
        String[] keys = path.split("\\.");

        for (String key : keys) {
            if (element.isJsonObject()) {
                element = element.getAsJsonObject().get(key);
            } else {
                LOG.warn("Cannot navigate further in JSON");
                return null;
            }
        }

        return element != null ? element.getAsString() : null;
    }

    /**
     * Check if JSON contains key
     */
    public static boolean containsKey(String json, String key) {
        LOG.debug("Checking if JSON contains key: {}", key);

        try {
            JsonObject object = JsonParser.parseString(json).getAsJsonObject();
            return object.has(key);
        } catch (Exception e) {
            LOG.warn("Error checking JSON key", e);
            return false;
        }
    }

    /**
     * Pretty print JSON
     */
    public static String prettyPrint(String json) {
        LOG.debug("Pretty printing JSON");

        try {
            JsonElement element = JsonParser.parseString(json);
            return GSON.toJson(element);
        } catch (JsonSyntaxException e) {
            LOG.error("Invalid JSON format", e);
            return json;
        }
    }
}

