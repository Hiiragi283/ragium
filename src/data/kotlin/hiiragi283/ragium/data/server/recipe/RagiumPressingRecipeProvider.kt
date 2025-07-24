package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredBlock

object RagiumPressingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        // Plastic Plate
        createPressing()
            .itemOutput(RagiumCommonTags.Items.PLATES_PLASTIC)
            .itemInput(RagiumModTags.Items.POLYMER_RESIN)
            .save(output)
        // Synthetic Fiber
        createPressing()
            .itemOutput(RagiumItems.SYNTHETIC_FIBER, 2)
            .itemInput(RagiumModTags.Items.POLYMER_RESIN)
            .catalyst(Tags.Items.STRINGS)
            .save(output)
        // Synthetic Leather
        createPressing()
            .itemOutput(RagiumItems.SYNTHETIC_LEATHER, 2)
            .itemInput(RagiumModTags.Items.POLYMER_RESIN)
            .catalyst(Tags.Items.LEATHERS)
            .save(output)

        circuits()
        redStones()
        diode()
    }

    private fun circuits() {
        // Basic
        HTShapedRecipeBuilder(RagiumItems.BASIC_CIRCUIT)
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', Tags.Items.INGOTS_COPPER)
            .define('B', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .define('C', Tags.Items.INGOTS_IRON)
            .save(output)
        // Advanced
        HTShapedRecipeBuilder(RagiumItems.ADVANCED_CIRCUIT)
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', Tags.Items.INGOTS_GOLD)
            .define('B', Tags.Items.DUSTS_GLOWSTONE)
            .define('C', RagiumCommonTags.Items.CIRCUITS_BASIC)
            .save(output)

        // Circuit Board
        createPressing()
            .itemOutput(RagiumItems.CIRCUIT_BOARD)
            .itemInput(RagiumCommonTags.Items.DUSTS_QUARTZ)
            .catalyst(RagiumCommonTags.Items.PLATES_PLASTIC)
            .save(output)

        mapOf(
            Tags.Items.INGOTS_COPPER to RagiumItems.BASIC_CIRCUIT,
            Tags.Items.INGOTS_GOLD to RagiumItems.ADVANCED_CIRCUIT,
            RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL to RagiumItems.ELITE_CIRCUIT,
            RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL to RagiumItems.ULTIMATE_CIRCUIT,
        ).forEach { (input: TagKey<Item>, circuit: ItemLike) ->
            createPressing()
                .itemOutput(circuit)
                .itemInput(input)
                .catalyst(RagiumItems.CIRCUIT_BOARD)
                .save(output)
        }
    }

    private fun redStones() {
        // Redstone Board
        createPressing()
            .itemOutput(RagiumItems.REDSTONE_BOARD, 4)
            .itemInput(Tags.Items.DUSTS_REDSTONE)
            .catalyst(Items.SMOOTH_STONE_SLAB)
            .save(output)
        // Repeater
        createPressing()
            .itemOutput(Items.REPEATER)
            .itemInput(Items.REDSTONE_TORCH)
            .catalyst(RagiumItems.REDSTONE_BOARD)
            .save(output)
        // Comparator
        createPressing()
            .itemOutput(Items.COMPARATOR)
            .itemInput(Items.REDSTONE_TORCH)
            .catalyst(Items.REPEATER)
            .save(output)
    }

    private fun diode() {
        // LED
        createPressing()
            .itemOutput(RagiumItems.LED, 4)
            .itemInput(RagiumItems.LUMINOUS_PASTE)
            .catalyst(Tags.Items.INGOTS_COPPER)
            .save(output)
        // LED Block
        HTShapedRecipeBuilder(RagiumBlocks.getLedBlock(DyeColor.WHITE), 8)
            .hollow8()
            .define('A', Tags.Items.GLASS_BLOCKS)
            .define('B', RagiumItems.LED)
            .saveSuffixed(output, "_from_led")

        for ((color: DyeColor, block: DeferredBlock<*>) in RagiumBlocks.LED_BLOCKS) {
            HTShapedRecipeBuilder(block, 8)
                .hollow8()
                .define('A', RagiumModTags.Items.LED_BLOCKS)
                .define('B', color.tag)
                .save(output)
        }

        // Solar Panel
        HTShapedRecipeBuilder(RagiumItems.SOLAR_PANEL, 3)
            .pattern(
                "AAA",
                "BBB",
                "CCC",
            ).define('A', Tags.Items.GLASS_BLOCKS)
            .define('B', RagiumItems.LUMINOUS_PASTE)
            .define('C', RagiumCommonTags.Items.PLATES_PLASTIC)
            .save(output)
    }
}
