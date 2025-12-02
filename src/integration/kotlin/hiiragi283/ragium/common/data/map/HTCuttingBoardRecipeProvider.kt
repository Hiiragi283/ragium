package hiiragi283.ragium.common.data.map

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.data.map.HTRuntimeRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.math.toFraction
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.impl.data.recipe.HTItemToChancedItemRecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult
import vectorwing.farmersdelight.common.registry.ModRecipeTypes

object HTCuttingBoardRecipeProvider : HTRuntimeRecipeProvider {
    @JvmField
    val CODEC: MapCodec<HTCuttingBoardRecipeProvider> = MapCodec.unit(HTCuttingBoardRecipeProvider)

    override fun type(): MapCodec<HTCuttingBoardRecipeProvider> = CODEC

    override fun generateRecipes(helper: HTRuntimeRecipeProvider.Helper) {
        for (holder: RecipeHolder<CuttingBoardRecipe> in helper.getAllRecipes(ModRecipeTypes.CUTTING.get())) {
            val id: ResourceLocation = holder.id()
            val recipe: CuttingBoardRecipe = holder.value()
            // Inputs
            val builder: HTItemToChancedItemRecipeBuilder = HTItemToChancedItemRecipeBuilder
                .cutting(HTItemIngredient.of(recipe.ingredients.first()))
            // Outputs
            for (result: ChanceResult in recipe.rollableResults) {
                builder.addResult(HTResultHelper.item(result.stack()), result.chance().toFraction())
            }
            builder.save(helper.output, id)
        }
    }
}
