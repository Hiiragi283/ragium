package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.data.savePrefixed
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object HTMachineRecipeProvider : RecipeProviderChild {
    override fun buildRecipes(output: RecipeOutput) {
        // Circuits
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
}
