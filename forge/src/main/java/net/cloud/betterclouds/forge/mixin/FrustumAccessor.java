package net.cloud.betterclouds.forge.mixin;

import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.Frustum;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Frustum.class)
public interface FrustumAccessor {
    @Accessor()
    FrustumIntersection getFrustumIntersection();

    @Accessor()
    Matrix4f getPositionProjectionMatrix();

    @Accessor()
    Vector4f getRecession();

    @Accessor()
    void setFrustumIntersection(FrustumIntersection intersection);

    @Accessor()
    void setPositionProjectionMatrix(Matrix4f matrix);

    @Accessor()
    void setRecession(Vector4f vector4f);

    @Accessor("x")
    double getX();
    @Accessor("y")
    double getY();
    @Accessor("z")
    double getZ();

    @Accessor("x")
    void setX(double x);
    @Accessor("y")
    void setY(double y);
    @Accessor("z")
    void setZ(double z);
}