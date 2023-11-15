package com.mentoskapraha.clear_skies_reforged;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.phys.Vec3;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
	@ModifyVariable(
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/util/CubicSampler;gaussianSampleVec3(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/util/CubicSampler$Vec3Fetcher;)Lnet/minecraft/world/phys/Vec3;"),
			method = "setupColor",
			ordinal = 2,
			require = 1,
			allow = 1)
	private static Vec3 onSampleColor(Vec3 val) {
		final Minecraft mc = Minecraft.getInstance();
		final ClientLevel world = mc.level;

		if (world.dimensionType().hasSkyLight()) {
			return world.getSkyColor(mc.gameRenderer.getMainCamera().getPosition(), mc.getFrameTime());
		} else {
			return val;
		}
	}

	@ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lcom/mojang/math/Vector3f;dot(Lcom/mojang/math/Vector3f;)F"), method = "setupColor", ordinal = 7, require = 1, allow = 1)
	private static float afterPlaneDot(float dotPrduct) {
		return 0;
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F"), method = "setupColor", require = 1, allow = 1)
	private static float onGetRainLevel(ClientLevel world, float tickDelta) {
		return 0;
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getThunderLevel(F)F"), method = "setupColor", require = 1, allow = 1)
	private static float onGetThunderLevel(ClientLevel world, float tickDelta) {
		return 0;
	}
}
