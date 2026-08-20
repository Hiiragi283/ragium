package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.registry.VanillaFluidContents
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import hiiragi283.ragium.api.tag.HTMaterial
import hiiragi283.ragium.fluid.RagiumFluids
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput

class RagiumChemicalRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        electrolyzing()
    }

    private fun electrolyzing() {
        // 2x H2O -> 2x H2 + O2
        RagiumRecipeBuilders.electrolyzing {
            fluidIngredient { +holderSet(VanillaFluidContents.WATER) }
            result { +RagiumFluids.HYDROGEN }
            result {
                +RagiumFluids.OXYGEN
                amount /= 2
            }
            recipeId suffix "_from_water"
        }.save(exporter)
        // 2x NaCl(aq) -> H2 + Cl2 + 2x NaOH(aq)
        RagiumRecipeBuilders.electrolyzing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, HTMaterial.Mineral.SALT) }
            fluidIngredient { +holderSet(VanillaFluidContents.WATER) }
            result {
                +RagiumFluids.HYDROGEN
                amount /= 2
            }
            result {
                +RagiumFluids.CHLORINE
                amount /= 2
            }
            result { +RagiumFluids.NAOH_SOLUTION }
            recipeId suffix "_from_salt_water"
        }.save(exporter)
    }

    override fun getName(): String = "Chemical Recipes"
}
