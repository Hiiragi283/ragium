package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTMachineRecipeJsonBuilder
import hiiragi283.ragium.api.data.recipe.HTMachineRecipeJsonBuilders
import hiiragi283.ragium.api.data.recipe.HTMaterialItemRecipeRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTFluidIngredient
import hiiragi283.ragium.api.recipe.HTFluidResult
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.recipe.machine.HTRecipeComponentTypes
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineTypes
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeKeys
import java.util.concurrent.CompletableFuture

class RagiumMachineRecipeProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricRecipeProvider(output, registriesFuture) {
    override fun getName(): String = "Recipes/Machine"

    override fun generate(exporter: RecipeExporter) {
        alloyFurnaceRecipes(exporter)
        assembler(exporter)
        blastFurnace(exporter)
        chemicalReactor(exporter)
        compressor(exporter)
        decompressor(exporter)
        distillation(exporter)
        electrolyzer(exporter)
        extractor(exporter)
        fluidDrill(exporter)
        grinder(exporter)
        metalFormer(exporter)
        mixer(exporter)
        mobExtractor(exporter)
        rockGenerator(exporter)
        sawMill(exporter)
        // patterned
        HTMaterialItemRecipeRegistry.generateRecipes(exporter, ::exporterWrapper1, ::exporterWrapper2)

        test(exporter)
    }

    private fun exporterWrapper1(exporter: RecipeExporter, bool: Boolean): RecipeExporter =
        withConditions(exporter, HTHardModeResourceCondition.fromBool((bool)))

    private fun exporterWrapper2(exporter: RecipeExporter, tagKey: TagKey<Item>): RecipeExporter =
        withConditions(exporter, ResourceConditions.tagsPopulated(tagKey))

    //    Alloy Furnace    //

    private fun alloyFurnaceRecipes(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilders.createAlloy(
            exporter,
            HTItemIngredient.ofItem(ConventionalItemTags.COPPER_INGOTS),
            HTItemIngredient.ofItem(RagiumContents.Dusts.CRUDE_RAGINITE, 4),
            HTItemResult.ofItem(RagiumContents.Ingots.RAGI_ALLOY),
        )
        HTMachineRecipeJsonBuilders.createAlloy(
            exporter,
            HTItemIngredient.ofItem(ConventionalItemTags.COPPER_INGOTS),
            HTItemIngredient.ofItem(RagiumContents.Dusts.RAGINITE),
            HTItemResult.ofItem(RagiumContents.Ingots.RAGI_ALLOY),
            recipeId = HTMachineRecipeJsonBuilders
                .createRecipeId(RagiumContents.Ingots.RAGI_ALLOY)
                .withSuffixedPath("_alt"),
        )
        HTMachineRecipeJsonBuilders.createAlloy(
            exporter,
            HTItemIngredient.ofItem(ConventionalItemTags.IRON_INGOTS, 2),
            HTItemIngredient.ofItem(RagiumItemTags.NICKEL_INGOTS),
            HTItemResult.ofItem(RagiumContents.Ingots.INVAR, 3),
        )
        HTMachineRecipeJsonBuilders.createAlloy(
            exporter,
            HTItemIngredient.ofItem(ConventionalItemTags.GOLD_INGOTS, 5),
            HTItemIngredient.ofItem(Items.NETHERITE_SCRAP, 3),
            HTItemResult.ofItem(Items.NETHERITE_INGOT, 2),
            HTMachineTier.ADVANCED,
        )
    }

    //    Assembler    //

