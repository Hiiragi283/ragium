package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class RagiumChemicalRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        // Cement + Water -> Mortar
        RagiumRecipeBuilders.bathing {
            itemIngredient { items { +RagiumItems.CEMENT } }
            fluidIngredient {
                +waterSet()
                amount /= 4
            }
            result { +RagiumItems.MORTAR }
        }.save(exporter)
    }

    override fun getName(): String = "Chemical Recipes"
}
