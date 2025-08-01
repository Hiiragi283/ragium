package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import java.util.*

abstract class HTFluidToObjRecipe(
    private val recipeType: RecipeType<*>,
    val ingredient: HTFluidIngredient,
    val itemResult: Optional<HTItemResult>,
    val fluidResults: List<HTFluidResult>,
) : HTRecipe<HTSingleFluidRecipeInput> {
    override fun test(input: HTSingleFluidRecipeInput): Boolean = !isIncomplete && ingredient.test(input.fluid)

    override fun isIncomplete(): Boolean {
        val bool1: Boolean = ingredient.hasNoMatchingStacks()
        val bool2: Boolean = itemResult.map(HTItemResult::hasNoMatchingStack).orElse(false)
        val bool3: Boolean = fluidResults.isEmpty()
        val bool4: Boolean = fluidResults.all(HTFluidResult::hasNoMatchingStack)
        return bool1 || bool2 || bool3 || bool4
    }

    override fun matches(input: HTSingleFluidRecipeInput, level: Level): Boolean = test(input)

    override fun assemble(input: HTSingleFluidRecipeInput, registries: HolderLookup.Provider): ItemStack =
        if (test(input)) getResultItem(registries) else ItemStack.EMPTY

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = itemResult.map(HTItemResult::get).orElse(ItemStack.EMPTY)

    override fun getType(): RecipeType<*> = recipeType
}
