package hiiragi283.ragium.data.recipe.integration

import appeng.core.definitions.AEBlocks
import appeng.core.definitions.AEItems
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.data.recipe.builder.HTItemToMultiItemRecipeBuilder
import hiiragi283.core.common.material.HCIntegrationMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTCombiningRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemAndFluidToItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTPrintingRecipeBuilder
import net.minecraft.world.level.ItemLike

data object RagiumAERecipeProvider : HTSubRecipeProvider.Integration(RagiumAPI.MOD_ID, "ae2") {
    override fun buildRecipeInternal() {
        alloying()
        printing()

        // Sky Stone Dust
        HTItemToMultiItemRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(AEBlocks.SKY_STONE_BLOCK)
            results += resultCreator.material(CommonParts.DUST, HCIntegrationMaterialKeys.SKY_STONE)
            recipeId suffix "_from_stone"
        }
        // Budding Certus Quartz
        HTItemAndFluidToItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(CommonTagPrefixes.STORAGE_BLOCK, HCIntegrationMaterialKeys.CERTUS_QUARTZ)
            fluidIngredient = inputCreator.create(HiiragiCoreTags.Fluids.ELDRITCH, 810)
            result = resultCreator.create(AEBlocks.FLAWLESS_BUDDING_QUARTZ)
        }
        // Mysterious Cube
        HTItemAndFluidToItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(AEBlocks.NOT_SO_MYSTERIOUS_CUBE)
            fluidIngredient = inputCreator.create(HiiragiCoreTags.Fluids.ELDRITCH, 810)
            result = resultCreator.create(AEBlocks.MYSTERIOUS_CUBE)
        }
    }

    @JvmStatic
    private fun alloying() {
        // Fluix Crystal
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.material(CommonParts.GEM, HCIntegrationMaterialKeys.FLUIX)
            ingredients += inputCreator.create(baseOrDust(HCIntegrationMaterialKeys.CERTUS_QUARTZ))
            ingredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.QUARTZ))
        }
        // Processor
        mapOf(
            AEItems.CALCULATION_PROCESSOR to AEItems.CALCULATION_PROCESSOR_PRINT,
            AEItems.ENGINEERING_PROCESSOR to AEItems.ENGINEERING_PROCESSOR_PRINT,
            AEItems.LOGIC_PROCESSOR to AEItems.LOGIC_PROCESSOR_PRINT,
        ).forEach { (processor: ItemLike, print: ItemLike) ->
            HTCombiningRecipeBuilder.alloying(output) {
                result = resultCreator.create(processor)
                ingredients += inputCreator.create(print)
                ingredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
                ingredients += inputCreator.create(AEItems.SILICON_PRINT)
            }
        }
    }

    @JvmStatic
    private fun printing() {
        HTPrintingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, HCIntegrationMaterialKeys.CERTUS_QUARTZ)
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
        HTPrintingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(HiiragiCoreTags.Items.SILICON)
            press += itemCreator.create(AEItems.SILICON_PRESS)
            result = resultCreator.create(AEItems.SILICON_PRINT)
        }
    }
}
