package io.github.haykam821.paintingcycle.mixin;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;

@Mixin(Entity.class)
public class EntityMixin {
	@Unique
	private List<PaintingMotive> getPotentialMotives(PaintingMotive currentMotive) {
		List<PaintingMotive> potentialMotives = Lists.newArrayList();

		Iterator<PaintingMotive> iterator = Registry.PAINTING_MOTIVE.iterator();
		while (iterator.hasNext()) {
			PaintingMotive motive = iterator.next();

			if (motive.getWidth() != currentMotive.getWidth()) continue;
			if (motive.getHeight() != currentMotive.getHeight()) continue;

			potentialMotives.add(motive);
		}

		return potentialMotives;
	}

	@Unique
	private boolean changeMotive() {
		PaintingMotive currentMotive = ((PaintingEntity) (Object) this).motive;
		List<PaintingMotive> potentialMotives = this.getPotentialMotives(currentMotive);
		if (potentialMotives.size() <= 1) return false;

		PaintingMotive newMotive = potentialMotives.get((potentialMotives.indexOf(currentMotive) + 1) % potentialMotives.size());
		((PaintingEntity) (Object) this).motive = newMotive;
		return true;
	}

	@Inject(method = "interact", at = @At("HEAD"), cancellable = true)
	public void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
		if (!((Entity) (Object) this instanceof PaintingEntity)) return;

		ItemStack handStack = player.getStackInHand(hand);
		if (handStack.getItem() != Items.PAINTING) return;

		boolean changed = this.changeMotive();
		ci.setReturnValue(changed ? ActionResult.SUCCESS : ActionResult.FAIL);
	}
}