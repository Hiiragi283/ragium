package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTDuplicatingRecipeBuilder
import net.minecraft.tags.ItemTags

object RagiumMatterRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // XX Log
        HTDuplicatingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.LOGS)
            requiredMatter = 4
        }
    }
}
