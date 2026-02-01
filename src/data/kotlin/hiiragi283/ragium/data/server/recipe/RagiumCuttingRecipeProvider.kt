package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTItemToChancedRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items

object RagiumCuttingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        wooden()
    }

    @JvmStatic
    private fun wooden() {
        // Stick
        HTItemToChancedRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(ItemTags.WOODEN_SLABS)
            result = resultCreator.create(Items.STICK, 4)
            recipeId suffix "_from_wooden_slabs"
        }

        HTItemToChancedRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(ItemTags.SAPLINGS)
            result = resultCreator.create(Items.STICK)
            recipeId suffix "_from_saplings"
        }
    }
}
