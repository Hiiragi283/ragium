package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier

object HTMachineRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        chemicalReactor(output)
        compressor(output)
    }

    //    Chemical Reactor    //

    fun chemicalReactor(output: RecipeOutput) {
        fun register(result: ItemLike, builder: Supplier<HTMachineRecipeBuilder>) {
            builder
                .get()
                .itemOutput(result)
                .save(output)
            builder
                .get()
                .catalyst(RagiumItems.OXIDIZATION_CATALYST)
                .itemOutput(result, 2)
                .saveSuffixed(output, "_alt")
        }

        register(RagiumItems.Plastics.BASIC) {
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.ETHENE)
        }
        register(RagiumItems.Plastics.ADVANCED) {
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.ACETYLENE)
                .fluidInput(RagiumFluids.CHLORINE)
        }
        register(RagiumItems.Plastics.ELITE) {
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.PROPENE)
        }
        register(RagiumItems.Plastics.ULTIMATE) {
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.AROMATIC_COMPOUNDS)
                .fluidInput(RagiumFluids.NITRIC_ACID)
        }
    }

    //    Compressor    //

    private fun compressor(output: RecipeOutput) {
        // Circuit Board
        RagiumItems.Plastics.entries.forEach { plastic: RagiumItems.Plastics ->
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.COMPRESSOR)
                .itemInput(plastic)
                .itemInput(RagiumItems.Dusts.QUARTZ)
                .catalyst(RagiumItems.PLATE_PRESS_MOLD)
                .itemOutput(RagiumItems.CIRCUIT_BOARD, plastic.ordinal + 1)
                .savePrefixed(output, "${plastic.machineTier.serializedName}_")
        }
    }
}