    private fun assembler(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilders.createAssembler(
            exporter,
            listOf(
                HTItemIngredient.ofItem(RagiumItemTags.STEEL_PLATES, 8),
                HTItemIngredient.ofItem(RagiumContents.Plates.RAGI_STEEL, 8),
            ),
            HTItemResult.ofItem(RagiumContents.Misc.ENGINE),
        )
        HTMachineRecipeJsonBuilders.createAssembler(
            exporter,
            listOf(
                HTItemIngredient.ofItem(RagiumContents.Gems.RAGI_CRYSTAL, 8),
                HTItemIngredient.ofItem(RagiumContents.Misc.PROCESSOR_SOCKET),
            ),
            HTItemResult.ofItem(RagiumContents.Misc.RAGI_CRYSTAL_PROCESSOR),
            tier = HTMachineTier.ADVANCED,
        )
        HTMachineRecipeJsonBuilders.createAssembler(
            exporter,
            listOf(
                HTItemIngredient.ofItem(RagiumItemTags.ALUMINUM_PLATES),
                HTItemIngredient.ofItem(RagiumContents.Misc.BASALT_MESH),
            ),
            HTItemResult.ofItem(RagiumContents.Plates.STELLA),
            tier = HTMachineTier.BASIC,
        )
        // circuits
        HTMachineRecipeJsonBuilders.createAssembler(
            exporter,
            listOf(
                HTItemIngredient.ofItem(RagiumContents.Plates.PLASTIC),
                HTItemIngredient.ofItem(RagiumItemTags.COPPER_PLATES),
            ),
            HTItemResult.ofItem(RagiumContents.Circuits.PRIMITIVE),
        )
        HTMachineRecipeJsonBuilders.createAssembler(
            exporter,
            listOf(
                HTItemIngredient.ofItem(RagiumContents.Plates.SILICON),
                HTItemIngredient.ofItem(RagiumItemTags.GOLD_PLATES),
            ),
            HTItemResult.ofItem(RagiumContents.Circuits.BASIC),
            tier = HTMachineTier.BASIC,
        )
        HTMachineRecipeJsonBuilders.createAssembler(
            exporter,
            listOf(
                HTItemIngredient.ofItem(RagiumContents.Plates.ENGINEERING_PLASTIC),
                HTItemIngredient.ofItem(RagiumContents.Plates.RAGI_ALLOY),
            ),
            HTItemResult.ofItem(RagiumContents.Circuits.ADVANCED),
            tier = HTMachineTier.ADVANCED,
        )
        // coils
        HTMachineRecipeJsonBuilders.createAssembler(
            exporter,
            listOf(
                HTItemIngredient.ofItem(ConventionalItemTags.COPPER_INGOTS, 8),
                HTItemIngredient.ofItem(RagiumBlocks.SHAFT),
            ),
            HTItemResult.ofItem(RagiumContents.Coils.COPPER),
        )
        HTMachineRecipeJsonBuilders.createAssembler(
            exporter,
            listOf(
                HTItemIngredient.ofItem(ConventionalItemTags.GOLD_INGOTS, 8),
                HTItemIngredient.ofItem(RagiumBlocks.SHAFT),
            ),
            HTItemResult.ofItem(RagiumContents.Coils.GOLD),
        )
        HTMachineRecipeJsonBuilders.createAssembler(
            exporter,
            listOf(
                HTItemIngredient.ofItem(RagiumContents.Ingots.RAGI_ALLOY, 8),
                HTItemIngredient.ofItem(RagiumBlocks.SHAFT),
            ),
            HTItemResult.ofItem(RagiumContents.Coils.RAGI_ALLOY),
        )
        // motors
        HTMachineRecipeJsonBuilders.createAssembler(
            exporter,
            listOf(
                HTItemIngredient.ofItem(RagiumItemTags.IRON_PLATES, 8),
                HTItemIngredient.ofItem(RagiumContents.Coils.COPPER),
            ),
            HTItemResult.ofItem(RagiumContents.Motors.PRIMITIVE),
        )
        HTMachineRecipeJsonBuilders.createAssembler(
            exporter,
            listOf(
                HTItemIngredient.ofItem(RagiumItemTags.IRON_PLATES, 8),
                HTItemIngredient.ofItem(RagiumContents.Coils.GOLD),
            ),
            HTItemResult.ofItem(RagiumContents.Motors.BASIC),
        )
        HTMachineRecipeJsonBuilders.createAssembler(
            exporter,
            listOf(
                HTItemIngredient.ofItem(RagiumItemTags.IRON_PLATES, 8),
                HTItemIngredient.ofItem(RagiumContents.Coils.RAGI_ALLOY),
            ),
            HTItemResult.ofItem(RagiumContents.Motors.ADVANCED),
        )
    }

    //    Blast Furnace    //

    private fun blastFurnace(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilders.createBlast(
            exporter,
            listOf(
                HTItemIngredient.ofItem(ConventionalItemTags.IRON_INGOTS),
                HTItemIngredient.ofItem(RagiumContents.Dusts.RAGINITE, 4),
            ),
            HTItemResult.ofItem(RagiumContents.Ingots.RAGI_STEEL),
        )
        HTMachineRecipeJsonBuilders.createBlast(
            exporter,
            listOf(
                HTItemIngredient.ofItem(ConventionalItemTags.IRON_INGOTS),
                HTItemIngredient.ofItem(ItemTags.COALS, 4),
            ),
            HTItemResult.ofItem(RagiumContents.Ingots.STEEL),
        )
        HTMachineRecipeJsonBuilders.createBlast(
            exporter,
            listOf(
                HTItemIngredient.ofItem(ConventionalItemTags.REDSTONE_DUSTS, 4),
                HTItemIngredient.ofItem(RagiumContents.Dusts.RAGINITE, 5),
            ),
            HTItemResult.ofItem(RagiumContents.Gems.RAGI_CRYSTAL),
            HTMachineTier.BASIC,
        )
        HTMachineRecipeJsonBuilders.createBlast(
            exporter,
            listOf(
                HTItemIngredient.ofItem(RagiumItemTags.STEEL_INGOTS),
                HTItemIngredient.ofItem(RagiumContents.Dusts.RAGI_CRYSTAL, 4),
                HTItemIngredient.ofItem(ConventionalItemTags.QUARTZ_GEMS),
            ),
            HTItemResult.ofItem(RagiumContents.Ingots.REFINED_RAGI_STEEL),
            HTMachineTier.BASIC,
        )
        HTMachineRecipeJsonBuilders.createBlast(
            exporter,
            listOf(
                HTItemIngredient.ofItem(ConventionalItemTags.QUARTZ_GEMS, 2),
                HTItemIngredient.ofItem(ItemTags.COALS, 4),
            ),
            HTItemResult.ofItem(RagiumContents.Plates.SILICON),
            HTMachineTier.BASIC,
        )
        HTMachineRecipeJsonBuilders.createBlast(
            exporter,
            listOf(
                HTItemIngredient.ofItem(RagiumContents.Fluids.BATTER),
                HTItemIngredient.ofItem(RagiumContents.Foods.BUTTER),
            ),
            HTItemResult.ofItem(RagiumBlocks.SPONGE_CAKE),
        )
        HTMachineRecipeJsonBuilders.createBlast(
            exporter,
            listOf(
                HTItemIngredient.ofItem(RagiumContents.Dusts.ALUMINA),
                HTItemIngredient.ofItem(ItemTags.COALS, 4),
            ),
            HTItemResult.ofItem(RagiumContents.Ingots.ALUMINUM),
            HTMachineTier.ADVANCED,
        )
    }

