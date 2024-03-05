package com.mentoskapraha.quilted_clear_skies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @ModifyVariable(
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/util/CubicSampler;sampleVec3d(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/CubicSampler$Vec3dFetcher;)Lnet/minecraft/util/math/Vec3d;"),
            method = "render",
            ordinal = 2,
            require = 1,
            allow = 1)
    /* IntelliJ says that onSampleColor should return a float and the variable val should also be a float.
       This is untrue, both values should be Vec3d. If anyone know why this happens let me know :)
       Also the code runs and builds just fine even with this error being flagged.
    */
    private static Vec3d onSampleColor(Vec3d val) {
        final MinecraftClient mc = MinecraftClient.getInstance();
        final ClientWorld world = mc.world;

        if(world == null) return val;

        if (world.getDimension().hasSkyLight()) {
            return world.getSkyColor(mc.gameRenderer.getCamera().getPos(), mc.getTickDelta());
        } else {
            return val;
        }
    }

    @ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/Vec3f;dot(Lnet/minecraft/util/math/Vec3f;)F"), method = "render", ordinal = 7, require = 1, allow = 1)
    private static float afterPlaneDot(float dotProduct) {
        return 0;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getRainGradient(F)F"), method = "render", require = 1, allow = 1)
    private static float onGetRainLevel(ClientWorld world, float tickDelta) {
        return 0;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getThunderGradient(F)F"), method = "render", require = 1, allow = 1)
    private static float onGetThunderLevel(ClientWorld world, float tickDelta) {
        return 0;
    }
}
