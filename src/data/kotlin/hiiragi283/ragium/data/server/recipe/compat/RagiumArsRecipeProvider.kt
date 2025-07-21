package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.data.HTRecipeProvider

@Suppress("MISSING_DEPENDENCY_SUPERCLASS_IN_TYPE_ARGUMENT")
object RagiumArsRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        imbuement()
    }

    private fun imbuement() {
        // Ragium Essence
        /*save(
            RagiumAPI.id("imbuement/ragium_essence"),
            ImbuementRecipe(
                Ingredient.of(RagiumItems.INACTIVE_RAGIUM_ESSENCE),
                RagiumItems.RAGIUM_ESSENCE.toStack(),
                10000,
                listOf(),
            ),
        )*/
    }
}
