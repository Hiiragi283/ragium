package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.registry.HTItemLike
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
            define('A') { itemCreator.create(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE) }
            define('B') { itemCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.COPPER) }
            resultStack = RagiumItems.RAGI_ALLOY_COMPOUND.toStack()
        }
        // Ragi-Alloy Compound -> Ragi-Alloy
        HTCookingRecipeBuilder.smeltingAndBlasting(output) {
            ingredient = itemCreator.create(RagiumItems.RAGI_ALLOY_COMPOUND)
            resultStack = getOrThrow(CommonParts.INGOT, RagiumMaterialKeys.RAGI_ALLOY).toStack()
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
            ingredient = itemCreator.create(CommonTagPrefixes.INGOT, RagiumMaterialKeys.MEAT)
            resultStack = RagiumItems.COOKED_MEAT_INGOT.toStack()
            exp = 0.35f
            recipeId suffix "_from_ingot"
        }
        // Meat + Bone -> Bone with Meat
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') { itemCreator.create(CommonTagPrefixes.INGOT, RagiumMaterialKeys.MEAT) }
            define('B') { itemCreator.create(Tags.Items.BONES) }
            resultStack = RagiumBlocks.MEAT_BLOCK.toStack()
        }
        HTCookingRecipeBuilder.smeltingAndSmoking(output) {
            ingredient = itemCreator.create(RagiumBlocks.MEAT_BLOCK)
            resultStack = RagiumBlocks.COOKED_MEAT_BLOCK.toStack()
            time *= 3
            exp = 1f
            recipeId suffix "_from_raw"
        }
        // Canned Cooked Meat
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') { itemCreator.create(CommonTagPrefixes.INGOT, RagiumMaterialKeys.COOKED_MEAT) }
            define('B') { itemCreator.create(CommonTagPrefixes.PLATE, VanillaMaterialKeys.IRON) }
            resultStack = RagiumItems.COOKED_MEAT_INGOT.toStack(8)
        }
    }

    @JvmStatic
    private fun stainless() {
        // Stainless
        HTShapelessRecipeBuilder.create(output) {
            repeat(6) {
                ingredients += itemCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.IRON)
            }
            repeat(2) {
                ingredients += itemCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.NICKEL)
            }
            ingredients += itemCreator.create(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGI_CRYSTAL)
            resultStack = getOrThrow(CommonParts.DUST, RagiumMaterialKeys.STAINLESS_STEEL).toStack(9)
        }
        // Stainless from Invar
        HTShapelessRecipeBuilder.create(output) {
            repeat(6) {
                ingredients += itemCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.INVAR)
            }
            repeat(2) {
                ingredients += itemCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.IRON)
                ingredients += itemCreator.create(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGI_CRYSTAL)
            }
            resultStack = getOrThrow(CommonParts.DUST, RagiumMaterialKeys.STAINLESS_STEEL).toStack(9)
            conditions += CommonTagPrefixes.DUST to CommonMaterialKeys.INVAR
            recipeId suffix "_from_invar"
        }
    }

    @JvmStatic
    private fun getOrThrow(part: HTPartLike, material: HTMaterialLike): HTItemLike<*> = HiiragiCoreAccess.INSTANCE
        .registeredContents
        .items
        .getOrThrow(part, material)
}
