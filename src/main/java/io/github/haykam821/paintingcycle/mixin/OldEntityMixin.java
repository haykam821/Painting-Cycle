package io.github.haykam821.paintingcycle.mixin;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.paintingcycle.PaintingCycle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;

@Mixin(Entity.class)
public class OldEntityMixin {
	@Unique
	private List<PaintingVariant> getPotentialVariants(PaintingVariant currentVariant) {
		List<PaintingVariant> potentialVariants = Lists.newArrayList();

		Iterator<PaintingVariant> iterator = Registry.PAINTING_VARIANT.iterator();
		while (iterator.hasNext()) {
			PaintingVariant variant = iterator.next();

			if (PaintingCycle.isPotentialVariant(variant, currentVariant)) {
				potentialVariants.add(variant);
			}
		}

		return potentialVariants;
	}

	@Unique
	private boolean changeVariant() {
		OldPaintingEntityAccessor accessor = (OldPaintingEntityAccessor) (Object) this;

		PaintingVariant currentVariant = accessor.getVariant();
		List<PaintingVariant> potentialVariants = this.getPotentialVariants(currentVariant);
		if (potentialVariants.size() <= 1) return false;

		PaintingVariant newVariant = potentialVariants.get((potentialVariants.indexOf(currentVariant) + 1) % potentialVariants.size());
		accessor.setVariant(newVariant);
		return true;
	}

	@Inject(method = "interact", at = @At("HEAD"), cancellable = true)
	public void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
		if (!((Entity) (Object) this instanceof PaintingEntity)) return;

		ItemStack handStack = player.getStackInHand(hand);
		if (handStack.getItem() != Items.PAINTING) return;

		boolean changed = this.changeVariant();
		ci.setReturnValue(changed ? ActionResult.SUCCESS : ActionResult.FAIL);
	}
}