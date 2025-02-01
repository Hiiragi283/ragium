package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.extension.biome
import hiiragi283.ragium.api.extension.catalyst
import hiiragi283.ragium.api.extension.tier
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes
import net.neoforged.neoforge.fluids.FluidType
import java.util.function.Supplier
import kotlin.math.pow

object HTMachineRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        chemicalReactor(output)
        compressor(output)
        cokeOven(output)
        resourcePlant(output, holderLookup.lookupOrThrow(Registries.BIOME))
    }

    //    Chemical Reactor    //

    fun chemicalReactor(output: RecipeOutput) {
        fun register(tier: HTMachineTier, builder: Supplier<HTMachineRecipeBuilder>) {
            val count: Int = 2.0.pow(tier.ordinal).toInt()
            builder
                .get()
                .itemOutput(RagiumItems.PLASTIC_PLATE, count)
                .tier(tier)
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
            .itemInput(RagiumItemTags.PLASTICS)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.QUARTZ)
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
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.CARBON)
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
            .catalyst(HTMachineTier.BASIC.getCircuitTag())
            .fluidOutput(RagiumFluids.AROMATIC_COMPOUNDS, FluidType.BUCKET_VOLUME * 3)
            .fluidOutput(RagiumFluids.ALCOHOL)
            .saveSuffixed(output, "_from_creosote")
    }

    //    Resource Plant    //

    private fun resourcePlant(output: RecipeOutput, lookup: HolderLookup.RegistryLookup<Biome>) {
        // Brine from Ocean
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.RESOURCE_PLANT)
            .biome(BiomeTags.IS_OCEAN, lookup)
            .fluidOutput(RagiumFluids.BRINE, FluidType.BUCKET_VOLUME / 4)
            .saveSuffixed(output, "_from_ocean")
        // Brine from Beach
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.RESOURCE_PLANT)
            .biome(BiomeTags.IS_BEACH, lookup)
            .fluidOutput(RagiumFluids.BRINE, FluidType.BUCKET_VOLUME / 4)
            .saveSuffixed(output, "_from_beach")

        // Oil from Nether
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.RESOURCE_PLANT)
            .biome(Biomes.SOUL_SAND_VALLEY, lookup)
            .fluidOutput(RagiumFluids.CRUDE_OIL, FluidType.BUCKET_VOLUME / 4)
            .save(output)
    }
}