    //    Chemical Reactor    //

    private fun chemicalReactor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(RagiumContents.Fluids.METHANE)
            .addInput(RagiumContents.Fluids.WATER)
            .setCatalyst(Items.HEART_OF_THE_SEA)
            .addOutput(RagiumContents.Fluids.HYDROGEN)
            .addOutput(RagiumContents.Fluids.METHANOL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(RagiumContents.Fluids.ETHYLENE)
            .addInput(RagiumContents.Fluids.WATER)
            .setCatalyst(RagiumContents.Fluids.SULFURIC_ACID)
            .addOutput(RagiumContents.Fluids.ETHANOL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, HTMachineTier.BASIC)
            .addInput(Items.BLAZE_ROD)
            .addInput(RagiumContents.Fluids.WATER, 2)
            .addOutput(RagiumContents.Fluids.SULFURIC_ACID, 2)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, HTMachineTier.BASIC)
            .addInput(Items.BREEZE_ROD)
            .addInput(RagiumContents.Fluids.WATER, 2)
            .addOutput(RagiumContents.Fluids.NITRIC_ACID, 2)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(RagiumContents.RawMaterials.BAUXITE)
            .addInput(RagiumContents.Fluids.SODIUM_HYDROXIDE)
            .addOutput(RagiumContents.Dusts.ALUMINA)
            .addOutput(RagiumContents.Fluids.WATER)
            .offerTo(exporter)

