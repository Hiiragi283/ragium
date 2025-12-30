package hiiragi283.ragium.client.emi

import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.stack.Comparison
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.integration.emi.HTEmiPlugin
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.emi.recipe.HTAlloyingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTComplexEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTExtraProcessingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTMeltingEmiRecipe
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.component.DataComponents

@EmiEntrypoint
class RagiumEmiPlugin : HTEmiPlugin(RagiumAPI.MOD_ID) {
    override fun register(registry: EmiRegistry) {
        // Category
        listOf(
            // Processor
            RagiumEmiRecipeCategories.ALLOYING,
            RagiumEmiRecipeCategories.CRUSHING,
            RagiumEmiRecipeCategories.DRYING,
            RagiumEmiRecipeCategories.MELTING,
            RagiumEmiRecipeCategories.PYROLYZING,
        ).forEach(::addCategory.partially1(registry))

        // Recipes
        addRegistryRecipes(registry, RagiumRecipeTypes.ALLOYING, ::HTAlloyingEmiRecipe)
        addRegistryRecipes(registry, RagiumRecipeTypes.CRUSHING, HTExtraProcessingEmiRecipe.Companion::crushing)
        addRegistryRecipes(registry, RagiumRecipeTypes.DRYING, HTComplexEmiRecipe.Companion::drying)
        addRegistryRecipes(registry, RagiumRecipeTypes.MELTING, ::HTMeltingEmiRecipe)
        addRegistryRecipes(registry, RagiumRecipeTypes.PYROLYZING, HTComplexEmiRecipe.Companion::pyrolyzing)

        // Misc

        registry.setDefaultComparison(
            RagiumItems.LOOT_TICKET.get(),
            Comparison.compareData { stack: EmiStack -> stack.get(RagiumDataComponents.LOOT_TICKET) },
        )

        val potion: Comparison = Comparison.compareData { stack: EmiStack -> stack.get(DataComponents.POTION_CONTENTS) }
        registry.setDefaultComparison(RagiumItems.POTION_DROP.get(), potion)
    }
}
