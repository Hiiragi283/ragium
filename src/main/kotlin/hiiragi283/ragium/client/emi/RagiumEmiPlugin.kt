package hiiragi283.ragium.client.emi

import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiRegistry
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.integration.emi.HTEmiPlugin
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.emi.recipe.HTAlloyingEmiRecipe
import hiiragi283.ragium.setup.RagiumRecipeTypes

@EmiEntrypoint
class RagiumEmiPlugin : HTEmiPlugin(RagiumAPI.MOD_ID) {
    override fun register(registry: EmiRegistry) {
        // Category
        listOf(
            // Processor
            RagiumEmiRecipeCategories.ALLOYING,
        ).forEach(::addCategory.partially1(registry))

        // Recipes
        addRegistryRecipes(registry, RagiumRecipeTypes.ALLOYING, ::HTAlloyingEmiRecipe)
    }
}
