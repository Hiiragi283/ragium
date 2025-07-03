package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumCrushingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        // Vanilla
        createCrushing()
            .itemOutput(Items.STRING, 4)
            .itemInput(ItemTags.WOOL)
            .saveSuffixed(output, "_from_wool")
        createCrushing()
            .itemOutput(Items.STRING, 2)
            .itemOutput(Items.STRING, chance = 1 / 3f)
            .itemInput(ItemTags.WOOL_CARPETS)
            .saveSuffixed(output, "_from_carpet")
        createCrushing()
            .itemOutput(Items.STRING, 5)
            .itemInput(Items.COBWEB)
            .saveSuffixed(output, "_from_web")

        createCrushing()
            .itemOutput(Items.GLOWSTONE_DUST, 4)
            .itemInput(Items.GLOWSTONE)
            .saveSuffixed(output, "_from_glowstone")

        createCrushing()
            .itemOutput(Items.MAGMA_CREAM, 4)
            .itemInput(Items.MAGMA_BLOCK)
            .saveSuffixed(output, "_from_block")

        createCrushing()
            .itemOutput(Items.MUD)
            .itemOutput(Items.MANGROVE_ROOTS, chance = 1 / 4f)
            .itemInput(Items.MUDDY_MANGROVE_ROOTS)
            .saveSuffixed(output, "_from_roots")

        createCrushing()
            .itemOutput(Items.SUGAR, 3)
            .itemInput(Tags.Items.CROPS_SUGAR_CANE)
            .saveSuffixed(output, "_from_cane")

        createCrushing()
            .itemOutput(Items.BLAZE_POWDER, 4)
            .itemInput(Tags.Items.RODS_BLAZE)
            .saveSuffixed(output, "_from_rod")
        createCrushing()
            .itemOutput(Items.WIND_CHARGE, 4)
            .itemInput(Tags.Items.RODS_BREEZE)
            .saveSuffixed(output, "_from_rod")

        createCrushing()
            .itemOutput(Items.DIRT)
            .itemOutput(Items.FLINT, chance = 1 / 4f)
            .itemInput(Items.COARSE_DIRT)
            .saveSuffixed(output, "_from_coarse")
        createCrushing()
            .itemOutput(Items.DIRT)
            .itemOutput(Items.HANGING_ROOTS, chance = 1 / 4f)
            .itemInput(Items.ROOTED_DIRT)
            .saveSuffixed(output, "_from_rooted")

        createCrushing()
            .itemOutput(Items.NETHER_WART, 9)
            .itemInput(Items.NETHER_WART_BLOCK)
            .saveSuffixed(output, "_from_block")

        createCrushing()
            .itemOutput(RagiumItems.WARPED_WART, 9)
            .itemInput(Items.WARPED_WART_BLOCK)
            .saveSuffixed(output, "_from_block")
        // Ragium
        createCrushing()
            .itemOutput(Items.BLACKSTONE)
            .itemOutput(Items.GOLD_NUGGET, 3)
            .itemOutput(Items.GOLD_NUGGET, 3, 1 / 2f)
            .itemOutput(Items.GOLD_NUGGET, 3, 1 / 4f)
            .itemInput(Items.GILDED_BLACKSTONE)
            .saveSuffixed(output, "_from_gilded")

        createCrushing()
            .itemOutput(RagiumItems.OBSIDIAN_DUST, 4)
            .itemInput(Tags.Items.OBSIDIANS_NORMAL)
            .saveSuffixed(output, "_from_block")

        createCrushing()
            .itemOutput(RagiumItems.ASH_DUST, 3)
            .itemInput(RagiumBlocks.ASH_LOG)
            .saveSuffixed(output, "_from_log")

        woodDust()
        sand()
        prismarine()
        snow()
    }

    private fun woodDust() {
        createCrushing()
            .itemOutput(RagiumItemTags.DUSTS_WOOD, 6)
            .itemInput(ItemTags.LOGS_THAT_BURN)
            .saveSuffixed(output, "_from_log")

        createCrushing()
            .itemOutput(RagiumItemTags.DUSTS_WOOD)
            .itemInput(ItemTags.PLANKS)
            .saveSuffixed(output, "_from_planks")

        createCrushing()
            .itemOutput(RagiumItemTags.DUSTS_WOOD)
            .itemInput(ItemTags.WOODEN_STAIRS)
            .saveSuffixed(output, "_from_stair")

        createCrushing()
            .itemOutput(RagiumItemTags.DUSTS_WOOD)
            .itemInput(Tags.Items.FENCES_WOODEN)
            .saveSuffixed(output, "_from_fence")

        createCrushing()
            .itemOutput(RagiumItemTags.DUSTS_WOOD, 2)
            .itemInput(Tags.Items.FENCE_GATES_WOODEN)
            .saveSuffixed(output, "_from_fence_gate")

        createCrushing()
            .itemOutput(RagiumItemTags.DUSTS_WOOD, 2)
            .itemInput(ItemTags.WOODEN_DOORS)
            .saveSuffixed(output, "_from_door")

        createCrushing()
            .itemOutput(RagiumItemTags.DUSTS_WOOD, 3)
            .itemInput(ItemTags.WOODEN_TRAPDOORS)
            .saveSuffixed(output, "_from_trapdoor")

        createCrushing()
            .itemOutput(RagiumItemTags.DUSTS_WOOD, 8)
            .itemInput(Tags.Items.CHESTS_WOODEN)
            .saveSuffixed(output, "_from_chest")

        createCrushing()
            .itemOutput(RagiumItemTags.DUSTS_WOOD, 7)
            .itemInput(Tags.Items.BARRELS_WOODEN)
            .saveSuffixed(output, "_from_wooden")

        createCrushing()
            .itemOutput(RagiumItemTags.DUSTS_WOOD, 5)
            .itemInput(ItemTags.BOATS)
            .saveSuffixed(output, "_from_boat")
    }

    private fun sand() {
        // Colorless
        createCrushing()
            .itemOutput(Items.GRAVEL)
            .itemInput(Tags.Items.COBBLESTONES)
            .saveSuffixed(output, "_from_cobble")

        createCrushing()
            .itemOutput(Items.SAND)
            .itemOutput(Items.FLINT, chance = 1 / 3f)
            .itemInput(Tags.Items.GRAVELS)
            .saveSuffixed(output, "_from_gravel")

        createCrushing()
            .itemOutput(Items.SAND, 4)
            .itemOutput(RagiumItems.SALTPETER_DUST, chance = 1 / 4f)
            .itemInput(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            .saveSuffixed(output, "_from_sandstone")

        createCrushing()
            .itemOutput(RagiumBlocks.SILT)
            .itemInput(Tags.Items.SANDS)
            .saveSuffixed(output, "_from_sand")
        // Red
        createCrushing()
            .itemOutput(Items.RED_SAND, 4)
            .itemOutput(Items.REDSTONE, chance = 1 / 8f)
            .itemInput(Tags.Items.SANDSTONE_RED_BLOCKS)
            .saveSuffixed(output, "_from_sandstone")
    }

    private fun prismarine() {
        createCrushing()
            .itemOutput(Items.PRISMARINE_CRYSTALS, 9)
            .itemInput(Items.SEA_LANTERN)
            .saveSuffixed(output, "_from_sea_lantern")
        createCrushing()
            .itemOutput(Items.PRISMARINE_CRYSTALS)
            .itemInput(Items.PRISMARINE_SHARD)
            .saveSuffixed(output, "_from_shard")

        createCrushing()
            .itemOutput(Items.PRISMARINE_SHARD, 4)
            .itemInput(Items.PRISMARINE)
            .saveSuffixed(output, "_from_block")
        createCrushing()
            .itemOutput(Items.PRISMARINE_SHARD, 9)
            .itemInput(Items.PRISMARINE_BRICKS)
            .saveSuffixed(output, "_from_bricks")
        createCrushing()
            .itemOutput(Items.PRISMARINE_SHARD, 8)
            .itemInput(Items.DARK_PRISMARINE)
            .saveSuffixed(output, "_from_dark")
    }

    private fun snow() {
        // Snow
        createCrushing()
            .itemOutput(Items.SNOWBALL, 4)
            .itemInput(Items.SNOW_BLOCK)
            .saveSuffixed(output, "_from_block")
        createCrushing()
            .itemOutput(Items.SNOWBALL, 4)
            .itemInput(Items.ICE)
            .saveSuffixed(output, "_from_ice")
        // Ice
        createCrushing()
            .itemOutput(Items.ICE, 9)
            .itemInput(Items.PACKED_ICE)
            .saveSuffixed(output, "_from_packed")
        createCrushing()
            .itemOutput(Items.PACKED_ICE, 9)
            .itemInput(Items.BLUE_ICE)
            .saveSuffixed(output, "_from_blue")
    }
}
