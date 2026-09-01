package hiiragi283.ragium.mixin;

import hiiragi283.lib.resource.HTKeyOrValue;
import hiiragi283.lib.util.Ior;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(Holder.Reference.class)
public abstract class HolderReferenceMixin<T> implements HTKeyOrValue<T, T> {
    @Shadow private ResourceKey<T> key;

    @Shadow private T value;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public @NotNull Ior<@NotNull ResourceKey<T>, T> unwrapWithKey() {
        return Objects.requireNonNull(
                Ior.fromNullable(key, value), "Either key or value required!");
    }
}
