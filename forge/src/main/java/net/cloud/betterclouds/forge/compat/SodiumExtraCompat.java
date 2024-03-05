package net.cloud.betterclouds.forge.compat;


import net.minecraftforge.fml.ModList;
import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;

public class SodiumExtraCompat {
    public static final boolean IS_LOADED = ModList.get().isLoaded("magnesium-extra");

    public static float getCloudsHeight() {
        return SodiumExtraClientMod.options().extraSettings.cloudHeight;
    }
}
