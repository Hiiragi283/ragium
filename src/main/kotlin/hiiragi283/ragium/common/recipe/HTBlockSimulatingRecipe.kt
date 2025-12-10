package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.common.recipe.base.HTBasicSimulatingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderSet
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import java.util.Optional

class HTBlockSimulatingRecipe(ingredient: Optional<HTItemIngredient>, catalyst: HolderSet<Block>, results: HTComplexResult) :
    HTBasicSimulatingRecipe<HolderSet<Block>>(ingredient, catalyst, results) {
    override fun testCatalyst(input: HTRecipeInput, level: Level): Boolean = input.pos
        ?.below()
        ?.let(level::getBlockState)
        ?.`is`(catalyst) ?: false

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SIMULATING_BLOCK
}
