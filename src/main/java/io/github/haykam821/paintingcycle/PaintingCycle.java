package io.github.haykam821.paintingcycle;

import net.minecraft.entity.decoration.painting.PaintingMotive;

public final class PaintingCycle {
	private PaintingCycle() {
		return;
	}

	public static boolean isPotentialMotive(PaintingMotive proposed, PaintingMotive current) {
		return proposed.getWidth() == current.getWidth() && proposed.getHeight() == current.getHeight();
	}
}
