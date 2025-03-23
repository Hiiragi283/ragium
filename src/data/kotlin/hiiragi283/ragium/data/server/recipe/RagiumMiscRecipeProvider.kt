package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items

object RagiumMiscRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        wells(output)
    }

    private fun wells(output: RecipeOutput) {
        // Water Well
        HTShapedRecipeBuilder(RagiumBlocks.WATER_WELL)
            .pattern(
                "A A",
                "ABA",
                "CCC",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.AZURE_STEEL)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .define('C', Items.DEEPSLATE_TILES)
            .save(output)
        // Lava Well
        HTShapedRecipeBuilder(RagiumBlocks.LAVA_WELL)
            .pattern(
                "A A",
                "ABA",
                "CCC",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.ADVANCED_RAGI_ALLOY)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .define('C', Items.NETHER_BRICKS)
            .save(output)
        // Milk Drain
        HTShapedRecipeBuilder(RagiumBlocks.MILK_DRAIN)
            .pattern(
                "ABA",
                "CCC",
            ).define('A', ItemTags.PLANKS)
            .define('B', Items.IRON_BARS)
            .define('C', ItemTags.STONE_BRICKS)
            .save(output)
    }
}
