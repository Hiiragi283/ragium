package hiiragi283.ragium.client.emi

import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.stack.Comparison
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.Bounds
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.integration.emi.HTEmiPlugin
import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.emi.recipe.HTAlloyingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTComplexEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTCrushingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTCuttingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTMeltingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTPlantingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTPyrolyzingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTRefiningEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTSolidifyingEmiRecipe
import hiiragi283.ragium.client.gui.screen.HTProcessorScreen
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.client.gui.screens.Screen
import net.minecraft.core.component.DataComponents
import java.util.function.Consumer

@EmiEntrypoint
class RagiumEmiPlugin : HTEmiPlugin(RagiumAPI.MOD_ID) {
    override fun register(registry: EmiRegistry) {
        // Category
        listOf(
            // Processor - Basic
            RagiumEmiRecipeCategories.ALLOYING,
            RagiumEmiRecipeCategories.CRUSHING,
            RagiumEmiRecipeCategories.CUTTING,
            // Processor - Advanced
            RagiumEmiRecipeCategories.DRYING,
            RagiumEmiRecipeCategories.MELTING,
            RagiumEmiRecipeCategories.MIXING,
            RagiumEmiRecipeCategories.PYROLYZING,
            RagiumEmiRecipeCategories.REFINING,
            RagiumEmiRecipeCategories.SOLIDIFYING,
            // Device - Basic
            RagiumEmiRecipeCategories.PLANTING,
        ).forEach(::addCategory.partially1(registry))

        // Recipes
        addRegistryRecipes(registry, RagiumRecipeTypes.ALLOYING, ::HTAlloyingEmiRecipe)
        addRegistryRecipes(registry, RagiumRecipeTypes.CRUSHING, ::HTCrushingEmiRecipe)
        addRegistryRecipes(registry, RagiumRecipeTypes.CUTTING, ::HTCuttingEmiRecipe)

        addRegistryRecipes(registry, RagiumRecipeTypes.DRYING, HTComplexEmiRecipe.Companion::drying)
        addRegistryRecipes(registry, RagiumRecipeTypes.MELTING, ::HTMeltingEmiRecipe)
        addRegistryRecipes(registry, RagiumRecipeTypes.MIXING, HTComplexEmiRecipe.Companion::mixing)
        addRegistryRecipes(registry, RagiumRecipeTypes.PYROLYZING, ::HTPyrolyzingEmiRecipe)
        addRegistryRecipes(registry, RagiumRecipeTypes.REFINING, ::HTRefiningEmiRecipe)
        addRegistryRecipes(registry, RagiumRecipeTypes.SOLIDIFYING, ::HTSolidifyingEmiRecipe)

        addRegistryRecipes(registry, RagiumRecipeTypes.PLANTING, ::HTPlantingEmiRecipe)

        // Misc
        registry.addGenericExclusionArea { screen: Screen, consumer: Consumer<Bounds> ->
            if (screen is HTProcessorScreen<*, *>) {
                consumer.accept(
                    Bounds(
                        HTSlotHelper.getSlotPosX(9),
                        HTSlotHelper.getSlotPosY(-0.5),
                        18 * 2,
                        18 * 4,
                    ),
                )
            }
        }

        registry.setDefaultComparison(
            RagiumItems.LOOT_TICKET.get(),
            Comparison.compareData { stack: EmiStack -> stack.get(RagiumDataComponents.LOOT_TICKET) },
        )

        val potion: Comparison = Comparison.compareData { stack: EmiStack -> stack.get(DataComponents.POTION_CONTENTS) }
        registry.setDefaultComparison(RagiumItems.POTION_DROP.get(), potion)
    }
}
