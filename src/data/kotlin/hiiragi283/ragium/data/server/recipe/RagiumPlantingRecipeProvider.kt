package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumPlantingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Wheat
        HTItemWithFluidToChancedItemRecipeBuilder
            .planting(
                ingredientHelper.item(Tags.Items.SEEDS_WHEAT),
                ingredientHelper.water(125),
            ).addResult(resultHelper.item(Items.WHEAT, 2))
            .addResult(resultHelper.item(Items.WHEAT_SEEDS))
            .save(output)
    }
}
