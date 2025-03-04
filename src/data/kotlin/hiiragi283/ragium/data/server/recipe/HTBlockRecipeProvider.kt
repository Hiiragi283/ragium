package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object HTBlockRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerAddons(output)
        registerCasings(output)

        registerMachines(output)
        registerConsumers(output)
        registerGenerators(output)
        registerProcessors(output)
    }

    //    Machines    //

    private fun registerAddons(output: RecipeOutput) {
        // E.N.I.
        HTShapedRecipeBuilder(RagiumBlocks.ENERGY_NETWORK_INTERFACE)
            .cross8()
            .define('A', HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .define('B', RagiumItemTags.CIRCUITS_ADVANCED)
            .define('C', Tags.Items.ENDER_PEARLS)
            .save(output)
    }

    private fun registerCasings(output: RecipeOutput) {
        fun register(
            result: ItemLike,
            topMetal: HTMaterialKey,
            glass: TagKey<Item>,
            center: TagKey<Item>,
        ) {
            HTShapedRecipeBuilder(result, 2)
                .cross8()
                .define('A', HTTagPrefix.INGOT, topMetal)
                .define('B', glass)
                .define('C', center)
                .save(output)
        }

        register(
            RagiumItems.MACHINE_CASING,
            RagiumMaterials.RAGI_ALLOY,
            Tags.Items.GLASS_BLOCKS_COLORLESS,
            Tags.Items.DUSTS_REDSTONE,
        )
        register(
            RagiumItems.CHEMICAL_MACHINE_CASING,
            RagiumMaterials.EMBER_ALLOY,
            RagiumItemTags.GLASS_BLOCKS_QUARTZ,
            Tags.Items.DUSTS_GLOWSTONE,
        )
        register(
            RagiumItems.PRECISION_MACHINE_CASING,
            RagiumMaterials.DURALUMIN,
            RagiumItemTags.GLASS_BLOCKS_OBSIDIAN,
            HTTagPrefix.GEM.createTag(RagiumMaterials.WARPED_CRYSTAL),
        )
    }

    private fun registerMachines(output: RecipeOutput) {
        // Manual Machine
        HTShapedRecipeBuilder(RagiumBlocks.MANUAL_GRINDER)
            .pattern("A  ")
            .pattern("BBB")
            .pattern("CCC")
            .define('A', Tags.Items.RODS_WOODEN)
            .define('B', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('C', Items.BRICKS)
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.PRIMITIVE_BLAST_FURNACE)
            .pattern("AAA")
            .pattern("ABA")
            .pattern("CCC")
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Items.BLAST_FURNACE)
            .define('C', Items.BRICKS)
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.DISENCHANTING_TABLE)
            .pattern(" A ")
            .pattern("BCB")
            .pattern("CCC")
            .define('A', Items.GRINDSTONE)
            .define('B', HTTagPrefix.GEM, RagiumMaterials.RAGI_CRYSTAL)
            .define('C', Tags.Items.OBSIDIANS_CRYING)
            .save(output)
    }

    private fun registerConsumers(output: RecipeOutput) {
        // Fisher
        HTShapedRecipeBuilder(HTMachineType.FISHER)
            .pattern(
                "ABA",
                "CDC",
                "EFE",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Tags.Items.TOOLS_FISHING_ROD)
            .define('C', Tags.Items.BARRELS_WOODEN)
            .define('D', RagiumItems.MACHINE_CASING)
            .define('E', HTTagPrefix.GEAR, VanillaMaterials.COPPER)
            .define('F', RagiumItemTags.CIRCUITS_BASIC)
            .save(output)
    }

    private fun registerGenerators(output: RecipeOutput) {
    }

    private fun registerProcessors(output: RecipeOutput) {
        // Assembler
        HTShapedRecipeBuilder(HTMachineType.ASSEMBLER)
            .pattern(
                "ABA",
                " C ",
                "DED",
            ).define('A', HTTagPrefix.BLOCK, CommonMaterials.STEEL)
            .define('B', Items.CRAFTER)
            .define('C', RagiumItems.MACHINE_CASING)
            .define('D', HTTagPrefix.GEAR, CommonMaterials.STEEL)
            .define('E', RagiumItemTags.CIRCUITS_ADVANCED)
            .save(output)
        // Auto Chisel
        HTShapedRecipeBuilder(HTMachineType.AUTO_CHISEL)
            .pattern(
                "AAA",
                " B ",
                "CDC",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', RagiumItems.MACHINE_CASING)
            .define('C', HTTagPrefix.GEAR, CommonMaterials.STEEL)
            .define('D', Items.STONECUTTER)
            .save(output)
        // Blast Furnace
        HTShapedRecipeBuilder(HTMachineType.BLAST_FURNACE)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Items.BLAST_FURNACE)
            .define('C', RagiumItems.MACHINE_CASING)
            .define('D', Items.DEEPSLATE_BRICKS)
            .save(output)

        HTShapedRecipeBuilder(HTMachineType.BLAST_FURNACE)
            .pattern(
                "AAA",
                "ABA",
                "CCC",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', RagiumBlocks.PRIMITIVE_BLAST_FURNACE)
            .define('C', Items.DEEPSLATE_BRICKS)
            .save(output, RagiumAPI.id("blast_furnace_alt"))
        // Compressor
        HTShapedRecipeBuilder(HTMachineType.COMPRESSOR)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Items.PISTON)
            .define('C', RagiumItems.MACHINE_CASING)
            .define('D', HTTagPrefix.GEAR, CommonMaterials.STEEL)
            .define('E', ItemTags.ANVIL)
            .save(output)
        // Crusher
        // Electric Furnace
        HTShapedRecipeBuilder(HTMachineType.ELECTRIC_FURNACE)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Tags.Items.PLAYER_WORKSTATIONS_FURNACES)
            .define('C', RagiumItems.MACHINE_CASING)
            .define('D', HTTagPrefix.COIL, VanillaMaterials.COPPER)
            .define('E', HTTagPrefix.BLOCK, VanillaMaterials.COPPER)
            .save(output)
        // Grinder
        HTShapedRecipeBuilder(HTMachineType.GRINDER)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Items.FLINT)
            .define('C', RagiumItems.MACHINE_CASING)
            .define('D', HTTagPrefix.GEAR, CommonMaterials.STEEL)
            .define('E', Items.HOPPER)
            .save(output)

        HTShapedRecipeBuilder(HTMachineType.GRINDER)
            .pattern(
                "AAA",
                " B ",
                "CDC",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', RagiumBlocks.MANUAL_GRINDER)
            .define('C', HTTagPrefix.GEAR, CommonMaterials.STEEL)
            .define('D', Items.HOPPER)
            .save(output, RagiumAPI.id("grinder_alt"))

        // Extractor
        HTShapedRecipeBuilder(HTMachineType.EXTRACTOR)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.EMBER_ALLOY)
            .define('B', Tags.Items.BUCKETS_EMPTY)
            .define('C', RagiumItems.CHEMICAL_MACHINE_CASING)
            .define('D', HTTagPrefix.COIL, VanillaMaterials.GOLD)
            .define('E', Items.HOPPER)
            .save(output)
        // Growth Chamber
        HTShapedRecipeBuilder(HTMachineType.GROWTH_CHAMBER)
            .pattern(
                "AAA",
                "BCB",
                "DEF",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.EMBER_ALLOY)
            .define('B', Tags.Items.GLASS_BLOCKS_COLORLESS)
            .define('C', RagiumItems.CHEMICAL_MACHINE_CASING)
            .define('D', ItemTags.AXES)
            .define('E', Items.HOPPER)
            .define('F', ItemTags.HOES)
            .save(output)
        // Infuser
        HTShapedRecipeBuilder(HTMachineType.INFUSER)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.EMBER_ALLOY)
            .define('B', Items.DISPENSER)
            .define('C', RagiumItems.CHEMICAL_MACHINE_CASING)
            .define('D', HTTagPrefix.COIL, VanillaMaterials.GOLD)
            .define('E', Items.HOPPER)
            .save(output)
        // Mixer
        HTShapedRecipeBuilder(HTMachineType.MIXER)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.EMBER_ALLOY)
            .define('B', Tags.Items.BUCKETS_EMPTY)
            .define('C', RagiumItems.CHEMICAL_MACHINE_CASING)
            .define('D', HTTagPrefix.COIL, VanillaMaterials.GOLD)
            .define('E', Items.CAULDRON)
            .save(output)
        // Refinery
        // Solidifier
        HTShapedRecipeBuilder(HTMachineType.SOLIDIFIER)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.EMBER_ALLOY)
            .define('B', RagiumItemTags.MOLDS)
            .define('C', RagiumItems.CHEMICAL_MACHINE_CASING)
            .define('D', HTTagPrefix.COIL, VanillaMaterials.GOLD)
            .define('E', Items.CAULDRON)
            .save(output)

        // Brewery
        HTShapedRecipeBuilder(HTMachineType.BREWERY)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.DURALUMIN)
            .define('B', Items.BREWING_STAND)
            .define('C', RagiumItems.PRECISION_MACHINE_CASING)
            .define('D', HTTagPrefix.GEAR, VanillaMaterials.NETHERITE)
            .define('E', RagiumItemTags.CIRCUITS_ELITE)
            .save(output)
        // Enchanter
        // Laser Assembly
        HTShapedRecipeBuilder(HTMachineType.LASER_ASSEMBLY)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.DURALUMIN)
            .define('B', RagiumItemTags.GLASS_BLOCKS_OBSIDIAN)
            .define('C', RagiumItems.PRECISION_MACHINE_CASING)
            .define('D', HTTagPrefix.GEM, RagiumMaterials.RAGI_CRYSTAL)
            .define('E', RagiumItemTags.CIRCUITS_ELITE)
            .save(output)
        // Multi Smelter
        HTShapedRecipeBuilder(HTMachineType.MULTI_SMELTER)
            .pattern(
                "AAA",
                "BCB",
                "DED",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.DURALUMIN)
            .define('B', Tags.Items.PLAYER_WORKSTATIONS_FURNACES)
            .define('C', RagiumItems.PRECISION_MACHINE_CASING)
            .define('D', HTTagPrefix.COIL, CommonMaterials.ALUMINUM)
            .define('E', HTTagPrefix.BLOCK, CommonMaterials.ALUMINUM)
            .save(output)
    }
}
