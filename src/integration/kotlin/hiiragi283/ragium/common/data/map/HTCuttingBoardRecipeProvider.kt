package hiiragi283.ragium.common.data.map

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.data.map.HTRuntimeRecipeProvider
import hiiragi283.ragium.impl.data.recipe.HTItemToExtraItemRecipeBuilder
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
            val results: List<ChanceResult> = recipe.rollableResults

            HTItemToExtraItemRecipeBuilder
                .cutting(
                    helper.itemCreator.fromVanilla(recipe.ingredients.first()),
                    helper.resultHelper.item(results[0].stack()),
                    results.getOrNull(1)?.stack()?.let(helper.resultHelper::item),
                ).save(helper.output, id)
        }
    }
}
