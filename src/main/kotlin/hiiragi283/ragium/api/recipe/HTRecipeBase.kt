package hiiragi283.ragium.api.recipe

import net.minecraft.item.ItemStack
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.registry.RegistryWrapper

interface HTRecipeBase<T : RecipeInput> : Recipe<T> {
    val inputs: List<HTIngredient>
    val outputs: List<HTRecipeResult>

    fun getInput(index: Int): HTIngredient? = inputs.getOrNull(index)

    fun getOutput(index: Int): HTRecipeResult? = outputs.getOrNull(index)

    override fun craft(input: T, lookup: RegistryWrapper.WrapperLookup): ItemStack = getResult(lookup)

    override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup): ItemStack = getOutput(0)?.toStack() ?: ItemStack.EMPTY

    override fun fits(width: Int, height: Int): Boolean = true

    override fun isIgnoredInRecipeBook(): Boolean = true
}
