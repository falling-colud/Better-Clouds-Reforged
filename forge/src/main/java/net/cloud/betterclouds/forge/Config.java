package net.cloud.betterclouds.forge;

import com.google.common.base.Objects;
import com.google.gson.InstanceCreator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Config {
    public static ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec GENERAL_SPEC;    
    public static final String DEFAULT_PRESET_KEY = "default";
    public static final InstanceCreator<Config> INSTANCE_CREATOR = type -> new Config();
    

       /* this.distance = other.distance;
        this.randomPlacement = other.randomPlacement;
        this.fuzziness = other.fuzziness;
        this.shuffle = other.shuffle;
        this.yRange = other.yRange;
        this.yOffset = other.yOffset;
        this.sparsity = other.sparsity;
        this.spacing = other.spacing;
        this.sizeXZ = other.sizeXZ;
        this.sizeY = other.sizeY;
        this.travelSpeed = other.travelSpeed;
        this.windFactor = other.windFactor;
        this.colorVariationFactor = other.colorVariationFactor;
        this.chunkSize = other.chunkSize;
        this.samplingScale = other.samplingScale;
        this.scaleFalloffMin = other.scaleFalloffMin;
        this.fadeEdge = other.fadeEdge;
        this.usePersistentBuffers = other.usePersistentBuffers;
        this.irisSupport = other.irisSupport;
        this.enabled = other.enabled;
        this.cloudOverride = other.cloudOverride;
        this.useIrisFBO = other.useIrisFBO;
        this.selectedPreset = other.selectedPreset;
        this.presets = other.presets;
        this.presets.replaceAll(ShaderConfigPreset::new);
        this.lastTelemetryVersion = other.lastTelemetryVersion;
        this.gpuIncompatibleMessageEnabled = other.gpuIncompatibleMessageEnabled;*/

    public static ForgeConfigSpec.BooleanValue enabled;
    public static ForgeConfigSpec.DoubleValue distance;
    public static ForgeConfigSpec.DoubleValue windEffectFactor;

    public static ForgeConfigSpec.DoubleValue randomPlacement;

    public static ForgeConfigSpec.DoubleValue fuzziness;

    public static ForgeConfigSpec.BooleanValue shuffle;

    public static ForgeConfigSpec.DoubleValue yRange;

    public static ForgeConfigSpec.DoubleValue yOffset;

    public static ForgeConfigSpec.DoubleValue sparsity;

    public static ForgeConfigSpec.DoubleValue spacing;

    public static ForgeConfigSpec.DoubleValue sizeXZ;

    public static ForgeConfigSpec.DoubleValue sizeY;

    public static ForgeConfigSpec.DoubleValue travelSpeed;

    public static ForgeConfigSpec.DoubleValue colorVariationFactor;
    public static ForgeConfigSpec.BooleanValue celestialBodyHalo;

    public static ForgeConfigSpec.IntValue chunkSize;

    public static ForgeConfigSpec.DoubleValue samplingScale;

    public static ForgeConfigSpec.DoubleValue scaleFalloffMin;

    public static ForgeConfigSpec.DoubleValue fadeEdge;

    public static ForgeConfigSpec.BooleanValue usePersistentBuffers;

    public static ForgeConfigSpec.BooleanValue irisSupport;

    public static ForgeConfigSpec.BooleanValue cloudOverride;

    public static ForgeConfigSpec.BooleanValue useIrisFBO;

    public static ForgeConfigSpec.IntValue selectedPreset;

    public static List<ShaderConfigPreset> presets = new ArrayList<>();

    public int lastTelemetryVersion = 0;

    public static ForgeConfigSpec.BooleanValue gpuIncompatibleMessageEnabled;

    static {

        Main.LOGGER.info("MAKECONFIG");

        distance = BUILDER.defineInRange("distance", 4.0, 0.1, 1024.0);
        enabled = BUILDER.define("is_enabled", true);
        shuffle = BUILDER.define("shuffle", false);
        usePersistentBuffers = BUILDER.define("usePersistentBuffers", true);
        irisSupport = BUILDER.define("irisSupport", true);
        cloudOverride = BUILDER.define("cloudOverride", true);
        useIrisFBO = BUILDER.define("useIrisFBO", true);
        gpuIncompatibleMessageEnabled = BUILDER.define("gpuIncompatibleMessageEnabled", true);

        windEffectFactor = BUILDER.defineInRange("windEffectFactor", 1f, 0f, 100.0f);
        celestialBodyHalo = BUILDER.define("celestialBodyHalo", false);
        randomPlacement = BUILDER.defineInRange("randomPlacement", 0.0, 0.0, 10.0);
        fuzziness = BUILDER.defineInRange("fuzziness", 1.0, 0.0, 10.0);
        yRange = BUILDER.defineInRange("yRange", 64.0, 0.0, 256.0);
        yOffset = BUILDER.defineInRange("yOffset", 0.0, -256.0, 256.0);
        sparsity = BUILDER.defineInRange("sparsity", 0.0, 0.0, 100.0);
        spacing = BUILDER.defineInRange("spacing", 5.25, 0.0, 100.0);
        sizeXZ = BUILDER.defineInRange("sizeXZ", 16.0, 0.0, 100.0);
        sizeY = BUILDER.defineInRange("sizeY", 8.0, 0.0, 10.0);
        travelSpeed = BUILDER.defineInRange("travelSpeed", 0.03, 0.0, 100.0);
        colorVariationFactor = BUILDER.defineInRange("colorVariationFactor", 1.0, 0.0, 100.0);
        samplingScale = BUILDER.defineInRange("samplingScale", 1.0, 0.0, 100.0);
        scaleFalloffMin = BUILDER.defineInRange("scaleFalloffMin", 0.25, 0.0, 100.0);
        fadeEdge = BUILDER.defineInRange("fadeEdge", 0.15, 0.0, 100.0);
        chunkSize = BUILDER.defineInRange("chunkSize", 32, 1, 1024);
        selectedPreset = BUILDER.defineInRange("selectedPreset", 0, 0, 128);

        GENERAL_SPEC = BUILDER.build();
    }


    public static void loadDefaultPresets() {
        // Remember which default preset was selected, if any
        String selectedDefaultPreset = preset().key;
        Map<String, ShaderConfigPreset> defaults = new HashMap<>(ShaderPresetLoader.INSTANCE.presets());
        boolean missingDefault = presets.stream().noneMatch(preset -> DEFAULT_PRESET_KEY.equals(preset.key));
        presets.removeIf(preset -> preset.key != null && !preset.editable && defaults.containsKey(preset.key));
        presets.addAll(defaults.values());

        if (selectedDefaultPreset != null) {
            // Restore the selected default preset
            presets.stream()
                    .filter(preset -> selectedDefaultPreset.equals(preset.key)).findFirst()
                    .ifPresentOrElse(prevSelectedPreset -> selectedPreset.set(presets.indexOf(prevSelectedPreset)), () -> selectedPreset.set(0));
        }

        if (missingDefault) {
            // No preset with the key 'default' was present,
            // so it is assumed that the presets are not initialized
            presets.removeIf(Config::isPresetEqualToEmpty);
            ShaderConfigPreset defaultPreset = defaults.get(DEFAULT_PRESET_KEY);
            if (defaultPreset != null) {
                ShaderConfigPreset defaultCopy = new ShaderConfigPreset(defaultPreset);
                defaultCopy.markAsCopy();
                presets.add(defaultCopy);
                selectedPreset.set(presets.indexOf(defaultCopy));
            }
        }
        sortPresets();
    }

    @NotNull
    public static ShaderConfigPreset preset() {
        if (presets.isEmpty()) {
            addFirstPreset();
        }
        //selectedPreset.set(MathHelper.clamp(selectedPreset.get(), 0, presets.size() - 1));
        return presets.get(selectedPreset.get());
    }

    private static boolean isPresetEqualToEmpty(ShaderConfigPreset preset) {
        if (preset == null) return true;
        String title = preset.title;
        // The title does not matter
        preset.title = ShaderConfigPreset.EMPTY_PRESET.title;
        boolean equal = preset.isEqualTo(ShaderConfigPreset.EMPTY_PRESET);
        preset.title = title;
        return equal;
    }

    public static void sortPresets() {
        ShaderConfigPreset selected = preset();
        Comparator<ShaderConfigPreset> comparator = Comparator.
                <ShaderConfigPreset, Boolean>comparing(preset -> !preset.editable)
                .thenComparing(preset -> !DEFAULT_PRESET_KEY.equals(preset.key))
                .thenComparing(preset -> preset.title);
        presets.sort(comparator);
        selectedPreset.set(presets.indexOf(selected));
    }

    public static void addFirstPreset() {
        if (!presets.isEmpty()) return;
        presets.add(new ShaderConfigPreset());
    }

    public static int blockDistance() {
        double dist = (distance != null) ? distance.get() : 4;
//        Main.LOGGER.info("Distance: " + dist);
        return (int) (dist * MinecraftClient.getInstance().options.getViewDistance().getValue() * 16);
    }

    public static class ShaderConfigPreset {

        public static final InstanceCreator<ShaderConfigPreset> INSTANCE_CREATOR = type -> new ShaderConfigPreset();
        protected static final ShaderConfigPreset EMPTY_PRESET = new ShaderConfigPreset();

        public ShaderConfigPreset() {
            this("");
        }

        public ShaderConfigPreset(String title) {
            this.title = title;
        }

        public ShaderConfigPreset(ShaderConfigPreset other) {
            this.title = other.title;
            this.key = other.key;
            this.editable = other.editable;
            this.upscaleResolutionFactor = other.upscaleResolutionFactor;
            this.gamma = other.gamma;
            this.sunPathAngle = other.sunPathAngle;
            this.dayBrightness = other.dayBrightness;
            this.nightBrightness = other.nightBrightness;
            this.sunriseStartTime = other.sunriseStartTime;
            this.sunriseEndTime = other.sunriseEndTime;
            this.sunsetStartTime = other.sunsetStartTime;
            this.sunsetEndTime = other.sunsetEndTime;
            this.saturation = other.saturation;
            this.opacity = other.opacity;
            this.opacityFactor = other.opacityFactor;
            this.opacityExponent = other.opacityExponent;
            this.tintRed = other.tintRed;
            this.tintGreen = other.tintGreen;
            this.tintBlue = other.tintBlue;

            //!! NOTE: Don't forget to update `isEqualTo` when adding fields
        }


        public String title;

        @Nullable
        public String key;

        public boolean editable = true;

        public float upscaleResolutionFactor = 1f;

        public float gamma = 1f;

        public float sunPathAngle = 0f;

        public int sunriseStartTime = -785;

        public int sunriseEndTime = 1163;

        public int sunsetStartTime = 10837;

        public int sunsetEndTime = 12785;

        public float dayBrightness = 1f;

        public float nightBrightness = 1f;

        public float saturation = 1f;

        public float opacity = 0.2f;

        public float opacityFactor = 1f;

        public float opacityExponent = 1.5f;

        public float tintRed = 1f;

        public float tintGreen = 1f;

        public float tintBlue = 1f;

        public float gamma() {
            if (gamma > 0) {
                return gamma;
            } else {
                return -1 / gamma;
            }
        }

        public void markAsCopy() {
            editable = true;
            key = null;
        }

        public boolean isEqualTo(ShaderConfigPreset other) {
            if (this == other) return true;
            if (other == null) return false;
            return editable == other.editable &&
                    Float.compare(other.upscaleResolutionFactor, upscaleResolutionFactor) == 0 &&
                    Float.compare(other.gamma, gamma) == 0 &&
                    Float.compare(other.sunPathAngle, sunPathAngle) == 0 &&
                    sunriseStartTime == other.sunriseStartTime &&
                    sunriseEndTime == other.sunriseEndTime &&
                    sunsetStartTime == other.sunsetStartTime &&
                    sunsetEndTime == other.sunsetEndTime &&
                    Float.compare(other.dayBrightness, dayBrightness) == 0 &&
                    Float.compare(other.nightBrightness, nightBrightness) == 0 &&
                    Float.compare(other.saturation, saturation) == 0 &&
                    Float.compare(other.opacity, opacity) == 0 &&
                    Float.compare(other.opacityFactor, opacityFactor) == 0 &&
                    Float.compare(other.opacityExponent, opacityExponent) == 0 &&
                    Float.compare(other.tintRed, tintRed) == 0 &&
                    Float.compare(other.tintGreen, tintGreen) == 0 &&
                    Float.compare(other.tintBlue, tintBlue) == 0 &&
                    Objects.equal(title, other.title) &&
                    Objects.equal(key, other.key);
        }
    }

}
