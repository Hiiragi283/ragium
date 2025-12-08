package hiiragi283.ragium.impl.data.recipe.ingredient

import hiiragi283.ragium.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.crafting.CompoundFluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

internal data object HTFluidIngredientCreatorImpl : HTFluidIngredientCreator {
    override fun from(type: Fluid, amount: Int): HTFluidIngredient = from(FluidIngredient.single(type), amount)

    override fun from(types: Collection<Fluid>, amount: Int): HTFluidIngredient =
        from(CompoundFluidIngredient.of(types.stream().map(FluidIngredient::single)), amount)

    override fun fromTagKey(tagKey: TagKey<Fluid>, amount: Int): HTFluidIngredient = from(FluidIngredient.tag(tagKey), amount)

    override fun fromTagKeys(tagKeys: Collection<TagKey<Fluid>>, amount: Int): HTFluidIngredient = when (tagKeys.size) {
        1 -> fromTagKey(tagKeys.first(), amount)
        else -> from(CompoundFluidIngredient.of(tagKeys.stream().map(FluidIngredient::tag)), amount)
    }
}