        // Fuels
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(RagiumItemTags.ALCOHOL, 3)
            .addInput(RagiumItemTags.ORGANIC_OILS)
            .addOutput(RagiumContents.Fluids.BIO_FUEL, 3)
            .addOutput(RagiumContents.Fluids.GLYCEROL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(RagiumItemTags.FUEL_CUBES, 4)
            .addInput(RagiumContents.Fluids.NITRIC_ACID)
            .addInput(RagiumContents.Fluids.SULFURIC_ACID)
            .addOutput(RagiumContents.Fluids.NITRO_FUEL, 4)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE, 2)
            .offerTo(exporter)
        // Dynamite
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, HTMachineTier.BASIC)
            .addInput(RagiumContents.Fluids.GLYCEROL)
            .addInput(RagiumContents.Fluids.NITRIC_ACID, 3)
            .addInput(RagiumContents.Fluids.SULFURIC_ACID, 3)
            .addOutput(RagiumContents.Fluids.NITRO_GLYCERIN)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE, 6)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(Items.PAPER)
            .addInput(ConventionalItemTags.STRINGS)
            .addInput(RagiumContents.Fluids.NITRO_GLYCERIN)
            .addOutput(RagiumContents.Misc.DYNAMITE, 2)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)
        // TNT
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .addInput(RagiumContents.Fluids.AROMATIC_COMPOUNDS)
            .addInput(RagiumContents.Fluids.NITRIC_ACID, 3)
            .addInput(RagiumContents.Fluids.SULFURIC_ACID, 3)
            .addOutput(RagiumContents.Fluids.TRINITROTOLUENE)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE, 6)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .addInput(RagiumContents.Fluids.TRINITROTOLUENE)
            .addInput(ItemTags.SAND)
            .addOutput(Items.TNT, 12)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        // PE
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(RagiumContents.Fluids.ETHYLENE)
            .addInput(RagiumContents.Fluids.OXYGEN)
            .addOutput(RagiumContents.Plates.PLASTIC)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        // PC

        registerOxidize(exporter, Items.CHISELED_COPPER, Items.OXIDIZED_CHISELED_COPPER)
        registerOxidize(exporter, Items.COPPER_BLOCK, Items.OXIDIZED_COPPER)
        registerOxidize(exporter, Items.COPPER_BULB, Items.OXIDIZED_COPPER_BULB)
        registerOxidize(exporter, Items.COPPER_DOOR, Items.OXIDIZED_COPPER_DOOR)
        registerOxidize(exporter, Items.COPPER_GRATE, Items.OXIDIZED_COPPER_GRATE)
        registerOxidize(exporter, Items.COPPER_TRAPDOOR, Items.OXIDIZED_COPPER_TRAPDOOR)
        registerOxidize(exporter, Items.CUT_COPPER, Items.OXIDIZED_CUT_COPPER)
        registerOxidize(exporter, Items.CUT_COPPER_SLAB, Items.OXIDIZED_CUT_COPPER_SLAB)
        registerOxidize(exporter, Items.CUT_COPPER_STAIRS, Items.OXIDIZED_CUT_COPPER_STAIRS)
    }

    private fun registerOxidize(exporter: RecipeExporter, before: ItemConvertible, after: ItemConvertible) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(before)
            .addInput(RagiumContents.Fluids.OXYGEN)
            .addOutput(after)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)
    }

    //    Compressor    //

    private fun compressor(exporter: RecipeExporter) {
        /*HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.COMPRESSOR)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addInput(Items.WATER_BUCKET)
            .addOutput(RagiumContents.Fluids.WATER)
            .addOutput(Items.BUCKET)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.COMPRESSOR)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addInput(Items.LAVA_BUCKET)
            .addOutput(RagiumContents.Fluids.LAVA)
            .addOutput(Items.BUCKET)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.COMPRESSOR)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addInput(Items.MILK_BUCKET)
            .addOutput(RagiumContents.Fluids.MILK)
            .addOutput(Items.BUCKET)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.COMPRESSOR)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addInput(Items.HONEY_BOTTLE, 4)
            .addOutput(RagiumContents.Fluids.HONEY)
            .addOutput(Items.GLASS_BOTTLE, 4)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.COMPRESSOR)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addInput(Items.HONEY_BLOCK)
            .addOutput(RagiumContents.Fluids.HONEY)
            .offerSuffix(exporter, suffix = "_alt")*/
    }

    //    Decompressor    //

    private fun decompressor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.DECOMPRESSOR)
            .addInput(Items.KELP, 64)
            .addOutput(Items.DRIED_KELP, 64)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.DECOMPRESSOR)
            .addInput(ConventionalItemTags.STORAGE_BLOCKS_DRIED_KELP, 64)
            .addOutput(Items.DRIED_KELP_BLOCK, 64)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.DECOMPRESSOR)
            .addInput(RagiumContents.Fluids.LAVA)
            .addOutput(RagiumBlocks.POROUS_NETHERRACK)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.DECOMPRESSOR)
            .addInput(Items.MUD)
            .addOutput(Items.CLAY)
            .offerTo(exporter)
    }

    //    Distillation Tower    //

    private fun distillation(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilders.createDistillation(
            exporter,
            HTFluidIngredient.ofFluid(RagiumContents.Fluids.CRUDE_OIL, FluidConstants.BUCKET * 3),
            listOf(
                HTFluidResult.ofFluid(RagiumContents.Fluids.REFINED_GAS),
                HTFluidResult.ofFluid(RagiumContents.Fluids.NAPHTHA, FluidConstants.BUCKET * 2),
                HTFluidResult.ofFluid(RagiumContents.Fluids.RESIDUAL_OIL),
            ),
            RagiumAPI.id("crude_oil"),
        )
        HTMachineRecipeJsonBuilders.createDistillation(
            exporter,
            HTFluidIngredient.ofFluid(RagiumContents.Fluids.REFINED_GAS, FluidConstants.BUCKET * 3),
            listOf(
                HTFluidResult.ofFluid(RagiumContents.Fluids.METHANE),
                HTFluidResult.ofFluid(RagiumContents.Fluids.LPG),
                HTFluidResult.ofFluid(RagiumContents.Fluids.ETHYLENE),
            ),
            RagiumAPI.id("refined_gas"),
        )
        HTMachineRecipeJsonBuilders.createDistillation(
            exporter,
            HTFluidIngredient.ofFluid(RagiumContents.Fluids.NAPHTHA, FluidConstants.BUCKET * 3),
            listOf(
                HTFluidResult.ofFluid(RagiumContents.Fluids.AROMATIC_COMPOUNDS),
                HTFluidResult.ofFluid(RagiumContents.Fluids.FUEL, FluidConstants.BUCKET * 2),
            ),
            RagiumAPI.id("naphtha"),
        )
        HTMachineRecipeJsonBuilders.createDistillation(
            exporter,
            HTFluidIngredient.ofFluid(RagiumContents.Fluids.RESIDUAL_OIL, FluidConstants.BUCKET * 3),
            listOf(
                HTFluidResult.ofFluid(RagiumContents.Fluids.FUEL),
                HTFluidResult.ofFluid(RagiumContents.Fluids.LUBRICANT),
                HTFluidResult.ofFluid(RagiumContents.Fluids.ASPHALT),
            ),
            RagiumAPI.id("residual_oil"),
        )
    }

    //    Electrolyzer    //

    private fun electrolyzer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ELECTROLYZER)
            .addInput(RagiumContents.Fluids.WATER)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE, 2)
            .addOutput(RagiumContents.Fluids.HYDROGEN, 2)
            .addOutput(RagiumContents.Fluids.OXYGEN)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ELECTROLYZER)
            .addInput(RagiumContents.Fluids.SALT_WATER)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE, 2)
            .addOutput(RagiumContents.Fluids.HYDROGEN)
            .addOutput(RagiumContents.Fluids.CHLORINE)
            .addOutput(RagiumContents.Fluids.SODIUM_HYDROXIDE)
            .offerSuffix(exporter, suffix = "_salty")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ELECTROLYZER)
            .addInput(Items.GLOWSTONE_DUST)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addOutput(RagiumContents.Gems.FLUORITE)
            .addOutput(Items.GOLD_NUGGET)
            .offerTo(exporter)
    }

    //    Extractor    //

    private fun extractor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilders.createExtractor(
            exporter,
            HTItemIngredient.ofItem(ItemTags.VILLAGER_PLANTABLE_SEEDS, 4),
            listOf(HTItemResult.ofItem(RagiumContents.Misc.EMPTY_FLUID_CUBE)),
            listOf(),
            HTMachineRecipeJsonBuilders.createRecipeId(RagiumContents.Misc.EMPTY_FLUID_CUBE),
        )
        HTMachineRecipeJsonBuilders.createExtractor(
            exporter,
            HTItemIngredient.ofItem(RagiumItemTags.PROTEIN_FOODS, 4),
            listOf(),
            listOf(HTFluidResult.ofFluid(RagiumContents.Fluids.TALLOW)),
            HTMachineRecipeJsonBuilders.createRecipeId(RagiumContents.Fluids.TALLOW),
        )
        HTMachineRecipeJsonBuilders.createExtractor(
            exporter,
            HTItemIngredient.ofItem(RagiumItemTags.BASALTS),
            listOf(HTItemResult.ofItem(RagiumContents.Misc.BASALT_MESH)),
            listOf(),
            HTMachineRecipeJsonBuilders.createRecipeId(RagiumContents.Misc.BASALT_MESH),
        )
        HTMachineRecipeJsonBuilders.createExtractor(
            exporter,
            HTItemIngredient.ofItem(RagiumContents.Foods.CHOCOLATE),
            listOf(),
            listOf(HTFluidResult.ofFluid(RagiumContents.Fluids.CHOCOLATE)),
            RagiumAPI.id("chocolate"),
        )
        HTMachineRecipeJsonBuilders.createExtractor(
            exporter,
            HTItemIngredient.ofItem(Items.SUGAR, 4),
            listOf(),
            listOf(HTFluidResult.ofFluid(RagiumContents.Fluids.STARCH_SYRUP)),
            RagiumAPI.id("starch_syrup"),
        )
        HTMachineRecipeJsonBuilders.createExtractor(
            exporter,
            HTItemIngredient.ofItem(Items.SWEET_BERRIES),
            listOf(),
            listOf(HTFluidResult.ofFluid(RagiumContents.Fluids.SWEET_BERRIES)),
            RagiumAPI.id("sweet_berries"),
        )
        HTMachineRecipeJsonBuilders.createExtractor(
            exporter,
            HTItemIngredient.ofItem(RagiumContents.Fluids.MILK),
            listOf(HTItemResult.ofItem(RagiumContents.Foods.BUTTER)),
            listOf(),
            HTMachineRecipeJsonBuilders.createRecipeId(RagiumContents.Foods.BUTTER),
        )
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.EXTRACTOR, HTMachineTier.BASIC)
            .addInput(Items.SOUL_SAND)
            .addInput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .addOutput(Items.SAND)
            .addOutput(RagiumContents.Fluids.CRUDE_OIL)
            .offerTo(exporter)
        HTMachineRecipeJsonBuilders.createExtractor(
            exporter,
            HTItemIngredient.ofItem(Items.SOUL_SAND),
            listOf(HTItemResult.ofItem(Items.SAND)),
            listOf(HTFluidResult.ofFluid(RagiumContents.Fluids.CRUDE_OIL)),
            RagiumAPI.id("crude_oil_from_soul_sand"),
        )
        HTMachineRecipeJsonBuilders.createExtractor(
            exporter,
            HTItemIngredient.ofItem(Items.SOUL_SOIL),
            listOf(HTItemResult.ofItem(Items.SAND)),
            listOf(HTFluidResult.ofFluid(RagiumContents.Fluids.CRUDE_OIL)),
            RagiumAPI.id("crude_oil_from_soul_soil"),
        )
    }

    //    Fluid Drill    //

    private fun fluidDrill(exporter: RecipeExporter) {
        registerDrilling(exporter, BiomeKeys.WARM_OCEAN, RagiumContents.Fluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.LUKEWARM_OCEAN, RagiumContents.Fluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.DEEP_LUKEWARM_OCEAN, RagiumContents.Fluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.OCEAN, RagiumContents.Fluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.DEEP_OCEAN, RagiumContents.Fluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.COLD_OCEAN, RagiumContents.Fluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.DEEP_COLD_OCEAN, RagiumContents.Fluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.FROZEN_OCEAN, RagiumContents.Fluids.SALT_WATER)
        registerDrilling(exporter, BiomeKeys.DEEP_FROZEN_OCEAN, RagiumContents.Fluids.SALT_WATER)

        registerDrilling(exporter, BiomeKeys.NETHER_WASTES, RagiumContents.Fluids.LAVA)
        registerDrilling(exporter, BiomeKeys.WARPED_FOREST, RagiumContents.Fluids.LAVA)
        registerDrilling(exporter, BiomeKeys.CRIMSON_FOREST, RagiumContents.Fluids.LAVA)
        registerDrilling(exporter, BiomeKeys.SOUL_SAND_VALLEY, RagiumContents.Fluids.CRUDE_OIL)
        registerDrilling(exporter, BiomeKeys.BASALT_DELTAS, RagiumContents.Fluids.LAVA)
    }

    private fun registerDrilling(exporter: RecipeExporter, biomeKey: RegistryKey<Biome>, fluid: RagiumContents.Fluids) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.FLUID_DRILL)
            .addOutput(fluid)
            .setCustomData(HTRecipeComponentTypes.BIOME, biomeKey)
            .offerTo(exporter, biomeKey.value)
        /*exporter.accept(
            biomeKey.value.withPrefixedPath("fluid_drill/"),
            HTFluidDrillRecipe(biomeKey, HTRecipeResult.item(fluid)),
            null,
        )*/
    }

    //    Grinder    //

    private fun grinder(exporter: RecipeExporter) {
        registerGrinder(exporter, ConventionalItemTags.COBBLESTONES to 1, Items.GRAVEL to 1)
        registerGrinder(exporter, ConventionalItemTags.QUARTZ_ORES to 1, Items.QUARTZ to 2)
        registerGrinder(exporter, ConventionalItemTags.RED_SANDSTONE_BLOCKS to 1, Items.RED_SAND to 4)
        registerGrinder(exporter, ConventionalItemTags.UNCOLORED_SANDSTONE_BLOCKS to 1, Items.SAND to 1)
        registerGrinder(exporter, ConventionalItemTags.WHEAT_CROPS to 1, RagiumContents.Foods.FLOUR to 1)
        registerGrinder(exporter, Items.ANCIENT_DEBRIS to 1, Items.NETHERITE_SCRAP to 2)
        registerGrinder(exporter, Items.COARSE_DIRT to 1, Items.DIRT to 1)
        registerGrinder(exporter, Items.DEEPSLATE to 1, Items.COBBLED_DEEPSLATE to 1)
        registerGrinder(exporter, Items.GRAVEL to 1, Items.SAND to 1, suffix = "_from_gravel")
        // registerGrinder(exporter, Items.NETHERRACK to 4, RagiumContents.Dusts.SULFUR to 1)
        registerGrinder(exporter, Items.STONE to 1, Items.COBBLESTONE to 1)
        registerGrinder(exporter, ItemTags.BOATS to 1, RagiumContents.Foods.PULP to 5, suffix = "_from_boat")
        registerGrinder(exporter, ItemTags.COAL_ORES to 1, Items.COAL to 2)
        registerGrinder(exporter, ItemTags.DIAMOND_ORES to 1, Items.DIAMOND to 2)
        registerGrinder(exporter, ItemTags.EMERALD_ORES to 1, Items.EMERALD to 2)
        registerGrinder(exporter, ItemTags.FENCE_GATES to 1, RagiumContents.Foods.PULP to 4, suffix = "_from_fence_gate")
        registerGrinder(exporter, ItemTags.LAPIS_ORES to 1, Items.LAPIS_LAZULI to 8)
        registerGrinder(exporter, ItemTags.LOGS to 1, RagiumContents.Foods.PULP to 4, suffix = "_from_log")
        registerGrinder(exporter, ItemTags.PLANKS to 1, RagiumContents.Foods.PULP to 1, suffix = "_from_plank")
        registerGrinder(exporter, ItemTags.REDSTONE_ORES to 1, Items.REDSTONE to 8)
        registerGrinder(exporter, ItemTags.SAPLINGS to 2, RagiumContents.Foods.PULP to 1, suffix = "_from_sapling")
        registerGrinder(exporter, ItemTags.WOODEN_BUTTONS to 1, RagiumContents.Foods.PULP to 1, suffix = "_from_button")
        registerGrinder(exporter, ItemTags.WOODEN_DOORS to 1, RagiumContents.Foods.PULP to 2, suffix = "_from_door")
        registerGrinder(exporter, ItemTags.WOODEN_FENCES to 3, RagiumContents.Foods.PULP to 5, suffix = "_from_fence")
        registerGrinder(exporter, ItemTags.WOODEN_PRESSURE_PLATES to 1, RagiumContents.Foods.PULP to 2, suffix = "_from_plate")
        registerGrinder(exporter, ItemTags.WOODEN_SLABS to 2, RagiumContents.Foods.PULP to 1, suffix = "_from_slab")
        registerGrinder(exporter, ItemTags.WOODEN_STAIRS to 4, RagiumContents.Foods.PULP to 6, suffix = "_from_stair")
        registerGrinder(exporter, ItemTags.WOODEN_TRAPDOORS to 1, RagiumContents.Foods.PULP to 3, suffix = "_from_trap_door")
        registerGrinder(exporter, ItemTags.WOOL to 1, Items.STRING to 4)
        registerGrinder(exporter, RagiumItemTags.PROTEIN_FOODS to 1, RagiumContents.Foods.MINCED_MEAT to 1)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .addInput(ConventionalItemTags.SUGAR_CANE_CROPS)
            .addOutput(Items.SUGAR, 2)
            .addOutput(RagiumContents.Foods.PULP)
            .offerTo(exporter)
    }

    @JvmName("registerGrinderItem")
    private fun registerGrinder(
        exporter: RecipeExporter,
        input: Pair<ItemConvertible, Int>,
        output: Pair<ItemConvertible, Int>,
        suffix: String = "",
    ) {
        HTMachineRecipeJsonBuilders.createGrinder(
            exporter,
            HTItemIngredient.ofItem(input.first, input.second.toLong()),
            HTItemResult.ofItem(output.first, output.second.toLong()),
            recipeId = HTMachineRecipeJsonBuilders.createRecipeId(output.first).withSuffixedPath(suffix),
        )
        /*HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .addInput(input.first, input.second)
            .addOutput(output.first, output.second)
            .offerSuffix(exporter, suffix)*/
    }

    @JvmName("registerGrinderTag")
    private fun registerGrinder(
        exporter: RecipeExporter,
        input: Pair<TagKey<Item>, Int>,
        output: Pair<ItemConvertible, Int>,
        suffix: String = "",
    ) {
        HTMachineRecipeJsonBuilders.createGrinder(
            exporter,
            HTItemIngredient.ofItem(input.first, input.second.toLong()),
            HTItemResult.ofItem(output.first, output.second.toLong()),
            recipeId = HTMachineRecipeJsonBuilders.createRecipeId(output.first).withSuffixedPath(suffix),
        )
    }

    //    Metal Former    //

    private fun metalFormer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilders.createMetalFormer(
            exporter,
            HTItemIngredient.ofItem(RagiumItemTags.STEEL_INGOTS, 2),
            HTItemResult.ofItem(RagiumBlocks.SHAFT),
        )
    }

    //    Mixer    //

    private fun mixer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumContents.Dusts.CRUDE_RAGINITE, 4)
            .addInput(RagiumContents.Fluids.WATER)
            .addOutput(RagiumContents.Dusts.RAGINITE, 4)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumItemTags.ORGANIC_OILS)
            .addInput(RagiumItemTags.ALKALI)
            .addOutput(RagiumContents.Misc.SOAP_INGOT)
            .addOutput(RagiumContents.Fluids.GLYCEROL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumContents.Foods.FLOUR, 3)
            .addInput(RagiumContents.Fluids.WATER)
            .addOutput(RagiumContents.Foods.DOUGH, 3)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumContents.Foods.PULP)
            .addInput(RagiumContents.Fluids.WATER)
            .addOutput(Items.PAPER)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumContents.Foods.PULP)
            .addInput(RagiumContents.Fluids.SODIUM_HYDROXIDE)
            .addOutput(Items.PAPER, 2)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerSuffix(exporter, "_alkali")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumContents.Foods.PULP)
            .addInput(RagiumContents.Fluids.WATER)
            .addInput(RagiumContents.Fluids.SULFURIC_ACID)
            .addOutput(Items.PAPER, 4)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE, 2)
            .offerSuffix(exporter, "_sulfuric_acid")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumContents.Foods.BUTTER)
            .addInput(Items.SUGAR)
            .addInput(RagiumContents.Fluids.MILK)
            .addOutput(RagiumContents.Foods.CARAMEL, 4)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .setCatalyst(RagiumContents.Misc.HEART_OF_THE_NETHER)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(Items.COCOA_BEANS)
            .addInput(Items.SUGAR)
            .addInput(RagiumContents.Fluids.MILK)
            .addOutput(RagiumContents.Foods.CHOCOLATE)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .setCatalyst(RagiumContents.Misc.HEART_OF_THE_NETHER)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(RagiumContents.Fluids.MILK)
            .addInput(RagiumContents.Foods.FLOUR)
            .addInput(Items.SUGAR)
            .addOutput(RagiumContents.Fluids.BATTER)
            .offerTo(exporter)

        registerBreaching(exporter, ConventionalItemTags.CONCRETE_POWDERS, Items.WHITE_CONCRETE_POWDER)
        registerBreaching(exporter, ConventionalItemTags.CONCRETES, Items.WHITE_CONCRETE)
        registerBreaching(exporter, ConventionalItemTags.GLASS_BLOCKS, Items.WHITE_STAINED_GLASS)
        registerBreaching(exporter, ConventionalItemTags.GLASS_PANES, Items.WHITE_STAINED_GLASS_PANE)
        registerBreaching(exporter, ConventionalItemTags.GLAZED_TERRACOTTAS, Items.WHITE_GLAZED_TERRACOTTA)
        registerBreaching(exporter, ItemTags.BANNERS, Items.WHITE_BANNER)
        registerBreaching(exporter, ItemTags.BEDS, Items.WHITE_BED)
        registerBreaching(exporter, ItemTags.CANDLES, Items.WHITE_CANDLE)
        registerBreaching(exporter, ItemTags.TERRACOTTA, Items.WHITE_TERRACOTTA)
        registerBreaching(exporter, ItemTags.WOOL, Items.WHITE_WOOL)
        registerBreaching(exporter, ItemTags.WOOL_CARPETS, Items.WHITE_CARPET)
    }

    private fun registerBreaching(exporter: RecipeExporter, input: TagKey<Item>, output: Item) {
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.MIXER)
            .addInput(input)
            .addInput(RagiumContents.Misc.SOAP_INGOT)
            .addInput(RagiumContents.Fluids.WATER)
            .addOutput(output)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.CHEMICAL_REACTOR)
            .addInput(input)
            .addInput(RagiumContents.Fluids.CHLORINE)
            .addOutput(output)
            .addOutput(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .offerTo(exporter)
    }

    //    Rock Generator    //

    private fun rockGenerator(exporter: RecipeExporter) {
        registerRock(exporter, Items.STONE)
        registerRock(exporter, Items.COBBLESTONE)
        registerRock(exporter, Items.GRANITE)
        registerRock(exporter, Items.DIORITE)
        registerRock(exporter, Items.ANDESITE)
        registerRock(exporter, Items.DEEPSLATE)
        registerRock(exporter, Items.COBBLED_DEEPSLATE)
        registerRock(exporter, Items.CALCITE)
        registerRock(exporter, Items.TUFF)
        registerRock(exporter, Items.DRIPSTONE_BLOCK)
        registerRock(exporter, Items.NETHERRACK)
        registerRock(exporter, Items.BASALT)
        registerRock(exporter, Items.BLACKSTONE)
        registerRock(exporter, Items.END_STONE)

        /*HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.ROCK_GENERATOR)
            .addInput(RagiumContents.Fluids.LAVA)
            .addOutput(Items.OBSIDIAN)
            .offerTo(exporter)*/
    }

    private fun registerRock(exporter: RecipeExporter, rock: ItemConvertible) {
        HTMachineRecipeJsonBuilders.createRockGen(
            exporter,
            HTItemResult.ofItem(rock, 8),
        )
    }

    //    Saw Mill    //

    private fun sawMill(exporter: RecipeExporter) {
        registerPlank(exporter, ItemTags.OAK_LOGS, Items.OAK_PLANKS)
        registerPlank(exporter, ItemTags.SPRUCE_LOGS, Items.SPRUCE_PLANKS)
        registerPlank(exporter, ItemTags.BIRCH_LOGS, Items.BIRCH_PLANKS)
        registerPlank(exporter, ItemTags.JUNGLE_LOGS, Items.JUNGLE_PLANKS)
        registerPlank(exporter, ItemTags.ACACIA_LOGS, Items.ACACIA_PLANKS)
        registerPlank(exporter, ItemTags.CHERRY_LOGS, Items.CHERRY_PLANKS)
        registerPlank(exporter, ItemTags.DARK_OAK_LOGS, Items.DARK_OAK_PLANKS)
        registerPlank(exporter, ItemTags.MANGROVE_LOGS, Items.MANGROVE_PLANKS)
        registerPlank(exporter, ItemTags.CRIMSON_STEMS, Items.CRIMSON_PLANKS)
        registerPlank(exporter, ItemTags.WARPED_STEMS, Items.WARPED_PLANKS)

        HTMachineRecipeJsonBuilders.createSawMill(
            exporter,
            HTItemIngredient.ofItem(ItemTags.PLANKS),
            HTItemResult.ofItem(Items.STICK, 4),
        )
    }

    private fun registerPlank(exporter: RecipeExporter, log: TagKey<Item>, plank: ItemConvertible) {
        HTMachineRecipeJsonBuilders.createSawMill(
            exporter,
            HTItemIngredient.ofItem(log),
            HTItemResult.ofItem(plank, 6),
            HTItemResult.ofItem(RagiumContents.Foods.PULP),
        )
    }

    //    Mob Extractor    //

    private fun mobExtractor(exporter: RecipeExporter) {
        registerMobDrop(exporter, Items.ARMADILLO_SCUTE, EntityType.ARMADILLO)
        registerMobDrop(exporter, Items.HONEYCOMB, EntityType.BEE)
        registerMobDrop(exporter, RagiumContents.Fluids.SULFURIC_ACID, EntityType.BLAZE)
        registerMobDrop(exporter, RagiumContents.Fluids.NITRIC_ACID, EntityType.BREEZE)
        registerMobDrop(exporter, Items.EGG, EntityType.CHICKEN)
        registerMobDrop(exporter, RagiumContents.Fluids.MILK, EntityType.COW)
        registerMobDrop(exporter, Items.GLOW_INK_SAC, EntityType.GLOW_SQUID)
        registerMobDrop(exporter, Items.IRON_NUGGET, EntityType.IRON_GOLEM)
        registerMobDrop(exporter, RagiumContents.Fluids.MILK, EntityType.MOOSHROOM) // TODO
        registerMobDrop(exporter, Items.WHITE_WOOL, EntityType.SHEEP)
        registerMobDrop(exporter, Items.SNOWBALL, EntityType.SNOW_GOLEM)
        registerMobDrop(exporter, Items.INK_SAC, EntityType.SQUID)
        registerMobDrop(exporter, Items.TURTLE_SCUTE, EntityType.TURTLE)
        registerMobDrop(exporter, Items.EMERALD, EntityType.WANDERING_TRADER)
        registerMobDrop(exporter, Items.ECHO_SHARD, EntityType.WARDEN) // TODO
        registerMobDrop(exporter, Items.WITHER_ROSE, EntityType.WITHER)
        registerMobDrop(exporter, Items.PLAYER_HEAD, EntityType.PLAYER)

        registerMobDrop(exporter, RagiumContents.Gems.OBLIVION_CRYSTAL, RagiumEntityTypes.OBLIVION_CUBE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.MOB_EXTRACTOR)
            .addOutput(Items.RED_MUSHROOM)
            .addOutput(Items.BROWN_MUSHROOM)
            .setCustomData(HTRecipeComponentTypes.ENTITY_TYPE, EntityType.BOGGED)
            .offerTo(exporter)
    }

    private fun registerMobDrop(exporter: RecipeExporter, output: ItemConvertible, entityType: EntityType<*>) {
        /*HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.MOB_EXTRACTOR)
            .addOutput(output)
            .setCustomData(HTRecipeComponentTypes.ENTITY_TYPE, entityType)
            .offerTo(exporter, Registries.ENTITY_TYPE.getId(entityType))*/
    }

    private fun test(exporter: RecipeExporter) {}
}
