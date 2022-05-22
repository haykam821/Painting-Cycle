package io.github.haykam821.paintingcycle.mixin;

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
import net.minecraft.tag.PaintingVariantTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

@Mixin(Entity.class)
public class EntityMixin {
	@Unique
	private List<RegistryEntry<PaintingVariant>> getPotentialVariants(PaintingVariant current) {
		List<RegistryEntry<PaintingVariant>> potentialVariants = Lists.newArrayList();

		for (RegistryEntry<PaintingVariant> potential : Registry.PAINTING_VARIANT.iterateEntries(PaintingVariantTags.PLACEABLE)) {
			if (PaintingCycle.isPotentialVariant(potential.value(), current)) {
				potentialVariants.add(potential);
			}
		}

		return potentialVariants;
	}

	@Unique
	private boolean changeVariant() {
		RegistryEntry<PaintingVariant> current = ((PaintingEntity) (Object) this).getVariant();

		List<RegistryEntry<PaintingVariant>> potentialVariants = this.getPotentialVariants(current.value());
		if (potentialVariants.size() <= 1) return false;

		int index = (potentialVariants.indexOf(current) + 1) % potentialVariants.size();
		RegistryEntry<PaintingVariant> replacement = potentialVariants.get(index);

		((PaintingEntityAccessor) (Object) this).callSetVariant(replacement);
		return true;
	}

	@Inject(method = "interact", at = @At("HEAD"), cancellable = true)
	public void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
		if (!((Entity) (Object) this instanceof PaintingEntity)) return;

		ItemStack handStack = player.getStackInHand(hand);
		if (!handStack.isOf(Items.PAINTING)) return;

		boolean changed = this.changeVariant();
		ci.setReturnValue(changed ? ActionResult.SUCCESS : ActionResult.FAIL);
	}
}