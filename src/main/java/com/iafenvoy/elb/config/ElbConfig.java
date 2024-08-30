package com.iafenvoy.elb.config;

import net.fabricmc.loader.api.FabricLoader;

public class ElbConfig {
    private static ElbConfig INSTANCE = null;

    public String logoPath = null;
    public String barTitle = "Minecraft %version%";
    public String barMessage = "Minecraft is launching, please wait";
    public String memoryBarColor = "-65536";
    public String messageBarColor = "16711935";

    public static ElbConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = ConfigLoader.load(ElbConfig.class, "./config/early-loading-bar.json", new ElbConfig());
            String version = FabricLoader.getInstance().getModContainer("minecraft").get().getMetadata().getVersion().getFriendlyString();
            INSTANCE.barTitle = INSTANCE.barTitle.replaceAll("%version%", version);
            INSTANCE.barMessage = INSTANCE.barMessage.replaceAll("%version%", version);
        }
        return INSTANCE;
    }
}
