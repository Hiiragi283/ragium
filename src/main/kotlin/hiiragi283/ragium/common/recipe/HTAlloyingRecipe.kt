package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTListItemRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import org.apache.commons.lang3.math.Fraction

class HTAlloyingRecipe(
    val firstIngredient: HTItemIngredient,
    val secondIngredient: HTItemIngredient,
    val thirdIngredient: HTItemIngredient?,
    val result: HTItemResult,
    time: Int,
    exp: Fraction,
) : HTProcessingRecipe<HTListItemRecipeInput>(time, exp) {
    constructor(
        ingredients: List<HTItemIngredient>,
        result: HTItemResult,
        time: Int,
        exp: Fraction,
    ) : this(ingredients[0], ingredients[1], ingredients.getOrNull(2), result, time, exp)

    override fun matches(input: HTListItemRecipeInput, level: Level): Boolean {
        val bool1: Boolean = firstIngredient.test(input.getItem(0))
        val bool2: Boolean = secondIngredient.test(input.getItem(1))
        val stack2: ItemStack = input.getItem(2)
        val bool3: Boolean = thirdIngredient?.test(stack2) ?: stack2.isEmpty
        return bool1 && bool2 && bool3
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ALLOYING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ALLOYING.get()
}
