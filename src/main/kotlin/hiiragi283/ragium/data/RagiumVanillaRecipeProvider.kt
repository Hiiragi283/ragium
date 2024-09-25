package hiiragi283.ragium.data

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
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

    private fun <T : CraftingRecipeJsonBuilder> T.itemCriterion(item: ItemConvertible): T = apply {
        criterion("has_input", RecipeProvider.conditionsFromItem(item))
    }

    private fun <T : CraftingRecipeJsonBuilder> T.tagCriterion(tagKey: TagKey<Item>): T = apply {
        criterion("has_input", RecipeProvider.conditionsFromTag(tagKey))
    }

    private fun RecipeExporter.conditions(vararg conditions: ResourceCondition): RecipeExporter = withConditions(this, *conditions)

    private fun RecipeExporter.hardMode(isHard: Boolean): RecipeExporter = conditions(HTHardModeResourceCondition.fromBool(isHard))

    //    Crafting    //

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

        createShapeless(Items.STICKY_PISTON)
            .input(ConventionalItemTags.SLIME_BALLS)
            .input(Items.PISTON)
            .itemCriterion(Items.PISTON)
            .offerTo(exporter, Ragium.id("shapeless/sticky_piston"))

        createShaped(Items.LEAD, 2)
            .pattern("AA ")
            .pattern("AB ")
            .pattern("  A")
            .input('A', ConventionalItemTags.STRINGS)
            .input('B', ConventionalItemTags.SLIME_BALLS)
            .tagCriterion(ConventionalItemTags.SLIME_BALLS)
            .offerTo(exporter, Ragium.id("shapeless/lead"))

        createShapeless(RagiumContents.CABLE)
            .input(ItemTags.WOOL_CARPETS)
            .input(ConventionalItemTags.COPPER_INGOTS)
            .input(ConventionalItemTags.COPPER_INGOTS)
            .input(ConventionalItemTags.COPPER_INGOTS)
            .tagCriterion(ConventionalItemTags.COPPER_INGOTS)
            .offerTo(exporter, Ragium.id("shapeless/cable"))

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
            val base: Block = material.tier.baseBlock
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
            .input('B', HTMachineTier.BASIC.baseBlock)
            .input('C', RagiumContents.SHAFT)
            .input('D', ConventionalItemTags.REDSTONE_DUSTS)
            .itemCriterion(RagiumContents.SHAFT)
            .offerTo(exporter, Ragium.id("shaped/gear_box"))

        // tiered machines
        // tier1
        createShaped(RagiumContents.BRICK_ALLOY_FURNACE)
            .pattern("AAA")
            .pattern("BCB")
            .pattern("DDD")
            .input('A', RagiumContents.Ingots.RAGI_ALLOY)
            .input('B', Items.FURNACE)
            .input('C', RagiumContents.Hulls.RAGI_ALLOY)
            .input('D', Items.BRICKS)
            .itemCriterion(RagiumContents.Hulls.RAGI_ALLOY)
            .offerTo(exporter, Ragium.id("shaped/brick_alloy_furnace"))

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
            .input('D', type.tier.baseBlock)
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
}
