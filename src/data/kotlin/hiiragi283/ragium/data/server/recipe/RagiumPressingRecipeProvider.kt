package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

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

        // Blaze Rod
        createPressing()
            .itemOutput(Items.BLAZE_ROD)
            .itemInput(Items.BLAZE_POWDER, 4)
            .catalyst(Tags.Items.RODS_WOODEN)
            .save(output)
        // Breeze Rod
        createPressing()
            .itemOutput(Items.BREEZE_ROD)
            .itemInput(Items.WIND_CHARGE, 6)
            .catalyst(Tags.Items.RODS_WOODEN)
            .save(output)

        circuits()
        redStones()
        diode()
    }

    private fun circuits() {
        // Basic
        HTShapedRecipeBuilder(RagiumItems.Circuits.BASIC)
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', Tags.Items.INGOTS_COPPER)
            .define('B', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .define('C', Tags.Items.INGOTS_IRON)
            .save(output)
        // Advanced
        HTShapedRecipeBuilder(RagiumItems.Circuits.ADVANCED)
            .cross4()
            .define('A', Tags.Items.INGOTS_GOLD)
            .define('B', Tags.Items.DUSTS_GLOWSTONE)
            .define('C', RagiumCommonTags.Items.CIRCUITS_BASIC)
            .save(output)

        // Circuit Board
        createPressing()
            .itemOutput(RagiumItems.CIRCUIT_BOARD)
            .itemInput(Tags.Items.DUSTS_REDSTONE)
            .catalyst(RagiumCommonTags.Items.PLATES_PLASTIC)
            .save(output)

        for (circuit: RagiumItems.Circuits in RagiumItems.Circuits.entries) {
            val input: TagKey<Item> = when (circuit) {
                RagiumItems.Circuits.BASIC -> Tags.Items.INGOTS_COPPER
                RagiumItems.Circuits.ADVANCED -> Tags.Items.INGOTS_GOLD
                RagiumItems.Circuits.ELITE -> RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL
                RagiumItems.Circuits.ULTIMATE -> RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL
            }
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
        HTShapedRecipeBuilder(RagiumBlocks.LEDBlocks.WHITE, 8)
            .hollow8()
            .define('A', Tags.Items.GLASS_BLOCKS)
            .define('B', RagiumItems.LED)
            .saveSuffixed(output, "_from_led")

        for (holderLike: RagiumBlocks.LEDBlocks in RagiumBlocks.LEDBlocks.entries) {
            HTShapedRecipeBuilder(holderLike, 8)
                .hollow8()
                .define('A', RagiumModTags.Items.LED_BLOCKS)
                .define('B', holderLike.color.tag)
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
