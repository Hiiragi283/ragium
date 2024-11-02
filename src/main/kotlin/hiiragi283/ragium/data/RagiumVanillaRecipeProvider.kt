package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.RagiumMaterials
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeJsonBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeJsonBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeJsonBuilder
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTSmithingModuleRecipe
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumMachineTypes
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
import net.minecraft.item.ItemStack
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
            .create(RagiumContents.Armors.STEEL_HELMET)
            .patterns(
                "AAA",
                "A A",
            ).input('A', RagiumContents.Ingots.STEEL)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Armors.STEEL_CHESTPLATE)
            .patterns(
                "A A",
                "AAA",
                "AAA",
            ).input('A', RagiumContents.Ingots.STEEL)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Armors.STEEL_LEGGINGS)
            .patterns(
                "AAA",
                "A A",
                "A A",
            ).input('A', RagiumContents.Ingots.STEEL)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Armors.STEEL_BOOTS)
            .patterns(
                "A A",
                "A A",
            ).input('A', RagiumContents.Ingots.STEEL)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)
        // stella
        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Armors.STELLA_GOGGLE)
            .patterns(
                "ABA",
                "A A",
            ).input('A', RagiumContents.Plates.STELLA)
            .input('B', RagiumContents.Gems.RAGI_CRYSTAL)
            .unlockedBy(RagiumContents.Plates.STELLA)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Armors.STELLA_JACKET)
            .patterns(
                "A A",
                "ABA",
                "AAA",
            ).input('A', RagiumContents.Plates.STELLA)
            .input('B', RagiumContents.Gems.RAGI_CRYSTAL)
            .unlockedBy(RagiumContents.Plates.STELLA)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Armors.STELLA_LEGGINGS)
            .patterns(
                "ABA",
                "A A",
                "A A",
            ).input('A', RagiumContents.Plates.STELLA)
            .input('B', RagiumContents.Gems.RAGI_CRYSTAL)
            .unlockedBy(RagiumContents.Plates.STELLA)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Armors.STELLA_BOOTS)
            .patterns(
                "A A",
                "ABA",
            ).input('A', RagiumContents.Plates.STELLA)
            .input('B', RagiumContents.Gems.RAGI_CRYSTAL)
            .unlockedBy(RagiumContents.Plates.STELLA)
            .offerTo(exporter)
    }

    //    Crafting - Tools    //

    private fun craftingTools(exporter: RecipeExporter) {
        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Misc.FORGE_HAMMER)
            .patterns(
                " AA",
                "BBA",
                " AA",
            ).input('A', RagiumContents.Ingots.RAGI_ALLOY)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumContents.Ingots.RAGI_ALLOY)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Misc.CRAFTER_HAMMER)
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
            .create(RagiumContents.Tools.STEEL_SWORD)
            .patterns(
                "B",
                "A",
                "A",
            ).input('A', RagiumContents.Ingots.STEEL)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Tools.STEEL_SHOVEL)
            .patterns(
                "B",
                "B",
                "A",
            ).input('A', RagiumContents.Ingots.STEEL)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Tools.STEEL_PICKAXE)
            .patterns(
                " B ",
                " B ",
                "AAA",
            ).input('A', RagiumContents.Ingots.STEEL)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Tools.STEEL_AXE)
            .patterns(
                "B ",
                "BA",
                "AA",
            ).input('A', RagiumContents.Ingots.STEEL)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Tools.STEEL_HOE)
            .patterns(
                "B ",
                "B ",
                "AA",
            ).input('A', RagiumContents.Ingots.STEEL)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)
        // invar

        // backpack
        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Misc.BACKPACK)
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
            .input(RagiumContents.Misc.BACKPACK)
            .input(createTagKey("dyes/${color.asString()}"))
            .unlockedBy(RagiumContents.Misc.BACKPACK)
            .offerPrefix(exporter, "dyed_${color.asString()}_")
    }

    private fun createTagKey(path: String): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier.of(TagUtil.C_TAG_NAMESPACE, path))

    //    Crafting - Foods    //

    private fun fluidIngredient(fluid: Fluid): Ingredient = DefaultCustomIngredients.components(
        Ingredient.ofItems(RagiumContents.Misc.FILLED_FLUID_CUBE),
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
            ).input('A', fluidIngredient(RagiumContents.Fluids.MILK.value))
            .input('B', Items.SWEET_BERRIES)
            .input('C', RagiumContents.Foods.CHOCOLATE)
            .input('D', Items.EGG)
            .input('E', RagiumBlocks.SPONGE_CAKE)
            .unlockedBy(RagiumBlocks.SPONGE_CAKE)
            .offerTo(exporter)

        HTShapelessRecipeJsonBuilder
            .create(RagiumContents.Foods.CHOCOLATE_BREAD)
            .input(Items.BREAD)
            .input(fluidIngredient(RagiumContents.Fluids.CHOCOLATE.value))
            .unlockedBy(Items.BREAD)
            .offerTo(exporter)

        HTShapelessRecipeJsonBuilder
            .create(RagiumContents.Foods.CHOCOLATE_APPLE)
            .input(Items.APPLE)
            .input(fluidIngredient(RagiumContents.Fluids.CHOCOLATE.value))
            .unlockedBy(Items.APPLE)
            .offerTo(exporter)

        /*HTShapelessRecipeJsonBuilder
            .create(RagiumContents.Foods.CANDY_APPLE)
            .input(Items.APPLE)
            .input(fluidIngredient(RagiumContents.Fluids.STARCH_SYRUP.value))
            .input(ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(Items.APPLE)
            .offerTo(exporter)*/
    }

    //    Crafting - Misc    //

    private fun craftingIngredients(exporter: RecipeExporter) {
        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Misc.RAGI_ALLOY_COMPOUND)
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
            .create(RagiumContents.Misc.RAGI_ALLOY_COMPOUND)
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
            .create(RagiumContents.Misc.SOLAR_PANEL)
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
            .create(RagiumContents.Misc.HEART_OF_THE_NETHER)
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
            .create(RagiumContents.Misc.TRADER_CATALOG)
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
            .create(RagiumContents.Misc.PROCESSOR_SOCKET)
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
            .create(RagiumAPI.getInstance().createFilledCube(Fluids.WATER))
            .input(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .input(Items.WATER_BUCKET)
            .unlockedBy(Items.WATER_BUCKET)
            .offerTo(exporter, RagiumAPI.id("water_cube"))

        HTShapelessRecipeJsonBuilder
            .create(RagiumAPI.getInstance().createFilledCube(Fluids.LAVA))
            .input(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .input(Items.LAVA_BUCKET)
            .unlockedBy(Items.LAVA_BUCKET)
            .offerTo(exporter, RagiumAPI.id("lava_cube"))

        HTShapelessRecipeJsonBuilder
            .create(RagiumAPI.getInstance().createFilledCube(RagiumContents.Fluids.MILK.value))
            .input(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .input(Items.MILK_BUCKET)
            .unlockedBy(Items.MILK_BUCKET)
            .offerTo(exporter, RagiumAPI.id("milk_cube"))

        HTShapelessRecipeJsonBuilder
            .create(RagiumAPI.getInstance().createFilledCube(RagiumContents.Fluids.HONEY.value))
            .input(RagiumContents.Misc.EMPTY_FLUID_CUBE)
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
            .create(RagiumContents.Misc.EMPTY_FLUID_CUBE, count)
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
                ).input('A', exporter1.tier.getPlate())
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
                .input('B', RagiumContents.Misc.FORGE_HAMMER)
                .unlockedBy(ItemTags.PLANKS)
                .offerTo(exporter)
        }
        // hulls
        listOf(
            RagiumMaterials.RAGI_ALLOY,
            RagiumMaterials.RAGI_STEEL,
            RagiumMaterials.REFINED_RAGI_STEEL,
        ).forEachIndexed { index: Int, material: RagiumMaterials ->
            val base: Block = HTMachineTier.entries[index].getBaseBlock()
            val hull: RagiumContents.Hulls = material.getHull() ?: return@forEachIndexed
            val plate: RagiumContents.Plates = material.getPlate() ?: return@forEachIndexed
            HTShapedRecipeJsonBuilder
                .create(hull)
                .patterns(
                    "AAA",
                    "A A",
                    "BBB",
                ).input('A', plate)
                .input('B', base)
                .unlockedBy(plate)
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
            ).input('A', RagiumContents.Plates.STELLA)
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
            .create(RagiumBlocks.FIREBOX)
            .patterns(
                "AAA",
                "ABA",
                "CCC",
            ).input('A', RagiumContents.Plates.RAGI_ALLOY)
            .input('B', Items.FURNACE)
            .input('C', Items.BRICKS)
            .unlockedBy(RagiumContents.Plates.RAGI_ALLOY)
            .offerTo(exporter)
        // generators
        createGenerator(
            exporter,
            RagiumMachineTypes.Generator.THERMAL,
            Items.MAGMA_BLOCK,
        )
        createGenerator(
            exporter,
            RagiumMachineTypes.Generator.COMBUSTION,
            RagiumContents.Misc.ENGINE,
        )
        createGenerator(
            exporter,
            RagiumMachineTypes.Generator.SOLAR,
            RagiumContents.Misc.SOLAR_PANEL,
        )
        createGenerator(
            exporter,
            RagiumMachineTypes.Generator.STEAM,
            Items.FURNACE,
        )
        // processors
        createProcessor(
            exporter,
            RagiumMachineTypes.Processor.ALLOY_FURNACE,
            Items.FURNACE,
        )
        createProcessor(
            exporter,
            RagiumMachineTypes.Processor.ASSEMBLER,
            HTMachineTier::getCircuit,
        )
        createProcessor(
            exporter,
            RagiumMachineTypes.Processor.CHEMICAL_REACTOR,
            Items.GLASS,
        )
        /*createMachine(
            exporter,
            RagiumMachineTypes.Processor.COMPRESSOR,
            Items.PISTON,
        )*/
        createProcessor(
            exporter,
            RagiumMachineTypes.Processor.ELECTROLYZER,
            Items.LIGHTNING_ROD,
        )
        createProcessor(
            exporter,
            RagiumMachineTypes.Processor.EXTRACTOR,
            Items.HOPPER,
        )
        createProcessor(
            exporter,
            RagiumMachineTypes.Processor.GRINDER,
            Items.FLINT,
        )
        createProcessor(
            exporter,
            RagiumMachineTypes.Processor.METAL_FORMER,
            RagiumBlocks.MANUAL_FORGE,
            RagiumContents.Misc.FORGE_HAMMER,
        )
        createProcessor(
            exporter,
            RagiumMachineTypes.Processor.MIXER,
            Items.CAULDRON,
        )
        createProcessor(
            exporter,
            RagiumMachineTypes.Processor.ROCK_GENERATOR,
            Items.LAVA_BUCKET,
            Items.WATER_BUCKET,
        )

        createProcessor(
            exporter,
            RagiumMachineTypes.BLAST_FURNACE,
            Items.BLAST_FURNACE,
        )
        createProcessor(
            exporter,
            RagiumMachineTypes.DISTILLATION_TOWER,
            RagiumContents.Misc.EMPTY_FLUID_CUBE,
        )
        createProcessor(
            exporter,
            RagiumMachineTypes.SAW_MILL,
            Items.STONECUTTER,
        )
    }

    private fun createGenerator(exporter: RecipeExporter, type: HTMachineConvertible, core: ItemConvertible) {
        createGenerator(exporter, type, Ingredient.ofItems(core))
    }

    private fun createGenerator(exporter: RecipeExporter, type: HTMachineConvertible, core: Ingredient) {
        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            val output: ItemStack = type.createItemStack(tier)

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
                        HTMachineTier.ADVANCED -> RagiumContents.Plates.STELLA
                    },
                ).input('B', core)
                .input('C', tier.getCircuit())
                .unlockedBy(tier.getCircuit())
                .offerTo(exporter, tier.createId(type))
        }
    }

    private fun createProcessor(
        exporter: RecipeExporter,
        type: HTMachineConvertible,
        left: ItemConvertible,
        right: ItemConvertible = left,
    ) {
        createMachineInternal(
            exporter,
            type,
            { Ingredient.ofItems(left) },
            { Ingredient.ofItems(right) },
        )
    }

    private fun createProcessor(
        exporter: RecipeExporter,
        type: HTMachineConvertible,
        left: (HTMachineTier) -> ItemConvertible,
        right: (HTMachineTier) -> ItemConvertible = left,
    ) {
        createMachineInternal(
            exporter,
            type,
            { Ingredient.ofItems(left(it)) },
            { Ingredient.ofItems(right(it)) },
        )
    }

    private fun createProcessor(
        exporter: RecipeExporter,
        type: HTMachineConvertible,
        left: TagKey<Item>,
        right: TagKey<Item> = left,
    ) {
        createMachineInternal(
            exporter,
            type,
            { Ingredient.fromTag(left) },
            { Ingredient.fromTag(right) },
        )
    }

    private fun createMachineInternal(
        exporter: RecipeExporter,
        type: HTMachineConvertible,
        left: (HTMachineTier) -> Ingredient,
        right: (HTMachineTier) -> Ingredient = left,
    ) {
        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            val output: ItemStack = type.createItemStack(tier)

            HTShapedRecipeJsonBuilder
                .create(output)
                .patterns(
                    "AAA",
                    "BCD",
                    "EEE",
                ).input('A', tier.getPlate())
                .input('B', left(tier))
                .input('C', tier.getHull())
                .input('D', right(tier))
                .input(
                    'E',
                    when (tier) {
                        HTMachineTier.PRIMITIVE -> RagiumContents.Plates.COPPER
                        HTMachineTier.BASIC -> RagiumContents.Plates.IRON
                        HTMachineTier.ADVANCED -> RagiumContents.Plates.STEEL
                    },
                ).unlockedBy(tier.getHull())
                .offerTo(exporter, tier.createId(type))
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
            .input('B', RagiumContents.Foods.BEE_WAX)
            .unlockedBy(RagiumContents.Foods.BEE_WAX)
            .offerTo(exporter)
    }

    //    Cooking   //

    private fun cookingRecipes(exporter: RecipeExporter) {
        HTCookingRecipeJsonBuilder.smeltAndBlast(
            exporter,
            RagiumContents.Misc.RAGI_ALLOY_COMPOUND,
            RagiumContents.Ingots.RAGI_ALLOY,
        )

        HTCookingRecipeJsonBuilder.smeltAndSmoke(
            exporter,
            Ingredient.ofItems(RagiumContents.Foods.DOUGH),
            Items.BREAD,
            RecipeProvider.conditionsFromItem(RagiumContents.Foods.DOUGH),
        )
    }

    //    Smithing    //

    private fun smithingRecipes(exporter: RecipeExporter) {
    }
}
