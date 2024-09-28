package hiiragi283.ragium.data

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.block.HTMachineBlockBase
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import hiiragi283.ragium.common.data.HTShapedRecipeJsonBuilder
import hiiragi283.ragium.common.init.RagiumMachineTypes
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

        // circuits
        HTShapedRecipeJsonBuilder
            .create(RagiumContents.Circuit.PRIMITIVE)
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
            .create(RagiumContents.Circuit.BASIC)
            .patterns(
                "ABA",
                "CDC",
                "ABA",
            ).input('A', RagiumContents.Dusts.RAGINITE)
            .input('B', RagiumItemTags.STEEL_PLATES)
            .input('C', RagiumItemTags.GOLD_PLATES)
            .input('D', RagiumContents.Circuit.PRIMITIVE)
            .unlockedBy(RagiumContents.Dusts.RAGINITE)
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
            .offerTo(exporter.hardMode(false))

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

        /*HTShapedRecipeJsonBuilder
            .create(RagiumContents.BURNING_BOX)
            .patterns(
                "AAA",
                "A A",
                "ABA",
            ).input('A', Items.BRICKS)
            .input('B', Items.FURNACE)
            .unlockedBy(Items.BRICKS)
            .offerTo(exporter)*/

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
            RagiumMachineTypes.Single.ALLOY_FURNACE,
            Items.BLAST_FURNACE,
        )
        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            val block: HTMachineBlockBase = RagiumMachineTypes.Single.ASSEMBLER.getBlock(tier) ?: return@forEach
            HTShapedRecipeJsonBuilder
                .create(block)
                .patterns(
                    "AAA",
                    "BCB",
                    "DDD",
                ).input('A', tier.getIngot())
                .input('B', tier.getCircuit())
                .input('C', tier.getHull())
                .input('D', tier.baseBlock)
                .unlockedBy(tier.getHull())
                .offerTo(exporter, tier.createId(RagiumMachineTypes.Single.ASSEMBLER))
        }
        // single
        createMachine(
            exporter,
            RagiumMachineTypes.Single.CENTRIFUGE,
            Items.COPPER_GRATE, // TODO
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Single.CHEMICAL_REACTOR,
            Items.GLASS, // TODO
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Single.COMPRESSOR,
            Items.PISTON,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Single.ELECTROLYZER,
            Items.LIGHTNING_ROD,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Single.EXTRACTOR,
            Items.HOPPER,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Single.GRINDER,
            Items.FLINT,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Single.METAL_FORMER,
            Items.ANVIL,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Single.MIXER,
            Items.CAULDRON,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.Single.ROCK_GENERATOR,
            Items.LAVA_BUCKET,
            Items.WATER_BUCKET,
        )
        // multi
        createMachine(
            exporter,
            RagiumMachineTypes.BLAST_FURNACE,
            Items.BLAST_FURNACE,
        )
        createMachine(
            exporter,
            RagiumMachineTypes.DISTILLATION_TOWER,
            RagiumContents.Circuit.ADVANCED,
        )
        // custom
        createMachine(
            exporter,
            RagiumMachineTypes.BURNING_BOX,
            Items.IRON_BARS,
        )

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
        type: HTMachineType<*>,
        left: ItemConvertible,
        right: ItemConvertible = left,
    ) {
        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            val block: HTMachineBlockBase = type.getBlock(tier) ?: return@forEach
            HTShapedRecipeJsonBuilder
                .create(block)
                .patterns(
                    "AAA",
                    "BCD",
                    "EEE",
                ).input('A', tier.getIngot())
                .input('B', left)
                .input('C', tier.getHull())
                .input('D', right)
                .input('E', tier.baseBlock)
                .unlockedBy(tier.getHull())
                .offerTo(exporter, tier.createId(type))
        }
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
