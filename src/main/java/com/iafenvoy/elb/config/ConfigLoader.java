package com.iafenvoy.elb.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iafenvoy.elb.EarlyLoadingBarPreLaunch;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ConfigLoader {
    private static final Gson GSON = new Gson();

    public static <T> T load(Class<T> clazz, String path, T defaultValue) {
        try {
            FileInputStream stream = new FileInputStream(path);
            InputStreamReader reader = new InputStreamReader(stream);
            return GSON.fromJson(reader, clazz);
        } catch (IOException e) {
            EarlyLoadingBarPreLaunch.LOGGER.error("Failed to read file {}", path, e);
            try {
                FileUtils.write(new File(path), GSON.toJson(defaultValue), StandardCharsets.UTF_8);
            } catch (IOException ex) {
                EarlyLoadingBarPreLaunch.LOGGER.error("Failed to write file {}", path, ex);
            }
            return defaultValue;
        }
    }
}
