package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCrushingRecipeBuilder
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.idOrNull
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumCrushingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Vanilla
        HTCrushingRecipeBuilder(Items.STRING, 4)
            .setIngredient(ItemTags.WOOL)
            .group("string")
            .saveSuffixed(output, "_from_wool")

        HTCrushingRecipeBuilder(Items.STRING, 3)
            .setIngredient(ItemTags.WOOL_CARPETS)
            .group("string")
            .saveSuffixed(output, "_from_carpet")

        HTCrushingRecipeBuilder(Items.STRING, 5)
            .setIngredient(Items.COBWEB)
            .group("string")
            .saveSuffixed(output, "_from_web")

        HTCrushingRecipeBuilder(Items.GLOWSTONE_DUST, 4)
            .setIngredient(HTTagPrefix.BLOCK, VanillaMaterials.GLOWSTONE)
            .group("glowstone_dust")
            .saveSuffixed(output, "_from_glowstone")

        HTCrushingRecipeBuilder(Items.MAGMA_CREAM, 4)
            .setIngredient(Items.MAGMA_BLOCK)
            .group("magma_cream")
            .saveSuffixed(output, "_from_block")

        HTCrushingRecipeBuilder(Items.MUD)
            .setIngredient(Items.MUDDY_MANGROVE_ROOTS)
            .group("mud")
            .saveSuffixed(output, "_from_roots")

        HTCrushingRecipeBuilder(Items.BROWN_MUSHROOM, 3)
            .setIngredient(Items.BROWN_MUSHROOM_BLOCK)
            .group("brown_mushroom")
            .saveSuffixed(output, "_from_block")

        HTCrushingRecipeBuilder(Items.RED_MUSHROOM, 3)
            .setIngredient(Items.RED_MUSHROOM_BLOCK)
            .group("red_mushroom")
            .saveSuffixed(output, "_from_block")

        HTCrushingRecipeBuilder(Items.SUGAR, 3)
            .setIngredient(Items.SUGAR_CANE)
            .group("sugar")
            .saveSuffixed(output, "_from_cane")

        HTCrushingRecipeBuilder(Items.BLAZE_POWDER, 4)
            .setIngredient(Tags.Items.RODS_BLAZE)
            .group("blaze_powder")
            .saveSuffixed(output, "_from_rod")

        HTCrushingRecipeBuilder(Items.WIND_CHARGE, 4)
            .setIngredient(Tags.Items.RODS_BREEZE)
            .group("wind_charge")
            .saveSuffixed(output, "_from_rod")
        // Ragium
        HTCrushingRecipeBuilder(RagiumItems.Dusts.SULFUR)
            .setIngredient(Tags.Items.NETHERRACKS)
            .group("sulfur_dust")
            .saveSuffixed(output, "_from_netherrack")

        HTCrushingRecipeBuilder(RagiumItems.Dusts.GOLD)
            .setIngredient(Items.GILDED_BLACKSTONE)
            .group("gold_dust")
            .saveSuffixed(output, "_from_blackstone")

        HTCrushingRecipeBuilder(RagiumItems.Dusts.OBSIDIAN, 4)
            .setIngredient(Tags.Items.OBSIDIANS_NORMAL)
            .group("obsidian_dust")
            .saveSuffixed(output, "_from_block")

        HTCrushingRecipeBuilder(RagiumItems.FLOUR, 9)
            .setIngredient(Tags.Items.STORAGE_BLOCKS_WHEAT)
            .group("flour")
            .saveSuffixed(output, "_from_block")

        HTCrushingRecipeBuilder(RagiumItems.FLOUR)
            .setIngredient(Tags.Items.CROPS_WHEAT)
            .group("flour")
            .saveSuffixed(output, "_from_crop")

        HTCrushingRecipeBuilder(RagiumItems.Dusts.IRON, 31)
            .setIngredient(ItemTags.ANVIL)
            .group("iron_dust")
            .saveSuffixed(output, "_from_anvil")

        HTCrushingRecipeBuilder(RagiumItems.Dusts.IRON, 7)
            .setIngredient(Items.CAULDRON)
            .group("iron_dust")
            .saveSuffixed(output, "_from_cauldron")

        woodDust(output)
        sand(output)
        prismarine(output)
        snow(output)
        flower(output)
    }

    private fun woodDust(output: RecipeOutput) {
        HTCrushingRecipeBuilder(RagiumItems.Dusts.WOOD, 6)
            .setIngredient(ItemTags.LOGS_THAT_BURN)
            .group("wood_dust")
            .saveSuffixed(output, "_from_log")

        HTCrushingRecipeBuilder(RagiumItems.Dusts.WOOD)
            .setIngredient(ItemTags.PLANKS)
            .group("wood_dust")
            .saveSuffixed(output, "_from_planks")

        HTCrushingRecipeBuilder(RagiumItems.Dusts.WOOD)
            .setIngredient(ItemTags.WOODEN_STAIRS)
            .group("wood_dust")
            .saveSuffixed(output, "_from_stair")

        HTCrushingRecipeBuilder(RagiumItems.Dusts.WOOD)
            .setIngredient(ItemTags.WOODEN_FENCES)
            .group("wood_dust")
            .saveSuffixed(output, "_from_fence")

        HTCrushingRecipeBuilder(RagiumItems.Dusts.WOOD, 2)
            .setIngredient(ItemTags.FENCE_GATES)
            .group("wood_dust")
            .saveSuffixed(output, "_from_fence_gate")

        HTCrushingRecipeBuilder(RagiumItems.Dusts.WOOD, 2)
            .setIngredient(ItemTags.WOODEN_DOORS)
            .group("wood_dust")
            .saveSuffixed(output, "_from_door")

        HTCrushingRecipeBuilder(RagiumItems.Dusts.WOOD, 3)
            .setIngredient(ItemTags.WOODEN_TRAPDOORS)
            .group("wood_dust")
            .saveSuffixed(output, "_from_trapdoor")

        HTCrushingRecipeBuilder(RagiumItems.Dusts.WOOD, 8)
            .setIngredient(Tags.Items.CHESTS_WOODEN)
            .group("wood_dust")
            .saveSuffixed(output, "_from_chest")

        HTCrushingRecipeBuilder(RagiumItems.Dusts.WOOD, 7)
            .setIngredient(Tags.Items.BARRELS_WOODEN)
            .group("wood_dust")
            .saveSuffixed(output, "_from_wooden")

        HTCrushingRecipeBuilder(RagiumItems.Dusts.WOOD, 5)
            .setIngredient(ItemTags.BOATS)
            .group("wood_dust")
            .saveSuffixed(output, "_from_boat")
    }

    private fun sand(output: RecipeOutput) {
        // Colorless
        HTCrushingRecipeBuilder(Items.GRAVEL)
            .setIngredient(Tags.Items.COBBLESTONES)
            .group("gravel")
            .saveSuffixed(output, "_from_cobble")

        HTCrushingRecipeBuilder(Items.SAND)
            .setIngredient(Tags.Items.GRAVELS)
            .group("sand")
            .saveSuffixed(output, "_from_gravel")

        HTCrushingRecipeBuilder(RagiumBlocks.SILT)
            .setIngredient(Tags.Items.SANDS)
            .group("silt")
            .saveSuffixed(output, "_from_sand")

        HTCrushingRecipeBuilder(Items.SAND, 4)
            .setIngredient(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            .group("sand")
            .saveSuffixed(output, "_from_sandstone")
        // Red
        HTCrushingRecipeBuilder(Items.RED_SAND, 4)
            .setIngredient(Tags.Items.SANDSTONE_RED_BLOCKS)
            .group("red_sand")
            .saveSuffixed(output, "_from_sandstone")
    }

    private fun prismarine(output: RecipeOutput) {
        HTCrushingRecipeBuilder(Items.PRISMARINE_CRYSTALS, 9)
            .setIngredient(Items.SEA_LANTERN)
            .group("prismarine_crystal")
            .saveSuffixed(output, "_from_sea_lantern")

        HTCrushingRecipeBuilder(Items.PRISMARINE_CRYSTALS)
            .setIngredient(Items.PRISMARINE_SHARD)
            .group("prismarine_crystal")
            .saveSuffixed(output, "_from_shard")

        HTCrushingRecipeBuilder(Items.PRISMARINE_SHARD, 4)
            .setIngredient(Items.PRISMARINE)
            .group("prismarine_shard")
            .saveSuffixed(output, "_from_block")

        HTCrushingRecipeBuilder(Items.PRISMARINE_SHARD, 9)
            .setIngredient(Items.PRISMARINE_BRICKS)
            .group("prismarine_shard")
            .saveSuffixed(output, "_from_bricks")

        HTCrushingRecipeBuilder(Items.PRISMARINE_SHARD, 8)
            .setIngredient(Items.DARK_PRISMARINE)
            .group("prismarine_shard")
            .saveSuffixed(output, "_from_dark")
    }

    private fun snow(output: RecipeOutput) {
        // Snow
        HTCrushingRecipeBuilder(Items.SNOWBALL, 4)
            .setIngredient(Items.SNOW_BLOCK)
            .group("snowball")
            .saveSuffixed(output, "_from_block")

        HTCrushingRecipeBuilder(Items.SNOWBALL, 4)
            .setIngredient(Items.ICE)
            .group("snowball")
            .saveSuffixed(output, "_from_ice")
        // Ice
        HTCrushingRecipeBuilder(Items.ICE, 9)
            .setIngredient(Items.PACKED_ICE)
            .group("ice")
            .saveSuffixed(output, "_from_packed")

        HTCrushingRecipeBuilder(Items.PACKED_ICE, 9)
            .setIngredient(Items.BLACKSTONE)
            .group("packed_ice")
            .saveSuffixed(output, "_from_blue")
    }

    private fun flower(output: RecipeOutput) {
        val small: Map<Item, Item> = mapOf(
            Items.DANDELION to Items.YELLOW_DYE,
            Items.POPPY to Items.RED_DYE,
            Items.BLUE_ORCHID to Items.LIGHT_BLUE_DYE,
            Items.ALLIUM to Items.MAGENTA_DYE,
            Items.AZURE_BLUET to Items.LIGHT_GRAY_DYE,
            Items.RED_TULIP to Items.RED_DYE,
            Items.ORANGE_TULIP to Items.ORANGE_DYE,
            Items.WHITE_TULIP to Items.LIGHT_GRAY_DYE,
            Items.PINK_TULIP to Items.PINK_DYE,
            Items.OXEYE_DAISY to Items.LIGHT_GRAY_DYE,
            Items.CORNFLOWER to Items.BLUE_DYE,
            Items.LILY_OF_THE_VALLEY to Items.WHITE_DYE,
            Items.WITHER_ROSE to Items.BLACK_DYE,
            Items.PINK_PETALS to Items.PINK_DYE,
            Items.CACTUS to Items.GREEN_DYE,
        )
        for ((flower: Item, dye: Item) in small) {
            val holder: Holder.Reference<Item> = flower.asItemHolder()
            val flowerName: String = holder.idOrNull?.path ?: continue
            HTCrushingRecipeBuilder(dye, 2)
                .setIngredient(flower)
                .saveSuffixed(output, "_from_$flowerName")
        }

        val large: Map<Item, Item> = mapOf(
            Items.TORCHFLOWER to Items.ORANGE_DYE,
            Items.SUNFLOWER to Items.YELLOW_DYE,
            Items.LILAC to Items.MAGENTA_DYE,
            Items.ROSE_BUSH to Items.RED_DYE,
            Items.PEONY to Items.PINK_DYE,
            Items.PITCHER_PLANT to Items.CYAN_DYE,
        )
        for ((flower: Item, dye: Item) in large) {
            val holder: Holder.Reference<Item> = flower.asItemHolder()
            val flowerName: String = holder.idOrNull?.path ?: continue
            HTCrushingRecipeBuilder(dye, 4)
                .setIngredient(flower)
                .saveSuffixed(output, "_from_$flowerName")
        }
    }
}
