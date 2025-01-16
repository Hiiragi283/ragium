package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.savePrefixed
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder

object HTBuildingRecipeProvider : RecipeProviderChild {
    override fun buildRecipes(output: RecipeOutput) {
        registerLEDs(output)
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
