package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
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
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object HTIngredientRecipeProvider : RecipeProviderChild {
    override fun buildRecipes(output: RecipeOutput) {
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
    }

    private fun registerCircuits(output: RecipeOutput) {
        RagiumItems.Circuits.entries.forEach { circuit: RagiumItems.Circuits ->
            // Assembler
            val silicon: ItemLike = when (circuit) {
                RagiumItems.Circuits.SIMPLE -> RagiumItems.CRUDE_SILICON
                RagiumItems.Circuits.BASIC -> RagiumItems.SILICON
                RagiumItems.Circuits.ADVANCED -> RagiumItems.REFINED_SILICON
                RagiumItems.Circuits.ELITE -> RagiumItems.STELLA_PLATE
            }
            val dust: ItemLike = when (circuit) {
                RagiumItems.Circuits.SIMPLE -> Items.REDSTONE
                RagiumItems.Circuits.BASIC -> Items.GLOWSTONE_DUST
                RagiumItems.Circuits.ADVANCED -> RagiumItems.LUMINESCENCE_DUST
                RagiumItems.Circuits.ELITE -> RagiumItems.Dusts.RAGI_CRYSTAL
            }

            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.ASSEMBLER, circuit.machineTier)
                .itemInput(silicon)
                .itemInput(HTTagPrefix.INGOT, circuit.machineTier.getSubMetal())
                .itemInput(dust)
                .itemOutput(circuit)
                .save(output)
        }

        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.Circuits.SIMPLE)
            .pattern(" A ")
            .pattern("BCB")
            .pattern(" A ")
            .define('A', Tags.Items.INGOTS_COPPER)
            .define('B', Tags.Items.DUSTS_REDSTONE)
            .define('C', ItemTags.PLANKS)
            .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
            .savePrefixed(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.Circuits.BASIC)
            .pattern("ABA")
            .pattern("CDC")
            .pattern("ABA")
            .define('A', Tags.Items.GEMS_LAPIS)
            .define('B', Tags.Items.DUSTS_REDSTONE)
            .define('C', Tags.Items.DUSTS_GLOWSTONE)
            .define('D', RagiumItems.Circuits.SIMPLE)
            .unlockedBy("has_circuit", has(RagiumItems.Circuits.SIMPLE))
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
