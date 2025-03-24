package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumMiscRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        casings(output)
        wells(output)
    }

    private fun casings(output: RecipeOutput) {
        // Wooden
        HTShapedRecipeBuilder(RagiumBlocks.WOODEN_CASING, 4)
            .cross8()
            .define('A', ItemTags.LOGS)
            .define('B', ItemTags.PLANKS)
            .define('C', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)
        // Stone
        HTShapedRecipeBuilder(RagiumBlocks.STONE_CASING, 4)
            .casing()
            .define('A', Tags.Items.COBBLESTONES_NORMAL)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .define('C', Items.SMOOTH_STONE)
            .save(output)
        // Machine
        HTShapedRecipeBuilder(RagiumBlocks.MACHINE_CASING)
            .hollow8()
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.AZURE_STEEL)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)
        // Advanced Machine
        HTShapedRecipeBuilder(RagiumBlocks.ADVANCED_MACHINE_CASING)
            .hollow8()
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.DEEP_STEEL)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)
        // Device
        HTShapedRecipeBuilder(RagiumBlocks.DEVICE_CASING)
            .cross8()
            .define('A', Tags.Items.OBSIDIANS_NORMAL)
            .define('B', HTTagPrefix.INGOT, RagiumMaterials.DEEP_STEEL)
            .define('C', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)
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
            .pattern("A", "B", "C")
            .define('A', HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .define('B', Tags.Items.BARRELS_WOODEN)
            .define('C', RagiumBlocks.STONE_CASING)
            .save(output)
    }
}
