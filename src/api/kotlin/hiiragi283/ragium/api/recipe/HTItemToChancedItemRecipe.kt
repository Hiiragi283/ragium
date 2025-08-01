package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level

abstract class HTItemToChancedItemRecipe(
    private val recipeType: RecipeType<*>,
    val ingredient: HTItemIngredient,
    val results: List<HTItemResult>,
    val chances: List<Float>,
) : HTRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val RANDOM: RandomSource = RandomSource.create()
    }

    val resultMap: Map<HTItemResult, Float> = results
        .mapIndexed { index: Int, result: HTItemResult ->
            result to (chances.getOrNull(index) ?: 1f)
        }.toMap()

    fun getChancedResult(index: Int): ItemStack {
        val chance: Float = chances.getOrNull(index) ?: 1f
        var result: ItemStack = ItemStack.EMPTY
        if (chance > RANDOM.nextFloat()) {
            results.getOrNull(index)?.get()?.let { result = it }
        }
        return result
    }

    override fun test(input: SingleRecipeInput): Boolean = !isIncomplete && ingredient.test(input.item())

    override fun isIncomplete(): Boolean =
        ingredient.hasNoMatchingStacks() || results.isEmpty() || results.all(HTItemResult::hasNoMatchingStack)

    override fun matches(input: SingleRecipeInput, level: Level): Boolean = test(input)

    override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack =
        if (test(input)) getResultItem(registries) else ItemStack.EMPTY

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = getChancedResult(0)

    override fun getType(): RecipeType<*> = recipeType
}
