package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.math.fraction
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTChancedRecipeBuilder
import net.minecraft.data.BlockFamilies
import net.minecraft.data.BlockFamily
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import kotlin.jvm.optionals.getOrNull

object RagiumCuttingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        wooden()
    }

    @JvmStatic
    private fun wooden() {
        // Planks from Parts
        BlockFamilies
            .getAllFamilies()
            .filter { family: BlockFamily -> family.recipeGroupPrefix.getOrNull() == "wooden" }
            .forEach { family: BlockFamily ->
                val base: Block = family.baseBlock
                val factory: (BlockFamily.Variant) -> ItemLike? = family::get
                // Button
                // Fence
                factory(BlockFamily.Variant.FENCE)?.let {
                    HTChancedRecipeBuilder
                        .cutting(
                            itemCreator.fromItem(it),
                            itemResult.create(base),
                        ).addResult(itemResult.create(Items.STICK))
                        .saveSuffixed(output, "_from_fence")
                }
                // Fence Gate
                factory(BlockFamily.Variant.FENCE_GATE)?.let {
                    HTChancedRecipeBuilder
                        .cutting(
                            itemCreator.fromItem(it),
                            itemResult.create(base, 2),
                        ).addResult(itemResult.create(Items.STICK, 4))
                        .saveSuffixed(output, "_from_fence_gate")
                }
                // Pressure Plate
                factory(BlockFamily.Variant.PRESSURE_PLATE)?.let {
                    HTChancedRecipeBuilder
                        .cutting(
                            itemCreator.fromItem(it),
                            itemResult.create(base, 2),
                        ).saveSuffixed(output, "_from_pressure_plate")
                }
                // Sign
                factory(BlockFamily.Variant.SIGN)?.let {
                    HTChancedRecipeBuilder
                        .cutting(
                            itemCreator.fromItem(it),
                            itemResult.create(base, 2),
                        ).addResult(itemResult.create(Items.STICK), fraction(1, 3))
                        .saveSuffixed(output, "_from_sign")
                }
                // Slab
                factory(BlockFamily.Variant.SLAB)?.let {
                    HTChancedRecipeBuilder
                        .cutting(
                            itemCreator.fromItem(base),
                            itemResult.create(it, 2),
                        ).save(output)
                }
                // Stairs
                // Door
                factory(BlockFamily.Variant.DOOR)?.let {
                    HTChancedRecipeBuilder
                        .cutting(
                            itemCreator.fromItem(it),
                            itemResult.create(base, 2),
                        ).saveSuffixed(output, "_from_door")
                }
                // Trapdoor
                factory(BlockFamily.Variant.TRAPDOOR)?.let {
                    HTChancedRecipeBuilder
                        .cutting(
                            itemCreator.fromItem(it),
                            itemResult.create(base, 3),
                        ).saveSuffixed(output, "_from_trapdoor")
                }
            }

        // Planks from Logs
        mapOf(
            ItemTags.OAK_LOGS to Items.OAK_PLANKS,
            ItemTags.SPRUCE_LOGS to Items.SPRUCE_PLANKS,
            ItemTags.BIRCH_LOGS to Items.BIRCH_PLANKS,
            ItemTags.JUNGLE_LOGS to Items.JUNGLE_PLANKS,
            ItemTags.ACACIA_LOGS to Items.ACACIA_PLANKS,
            ItemTags.CHERRY_LOGS to Items.CHERRY_PLANKS,
            ItemTags.DARK_OAK_LOGS to Items.DARK_OAK_PLANKS,
            ItemTags.MANGROVE_LOGS to Items.MANGROVE_PLANKS,
            ItemTags.BAMBOO_BLOCKS to Items.BAMBOO_PLANKS,
            ItemTags.CRIMSON_STEMS to Items.CRIMSON_PLANKS,
            ItemTags.WARPED_STEMS to Items.WARPED_PLANKS,
        ).forEach { (log: TagKey<Item>, planks: Item) ->
            // Log -> 6x Planks
            HTChancedRecipeBuilder
                .cutting(
                    itemCreator.fromTagKey(log),
                    itemResult.create(planks, 6),
                ).saveSuffixed(output, "_from_log")
        }

        // Stick
        HTChancedRecipeBuilder
            .cutting(
                itemCreator.fromTagKey(ItemTags.WOODEN_SLABS),
                itemResult.create(Items.STICK, 4),
            ).saveSuffixed(output, "_from_wooden_slabs")

        HTChancedRecipeBuilder
            .cutting(
                itemCreator.fromTagKey(ItemTags.SAPLINGS),
                itemResult.create(Items.STICK),
            ).saveSuffixed(output, "_from_saplings")
    }
}
