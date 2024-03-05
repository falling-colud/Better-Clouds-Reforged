package net.cloud.betterclouds.forge;

import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.Box;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeBlockEntity;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Overwrite;

@OnlyIn(Dist.CLIENT)
public class CustomFrustum extends Frustum {
    public FrustumIntersection frustumIntersection = new FrustumIntersection();
    public Matrix4f positionProjectionMatrix = new Matrix4f();
    public Vector4f recession;
    public double x;
    public double y;
    public double z;

    /**
     * @author
     * @reason
     */

    public CustomFrustum(Matrix4f positionMatrix, Matrix4f projectionMatrix) {
        super(positionMatrix, projectionMatrix);
    }

    public CustomFrustum(CustomFrustum frustum) {
        super(frustum);
    }

    public Frustum coverBoxAroundSetPosition(int boxSize) {
        double d0 = Math.floor(this.x / (double)boxSize) * (double)boxSize;
        double d1 = Math.floor(this.y / (double)boxSize) * (double)boxSize;
        double d2 = Math.floor(this.z / (double)boxSize) * (double)boxSize;
        double d3 = Math.ceil(this.x / (double)boxSize) * (double)boxSize;
        double d4 = Math.ceil(this.y / (double)boxSize) * (double)boxSize;

        for(double d5 = Math.ceil(this.z / (double)boxSize) * (double)boxSize; this.frustumIntersection.intersectAab((float)(d0 - this.x), (float)(d1 - this.y), (float)(d2 - this.z), (float)(d3 - this.x), (float)(d4 - this.y), (float)(d5 - this.z)) != -2; this.z -= (double)(this.recession.z() * 4.0F)) {
            this.x -= (double)(this.recession.x() * 4.0F);
            this.y -= (double)(this.recession.y() * 4.0F);
        }

        return (Frustum) (Object) this;
    }

    /**
     * @author
     * @reason
     */
    
    public void setPosition(double cameraX, double cameraY, double cameraZ) {
        this.x = cameraX;
        this.y = cameraY;
        this.z = cameraZ;
    }
    /**
     * @author
     * @reason
     */
    
    private void init(Matrix4f positionMatrix, Matrix4f projectionMatrix) {
        projectionMatrix.mul(positionMatrix, this.positionProjectionMatrix);
        this.frustumIntersection.set(this.positionProjectionMatrix);
        this.recession = this.positionProjectionMatrix.transformTranspose(new Vector4f(0.0F, 0.0F, 1.0F, 0.0F));
    }

    /**
     * @author
     * @reason
     */
    
    public boolean isVisible(Box box) {
        return box.equals(IForgeBlockEntity.INFINITE_EXTENT_AABB) ? true : this.isVisible(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    /**
     * @author
     * @reason
     */
    
    private boolean isVisible(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        float f = (float)(minX - this.x);
        float f1 = (float)(minY - this.y);
        float f2 = (float)(minZ - this.z);
        float f3 = (float)(maxX - this.x);
        float f4 = (float)(maxY - this.y);
        float f5 = (float)(maxZ - this.z);
        return this.frustumIntersection.testAab(f, f1, f2, f3, f4, f5);
    }
}
