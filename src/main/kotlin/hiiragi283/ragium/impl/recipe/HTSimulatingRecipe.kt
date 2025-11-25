package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.impl.recipe.base.HTBasicItemWithCatalystRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.Optional

class HTSimulatingRecipe(catalyst: HTItemIngredient, ingredient: Optional<HTItemIngredient>, results: HTComplexResult) :
    HTBasicItemWithCatalystRecipe(catalyst, ingredient, results) {
    override fun getRequiredCount(index: Int, stack: ImmutableItemStack): Int = when (index) {
        0 -> optional.map { it.getRequiredAmount(stack) }.orElse(0)
        else -> 0
    }

    override fun test(input: HTMultiRecipeInput): Boolean {
        val stackIn: ItemStack = input.getItem(0)
        val bool1: Boolean = optional
            .map { ingredient: HTItemIngredient -> ingredient.testOnlyType(stackIn) }
            .orElse(stackIn.isEmpty)
        val bool2: Boolean = required.test(input.getItem(1))
        return bool1 && bool2
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SIMULATING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.SIMULATING.get()

    override fun getRequiredCount(stack: ImmutableItemStack): Int = optional.map { it.getRequiredAmount(stack) }.orElse(0)
}
