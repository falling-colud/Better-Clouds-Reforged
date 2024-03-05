package net.cloud.betterclouds.fabric;

import net.cloud.betterclouds.BetterClouds;
import net.fabricmc.api.ModInitializer;

public class BetterCloudsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BetterClouds.init();
    }
}