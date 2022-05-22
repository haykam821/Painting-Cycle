package io.github.haykam821.paintingcycle;

import net.minecraft.entity.decoration.painting.PaintingVariant;

public final class PaintingCycle {
	private PaintingCycle() {
		return;
	}

	public static boolean isPotentialVariant(PaintingVariant proposed, PaintingVariant current) {
		return proposed.getWidth() == current.getWidth() && proposed.getHeight() == current.getHeight();
	}
}
