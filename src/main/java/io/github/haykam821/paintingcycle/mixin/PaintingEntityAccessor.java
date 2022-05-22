package io.github.haykam821.paintingcycle.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.util.registry.RegistryEntry;

@Mixin(PaintingEntity.class)
public interface PaintingEntityAccessor {
	@Invoker("setVariant")
	public void callSetVariant(RegistryEntry<PaintingVariant> variant);
}
