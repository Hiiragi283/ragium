package hiiragi283.ragium.impl.data.recipe.ingredient

import hiiragi283.ragium.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.holdersets.OrHolderSet

internal class HTFluidIngredientCreatorImpl(private val getter: HolderGetter<Fluid>) : HTFluidIngredientCreator {
    @Suppress("DEPRECATION")
    private fun holder(fluid: Fluid): Holder<Fluid> = fluid.builtInRegistryHolder()

    override fun from(type: Fluid, amount: Int): HTFluidIngredient = from(listOf(type), amount)

    override fun from(types: Collection<Fluid>, amount: Int): HTFluidIngredient = fromSet(HolderSet.direct(::holder, types), amount)

    override fun fromTagKey(tagKey: TagKey<Fluid>, amount: Int): HTFluidIngredient = fromSet(getter.getOrThrow(tagKey), amount)

    override fun fromTagKeys(tagKeys: Collection<TagKey<Fluid>>, amount: Int): HTFluidIngredient = when (tagKeys.size) {
        1 -> fromTagKey(tagKeys.first(), amount)
        else -> fromSet(OrHolderSet(tagKeys.map(getter::getOrThrow)), amount)
    }

    private fun fromSet(holderSet: HolderSet<Fluid>, amount: Int): HTFluidIngredient = HTFluidIngredient.of(holderSet, amount)
}
