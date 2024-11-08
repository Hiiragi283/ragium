package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeJsonBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeJsonBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeJsonBuilder
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTMachineBlock
import hiiragi283.ragium.api.recipe.HTSmithingModuleRecipe
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.item.HTBackpackItem
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.component.ComponentChanges
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.RecipeProvider
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture

class RagiumVanillaRecipeProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricRecipeProvider(output, registriesFuture) {
    override fun getName(): String = "Recipes/Vanilla"

    override fun generate(exporter: RecipeExporter) {
        craftingRecipes(exporter)
        cookingRecipes(exporter)
        smithingRecipes(exporter)

        exporter.accept(
            RagiumAPI.id("smithing/install_module"),
            HTSmithingModuleRecipe,
            null,
        )
    }

    //    Crafting    //

    private fun craftingRecipes(exporter: RecipeExporter) {
        craftingAlternatives(exporter)
        craftingArmors(exporter)
        craftingFluids(exporter)
        craftingFoods(exporter)
        craftingIngredients(exporter)
        craftingMachines(exporter)
        craftingTools(exporter)
    }

    //    Crafting - Armors    //

    private fun craftingArmors(exporter: RecipeExporter) {
        // steel
        HTShapedRecipeJsonBuilder
            .create(RagiumItems.STEEL_HELMET)
            .patterns(
                "AAA",
                "A A",
            ).input('A', RagiumContents.Ingots.STEEL)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.STEEL_CHESTPLATE)
            .patterns(
                "A A",
                "AAA",
                "AAA",
            ).input('A', RagiumContents.Ingots.STEEL)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.STEEL_LEGGINGS)
            .patterns(
                "AAA",
                "A A",
                "A A",
            ).input('A', RagiumContents.Ingots.STEEL)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.STEEL_BOOTS)
            .patterns(
                "A A",
                "A A",
            ).input('A', RagiumContents.Ingots.STEEL)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)
        // stella
        HTShapedRecipeJsonBuilder
            .create(RagiumItems.STELLA_GOGGLE)
            .patterns(
                "ABA",
                "A A",
            ).input('A', RagiumContents.Plates.STELLA)
            .input('B', RagiumContents.Gems.RAGIUM)
            .unlockedBy(RagiumContents.Plates.STELLA)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.STELLA_JACKET)
            .patterns(
                "A A",
                "ABA",
                "AAA",
            ).input('A', RagiumContents.Plates.STELLA)
            .input('B', RagiumContents.Gems.RAGIUM)
            .unlockedBy(RagiumContents.Plates.STELLA)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.STELLA_LEGGINGS)
            .patterns(
                "ABA",
                "A A",
                "A A",
            ).input('A', RagiumContents.Plates.STELLA)
            .input('B', RagiumContents.Gems.RAGIUM)
            .unlockedBy(RagiumContents.Plates.STELLA)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.STELLA_BOOTS)
            .patterns(
                "A A",
                "ABA",
            ).input('A', RagiumContents.Plates.STELLA)
            .input('B', RagiumContents.Gems.RAGIUM)
            .unlockedBy(RagiumContents.Plates.STELLA)
            .offerTo(exporter)
    }

    //    Crafting - Tools    //

    private fun craftingTools(exporter: RecipeExporter) {
        HTShapedRecipeJsonBuilder
            .create(RagiumItems.FORGE_HAMMER)
            .patterns(
                " AA",
                "BBA",
                " AA",
            ).input('A', RagiumContents.Ingots.RAGI_ALLOY)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumContents.Ingots.RAGI_ALLOY)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.CRAFTER_HAMMER)
            .patterns(
                " AA",
                "BBA",
                " AA",
            ).input('A', RagiumContents.StorageBlocks.STEEL)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumContents.StorageBlocks.STEEL)
            .offerTo(exporter)

        // steel
        HTShapedRecipeJsonBuilder
            .create(RagiumItems.STEEL_SWORD)
            .patterns(
                "B",
                "A",
                "A",
            ).input('A', RagiumContents.Ingots.STEEL)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.STEEL_SHOVEL)
            .patterns(
                "B",
                "B",
                "A",
            ).input('A', RagiumContents.Ingots.STEEL)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.STEEL_PICKAXE)
            .patterns(
                " B ",
                " B ",
                "AAA",
            ).input('A', RagiumContents.Ingots.STEEL)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.STEEL_AXE)
            .patterns(
                "B ",
                "BA",
                "AA",
            ).input('A', RagiumContents.Ingots.STEEL)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.STEEL_HOE)
            .patterns(
                "B ",
                "B ",
                "AA",
            ).input('A', RagiumContents.Ingots.STEEL)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)
        // backpack
        HTShapedRecipeJsonBuilder
            .create(RagiumItems.BACKPACK)
            .patterns(
                " A ",
                "ABA",
                "AAA",
            ).input('A', ItemTags.WOOL)
            .input('B', ConventionalItemTags.CHESTS)
            .unlockedBy(ItemTags.WOOL)
            .offerTo(exporter)

        DyeColor.entries.forEach { color: DyeColor -> dyeBackpack(exporter, color) }
    }

    private fun dyeBackpack(exporter: RecipeExporter, color: DyeColor) {
        HTShapelessRecipeJsonBuilder
            .create(HTBackpackItem.createStack(color))
            .input(RagiumItems.BACKPACK)
            .input(createTagKey("dyes/${color.asString()}"))
            .unlockedBy(RagiumItems.BACKPACK)
            .offerPrefix(exporter, "dyed_${color.asString()}_")
    }

    private fun createTagKey(path: String): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier.of(TagUtil.C_TAG_NAMESPACE, path))

    //    Crafting - Foods    //

    private fun fluidIngredient(fluid: Fluid): Ingredient = DefaultCustomIngredients.components(
        Ingredient.ofItems(RagiumItems.FILLED_FLUID_CUBE),
    ) { builder: ComponentChanges.Builder ->
        builder.add(RagiumComponentTypes.FLUID, fluid)
    }

    private fun craftingFoods(exporter: RecipeExporter) {
        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.SWEET_BERRIES_CAKE)
            .patterns(
                "ABA",
                "CDC",
                "EEE",
            ).input('A', fluidIngredient(RagiumFluids.MILK.value))
            .input('B', Items.SWEET_BERRIES)
            .input('C', RagiumItems.CHOCOLATE)
            .input('D', Items.EGG)
            .input('E', RagiumBlocks.SPONGE_CAKE)
            .unlockedBy(RagiumBlocks.SPONGE_CAKE)
            .offerTo(exporter)

        HTShapelessRecipeJsonBuilder
            .create(RagiumItems.CHOCOLATE_BREAD)
            .input(Items.BREAD)
            .input(fluidIngredient(RagiumFluids.CHOCOLATE.value))
            .unlockedBy(Items.BREAD)
            .offerTo(exporter)

        HTShapelessRecipeJsonBuilder
            .create(RagiumItems.CHOCOLATE_APPLE)
            .input(Items.APPLE)
            .input(fluidIngredient(RagiumFluids.CHOCOLATE.value))
            .unlockedBy(Items.APPLE)
            .offerTo(exporter)

        /*HTShapelessRecipeJsonBuilder
            .create(RagiumItems)
            .input(Items.APPLE)
            .input(fluidIngredient(RagiumFluids.STARCH_SYRUP.value))
            .input(ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(Items.APPLE)
            .offerTo(exporter)*/
    }

    //    Crafting - Misc    //

    private fun craftingIngredients(exporter: RecipeExporter) {
        HTShapedRecipeJsonBuilder
            .create(RagiumItems.RAGI_ALLOY_COMPOUND)
            .group("ragi_alloy_compound")
            .patterns(
                "AAA",
                "ABA",
                "AAA",
            ).input('A', RagiumContents.RawMaterials.CRUDE_RAGINITE)
            .input('B', ConventionalItemTags.COPPER_INGOTS)
            .unlockedBy(RagiumContents.RawMaterials.RAGINITE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.RAGI_ALLOY_COMPOUND)
            .group("ragi_alloy_compound")
            .patterns(
                " A ",
                "ABA",
                " A ",
            ).input('A', RagiumContents.Dusts.CRUDE_RAGINITE)
            .input('B', ConventionalItemTags.COPPER_INGOTS)
            .unlockedBy(RagiumContents.Dusts.CRUDE_RAGINITE)
            .offerSuffix(exporter, "_1")

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.SOLAR_PANEL)
            .patterns(
                "AAA",
                "BBB",
                "CCC",
            ).input('A', ConventionalItemTags.GLASS_PANES)
            .input('B', RagiumContents.Plates.SILICON)
            .input('C', RagiumContents.Plates.ALUMINUM)
            .unlockedBy(RagiumContents.Plates.SILICON)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.HEART_OF_THE_NETHER)
            .patterns(
                "ABA",
                "BCB",
                "ABA",
            ).input('A', Items.MAGMA_BLOCK)
            .input('B', Items.BLAZE_POWDER)
            .input('C', ConventionalItemTags.STORAGE_BLOCKS_NETHERITE)
            .unlockedBy(ConventionalItemTags.STORAGE_BLOCKS_NETHERITE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.SHAFT, 6)
            .patterns(
                "A",
                "A",
            ).input('A', ConventionalItemTags.STORAGE_BLOCKS_IRON)
            .unlockedBy(ConventionalItemTags.STORAGE_BLOCKS_IRON)
            .offerTo(exporter)

        HTShapelessRecipeJsonBuilder
            .create(RagiumItems.TRADER_CATALOG)
            .input(Items.BOOK)
            .input(ConventionalItemTags.EMERALD_GEMS)
            .input(ConventionalItemTags.CHESTS)
            .unlockedBy(Items.BOOK)
            .offerTo(exporter)
        // circuits
        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Circuits.PRIMITIVE)
            .patterns(
                "ABA",
                "CDC",
                "ABA",
            ).input('A', ConventionalItemTags.REDSTONE_DUSTS)
            .input('B', ConventionalItemTags.IRON_INGOTS)
            .input('C', ConventionalItemTags.COPPER_INGOTS)
            .input('D', ItemTags.PLANKS)
            .unlockedBy(ConventionalItemTags.REDSTONE_DUSTS)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Circuits.BASIC)
            .patterns(
                "ABA",
                "CDC",
                "ABA",
            ).input('A', RagiumContents.Dusts.RAGINITE)
            .input('B', RagiumContents.Plates.STEEL)
            .input('C', RagiumContents.Plates.GOLD)
            .input('D', RagiumContents.Circuits.PRIMITIVE)
            .unlockedBy(RagiumContents.Dusts.RAGINITE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.PROCESSOR_SOCKET)
            .patterns(
                "ABA",
                "BCB",
                "ABA",
            ).input('A', RagiumContents.Circuits.ADVANCED)
            .input('B', RagiumContents.Plates.GOLD)
            .input('C', RagiumContents.Plates.STELLA)
            .unlockedBy(RagiumContents.Plates.STELLA)
            .offerTo(exporter)
    }

    //    Crafting - Fluids    //

    private fun craftingFluids(exporter: RecipeExporter) {
        createEmptyFluidCube(exporter, Items.GLASS_PANE, 4)
        createEmptyFluidCube(exporter, RagiumContents.Plates.PLASTIC, 8, "_pe")
        createEmptyFluidCube(exporter, RagiumContents.Plates.ENGINEERING_PLASTIC, 16, "_pvc")

        HTShapelessRecipeJsonBuilder
            .create(RagiumItems.EMPTY_FLUID_CUBE)
            .input(RagiumItems.EMPTY_FLUID_CUBE)
            .unlockedBy(RagiumItems.EMPTY_FLUID_CUBE)
            .offerTo(exporter, RagiumAPI.id("clear_fluid_cube"))

        HTShapelessRecipeJsonBuilder
            .create(RagiumAPI.getInstance().createFilledCube(Fluids.WATER))
            .input(RagiumItems.EMPTY_FLUID_CUBE)
            .input(Items.WATER_BUCKET)
            .unlockedBy(Items.WATER_BUCKET)
            .offerTo(exporter, RagiumAPI.id("water_cube"))

        HTShapelessRecipeJsonBuilder
            .create(RagiumAPI.getInstance().createFilledCube(Fluids.LAVA))
            .input(RagiumItems.EMPTY_FLUID_CUBE)
            .input(Items.LAVA_BUCKET)
            .unlockedBy(Items.LAVA_BUCKET)
            .offerTo(exporter, RagiumAPI.id("lava_cube"))

        HTShapelessRecipeJsonBuilder
            .create(RagiumAPI.getInstance().createFilledCube(RagiumFluids.MILK.value))
            .input(RagiumItems.EMPTY_FLUID_CUBE)
            .input(Items.MILK_BUCKET)
            .unlockedBy(Items.MILK_BUCKET)
            .offerTo(exporter, RagiumAPI.id("milk_cube"))

        HTShapelessRecipeJsonBuilder
            .create(RagiumAPI.getInstance().createFilledCube(RagiumFluids.HONEY.value))
            .input(RagiumItems.EMPTY_FLUID_CUBE)
            .input(Items.HONEY_BOTTLE)
            .input(Items.HONEY_BOTTLE)
            .input(Items.HONEY_BOTTLE)
            .input(Items.HONEY_BOTTLE)
            .unlockedBy(Items.HONEY_BOTTLE)
            .offerTo(exporter, RagiumAPI.id("honey_cube"))
    }

    private fun createEmptyFluidCube(
        exporter: RecipeExporter,
        input: ItemConvertible,
        count: Int,
        suffix: String? = null,
    ) {
        HTShapedRecipeJsonBuilder
            .create(RagiumItems.EMPTY_FLUID_CUBE, count)
            .patterns(
                " A ",
                "A A",
                " A ",
            ).input('A', input)
            .unlockedBy(input)
            .offerTo(exporter, RagiumAPI.id("empty_fluid_cube$suffix"))
    }

    //    Crafting - Machines    //

    private fun craftingMachines(exporter: RecipeExporter) {
        // exporters
        RagiumContents.Exporters.entries.forEach { exporter1: RagiumContents.Exporters ->
            HTShapedRecipeJsonBuilder
                .create(exporter1)
                .patterns(
                    "AAA",
                    " B ",
                    "CDC",
                ).input('A', exporter1.tier.getMainPlate())
                .input('B', ConventionalItemTags.GLASS_BLOCKS)
                .input('C', exporter1.tier.getCoil())
                .input('D', RagiumItemTags.PIPES)
                .unlockedBy(exporter1.tier.getCoil())
                .offerTo(exporter)
        }
        // pipes
        RagiumContents.Pipes.entries.forEach { pipe: RagiumContents.Pipes ->
            val input: TagKey<Item> = when (pipe) {
                RagiumContents.Pipes.IRON -> RagiumContents.Plates.IRON.prefixedTagKey
                RagiumContents.Pipes.WOODEN -> ItemTags.PLANKS
                RagiumContents.Pipes.STEEL -> RagiumContents.Plates.STEEL.prefixedTagKey
                RagiumContents.Pipes.COPPER -> RagiumContents.Plates.COPPER.prefixedTagKey
                RagiumContents.Pipes.UNIVERSAL -> RagiumContents.Plates.REFINED_RAGI_STEEL.prefixedTagKey
            }
            HTShapedRecipeJsonBuilder
                .create(pipe, 3)
                .patterns(
                    "AAA",
                    " B ",
                    "AAA",
                ).input('A', input)
                .input('B', RagiumItems.FORGE_HAMMER)
                .unlockedBy(ItemTags.PLANKS)
                .offerTo(exporter)
        }
        // drums
        RagiumContents.Drums.entries.forEach { drum: RagiumContents.Drums ->
            HTShapedRecipeJsonBuilder
                .create(drum)
                .patterns(
                    "ABA",
                    "ACA",
                    "ABA",
                ).input('A', drum.tier.getSubPlate())
                .input('B', drum.tier.getMainPlate())
                .input('C', Items.BUCKET)
                .unlockedBy(drum.tier.getMainPlate())
                .offerTo(exporter)
        }
        // hulls
        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            val base: Block = tier.getBaseBlock()
            val hull: RagiumContents.Hulls = tier.getHull()
            HTShapedRecipeJsonBuilder
                .create(hull)
                .patterns(
                    "AAA",
                    "A A",
                    "BBB",
                ).input('A', tier.getMainPlate())
                .input('B', base)
                .unlockedBy(tier.getMainPlate())
                .offerTo(exporter)
        }
        // casings
        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.BASIC_CASING, 3)
            .patterns(
                "ABA",
                "BCB",
                "ABA",
            ).input('A', RagiumContents.Plates.IRON)
            .input('B', RagiumContents.Plates.STEEL)
            .input('C', ConventionalItemTags.REDSTONE_DUSTS)
            .unlockedBy(ConventionalItemTags.REDSTONE_DUSTS)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.ADVANCED_CASING, 3)
            .patterns(
                "ABA",
                "BCB",
                "ABA",
            ).input('A', RagiumContents.Plates.DEEP_STEEL)
            .input('B', RagiumContents.Plates.ALUMINUM)
            .input('C', RagiumContents.Gems.RAGI_CRYSTAL)
            .unlockedBy(RagiumContents.Gems.RAGI_CRYSTAL)
            .offerTo(exporter)
        // machines
        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.MANUAL_FORGE)
            .patterns(
                "AAA",
                " B ",
                "BBB",
            ).input('A', RagiumContents.StorageBlocks.RAGI_ALLOY)
            .input('B', Blocks.SMOOTH_STONE)
            .unlockedBy(RagiumContents.StorageBlocks.RAGI_ALLOY)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.MANUAL_GRINDER)
            .patterns(
                "A  ",
                "BBB",
                "CCC",
            ).input('A', ConventionalItemTags.WOODEN_RODS)
            .input('B', RagiumContents.Ingots.RAGI_ALLOY)
            .input('C', Items.SMOOTH_STONE)
            .unlockedBy(RagiumContents.Ingots.RAGI_ALLOY)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.MANUAL_MIXER)
            .patterns(
                "A A",
                "A A",
                "BBB",
            ).input('A', RagiumContents.Plates.RAGI_ALLOY)
            .input('B', ItemTags.TERRACOTTA)
            .unlockedBy(RagiumContents.Plates.RAGI_ALLOY)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.NETWORK_INTERFACE)
            .patterns(
                "ABA",
                "BCB",
                "ABA",
            ).input('A', RagiumContents.Plates.DEEP_STEEL)
            .input('B', RagiumContents.Plates.STEEL)
            .input('C', RagiumContents.Circuits.ADVANCED)
            .unlockedBy(RagiumContents.Plates.DEEP_STEEL)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.LARGE_PROCESSOR)
            .patterns(
                "ABA",
                "BCB",
                "ABA",
            ).input('A', RagiumContents.Plates.DEEP_STEEL)
            .input('B', RagiumContents.Plates.RAGI_ALLOY)
            .input('C', RagiumItems.RAGI_CRYSTAL_PROCESSOR)
            .unlockedBy(RagiumItems.RAGI_CRYSTAL_PROCESSOR)
            .offerTo(exporter)
        // generators
        createGenerator(
            exporter,
            RagiumMachineKeys.THERMAL_GENERATOR,
            Items.MAGMA_BLOCK,
        )
        createGenerator(
            exporter,
            RagiumMachineKeys.COMBUSTION_GENERATOR,
            RagiumItems.ENGINE,
        )
        createGenerator(
            exporter,
            RagiumMachineKeys.SOLAR_PANEL,
            RagiumItems.SOLAR_PANEL,
        )
        createGenerator(
            exporter,
            RagiumMachineKeys.STEAM_GENERATOR,
            Items.FURNACE,
        )
        // processors
        createProcessor(
            exporter,
            RagiumMachineKeys.ALLOY_FURNACE,
            Items.FURNACE,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.ASSEMBLER,
            RagiumContents.Circuits.PRIMITIVE,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.BLAST_FURNACE,
            Items.BLAST_FURNACE,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.CHEMICAL_REACTOR,
            Items.GLASS,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.DISTILLATION_TOWER,
            RagiumItems.EMPTY_FLUID_CUBE,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.ELECTROLYZER,
            Items.LIGHTNING_ROD,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.EXTRACTOR,
            Items.HOPPER,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.GRINDER,
            Items.FLINT,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.LASER_TRANSFORMER,
            RagiumItems.LASER_EMITTER,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.METAL_FORMER,
            RagiumBlocks.MANUAL_FORGE,
            RagiumItems.FORGE_HAMMER,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.MIXER,
            Items.CAULDRON,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.MULTI_SMELTER,
            RagiumItems.HEART_OF_THE_NETHER,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.ROCK_GENERATOR,
            Items.LAVA_BUCKET,
            Items.WATER_BUCKET,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.SAW_MILL,
            Items.STONECUTTER,
        )
    }

    private fun createGenerator(exporter: RecipeExporter, key: HTMachineKey, core: ItemConvertible) {
        createGenerator(exporter, key, Ingredient.ofItems(core))
    }

    private fun createGenerator(exporter: RecipeExporter, key: HTMachineKey, core: Ingredient) {
        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            val output: HTMachineBlock = key.entry.getBlock(tier)
            HTShapedRecipeJsonBuilder
                .create(output)
                .patterns(
                    "AAA",
                    "ABA",
                    "ACA",
                ).input(
                    'A',
                    when (tier) {
                        HTMachineTier.PRIMITIVE -> RagiumContents.Plates.IRON
                        HTMachineTier.BASIC -> RagiumContents.Plates.STEEL
                        HTMachineTier.ADVANCED -> RagiumContents.Plates.ALUMINUM
                    },
                ).input('B', core)
                .input('C', tier.getCircuit())
                .unlockedBy(tier.getCircuit())
                .offerTo(exporter, tier.createId(key))
        }
    }

    private fun createProcessor(
        exporter: RecipeExporter,
        key: HTMachineKey,
        left: ItemConvertible,
        right: ItemConvertible = left,
    ) {
        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            val output: HTMachineBlock = key.entry.getBlock(tier)
            HTShapedRecipeJsonBuilder
                .create(output)
                .patterns(
                    "AAA",
                    "BCD",
                    "EEE",
                ).input('A', tier.getMainPlate())
                .input('B', left)
                .input('C', tier.getHull())
                .input('D', right)
                .input('E', tier.getSteelPlate())
                .unlockedBy(tier.getHull())
                .offerTo(exporter, tier.createId(key))
        }
    }

    //    Crafting - Alternatives    //

    private fun craftingAlternatives(exporter: RecipeExporter) {
        /*HTShapedRecipeJsonBuilder
            .create(Items.NETHER_STAR)
            .patterns(
                " A ",
                "DEB",
                " C ",
            ).input('A', RagiumContents.Element.RAGIUM.dustItem)
            .input('B', RagiumContents.Element.RIGIUM.dustItem)
            .input('C', RagiumContents.Element.RUGIUM.dustItem)
            .input('D', RagiumContents.Element.REGIUM.dustItem)
            .input('E', RagiumContents.Element.ROGIUM.dustItem)
            .unlockedBy(Items.NETHER_STAR)
            .offerTo(exporter)*/

        HTShapelessRecipeJsonBuilder
            .create(Items.STICKY_PISTON)
            .input(ConventionalItemTags.SLIME_BALLS)
            .input(Items.PISTON)
            .unlockedBy(Items.PISTON)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(Items.LEAD, 2)
            .patterns(
                "AA ",
                "AB ",
                "  A",
            ).input('A', ConventionalItemTags.STRINGS)
            .input('B', ConventionalItemTags.SLIME_BALLS)
            .unlockedBy(ConventionalItemTags.SLIME_BALLS)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(Items.CANDLE)
            .patterns(
                "A",
                "B",
            ).input('A', ConventionalItemTags.STRINGS)
            .input('B', RagiumItems.BEE_WAX)
            .unlockedBy(RagiumItems.BEE_WAX)
            .offerTo(exporter)
    }

    //    Cooking   //

    private fun cookingRecipes(exporter: RecipeExporter) {
        HTCookingRecipeJsonBuilder.smeltAndBlast(
            exporter,
            RagiumItems.RAGI_ALLOY_COMPOUND,
            RagiumContents.Ingots.RAGI_ALLOY,
        )

        HTCookingRecipeJsonBuilder.smeltAndSmoke(
            exporter,
            Ingredient.ofItems(RagiumItems.DOUGH),
            Items.BREAD,
            RecipeProvider.conditionsFromItem(RagiumItems.DOUGH),
        )
    }

    //    Smithing    //

    private fun smithingRecipes(exporter: RecipeExporter) {
    }
}
