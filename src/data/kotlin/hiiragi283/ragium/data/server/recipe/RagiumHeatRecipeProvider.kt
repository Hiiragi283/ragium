package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTAlloyingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemAndFluidRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumHeatRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        alloying()
        melting()
    }

    //    Alloying    //

    @JvmStatic
    private fun alloying() {
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

        // Ambrosia
        HTAlloyingRecipeBuilder.create(output) {
            result = resultCreator.create(HCItems.AMBROSIA)
            ingredients += inputCreator.create(HCItems.IRIDESCENT_POWDER)
            ingredients += inputCreator.create(Items.HONEY_BLOCK, 64)
            ingredients += inputCreator.create(Items.ENCHANTED_GOLDEN_APPLE, 16)
        }

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

    //    Melting    //

    @JvmStatic
    private fun melting() {
        // Water
        HTItemOrFluidRecipeBuilder.melting(output) {
            ingredient += inputCreator.create(Items.SNOW_BLOCK)
            result += resultCreator.water(1000)
            time = 20 * 5
            recipeId suffix "_from_snow_block"
        }
        HTItemOrFluidRecipeBuilder.melting(output) {
            ingredient += inputCreator.create(Items.SNOWBALL)
            result += resultCreator.water(250)
            time = 20
            recipeId suffix "_from_snowball"
        }

        HTItemAndFluidRecipeBuilder.solidifying(output) {
            fluidIngredient = inputCreator.water(250)
            itemIngredient = inputCreator.create(HTMoldType.BALL)
            result = resultCreator.create(Items.SNOWBALL)
            time /= 4
        }
        // Lava
        HTItemOrFluidRecipeBuilder.melting(output) {
            ingredient += inputCreator.create(listOf(Tags.Items.COBBLESTONES, Tags.Items.STONES))
            result += resultCreator.lava(125)
            time = 20 * 30
            recipeId suffix "_from_stones"
        }
        HTItemOrFluidRecipeBuilder.melting(output) {
            ingredient += inputCreator.create(Tags.Items.NETHERRACKS)
            result += resultCreator.lava(125)
            recipeId suffix "_from_netherrack"
        }
        HTItemOrFluidRecipeBuilder.melting(output) {
            ingredient += inputCreator.create(Items.MAGMA_BLOCK)
            result += resultCreator.lava(250)
            recipeId suffix "_from_magma"
        }
        // Honey
        HTItemOrFluidRecipeBuilder.melting(output) {
            ingredient += inputCreator.create(Items.HONEY_BLOCK)
            result += resultCreator.create(HCFluids.HONEY)
            recipeId suffix "_from_block"
        }

        // Meat
        HTItemOrFluidRecipeBuilder.melting(output) {
            ingredient += inputCreator.create(Items.ROTTEN_FLESH)
            result += resultCreator.create(HCFluids.MEAT, HTConst.INGOT_AMOUNT)
            recipeId suffix "_from_rotten"
        }
        // Glass
        HTItemOrFluidRecipeBuilder.melting(output) {
            ingredient += inputCreator.create(Tags.Items.GLASS_PANES)
            result += resultCreator.molten(VanillaMaterialKeys.GLASS) { 375 }
            recipeId suffix "_from_pane"
        }

        // Eldritch
        for (i: Int in (0..4)) {
            HTItemOrFluidRecipeBuilder.melting(output) {
                ingredient += inputCreator.create(
                    false,
                    Items.OMINOUS_BOTTLE,
                ) { expect(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, i) }
                result += resultCreator.molten(HCMaterialKeys.ELDRITCH) { it * (i + 1) }
                recipeId suffix "/$i"
            }
        }

        // Air
        HTItemOrFluidRecipeBuilder.melting(output) {
            ingredient += inputCreator.create(Items.WIND_CHARGE)
            result += resultCreator.create(RagiumFluids.AIR, 125)
            recipeId suffix "_from_wind_charge"
        }
        HTItemOrFluidRecipeBuilder.melting(output) {
            ingredient += inputCreator.create(Tags.Items.RODS_BREEZE)
            result += resultCreator.create(RagiumFluids.AIR, 500)
            recipeId suffix "_from_breeze_rod"
        }
    }
}
