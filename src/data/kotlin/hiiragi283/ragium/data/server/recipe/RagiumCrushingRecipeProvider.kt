package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumCrushingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Vanilla
        crush().itemOutput(Items.STRING, 4).itemInput(ItemTags.WOOL).saveSuffixed(output, "_from_wool")
        crush().itemOutput(Items.STRING, 3).itemInput(ItemTags.WOOL_CARPETS).saveSuffixed(output, "_from_carpet")
        crush().itemOutput(Items.STRING, 5).itemInput(Items.COBWEB).saveSuffixed(output, "_from_web")

        crush()
            .itemOutput(Items.GLOWSTONE_DUST, 4)
            .itemInput(HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.GLOWSTONE)
            .saveSuffixed(output, "_from_glowstone")

        crush().itemOutput(Items.MAGMA_CREAM, 4).itemInput(Items.MAGMA_BLOCK).saveSuffixed(output, "_from_block")

        crush().itemOutput(Items.MUD).itemInput(Items.MUDDY_MANGROVE_ROOTS).saveSuffixed(output, "_from_roots")

        crush().itemOutput(Items.SUGAR, 3).itemInput(Tags.Items.CROPS_SUGAR_CANE).saveSuffixed(output, "_from_cane")

        crush().itemOutput(Items.BLAZE_POWDER, 4).itemInput(Tags.Items.RODS_BLAZE).saveSuffixed(output, "_from_rod")
        crush().itemOutput(Items.WIND_CHARGE, 4).itemInput(Tags.Items.RODS_BREEZE).saveSuffixed(output, "_from_rod")

        crush().itemOutput(Items.DIRT).itemInput(Items.COARSE_DIRT).saveSuffixed(output, "_from_coarse")
        crush().itemOutput(Items.DIRT).itemInput(Items.ROOTED_DIRT).saveSuffixed(output, "_from_rooted")
        // Ragium
        crush().itemOutput(RagiumItems.Dusts.GOLD).itemInput(Items.GILDED_BLACKSTONE).saveSuffixed(output, "_from_blackstone")

        crush().itemOutput(RagiumItems.Dusts.OBSIDIAN, 4).itemInput(Tags.Items.OBSIDIANS_NORMAL).saveSuffixed(output, "_from_block")

        crush().itemOutput(RagiumItems.FLOUR, 9).itemInput(Tags.Items.STORAGE_BLOCKS_WHEAT).saveSuffixed(output, "_from_block")
        crush().itemOutput(RagiumItems.FLOUR).itemInput(Tags.Items.CROPS_WHEAT).saveSuffixed(output, "_from_crop")

        crush().itemOutput(RagiumItems.Dusts.IRON, 31).itemInput(ItemTags.ANVIL).saveSuffixed(output, "_from_anvil")
        crush().itemOutput(RagiumItems.Dusts.IRON, 7).itemInput(Items.CAULDRON).saveSuffixed(output, "_from_cauldron")

        woodDust(output)
        sand(output)
        prismarine(output)
        snow(output)
    }

    private fun woodDust(output: RecipeOutput) {
        crush().itemOutput(RagiumItems.Dusts.WOOD, 6).itemInput(ItemTags.LOGS_THAT_BURN).saveSuffixed(output, "_from_log")
        crush().itemOutput(RagiumItems.Dusts.WOOD).itemInput(ItemTags.PLANKS).saveSuffixed(output, "_from_planks")
        crush().itemOutput(RagiumItems.Dusts.WOOD).itemInput(ItemTags.WOODEN_STAIRS).saveSuffixed(output, "_from_stair")
        crush().itemOutput(RagiumItems.Dusts.WOOD).itemInput(Tags.Items.FENCES_WOODEN).saveSuffixed(output, "_from_fence")
        crush().itemOutput(RagiumItems.Dusts.WOOD, 2).itemInput(Tags.Items.FENCE_GATES_WOODEN).saveSuffixed(output, "_from_fence_gate")
        crush().itemOutput(RagiumItems.Dusts.WOOD, 2).itemInput(ItemTags.WOODEN_DOORS).saveSuffixed(output, "_from_door")
        crush().itemOutput(RagiumItems.Dusts.WOOD, 3).itemInput(ItemTags.WOODEN_TRAPDOORS).saveSuffixed(output, "_from_trapdoor")
        crush().itemOutput(RagiumItems.Dusts.WOOD, 8).itemInput(Tags.Items.CHESTS_WOODEN).saveSuffixed(output, "_from_chest")
        crush().itemOutput(RagiumItems.Dusts.WOOD, 7).itemInput(Tags.Items.BARRELS_WOODEN).saveSuffixed(output, "_from_wooden")
        crush().itemOutput(RagiumItems.Dusts.WOOD, 5).itemInput(ItemTags.BOATS).saveSuffixed(output, "_from_boat")
    }

    private fun sand(output: RecipeOutput) {
        // Colorless
        crush().itemOutput(Items.GRAVEL).itemInput(Tags.Items.COBBLESTONES).saveSuffixed(output, "_from_cobble")
        crush().itemOutput(Items.SAND).itemInput(Tags.Items.GRAVELS).saveSuffixed(output, "_from_gravel")
        crush().itemOutput(Items.SAND, 4).itemInput(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS).saveSuffixed(output, "_from_sandstone")
        crush().itemOutput(RagiumBlocks.SILT).itemInput(Tags.Items.SANDS).saveSuffixed(output, "_from_sand")
        // Red
        crush().itemOutput(Items.RED_SAND, 4).itemInput(Tags.Items.SANDSTONE_RED_BLOCKS).saveSuffixed(output, "_from_sandstone")
    }

    private fun prismarine(output: RecipeOutput) {
        crush().itemOutput(Items.PRISMARINE_CRYSTALS, 9).itemInput(Items.SEA_LANTERN).saveSuffixed(output, "_from_sea_lantern")
        crush().itemOutput(Items.PRISMARINE_CRYSTALS).itemInput(Items.PRISMARINE_SHARD).saveSuffixed(output, "_from_shard")

        crush().itemOutput(Items.PRISMARINE_SHARD, 4).itemInput(Items.PRISMARINE).saveSuffixed(output, "_from_block")
        crush().itemOutput(Items.PRISMARINE_SHARD, 9).itemInput(Items.PRISMARINE_BRICKS).saveSuffixed(output, "_from_bricks")
        crush().itemOutput(Items.PRISMARINE_SHARD, 8).itemInput(Items.DARK_PRISMARINE).saveSuffixed(output, "_from_dark")
    }

    private fun snow(output: RecipeOutput) {
        // Snow
        crush().itemOutput(Items.SNOWBALL, 4).itemInput(Items.SNOW_BLOCK).saveSuffixed(output, "_from_block")
        crush().itemOutput(Items.SNOWBALL, 4).itemInput(Items.ICE).saveSuffixed(output, "_from_ice")
        // Ice
        crush().itemOutput(Items.ICE, 9).itemInput(Items.PACKED_ICE).saveSuffixed(output, "_from_packed")
        crush().itemOutput(Items.PACKED_ICE, 9).itemInput(Items.BLUE_ICE).saveSuffixed(output, "_from_blue")
    }
}
