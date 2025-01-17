package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import hiiragi283.ragium.data.define
import hiiragi283.ragium.data.savePrefixed
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType
import java.util.function.Supplier

object HTIngredientRecipeProvider : RecipeProviderChild {
    override fun buildRecipes(output: RecipeOutput) {
        registerRaginite(output)
        registerSteels(output)

        registerPlastics(output)
        registerCircuits(output)

        registerCatalysts(output)
        registerPressMolds(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.TOOLS, RagiumItems.FORGE_HAMMER)
            .pattern(" AA")
            .pattern("BBA")
            .pattern(" AA")
            .define('A', HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            .define('B', Tags.Items.RODS_WOODEN)
            .unlockedBy("has_ragi_alloy", has(HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_ALLOY))
            .savePrefixed(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.SLOT_LOCK, 3)
            .pattern("AAA")
            .pattern("BBB")
            .pattern("AAA")
            .define('A', HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            .define('B', Tags.Items.DYES_WHITE)
            .unlockedBy("has_ragi_alloy", has(HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_ALLOY))
            .savePrefixed(output)
    }

    private fun registerRaginite(output: RecipeOutput) {
        // Ragi-Alloy
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.RAGI_ALLOY_COMPOUND)
            .pattern("AAA")
            .pattern("ABA")
            .pattern("AAA")
            .define('A', HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.CRUDE_RAGINITE)
            .define('B', HTTagPrefix.INGOT, RagiumMaterialKeys.COPPER)
            .unlockedBy("has_crude_raginite", has(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.CRUDE_RAGINITE))
            .savePrefixed(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.RAGI_ALLOY_COMPOUND)
            .pattern(" A ")
            .pattern("ABA")
            .pattern(" A ")
            .define('A', HTTagPrefix.DUST, RagiumMaterialKeys.CRUDE_RAGINITE)
            .define('B', HTTagPrefix.INGOT, RagiumMaterialKeys.COPPER)
            .unlockedBy("has_crude_raginite", has(HTTagPrefix.DUST, RagiumMaterialKeys.CRUDE_RAGINITE))
            .save(output, RagiumAPI.id("shaped/ragi_alloy_compound_alt"))

        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.RAGI_ALLOY_COMPOUND),
                RagiumItems.Ingots.RAGI_ALLOY,
                exp = 0.5f,
                types = setOf(HTCookingRecipeBuilder.Type.SMELTING, HTCookingRecipeBuilder.Type.BLASTING),
            ).unlockedBy("has_compound", has(RagiumItems.RAGI_ALLOY_COMPOUND))
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterialKeys.COPPER)
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.RAGINITE)
            .itemOutput(RagiumItems.Ingots.RAGI_ALLOY)
            .save(output)
        // Ragi-Steel
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterialKeys.IRON)
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.RAGINITE, 4)
            .itemOutput(RagiumItems.Ingots.RAGI_STEEL)
            .save(output)
        // Refined Ragi-Steel
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.ADVANCED)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterialKeys.STEEL)
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.RAGI_CRYSTAL, 4)
            .itemOutput(RagiumItems.Ingots.REFINED_RAGI_STEEL)
            .save(output)
        // Ragium
    }

    private fun registerSteels(output: RecipeOutput) {
        // Steel
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterialKeys.IRON)
            .itemInput(ItemTags.COALS, 4)
            .itemOutput(RagiumItems.Ingots.STEEL)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.ADVANCED)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterialKeys.IRON)
            .itemInput(ItemTags.COALS, 2)
            .catalyst(RagiumItems.REDUCTION_CATALYST)
            .itemOutput(RagiumItems.Ingots.STEEL)
            .saveSuffixed(output, "_alt")
        // Deep Steel
    }

    private fun registerPlastics(output: RecipeOutput) {
        fun register(result: ItemLike, builder: Supplier<HTMachineRecipeBuilder>) {
            builder
                .get()
                .itemInput(RagiumItems.POLYMER_RESIN)
                .itemOutput(result)
                .save(output)
            builder
                .get()
                .itemInput(RagiumItems.POLYMER_RESIN)
                .catalyst(RagiumItems.OXIDIZATION_CATALYST)
                .itemOutput(result, 2)
                .saveSuffixed(output, "_alt")
        }

        register(RagiumItems.Plastics.BASIC) {
            HTMachineRecipeBuilder.create(RagiumMachineKeys.CHEMICAL_REACTOR)
        }
        register(RagiumItems.Plastics.ADVANCED) {
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.CHLORINE, FluidType.BUCKET_VOLUME / 10)
        }
        register(RagiumItems.Plastics.ELITE) {
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.HYDROGEN_FLUORIDE, FluidType.BUCKET_VOLUME / 5)
        }
        register(RagiumItems.Plastics.ULTIMATE) {
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.AROMATIC_COMPOUNDS, FluidType.BUCKET_VOLUME / 2)
        }

        RagiumItems.Plastics.entries.forEach { plastic: RagiumItems.Plastics ->
            // Circuit Board
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.COMPRESSOR, plastic.machineTier)
                .itemInput(plastic)
                .itemInput(RagiumItems.Dusts.QUARTZ)
                .catalyst(RagiumItems.PLATE_PRESS_MOLD)
                .itemOutput(RagiumItems.CIRCUIT_BOARD, plastic.ordinal + 1)
                .savePrefixed(output, "${plastic.machineTier.serializedName}_")
        }
    }

    private fun registerCircuits(output: RecipeOutput) {
        RagiumItems.Circuits.entries.forEach { circuit: RagiumItems.Circuits ->
            // Assembler
            val dust: ItemLike = when (circuit) {
                RagiumItems.Circuits.BASIC -> Items.REDSTONE
                RagiumItems.Circuits.ADVANCED -> Items.GLOWSTONE_DUST
                RagiumItems.Circuits.ELITE -> RagiumItems.LUMINESCENCE_DUST
                RagiumItems.Circuits.ULTIMATE -> RagiumItems.Dusts.RAGI_CRYSTAL
            }

            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.ASSEMBLER, circuit.machineTier)
                .itemInput(RagiumItems.CIRCUIT_BOARD)
                .itemInput(HTTagPrefix.INGOT, circuit.machineTier.getSubMetal())
                .itemInput(dust)
                .itemOutput(circuit)
                .save(output)
        }

        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.Circuits.BASIC)
            .pattern(" A ")
            .pattern("BCB")
            .pattern(" A ")
            .define('A', Tags.Items.INGOTS_COPPER)
            .define('B', Tags.Items.DUSTS_REDSTONE)
            .define('C', ItemTags.PLANKS)
            .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
            .savePrefixed(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.Circuits.ADVANCED)
            .pattern("ABA")
            .pattern("CDC")
            .pattern("ABA")
            .define('A', Tags.Items.GEMS_LAPIS)
            .define('B', Tags.Items.DUSTS_REDSTONE)
            .define('C', Tags.Items.DUSTS_GLOWSTONE)
            .define('D', RagiumItems.Circuits.BASIC)
            .unlockedBy("has_circuit", has(RagiumItems.Circuits.BASIC))
            .savePrefixed(output)
    }

    private fun registerCatalysts(output: RecipeOutput) {
        fun register(catalyst: ItemLike, corner: HTMaterialKey, edge: ItemLike) {
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, catalyst)
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', HTTagPrefix.STORAGE_BLOCK, corner)
                .define('B', edge)
                .define('C', Items.IRON_BARS)
                .unlockedBy("has_iron_bars", has(Items.IRON_BARS))
                .savePrefixed(output)
        }

        register(RagiumItems.HEATING_CATALYST, RagiumMaterialKeys.COPPER, Items.MAGMA_BLOCK)
        register(RagiumItems.COOLING_CATALYST, RagiumMaterialKeys.ALUMINUM, Items.PACKED_ICE)
        register(RagiumItems.OXIDIZATION_CATALYST, RagiumMaterialKeys.IRON, Items.COAL_BLOCK)
        register(RagiumItems.REDUCTION_CATALYST, RagiumMaterialKeys.GOLD, Items.WATER_BUCKET)
        register(RagiumItems.DEHYDRATION_CATALYST, RagiumMaterialKeys.STEEL, Items.SOUL_SAND)
    }

    private fun registerPressMolds(output: RecipeOutput) {
        fun register(pressMold: ItemLike, prefix: HTTagPrefix) {
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, pressMold)
                .pattern("AA")
                .pattern("AA")
                .pattern("BC")
                .define('A', HTTagPrefix.INGOT, RagiumMaterialKeys.STEEL)
                .define('B', RagiumItems.FORGE_HAMMER)
                .define('C', prefix.commonTagKey)
                .unlockedBy("has_steel", has(HTTagPrefix.INGOT, RagiumMaterialKeys.STEEL))
                .savePrefixed(output)
        }

        register(RagiumItems.GEAR_PRESS_MOLD, HTTagPrefix.GEAR)
        register(RagiumItems.ROD_PRESS_MOLD, HTTagPrefix.ROD)
        register(RagiumItems.PLATE_PRESS_MOLD, HTTagPrefix.PLATE)
    }
}
