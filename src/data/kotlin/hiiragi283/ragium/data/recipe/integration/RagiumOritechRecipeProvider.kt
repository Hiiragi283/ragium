package hiiragi283.ragium.data.recipe.integration

import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.integration.HCIConstants
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import rearth.oritech.init.FluidContent
import rearth.oritech.init.ItemContent

class RagiumOritechRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider.Integration(packOutput, future, RagiumAPI.MOD_ID, HCIConstants.ORITECH) {
    override fun buildRecipes() {
        // Reinforced Carbon Sheet
        RagiumRecipeBuilder.assembling {
            primary {
                +tag(CommonTagPrefixes.PLATE, CommonMaterialKeys.CARBON)
                count = 2
            }
            secondary { +tag(CommonTagPrefixes.PLATE, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) }
            result { +ItemContent.REINFORCED_CARBON_SHEET }
        }.save(exporter)
        // Silicon Wafer
        RagiumRecipeBuilder.bathing {
            itemIngredient { +tag(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGI_CRYSTAL) }
            fluidIngredient { +FluidContent.STILL_SILICON_WASH.get() }
            result { +RagiumItems.SILICON_WAFER }
            recipeId suffix "_from_wash"
        }.save(exporter)
        // Raw biopolymer
        RagiumRecipeBuilder.bathing {
            itemIngredient { +setOf(ItemContent.SOLID_BIOFUEL, ItemContent.PACKED_WHEAT) }
            fluidIngredient {
                water()
                amount = 250
            }
            result { +ItemContent.RAW_BIOPOLYMER }
        }.save(exporter)
        // Unholy Intelligence
        RagiumRecipeBuilder.bathing {
            itemIngredient { +ItemContent.DUBIOS_CONTAINER }
            fluidIngredient { +HiiragiCoreTags.Fluids.ELDRITCH }
            result { +ItemContent.UNHOLY_INTELLIGENCE }
        }.save(exporter)
    }

    override fun getName(): String = "Oritech Recipes"
}
