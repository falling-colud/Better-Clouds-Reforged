package net.cloud.betterclouds.forge;

import net.cloud.betterclouds.forge.clouds.Debug;
import net.cloud.betterclouds.forge.compat.DistantHorizonsCompat;
import net.cloud.betterclouds.forge.compat.GLCompat;
import net.cloud.betterclouds.forge.compat.Telemetry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.GL32;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Mod(Main.MODID)
public class Main {

    //TODO make icon
    //TODO get description?

    public static final String MODID = "betterclouds";
    public static final boolean IS_DEV = FMLLoader.isProduction();
    public static final NamedLogger LOGGER = new NamedLogger(LogManager.getLogger(MODID), !IS_DEV);
    public static GLCompat glCompat;

    public static String getVersion() {
        return "1.0.0";
    }


    public Main() {
        LOGGER.info("INIT");
        PreLaunchHandler.onPreLaunch();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onClientSetup);
        MinecraftForge.EVENT_BUS.register(this);
        DistantHorizonsCompat.initialize();
    }

    public static void initGlCompat() {
        try {
            glCompat = new GLCompat(IS_DEV);
        } catch (Exception e) {
            Telemetry.INSTANCE.sendUnhandledException(e);
            throw e;
        }

        if (glCompat.isIncompatible()) {
            LOGGER.warn("Your GPU (or configuration) is not compatible with Better Clouds. Try updating your drivers?");
            LOGGER.info(" - Vendor:       {}", glCompat.getString(GL32.GL_VENDOR));
            LOGGER.info(" - Renderer:     {}", glCompat.getString(GL32.GL_RENDERER));
            LOGGER.info(" - GL Version:   {}", glCompat.getString(GL32.GL_VERSION));
            LOGGER.info(" - GLSL Version: {}", glCompat.getString(GL32.GL_SHADING_LANGUAGE_VERSION));
            LOGGER.info(" - Extensions:   {}", String.join(", ", glCompat.supportedCheckedExtensions));
            LOGGER.info(" - Functions:    {}", String.join(", ", glCompat.supportedCheckedFunctions));
        } else if (glCompat.isPartiallyIncompatible()) {
            LOGGER.warn("Your GPU is not fully compatible with Better Clouds.");
            for (String fallback : glCompat.usedFallbacks) {
                LOGGER.info("- Using {} fallback", fallback);
            }
        }

        /*
        if (Config.lastTelemetryVersion < Telemetry.VERSION) {
            Telemetry.INSTANCE.sendSystemInfo()
                    .whenComplete((success, throwable) -> {
                        MinecraftClient client = MinecraftClient.getInstance();
                        if (success && client != null) {
                            client.execute(() -> {
                                Main.lastTelemetryVersion = Telemetry.VERSION;
                                CONFIG.save();
                            });
                        }
                    });
        }
         */
    }


    public static boolean isProfilingEnabled() {
        return Debug.profileInterval > 0;
    }

    public static void debugChatMessage(String id, Object... args) {
        debugChatMessage(Text.translatable(debugChatMessageKey(id), args));
    }

    public static void debugChatMessage(Text message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null) return;
        client.inGameHud.getChatHud().addMessage(Text.literal("§e[§bBC§b§e]§r ").append(message));
    }

    public static String debugChatMessageKey(String id) {
        return MODID + ".message." + id;
    }

//    public static Version getVersion() {
//        return version;
//    }

    public void onClientSetup(FMLClientSetupEvent event) {
//        ClientLifecycleEvents.CLIENT_STARTED.register(client -> glCompat.enableDebugOutputSynchronous());
        //glCompat.enableDebugOutputSynchronous();

        //ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(ShaderPresetLoader.INSTANCE);

        if (!IS_DEV) return;
        LOGGER.info("Initialized in dev mode, performance might vary");
    }

    @SubscribeEvent
    public void onClientLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (glCompat.isIncompatible()) {
            CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS).execute(Main::sendGpuIncompatibleChatMessage);
        }
        if (glCompat.isIncompatible()) {
            CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS).execute(Main::sendGpuIncompatibleChatMessage);
        } else if (glCompat.isPartiallyIncompatible()) {
            CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS).execute(Main::sendGpuPartiallyIncompatibleChatMessage);
        }
    }

    public static void sendGpuIncompatibleChatMessage() {
        if (!Config.gpuIncompatibleMessageEnabled.get()) return;
        debugChatMessage(
                Text.translatable(debugChatMessageKey("gpuIncompatible"))
                        .append(Text.literal("\n - "))
                        .append(Text.translatable(debugChatMessageKey("disable"))
                                .styled(style -> style.withItalic(true).withUnderline(true).withColor(Formatting.GRAY)
                                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                                "/betterclouds:config gpuIncompatibleMessage false")))));
    }

    public static void sendGpuPartiallyIncompatibleChatMessage() {
        if (!Config.gpuIncompatibleMessageEnabled.get()) return;
        debugChatMessage(
                Text.translatable(debugChatMessageKey("gpuPartiallyIncompatible"))
                        .append(Text.literal("\n - "))
                        .append(Text.translatable(debugChatMessageKey("disable"))
                                .styled(style -> style.withItalic(true).withUnderline(true).withColor(Formatting.GRAY)
                                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                                "/betterclouds:config gpuIncompatibleMessage false")))));
    }

    public void addReloadListenerEvent(AddReloadListenerEvent e) {
    }

    @SubscribeEvent
    public void onRegisterCommandEvent(RegisterClientCommandsEvent event) {
        Commands.register(event.getDispatcher());
    }
}
