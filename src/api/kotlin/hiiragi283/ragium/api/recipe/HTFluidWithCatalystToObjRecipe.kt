package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import java.util.Optional

interface HTFluidWithCatalystToObjRecipe<R : HTRecipeResult<*, *>> : HTRecipe<HTItemWithFluidRecipeInput> {
    val ingredient: HTFluidIngredient
    val catalyst: Optional<HTItemIngredient>
    val result: R

    override fun test(input: HTItemWithFluidRecipeInput): Boolean {
        val bool1: Boolean = ingredient.test(input.fluid)
        val bool2: Boolean = catalyst
            .map { ingredient: HTItemIngredient -> ingredient.testOnlyType(input.item) }
            .orElse(input.item.isEmpty)
        return !isIncomplete && bool1 && bool2
    }

    override fun matches(input: HTItemWithFluidRecipeInput, level: Level): Boolean = test(input)

    override fun assemble(input: HTItemWithFluidRecipeInput, registries: HolderLookup.Provider): ItemStack =
        if (test(input)) getResultItem(registries) else ItemStack.EMPTY
}
