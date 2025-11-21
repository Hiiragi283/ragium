package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.impl.recipe.base.HTItemWithCatalystRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.Optional

class HTExtractingRecipe(ingredient: HTItemIngredient, catalyst: Optional<HTItemIngredient>, results: Ior<HTItemResult, HTFluidResult>) :
    HTItemWithCatalystRecipe(ingredient, catalyst, results) {
    override fun getRequiredCount(index: Int, stack: ImmutableItemStack): Int = when (index) {
        0 -> required.getRequiredAmount(stack)
        else -> 0
    }

    override fun test(input: HTMultiRecipeInput): Boolean {
        val stackIn: ItemStack = input.getItem(1)
        val bool1: Boolean = optional
            .map { ingredient: HTItemIngredient -> ingredient.testOnlyType(stackIn) }
            .orElse(stackIn.isEmpty)
        val bool2: Boolean = required.test(input.getItem(0))
        return bool1 && bool2
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.EXTRACTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.EXTRACTING.get()
}
