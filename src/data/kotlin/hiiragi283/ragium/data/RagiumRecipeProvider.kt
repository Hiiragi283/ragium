package hiiragi283.ragium.data

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.data.HTMachineRecipeJsonBuilder
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.resource.HTHardModeResourceCondition
import hiiragi283.ragium.data.group.HTMetalItemRecipeGroup
import hiiragi283.ragium.data.group.RagiumMetalItemRecipeGroups
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.block.Blocks
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
        mixer(exporter)
        blazingBlastFurnace(exporter)
        // tier3
        centrifuge(exporter)
        // tier4
        // tier5
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

        createShaped(RagiumItems.FORGE_HAMMER)
            .pattern(" AA")
            .pattern("BBA")
            .pattern(" AA")
            .input('A', RagiumItems.RAGI_ALLOY_INGOT)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .itemCriterion(RagiumItems.RAGI_ALLOY_INGOT)
            .offerTo(exporter, Ragium.id("shaped/forge_hammer"))

        createEmptyFluidCube(exporter, Items.GLASS_PANE, 4)

        // hulls
        createShaped(RagiumBlocks.RAGI_ALLOY_HULL)
            .pattern("AAA")
            .pattern("A A")
            .pattern("BBB")
            .input('A', RagiumItems.RAGI_ALLOY_INGOT)
            .input('B', Blocks.BRICKS)
            .itemCriterion(RagiumItems.RAGI_ALLOY_INGOT)
            .offerTo(
                exporter.hardMode(false),
                Ragium.id("shaped/ragi_alloy_hull"),
            )

        createShaped(RagiumBlocks.RAGI_ALLOY_HULL)
            .pattern("AAA")
            .pattern("A A")
            .pattern("BBB")
            .input('A', RagiumItems.RAGI_ALLOY_PLATE)
            .input('B', Blocks.BRICKS)
            .itemCriterion(RagiumItems.RAGI_ALLOY_PLATE)
            .offerTo(
                exporter.hardMode(true),
                Ragium.id("shaped/hard/ragi_alloy_hull"),
            )

        createShaped(RagiumBlocks.RAGI_STEEL_HULL)
            .pattern("AAA")
            .pattern("A A")
            .pattern("BBB")
            .input('A', RagiumItems.RAGI_STEEL_INGOT)
            .input('B', Blocks.DEEPSLATE_TILES)
            .itemCriterion(RagiumItems.RAGI_STEEL_INGOT)
            .offerTo(
                exporter.hardMode(false),
                Ragium.id("shaped/ragi_steel_hull"),
            )

        createShaped(RagiumBlocks.RAGI_STEEL_HULL)
            .pattern("AAA")
            .pattern("A A")
            .pattern("BBB")
            .input('A', RagiumItems.RAGI_STEEL_PLATE)
            .input('B', Blocks.DEEPSLATE_TILES)
            .itemCriterion(RagiumItems.RAGI_STEEL_PLATE)
            .offerTo(
                exporter.hardMode(false),
                Ragium.id("shaped/hard/ragi_steel_hull"),
            )

        createShaped(RagiumBlocks.REFINED_RAGI_STEEL_HULL)
            .pattern("AAA")
            .pattern("A A")
            .pattern("BBB")
            .input('A', RagiumItems.REFINED_RAGI_STEEL_INGOT)
            .input('B', Blocks.CHISELED_QUARTZ_BLOCK)
            .itemCriterion(RagiumItems.REFINED_RAGI_STEEL_INGOT)
            .offerTo(
                exporter.hardMode(false),
                Ragium.id("shaped/refined_ragi_steel_hull"),
            )

        createShaped(RagiumBlocks.REFINED_RAGI_STEEL_HULL)
            .pattern("AAA")
            .pattern("A A")
            .pattern("BBB")
            .input('A', RagiumItems.REFINED_RAGI_STEEL_PLATE)
            .input('B', Blocks.CHISELED_QUARTZ_BLOCK)
            .itemCriterion(RagiumItems.REFINED_RAGI_STEEL_PLATE)
            .offerTo(
                exporter.hardMode(false),
                Ragium.id("shaped/hard/refined_ragi_steel_hull"),
            )

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

        createShaped(HTMachineType.Single.ALLOY_FURNACE)
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .input('A', RagiumItems.RAGI_ALLOY_INGOT)
            .input('B', Items.FURNACE)
            .input('C', RagiumBlocks.RAGI_ALLOY_HULL)
            .input('D', Items.SMOOTH_STONE)
            .itemCriterion(RagiumBlocks.RAGI_ALLOY_HULL)
            .offerTo(exporter, Ragium.id("shaped/alloy_furnace"))

        createShaped(HTMachineType.Multi.BRICK_BLAST_FURNACE)
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .input('A', RagiumItems.RAGI_ALLOY_INGOT)
            .input('B', Items.BLAST_FURNACE)
            .input('C', RagiumBlocks.RAGI_ALLOY_HULL)
            .input('D', Items.BRICKS)
            .itemCriterion(RagiumBlocks.RAGI_ALLOY_HULL)
            .offerTo(exporter, Ragium.id("shaped/brick_blast_furnace"))
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

    //    Mixer    //

    private fun mixer(exporter: RecipeExporter) {
        HTMachineRecipeJsonBuilder(HTMachineType.Single.MIXER)
            .addInput(RagiumItems.RAW_RAGINITE, 4)
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
}
