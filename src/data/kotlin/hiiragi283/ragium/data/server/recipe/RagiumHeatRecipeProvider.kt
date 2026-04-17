package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTCombiningRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMeltingRecipeBuilder
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
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.material(CommonParts.INGOT, VanillaMaterialKeys.NETHERITE, 2)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.GOLD), 4)
            ingredients += inputCreator.create(Items.NETHERITE_SCRAP, 4)
        }

        // Steel from Coal
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.material(CommonParts.INGOT, CommonMaterialKeys.STEEL)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.IRON))
            ingredients += inputCreator.create(listOf(VanillaMaterialKeys.COAL, VanillaMaterialKeys.CHARCOAL).flatMap(::baseOrDust), 2)
            recipeId suffix "_from_coal"
        }
        // Steel from Coke
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.material(CommonParts.INGOT, CommonMaterialKeys.STEEL)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.IRON))
            ingredients += inputCreator.create(baseOrDust(CommonMaterialKeys.COAL_COKE))
            recipeId suffix "_from_coke"
        }
        // Invar
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.material(CommonParts.INGOT, CommonMaterialKeys.INVAR, 3)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.IRON), 2)
            ingredients += inputCreator.create(baseOrDust(CommonMaterialKeys.NICKEL))
            conditions += CommonTagPrefixes.INGOT.itemTagKey(CommonMaterialKeys.INVAR)
        }
        // Electrum
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.material(CommonParts.INGOT, CommonMaterialKeys.ELECTRUM, 2)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.GOLD))
            ingredients += inputCreator.create(baseOrDust(CommonMaterialKeys.SILVER))
            conditions += CommonTagPrefixes.INGOT.itemTagKey(CommonMaterialKeys.ELECTRUM)
        }
        // Bronze
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.material(CommonParts.INGOT, CommonMaterialKeys.BRONZE, 4)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.COPPER), 3)
            ingredients += inputCreator.create(baseOrDust(CommonMaterialKeys.TIN))
        }
        // Brass
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.material(CommonParts.INGOT, CommonMaterialKeys.BRASS, 2)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.COPPER))
            ingredients += inputCreator.create(baseOrDust(CommonMaterialKeys.ZINC))
        }
        // Constantan
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.material(CommonParts.INGOT, CommonMaterialKeys.CONSTANTAN, 2)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.COPPER))
            ingredients += inputCreator.create(baseOrDust(CommonMaterialKeys.NICKEL))
            conditions += CommonTagPrefixes.INGOT.itemTagKey(CommonMaterialKeys.CONSTANTAN)
        }

        // Amethyst + Lapis -> Azure Shard
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.material(CommonParts.GEM, HCMaterialKeys.AZURE, 2)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.AMETHYST))
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.LAPIS))
        }
        // Azure Shard + Iron -> Azure Steel
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.material(CommonParts.INGOT, HCMaterialKeys.AZURE_STEEL)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.IRON))
            ingredients += inputCreator.create(baseOrDust(HCMaterialKeys.AZURE), 2)
        }

        // Raginite + Copper -> Ragi-Alloy
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.material(CommonParts.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.COPPER))
            ingredients += inputCreator.create(baseOrDust(RagiumMaterialKeys.RAGINITE), 2)
        }
        // Ragi-Alloy + Glowstone -> Adv Ragi-Alloy
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.material(CommonParts.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)
            ingredients += inputCreator.create(baseOrDust(RagiumMaterialKeys.RAGI_ALLOY))
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.GLOWSTONE), 2)
        }
        // Raginite + Diamond -> Ragi-Crystal
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.material(CommonParts.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.DIAMOND))
            ingredients += inputCreator.create(baseOrDust(RagiumMaterialKeys.RAGINITE), 8)
        }
    }

    //    Melting    //

    @JvmStatic
    private fun melting() {
        // Water
        HTMeltingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.SNOW_BLOCK)
            result = resultCreator.water(1000)
            time = 20 * 5
            recipeId suffix "_from_snow_block"
        }
        HTMeltingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.SNOWBALL)
            result = resultCreator.water(250)
            time = 20
            recipeId suffix "_from_snowball"
        }
        // Lava
        HTMeltingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(listOf(Tags.Items.COBBLESTONES, Tags.Items.STONES))
            result = resultCreator.lava(125)
            time = 20 * 30
            recipeId suffix "_from_stones"
        }
        HTMeltingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.NETHERRACKS)
            result = resultCreator.lava(125)
            recipeId suffix "_from_netherrack"
        }
        HTMeltingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.MAGMA_BLOCK)
            result = resultCreator.lava(250)
            recipeId suffix "_from_magma"
        }
        // Honey
        HTMeltingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.HONEY_BLOCK)
            result = resultCreator.create(HCFluids.HONEY)
            recipeId suffix "_from_block"
        }

        // Meat
        HTMeltingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.ROTTEN_FLESH)
            result = resultCreator.create(HCFluids.MEAT, HTConst.INGOT_AMOUNT)
            recipeId suffix "_from_rotten"
        }
        // Glass
        HTMeltingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.GLASS_PANES)
            result = resultCreator.molten(VanillaMaterialKeys.GLASS) { 375 }
            recipeId suffix "_from_pane"
        }

        // Eldritch
        for (i: Int in (0..4)) {
            HTMeltingRecipeBuilder.create(output) {
                ingredient = inputCreator.create(
                    false,
                    Items.OMINOUS_BOTTLE,
                ) { expect(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, i) }
                result = resultCreator.create(HCFluids.OMINOUS_FLUX, HTConst.INGOT_AMOUNT * (i + 1))
                recipeId suffix "_$i"
            }
        }

        // Cinnabar -> Mercury
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.create(RagiumFluids.MERCURY.bucketHolder)
            ingredients += inputCreator.create(baseOrDust(CommonMaterialKeys.CINNABAR), 8)
            ingredients += inputCreator.create(Tags.Items.BUCKETS_EMPTY)
        }
        HTMeltingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(baseOrDust(CommonMaterialKeys.CINNABAR))
            result = resultCreator.create(RagiumFluids.MERCURY, 125)
            recipeId suffix "_from_cinnabar"
        }
        // Ragi-Matter
        HTMeltingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(RagiumItems.RAGI_MATTER)
            result = resultCreator.create(RagiumFluids.RAGI_MATTER, 125)
            time *= 10
        }
    }
}
