package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.HTFluidRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack

/**
 * 複数のインプット（アイテム，液体）から複数の完成品（アイテム，液体）を生産するレシピ
 */
interface HTComplexRecipe : HTFluidRecipe {
    fun getRequiredCount(index: Int, stack: ImmutableItemStack): Int

    fun getRequiredAmount(index: Int, stack: ImmutableFluidStack): Int
}
