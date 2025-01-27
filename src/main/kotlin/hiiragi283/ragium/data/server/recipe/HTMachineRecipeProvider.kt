package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.condition.HTTierCondition
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.fluids.FluidType
import java.util.function.Supplier
import kotlin.math.pow

object HTMachineRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        chemicalReactor(output)
        compressor(output)
        cokeOven(output)
    }

    //    Chemical Reactor    //

    fun chemicalReactor(output: RecipeOutput) {
        fun register(tier: HTMachineTier, builder: Supplier<HTMachineRecipeBuilder>) {
            val count: Int = 2.0.pow(tier.ordinal).toInt()
            builder
                .get()
                .itemOutput(RagiumItems.PLASTIC_PLATE, count)
                .machineConditions(HTTierCondition(tier))
                .savePrefixed(output, "${tier.serializedName}_")
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

    //    Coke Oven    //

    private fun cokeOven(output: RecipeOutput) {
        // Log -> Charcoal + Creosote
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.COKE_OVEN)
            .itemInput(ItemTags.LOGS_THAT_BURN)
            .itemOutput(Items.CHARCOAL)
            .fluidOutput(RagiumFluids.CREOSOTE, 200)
            .saveSuffixed(output, "_from_logs")
        // Planks -> Carbon Dust + Creosote
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.COKE_OVEN)
            .itemInput(ItemTags.PLANKS)
            .itemOutput(RagiumItems.Dusts.CARBON)
            .fluidOutput(RagiumFluids.CREOSOTE, 50)
            .save(output)

        // Coal -> Coke + Creosote
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.COKE_OVEN)
            .itemInput(Items.COAL)
            .itemOutput(RagiumItems.COKE)
            .fluidOutput(RagiumFluids.CREOSOTE, 500)
            .save(output)

        // Creosote -> Aromatic Compound
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.CREOSOTE, FluidType.BUCKET_VOLUME * 4)
            .fluidOutput(RagiumFluids.AROMATIC_COMPOUNDS, FluidType.BUCKET_VOLUME * 3)
            .fluidOutput(RagiumFluids.ALCOHOL)
            .saveSuffixed(output, "_from_creosote")
    }
}
