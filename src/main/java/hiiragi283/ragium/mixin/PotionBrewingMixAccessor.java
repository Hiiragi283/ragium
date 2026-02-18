package hiiragi283.ragium.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net/minecraft/world/item/alchemy/PotionBrewing$Mix")
public interface PotionBrewingMixAccessor<T> {
    @Accessor
    Holder<T> getFrom();

    @Accessor
    Ingredient getIngredient();

    @Accessor
    Holder<T> getTo();
}
