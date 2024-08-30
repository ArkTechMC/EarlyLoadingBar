package com.iafenvoy.elb;

import com.iafenvoy.elb.gui.PreLaunchWindow;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class EarlyLoadingBar implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> PreLaunchWindow.remove());
    }
}
