package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTMultiItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.extension.commonTag
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.HTBlockFamily
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.TransparentBlock
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredBlock

object HTBlockRecipeProvider : RagiumRecipeProvider.Child() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Soul Magma
        HTShapedRecipeBuilder(RagiumBlocks.SOUL_MAGMA_BLOCK)
            .hollow4()
            .define('A', ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .define('B', Items.MAGMA_BLOCK)
            .save(output)

        registerFamily(output, RagiumBlocks.RAGI_BRICK_FAMILY)
        registerFamily(output, RagiumBlocks.PLASTIC_FAMILY)

        registerBurners(output)
        registerDrums(output)

        registerGlasses(output)
        registerDecorations(output)
        registerLEDs(output)

        registerAddons(output)
        registerMachines(output)
    }

    //    Block Family    //

    private fun registerFamily(output: RecipeOutput, family: HTBlockFamily) {
        // Base -> Slab
        HTShapedRecipeBuilder(family.slab, 6, CraftingBookCategory.BUILDING)
            .pattern("AAA")
            .define('A', family.base)
            .save(output)

        SingleItemRecipeBuilder
            .stonecutting(Ingredient.of(family.base), RecipeCategory.BUILDING_BLOCKS, family.slab, 2)
            .unlockedBy("has_base", has(family.base))
            .savePrefixed(output)
        // Base -> Stairs
        HTShapedRecipeBuilder(family.stairs, 4, CraftingBookCategory.BUILDING)
            .pattern("A  ")
            .pattern("AA ")
            .pattern("AAA")
            .define('A', family.base)
            .save(output)

        SingleItemRecipeBuilder
            .stonecutting(Ingredient.of(family.base), RecipeCategory.BUILDING_BLOCKS, family.stairs)
            .unlockedBy("has_base", has(family.base))
            .savePrefixed(output)
        // Base -> Wall
        HTShapedRecipeBuilder(family.wall, 4, CraftingBookCategory.BUILDING)
            .pattern("AAA")
            .pattern("AAA")
            .define('A', family.base)
            .save(output)

        SingleItemRecipeBuilder
            .stonecutting(Ingredient.of(family.base), RecipeCategory.BUILDING_BLOCKS, family.wall)
            .unlockedBy("has_base", has(family.base))
            .savePrefixed(output)
    }

    //    Components    //

    private fun registerBurners(output: RecipeOutput) {
        RagiumBlocks.BURNERS.forEach { burner: DeferredBlock<Block> ->
            val core: ItemLike = when (burner) {
                RagiumBlocks.MAGMA_BURNER -> Items.MAGMA_BLOCK
                RagiumBlocks.SOUL_BURNER -> RagiumBlocks.SOUL_MAGMA_BLOCK
                RagiumBlocks.FIERY_BURNER -> RagiumBlocks.STORAGE_BLOCKS[RagiumMaterials.FIERY_COAL]!!
                else -> return
            }
            val base: Item = when (burner) {
                RagiumBlocks.MAGMA_BURNER -> Items.POLISHED_BLACKSTONE_BRICKS
                RagiumBlocks.SOUL_BURNER -> Items.END_STONE_BRICKS
                RagiumBlocks.FIERY_BURNER -> Items.RED_NETHER_BRICKS
                else -> return
            }
            // Shaped Crafting
            HTShapedRecipeBuilder(burner)
                .pattern("A A")
                .pattern("ABA")
                .pattern("CCC")
                .define('A', Items.IRON_BARS)
                .define('B', core)
                .define('C', base)
                .save(output)
        }
    }

    private fun registerDrums(output: RecipeOutput) {
        // Shaped Crafting
        HTShapedRecipeBuilder(RagiumBlocks.COPPER_DRUM)
            .cross8()
            .define('A', HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .define('B', Items.SMOOTH_STONE_SLAB)
            .define('C', Items.BUCKET)
            .save(output)
    }

    //    Decorations    //

    private fun registerGlasses(output: RecipeOutput) {
        mapOf(
            RagiumItemTags.SLAG to RagiumBlocks.CHEMICAL_GLASS,
            ItemTags.TRAPDOORS to RagiumBlocks.MOB_GLASS,
            Tags.Items.OBSIDIANS_NORMAL to RagiumBlocks.OBSIDIAN_GLASS,
            ItemTags.SOUL_FIRE_BASE_BLOCKS to RagiumBlocks.SOUL_GLASS,
        ).forEach { (input: TagKey<Item>, glass: DeferredBlock<out TransparentBlock>) ->
            HTMultiItemRecipeBuilder
                .blastFurnace(lookup)
                .itemInput(Tags.Items.GLASS_BLOCKS)
                .itemInput(input, 2)
                .itemOutput(glass)
                .save(output)
        }
    }

    private fun registerDecorations(output: RecipeOutput) {
        // Ragi-Bricks
        HTShapedRecipeBuilder(RagiumBlocks.RAGI_BRICKS, 2, CraftingBookCategory.BUILDING)
            .cross8()
            .define('A', HTTagPrefix.DUST, RagiumMaterials.RAGINITE)
            .define('B', Tags.Items.BRICKS_NORMAL)
            .define('C', Items.CLAY)
            .save(output)
        // Plastic Block
        HTShapedRecipeBuilder(RagiumBlocks.PLASTIC_BLOCK, 4, CraftingBookCategory.BUILDING)
            .hollow4()
            .define('A', RagiumItemTags.PLASTICS)
            .define('B', RagiumItems.FORGE_HAMMER)
            .save(output)
    }

    private fun registerLEDs(output: RecipeOutput) {
        // LED
        HTShapedRecipeBuilder(RagiumBlocks.getLedBlock(DyeColor.WHITE), 4, CraftingBookCategory.BUILDING)
            .hollow4()
            .define('A', Tags.Items.GLASS_BLOCKS)
            .define('B', RagiumItems.LED)
            .save(output, RagiumAPI.id("led_block"))

        RagiumBlocks.LED_BLOCKS.forEach { (color: DyeColor, block: DeferredBlock<Block>) ->
            // Shaped Crafting
            HTShapedRecipeBuilder(block, 4, CraftingBookCategory.BUILDING)
                .hollow8()
                .define('A', RagiumItemTags.LED_BLOCKS)
                .define('B', color.commonTag)
                .save(output)
        }
    }

    //    Machines    //

    private fun registerAddons(output: RecipeOutput) {
        // E.N.I.
        HTShapedRecipeBuilder(RagiumBlocks.ENERGY_NETWORK_INTERFACE)
            .cross8()
            .define('A', HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .define('B', RagiumItemTags.ADVANCED_CIRCUIT)
            .define('C', Tags.Items.ENDER_PEARLS)
            .save(output)
        // Slag Collector
        HTShapedRecipeBuilder(RagiumBlocks.SLAG_COLLECTOR)
            .cross8()
            .define('A', HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .define('B', Tags.Items.GRAVELS)
            .define('C', Items.HOPPER)
            .save(output)
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

        // Machine Casing
        fun casing(
            result: ItemLike,
            topMetal: HTMaterialKey,
            glass: Ingredient,
            gearMetal: HTMaterialKey,
        ) {
            HTShapedRecipeBuilder(result)
                .cross8()
                .define('A', HTTagPrefix.INGOT, topMetal)
                .define('B', glass)
                .define('C', HTTagPrefix.GEAR, gearMetal)
                .save(output)
        }

        casing(
            RagiumItems.MACHINE_CASING,
            RagiumMaterials.RAGI_ALLOY,
            Ingredient.of(Tags.Items.GLASS_BLOCKS),
            CommonMaterials.STEEL,
        )
        casing(
            RagiumItems.CHEMICAL_MACHINE_CASING,
            VanillaMaterials.GOLD,
            Ingredient.of(RagiumBlocks.CHEMICAL_GLASS),
            RagiumMaterials.DEEP_STEEL,
        )
        casing(
            RagiumItems.PRECISION_MACHINE_CASING,
            CommonMaterials.ALUMINUM,
            Ingredient.of(RagiumBlocks.OBSIDIAN_GLASS),
            VanillaMaterials.NETHERITE,
        )

        // Combustion Generator
        registerGenerator(
            output,
            HTMachineType.COMBUSTION_GENERATOR,
            RagiumItems.CHEMICAL_MACHINE_CASING,
            Ingredient.of(RagiumItems.ENGINE),
        )
        // Solar Generator
        registerGenerator(
            output,
            HTMachineType.SOLAR_GENERATOR,
            RagiumItems.PRECISION_MACHINE_CASING,
            Ingredient.of(RagiumItemTags.SOLAR_PANELS),
        )
        // Stirling Generator
        registerGenerator(
            output,
            HTMachineType.STIRLING_GENERATOR,
            RagiumItems.MACHINE_CASING,
            Ingredient.of(Tags.Items.PLAYER_WORKSTATIONS_FURNACES),
        )
        // Thermal Generator
        registerGenerator(
            output,
            HTMachineType.THERMAL_GENERATOR,
            RagiumItems.CHEMICAL_MACHINE_CASING,
            Ingredient.of(Tags.Items.BUCKETS_LAVA),
        )

        // Bedrock Miner
        registerMachine(
            output,
            HTMachineType.BEDROCK_MINER,
            RagiumItems.PRECISION_MACHINE_CASING,
            Ingredient.of(Items.BEACON),
            Ingredient.of(Items.NETHERITE_PICKAXE),
        )
        // Fisher
        registerMachine(
            output,
            HTMachineType.FISHER,
            RagiumItems.MACHINE_CASING,
            Ingredient.of(Items.FISHING_ROD),
            Ingredient.of(Tags.Items.BARRELS),
        )

        // Assembler
        registerMachine(
            output,
            HTMachineType.ASSEMBLER,
            RagiumItems.MACHINE_CASING,
            Ingredient.of(Items.CRAFTER),
            Ingredient.of(RagiumItemTags.ADVANCED_CIRCUIT),
        )
        // Blast Furnace
        registerMachine(
            output,
            HTMachineType.BLAST_FURNACE,
            RagiumItems.MACHINE_CASING,
            Ingredient.of(RagiumBlocks.PRIMITIVE_BLAST_FURNACE),
            Ingredient.of(Items.MAGMA_BLOCK),
        )
        // Compressor
        registerMachine(
            output,
            HTMachineType.COMPRESSOR,
            RagiumItems.MACHINE_CASING,
            Ingredient.of(RagiumItems.FORGE_HAMMER),
            Ingredient.of(Items.PISTON),
        )
        // Grinder
        registerMachine(
            output,
            HTMachineType.GRINDER,
            RagiumItems.MACHINE_CASING,
            Ingredient.of(RagiumBlocks.MANUAL_GRINDER),
            Ingredient.of(Items.FLINT),
        )
        // Multi Smelter
        registerMachine(
            output,
            HTMachineType.MULTI_SMELTER,
            RagiumItems.MACHINE_CASING,
            Ingredient.of(Tags.Items.RODS_BLAZE),
            Ingredient.of(Items.FURNACE),
        )

        // Extractor
        registerMachine(
            output,
            HTMachineType.EXTRACTOR,
            RagiumItems.CHEMICAL_MACHINE_CASING,
            Ingredient.of(Items.HOPPER),
            Ingredient.of(Items.PISTON),
        )
        // Growth Chamber
        registerMachine(
            output,
            HTMachineType.GROWTH_CHAMBER,
            RagiumItems.CHEMICAL_MACHINE_CASING,
            Ingredient.of(RagiumItems.LED),
            Ingredient.of(ItemTags.HOES),
            Ingredient.of(ItemTags.AXES),
        )
        // Infuser
        registerMachine(
            output,
            HTMachineType.INFUSER,
            RagiumItems.CHEMICAL_MACHINE_CASING,
            Ingredient.of(Items.HOPPER),
            Ingredient.of(Items.BUCKET),
        )
        // Mixer
        registerMachine(
            output,
            HTMachineType.MIXER,
            RagiumItems.CHEMICAL_MACHINE_CASING,
            Ingredient.of(Tags.Items.GLASS_BLOCKS),
            Ingredient.of(Items.CAULDRON),
        )
        // Refinery
        registerMachine(
            output,
            HTMachineType.REFINERY,
            RagiumItems.CHEMICAL_MACHINE_CASING,
            Ingredient.of(RagiumItems.CRUDE_OIL_BUCKET),
            Ingredient.of(Tags.Items.GLASS_BLOCKS),
        )
        // Solidifier
        registerMachine(
            output,
            HTMachineType.SOLIDIFIER,
            RagiumItems.CHEMICAL_MACHINE_CASING,
            Ingredient.of(Items.CAULDRON),
            Ingredient.of(Tags.Items.GLASS_BLOCKS),
        )

        // Alchemical Brewery
        registerMachine(
            output,
            HTMachineType.ALCHEMICAL_BREWERY,
            RagiumItems.PRECISION_MACHINE_CASING,
            Ingredient.of(Items.BREWING_STAND),
            HTTagPrefix.GEM.createIngredient(VanillaMaterials.EMERALD),
        )
        // Arcane Enchanter
        registerMachine(
            output,
            HTMachineType.ARCANE_ENCHANTER,
            RagiumItems.PRECISION_MACHINE_CASING,
            Ingredient.of(Items.ENCHANTING_TABLE),
            HTTagPrefix.GEM.createIngredient(VanillaMaterials.AMETHYST),
        )
        // Laser Assembly
        registerMachine(
            output,
            HTMachineType.LASER_ASSEMBLY,
            RagiumItems.PRECISION_MACHINE_CASING,
            Ingredient.of(Items.END_CRYSTAL),
            HTTagPrefix.GEM.createIngredient(RagiumMaterials.RAGI_CRYSTAL),
        )
    }

    private fun registerGenerator(
        output: RecipeOutput,
        machine: HTMachineType,
        casing: ItemLike,
        bottom: Ingredient,
    ) {
        val top: TagKey<Item> = when (casing) {
            RagiumItems.MACHINE_CASING -> Tags.Items.DUSTS_REDSTONE
            RagiumItems.CHEMICAL_MACHINE_CASING -> Tags.Items.STORAGE_BLOCKS_REDSTONE
            RagiumItems.PRECISION_MACHINE_CASING -> HTTagPrefix.GEM.createTag(RagiumMaterials.RAGI_CRYSTAL)
            else -> return
        }
        val metal: HTMaterialKey = when (casing) {
            RagiumItems.MACHINE_CASING -> VanillaMaterials.COPPER
            RagiumItems.CHEMICAL_MACHINE_CASING -> VanillaMaterials.GOLD
            RagiumItems.PRECISION_MACHINE_CASING -> CommonMaterials.ALUMINUM
            else -> return
        }
        val gearMetal: HTMaterialKey = when (casing) {
            RagiumItems.MACHINE_CASING -> VanillaMaterials.IRON
            RagiumItems.CHEMICAL_MACHINE_CASING -> CommonMaterials.STEEL
            RagiumItems.PRECISION_MACHINE_CASING -> VanillaMaterials.DIAMOND
            else -> return
        }
        HTShapedRecipeBuilder(machine)
            .pattern(" A ")
            .pattern("BCB")
            .pattern("DED")
            .define('A', top)
            .define('B', bottom)
            .define('C', casing)
            .define('D', HTTagPrefix.COIL, metal)
            .define('E', HTTagPrefix.GEAR, gearMetal)
            .save(output)
    }

    private fun registerMachine(
        output: RecipeOutput,
        machine: HTMachineType,
        casing: ItemLike,
        top: Ingredient,
        left: Ingredient,
        right: Ingredient = left,
    ) {
        val metal: HTMaterialKey = when (casing) {
            RagiumItems.MACHINE_CASING -> VanillaMaterials.COPPER
            RagiumItems.CHEMICAL_MACHINE_CASING -> VanillaMaterials.GOLD
            RagiumItems.PRECISION_MACHINE_CASING -> CommonMaterials.ALUMINUM
            else -> return
        }
        val circuit: TagKey<Item> = when (casing) {
            RagiumItems.MACHINE_CASING -> RagiumItemTags.BASIC_CIRCUIT
            RagiumItems.CHEMICAL_MACHINE_CASING -> RagiumItemTags.ADVANCED_CIRCUIT
            RagiumItems.PRECISION_MACHINE_CASING -> RagiumItemTags.ELITE_CIRCUIT
            else -> return
        }
        HTShapedRecipeBuilder(machine)
            .pattern(" A ")
            .pattern("BCD")
            .pattern("EFE")
            .define('A', top)
            .define('B', left)
            .define('C', casing)
            .define('D', right)
            .define('E', HTTagPrefix.COIL, metal)
            .define('F', circuit)
            .save(output)
    }
}
