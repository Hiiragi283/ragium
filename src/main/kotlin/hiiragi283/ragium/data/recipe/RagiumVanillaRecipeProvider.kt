package hiiragi283.ragium.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.*
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.item.HTBackpackItem
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.component.ComponentChanges
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.book.RecipeCategory
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

    private lateinit var exporterCache: RecipeExporter

    override fun generate(exporter: RecipeExporter) {
        exporterCache = exporter
        craftingRecipes(exporter)
        cookingRecipes(exporter)
    }

    //    Crafting    //

    private fun craftingRecipes(exporter: RecipeExporter) {
        craftingAlternatives(exporter)
        craftingArmors(exporter)
        craftingBuildings(exporter)
        craftingFoods(exporter)
        craftingIngredients(exporter)
        craftingTools(exporter)
    }

    //    Crafting - Alternatives    //

    private fun craftingAlternatives(exporter: RecipeExporter) {
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
            ).input('A', RagiumItems.STELLA_PLATE)
            .input('B', RagiumContents.Gems.RAGIUM)
            .unlockedBy(RagiumItems.STELLA_PLATE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.STELLA_JACKET)
            .patterns(
                "A A",
                "ABA",
                "AAA",
            ).input('A', RagiumItems.STELLA_PLATE)
            .input('B', RagiumContents.Gems.RAGIUM)
            .unlockedBy(RagiumItems.STELLA_PLATE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.STELLA_LEGGINGS)
            .patterns(
                "ABA",
                "A A",
                "A A",
            ).input('A', RagiumItems.STELLA_PLATE)
            .input('B', RagiumContents.Gems.RAGIUM)
            .unlockedBy(RagiumItems.STELLA_PLATE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.STELLA_BOOTS)
            .patterns(
                "A A",
                "ABA",
            ).input('A', RagiumItems.STELLA_PLATE)
            .input('B', RagiumContents.Gems.RAGIUM)
            .unlockedBy(RagiumItems.STELLA_PLATE)
            .offerTo(exporter)
    }

    //    Crafting - Buildings    //

    private fun craftingBuildings(exporter: RecipeExporter) {
        // asphalt
        registerSlab(exporter, RagiumBlocks.ASPHALT_SLAB, RagiumBlocks.ASPHALT)
        registerStair(exporter, RagiumBlocks.ASPHALT_STAIRS, RagiumBlocks.ASPHALT)
        // lined asphalt
        /* HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.LINED_ASPHALT, 6)
            .patterns(
                "ABA",
                "ABA",
                "ABA",
            ).input('A', RagiumBlocks.ASPHALT)
            .input('B', ConventionalItemTags.WHITE_DYES)
            .unlockedBy(RagiumBlocks.ASPHALT)
            .offerTo(exporter)
        registerSlab(exporter, RagiumBlocks.LINED_ASPHALT_SLAB, RagiumBlocks.LINED_ASPHALT)
        registerStair(exporter, RagiumBlocks.LINED_ASPHALT_STAIRS, RagiumBlocks.LINED_ASPHALT) */
        // polished asphalt
        HTStonecuttingRecipeJsonBuilder.register(
            exporter,
            RagiumBlocks.ASPHALT,
            RagiumBlocks.POLISHED_ASPHALT,
            category = RecipeCategory.BUILDING_BLOCKS,
        )
        registerSlab(exporter, RagiumBlocks.POLISHED_ASPHALT_SLAB, RagiumBlocks.POLISHED_ASPHALT)
        registerStair(exporter, RagiumBlocks.POLISHED_ASPHALT_STAIRS, RagiumBlocks.POLISHED_ASPHALT)
        // gypsum
        registerSlab(exporter, RagiumBlocks.GYPSUM_SLAB, RagiumBlocks.GYPSUM)
        registerStair(exporter, RagiumBlocks.GYPSUM_STAIRS, RagiumBlocks.GYPSUM)
        // polished gypsum
        HTStonecuttingRecipeJsonBuilder.register(
            exporter,
            RagiumBlocks.GYPSUM,
            RagiumBlocks.POLISHED_GYPSUM,
            category = RecipeCategory.BUILDING_BLOCKS,
        )
        registerSlab(exporter, RagiumBlocks.POLISHED_GYPSUM_SLAB, RagiumBlocks.POLISHED_GYPSUM)
        registerStair(exporter, RagiumBlocks.POLISHED_GYPSUM_STAIRS, RagiumBlocks.POLISHED_GYPSUM)
        // slate
        registerSlab(exporter, RagiumBlocks.SLATE_SLAB, RagiumBlocks.SLATE)
        registerStair(exporter, RagiumBlocks.SLATE_STAIRS, RagiumBlocks.SLATE)
        // polished slate
        HTStonecuttingRecipeJsonBuilder.register(
            exporter,
            RagiumBlocks.SLATE,
            RagiumBlocks.POLISHED_SLATE,
            category = RecipeCategory.BUILDING_BLOCKS,
        )
        registerSlab(exporter, RagiumBlocks.POLISHED_SLATE_SLAB, RagiumBlocks.POLISHED_SLATE)
        registerStair(exporter, RagiumBlocks.POLISHED_SLATE_STAIRS, RagiumBlocks.POLISHED_SLATE)
    }

    private fun registerSlab(
        exporter: RecipeExporter,
        output: ItemConvertible,
        input: ItemConvertible,
        category: RecipeCategory = RecipeCategory.BUILDING_BLOCKS,
    ) {
        // shaped crafting
        HTShapedRecipeJsonBuilder
            .create(output, 6)
            .patterns("AAA")
            .input('A', input)
            .unlockedBy(input)
            .category(category)
            .offerTo(exporter)
        // stone cutting
        HTStonecuttingRecipeJsonBuilder.register(
            exporter,
            input,
            output,
            count = 2,
            category = category,
        )
    }

    private fun registerStair(
        exporter: RecipeExporter,
        output: ItemConvertible,
        input: ItemConvertible,
        category: RecipeCategory = RecipeCategory.BUILDING_BLOCKS,
    ) {
        // shaped crafting
        HTShapedRecipeJsonBuilder
            .create(output, 4)
            .patterns(
                "A  ",
                "AA ",
                "AAA",
            ).input('A', input)
            .unlockedBy(input)
            .category(category)
            .offerTo(exporter)
        // stone cutting
        HTStonecuttingRecipeJsonBuilder.register(
            exporter,
            input,
            output,
            category = category,
        )
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
            .create(RagiumItems.GIGANT_HAMMER)
            .patterns(
                "AAA",
                "AB ",
                " B ",
            ).input('A', RagiumContents.Gems.RAGIUM)
            .input('B', RagiumBlocks.SHAFT)
            .unlockedBy(RagiumContents.Gems.RAGIUM)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.RAGI_WRENCH)
            .patterns(
                "A A",
                "AAA",
                " A ",
            ).input('A', RagiumContents.Ingots.RAGI_ALLOY)
            .unlockedBy(RagiumContents.Ingots.RAGI_ALLOY)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.ROPE, 8)
            .patterns(
                "A",
                "A",
                "A",
            ).input('A', ItemTags.WOOL)
            .unlockedBy(ItemTags.WOOL)
            .offerTo(exporter)
        // dynamites
        HTShapelessRecipeJsonBuilder
            .create(RagiumItems.ANVIL_DYNAMITE)
            .input(RagiumItems.DYNAMITE)
            .input(ItemTags.ANVIL)
            .unlockedBy(RagiumItems.DYNAMITE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.BEDROCK_DYNAMITE, 8)
            .patterns(
                "AAA",
                "ABA",
                "AAA",
            ).input('A', RagiumItems.DYNAMITE)
            .input('B', Items.DIAMOND_PICKAXE)
            .unlockedBy(RagiumItems.DYNAMITE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.FLATTENING_DYNAMITE, 8)
            .patterns(
                "AAA",
                "ABA",
                "AAA",
            ).input('A', RagiumItems.DYNAMITE)
            .input('B', Items.NETHER_STAR)
            .unlockedBy(RagiumItems.DYNAMITE)
            .offerTo(exporter)
        // filter
        HTShapelessRecipeJsonBuilder
            .create(RagiumItems.FLUID_FILTER)
            .input(Items.PAPER)
            .input(ConventionalItemTags.LIGHT_BLUE_DYES)
            .input(RagiumContents.Dusts.RAGINITE)
            .unlockedBy(Items.PAPER)
            .offerTo(exporter)

        HTShapelessRecipeJsonBuilder
            .create(RagiumItems.ITEM_FILTER)
            .input(Items.PAPER)
            .input(ConventionalItemTags.ORANGE_DYES)
            .input(RagiumContents.Dusts.RAGINITE)
            .unlockedBy(Items.PAPER)
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
        // sweet berries cake
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
            .create(RagiumItems.SWEET_BERRIES_CAKE_PIECE, 8)
            .input(RagiumBlocks.SWEET_BERRIES_CAKE)
            .unlockedBy(RagiumItems.SWEET_BERRIES_CAKE_PIECE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.SWEET_BERRIES_CAKE)
            .patterns(
                "AAA",
                "A A",
                "AAA",
            ).input('A', RagiumItems.SWEET_BERRIES_CAKE_PIECE)
            .unlockedBy(RagiumItems.SWEET_BERRIES_CAKE_PIECE)
            .offerSuffix(exporter, "_from_piece")
        // yellow cake
        HTShapelessRecipeJsonBuilder
            .create(RagiumItems.YELLOW_CAKE_PIECE, 8)
            .input(RagiumItems.YELLOW_CAKE)
            .unlockedBy(RagiumItems.YELLOW_CAKE_PIECE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.YELLOW_CAKE)
            .patterns(
                "AAA",
                "A A",
                "AAA",
            ).input('A', RagiumItems.YELLOW_CAKE_PIECE)
            .unlockedBy(RagiumItems.YELLOW_CAKE_PIECE)
            .offerSuffix(exporter, "_from_piece")
        // chocolate
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
        // fluid cubes
        createEmptyFluidCube(exporter, Items.GLASS_PANE, 4)
        createEmptyFluidCube(exporter, RagiumItems.PLASTIC_PLATE, 8, "_pe")
        createEmptyFluidCube(exporter, RagiumItems.ENGINEERING_PLASTIC_PLATE, 16, "_pvc")

        HTShapelessRecipeJsonBuilder
            .create(RagiumItems.EMPTY_FLUID_CUBE)
            .input(RagiumItems.EMPTY_FLUID_CUBE)
            .unlockedBy(RagiumItems.EMPTY_FLUID_CUBE)
            .offerTo(exporter, RagiumAPI.id("clear_fluid_cube"))
    }

    private fun createEmptyFluidCube(
        exporter: RecipeExporter,
        input: ItemConvertible,
        count: Int,
        suffix: String? = null,
    ) {
        val id: Identifier = RagiumAPI.id("empty_fluid_cube$suffix")
        // shaped crafting
        HTShapedRecipeJsonBuilder
            .create(RagiumItems.EMPTY_FLUID_CUBE, count)
            .patterns(
                " A ",
                "A A",
                " A ",
            ).input('A', input)
            .unlockedBy(input)
            .offerTo(exporter, id)
        // assembler
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ASSEMBLER)
            .itemInput(input, 4)
            .catalyst(RagiumItems.EMPTY_FLUID_CUBE)
            .itemOutput(RagiumItems.EMPTY_FLUID_CUBE, count)
            .offerTo(exporter, id)
    }

    //    Cooking   //

    private fun cookingRecipes(exporter: RecipeExporter) {
        HTCookingRecipeJsonBuilder.smeltAndBlast(
            exporter,
            RagiumItems.RAGI_ALLOY_COMPOUND,
            RagiumContents.Ingots.RAGI_ALLOY,
        )
        HTCookingRecipeJsonBuilder.smeltAndBlast(
            exporter,
            RagiumItems.CRIMSON_CRYSTAL,
            Items.BLAZE_POWDER,
        )
        HTCookingRecipeJsonBuilder.smeltAndBlast(
            exporter,
            RagiumItems.WARPED_CRYSTAL,
            Items.ENDER_PEARL,
        )
        HTCookingRecipeJsonBuilder.smeltAndBlast(
            exporter,
            RagiumContents.Dusts.QUARTZ.prefixedTagKey,
            RagiumItems.CRUDE_SILICON,
        )

        HTCookingRecipeJsonBuilder.smeltAndSmoke(
            exporter,
            RagiumItems.DOUGH,
            Items.BREAD,
        )
        HTCookingRecipeJsonBuilder.smeltAndSmoke(
            exporter,
            RagiumItems.MEAT_INGOT,
            RagiumItems.COOKED_MEAT_INGOT,
        )
    }
}
