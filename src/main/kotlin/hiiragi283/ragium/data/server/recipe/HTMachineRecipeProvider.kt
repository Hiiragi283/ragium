package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import java.util.function.Supplier
import kotlin.math.pow

object HTMachineRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        chemicalReactor(output)
        compressor(output)
    }

    //    Chemical Reactor    //

    fun chemicalReactor(output: RecipeOutput) {
        fun register(tier: HTMachineTier, builder: Supplier<HTMachineRecipeBuilder>) {
            val count: Int = 2.0.pow(tier.ordinal).toInt()
            builder
                .get()
                .itemOutput(RagiumItems.PLASTIC_PLATE, count)
                .savePrefixed(output, "${tier.serializedName}_")
            builder
                .get()
                .catalyst(RagiumItems.OXIDIZATION_CATALYST)
                .itemOutput(RagiumItems.PLASTIC_PLATE, count * 2)
                .save(output, "${tier.serializedName}_plastic_plate_alt")
        }

        register(HTMachineTier.BASIC) {
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.ETHENE)
        }
        register(HTMachineTier.ADVANCED) {
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.ACETYLENE)
                .fluidInput(RagiumFluids.CHLORINE)
        }
        register(HTMachineTier.ELITE) {
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.PROPENE)
        }
        register(HTMachineTier.ULTIMATE) {
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.AROMATIC_COMPOUNDS)
                .fluidInput(RagiumFluids.NITRIC_ACID)
        }
    }

    //    Compressor    //

    private fun compressor(output: RecipeOutput) {
        // Circuit Board
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.COMPRESSOR)
            .itemInput(RagiumItemTags.PLASTIC_PLATES)
            .itemInput(RagiumItems.Dusts.QUARTZ)
            .catalyst(RagiumItems.PLATE_PRESS_MOLD)
            .itemOutput(RagiumItems.CIRCUIT_BOARD)
            .save(output)
    }
}
