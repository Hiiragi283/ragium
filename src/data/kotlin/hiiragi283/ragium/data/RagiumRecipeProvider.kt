package hiiragi283.ragium.data

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.data.HTMachineRecipeJsonBuilder
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.resource.HTHardModeResourceCondition
import hiiragi283.ragium.data.group.HTMetalItemRecipeGroup
import hiiragi283.ragium.data.group.RagiumMetalItemRecipeGroups
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

        RagiumMetalItemRecipeGroups
        HTMetalItemRecipeGroup.registry.forEach { (name: String, family: HTMetalItemRecipeGroup) ->
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
        createShaped(RagiumItems.RAGI_ALLOY_COMPOUND)
            .group("ragi_alloy_compound")
            .pattern("AAA")
            .pattern("ABA")
            .pattern("AAA")
            .input('A', RagiumItems.RAW_RAGINITE)
            .input('B', ConventionalItemTags.COPPER_INGOTS)
            .itemCriterion(RagiumItems.RAW_RAGINITE)
            .offerTo(exporter, Ragium.id("shaped/ragi_alloy_compound"))

        createShaped(RagiumItems.RAGI_ALLOY_COMPOUND)
            .group("ragi_alloy_compound")
            .pattern(" A ")
            .pattern("ABA")
            .pattern(" A ")
            .input('A', RagiumItems.RAW_RAGINITE_DUST)
            .input('B', ConventionalItemTags.COPPER_INGOTS)
            .itemCriterion(RagiumItems.RAW_RAGINITE_DUST)
            .offerTo(
                exporter.hardMode(false),
                Ragium.id("shaped/ragi_alloy_compound_1"),
            )

        createEmptyFluidCube(exporter, Items.GLASS_PANE, 4)

        // tools
        createShaped(RagiumItems.FORGE_HAMMER)
            .pattern(" AA")
            .pattern("BBA")
            .pattern(" AA")
            .input('A', RagiumItems.RAGI_ALLOY_INGOT)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .itemCriterion(RagiumItems.RAGI_ALLOY_INGOT)
            .offerTo(exporter, Ragium.id("shaped/forge_hammer"))

        createShaped(RagiumItems.STEEL_SWORD)
            .pattern("B")
            .pattern("A")
            .pattern("A")
            .input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .tagCriterion(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter, Ragium.id("shaped/steel_sword"))

        createShaped(RagiumItems.STEEL_SHOVEL)
            .pattern("B")
            .pattern("B")
            .pattern("A")
            .input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .tagCriterion(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter, Ragium.id("shaped/steel_shovel"))

        createShaped(RagiumItems.STEEL_PICKAXE)
            .pattern(" B ")
            .pattern(" B ")
            .pattern("AAA")
            .input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .tagCriterion(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter, Ragium.id("shaped/steel_pickaxe"))

        createShaped(RagiumItems.STEEL_AXE)
            .pattern("B ")
            .pattern("BA")
            .pattern("AA")
            .input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .tagCriterion(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter, Ragium.id("shaped/steel_axe"))

        createShaped(RagiumItems.STEEL_HOE)
            .pattern("B ")
            .pattern("B ")
            .pattern("AA")
            .input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .tagCriterion(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter, Ragium.id("shaped/steel_hoe"))

        // hulls
        val hullMap: Map<HTMachineTier, Block> = mapOf(
            HTMachineTier.HEAT to RagiumBlocks.RAGI_ALLOY_HULL,
            HTMachineTier.KINETIC to RagiumBlocks.RAGI_STEEL_HULL,
            HTMachineTier.ELECTRIC to RagiumBlocks.REFINED_RAGI_STEEL_HULL,
        )
        val ingotMap: Map<HTMachineTier, Item> = mapOf(
            HTMachineTier.HEAT to RagiumItems.RAGI_ALLOY_INGOT,
            HTMachineTier.KINETIC to RagiumItems.RAGI_STEEL_INGOT,
            HTMachineTier.ELECTRIC to RagiumItems.REFINED_RAGI_STEEL_INGOT,
        )
        val plateMap: Map<HTMachineTier, Item> = mapOf(
            HTMachineTier.HEAT to RagiumItems.RAGI_ALLOY_PLATE,
            HTMachineTier.KINETIC to RagiumItems.RAGI_STEEL_PLATE,
            HTMachineTier.ELECTRIC to RagiumItems.REFINED_RAGI_STEEL_PLATE,
        )

        ingotMap.forEach { (tier: HTMachineTier, ingot: Item) ->
            val hull: Block = hullMap[tier]!!
            createShaped(hull)
                .pattern("AAA")
                .pattern("A A")
                .pattern("BBB")
                .input('A', ingot)
                .input('B', tier.base)
                .itemCriterion(ingot)
                .offerTo(
                    exporter.hardMode(false),
                    CraftingRecipeJsonBuilder.getItemId(hull).withPrefixedPath("shaped/"),
                )
        }
        plateMap.forEach { (tier: HTMachineTier, plate: Item) ->
            val hull: Block = hullMap[tier]!!
            createShaped(hull)
                .pattern("AAA")
                .pattern("A A")
                .pattern("BBB")
                .input('A', plate)
                .input('B', tier.base)
                .itemCriterion(plate)
                .offerTo(
                    exporter.hardMode(true),
                    CraftingRecipeJsonBuilder.getItemId(hull).withPrefixedPath("shaped/hard/"),
                )
        }
        // machines
        createShaped(RagiumBlocks.MANUAL_GRINDER)
            .pattern("A  ")
            .pattern("BBB")
            .pattern("CCC")
            .input('A', ConventionalItemTags.WOODEN_RODS)
            .input('B', RagiumItems.RAGI_ALLOY_INGOT)
            .input('C', Items.SMOOTH_STONE)
            .itemCriterion(RagiumItems.RAGI_ALLOY_INGOT)
            .offerTo(exporter, Ragium.id("shaped/manual_grinder"))

        createShaped(RagiumBlocks.MANUAL_GRINDER)
            .pattern("A  ")
            .pattern("BBB")
            .pattern("CCC")
            .input('A', ConventionalItemTags.WOODEN_RODS)
            .input('B', RagiumItems.RAGI_ALLOY_PLATE)
            .input('C', Items.SMOOTH_STONE)
            .itemCriterion(RagiumItems.RAGI_ALLOY_PLATE)
            .offerTo(exporter, Ragium.id("shaped/hard/manual_grinder"))

        createShaped(RagiumBlocks.BURNING_BOX)
            .pattern("AAA")
            .pattern("A A")
            .pattern("ABA")
            .input('A', Items.BRICKS)
            .input('B', Items.FURNACE)
            .itemCriterion(Items.BRICKS)
            .offerTo(exporter, Ragium.id("shaped/burning_box"))

        createShaped(RagiumBlocks.GEAR_BOX)
            .pattern("AAA")
            .pattern("BCB")
            .pattern("BDB")
            .input('A', RagiumItems.RAGI_STEEL_INGOT)
            .input('B', HTMachineTier.KINETIC.base)
            .input('C', RagiumBlocks.SHAFT)
            .input('D', ConventionalItemTags.REDSTONE_DUSTS)
            .itemCriterion(RagiumBlocks.SHAFT)
            .offerTo(exporter, Ragium.id("shaped/gear_box"))

        // tiered machines
        // tier1
        createMachine(
            exporter,
            HTMachineType.Single.ALLOY_FURNACE,
            RagiumItems.RAGI_ALLOY_INGOT,
            Items.FURNACE,
            RagiumBlocks.RAGI_ALLOY_HULL,
        )

        createMachine(
            exporter,
            HTMachineType.Multi.BRICK_BLAST_FURNACE,
            RagiumItems.RAGI_ALLOY_INGOT,
            Items.BLAST_FURNACE,
            RagiumBlocks.RAGI_ALLOY_HULL,
        )

        // tier2
        createMachine(
            exporter,
            HTMachineType.Single.COMPRESSOR,
            RagiumItems.RAGI_STEEL_INGOT,
            Items.PISTON,
            RagiumBlocks.RAGI_STEEL_HULL,
        )

        createMachine(
            exporter,
            HTMachineType.Single.EXTRACTOR,
            RagiumItems.RAGI_STEEL_INGOT,
            Items.BUCKET, // TODO
            RagiumBlocks.RAGI_STEEL_HULL,
        )

        createMachine(
            exporter,
            HTMachineType.Single.GRINDER,
            RagiumItems.RAGI_STEEL_INGOT,
            Items.FLINT,
            RagiumBlocks.RAGI_STEEL_HULL,
        )

        createMachine(
            exporter,
            HTMachineType.Single.METAL_FORMER,
            RagiumItems.RAGI_STEEL_INGOT,
            RagiumItems.FORGE_HAMMER,
            RagiumBlocks.RAGI_STEEL_HULL,
        )

        createMachine(
            exporter,
            HTMachineType.Single.MIXER,
            RagiumItems.RAGI_STEEL_INGOT,
            Items.CAULDRON, // TODO
            RagiumBlocks.RAGI_STEEL_HULL,
        )

        createMachine(
            exporter,
            HTMachineType.Multi.BLAZING_BLAST_FURNACE,
            RagiumItems.RAGI_STEEL_INGOT,
            HTMachineType.Multi.BRICK_BLAST_FURNACE,
            RagiumBlocks.RAGI_STEEL_HULL,
        )

        // tier3
        createMachine(
            exporter,
            HTMachineType.Single.CENTRIFUGE,
            RagiumItems.REFINED_RAGI_STEEL_INGOT,
            Items.COPPER_GRATE, // TODO
            RagiumBlocks.REFINED_RAGI_STEEL_HULL,
        )

        createMachine(
            exporter,
            HTMachineType.Single.CHEMICAL_REACTOR,
            RagiumItems.REFINED_RAGI_STEEL_INGOT,
            Items.CRAFTING_TABLE, // TODO
            RagiumBlocks.REFINED_RAGI_STEEL_HULL,
        )

        createMachine(
            exporter,
            HTMachineType.Single.ELECTROLYZER,
            RagiumItems.REFINED_RAGI_STEEL_INGOT,
            Items.HOPPER, // TODO
            RagiumBlocks.REFINED_RAGI_STEEL_HULL,
        )

        createMachine(
            exporter,
            HTMachineType.Multi.DISTILLATION_TOWER,
            RagiumItems.REFINED_RAGI_STEEL_INGOT,
            Items.COPPER_GRATE,
            RagiumBlocks.REFINED_RAGI_STEEL_HULL,
        )
        // tier4
    }

    private fun createShaped(output: ItemConvertible, count: Int = 1): ShapedRecipeJsonBuilder =
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, output, count)

    private fun createShapeless(output: ItemConvertible): ShapelessRecipeJsonBuilder =
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, output)

    private fun createEmptyFluidCube(
        exporter: RecipeExporter,
        input: Item,
        count: Int,
        suffix: String? = null,
    ) {
        createShaped(RagiumItems.EMPTY_FLUID_CUBE, count)
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
                Ingredient.ofItems(RagiumItems.RAGI_ALLOY_COMPOUND),
                RecipeCategory.MISC,
                RagiumItems.RAGI_ALLOY_INGOT,
                0.0f,
                200,
            ).itemCriterion(RagiumItems.RAGI_ALLOY_COMPOUND)
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
            .addInput(RagiumItems.RAW_RAGINITE_DUST, 4)
            .addOutput(RagiumItems.RAGI_ALLOY_INGOT)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.ALLOY_FURNACE)
            .addInput(ConventionalItemTags.COPPER_INGOTS)
            .addInput(ConventionalItemTags.GOLD_INGOTS)
            .addOutput(RagiumItems.TWILIGHT_METAL_INGOT, 2)
            .offerTo(exporter)
    }

    //    Compressor    //

    private fun compressor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.COMPRESSOR)
            .addInput(RagiumItems.EMPTY_FLUID_CUBE)
            .addInput(Items.WATER_BUCKET)
            .addOutput(RagiumItems.WATER_FLUID_CUBE)
            .addOutput(Items.BUCKET)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.COMPRESSOR)
            .addInput(RagiumItems.EMPTY_FLUID_CUBE)
            .addInput(Items.LAVA_BUCKET)
            .addOutput(RagiumItems.LAVA_FLUID_CUBE)
            .addOutput(Items.BUCKET)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.COMPRESSOR)
            .addInput(RagiumItems.EMPTY_FLUID_CUBE)
            .addInput(Items.MILK_BUCKET)
            .addOutput(RagiumItems.MILK_FLUID_CUBE)
            .addOutput(Items.BUCKET)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.COMPRESSOR)
            .addInput(RagiumItems.EMPTY_FLUID_CUBE)
            .addInput(Items.HONEY_BOTTLE, 4)
            .addOutput(RagiumItems.HONEY_FLUID_CUBE)
            .addOutput(Items.GLASS_BOTTLE, 4)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.COMPRESSOR)
            .addInput(RagiumItems.EMPTY_FLUID_CUBE)
            .addInput(Items.HONEY_BLOCK)
            .addOutput(RagiumItems.HONEY_FLUID_CUBE)
            .offerTo(exporter, suffix = "_alt")
    }

    //    Extractor    //

    private fun extractor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.EXTRACTOR)
            .addInput(ItemTags.VILLAGER_PLANTABLE_SEEDS, 4)
            .addInput(RagiumItems.EMPTY_FLUID_CUBE)
            .addOutput(RagiumItems.SEED_OIL_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.EXTRACTOR)
            .addInput(ItemTags.MEAT, 2)
            .addInput(RagiumItems.EMPTY_FLUID_CUBE)
            .addOutput(RagiumItems.TALLOW_FLUID_CUBE)
            .offerTo(exporter)
    }

    //    Metal Former    //

    private fun metalFormer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.METAL_FORMER)
            .addInput(RagiumItemTags.STEEL_INGOTS, 2)
            .addOutput(RagiumBlocks.SHAFT)
            .offerTo(exporter)
    }

    //    Mixer    //

    private fun mixer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.MIXER)
            .addInput(RagiumItems.RAW_RAGINITE_DUST, 4)
            .addInput(RagiumItems.WATER_FLUID_CUBE)
            .addOutput(RagiumItems.RAGINITE_DUST, 4)
            .addOutput(RagiumItems.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.MIXER)
            .addInput(RagiumItems.SEED_OIL_FLUID_CUBE)
            .addInput(RagiumItems.ASH_DUST)
            .addOutput(RagiumItems.SOAP_INGOT)
            .addOutput(RagiumItems.GLYCEROL_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.MIXER)
            .addInput(RagiumItems.RAGINITE_DUST, 2)
            .addInput(RagiumItems.SOAP_INGOT)
            .addInput(RagiumItems.WATER_FLUID_CUBE)
            .addOutput(RagiumItems.REFINED_RAGINITE_DUST, 2)
            .addOutput(RagiumItems.EMPTY_FLUID_CUBE)
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
            .addInput(RagiumItems.SOAP_INGOT)
            .addInput(RagiumItems.WATER_FLUID_CUBE)
            .addOutput(output)
            .addOutput(RagiumItems.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.CHEMICAL_REACTOR)
            .addInput(input)
            .addInput(RagiumItems.CHLORINE_FLUID_CUBE)
            .addOutput(output)
            .addOutput(RagiumItems.EMPTY_FLUID_CUBE)
            .offerTo(exporter)
    }

    //    Centrifuge    //

    private fun centrifuge(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.CENTRIFUGE)
            .addInput(Items.SOUL_SAND)
            .addInput(RagiumItems.EMPTY_FLUID_CUBE)
            .addOutput(Items.SAND)
            .addOutput(RagiumItems.OIL_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.CENTRIFUGE)
            .addInput(Items.SOUL_SOIL)
            .addInput(RagiumItems.EMPTY_FLUID_CUBE)
            .addOutput(Items.CLAY)
            .addOutput(RagiumItems.OIL_FLUID_CUBE)
            .offerTo(exporter)
    }

    //    Chemical Reactor    //

    private fun chemicalReactor(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.CHEMICAL_REACTOR)
            .addInput(RagiumItems.WATER_FLUID_CUBE)
            .addInput(RagiumItems.METHANE_FLUID_CUBE)
            .setCatalyst(Items.HEART_OF_THE_SEA)
            .addOutput(RagiumItems.HYDROGEN_FLUID_CUBE)
            .addOutput(RagiumItems.METHANOL_FLUID_CUBE)
            .offerTo(exporter)

        // PE
        HTMachineRecipeJsonBuilder(HTMachineType.Single.CHEMICAL_REACTOR)
            .addInput(RagiumItems.ETHYLENE_FLUID_CUBE)
            .addInput(RagiumItems.OXYGEN_FLUID_CUBE)
            .addOutput(RagiumItems.PE_PLATE)
            .addOutput(RagiumItems.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        // PVC
        HTMachineRecipeJsonBuilder(HTMachineType.Single.CHEMICAL_REACTOR)
            .addInput(RagiumItems.ETHYLENE_FLUID_CUBE)
            .addInput(RagiumItems.CHLORINE_FLUID_CUBE)
            .addOutput(RagiumItems.VC_FLUID_CUBE)
            .addOutput(RagiumItems.HYDROGEN_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.CHEMICAL_REACTOR)
            .addInput(RagiumItems.VC_FLUID_CUBE)
            .addInput(RagiumItems.OXYGEN_FLUID_CUBE)
            .addOutput(RagiumItems.PVC_PLATE)
            .addOutput(RagiumItems.EMPTY_FLUID_CUBE)
            .offerTo(exporter)

        // PTFE
        HTMachineRecipeJsonBuilder(HTMachineType.Single.CHEMICAL_REACTOR)
            .addInput(RagiumItems.ETHYLENE_FLUID_CUBE)
            .addInput(RagiumItems.FLUORINE_FLUID_CUBE, 2)
            .addOutput(RagiumItems.TFE_FLUID_CUBE)
            .addOutput(RagiumItems.HYDROGEN_FLUID_CUBE, 2)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.CHEMICAL_REACTOR)
            .addInput(RagiumItems.TFE_FLUID_CUBE)
            .addInput(RagiumItems.OXYGEN_FLUID_CUBE)
            .addOutput(RagiumItems.PTFE_PLATE)
            .addOutput(RagiumItems.EMPTY_FLUID_CUBE)
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
            .addInput(RagiumItems.OXYGEN_FLUID_CUBE)
            .addOutput(after)
            .addOutput(RagiumItems.EMPTY_FLUID_CUBE)
            .offerTo(exporter)
    }

    //    Electrolyzer    //

    private fun electrolyzer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.ELECTROLYZER)
            .addInput(RagiumItems.WATER_FLUID_CUBE)
            .addInput(RagiumItems.EMPTY_FLUID_CUBE, 2)
            .addOutput(RagiumItems.HYDROGEN_FLUID_CUBE, 2)
            .addOutput(RagiumItems.OXYGEN_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Single.ELECTROLYZER)
            .addInput(RagiumItems.SALT_WATER_FLUID_CUBE)
            .addInput(RagiumItems.EMPTY_FLUID_CUBE)
            .addOutput(RagiumItems.HYDROGEN_FLUID_CUBE)
            .addOutput(RagiumItems.CHLORINE_FLUID_CUBE)
            .addOutput(RagiumItems.ASH_DUST)
            .offerTo(exporter, suffix = "_salty")
    }

    //    Blast Furnace    //

    private fun blastFurnace(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Multi.BRICK_BLAST_FURNACE)
            .addInput(ConventionalItemTags.IRON_INGOTS)
            .addInput(RagiumItems.RAGINITE_DUST, 4)
            .addOutput(RagiumItems.RAGI_STEEL_INGOT)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Multi.BRICK_BLAST_FURNACE)
            .addInput(ConventionalItemTags.IRON_INGOTS)
            .addInput(ItemTags.COALS, 4)
            .addOutput(RagiumItems.STEEL_INGOT)
            .offerTo(exporter)
    }

    //    Blazing Blast Furnace    //

    private fun blazingBlastFurnace(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Multi.BLAZING_BLAST_FURNACE)
            .addInput(RagiumItemTags.STEEL_INGOTS)
            .addInput(RagiumItems.REFINED_RAGINITE_DUST, 4)
            .addInput(ConventionalItemTags.QUARTZ_GEMS)
            .addOutput(RagiumItems.REFINED_RAGI_STEEL_INGOT)
            .offerTo(exporter)
    }

    //    Distillation Tower    //

    private fun distillation(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Multi.DISTILLATION_TOWER)
            .addInput(RagiumItems.OIL_FLUID_CUBE, 4)
            .addOutput(RagiumItems.REFINED_GAS_FLUID_CUBE)
            .addOutput(RagiumItems.NAPHTHA_FLUID_CUBE, 2)
            .addOutput(RagiumItems.TAR_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Multi.DISTILLATION_TOWER)
            .addInput(RagiumItems.REFINED_GAS_FLUID_CUBE, 4)
            .addOutput(RagiumItems.METHANE_FLUID_CUBE, 2)
            .addOutput(RagiumItems.LPG_FLUID_CUBE)
            .addOutput(RagiumItems.HELIUM_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Multi.DISTILLATION_TOWER)
            .addInput(RagiumItems.NAPHTHA_FLUID_CUBE, 2)
            .addOutput(RagiumItems.ETHYLENE_FLUID_CUBE)
            .addOutput(RagiumItems.DIESEL_FLUID_CUBE)
            .offerTo(exporter)

        HTMachineRecipeJsonBuilder(HTMachineType.Multi.DISTILLATION_TOWER)
            .addInput(RagiumItems.TAR_FLUID_CUBE, 3)
            .addOutput(RagiumItems.BENZENE_FLUID_CUBE)
            .addOutput(RagiumItems.TOLUENE_FLUID_CUBE)
            .addOutput(RagiumItems.PHENOL_FLUID_CUBE)
            .offerTo(exporter)
    }
}
