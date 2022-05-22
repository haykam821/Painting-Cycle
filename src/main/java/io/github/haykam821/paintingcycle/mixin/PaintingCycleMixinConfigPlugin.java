package io.github.haykam821.paintingcycle.mixin;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;

public class PaintingCycleMixinConfigPlugin implements IMixinConfigPlugin {
	private static final boolean HAS_VARIANT_REGISTRY = isMinecraftVersion(">=1.19-alpha.22.16.a");
	private static final String MIXIN_CLASS_PREFIX = "io.github.haykam821.paintingcycle.mixin.";

	private static final String OLD_ENTITY_CLASS = MIXIN_CLASS_PREFIX + "OldEntityMixin";
	private static final String OLD_PAINTING_ACCESSOR_CLASS = MIXIN_CLASS_PREFIX + "OldPaintingEntityAccessor";

	private static final String NEW_ENTITY_CLASS = MIXIN_CLASS_PREFIX + "EntityMixin";
	private static final String NEW_PAINTING_ACCESSOR_CLASS = MIXIN_CLASS_PREFIX + "PaintingEntityAccessor";

	@Override
	public void onLoad(String mixinPackage) {
		return;
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClass, String mixinClass) {
		if (mixinClass.equals(OLD_ENTITY_CLASS) || mixinClass.equals(OLD_PAINTING_ACCESSOR_CLASS)) {
			return !HAS_VARIANT_REGISTRY;
		} else if (mixinClass.equals(NEW_ENTITY_CLASS) || mixinClass.equals(NEW_PAINTING_ACCESSOR_CLASS)) {
			return HAS_VARIANT_REGISTRY;
		}

		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
		return;
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		return;
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		return;
	}

	private static Version getMinecraftVersion() {
		Optional<ModContainer> container = FabricLoader.getInstance().getModContainer("minecraft");

		if (container.isPresent()) {
			Version version = container.get().getMetadata().getVersion();
			if (version instanceof SemanticVersion) {
				return version;
			}
		}

		return null;
	}

	private static boolean isMinecraftVersion(String versionRange) {
		try {
			Predicate<Version> predicate = VersionPredicate.parse(versionRange);
			return predicate.test(getMinecraftVersion());
		} catch (VersionParsingException exception) {
			return false;
		}
	}
}