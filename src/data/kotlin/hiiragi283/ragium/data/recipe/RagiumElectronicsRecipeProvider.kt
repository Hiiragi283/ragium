package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.common.fluid.RagiumFluids
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class RagiumElectronicsRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        electrolyzing()
        resourceExtracting()
    }

    private fun electrolyzing() {
        // 2x H2O -> 2x H2 + O2
        RagiumRecipeBuilders.electrolyzing {
            ingredient { +waterSet() }
            result { +RagiumFluids.HYDROGEN }
            result {
                +RagiumFluids.OXYGEN
                amount /= 2
            }
            recipeId suffix "_from_water"
        }.save(exporter)
    }

    private fun resourceExtracting() {
        // XX -> Water
        RagiumRecipeBuilders.resourceExtracting {
            biomes { +holderSet(Tags.Biomes.IS_OCEAN) }
            result {
                +RagiumFluids.SALT_WATER
                amount *= 4
            }
            recipeId suffix "_at_ocean"
        }.save(exporter)
        RagiumRecipeBuilders.resourceExtracting {
            biomes { +holderSet(Tags.Biomes.IS_RIVER) }
            result {
                +Fluids.WATER
                amount *= 2
            }
            recipeId suffix "_at_river"
        }.save(exporter)
        RagiumRecipeBuilders.resourceExtracting {
            biomes { +holderSet(Tags.Biomes.IS_WET) }
            result { +Fluids.WATER }
            recipeId suffix "_at_wet_biomes"
        }.save(exporter)

        // Nether -> Lava
        RagiumRecipeBuilders.resourceExtracting {
            biomes { +holderSet(Tags.Biomes.IS_NETHER) }
            result {
                +Fluids.LAVA
                amount /= 4
            }
            recipeId suffix "_at_nether"
        }.save(exporter)
        // Soul Sand Valley -> Crude Oil
        RagiumRecipeBuilders.resourceExtracting {
            biomes { biomes { +registries.getOrThrow(Biomes.SOUL_SAND_VALLEY) } }
            result {
                +RagiumFluids.CRUDE_OIL
                amount /= 4
            }
            recipeId suffix "_at_soul_sand_valley"
        }.save(exporter)
    }

    override fun getName(): String = "Electronics Recipes"
}
