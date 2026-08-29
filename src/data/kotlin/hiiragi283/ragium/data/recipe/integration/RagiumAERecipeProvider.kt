package hiiragi283.ragium.data.recipe.integration

import appeng.core.definitions.AEBlocks
import appeng.core.definitions.AEItems
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.data.recipe.HCRecipeBuilders
import hiiragi283.core.common.integration.HCIConstants
import hiiragi283.core.common.material.HCIntegrationMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTAlloyingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.level.ItemLike

class RagiumAERecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider.Integration(packOutput, future, RagiumAPI.MOD_ID, HCIConstants.AE2) {
    override fun buildRecipes() {
        alloying()
        printing()

        // Sky Stone Dust
        HCRecipeBuilders.crushing {
            ingredient { +AEBlocks.SKY_STONE_BLOCK }
            result { +HTItemResult.MaterialPartEntry(CommonParts.DUST, HCIntegrationMaterialKeys.SKY_STONE) }
            recipeId suffix "_from_stone"
            condition { +condition }
        }.save(exporter)
        // Budding Certus Quartz
        RagiumRecipeBuilder.bathing {
            itemIngredient { +tag(CommonTagPrefixes.STORAGE_BLOCK, HCIntegrationMaterialKeys.CERTUS_QUARTZ) }
            fluidIngredient {
                +HiiragiCoreTags.Fluids.ELDRITCH
                amount = 810
            }
            result { +AEBlocks.FLAWLESS_BUDDING_QUARTZ }
            condition { +condition }
        }.save(exporter)
        // Mysterious Cube
        RagiumRecipeBuilder.bathing {
            itemIngredient { +AEBlocks.NOT_SO_MYSTERIOUS_CUBE }
            fluidIngredient {
                +HiiragiCoreTags.Fluids.ELDRITCH
                amount = 810
            }
            result { +AEBlocks.MYSTERIOUS_CUBE }
            condition { +condition }
        }.save(exporter)
    }

    override fun getName(): String = "AE Recipes"

    private fun alloying() {
        // Fluix Crystal
        HTAlloyingRecipeBuilder.create {
            result { +HTItemResult.MaterialPartEntry(CommonParts.GEM, HCIntegrationMaterialKeys.FLUIX) }
            ingredient { +baseOrDust(HCIntegrationMaterialKeys.CERTUS_QUARTZ) }
            ingredient { +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE) }
            ingredient { +baseOrDust(VanillaMaterialKeys.QUARTZ) }
            condition { +condition }
        }.save(exporter)
        // Processor
        mapOf(
            AEItems.CALCULATION_PROCESSOR to AEItems.CALCULATION_PROCESSOR_PRINT,
            AEItems.ENGINEERING_PROCESSOR to AEItems.ENGINEERING_PROCESSOR_PRINT,
            AEItems.LOGIC_PROCESSOR to AEItems.LOGIC_PROCESSOR_PRINT,
        ).forEach { (processor: ItemLike, print: ItemLike) ->
            HTAlloyingRecipeBuilder.create {
                result { +processor }
                ingredient { +print }
                ingredient { +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE) }
                ingredient { +AEItems.SILICON_PRINT }
                condition { +condition }
            }.save(exporter)
        }
    }

    private fun printing() {
        RagiumRecipeBuilder.printing {
            primary { +tag(CommonTagPrefixes.GEM, HCIntegrationMaterialKeys.CERTUS_QUARTZ) }
            secondary { +AEItems.CALCULATION_PROCESSOR_PRESS }
            result { +AEItems.CALCULATION_PROCESSOR_PRINT }
            condition { +condition }
        }.save(exporter)
        RagiumRecipeBuilder.printing {
            primary { +tag(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND) }
            secondary { +AEItems.ENGINEERING_PROCESSOR_PRESS }
            result { +AEItems.ENGINEERING_PROCESSOR_PRINT }
            condition { +condition }
        }.save(exporter)
        RagiumRecipeBuilder.printing {
            primary { +tag(CommonTagPrefixes.INGOT, VanillaMaterialKeys.GOLD) }
            secondary { +AEItems.LOGIC_PROCESSOR_PRESS }
            result { +AEItems.LOGIC_PROCESSOR_PRINT }
            condition { +condition }
        }.save(exporter)
        RagiumRecipeBuilder.printing {
            primary { +HiiragiCoreTags.Items.SILICON }
            secondary { +AEItems.SILICON_PRESS }
            result { +AEItems.SILICON_PRINT }
            condition { +condition }
        }.save(exporter)
    }
}
