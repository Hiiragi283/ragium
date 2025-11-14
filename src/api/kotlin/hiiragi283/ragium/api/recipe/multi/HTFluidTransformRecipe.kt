package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.world.item.crafting.RecipeType

/**
 * 単一のアイテムと液体から，単一のアイテムまたは液体の完成品を生成するレシピ
 */
interface HTFluidTransformRecipe :
    HTMultiOutputsRecipe<HTItemWithFluidRecipeInput>,
    HTItemIngredient.CountGetter,
    HTFluidIngredient.AmountGetter {
    override fun getRequiredCount(index: Int, stack: ImmutableItemStack): Int = getRequiredCount(stack)

    override fun getRequiredAmount(index: Int, stack: ImmutableFluidStack): Int = getRequiredAmount(stack)

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.FLUID_TRANSFORM.get()
}
