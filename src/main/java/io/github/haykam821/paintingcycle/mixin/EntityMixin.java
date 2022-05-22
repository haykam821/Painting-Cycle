package io.github.haykam821.paintingcycle.mixin;

import java.util.List;

import com.google.common.collect.Lists;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.paintingcycle.PaintingCycle;
import net.minecraft.class_7406;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

@Mixin(Entity.class)
public class EntityMixin {
	@Unique
	private List<RegistryEntry<PaintingMotive>> getPotentialMotives(PaintingMotive current) {
		List<RegistryEntry<PaintingMotive>> potentialMotives = Lists.newArrayList();

		for (RegistryEntry<PaintingMotive> potential : Registry.PAINTING_MOTIVE.iterateEntries(class_7406.PLACEABLE)) {
			if (PaintingCycle.isPotentialMotive(potential.value(), current)) {
				potentialMotives.add(potential);
			}
		}

		return potentialMotives;
	}

	@Unique
	private boolean changeMotive() {
		RegistryEntry<PaintingMotive> current = ((PaintingEntity) (Object) this).method_43404();

		List<RegistryEntry<PaintingMotive>> potentialMotives = this.getPotentialMotives(current.value());
		if (potentialMotives.size() <= 1) return false;

		int index = (potentialMotives.indexOf(current) + 1) % potentialMotives.size();
		RegistryEntry<PaintingMotive> replacement = potentialMotives.get(index);

		((PaintingEntityAccessor) (Object) this).setMotive(replacement);
		return true;
	}

	@Inject(method = "interact", at = @At("HEAD"), cancellable = true)
	public void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
		if (!((Entity) (Object) this instanceof PaintingEntity)) return;

		ItemStack handStack = player.getStackInHand(hand);
		if (!handStack.isOf(Items.PAINTING)) return;

		boolean changed = this.changeMotive();
		ci.setReturnValue(changed ? ActionResult.SUCCESS : ActionResult.FAIL);
	}
}