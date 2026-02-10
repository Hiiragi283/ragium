package hiiragi283.ragium.common.datagen.recipe

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.item.HTFoodCanType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import kotlin.collections.iterator

object RagiumMaterialRecipeProvider : HTRecipeProvider() {
    override fun buildRecipes() {
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
            resultStack += getOrThrow(CommonTagPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            exp = 0.7f
            recipeId suffix "_from_compound"
        }
    }

    @JvmStatic
    private fun meat() {
        // Meat Ingot -> Cooked Meat Ingot
        HTCookingRecipeBuilder.smeltingAndSmoking(output) {
            ingredient += CommonTagPrefixes.INGOT to RagiumMaterialKeys.MEAT
            resultStack += getOrThrow(CommonTagPrefixes.INGOT, RagiumMaterialKeys.COOKED_MEAT)
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

        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') += CommonTagPrefixes.INGOT to RagiumMaterialKeys.COOKED_MEAT
            define('B') += Tags.Items.BONES
            resultStack += RagiumBlocks.COOKED_MEAT_BLOCK
        }

        HTCookingRecipeBuilder.smeltingAndSmoking(output) {
            ingredient += RagiumBlocks.MEAT_BLOCK
            resultStack += RagiumBlocks.COOKED_MEAT_BLOCK
            time *= 3
            exp = 1f
            recipeId suffix "_from_raw"
        }

        // Food Cans
        for ((canType: HTFoodCanType, item: ItemLike) in RagiumItems.FOOD_CANS) {
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
            resultStack += getOrThrow(CommonTagPrefixes.DUST, RagiumMaterialKeys.STAINLESS_STEEL) to 9
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
            resultStack += getOrThrow(CommonTagPrefixes.DUST, RagiumMaterialKeys.STAINLESS_STEEL) to 9
            conditions += CommonTagPrefixes.DUST to CommonMaterialKeys.INVAR
            recipeId suffix "_from_invar"
        }
    }

    @JvmStatic
    private fun getOrThrow(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*> = HiiragiCoreAccess.INSTANCE
        .materialContents
        .getItem(prefix, material)
        ?: error("Unknown ${prefix.name} for ${material.asMaterialId()}")
}
