package hiiragi283.ragium.data

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import hiiragi283.ragium.common.data.HTShapedRecipeJsonBuilder
import hiiragi283.ragium.common.init.RagiumMaterials
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.tags.RagiumItemTags
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
import java.util.concurrent.CompletableFuture

class RagiumVanillaRecipeProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricRecipeProvider(output, registriesFuture) {
    override fun getName(): String = "Recipes/Vanilla"

    override fun generate(exporter: RecipeExporter) {
        craftingRecipes(exporter)
        cookingRecipes(exporter)
    }

    private fun <T : CraftingRecipeJsonBuilder> T.unlockedBy(item: ItemConvertible): T = apply {
        criterion("has_the_input", RecipeProvider.conditionsFromItem(item))
    }

    private fun <T : CraftingRecipeJsonBuilder> T.unlockedBy(tagKey: TagKey<Item>): T = apply {
        criterion("has_the_input", RecipeProvider.conditionsFromTag(tagKey))
    }

    private fun RecipeExporter.conditions(vararg conditions: ResourceCondition): RecipeExporter = withConditions(this, *conditions)

    private fun RecipeExporter.hardMode(isHard: Boolean): RecipeExporter = conditions(HTHardModeResourceCondition.fromBool(isHard))

    //    Crafting    //

    private fun craftingRecipes(exporter: RecipeExporter) {
        // ingredients
        HTShapedRecipeJsonBuilder
            .create(RagiumContents.RAGI_ALLOY_COMPOUND)
            .group("ragi_alloy_compound")
            .patterns(
                "AAA",
                "ABA",
                "AAA",
            ).input('A', RagiumContents.RAW_RAGINITE)
            .input('B', ConventionalItemTags.COPPER_INGOTS)
            .unlockedBy(RagiumContents.RAW_RAGINITE)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.RAGI_ALLOY_COMPOUND)
            .group("ragi_alloy_compound")
            .patterns(
                " A ",
                "ABA",
                " A ",
            ).input('A', RagiumContents.Dusts.RAW_RAGINITE)
            .input('B', ConventionalItemTags.COPPER_INGOTS)
            .unlockedBy(RagiumContents.Dusts.RAW_RAGINITE)
            .offerSuffix(exporter.hardMode(false), "_1")

        createEmptyFluidCube(exporter, Items.GLASS_PANE, 4)
        createEmptyFluidCube(exporter, RagiumContents.Plates.PE, 8, "_pe")
        createEmptyFluidCube(exporter, RagiumContents.Plates.PVC, 16, "_pvc")
        createEmptyFluidCube(exporter, RagiumContents.Plates.PTFE, 32, "_ptfe")

        HTShapedRecipeJsonBuilder
            .create(Items.NETHER_STAR)
            .patterns(
                " A ",
                "DEB",
                " C ",
            ).input('A', RagiElement.RAGIUM.dustItem)
            .input('B', RagiElement.RIGIUM.dustItem)
            .input('C', RagiElement.RUGIUM.dustItem)
            .input('D', RagiElement.REGIUM.dustItem)
            .input('E', RagiElement.ROGIUM.dustItem)
            .unlockedBy(Items.NETHER_STAR)
            .offerTo(exporter)

        createShapeless(Items.STICKY_PISTON)
            .input(ConventionalItemTags.SLIME_BALLS)
            .input(Items.PISTON)
            .unlockedBy(Items.PISTON)
            .offerTo(exporter, Ragium.id("shapeless/sticky_piston"))

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

        createShapeless(RagiumContents.CABLE)
            .input(ItemTags.WOOL_CARPETS)
            .input(ConventionalItemTags.COPPER_INGOTS)
            .input(ConventionalItemTags.COPPER_INGOTS)
            .input(ConventionalItemTags.COPPER_INGOTS)
            .unlockedBy(ConventionalItemTags.COPPER_INGOTS)
            .offerTo(exporter, Ragium.id("shapeless/cable"))

