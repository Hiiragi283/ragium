package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.material.HTBlockMaterialVariant
import hiiragi283.ragium.api.material.HTItemMaterialVariant
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.common.material.HTCommonMaterialTypes
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
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
                HTIngredientHelper.item(ItemTags.WOOL),
                HTResultHelper.INSTANCE.item(Items.STRING, 4),
            ).saveSuffixed(output, "_from_wool")
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(ItemTags.WOOL_CARPETS))
            .addResult(HTResultHelper.INSTANCE.item(Items.STRING, 2))
            .addResult(HTResultHelper.INSTANCE.item(Items.STRING), 1 / 3f)
            .saveSuffixed(output, "_from_carpet")
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.COBWEB),
                HTResultHelper.INSTANCE.item(Items.STRING, 4),
            ).saveSuffixed(output, "_from_web")

        mapOf(
            HTVanillaMaterialType.AMETHYST to Items.AMETHYST_SHARD,
            HTVanillaMaterialType.GLOWSTONE to Items.GLOWSTONE_DUST,
            HTVanillaMaterialType.QUARTZ to Items.QUARTZ,
        ).forEach { (material: HTMaterialType, result: Item) ->
            HTItemToObjRecipeBuilder
                .pulverizing(
                    HTIngredientHelper.item(HTBlockMaterialVariant.STORAGE_BLOCK, material),
                    HTResultHelper.INSTANCE.item(result, 4),
                ).saveSuffixed(output, "_from_block")
        }

        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.MAGMA_BLOCK),
                HTResultHelper.INSTANCE.item(Items.MAGMA_CREAM, 4),
            ).saveSuffixed(output, "_from_block")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.MUDDY_MANGROVE_ROOTS))
            .addResult(HTResultHelper.INSTANCE.item(Items.MUD))
            .addResult(HTResultHelper.INSTANCE.item(Items.MANGROVE_ROOTS), 1 / 4f)
            .saveSuffixed(output, "_from_roots")

        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Tags.Items.CROPS_SUGAR_CANE),
                HTResultHelper.INSTANCE.item(Items.SUGAR, 3),
            ).saveSuffixed(output, "_from_cane")

        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Tags.Items.RODS_BLAZE),
                HTResultHelper.INSTANCE.item(Items.BLAZE_POWDER, 4),
            ).saveSuffixed(output, "_from_rod")
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Tags.Items.RODS_BREEZE),
                HTResultHelper.INSTANCE.item(Items.WIND_CHARGE, 6),
            ).saveSuffixed(output, "_from_rod")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.COARSE_DIRT))
            .addResult(HTResultHelper.INSTANCE.item(Items.DIRT))
            .addResult(HTResultHelper.INSTANCE.item(Items.FLINT), 1 / 4f)
            .saveSuffixed(output, "_from_coarse")
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.ROOTED_DIRT))
            .addResult(HTResultHelper.INSTANCE.item(Items.DIRT))
            .addResult(HTResultHelper.INSTANCE.item(Items.HANGING_ROOTS), 1 / 4f)
            .saveSuffixed(output, "_from_rooted")

        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.NETHER_WART_BLOCK),
                HTResultHelper.INSTANCE.item(Items.NETHER_WART, 3),
            ).saveSuffixed(output, "_from_block")
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.WARPED_WART_BLOCK),
                HTResultHelper.INSTANCE.item(RagiumBlocks.WARPED_WART, 3),
            ).saveSuffixed(output, "_from_block")
        // Ragium
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Tags.Items.OBSIDIANS_NORMAL),
                HTResultHelper.INSTANCE.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.OBSIDIAN, 4),
            ).saveSuffixed(output, "_from_block")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Items.GLOW_INK_SAC))
            .addResult(HTResultHelper.INSTANCE.item(RagiumItems.LUMINOUS_PASTE))
            .addResult(HTResultHelper.INSTANCE.item(Items.INK_SAC))
            .save(output)

        woodDust()
        sand()
        prismarine()
        snow()

        material()
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
                    HTIngredientHelper.item(tagKey, input),
                    HTResultHelper.INSTANCE.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.WOOD, output),
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
            .crushing(HTIngredientHelper.item(Tags.Items.COBBLESTONES))
            .addResult(HTResultHelper.INSTANCE.item(Items.GRAVEL))
            .saveSuffixed(output, "_from_cobble")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.GRAVELS))
            .addResult(HTResultHelper.INSTANCE.item(Items.SAND))
            .addResult(HTResultHelper.INSTANCE.item(Items.FLINT), 1 / 3f)
            .saveSuffixed(output, "_from_gravel")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS))
            .addResult(HTResultHelper.INSTANCE.item(Items.SAND, 4))
            .addResult(HTResultHelper.INSTANCE.item(HTItemMaterialVariant.DUST, RagiumMaterialType.SALTPETER), 1 / 4f)
            .saveSuffixed(output, "_from_sandstone")

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.SANDS))
            .addResult(HTResultHelper.INSTANCE.item(RagiumBlocks.SILT))
            .saveSuffixed(output, "_from_sand")
        // Red
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.SANDSTONE_RED_BLOCKS))
            .addResult(HTResultHelper.INSTANCE.item(Items.RED_SAND, 4))
            .addResult(HTResultHelper.INSTANCE.item(Items.REDSTONE), 1 / 8f)
            .saveSuffixed(output, "_from_sandstone")
    }

    @JvmStatic
    private fun prismarine() {
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.SEA_LANTERN),
                HTResultHelper.INSTANCE.item(Items.PRISMARINE_CRYSTALS, 9),
            ).saveSuffixed(output, "_from_sea_lantern")
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.PRISMARINE_SHARD),
                HTResultHelper.INSTANCE.item(Items.PRISMARINE_CRYSTALS),
            ).saveSuffixed(output, "_from_shard")

        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.PRISMARINE),
                HTResultHelper.INSTANCE.item(Items.PRISMARINE_SHARD, 4),
            ).saveSuffixed(output, "_from_block")
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.PRISMARINE_BRICKS),
                HTResultHelper.INSTANCE.item(Items.PRISMARINE_SHARD, 9),
            ).saveSuffixed(output, "_from_bricks")
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.DARK_PRISMARINE),
                HTResultHelper.INSTANCE.item(Items.PRISMARINE_SHARD, 8),
            ).saveSuffixed(output, "_from_dark")
    }

    @JvmStatic
    private fun snow() {
        // Snow
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.SNOW_BLOCK),
                HTResultHelper.INSTANCE.item(Items.SNOWBALL, 4),
            ).saveSuffixed(output, "_from_block")
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.ICE),
                HTResultHelper.INSTANCE.item(Items.SNOWBALL, 4),
            ).saveSuffixed(output, "_from_ice")
        // Ice
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.PACKED_ICE),
                HTResultHelper.INSTANCE.item(Items.ICE, 9),
            ).saveSuffixed(output, "_from_packed")
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(Items.BLUE_ICE),
                HTResultHelper.INSTANCE.item(Items.PACKED_ICE, 9),
            ).saveSuffixed(output, "_from_blue")
    }

    @JvmStatic
    private fun material() {
        // Builtin
        for ((material: HTMaterialType, _) in RagiumItems.MATERIALS.row(HTItemMaterialVariant.DUST)) {
            val baseVariant: HTMaterialVariant.ItemTag = RagiumAPI.getInstance().getBaseVariant(material) ?: continue
            if (baseVariant == HTItemMaterialVariant.DUST) continue
            HTItemToObjRecipeBuilder
                .pulverizing(
                    HTIngredientHelper.item(baseVariant, material),
                    HTResultHelper.INSTANCE.item(HTItemMaterialVariant.DUST, material),
                ).saveSuffixed(output, "_from_${baseVariant.serializedName}")
        }

        // Common
        for (material: HTMaterialType in HTCommonMaterialTypes.METALS.values) {
            HTItemToObjRecipeBuilder
                .pulverizing(
                    HTIngredientHelper.item(HTItemMaterialVariant.INGOT, material),
                    HTResultHelper.INSTANCE.item(HTItemMaterialVariant.DUST, material),
                ).tagCondition(HTItemMaterialVariant.DUST, material)
                .saveSuffixed(output, "_from_ingot")
        }

        for (material: HTMaterialType in HTCommonMaterialTypes.ALLOYS.values) {
            HTItemToObjRecipeBuilder
                .pulverizing(
                    HTIngredientHelper.item(HTItemMaterialVariant.INGOT, material),
                    HTResultHelper.INSTANCE.item(HTItemMaterialVariant.DUST, material),
                ).tagCondition(HTItemMaterialVariant.DUST, material)
                .saveSuffixed(output, "_from_ingot")
        }

        for (material: HTMaterialType in HTCommonMaterialTypes.GEMS.values) {
            HTItemToObjRecipeBuilder
                .pulverizing(
                    HTIngredientHelper.item(HTItemMaterialVariant.GEM, material),
                    HTResultHelper.INSTANCE.item(HTItemMaterialVariant.DUST, material),
                ).tagCondition(HTItemMaterialVariant.DUST, material)
                .saveSuffixed(output, "_from_gem")
        }
    }
}
