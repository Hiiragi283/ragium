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

class HTPressingRecipe(
    val ingredient: HTItemIngredient,
    val catalyst: HTItemIngredient,
    val result: HTItemResult,
    time: Int,
    exp: Fraction,
) : HTProcessingRecipe<HTListItemRecipeInput>(time, exp) {
    constructor(pair: Pair<HTItemIngredient, HTItemIngredient>, result: HTItemResult, time: Int, exp: Fraction) :
        this(pair.first, pair.second, result, time, exp)

    override fun matches(input: HTListItemRecipeInput, level: Level): Boolean =
        ingredient.test(input.getItem(0)) && catalyst.testOnlyType(input.getItem(1))

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PRESSING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PRESSING.get()
}
