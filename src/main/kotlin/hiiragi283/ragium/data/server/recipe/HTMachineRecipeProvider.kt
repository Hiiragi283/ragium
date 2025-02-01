package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.extension.biome
import hiiragi283.ragium.api.extension.catalyst
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumRecipes
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes
import net.neoforged.neoforge.fluids.FluidType
import java.util.function.Supplier
import kotlin.math.pow

object HTMachineRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        chemicalReactor(output)
        compressor(output)
        resourcePlant(output, holderLookup.lookupOrThrow(Registries.BIOME))
    }

    //    Chemical Reactor    //

    fun chemicalReactor(output: RecipeOutput) {
        fun register(tier: HTMachineTier, builder: Supplier<HTMachineRecipeBuilder>) {
            val count: Int = 2.0.pow(tier.ordinal).toInt()
            builder
                .get()
                .itemOutput(RagiumItems.PLASTIC_PLATE, count)
                .catalyst(tier)
                .savePrefixed(output, "${tier.serializedName}_")
        }

        register(HTMachineTier.BASIC) {
            HTMachineRecipeBuilder
                .create(RagiumRecipes.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.ETHENE)
        }
        register(HTMachineTier.ADVANCED) {
            HTMachineRecipeBuilder
                .create(RagiumRecipes.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.ACETYLENE)
                .fluidInput(RagiumFluids.CHLORINE)
        }
        register(HTMachineTier.ELITE) {
            HTMachineRecipeBuilder
                .create(RagiumRecipes.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.PROPENE)
        }
        register(HTMachineTier.ULTIMATE) {
            HTMachineRecipeBuilder
                .create(RagiumRecipes.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.AROMATIC_COMPOUNDS)
                .fluidInput(RagiumFluids.NITRIC_ACID)
        }
    }

    //    Compressor    //

    private fun compressor(output: RecipeOutput) {
        // Circuit Board
        HTMachineRecipeBuilder
            .create(RagiumRecipes.COMPRESSOR)
            .itemInput(RagiumItemTags.PLASTICS)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.QUARTZ)
            .catalyst(RagiumItems.PLATE_PRESS_MOLD)
            .itemOutput(RagiumItems.CIRCUIT_BOARD)
            .save(output)
    }

    //    Resource Plant    //

    private fun resourcePlant(output: RecipeOutput, lookup: HolderLookup.RegistryLookup<Biome>) {
        // Brine from Ocean
        HTMachineRecipeBuilder
            .create(RagiumRecipes.RESOURCE_PLANT)
            .biome(BiomeTags.IS_OCEAN, lookup)
            .fluidOutput(RagiumFluids.BRINE, FluidType.BUCKET_VOLUME / 4)
            .saveSuffixed(output, "_from_ocean")
        // Brine from Beach
        HTMachineRecipeBuilder
            .create(RagiumRecipes.RESOURCE_PLANT)
            .biome(BiomeTags.IS_BEACH, lookup)
            .fluidOutput(RagiumFluids.BRINE, FluidType.BUCKET_VOLUME / 4)
            .saveSuffixed(output, "_from_beach")

        // Oil from Nether
        HTMachineRecipeBuilder
            .create(RagiumRecipes.RESOURCE_PLANT)
            .biome(Biomes.SOUL_SAND_VALLEY, lookup)
            .fluidOutput(RagiumFluids.CRUDE_OIL, FluidType.BUCKET_VOLUME / 4)
            .save(output)
    }
}
