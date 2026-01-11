package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTChancedRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items

object RagiumCuttingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        wooden()
    }

    @JvmStatic
    private fun wooden() {
        // Stick
        HTChancedRecipeBuilder
            .cutting(
                itemCreator.fromTagKey(ItemTags.WOODEN_SLABS),
                itemResult.create(Items.STICK, 4),
            ).saveSuffixed(output, "_from_wooden_slabs")

        HTChancedRecipeBuilder
            .cutting(
                itemCreator.fromTagKey(ItemTags.SAPLINGS),
                itemResult.create(Items.STICK),
            ).saveSuffixed(output, "_from_saplings")
    }
}
