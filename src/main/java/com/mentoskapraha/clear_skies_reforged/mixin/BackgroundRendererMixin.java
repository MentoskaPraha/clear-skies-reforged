package com.mentoskapraha.clear_skies_reforged.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;

@Mixin(BackgroundRenderer.class)
@ClientOnly
public class BackgroundRendererMixin {
	@Shadow private static float red;
	@Shadow private static float green;
	@Shadow private static float blue;
	@Shadow private static int waterFogColor = -1;
	@Shadow private static int nextWaterFogColor = -1;
	@Shadow private static long lastWaterFogColorUpdateTime = -1L;

	@ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/CubicSampler;sampleColor(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/CubicSampler$RgbFetcher;)Lnet/minecraft/util/math/Vec3d;"), method = "render", ordinal = 2, require = 1, allow = 1)
	private static Vec3d onSampleColor(Vec3d val) {
		final MinecraftClient mc = MinecraftClient.getInstance();
		final ClientWorld world = mc.world;

		if (world.getDimension().hasSkyLight()) {
			return world.getSkyColor(mc.gameRenderer.getCamera().getPos(), mc.getTickDelta());
		} else {
			return val;
		}
	}

	@ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/util/math/Vector3f;dot(Lnet/minecraft/client/util/math/Vector3f;)F"), method = "render", ordinal = 7, require = 1, allow = 1)
	private static float afterPlaneDot(float dotPrduct) {
		return 0;
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getRainGradient(F)F"), method = "render", require = 1, allow = 1)
	private static float onGetRainGradient(ClientWorld world, float tickDelta) {
		return 0;
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getThunderGradient(F)F"), method = "render", require = 1, allow = 1)
	private static float onGetThunderGradient(ClientWorld world, float tickDelta) {
		return 0;
	}
}
