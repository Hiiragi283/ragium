package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.HTFluidRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 単一のアイテムまたは液体の完成品を生成するレシピ
 */
interface HTMultiOutputsRecipe<INPUT : RecipeInput> : HTFluidRecipe<INPUT> {
    fun getRequiredCount(index: Int, stack: ImmutableItemStack): Int

    fun getRequiredAmount(index: Int, stack: ImmutableFluidStack): Int
}
