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
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.item.HTBackpackItem
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.block.Block
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.RecipeProvider
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
import vazkii.patchouli.common.item.ItemModBook
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

    private fun RecipeExporter.conditions(vararg conditions: ResourceCondition): RecipeExporter = withConditions(this, *conditions)

    private fun RecipeExporter.hardMode(isHard: Boolean): RecipeExporter = conditions(HTHardModeResourceCondition.fromBool(isHard))

    //    Crafting    //

    private fun craftingRecipes(exporter: RecipeExporter) {
        craftingAlternatives(exporter)
        craftingArmors(exporter)
        craftingFluids(exporter)
        craftingFoods(exporter)
        craftingIngredients(exporter)
        craftingMachines(exporter)
        craftingTools(exporter)

        HTShapelessRecipeJsonBuilder
            .create(ItemModBook.forBook(RagiumAPI.id("ragi_wiki")))
            .input(Items.BOOK)
            .input(RagiumContents.RawMaterials.CRUDE_RAGINITE)
            .input(ConventionalItemTags.IRON_INGOTS)
            .offerTo(exporter.conditions(ResourceConditions.allModsLoaded("patchouli")))
    }

    //    Crafting - Armors    //

    private fun craftingArmors(exporter: RecipeExporter) {
        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Armors.STEEL_HELMET)
            .patterns(
                "AAA",
                "A A",
            ).input('A', RagiumItemTags.STEEL_INGOTS)
            .unlockedBy(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Armors.STEEL_CHESTPLATE)
            .patterns(
                "A A",
                "AAA",
                "AAA",
            ).input('A', RagiumItemTags.STEEL_INGOTS)
            .unlockedBy(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Armors.STEEL_LEGGINGS)
            .patterns(
                "AAA",
                "A A",
                "A A",
            ).input('A', RagiumItemTags.STEEL_INGOTS)
            .unlockedBy(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Armors.STEEL_BOOTS)
            .patterns(
                "A A",
                "A A",
            ).input('A', RagiumItemTags.STEEL_INGOTS)
            .unlockedBy(RagiumItemTags.STEEL_INGOTS)
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
            ).input('A', RagiumItemTags.STEEL_BLOCKS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumItemTags.STEEL_BLOCKS)
            .offerTo(exporter)

        // steel
        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Tools.STEEL_SWORD)
            .patterns(
                "B",
                "A",
                "A",
            ).input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Tools.STEEL_SHOVEL)
            .patterns(
                "B",
                "B",
                "A",
            ).input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Tools.STEEL_PICKAXE)
            .patterns(
                " B ",
                " B ",
                "AAA",
            ).input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Tools.STEEL_AXE)
            .patterns(
                "B ",
                "BA",
                "AA",
            ).input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Tools.STEEL_HOE)
            .patterns(
                "B ",
                "B ",
                "AA",
            ).input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumItemTags.STEEL_INGOTS)
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

    private fun craftingFoods(exporter: RecipeExporter) {
        HTShapelessRecipeJsonBuilder
            .create(RagiumContents.Foods.DOUGH, 3)
            .input(RagiumContents.Foods.FLOUR)
            .input(RagiumContents.Foods.FLOUR)
            .input(RagiumContents.Foods.FLOUR)
            .input(RagiumContents.Fluids.WATER)
            .unlockedBy(RagiumContents.Foods.FLOUR)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.SWEET_BERRIES_CAKE)
            .patterns(
                "ABA",
                "CDC",
                "EEE",
            ).input('A', RagiumContents.Fluids.MILK)
            .input('B', Items.SWEET_BERRIES)
            .input('C', RagiumContents.Foods.CHOCOLATE)
            .input('D', Items.EGG)
            .input('E', RagiumBlocks.SPONGE_CAKE)
            .unlockedBy(RagiumBlocks.SPONGE_CAKE)
            .offerTo(exporter)

        HTShapelessRecipeJsonBuilder
            .create(RagiumContents.Foods.CHOCOLATE_BREAD)
            .input(Items.BREAD)
            .input(RagiumContents.Fluids.CHOCOLATE)
            .unlockedBy(RagiumContents.Fluids.CHOCOLATE)
            .offerTo(exporter)

        HTShapelessRecipeJsonBuilder
            .create(RagiumContents.Foods.CHOCOLATE_APPLE)
            .input(Items.APPLE)
            .input(RagiumContents.Fluids.CHOCOLATE)
            .unlockedBy(RagiumContents.Fluids.CHOCOLATE)
            .offerTo(exporter)

        HTShapelessRecipeJsonBuilder
            .create(RagiumContents.Foods.CANDY_APPLE)
            .input(Items.APPLE)
            .input(RagiumContents.Fluids.STARCH_SYRUP)
            .input(ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumContents.Fluids.STARCH_SYRUP)
            .offerTo(exporter)
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
            .offerSuffix(exporter.hardMode(false), "_1")

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Misc.SOLAR_PANEL)
            .patterns(
                "AAA",
                "BBB",
                "CCC",
            ).input('A', ConventionalItemTags.GLASS_PANES)
            .input('B', RagiumItemTags.SILICON_PLATES)
            .input('C', RagiumItemTags.ALUMINUM_PLATES)
            .unlockedBy(RagiumItemTags.SILICON_PLATES)
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
            .input('B', RagiumItemTags.STEEL_PLATES)
            .input('C', RagiumItemTags.GOLD_PLATES)
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
            .input('B', RagiumItemTags.GOLD_PLATES)
            .input('C', RagiumContents.Plates.STELLA)
            .unlockedBy(RagiumContents.Plates.STELLA)
            .offerTo(exporter)
    }

    //    Crafting - Fluids    //

    private fun craftingFluids(exporter: RecipeExporter) {
        createEmptyFluidCube(exporter, Items.GLASS_PANE, 4)
        createEmptyFluidCube(exporter, RagiumContents.Plates.PE, 8, "_pe")
        createEmptyFluidCube(exporter, RagiumContents.Plates.PVC, 16, "_pvc")
        createEmptyFluidCube(exporter, RagiumContents.Plates.PTFE, 32, "_ptfe")

        HTShapelessRecipeJsonBuilder
            .create(RagiumContents.Fluids.WATER)
            .input(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .input(Items.WATER_BUCKET)
            .unlockedBy(Items.WATER_BUCKET)
            .offerTo(exporter)

        HTShapelessRecipeJsonBuilder
            .create(RagiumContents.Fluids.LAVA)
            .input(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .input(Items.LAVA_BUCKET)
            .unlockedBy(Items.LAVA_BUCKET)
            .offerTo(exporter)

        HTShapelessRecipeJsonBuilder
            .create(RagiumContents.Fluids.MILK)
            .input(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .input(Items.MILK_BUCKET)
            .unlockedBy(Items.MILK_BUCKET)
            .offerTo(exporter)

        HTShapelessRecipeJsonBuilder
            .create(RagiumContents.Fluids.HONEY)
            .input(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            .input(Items.HONEY_BOTTLE)
            .input(Items.HONEY_BOTTLE)
            .input(Items.HONEY_BOTTLE)
            .input(Items.HONEY_BOTTLE)
            .unlockedBy(Items.HONEY_BOTTLE)
            .offerTo(exporter)
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
        // hulls
        listOf(
            RagiumMaterials.RAGI_ALLOY,
            RagiumMaterials.RAGI_STEEL,
            RagiumMaterials.REFINED_RAGI_STEEL,
        ).forEach { material: RagiumMaterials ->
            val base: Block = material.tier.getBaseBlock()
            val ingot: RagiumContents.Ingots = material.getIngot() ?: return@forEach
            val hull: RagiumContents.Hulls = material.getHull() ?: return@forEach
            HTShapedRecipeJsonBuilder
                .create(hull)
                .patterns(
                    "AAA",
                    "A A",
                    "BBB",
                ).input('A', ingot)
                .input('B', base)
                .unlockedBy(ingot)
                .offerTo(exporter.hardMode(false))
            val plate: RagiumContents.Plates = material.getPlate() ?: return@forEach
            HTShapedRecipeJsonBuilder
                .create(hull)
                .patterns(
                    "AAA",
                    "A A",
                    "BBB",
                ).input('A', plate)
                .input('B', base)
                .unlockedBy(plate)
                .offerPrefix(exporter.hardMode(true), "hard/")
        }
        // casings
        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.BASIC_CASING, 3)
            .patterns(
                "ABA",
                "BCB",
                "ABA",
            ).input('A', RagiumItemTags.IRON_PLATES)
            .input('B', RagiumItemTags.INVAR_PLATES)
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
            .input('B', RagiumItemTags.STEEL_PLATES)
            .input('C', RagiumContents.Misc.RAGI_CRYSTAL)
            .unlockedBy(RagiumContents.Misc.RAGI_CRYSTAL)
            .offerTo(exporter)
        // machines
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
            .offerTo(exporter.hardMode(false))

        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.MANUAL_GRINDER)
            .patterns(
                "A  ",
                "BBB",
                "CCC",
            ).input('A', ConventionalItemTags.WOODEN_RODS)
            .input('B', RagiumContents.Plates.RAGI_ALLOY)
            .input('C', Items.SMOOTH_STONE)
            .unlockedBy(RagiumContents.Plates.RAGI_ALLOY)
            .offerPrefix(exporter.hardMode(true), "hard/")

        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.ALCHEMICAL_INFUSER)
            .patterns(
                "AAA",
                "BCB",
                "DDD",
            ).input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', Items.DRAGON_BREATH)
            .input('C', Items.NETHER_STAR)
            .input('D', Items.CRYING_OBSIDIAN)
            .unlockedBy(Items.NETHER_STAR)
            .offerTo(exporter)
        // generators
        createMachine(
            exporter,
            RagiumMachineTypes.Generator.THERMAL,
            Items.MAGMA_BLOCK,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Generator.COMBUSTION,
            RagiumContents.Misc.ENGINE,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Generator.SOLAR,
            RagiumContents.Misc.SOLAR_PANEL,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Generator.WATER,
            ItemTags.TRAPDOORS,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.HEAT_GENERATOR,
            Items.FURNACE,
            Items.WATER_BUCKET,
        )
        // processors
        createMachine(
            exporter,
            RagiumMachineTypes.Processor.ALLOY_FURNACE,
            Items.FURNACE,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Processor.ASSEMBLER,
            HTMachineTier::getCircuit,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Processor.CHEMICAL_REACTOR,
            Items.GLASS,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Processor.COMPRESSOR,
            Items.PISTON,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Processor.ELECTROLYZER,
            Items.LIGHTNING_ROD,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Processor.EXTRACTOR,
            Items.HOPPER,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Processor.GRINDER,
            Items.FLINT,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Processor.METAL_FORMER,
            Items.ANVIL,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Processor.MIXER,
            Items.CAULDRON,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Processor.ROCK_GENERATOR,
            Items.LAVA_BUCKET,
            Items.WATER_BUCKET,
        )

        createMachine(
            exporter,
            RagiumMachineTypes.BLAST_FURNACE,
            Items.BLAST_FURNACE,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.DISTILLATION_TOWER,
            RagiumContents.Misc.EMPTY_FLUID_CUBE,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.SAW_MILL,
            Items.STONECUTTER,
        )
    }

    private fun createMachine(
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

    private fun createMachine(
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

    private fun createMachine(
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

            fun createPattern(isHard: Boolean): HTShapedRecipeJsonBuilder = HTShapedRecipeJsonBuilder
                .create(output)
                .patterns(
                    "AAA",
                    "BCD",
                    "EEE",
                ).input('A', getMachineMaterial(tier, isHard))
                .input('B', left(tier))
                .input('C', tier.getHull())
                .input('D', right(tier))
                .input('E', getMachineBase(tier, isHard))
                .unlockedBy(tier.getHull())
            createPattern(true).offerTo(exporter.hardMode(true), tier.createId(type).withPrefixedPath("hard/"))
            createPattern(false).offerTo(exporter.hardMode(false), tier.createId(type))
        }
    }

    private fun getMachineMaterial(tier: HTMachineTier, isHard: Boolean): ItemConvertible = when (isHard) {
        true -> tier.getPlate()
        false -> tier.getIngot()
    }

    private fun getMachineBase(tier: HTMachineTier, isHard: Boolean): TagKey<Item> = when (isHard) {
        true -> when (tier) {
            HTMachineTier.PRIMITIVE -> RagiumItemTags.COPPER_PLATES
            HTMachineTier.BASIC -> RagiumItemTags.IRON_PLATES
            HTMachineTier.ADVANCED -> RagiumItemTags.STEEL_PLATES
        }
        false -> when (tier) {
            HTMachineTier.PRIMITIVE -> ConventionalItemTags.COPPER_INGOTS
            HTMachineTier.BASIC -> ConventionalItemTags.IRON_INGOTS
            HTMachineTier.ADVANCED -> RagiumItemTags.STEEL_INGOTS
        }
    }

    //    Crafting - Alternatives    //

    private fun craftingAlternatives(exporter: RecipeExporter) {
        HTShapedRecipeJsonBuilder
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
            .offerTo(exporter)

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