        // tools
        HTShapedRecipeJsonBuilder
            .create(RagiumContents.FORGE_HAMMER)
            .patterns(
                " AA",
                "BBA",
                " AA",
            ).input('A', RagiumContents.Ingots.RAGI_ALLOY)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumContents.Ingots.RAGI_ALLOY)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.STEEL_SWORD)
            .patterns(
                "B",
                "A",
                "A",
            ).input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.STEEL_SHOVEL)
            .patterns(
                "B",
                "B",
                "A",
            ).input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.STEEL_PICKAXE)
            .patterns(
                " B ",
                " B ",
                "AAA",
            ).input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.STEEL_AXE)
            .patterns(
                "B ",
                "BA",
                "AA",
            ).input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.STEEL_HOE)
            .patterns(
                "B ",
                "B ",
                "AA",
            ).input('A', RagiumItemTags.STEEL_INGOTS)
            .input('B', ConventionalItemTags.WOODEN_RODS)
            .unlockedBy(RagiumItemTags.STEEL_INGOTS)
            .offerTo(exporter)

        // hulls
        val materials: List<RagiumMaterials> = listOf(
            RagiumMaterials.RAGI_ALLOY,
            RagiumMaterials.RAGI_STEEL,
            RagiumMaterials.REFINED_RAGI_STEEL,
        )

        materials.forEach { material: RagiumMaterials ->
            val base: Block = material.tier.baseBlock
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
        // machines
        HTShapedRecipeJsonBuilder
            .create(RagiumContents.MANUAL_GRINDER)
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
            .create(RagiumContents.MANUAL_GRINDER)
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
            .create(RagiumContents.BURNING_BOX)
            .patterns(
                "AAA",
                "A A",
                "ABA",
            ).input('A', Items.BRICKS)
            .input('B', Items.FURNACE)
            .unlockedBy(Items.BRICKS)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumContents.GEAR_BOX)
            .patterns(
                "AAA",
                "BCB",
                "BDB",
            ).input('A', RagiumContents.Ingots.RAGI_STEEL)
            .input('B', HTMachineTier.BASIC.baseBlock)
            .input('C', RagiumContents.SHAFT)
            .input('D', ConventionalItemTags.REDSTONE_DUSTS)
            .unlockedBy(RagiumContents.SHAFT)
            .offerTo(exporter)

        // tiered machines
        // tier1
        HTShapedRecipeJsonBuilder
            .create(RagiumContents.BRICK_ALLOY_FURNACE)
            .patterns(
                "AAA",
                "BCB",
                "DDD",
            ).input('A', RagiumContents.Ingots.RAGI_ALLOY)
            .input('B', Items.FURNACE)
            .input('C', RagiumContents.Hulls.RAGI_ALLOY)
            .input('D', Items.BRICKS)
            .unlockedBy(RagiumContents.Hulls.RAGI_ALLOY)
            .offerTo(exporter)

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
            HTMachineType.Single.ALLOY_FURNACE,
            RagiumContents.Ingots.RAGI_ALLOY,
            Items.FURNACE,
            RagiumContents.Hulls.RAGI_ALLOY,
        )

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
        HTShapedRecipeJsonBuilder
            .create(RagiumContents.ALCHEMICAL_INFUSER)
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
    }

    private fun createShapeless(output: ItemConvertible): ShapelessRecipeJsonBuilder =
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, output)

    private fun createEmptyFluidCube(
        exporter: RecipeExporter,
        input: ItemConvertible,
        count: Int,
        suffix: String? = null,
    ) {
        HTShapedRecipeJsonBuilder
            .create(RagiumContents.EMPTY_FLUID_CUBE, count)
            .patterns(
                " A ",
                "A A",
                " A ",
            ).input('A', input)
            .unlockedBy(input)
            .offerTo(exporter, Ragium.id("empty_fluid_cube$suffix"))
    }

    private fun createMachine(
        exporter: RecipeExporter,
        type: HTMachineType,
        ingot: ItemConvertible,
        side: ItemConvertible,
        hull: ItemConvertible,
    ) {
        HTShapedRecipeJsonBuilder
            .create(type)
            .patterns(
                "AAA",
                "BCB",
                "DDD",
            ).input('A', ingot)
            .input('B', side)
            .input('C', hull)
            .input('D', type.tier.baseBlock)
            .unlockedBy(hull)
            .offerTo(exporter, type.id)
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
            ).unlockedBy(RagiumContents.RAGI_ALLOY_COMPOUND)
            .offerTo(exporter, Ragium.id("smelting/ragi_alloy_ingot_alt"))
    }
}
