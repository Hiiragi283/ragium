package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumCrushingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Vanilla
        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromTagKey(ItemTags.WOOL),
                resultHelper.item(Items.STRING, 4),
            ).saveSuffixed(output, "_from_wool")
        HTItemToChancedItemRecipeBuilder
            .crushing(itemCreator.fromTagKey(ItemTags.WOOL_CARPETS))
            .addResult(resultHelper.item(Items.STRING, 2))
            .addResult(resultHelper.item(Items.STRING), 1 / 3f)
            .saveSuffixed(output, "_from_carpet")
        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromItem(Items.COBWEB),
                resultHelper.item(Items.STRING, 4),
            ).saveSuffixed(output, "_from_web")

        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromItem(Items.MAGMA_BLOCK),
                resultHelper.item(Items.MAGMA_CREAM, 4),
            ).saveSuffixed(output, "_from_block")

        HTItemToChancedItemRecipeBuilder
            .crushing(itemCreator.fromItem(Items.MUDDY_MANGROVE_ROOTS))
            .addResult(resultHelper.item(Items.MUD))
            .addResult(resultHelper.item(Items.MANGROVE_ROOTS), 1 / 4f)
            .saveSuffixed(output, "_from_roots")

        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromTagKey(Tags.Items.CROPS_SUGAR_CANE),
                resultHelper.item(Items.SUGAR, 3),
            ).saveSuffixed(output, "_from_cane")

        // Bone <-> Bone Meal
        crushAndCompress(Items.BONE, Items.BONE_MEAL, 4)
        // Blaze Rod <-> Blaze Powder
        crushAndCompress(Items.BLAZE_ROD, Items.BLAZE_POWDER, 4)
        // Breeze Rod <-> Wind Charge
        crushAndCompress(Items.BREEZE_ROD, Items.WIND_CHARGE, 6)

        HTItemToChancedItemRecipeBuilder
            .crushing(itemCreator.fromItem(Items.COARSE_DIRT))
            .addResult(resultHelper.item(Items.DIRT))
            .addResult(resultHelper.item(Items.FLINT), 1 / 4f)
            .saveSuffixed(output, "_from_coarse")
        HTItemToChancedItemRecipeBuilder
            .crushing(itemCreator.fromItem(Items.ROOTED_DIRT))
            .addResult(resultHelper.item(Items.DIRT))
            .addResult(resultHelper.item(Items.HANGING_ROOTS), 1 / 4f)
            .saveSuffixed(output, "_from_rooted")

        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromItem(Items.NETHER_WART_BLOCK),
                resultHelper.item(Items.NETHER_WART, 3),
            ).saveSuffixed(output, "_from_block")
        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromItem(Items.WARPED_WART_BLOCK),
                resultHelper.item(RagiumBlocks.WARPED_WART, 3),
            ).saveSuffixed(output, "_from_block")
        // Ragium
        HTItemToChancedItemRecipeBuilder
            .crushing(itemCreator.fromItem(Items.GLOW_INK_SAC))
            .addResult(resultHelper.item(RagiumItems.LUMINOUS_PASTE))
            .addResult(resultHelper.item(Items.INK_SAC))
            .save(output)
        // Common
        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromTagKey(Tags.Items.OBSIDIANS),
                resultHelper.item(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.OBSIDIAN)
            ).save(output)

        woodDust()
        sand()
        prismarine()
        snow()
    }

    @JvmStatic
    private fun woodDust() {
        fun wood(
            tagKey: TagKey<Item>,
            input: Int,
            output: Int,
            suffix: String,
        ) {
            HTItemToObjRecipeBuilder
                .pulverizing(
                    itemCreator.fromTagKey(tagKey, input),
                    resultHelper.item(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.WOOD, output),
                ).saveSuffixed(RagiumCrushingRecipeProvider.output, suffix)
        }

        wood(ItemTags.BOATS, 1, 5, "_from_boat")
        wood(ItemTags.LOGS_THAT_BURN, 1, 6, "_from_log")
        wood(ItemTags.PLANKS, 1, 1, "_from_planks")
        wood(ItemTags.WOODEN_BUTTONS, 1, 1, "_from_button")
        wood(ItemTags.WOODEN_DOORS, 1, 2, "_from_door")
        wood(ItemTags.WOODEN_PRESSURE_PLATES, 1, 2, "_from_pressure_plate")
        wood(ItemTags.WOODEN_SLABS, 2, 1, "_from_slabs")
        wood(ItemTags.WOODEN_STAIRS, 4, 3, "_from_stairs")
        wood(ItemTags.WOODEN_TRAPDOORS, 1, 3, "_from_trapdoor")
        wood(Tags.Items.BARRELS_WOODEN, 1, 7, "_from_wooden")
        wood(Tags.Items.CHESTS_WOODEN, 1, 8, "_from_chest")
        wood(Tags.Items.FENCE_GATES_WOODEN, 1, 4, "_from_fence_gate")
        wood(Tags.Items.FENCES_WOODEN, 1, 5, "_from_fence")
        wood(Tags.Items.RODS_WOODEN, 2, 1, "_from_stick")
    }

    @JvmStatic
    private fun sand() {
        // Colorless
        HTItemToChancedItemRecipeBuilder
            .crushing(itemCreator.fromTagKey(Tags.Items.COBBLESTONES))
            .addResult(resultHelper.item(Items.GRAVEL))
            .saveSuffixed(output, "_from_cobble")

        HTItemToChancedItemRecipeBuilder
            .crushing(itemCreator.fromTagKey(Tags.Items.GRAVELS))
            .addResult(resultHelper.item(Items.SAND))
            .addResult(resultHelper.item(Items.FLINT), 1 / 3f)
            .saveSuffixed(output, "_from_gravel")

        HTItemToChancedItemRecipeBuilder
            .crushing(itemCreator.fromTagKey(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS))
            .addResult(resultHelper.item(Items.SAND, 4))
            .addResult(resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALTPETER), 1 / 4f)
            .saveSuffixed(output, "_from_sandstone")

        HTItemToChancedItemRecipeBuilder
            .crushing(itemCreator.fromTagKey(Tags.Items.SANDS))
            .addResult(resultHelper.item(RagiumBlocks.SILT))
            .saveSuffixed(output, "_from_sand")
        // Red
        HTItemToChancedItemRecipeBuilder
            .crushing(itemCreator.fromTagKey(Tags.Items.SANDSTONE_RED_BLOCKS))
            .addResult(resultHelper.item(Items.RED_SAND, 4))
            .addResult(resultHelper.item(Items.REDSTONE), 1 / 8f)
            .saveSuffixed(output, "_from_sandstone")
    }

    @JvmStatic
    private fun prismarine() {
        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromItem(Items.SEA_LANTERN),
                resultHelper.item(Items.PRISMARINE_CRYSTALS, 9),
            ).saveSuffixed(output, "_from_sea_lantern")
        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromItem(Items.PRISMARINE_SHARD),
                resultHelper.item(Items.PRISMARINE_CRYSTALS),
            ).saveSuffixed(output, "_from_shard")

        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromItem(Items.PRISMARINE),
                resultHelper.item(Items.PRISMARINE_SHARD, 4),
            ).saveSuffixed(output, "_from_block")
        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromItem(Items.PRISMARINE_BRICKS),
                resultHelper.item(Items.PRISMARINE_SHARD, 9),
            ).saveSuffixed(output, "_from_bricks")
        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromItem(Items.DARK_PRISMARINE),
                resultHelper.item(Items.PRISMARINE_SHARD, 8),
            ).saveSuffixed(output, "_from_dark")
    }

    @JvmStatic
    private fun snow() {
        // Snow
        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromItem(Items.SNOW_BLOCK),
                resultHelper.item(Items.SNOWBALL, 4),
            ).saveSuffixed(output, "_from_block")
        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromItem(Items.ICE),
                resultHelper.item(Items.SNOWBALL, 4),
            ).saveSuffixed(output, "_from_ice")
        // Blue -> Packed -> Ice
        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromItem(Items.PACKED_ICE),
                resultHelper.item(Items.ICE, 9),
            ).saveSuffixed(output, "_from_packed")
        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromItem(Items.BLUE_ICE),
                resultHelper.item(Items.PACKED_ICE, 9),
            ).saveSuffixed(output, "_from_blue")
        // Ice -> Packed -> Blue
        HTItemWithCatalystRecipeBuilder
            .compressing(
                itemCreator.fromItem(Items.ICE, 4),
                resultHelper.item(Items.PACKED_ICE),
            ).saveSuffixed(output, "_from_ice")
        HTItemWithCatalystRecipeBuilder
            .compressing(
                itemCreator.fromItem(Items.PACKED_ICE, 4),
                resultHelper.item(Items.BLUE_ICE),
            ).saveSuffixed(output, "_from_packed")
    }
}
