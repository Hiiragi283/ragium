package hiiragi283.ragium.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTFluidContent
import hiiragi283.ragium.api.data.*
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
        RagiumItemsNew.SteelArmors.entries.forEach { armor ->
            HTShapedRecipeJsonBuilder
                .create(armor)
                .patterns(armor.armorType.getShapedPattern())
                .input('A', RagiumItemsNew.Ingots.STEEL)
                .unlockedBy(RagiumItemsNew.Ingots.STEEL)
                .offerTo(exporter)
        }
        // deep steel
        RagiumItemsNew.DeepSteelArmors.entries.forEach { armor ->
            HTShapedRecipeJsonBuilder
                .create(armor)
                .patterns(armor.armorType.getShapedPattern())
                .input('A', RagiumItemsNew.Ingots.DEEP_STEEL)
                .unlockedBy(RagiumItemsNew.Ingots.DEEP_STEEL)
                .offerTo(exporter)
        }
        // stella
        HTShapedRecipeJsonBuilder
            .create(RagiumItemsNew.StellaSuits.GOGGLE)
            .patterns(
                "ABA",
                "A A",
            ).input('A', RagiumItems.STELLA_PLATE)
            .input('B', RagiumItemsNew.Gems.RAGIUM)
            .unlockedBy(RagiumItems.STELLA_PLATE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItemsNew.StellaSuits.JACKET)
            .patterns(
                "A A",
                "ABA",
                "AAA",
            ).input('A', RagiumItems.STELLA_PLATE)
            .input('B', RagiumItemsNew.Gems.RAGIUM)
            .unlockedBy(RagiumItems.STELLA_PLATE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItemsNew.StellaSuits.LEGGINGS)
            .patterns(
                "ABA",
                "A A",
                "A A",
            ).input('A', RagiumItems.STELLA_PLATE)
            .input('B', RagiumItemsNew.Gems.RAGIUM)
            .unlockedBy(RagiumItems.STELLA_PLATE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItemsNew.StellaSuits.BOOTS)
            .patterns(
                "A A",
                "ABA",
            ).input('A', RagiumItems.STELLA_PLATE)
            .input('B', RagiumItemsNew.Gems.RAGIUM)
            .unlockedBy(RagiumItems.STELLA_PLATE)
            .offerTo(exporter)
    }

    //    Crafting - Buildings    //

    private fun craftingBuildings(exporter: RecipeExporter) {
        // slabs
        RagiumBlocks.Slabs.entries.forEach { slabs: RagiumBlocks.Slabs ->
            registerSlab(exporter, slabs, slabs.baseStone)
        }
        // stairs
        RagiumBlocks.Stairs.entries.forEach { stair: RagiumBlocks.Stairs ->
            registerStair(exporter, stair, stair.baseStone)
        }
        // polished asphalt
        HTStonecuttingRecipeJsonBuilder.register(
            exporter,
            RagiumBlocks.Stones.ASPHALT,
            RagiumBlocks.Stones.POLISHED_ASPHALT,
            category = RecipeCategory.BUILDING_BLOCKS,
        )
        // polished gypsum
        HTStonecuttingRecipeJsonBuilder.register(
            exporter,
            RagiumBlocks.Stones.GYPSUM,
            RagiumBlocks.Stones.POLISHED_GYPSUM,
            category = RecipeCategory.BUILDING_BLOCKS,
        )
        // polished slate
        HTStonecuttingRecipeJsonBuilder.register(
            exporter,
            RagiumBlocks.Stones.SLATE,
            RagiumBlocks.Stones.POLISHED_SLATE,
            category = RecipeCategory.BUILDING_BLOCKS,
        )
        // white line
        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.WhiteLines.SIMPLE, 12)
            .patterns(
                "A",
                "A",
                "A",
            ).input('A', ConventionalItemTags.WHITE_DYES)
            .unlockedBy(ConventionalItemTags.WHITE_DYES)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.WhiteLines.T_SHAPED, 12)
            .patterns(
                "AAA",
                " A ",
            ).input('A', ConventionalItemTags.WHITE_DYES)
            .unlockedBy(ConventionalItemTags.WHITE_DYES)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.WhiteLines.CROSS, 12)
            .patterns(
                " A ",
                "AAA",
                " A ",
            ).input('A', ConventionalItemTags.WHITE_DYES)
            .unlockedBy(ConventionalItemTags.WHITE_DYES)
            .offerTo(exporter)
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
            .slabPattern()
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
            .stairPattern()
            .input('A', input)
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
            .create(RagiumItemsNew.FORGE_HAMMER)
            .patterns(
                " AA",
                "BBA",
                " AA",
            ).input('A', RagiumItemsNew.Ingots.RAGI_ALLOY)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumItemsNew.Ingots.RAGI_ALLOY)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItemsNew.RAGI_WRENCH)
            .patterns(
                "A A",
                "AAA",
                " A ",
            ).input('A', RagiumItemsNew.Ingots.RAGI_ALLOY)
            .unlockedBy(RagiumItemsNew.Ingots.RAGI_ALLOY)
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
        // patchouli
        HTShapelessRecipeJsonBuilder
            .create(RagiumItemsNew.GUIDE_BOOK)
            .input(Items.BOOK)
            .input(RagiumItemsNew.RawMaterials.CRUDE_RAGINITE)
            .input(ConventionalItemTags.IRON_INGOTS)
            .unlockedBy(RagiumItemsNew.RawMaterials.CRUDE_RAGINITE)
            .offerTo(exporter)
        // dynamites
        mapOf(
            RagiumItemsNew.Dynamites.ANVIL to Items.ANVIL,
            RagiumItemsNew.Dynamites.BLAZING to Items.BLAZE_POWDER,
            RagiumItemsNew.Dynamites.BEDROCK to Items.DIAMOND_PICKAXE,
            RagiumItemsNew.Dynamites.FLATTENING to Items.NETHER_STAR,
            RagiumItemsNew.Dynamites.FROSTING to Items.POWDER_SNOW_BUCKET,
        ).forEach { (dynamite: RagiumItemsNew.Dynamites, input: Item) ->
            HTShapedRecipeJsonBuilder
                .create(dynamite, 8)
                .wrapPattern8()
                .input('A', RagiumItemsNew.Dynamites.SIMPLE)
                .input('B', input)
                .unlockedBy(RagiumItemsNew.Dynamites.SIMPLE)
                .offerTo(exporter)
        }
        // filter
        HTShapelessRecipeJsonBuilder
            .create(RagiumItemsNew.FLUID_FILTER)
            .input(Items.PAPER)
            .input(ConventionalItemTags.LIGHT_BLUE_DYES)
            .input(RagiumItemsNew.Dusts.RAGINITE)
            .unlockedBy(Items.PAPER)
            .offerTo(exporter)

        HTShapelessRecipeJsonBuilder
            .create(RagiumItemsNew.ITEM_FILTER)
            .input(Items.PAPER)
            .input(ConventionalItemTags.ORANGE_DYES)
            .input(RagiumItemsNew.Dusts.RAGINITE)
            .unlockedBy(Items.PAPER)
            .offerTo(exporter)
        // steel
        RagiumItemsNew.SteelTools.entries.forEach { tool: RagiumItemsNew.SteelTools ->
            HTShapedRecipeJsonBuilder
                .create(tool)
                .patterns(tool.toolType.getShapedPattern())
                .input('A', RagiumItemsNew.Ingots.STEEL)
                .input('B', ConventionalItemTags.WOODEN_RODS)
                .unlockedBy(RagiumItemsNew.Ingots.STEEL)
                .offerTo(exporter)
        }
        // deep steel
        RagiumItemsNew.DeepSteelTools.entries.forEach { tool: RagiumItemsNew.DeepSteelTools ->
            HTShapedRecipeJsonBuilder
                .create(tool)
                .patterns(tool.toolType.getShapedPattern())
                .input('A', RagiumItemsNew.Ingots.DEEP_STEEL)
                .input('B', ConventionalItemTags.WOODEN_RODS)
                .unlockedBy(RagiumItemsNew.Ingots.DEEP_STEEL)
                .offerTo(exporter)
        }
        // stella
        HTShapedRecipeJsonBuilder
            .create(RagiumItemsNew.STELLA_SABER)
            .patterns(
                "B",
                "A",
                "A",
            ).input('A', RagiumItems.STELLA_PLATE)
            .input('B', RagiumBlocks.SHAFT)
            .unlockedBy(RagiumItems.STELLA_PLATE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItemsNew.RAGIUM_SABER)
            .patterns(
                "  A",
                " A ",
                "B  ",
            ).input('A', RagiumItemsNew.Gems.RAGIUM)
            .input('B', RagiumItemsNew.STELLA_SABER)
            .unlockedBy(RagiumItemsNew.Gems.RAGIUM)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItemsNew.GIGANT_HAMMER)
            .patterns(
                "ABB",
                "AC ",
                " C ",
            ).input('A', RagiumBlocks.StorageBlocks.DEEP_STEEL)
            .input('B', RagiumItemsNew.Gems.RAGIUM)
            .input('C', RagiumBlocks.SHAFT)
            .unlockedBy(RagiumItemsNew.Gems.RAGIUM)
            .offerTo(exporter)
        // backpack
        HTShapedRecipeJsonBuilder
            .create(RagiumItemsNew.BACKPACK)
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
            .input(RagiumItemsNew.BACKPACK)
            .input(createTagKey("dyes/${color.asString()}"))
            .unlockedBy(RagiumItemsNew.BACKPACK)
            .offerPrefix(exporter, "dyed_${color.asString()}_")
    }

    private fun createTagKey(path: String): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier.of(TagUtil.C_TAG_NAMESPACE, path))

    //    Crafting - Foods    //

    private fun fluidIngredient(content: HTFluidContent): Ingredient = fluidIngredient(content.get())

    private fun fluidIngredient(fluid: Fluid): Ingredient = DefaultCustomIngredients.components(
        Ingredient.ofItems(RagiumItemsNew.FILLED_FLUID_CUBE),
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
            ).input('A', fluidIngredient(RagiumFluids.MILK))
            .input('B', Items.SWEET_BERRIES)
            .input('C', RagiumItemsNew.CHOCOLATE)
            .input('D', Items.EGG)
            .input('E', RagiumBlocks.SPONGE_CAKE)
            .unlockedBy(RagiumBlocks.SPONGE_CAKE)
            .offerTo(exporter)

        HTShapelessRecipeJsonBuilder
            .create(RagiumItemsNew.SWEET_BERRIES_CAKE_PIECE, 8)
            .input(RagiumBlocks.SWEET_BERRIES_CAKE)
            .unlockedBy(RagiumItemsNew.SWEET_BERRIES_CAKE_PIECE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.SWEET_BERRIES_CAKE)
            .hollowPattern()
            .input('A', RagiumItemsNew.SWEET_BERRIES_CAKE_PIECE)
            .unlockedBy(RagiumItemsNew.SWEET_BERRIES_CAKE_PIECE)
            .offerSuffix(exporter, "_from_piece")
        // yellow cake
        HTShapelessRecipeJsonBuilder
            .create(RagiumItems.YELLOW_CAKE_PIECE, 8)
            .input(RagiumItems.YELLOW_CAKE)
            .unlockedBy(RagiumItems.YELLOW_CAKE_PIECE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.YELLOW_CAKE)
            .hollowPattern()
            .input('A', RagiumItems.YELLOW_CAKE_PIECE)
            .unlockedBy(RagiumItems.YELLOW_CAKE_PIECE)
            .offerSuffix(exporter, "_from_piece")

        HTShapelessRecipeJsonBuilder
            .create(RagiumItemsNew.MELON_PIE)
            .input(Items.MELON)
            .input(Items.SUGAR)
            .input(Items.EGG)
            .unlockedBy(Items.MELON)
            .offerTo(exporter)
        // chocolate
        HTShapelessRecipeJsonBuilder
            .create(RagiumItemsNew.CHOCOLATE_APPLE)
            .input(Items.APPLE)
            .input(fluidIngredient(RagiumFluids.CHOCOLATE))
            .unlockedBy(Items.APPLE)
            .offerTo(exporter)

        HTShapelessRecipeJsonBuilder
            .create(RagiumItemsNew.CHOCOLATE_BREAD)
            .input(Items.BREAD)
            .input(fluidIngredient(RagiumFluids.CHOCOLATE))
            .unlockedBy(Items.BREAD)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItemsNew.CHOCOLATE_COOKIE, 8)
            .wrapPattern8()
            .input('A', Items.COOKIE)
            .input('B', RagiumItemsNew.CHOCOLATE)
            .unlockedBy(Items.COOKIE)
            .offerTo(exporter)
    }

    //    Crafting - Misc    //

    private fun craftingIngredients(exporter: RecipeExporter) {
        HTShapedRecipeJsonBuilder
            .create(RagiumItems.RAGI_ALLOY_COMPOUND)
            .group("ragi_alloy_compound")
            .wrapPattern8()
            .input('A', RagiumItemsNew.RawMaterials.CRUDE_RAGINITE)
            .input('B', ConventionalItemTags.COPPER_INGOTS)
            .unlockedBy(RagiumItemsNew.RawMaterials.RAGINITE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumItems.RAGI_ALLOY_COMPOUND)
            .group("ragi_alloy_compound")
            .patterns(
                " A ",
                "ABA",
                " A ",
            ).input('A', RagiumItemsNew.Dusts.CRUDE_RAGINITE)
            .input('B', ConventionalItemTags.COPPER_INGOTS)
            .unlockedBy(RagiumItemsNew.Dusts.CRUDE_RAGINITE)
            .offerSuffix(exporter, "_1")

        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.SHAFT, 6)
            .patterns(
                "A",
                "A",
            ).input('A', ConventionalItemTags.STORAGE_BLOCKS_IRON)
            .unlockedBy(ConventionalItemTags.STORAGE_BLOCKS_IRON)
            .offerTo(exporter)
        // fluid cubes
        createEmptyFluidCube(exporter, Items.GLASS_PANE, 4)
        createEmptyFluidCube(exporter, RagiumItems.PLASTIC_PLATE, 8, "_pe")
        createEmptyFluidCube(exporter, RagiumItems.ENGINEERING_PLASTIC_PLATE, 16, "_pvc")

        HTShapelessRecipeJsonBuilder
            .create(RagiumItemsNew.EMPTY_FLUID_CUBE)
            .input(RagiumItemsNew.EMPTY_FLUID_CUBE)
            .unlockedBy(RagiumItemsNew.EMPTY_FLUID_CUBE)
            .offerTo(exporter, RagiumAPI.id("clear_empty_fluid_cube"))

        HTShapelessRecipeJsonBuilder
            .create(RagiumItemsNew.EMPTY_FLUID_CUBE)
            .input(RagiumItemsNew.FILLED_FLUID_CUBE)
            .unlockedBy(RagiumItemsNew.FILLED_FLUID_CUBE)
            .offerTo(exporter, RagiumAPI.id("clear_filled_fluid_cube"))
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
            .create(RagiumItemsNew.EMPTY_FLUID_CUBE, count)
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
            .catalyst(RagiumItemsNew.EMPTY_FLUID_CUBE)
            .itemOutput(RagiumItemsNew.EMPTY_FLUID_CUBE, count)
            .offerTo(exporter, id)
    }

    //    Cooking   //

    private fun cookingRecipes(exporter: RecipeExporter) {
        HTCookingRecipeJsonBuilder.smeltAndBlast(
            exporter,
            RagiumItems.RAGI_ALLOY_COMPOUND,
            RagiumItemsNew.Ingots.RAGI_ALLOY,
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
            RagiumItemsNew.Dusts.QUARTZ.prefixedTagKey,
            RagiumItems.CRUDE_SILICON,
        )

        HTCookingRecipeJsonBuilder.smeltAndSmoke(
            exporter,
            RagiumItemsNew.DOUGH,
            Items.BREAD,
        )
        HTCookingRecipeJsonBuilder.smeltAndSmoke(
            exporter,
            RagiumItemsNew.MEAT_INGOT,
            RagiumItemsNew.COOKED_MEAT_INGOT,
        )
    }
}
