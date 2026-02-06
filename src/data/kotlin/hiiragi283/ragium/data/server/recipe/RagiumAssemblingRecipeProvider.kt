package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTAssemblingRecipeBuilder

object RagiumAssemblingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Platinum Group Dusts + Eldritch Flux -> Iridescent Powder
        HTAssemblingRecipeBuilder.create(output) {
            listOf(
                CommonMaterialKeys.RUTHENIUM,
                CommonMaterialKeys.RHODIUM,
                CommonMaterialKeys.PALLADIUM,
                CommonMaterialKeys.OSMIUM,
                CommonMaterialKeys.IRIDIUM,
                CommonMaterialKeys.PLATINUM,
            ).map { inputCreator.create(CommonTagPrefixes.INGOT, it, 64) }
                .forEach(itemIngredients::add)
            fluidIngredient = inputCreator.molten(HCMaterialKeys.ELDRITCH) { it * 63 }
            result = resultCreator.create(HCItems.IRIDESCENT_POWDER)
            time *= 10
        }
    }
}
