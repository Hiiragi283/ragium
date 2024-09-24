package hiiragi283.ragium.data

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import hiiragi283.ragium.common.data.HTInfusionRecipeJsonBuilder
import hiiragi283.ragium.common.data.HTMachineRecipeJsonBuilder
import hiiragi283.ragium.common.init.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumMaterials
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.data.util.HTMetalItemRecipeGroup
import hiiragi283.ragium.data.util.RagiumMetalItemRecipeGroups
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.block.Block
import net.minecraft.data.server.recipe.*
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture

class RagiumRecipeProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricRecipeProvider(output, registriesFuture) {
    // override fun getRecipeIdentifier(identifier: Identifier): Identifier = identifier

    override fun generate(exporter: RecipeExporter) {
        craftingRecipes(exporter)
        cookingRecipes(exporter)
        // tier1
        grinderRecipes(exporter)
        alloyFurnaceRecipes(exporter)
        blastFurnace(exporter)
        // tier2
        compressor(exporter)
        extractor(exporter)
        metalFormer(exporter)
        mixer(exporter)
        blazingBlastFurnace(exporter)
        // tier3
        centrifuge(exporter)
        chemicalReactor(exporter)
        electrolyzer(exporter)
        distillation(exporter)
        // tier4
        infusion(exporter)
        transform(exporter)
        // patterned
        RagiumMetalItemRecipeGroups
        HTMetalItemRecipeGroup.registry.forEach { (_: String, family: HTMetalItemRecipeGroup) ->
            family.generateRecipes(exporter, ::exporterWrapper)
        }
    }

    private fun exporterWrapper(exporter: RecipeExporter, bool: Boolean): RecipeExporter =
        exporter.conditions(HTHardModeResourceCondition.fromBool((bool)))

    //    Crafting    //

    private fun <T : CraftingRecipeJsonBuilder> T.itemCriterion(item: ItemConvertible): T = apply {
        criterion("has_input", RecipeProvider.conditionsFromItem(item))
    }

    private fun <T : CraftingRecipeJsonBuilder> T.tagCriterion(tagKey: TagKey<Item>): T = apply {
        criterion("has_input", RecipeProvider.conditionsFromTag(tagKey))
    }

    private fun RecipeExporter.conditions(vararg conditions: ResourceCondition): RecipeExporter = withConditions(this, *conditions)

    private fun RecipeExporter.hardMode(isHard: Boolean): RecipeExporter = conditions(HTHardModeResourceCondition.fromBool(isHard))

