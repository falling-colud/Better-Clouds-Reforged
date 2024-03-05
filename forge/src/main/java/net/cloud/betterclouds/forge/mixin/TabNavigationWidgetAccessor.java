package net.cloud.betterclouds.forge.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.widget.TabButtonWidget;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TabNavigationWidget.class)
public interface TabNavigationWidgetAccessor {
    @Accessor
    ImmutableList<TabButtonWidget> getTabButtons();
}
