package com.iafenvoy.elb;

import com.iafenvoy.elb.gui.PreLaunchWindow;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EarlyLoadingBarPreLaunch implements PreLaunchEntrypoint {
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onPreLaunch() {
        if (isWindows() && FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            PreLaunchWindow.display();
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
}
