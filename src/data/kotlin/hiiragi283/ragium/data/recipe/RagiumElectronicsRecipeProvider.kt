package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.common.fluid.RagiumFluids
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class RagiumElectronicsRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        electrolyzing()
    }

    private fun electrolyzing() {
        // 2x H2O -> 2x H2 + O2
        RagiumRecipeBuilders.electrolyzing {
            ingredient { +waterSet() }
            fluidResult { +RagiumFluids.HYDROGEN }
            fluidResult {
                +RagiumFluids.OXYGEN
                amount /= 2
            }
            recipeId replace id("water")
        }.save(exporter)
    }

    override fun getName(): String = "Electronics Recipes"
}
