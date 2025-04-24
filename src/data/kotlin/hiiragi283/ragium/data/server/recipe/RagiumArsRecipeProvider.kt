package hiiragi283.ragium.data.server.recipe

import com.hollingsworth.arsnouveau.common.crafting.recipes.ImbuementRecipe
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.crafting.Ingredient

@Suppress("MISSING_DEPENDENCY_SUPERCLASS_IN_TYPE_ARGUMENT")
object RagiumArsRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        imbuement()
    }

    private fun imbuement() {
        // Ragium Essence
        save(
            RagiumAPI.id("imbuement/ragium_essence"),
            ImbuementRecipe(
                Ingredient.of(RagiumItems.INACTIVE_RAGIUM_ESSENCE),
                RagiumItems.RAGIUM_ESSENCE.toStack(),
                10000,
                listOf(),
            ),
        )
    }
}
