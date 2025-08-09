package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import net.neoforged.neoforge.common.Tags

object RagiumCompressingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        // Coal -> Diamond
        HTItemToObjRecipeBuilder
            .compressing(
                HTIngredientHelper.item(HTIngredientHelper.coal(), 64),
                HTResultHelper.item(Tags.Items.GEMS_DIAMOND),
            ).saveSuffixed(output, "_from_coal")
    }
}
