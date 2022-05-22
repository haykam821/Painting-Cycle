package io.github.haykam821.paintingcycle.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.util.registry.RegistryEntry;

@Mixin(PaintingEntity.class)
public interface PaintingEntityAccessor {
	@Invoker(value = "method_43402", remap = false)
	public void setMotive(RegistryEntry<PaintingMotive> motive);
}