    private fun craftingRecipes(exporter: RecipeExporter) {
        // ingredients
        createShaped(RagiumContents.RAGI_ALLOY_COMPOUND)
            .group("ragi_alloy_compound")
            .pattern("AAA")
            .pattern("ABA")
            .pattern("AAA")
            .input('A', RagiumContents.RAW_RAGINITE)
            .input('B', ConventionalItemTags.COPPER_INGOTS)
            .itemCriterion(RagiumContents.RAW_RAGINITE)
            .offerTo(exporter, Ragium.id("shaped/ragi_alloy_compound"))

        createShaped(RagiumContents.RAGI_ALLOY_COMPOUND)
            .group("ragi_alloy_compound")
            .pattern(" A ")
            .pattern("ABA")
            .pattern(" A ")
            .input('A', RagiumContents.Dusts.RAW_RAGINITE)
            .input('B', ConventionalItemTags.COPPER_INGOTS)
            .itemCriterion(RagiumContents.Dusts.RAW_RAGINITE)
            .offerTo(
                exporter.hardMode(false),
                Ragium.id("shaped/ragi_alloy_compound_1"),
            )

        createEmptyFluidCube(exporter, Items.GLASS_PANE, 4)
        createEmptyFluidCube(exporter, RagiumContents.Plates.PE, 8, "_pe")
        createEmptyFluidCube(exporter, RagiumContents.Plates.PVC, 16, "_pvc")
        createEmptyFluidCube(exporter, RagiumContents.Plates.PTFE, 32, "_ptfe")

        createShaped(Items.NETHER_STAR)
            .pattern(" A ")
            .pattern("DEB")
            .pattern(" C ")
            .input('A', RagiElement.RAGIUM.dustItem)
            .input('B', RagiElement.RIGIUM.dustItem)
            .input('C', RagiElement.RUGIUM.dustItem)
            .input('D', RagiElement.REGIUM.dustItem)
            .input('E', RagiElement.ROGIUM.dustItem)
            .itemCriterion(Items.NETHER_STAR)
            .offerTo(
                exporter,
                Ragium.id("shaped/nether_star"),
            )

        // tools
        createShaped(RagiumContents.FORGE_HAMMER)
            .pattern(" AA")
            .pattern("BBA")
            .pattern(" AA")
            .input('A', RagiumContents.Ingots.RAGI_ALLOY)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .itemCriterion(RagiumContents.Ingots.RAGI_ALLOY)
            .offerTo(exporter, Ragium.id("shaped/forge_hammer"))

        createShaped(RagiumContents.STEEL_SWORD)
            .pattern("B")
            .pattern("A")
            .pattern("A")
            .input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .tagCriterion(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter, Ragium.id("shaped/steel_sword"))

        createShaped(RagiumContents.STEEL_SHOVEL)
            .pattern("B")
            .pattern("B")
            .pattern("A")
            .input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .tagCriterion(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter, Ragium.id("shaped/steel_shovel"))

        createShaped(RagiumContents.STEEL_PICKAXE)
            .pattern(" B ")
            .pattern(" B ")
            .pattern("AAA")
            .input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .tagCriterion(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter, Ragium.id("shaped/steel_pickaxe"))

        createShaped(RagiumContents.STEEL_AXE)
            .pattern("B ")
            .pattern("BA")
            .pattern("AA")
            .input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .tagCriterion(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter, Ragium.id("shaped/steel_axe"))

        createShaped(RagiumContents.STEEL_HOE)
            .pattern("B ")
            .pattern("B ")
            .pattern("AA")
            .input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .tagCriterion(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter, Ragium.id("shaped/steel_hoe"))

        // hulls
        val materials: List<RagiumMaterials> = listOf(
            RagiumMaterials.RAGI_ALLOY,
            RagiumMaterials.RAGI_STEEL,
            RagiumMaterials.REFINED_RAGI_STEEL,
        )

        materials.forEach { material: RagiumMaterials ->
            val base: Block = material.tier.base
            val ingot: RagiumContents.Ingots = material.getIngot() ?: return@forEach
            val hull: RagiumContents.Hulls = material.getHull() ?: return@forEach
            createShaped(hull)
                .pattern("AAA")
                .pattern("A A")
                .pattern("BBB")
                .input('A', ingot)
                .input('B', base)
                .itemCriterion(ingot)
                .offerTo(
                    exporter.hardMode(false),
                    CraftingRecipeJsonBuilder.getItemId(hull).withPrefixedPath("shaped/"),
                )
            val plate: RagiumContents.Plates = material.getPlate() ?: return@forEach
            createShaped(hull)
                .pattern("AAA")
                .pattern("A A")
                .pattern("BBB")
                .input('A', plate)
                .input('B', base)
                .itemCriterion(plate)
                .offerTo(
                    exporter.hardMode(true),
                    CraftingRecipeJsonBuilder.getItemId(hull).withPrefixedPath("shaped/hard/"),
                )
        }
        // machines
        createShaped(RagiumContents.MANUAL_GRINDER)
            .pattern("A  ")
            .pattern("BBB")
            .pattern("CCC")
            .input('A', ConventionalItemTags.WOODEN_RODS)
            .input('B', RagiumContents.Ingots.RAGI_ALLOY)
            .input('C', Items.SMOOTH_STONE)
            .itemCriterion(RagiumContents.Ingots.RAGI_ALLOY)
            .offerTo(exporter, Ragium.id("shaped/manual_grinder"))

        createShaped(RagiumContents.MANUAL_GRINDER)
            .pattern("A  ")
            .pattern("BBB")
            .pattern("CCC")
            .input('A', ConventionalItemTags.WOODEN_RODS)
            .input('B', RagiumContents.Plates.RAGI_ALLOY)
            .input('C', Items.SMOOTH_STONE)
            .itemCriterion(RagiumContents.Plates.RAGI_ALLOY)
            .offerTo(exporter, Ragium.id("shaped/hard/manual_grinder"))

        createShaped(RagiumContents.BURNING_BOX)
            .pattern("AAA")
            .pattern("A A")
            .pattern("ABA")
            .input('A', Items.BRICKS)
            .input('B', Items.FURNACE)
            .itemCriterion(Items.BRICKS)
            .offerTo(exporter, Ragium.id("shaped/burning_box"))

        createShaped(RagiumContents.GEAR_BOX)
            .pattern("AAA")
            .pattern("BCB")
            .pattern("BDB")
            .input('A', RagiumContents.Ingots.RAGI_STEEL)
            .input('B', HTMachineTier.ELECTRIC.base)
            .input('C', RagiumContents.SHAFT)
            .input('D', ConventionalItemTags.REDSTONE_DUSTS)
            .itemCriterion(RagiumContents.SHAFT)
            .offerTo(exporter, Ragium.id("shaped/gear_box"))

        // tiered machines
        // tier1
        createMachine(
            exporter,
            HTMachineType.Single.ALLOY_FURNACE,
            RagiumContents.Ingots.RAGI_ALLOY,
            Items.FURNACE,
            RagiumContents.Hulls.RAGI_ALLOY,
        )

        createMachine(
            exporter,
            HTMachineType.Multi.BRICK_BLAST_FURNACE,
            RagiumContents.Ingots.RAGI_ALLOY,
            Items.BLAST_FURNACE,
            RagiumContents.Hulls.RAGI_ALLOY,
        )

        // tier2
        createMachine(
            exporter,
            HTMachineType.Single.COMPRESSOR,
            RagiumContents.Ingots.RAGI_STEEL,
            Items.PISTON,
            RagiumContents.Hulls.RAGI_STEEL,
        )

        createMachine(
            exporter,
            HTMachineType.Single.EXTRACTOR,
            RagiumContents.Ingots.RAGI_STEEL,
            Items.BUCKET, // TODO
            RagiumContents.Hulls.RAGI_STEEL,
        )

        createMachine(
            exporter,
            HTMachineType.Single.GRINDER,
            RagiumContents.Ingots.RAGI_STEEL,
            Items.FLINT,
            RagiumContents.Hulls.RAGI_STEEL,
        )

        createMachine(
            exporter,
            HTMachineType.Single.METAL_FORMER,
            RagiumContents.Ingots.RAGI_STEEL,
            RagiumContents.FORGE_HAMMER,
            RagiumContents.Hulls.RAGI_STEEL,
        )

        createMachine(
            exporter,
            HTMachineType.Single.MIXER,
            RagiumContents.Ingots.RAGI_STEEL,
            Items.CAULDRON, // TODO
            RagiumContents.Hulls.RAGI_STEEL,
        )

        createMachine(
            exporter,
            HTMachineType.Multi.BLAZING_BLAST_FURNACE,
            RagiumContents.Ingots.RAGI_STEEL,
            HTMachineType.Multi.BRICK_BLAST_FURNACE,
            RagiumContents.Hulls.RAGI_STEEL,
        )

        // tier3
        createMachine(
            exporter,
            HTMachineType.Single.CENTRIFUGE,
            RagiumContents.Ingots.REFINED_RAGI_STEEL,
            Items.COPPER_GRATE, // TODO
            RagiumContents.Hulls.REFINED_RAGI_STEEL,
        )

        createMachine(
            exporter,
            HTMachineType.Single.CHEMICAL_REACTOR,
            RagiumContents.Ingots.REFINED_RAGI_STEEL,
            Items.CRAFTING_TABLE, // TODO
            RagiumContents.Hulls.REFINED_RAGI_STEEL,
        )

        createMachine(
            exporter,
            HTMachineType.Single.ELECTROLYZER,
            RagiumContents.Ingots.REFINED_RAGI_STEEL,
            Items.HOPPER, // TODO
            RagiumContents.Hulls.REFINED_RAGI_STEEL,
        )

        createMachine(
            exporter,
            HTMachineType.Multi.DISTILLATION_TOWER,
            RagiumContents.Ingots.REFINED_RAGI_STEEL,
            Items.COPPER_GRATE,
            RagiumContents.Hulls.REFINED_RAGI_STEEL,
        )
        // tier4
        createShaped(RagiumContents.ALCHEMICAL_INFUSER)
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', Items.DRAGON_BREATH)
            .input('C', Items.NETHER_STAR)
            .input('D', Items.CRYING_OBSIDIAN)
            .itemCriterion(Items.NETHER_STAR)
            .offerTo(exporter, Ragium.id("shaped/alchemical_infuser"))
    }

