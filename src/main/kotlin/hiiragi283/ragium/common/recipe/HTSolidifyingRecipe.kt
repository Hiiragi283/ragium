package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import org.apache.commons.lang3.math.Fraction

class HTSolidifyingRecipe(
    val ingredient: HTFluidIngredient,
    val catalyst: HTItemIngredient,
    val result: HTItemResult,
    time: Int,
    exp: Fraction,
) : HTProcessingRecipe(time, exp) {
    constructor(pair: Pair<HTFluidIngredient, HTItemIngredient>, result: HTItemResult, time: Int, exp: Fraction) :
        this(pair.first, pair.second, result, time, exp)

    override fun matches(input: HTRecipeInput, level: Level): Boolean = input.testFluid(0, ingredient) && input.testCatalyst(0, catalyst)

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SOLIDIFYING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.SOLIDIFYING.get()
}
