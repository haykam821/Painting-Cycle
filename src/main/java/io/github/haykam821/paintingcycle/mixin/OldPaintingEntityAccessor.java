package io.github.haykam821.paintingcycle.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.decoration.painting.PaintingMotive;

@Pseudo
@Mixin(targets = "net.minecraft.class_1534", remap = false)
public interface OldPaintingEntityAccessor {
	@Accessor("field_7134")
	public PaintingMotive getMotive();

	@Accessor("field_7134")
	public void setMotive(PaintingMotive motive);
}