    private fun createShaped(output: ItemConvertible, count: Int = 1): ShapedRecipeJsonBuilder =
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, output, count)

    private fun createShapeless(output: ItemConvertible): ShapelessRecipeJsonBuilder =
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, output)

    private fun createEmptyFluidCube(
        exporter: RecipeExporter,
        input: ItemConvertible,
        count: Int,
        suffix: String? = null,
    ) {
        createShaped(RagiumContents.EMPTY_FLUID_CUBE, count)
            .pattern(" A ")
            .pattern("A A")
            .pattern(" A ")
            .input('A', input)
            .itemCriterion(input)
            .offerTo(exporter, Ragium.id("shaped/empty_fluid_cube$suffix"))
    }

    private fun createMachine(
        exporter: RecipeExporter,
        type: HTMachineType,
        ingot: ItemConvertible,
        side: ItemConvertible,
        hull: ItemConvertible,
    ) {
        createShaped(type)
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .input('A', ingot)
            .input('B', side)
            .input('C', hull)
            .input('D', type.tier.base)
            .itemCriterion(hull)
            .offerTo(exporter, type.id.withPrefixedPath("shaped/"))
    }

    //    Cooking   //

    private fun cookingRecipes(exporter: RecipeExporter) {
        CookingRecipeJsonBuilder
            .createSmelting(
                Ingredient.ofItems(RagiumContents.RAGI_ALLOY_COMPOUND),
                RecipeCategory.MISC,
                RagiumContents.Ingots.RAGI_ALLOY,
                0.0f,
                200,
            ).itemCriterion(RagiumContents.RAGI_ALLOY_COMPOUND)
            .offerTo(exporter, Ragium.id("smelting/ragi_alloy_ingot_alt"))
    }

    //    Grinder    //

    private fun grinderRecipes(exporter: RecipeExporter) {
        grinderRecipe(exporter, ConventionalItemTags.QUARTZ_ORES, Items.QUARTZ, 2)
        grinderRecipe(exporter, ConventionalItemTags.RED_SANDSTONE_BLOCKS, Items.RED_SAND, 4)
        grinderRecipe(exporter, ConventionalItemTags.UNCOLORED_SANDSTONE_BLOCKS, Items.SAND, 4)
        grinderRecipe(exporter, Items.COARSE_DIRT, Items.DIRT)
        grinderRecipe(exporter, Items.COBBLESTONE, Items.GRAVEL)
        grinderRecipe(exporter, Items.DEEPSLATE, Items.DEEPSLATE)
        grinderRecipe(exporter, Items.GRAVEL, Items.SAND, id = Identifier.ofVanilla("gravel_to_sand"))
        grinderRecipe(exporter, Items.STONE, Items.COBBLESTONE)
        grinderRecipe(exporter, ItemTags.COAL_ORES, Items.COAL, 2)
        grinderRecipe(exporter, ItemTags.DIAMOND_ORES, Items.DIAMOND, 2)
        grinderRecipe(exporter, ItemTags.EMERALD_ORES, Items.EMERALD, 2)
        grinderRecipe(exporter, ItemTags.LAPIS_ORES, Items.LAPIS_LAZULI, 8)
        grinderRecipe(exporter, ItemTags.REDSTONE_ORES, Items.REDSTONE, 8)
        grinderRecipe(exporter, ItemTags.WOOL, Items.STRING, 4)
    }

    private fun grinderRecipe(
        exporter: RecipeExporter,
        input: ItemConvertible,
        output: ItemConvertible,
        count: Int = 1,
        id: Identifier = CraftingRecipeJsonBuilder.getItemId(output),
    ) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.GRINDER)
            .addInput(input)
            .addOutput(output, count)
            .offerTo(exporter, id)
    }

    private fun grinderRecipe(
        exporter: RecipeExporter,
        input: TagKey<Item>,
        output: ItemConvertible,
        count: Int = 1,
        id: Identifier = CraftingRecipeJsonBuilder.getItemId(output),
    ) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.GRINDER)
            .addInput(input)
            .addOutput(output, count)
            .offerTo(exporter, id)
    }

    //    Alloy Furnace    //

    private fun alloyFurnaceRecipes(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.ALLOY_FURNACE)
            .addInput(ConventionalItemTags.COPPER_INGOTS)
            .addInput(RagiumContents.Dusts.RAW_RAGINITE, 4)
            .addOutput(RagiumContents.Ingots.RAGI_ALLOY)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.ALLOY_FURNACE)
            .addInput(ConventionalItemTags.COPPER_INGOTS)
            .addInput(ConventionalItemTags.GOLD_INGOTS)
            .addOutput(RagiumContents.Ingots.TWILIGHT_METAL, 2)
            .offerTo(exporter)
    }

    //    Compressor    //

    private fun compressor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.COMPRESSOR)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addInput(Items.WATER_BUCKET)
            .addOutput(RagiumContents.Fluids.WATER)
            .addOutput(Items.BUCKET)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.COMPRESSOR)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addInput(Items.LAVA_BUCKET)
            .addOutput(RagiumContents.Fluids.LAVA)
            .addOutput(Items.BUCKET)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.COMPRESSOR)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addInput(Items.MILK_BUCKET)
            .addOutput(RagiumContents.Fluids.MILK)
            .addOutput(Items.BUCKET)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.COMPRESSOR)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addInput(Items.HONEY_BOTTLE, 4)
            .addOutput(RagiumContents.Fluids.HONEY)
            .addOutput(Items.GLASS_BOTTLE, 4)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.COMPRESSOR)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addInput(Items.HONEY_BLOCK)
            .addOutput(RagiumContents.Fluids.HONEY)
            .offerTo(exporter, suffix = "_alt")
    }

    //    Extractor    //

    private fun extractor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.EXTRACTOR)
            .addInput(ItemTags.VILLAGER_PLANTABLE_SEEDS, 4)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addOutput(RagiumContents.Fluids.SEED_OIL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.EXTRACTOR)
            .addInput(ItemTags.MEAT, 2)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addOutput(RagiumContents.Fluids.TALLOW)
            .offerTo(exporter)
    }

    //    Metal Former    //

    private fun metalFormer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.METAL_FORMER)
            .addInput(RagiumItemTags.STEEL_INGOTS, 2)
            .addOutput(RagiumContents.SHAFT)
            .offerTo(exporter)
    }

    //    Mixer    //

    private fun mixer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.MIXER)
            .addInput(RagiumContents.Dusts.RAW_RAGINITE, 4)
            .addInput(RagiumContents.Fluids.WATER)
            .addOutput(RagiumContents.Dusts.RAGINITE, 4)
            .addOutput(RagiumContents.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.MIXER)
            .addInput(RagiumItemTags.ORGANIC_OILS)
            .addInput(RagiumContents.Dusts.ASH)
            .addOutput(RagiumContents.SOAP_INGOT)
            .addOutput(RagiumContents.Fluids.GLYCEROL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.MIXER)
            .addInput(RagiumContents.Dusts.RAGINITE, 2)
            .addInput(RagiumContents.SOAP_INGOT)
            .addInput(RagiumContents.Fluids.WATER)
            .addOutput(RagiumContents.Dusts.REFINED_RAGINITE, 2)
            .addOutput(RagiumContents.EMPTY_FLUID_CUBE)
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
        HTMachineRecipeJsonBuilder(HTMachineType.Single.MIXER)
            .addInput(input)
            .addInput(RagiumContents.SOAP_INGOT)
            .addInput(RagiumContents.Fluids.WATER)
            .addOutput(output)
            .addOutput(RagiumContents.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.CHEMICAL_REACTOR)
            .addInput(input)
            .addInput(RagiumContents.Fluids.CHLORINE)
            .addOutput(output)
            .addOutput(RagiumContents.EMPTY_FLUID_CUBE)
            .offerTo(exporter)
    }

    //    Centrifuge    //

    private fun centrifuge(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.CENTRIFUGE)
            .addInput(Items.SOUL_SAND)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addOutput(Items.SAND)
            .addOutput(RagiumContents.Fluids.OIL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.CENTRIFUGE)
            .addInput(Items.SOUL_SOIL)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addOutput(Items.CLAY)
            .addOutput(RagiumContents.Fluids.OIL)
            .offerTo(exporter)
    }

    //    Chemical Reactor    //

    private fun chemicalReactor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.CHEMICAL_REACTOR)
            .addInput(RagiumContents.Fluids.WATER)
            .addInput(RagiumContents.Fluids.METHANE)
            .setCatalyst(Items.HEART_OF_THE_SEA)
            .addOutput(RagiumContents.Fluids.HYDROGEN)
            .addOutput(RagiumContents.Fluids.METHANOL)
            .offerTo(exporter)

        // PE
        HTMachineRecipeJsonBuilder(HTMachineType.Single.CHEMICAL_REACTOR)
            .addInput(RagiumContents.Fluids.ETHYLENE)
            .addInput(RagiumContents.Fluids.OXYGEN)
            .addOutput(RagiumContents.Plates.PE)
            .addOutput(RagiumContents.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        // PVC
        HTMachineRecipeJsonBuilder(HTMachineType.Single.CHEMICAL_REACTOR)
            .addInput(RagiumContents.Fluids.ETHYLENE)
            .addInput(RagiumContents.Fluids.CHLORINE)
            .addOutput(RagiumContents.Fluids.VINYL_CHLORIDE)
            .addOutput(RagiumContents.Fluids.HYDROGEN)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.CHEMICAL_REACTOR)
            .addInput(RagiumContents.Fluids.VINYL_CHLORIDE)
            .addInput(RagiumContents.Fluids.OXYGEN)
            .addOutput(RagiumContents.Plates.PVC)
            .addOutput(RagiumContents.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        // PTFE
        HTMachineRecipeJsonBuilder(HTMachineType.Single.CHEMICAL_REACTOR)
            .addInput(RagiumContents.Fluids.ETHYLENE)
            .addInput(RagiumContents.Fluids.FLUORINE, 2)
            .addOutput(RagiumContents.Fluids.TETRA_FLUORO_ETHYLENE)
            .addOutput(RagiumContents.Fluids.HYDROGEN, 2)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.CHEMICAL_REACTOR)
            .addInput(RagiumContents.Fluids.TETRA_FLUORO_ETHYLENE)
            .addInput(RagiumContents.Fluids.OXYGEN)
            .addOutput(RagiumContents.Plates.PTFE)
            .addOutput(RagiumContents.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

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
        HTMachineRecipeJsonBuilder(HTMachineType.Single.CHEMICAL_REACTOR)
            .addInput(before)
            .addInput(RagiumContents.Fluids.OXYGEN)
            .addOutput(after)
            .addOutput(RagiumContents.EMPTY_FLUID_CUBE)
            .offerTo(exporter)
    }

    //    Electrolyzer    //

    private fun electrolyzer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.ELECTROLYZER)
            .addInput(RagiumContents.Fluids.WATER)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE, 2)
            .addOutput(RagiumContents.Fluids.HYDROGEN, 2)
            .addOutput(RagiumContents.Fluids.OXYGEN)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.ELECTROLYZER)
            .addInput(RagiumContents.Fluids.SALT_WATER)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addOutput(RagiumContents.Fluids.HYDROGEN)
            .addOutput(RagiumContents.Fluids.CHLORINE)
            .addOutput(RagiumContents.Dusts.ASH)
            .offerTo(exporter, suffix = "_salty")

        HTMachineRecipeJsonBuilder(HTMachineType.Single.ELECTROLYZER)
            .addInput(Items.GLOWSTONE_DUST)
            .addInput(RagiumContents.EMPTY_FLUID_CUBE)
            .addOutput(RagiumContents.Fluids.FLUORINE)
            .addOutput(Items.GOLD_NUGGET)
            .offerTo(exporter)
    }

    //    Blast Furnace    //

    private fun blastFurnace(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Multi.BRICK_BLAST_FURNACE)
            .addInput(ConventionalItemTags.IRON_INGOTS)
            .addInput(RagiumContents.Dusts.RAGINITE, 4)
            .addOutput(RagiumContents.Ingots.RAGI_STEEL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Multi.BRICK_BLAST_FURNACE)
            .addInput(ConventionalItemTags.IRON_INGOTS)
            .addInput(ItemTags.COALS, 4)
            .addOutput(RagiumContents.Ingots.STEEL)
            .offerTo(exporter)
    }

    //    Blazing Blast Furnace    //

    private fun blazingBlastFurnace(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Multi.BLAZING_BLAST_FURNACE)
            .addInput(RagiumItemTags.STEEL_INGOTS)
            .addInput(RagiumContents.Dusts.REFINED_RAGINITE, 4)
            .addInput(ConventionalItemTags.QUARTZ_GEMS)
            .addOutput(RagiumContents.Ingots.REFINED_RAGI_STEEL)
            .offerTo(exporter)
    }

    //    Distillation Tower    //

    private fun distillation(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Multi.DISTILLATION_TOWER)
            .addInput(RagiumContents.Fluids.OIL, 4)
            .addOutput(RagiumContents.Fluids.REFINED_GAS)
            .addOutput(RagiumContents.Fluids.NAPHTHA, 2)
            .addOutput(RagiumContents.Fluids.TAR)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Multi.DISTILLATION_TOWER)
            .addInput(RagiumContents.Fluids.REFINED_GAS, 4)
            .addOutput(RagiumContents.Fluids.METHANE, 2)
            .addOutput(RagiumContents.Fluids.LPG)
            .addOutput(RagiumContents.Fluids.HELIUM)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Multi.DISTILLATION_TOWER)
            .addInput(RagiumContents.Fluids.NAPHTHA, 2)
            .addOutput(RagiumContents.Fluids.ETHYLENE)
            .addOutput(RagiumContents.Fluids.DIESEL)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Multi.DISTILLATION_TOWER)
            .addInput(RagiumContents.Fluids.TAR, 3)
            .addOutput(RagiumContents.Fluids.BENZENE)
            .addOutput(RagiumContents.Fluids.TOLUENE)
            .addOutput(RagiumContents.Fluids.PHENOL)
            .offerTo(exporter)
    }

    //    Alchemical Infusion    //

    private fun infusion(exporter: RecipeExporter) {
        registerBudding(
            exporter,
            RagiElement.RAGIUM,
            Items.RED_STAINED_GLASS,
            Items.RED_NETHER_BRICKS,
            Items.BLAZE_POWDER,
        )
        registerBudding(
            exporter,
            RagiElement.RIGIUM,
            Items.YELLOW_STAINED_GLASS,
            Items.GLOWSTONE,
            Items.GOLDEN_APPLE,
        )
        registerBudding(
            exporter,
            RagiElement.RUGIUM,
            Items.LIME_STAINED_GLASS,
            Items.MELON,
            Items.ENDER_PEARL,
        )
        registerBudding(
            exporter,
            RagiElement.REGIUM,
            Items.LIGHT_BLUE_STAINED_GLASS,
            Items.BLUE_ICE,
            Items.PRISMARINE_SHARD,
        )
        registerBudding(
            exporter,
            RagiElement.ROGIUM,
            Items.PURPLE_STAINED_GLASS,
            Items.AMETHYST_BLOCK,
            Items.SHULKER_SHELL,
        )
    }

    private fun registerBudding(
        exporter: RecipeExporter,
        element: RagiElement,
        glass: ItemConvertible,
        ing1: ItemConvertible,
        ing2: ItemConvertible,
    ) {
        HTInfusionRecipeJsonBuilder(element.buddingBlock)
            .addInput(Items.CHISELED_QUARTZ_BLOCK)
            .addInput(glass, 64)
            .addInput(ing1, 16)
            .addInput(ing2, 8)
            .hasInput(RagiumContents.ALCHEMICAL_INFUSER)
            .offerTo(exporter)
    }

    //    Alchemical Transform    //

    private fun transform(exporter: RecipeExporter) {
        /*HTTransformRecipeJsonBuilder(Items.DIAMOND, Items.COAL_BLOCK)
            .addUpgrade(Items.COAL_BLOCK, 7)
            .hasInput(Items.COAL_BLOCK)
            .modifyComponents {
                add(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
            }.offerTo(exporter)*/
    }
}
