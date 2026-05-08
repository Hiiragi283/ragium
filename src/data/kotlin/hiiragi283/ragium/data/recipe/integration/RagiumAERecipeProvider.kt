package hiiragi283.ragium.data.recipe.integration

import appeng.core.definitions.AEItems
import appeng.datagen.providers.tags.ConventionTags
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTPrintingRecipeBuilder

data object RagiumAERecipeProvider : HTSubRecipeProvider.Integration(RagiumAPI.MOD_ID, "ae2") {
    override fun buildRecipeInternal() {
        printing()
    }

    @JvmStatic
    private fun printing() {
        HTPrintingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ConventionTags.CERTUS_QUARTZ)
            press += itemCreator.create(AEItems.CALCULATION_PROCESSOR_PRESS)
            result = resultCreator.create(AEItems.CALCULATION_PROCESSOR_PRINT)
        }
        HTPrintingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND)
            press += itemCreator.create(AEItems.ENGINEERING_PROCESSOR_PRESS)
            result = resultCreator.create(AEItems.ENGINEERING_PROCESSOR_PRINT)
        }
        HTPrintingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.GOLD)
            press += itemCreator.create(AEItems.LOGIC_PROCESSOR_PRESS)
            result = resultCreator.create(AEItems.LOGIC_PROCESSOR_PRINT)
        }
    }
}
