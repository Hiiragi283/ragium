package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTDefinitionRecipeBuilder
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.common.recipe.HTMaterialCrushingRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumCrushingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Vanilla
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.STRING, 4)
            .itemInput(ItemTags.WOOL)
            .saveSuffixed(output, "_from_wool")
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.STRING, 3)
            .itemInput(ItemTags.WOOL_CARPETS)
            .saveSuffixed(output, "_from_carpet")
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.STRING, 5)
            .itemInput(Items.COBWEB)
            .saveSuffixed(output, "_from_web")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.GLOWSTONE_DUST, 4)
            .itemInput(HTTagPrefixes.STORAGE_BLOCK, VanillaMaterials.GLOWSTONE)
            .saveSuffixed(output, "_from_glowstone")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.MAGMA_CREAM, 4)
            .itemInput(Items.MAGMA_BLOCK)
            .saveSuffixed(output, "_from_block")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.MUD)
            .itemInput(Items.MUDDY_MANGROVE_ROOTS)
            .saveSuffixed(output, "_from_roots")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.SUGAR, 3)
            .itemInput(Tags.Items.CROPS_SUGAR_CANE)
            .saveSuffixed(output, "_from_cane")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.BLAZE_POWDER, 4)
            .itemInput(Tags.Items.RODS_BLAZE)
            .saveSuffixed(output, "_from_rod")
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.WIND_CHARGE, 4)
            .itemInput(Tags.Items.RODS_BREEZE)
            .saveSuffixed(output, "_from_rod")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.DIRT)
            .itemInput(Items.COARSE_DIRT)
            .saveSuffixed(output, "_from_coarse")
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.DIRT)
            .itemInput(Items.ROOTED_DIRT)
            .saveSuffixed(output, "_from_rooted")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.NETHER_WART, 9)
            .itemInput(Items.NETHER_WART_BLOCK)
            .saveSuffixed(output, "_from_block")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumItems.WARPED_WART, 9)
            .itemInput(Items.WARPED_WART_BLOCK)
            .saveSuffixed(output, "_from_block")
        // Ragium
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumItems.Dusts.GOLD)
            .itemInput(Items.GILDED_BLACKSTONE)
            .saveSuffixed(output, "_from_blackstone")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumItems.Dusts.OBSIDIAN, 4)
            .itemInput(Tags.Items.OBSIDIANS_NORMAL)
            .saveSuffixed(output, "_from_block")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumItems.Dusts.IRON, 31)
            .itemInput(ItemTags.ANVIL)
            .saveSuffixed(output, "_from_anvil")
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumItems.Dusts.IRON, 7)
            .itemInput(Items.CAULDRON)
            .saveSuffixed(output, "_from_cauldron")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumItems.Dusts.ASH, 3)
            .itemInput(RagiumBlocks.ASH_LOG)
            .saveSuffixed(output, "_from_log")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumItems.Dusts.ENDER_PEARL)
            .itemInput(RagiumBlocks.LILY_OF_THE_ENDER)
            .saveSuffixed(output, "_from_lily")

        woodDust(output)
        sand(output)
        prismarine(output)
        snow(output)

        // Gear -> Dust
        output.accept(
            RagiumAPI.id("crushing/gear_to_dust"),
            HTMaterialCrushingRecipe(HTTagPrefixes.GEAR, 1, 4),
            null,
        )
        // Gem -> Dust
        output.accept(
            RagiumAPI.id("crushing/gem_to_dust"),
            HTMaterialCrushingRecipe(HTTagPrefixes.GEM, 1, 1),
            null,
        )
        // Ingot -> Dust
        output.accept(
            RagiumAPI.id("crushing/ingot_to_dust"),
            HTMaterialCrushingRecipe(HTTagPrefixes.INGOT, 1, 1),
            null,
        )
        // Plate -> Dust
        output.accept(
            RagiumAPI.id("crushing/plate_to_dust"),
            HTMaterialCrushingRecipe(HTTagPrefixes.PLATE, 1, 1),
            null,
        )
        // Raw -> Dust
        output.accept(
            RagiumAPI.id("crushing/raw_to_dust"),
            HTMaterialCrushingRecipe(HTTagPrefixes.RAW_MATERIAL, 1, 2),
            null,
        )
        // Rod -> Dust
        output.accept(
            RagiumAPI.id("crushing/rod_to_dust"),
            HTMaterialCrushingRecipe(HTTagPrefixes.ROD, 2, 1),
            null,
        )
    }

    private fun woodDust(output: RecipeOutput) {
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumItems.Dusts.WOOD, 6)
            .itemInput(ItemTags.LOGS_THAT_BURN)
            .saveSuffixed(output, "_from_log")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumItems.Dusts.WOOD)
            .itemInput(ItemTags.PLANKS)
            .saveSuffixed(output, "_from_planks")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumItems.Dusts.WOOD)
            .itemInput(ItemTags.WOODEN_STAIRS)
            .saveSuffixed(output, "_from_stair")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumItems.Dusts.WOOD)
            .itemInput(Tags.Items.FENCES_WOODEN)
            .saveSuffixed(output, "_from_fence")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumItems.Dusts.WOOD, 2)
            .itemInput(Tags.Items.FENCE_GATES_WOODEN)
            .saveSuffixed(output, "_from_fence_gate")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumItems.Dusts.WOOD, 2)
            .itemInput(ItemTags.WOODEN_DOORS)
            .saveSuffixed(output, "_from_door")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumItems.Dusts.WOOD, 3)
            .itemInput(ItemTags.WOODEN_TRAPDOORS)
            .saveSuffixed(output, "_from_trapdoor")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumItems.Dusts.WOOD, 8)
            .itemInput(Tags.Items.CHESTS_WOODEN)
            .saveSuffixed(output, "_from_chest")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumItems.Dusts.WOOD, 7)
            .itemInput(Tags.Items.BARRELS_WOODEN)
            .saveSuffixed(output, "_from_wooden")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumItems.Dusts.WOOD, 5)
            .itemInput(ItemTags.BOATS)
            .saveSuffixed(output, "_from_boat")
    }

    private fun sand(output: RecipeOutput) {
        // Colorless
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.GRAVEL)
            .itemInput(Tags.Items.COBBLESTONES)
            .saveSuffixed(output, "_from_cobble")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.SAND)
            .itemInput(Tags.Items.GRAVELS)
            .saveSuffixed(output, "_from_gravel")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.SAND, 4)
            .itemInput(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            .saveSuffixed(output, "_from_sandstone")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(RagiumBlocks.SILT)
            .itemInput(Tags.Items.SANDS)
            .saveSuffixed(output, "_from_sand")
        // Red
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.RED_SAND, 4)
            .itemInput(Tags.Items.SANDSTONE_RED_BLOCKS)
            .saveSuffixed(output, "_from_sandstone")
    }

    private fun prismarine(output: RecipeOutput) {
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.PRISMARINE_CRYSTALS, 9)
            .itemInput(Items.SEA_LANTERN)
            .saveSuffixed(output, "_from_sea_lantern")
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.PRISMARINE_CRYSTALS)
            .itemInput(Items.PRISMARINE_SHARD)
            .saveSuffixed(output, "_from_shard")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.PRISMARINE_SHARD, 4)
            .itemInput(Items.PRISMARINE)
            .saveSuffixed(output, "_from_block")
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.PRISMARINE_SHARD, 9)
            .itemInput(Items.PRISMARINE_BRICKS)
            .saveSuffixed(output, "_from_bricks")
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.PRISMARINE_SHARD, 8)
            .itemInput(Items.DARK_PRISMARINE)
            .saveSuffixed(output, "_from_dark")
    }

    private fun snow(output: RecipeOutput) {
        // Snow
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.SNOWBALL, 4)
            .itemInput(Items.SNOW_BLOCK)
            .saveSuffixed(output, "_from_block")
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.SNOWBALL, 4)
            .itemInput(Items.ICE)
            .saveSuffixed(output, "_from_ice")
        // Ice
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.ICE, 9)
            .itemInput(Items.PACKED_ICE)
            .saveSuffixed(output, "_from_packed")
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.CRUSHING)
            .itemOutput(Items.PACKED_ICE, 9)
            .itemInput(Items.BLUE_ICE)
            .saveSuffixed(output, "_from_blue")
    }
}
