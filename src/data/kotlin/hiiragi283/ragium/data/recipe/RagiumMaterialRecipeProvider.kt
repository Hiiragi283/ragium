package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.HCRecipeBuilders
import hiiragi283.core.common.data.recipe.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.Tags

class RagiumMaterialRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        raginite()
        meat()
        stainless()
    }

    private fun raginite() {
        // Raginite + Copper -> Ragi-Alloy Compound
        HTShapedRecipeBuilder.create {
            hollow4()
            define('A') { +tag(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE) }
            define('B') { +tag(CommonTagPrefixes.INGOT, VanillaMaterialKeys.COPPER) }
            +RagiumItems.RAGI_ALLOY_COMPOUND.toStack()
        }.save(exporter)
        // Ragi-Alloy Compound -> Ragi-Alloy
        useItem(CommonParts.INGOT, RagiumMaterialKeys.RAGI_ALLOY) {
            HTCookingRecipeBuilder.smeltingAndBlasting {
                ingredient { +RagiumItems.RAGI_ALLOY_COMPOUND }
                +it.toStack()
                exp = 0.7f
                recipeId suffix "_from_compound"
            }.forEach { it.save(exporter) }
        }
    }

    private fun meat() {
        // Raw Meat -> Minced Meat
        HCRecipeBuilders.crushing {
            ingredient { +listOf(Tags.Items.FOODS_RAW_MEAT, Tags.Items.FOODS_RAW_FISH) }
            +HTItemResult.MaterialPart(CommonParts.DUST, RagiumMaterialKeys.MEAT)
        }.save(exporter)
        // Minced Meat -> Meat Ingot
        RagiumRecipeBuilder.compressing {
            ingredient { +tag(CommonTagPrefixes.DUST, RagiumMaterialKeys.MEAT) }
            +HTItemResult.MaterialPart(CommonParts.INGOT, RagiumMaterialKeys.MEAT)
        }.save(exporter)
        // Meat Ingot -> Cooked Meat Ingot
        HTCookingRecipeBuilder.smeltingAndSmoking {
            ingredient { +tag(CommonTagPrefixes.INGOT, RagiumMaterialKeys.MEAT) }
            +RagiumItems.COOKED_MEAT_INGOT.toStack()
            exp = 0.35f
            recipeId suffix "_from_ingot"
        }.forEach { it.save(exporter) }
        // Meat + Bone -> Bone with Meat
        HTShapedRecipeBuilder.create {
            hollow8()
            define('A') { +tag(CommonTagPrefixes.INGOT, RagiumMaterialKeys.MEAT) }
            define('B') { +Tags.Items.BONES }
            +RagiumBlocks.MEAT_BLOCK.toStack()
        }.save(exporter)
        HTCookingRecipeBuilder.smeltingAndSmoking {
            ingredient { +RagiumBlocks.MEAT_BLOCK }
            +RagiumBlocks.COOKED_MEAT_BLOCK.toStack()
            time *= 3
            exp = 1f
            recipeId suffix "_from_raw"
        }.forEach { it.save(exporter) }
        // Canned Cooked Meat
        HTShapedRecipeBuilder.create {
            hollow8()
            define('A') { +tag(CommonTagPrefixes.INGOT, RagiumMaterialKeys.COOKED_MEAT) }
            define('B') { +tag(CommonTagPrefixes.PLATE, VanillaMaterialKeys.IRON) }
            +RagiumItems.COOKED_MEAT_INGOT.toStack(8)
        }.save(exporter)
    }

    private fun stainless() {
        useItem(CommonParts.DUST, RagiumMaterialKeys.STAINLESS_STEEL) {
            // Stainless
            HTShapelessRecipeBuilder.create {
                repeat(6) { ingredient { +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.IRON) } }
                repeat(2) { ingredient { +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.NICKEL) } }
                ingredient { +tag(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGI_CRYSTAL) }
                +it.toStack(9)
            }.save(exporter)
            // Stainless from Invar
            HTShapelessRecipeBuilder.create {
                repeat(6) { ingredient { +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.INVAR) } }
                repeat(2) { ingredient { +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.IRON) } }
                ingredient { +tag(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGI_CRYSTAL) }
                +it.toStack(9)
                condition { +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.INVAR) }
                recipeId suffix "_from_invar"
            }.save(exporter)
        }
    }

    override fun getName(): String = "Material Recipes"
}
