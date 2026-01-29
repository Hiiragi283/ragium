package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTPyrolyzingRecipeBuilder
import hiiragi283.ragium.common.item.HTFoodCanType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumMaterialRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Crimson Stem -> Crimson Blood
        HTPyrolyzingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.CRIMSON_STEMS, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
            fluidResult = resultCreator.molten(HCMaterialKeys.CRIMSON_CRYSTAL)
            recipeId suffix "_from_crimson_stem"
        }
        // Warped Stem -> Dew of the Warp
        HTPyrolyzingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.WARPED_STEMS, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
            fluidResult = resultCreator.molten(HCMaterialKeys.WARPED_CRYSTAL)
            recipeId suffix "_from_warped_stem"
        }

        raginite()
        meat()
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
    private fun getOrThrow(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*> =
        HiiragiCoreAccess.INSTANCE.materialContents.getItemOrThrow(prefix, material)
}
