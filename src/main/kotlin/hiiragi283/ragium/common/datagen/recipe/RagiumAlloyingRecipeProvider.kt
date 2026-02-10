package hiiragi283.ragium.common.datagen.recipe

import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.common.data.recipe.HTAlloyingRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items

object RagiumAlloyingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipes() {
        common()
        hiiragiCore()
        ragium()
    }

    @JvmStatic
    private fun common() {
        // Netherite
        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.material(CommonTagPrefixes.INGOT, VanillaMaterialKeys.NETHERITE, 2)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.GOLD), 4)
            ingredients += inputCreator.create(CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 4)
        }

        // Steel from Coal
        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.material(CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.IRON))
            ingredients += inputCreator.create(listOf(VanillaMaterialKeys.COAL, VanillaMaterialKeys.CHARCOAL).flatMap(::baseOrDust), 2)
            recipeId suffix "_from_coal"
        }
        // Steel from Coke
        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.material(CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.IRON))
            ingredients += inputCreator.create(baseOrDust(CommonMaterialKeys.COAL_COKE))
            recipeId suffix "_from_coke"
        }
        // Invar
        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.material(CommonTagPrefixes.INGOT, CommonMaterialKeys.INVAR, 3)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.IRON), 2)
            ingredients += inputCreator.create(baseOrDust(CommonMaterialKeys.NICKEL))
            conditions += CommonTagPrefixes.INGOT.itemTagKey(CommonMaterialKeys.INVAR)
        }
        // Electrum
        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.material(CommonTagPrefixes.INGOT, CommonMaterialKeys.ELECTRUM, 2)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.GOLD))
            ingredients += inputCreator.create(baseOrDust(CommonMaterialKeys.SILVER))
            conditions += CommonTagPrefixes.INGOT.itemTagKey(CommonMaterialKeys.ELECTRUM)
        }
        // Bronze
        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.material(CommonTagPrefixes.INGOT, CommonMaterialKeys.BRONZE, 4)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.COPPER), 3)
            ingredients += inputCreator.create(baseOrDust(CommonMaterialKeys.TIN))
        }
        // Brass
        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.material(CommonTagPrefixes.INGOT, CommonMaterialKeys.BRASS, 2)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.COPPER))
            ingredients += inputCreator.create(baseOrDust(CommonMaterialKeys.ZINC))
        }
        // Constantan
        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.material(CommonTagPrefixes.INGOT, CommonMaterialKeys.CONSTANTAN, 2)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.COPPER))
            ingredients += inputCreator.create(baseOrDust(CommonMaterialKeys.NICKEL))
            conditions += CommonTagPrefixes.INGOT.itemTagKey(CommonMaterialKeys.CONSTANTAN)
        }
    }

    @JvmStatic
    private fun hiiragiCore() {
        // Amethyst + Lapis -> Azure Shard
        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.material(CommonTagPrefixes.GEM, HCMaterialKeys.AZURE, 2)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.AMETHYST))
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.LAPIS))
        }
        // Azure Shard + Iron -> Azure Steel
        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.material(CommonTagPrefixes.INGOT, HCMaterialKeys.AZURE_STEEL)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.IRON))
            ingredients += inputCreator.create(baseOrDust(HCMaterialKeys.AZURE), 2)
        }
        // Rubber Bar
        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.material(CommonTagPrefixes.INGOT, CommonMaterialKeys.RUBBER, 2)
            ingredients += inputCreator.create(HCItems.RAW_RUBBER)
            ingredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
            recipeId suffix "_with_sulfur"
        }

        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.material(CommonTagPrefixes.INGOT, CommonMaterialKeys.RUBBER, 4)
            ingredients += inputCreator.create(HCItems.RAW_RUBBER)
            ingredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
            ingredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.CARBON)
            recipeId suffix "_with_sulfur_and_carbon"
        }

        // Ambrosia
        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.create(HCItems.AMBROSIA)
            ingredients += inputCreator.create(HCItems.IRIDESCENT_POWDER)
            ingredients += inputCreator.create(Items.HONEY_BLOCK, 64)
            ingredients += inputCreator.create(Items.ENCHANTED_GOLDEN_APPLE, 16)
        }
    }

    @JvmStatic
    private fun ragium() {
        // Raginite + Copper -> Ragi-Alloy
        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.material(CommonTagPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.COPPER))
            ingredients += inputCreator.create(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 2)
        }
        // Ragi-Alloy + Glowstone -> Adv Ragi-Alloy
        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.material(CommonTagPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)
            ingredients += inputCreator.create(baseOrDust(RagiumMaterialKeys.RAGI_ALLOY))
            ingredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE, 2)
        }

        // Quartz + Plastic -> Circuit Board
        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.create(RagiumItems.CIRCUIT_BOARD)
            ingredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.QUARTZ)
            ingredients += inputCreator.create(CommonTagPrefixes.PLATE, CommonMaterialKeys.PLASTIC)
        }
        // Circuit Board + Gold Plate -> Plated
        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.create(RagiumItems.PLATED_CIRCUIT_BOARD)
            ingredients += inputCreator.create(RagiumItems.CIRCUIT_BOARD)
            ingredients += inputCreator.create(CommonTagPrefixes.PLATE, VanillaMaterialKeys.GOLD)
        }
    }
}
