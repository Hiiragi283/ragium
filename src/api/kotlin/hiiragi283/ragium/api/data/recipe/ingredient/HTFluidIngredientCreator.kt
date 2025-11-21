package hiiragi283.ragium.api.data.recipe.ingredient

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.registry.HTFluidHolderLike
import net.minecraft.core.HolderSet
import net.minecraft.world.level.material.Fluid

/**
 * [HTFluidIngredient]を返す[HTIngredientCreator]の拡張インターフェース
 * @see mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator
 */
interface HTFluidIngredientCreator : HTIngredientCreator<Fluid, HTFluidIngredient> {
    override fun fromSet(holderSet: HolderSet<Fluid>, amount: Int): HTFluidIngredient = HTFluidIngredient.of(holderSet, amount)

    fun fromHolder(content: HTFluidHolderLike, amount: Int): HTFluidIngredient = fromTagKey(content.getFluidTag(), amount)

    fun water(amount: Int): HTFluidIngredient = fromHolder(HTFluidHolderLike.WATER, amount)

    fun lava(amount: Int): HTFluidIngredient = fromHolder(HTFluidHolderLike.LAVA, amount)

    fun milk(amount: Int): HTFluidIngredient = fromHolder(HTFluidHolderLike.MILK, amount)
}
