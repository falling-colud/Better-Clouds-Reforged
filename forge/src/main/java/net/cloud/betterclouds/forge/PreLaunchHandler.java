package net.cloud.betterclouds.forge;


import net.minecraftforge.fml.ModList;
import net.cloud.betterclouds.forge.renderdoc.CaptureManager;
import net.cloud.betterclouds.forge.renderdoc.RenderDoc;
import net.cloud.betterclouds.forge.renderdoc.RenderDocLoader;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.Environment;
import org.apache.logging.log4j.LogManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber()
public class PreLaunchHandler {
    public static void onPreLaunch() {
        
        NamedLogger logger = new NamedLogger(LogManager.getLogger("BetterClouds PreLaunch"), !FMLEnvironment.production);
        try {
            CaptureManager.LaunchConfig config = CaptureManager.readLaunchConfig();
            if(config.isExpired()) {
                CaptureManager.deleteLaunchConfig();
                return;
            }
            if(!config.load()) {
                return;
            }
            if(!RenderDocLoader.isAvailable()) {
                logger.info("RenderDoc is not available");
                return;
            }
            RenderDocLoader.load();
            if(!RenderDoc.isAvailable()) {
                logger.info("RenderDoc is not available");
                return;
            }

            Path captureTemplatePath = Path.of("./better-clouds/captures/capture");
            Files.createDirectories(captureTemplatePath.getParent());
            RenderDoc.setCaptureOption(RenderDoc.CaptureOption.API_VALIDATION, true);
            RenderDoc.disableOverlayOptions(RenderDoc.OverlayOption.ENABLED);
            RenderDoc.setCaptureKeys();
            RenderDoc.setCaptureFilePathTemplate(captureTemplatePath.toString());
            logger.info("RenderDoc loaded and ready");
            if(config.once()) {
                try {
                    CaptureManager.writeLaunchConfig(new CaptureManager.LaunchConfig(false, true, config.expires()));
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            logger.error("RenderDoc could not be loaded: {}", e);
        }
    }
}
