package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTItemWithCatalystRecipe
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.impl.recipe.base.HTBasicSingleOutputRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.*

class HTCompressingRecipe(
    override val required: HTItemIngredient,
    override val optional: Optional<HTItemIngredient>,
    result: HTItemResult,
) : HTBasicSingleOutputRecipe<HTMultiRecipeInput>(result),
    HTItemWithCatalystRecipe {
    override fun test(input: HTMultiRecipeInput): Boolean =
        required.test(input.getItem(0)) && (optional.isEmpty || optional.get().test(input.getItem(1)))

    override fun isIncomplete(): Boolean {
        val bool1: Boolean = required.hasNoMatchingStacks()
        val bool2: Boolean = optional.isPresent && optional.get().hasNoMatchingStacks()
        val bool3: Boolean = result.hasNoMatchingStack()
        return bool1 || bool2 || bool3
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.COMPRESSING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.COMPRESSING.get()

    override fun getRequiredCount(stack: ImmutableItemStack): Int = required.getRequiredAmount(stack)
}
