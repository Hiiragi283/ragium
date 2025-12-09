package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.single.HTSingleFluidRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup

/**
 * [HTSingleFluidRecipe]の抽象クラス
 */
abstract class HTBasicSingleFluidRecipe(val ingredient: HTItemIngredient, val result: HTFluidResult) : HTSingleFluidRecipe {
    final override fun test(stack: ImmutableItemStack): Boolean = ingredient.test(stack)

    final override fun getRequiredCount(): Int = ingredient.getRequiredAmount()

    final override fun assembleFluid(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableFluidStack? =
        result.getStackOrNull(provider)
}
