package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.util.TriPredicate

/**
 * 2種類のアイテムと単一の液体から，単一のアイテムを生産するレシピ
 */
interface HTCombineRecipe :
    HTRecipe,
    TriPredicate<ImmutableItemStack, ImmutableItemStack, ImmutableFluidStack> {
    fun getLeftRequiredCount(): Int

    fun getRightRequiredCount(): Int

    fun getRequiredAmount(input: HTRecipeInput): Int

    override fun matches(input: HTRecipeInput, level: Level): Boolean {
        val left: ImmutableItemStack = input.item(0) ?: return false
        val right: ImmutableItemStack = input.item(1) ?: return false
        val fluid: ImmutableFluidStack = input.fluid(0) ?: return false
        return this.test(left, right, fluid)
    }

    override fun test(left: ImmutableItemStack, right: ImmutableItemStack, fluid: ImmutableFluidStack): Boolean
}
