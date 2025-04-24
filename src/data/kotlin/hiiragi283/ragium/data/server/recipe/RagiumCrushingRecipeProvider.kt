package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.common.recipe.custom.HTMaterialCrushingRecipe
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
            .itemOutput(Items.STRING, 3)
            .itemInput(ItemTags.WOOL_CARPETS)
            .saveSuffixed(output, "_from_carpet")
        createCrushing()
            .itemOutput(Items.STRING, 5)
            .itemInput(Items.COBWEB)
            .saveSuffixed(output, "_from_web")

        createCrushing()
            .itemOutput(Items.GLOWSTONE_DUST, 4)
            .itemInput(HTTagPrefixes.STORAGE_BLOCK, VanillaMaterials.GLOWSTONE)
            .saveSuffixed(output, "_from_glowstone")

        createCrushing()
            .itemOutput(Items.MAGMA_CREAM, 4)
            .itemInput(Items.MAGMA_BLOCK)
            .saveSuffixed(output, "_from_block")

        createCrushing()
            .itemOutput(Items.MUD)
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
            .itemInput(Items.COARSE_DIRT)
            .saveSuffixed(output, "_from_coarse")
        createCrushing()
            .itemOutput(Items.DIRT)
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
            .itemOutput(RagiumItems.Dusts.GOLD)
            .itemInput(Items.GILDED_BLACKSTONE)
            .saveSuffixed(output, "_from_blackstone")

        createCrushing()
            .itemOutput(RagiumItems.Dusts.OBSIDIAN, 4)
            .itemInput(Tags.Items.OBSIDIANS_NORMAL)
            .saveSuffixed(output, "_from_block")

        createCrushing()
            .itemOutput(RagiumItems.Dusts.IRON, 31)
            .itemInput(ItemTags.ANVIL)
            .saveSuffixed(output, "_from_anvil")
        createCrushing()
            .itemOutput(RagiumItems.Dusts.IRON, 7)
            .itemInput(Items.CAULDRON)
            .saveSuffixed(output, "_from_cauldron")

        createCrushing()
            .itemOutput(RagiumItems.Dusts.ASH, 3)
            .itemInput(RagiumBlocks.ASH_LOG)
            .saveSuffixed(output, "_from_log")

        createCrushing()
            .itemOutput(RagiumItems.Dusts.ENDER_PEARL)
            .itemInput(RagiumBlocks.LILY_OF_THE_ENDER)
            .saveSuffixed(output, "_from_lily")

        woodDust()
        sand()
        prismarine()
        snow()

        // Gear -> Dust
        save(
            RagiumAPI.id("crushing/gear_to_dust"),
            HTMaterialCrushingRecipe(HTTagPrefixes.GEAR, 1, 4),
        )
        // Gem -> Dust
        save(
            RagiumAPI.id("crushing/gem_to_dust"),
            HTMaterialCrushingRecipe(HTTagPrefixes.GEM, 1, 1),
        )
        // Ingot -> Dust
        save(
            RagiumAPI.id("crushing/ingot_to_dust"),
            HTMaterialCrushingRecipe(HTTagPrefixes.INGOT, 1, 1),
        )
        // Plate -> Dust
        save(
            RagiumAPI.id("crushing/plate_to_dust"),
            HTMaterialCrushingRecipe(HTTagPrefixes.PLATE, 1, 1),
        )
        // Raw -> Dust
        save(
            RagiumAPI.id("crushing/raw_to_dust"),
            HTMaterialCrushingRecipe(HTTagPrefixes.RAW_MATERIAL, 1, 2),
        )
        // Rod -> Dust
        save(
            RagiumAPI.id("crushing/rod_to_dust"),
            HTMaterialCrushingRecipe(HTTagPrefixes.ROD, 2, 1),
        )
    }

    private fun woodDust() {
        createCrushing()
            .itemOutput(RagiumItems.Dusts.WOOD, 6)
            .itemInput(ItemTags.LOGS_THAT_BURN)
            .saveSuffixed(output, "_from_log")

        createCrushing()
            .itemOutput(RagiumItems.Dusts.WOOD)
            .itemInput(ItemTags.PLANKS)
            .saveSuffixed(output, "_from_planks")

        createCrushing()
            .itemOutput(RagiumItems.Dusts.WOOD)
            .itemInput(ItemTags.WOODEN_STAIRS)
            .saveSuffixed(output, "_from_stair")

        createCrushing()
            .itemOutput(RagiumItems.Dusts.WOOD)
            .itemInput(Tags.Items.FENCES_WOODEN)
            .saveSuffixed(output, "_from_fence")

        createCrushing()
            .itemOutput(RagiumItems.Dusts.WOOD, 2)
            .itemInput(Tags.Items.FENCE_GATES_WOODEN)
            .saveSuffixed(output, "_from_fence_gate")

        createCrushing()
            .itemOutput(RagiumItems.Dusts.WOOD, 2)
            .itemInput(ItemTags.WOODEN_DOORS)
            .saveSuffixed(output, "_from_door")

        createCrushing()
            .itemOutput(RagiumItems.Dusts.WOOD, 3)
            .itemInput(ItemTags.WOODEN_TRAPDOORS)
            .saveSuffixed(output, "_from_trapdoor")

        createCrushing()
            .itemOutput(RagiumItems.Dusts.WOOD, 8)
            .itemInput(Tags.Items.CHESTS_WOODEN)
            .saveSuffixed(output, "_from_chest")

        createCrushing()
            .itemOutput(RagiumItems.Dusts.WOOD, 7)
            .itemInput(Tags.Items.BARRELS_WOODEN)
            .saveSuffixed(output, "_from_wooden")

        createCrushing()
            .itemOutput(RagiumItems.Dusts.WOOD, 5)
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
            .itemInput(Tags.Items.GRAVELS)
            .saveSuffixed(output, "_from_gravel")

        createCrushing()
            .itemOutput(Items.SAND, 4)
            .itemInput(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            .saveSuffixed(output, "_from_sandstone")

        createCrushing()
            .itemOutput(RagiumBlocks.SILT)
            .itemInput(Tags.Items.SANDS)
            .saveSuffixed(output, "_from_sand")
        // Red
        createCrushing()
            .itemOutput(Items.RED_SAND, 4)
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
