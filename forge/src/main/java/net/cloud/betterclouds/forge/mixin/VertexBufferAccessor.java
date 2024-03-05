package net.cloud.betterclouds.forge.mixin;

import net.minecraft.client.gl.VertexBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VertexBuffer.class)
public interface VertexBufferAccessor {
    @Accessor("vertexBufferId")
    int getVertexBufferId();

    @Accessor("indexBufferId")
    int getIndexBufferId();

    @Accessor("vertexArrayId")
    int getVertexArrayId();
}