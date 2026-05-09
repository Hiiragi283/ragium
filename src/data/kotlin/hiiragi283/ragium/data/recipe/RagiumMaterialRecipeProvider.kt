package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTItemToMultiItemRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.Tags

object RagiumMaterialRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        raginite()
        meat()
        stainless()
    }

    @JvmStatic
    private fun raginite() {
        // Raginite + Copper -> Ragi-Alloy Compound
        HTShapedRecipeBuilder.create(output) {
            hollow4()
            define('A') += CommonTagPrefixes.DUST to RagiumMaterialKeys.RAGINITE
            define('B') += CommonTagPrefixes.INGOT to VanillaMaterialKeys.COPPER
            resultStack += RagiumItems.RAGI_ALLOY_COMPOUND
        }
        // Ragi-Alloy Compound -> Ragi-Alloy
        HTCookingRecipeBuilder.smeltingAndBlasting(output) {
            ingredient += RagiumItems.RAGI_ALLOY_COMPOUND
            resultStack += getOrThrow(CommonParts.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            exp = 0.7f
            recipeId suffix "_from_compound"
        }
    }

    @JvmStatic
    private fun meat() {
        // Raw Meat -> Minced Meat
        HTItemToMultiItemRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(listOf(Tags.Items.FOODS_RAW_MEAT, Tags.Items.FOODS_RAW_FISH))
            results += resultCreator.material(CommonParts.DUST, RagiumMaterialKeys.MEAT)
        }
        // Minced Meat -> Meat Ingot
        RagiumRecipeBuilder.compressing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, RagiumMaterialKeys.MEAT)
            result = resultCreator.material(CommonParts.INGOT, RagiumMaterialKeys.MEAT)
        }
        // Meat Ingot -> Cooked Meat Ingot
        HTCookingRecipeBuilder.smeltingAndSmoking(output) {
            ingredient += CommonTagPrefixes.INGOT to RagiumMaterialKeys.MEAT
            resultStack += RagiumItems.COOKED_MEAT_INGOT
            exp = 0.35f
            recipeId suffix "_from_ingot"
        }
        // Meat + Bone -> Bone with Meat
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') += CommonTagPrefixes.INGOT to RagiumMaterialKeys.MEAT
            define('B') += Tags.Items.BONES
            resultStack += RagiumBlocks.MEAT_BLOCK
        }
        HTCookingRecipeBuilder.smeltingAndSmoking(output) {
            ingredient += RagiumBlocks.MEAT_BLOCK
            resultStack += RagiumBlocks.COOKED_MEAT_BLOCK
            time *= 3
            exp = 1f
            recipeId suffix "_from_raw"
        }
        // Canned Cooked Meat
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') += CommonTagPrefixes.INGOT to RagiumMaterialKeys.COOKED_MEAT
            define('B') += CommonTagPrefixes.PLATE to VanillaMaterialKeys.IRON
            resultStack += RagiumItems.COOKED_MEAT_INGOT to 8
        }
    }

    @JvmStatic
    private fun stainless() {
        // Stainless
        HTShapelessRecipeBuilder.create(output) {
            repeat(6) {
                ingredients += CommonTagPrefixes.DUST to VanillaMaterialKeys.IRON
            }
            repeat(2) {
                ingredients += CommonTagPrefixes.DUST to CommonMaterialKeys.NICKEL
            }
            ingredients += CommonTagPrefixes.DUST to RagiumMaterialKeys.RAGI_CRYSTAL
            resultStack += getOrThrow(CommonParts.DUST, RagiumMaterialKeys.STAINLESS_STEEL) to 9
        }
        // Stainless from Invar
        HTShapelessRecipeBuilder.create(output) {
            repeat(6) {
                ingredients += CommonTagPrefixes.DUST to CommonMaterialKeys.INVAR
            }
            repeat(2) {
                ingredients += CommonTagPrefixes.DUST to VanillaMaterialKeys.IRON
                ingredients += CommonTagPrefixes.DUST to RagiumMaterialKeys.RAGI_CRYSTAL
            }
            resultStack += getOrThrow(CommonParts.DUST, RagiumMaterialKeys.STAINLESS_STEEL) to 9
            conditions += CommonTagPrefixes.DUST to CommonMaterialKeys.INVAR
            recipeId suffix "_from_invar"
        }
    }

    @JvmStatic
    private fun getOrThrow(part: HTPartLike, material: HTMaterialLike): Item = HiiragiCoreAccess.INSTANCE
        .registeredContents
        .items
        .getOrThrow(part, material)
        .get()
}
