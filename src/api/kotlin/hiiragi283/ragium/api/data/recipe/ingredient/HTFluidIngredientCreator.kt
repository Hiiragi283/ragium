package hiiragi283.ragium.api.data.recipe.ingredient

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.registry.HTFluidHolderLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

/**
 * [HTFluidIngredient]を返す[HTIngredientCreator]の拡張インターフェース
 * @see mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator
 */
interface HTFluidIngredientCreator : HTIngredientCreator<Fluid, HTFluidIngredient> {
    fun from(ingredient: FluidIngredient, amount: Int): HTFluidIngredient = HTFluidIngredient(ingredient, amount)

    fun fromHolder(content: HTFluidHolderLike, amount: Int): HTFluidIngredient = fromTagKey(content.getFluidTag(), amount)

    fun water(amount: Int): HTFluidIngredient = fromHolder(HTFluidHolderLike.WATER, amount)

    fun lava(amount: Int): HTFluidIngredient = fromHolder(HTFluidHolderLike.LAVA, amount)

    fun milk(amount: Int): HTFluidIngredient = fromHolder(HTFluidHolderLike.MILK, amount)
}
