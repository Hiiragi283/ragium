package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.savePrefixed
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.world.item.crafting.Ingredient

object HTBuildingRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerDecorations(output)
        registerLEDs(output)
    }

    private fun registerDecorations(output: RecipeOutput) {
        // Plastic Decoration
        RagiumItems.Plastics.entries.forEach { plastic: RagiumItems.Plastics ->
            val tier: HTMachineTier = plastic.machineTier
            val count: Int = (tier.ordinal + 1) * 4
            // Shaped Crafting
            ShapedRecipeBuilder
                .shaped(RecipeCategory.BUILDING_BLOCKS, RagiumBlocks.PLASTIC_BLOCK, count)
                .pattern("AA")
                .pattern("AA")
                .define('A', plastic)
                .unlockedBy("has_plastic", has(plastic))
                .save(output, RagiumAPI.id("shaped/${tier.serializedName}_plastic_block"))
        }

        RagiumBlocks.Decorations.entries.forEach { decoration: RagiumBlocks.Decorations ->
            // Stone Cutting
            SingleItemRecipeBuilder
                .stonecutting(
                    Ingredient.of(RagiumBlocks.PLASTIC_BLOCK),
                    RecipeCategory.BUILDING_BLOCKS,
                    decoration,
                ).unlockedBy("has_plastic", has(RagiumBlocks.PLASTIC_BLOCK))
                .savePrefixed(output)
        }
    }

    private fun registerLEDs(output: RecipeOutput) {
        // LED
        RagiumBlocks.LEDBlocks.entries.forEach { ledBlock: RagiumBlocks.LEDBlocks ->
            // Shaped Crafting
            ShapedRecipeBuilder
                .shaped(RecipeCategory.BUILDING_BLOCKS, ledBlock, 4)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', ledBlock.baseBlock)
                .define('B', RagiumItems.LED)
                .unlockedBy("has_led", has(RagiumItems.LED))
                .savePrefixed(output)
        }
    }
}
