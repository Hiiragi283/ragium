package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumWashingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Gravel -> Flint
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                HTIngredientHelper.INSTANCE.item(Tags.Items.GRAVELS),
                HTIngredientHelper.INSTANCE.water(250),
            ).addResult(HTResultHelper.INSTANCE.item(Items.FLINT))
            .addResult(HTResultHelper.INSTANCE.item(Items.FLINT), 1 / 2f)
            .save(output)
    }
}
