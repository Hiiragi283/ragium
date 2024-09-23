package hiiragi283.ragium.client.integration.rei.display

import hiiragi283.ragium.client.integration.rei.RagiumREIClient
import hiiragi283.ragium.client.integration.rei.entryIngredient
import hiiragi283.ragium.common.recipe.HTTransformRecipe
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.util.EntryIngredients
import net.minecraft.recipe.RecipeEntry
import net.minecraft.util.Identifier

class HTTransformRecipeDisplay(recipe: HTTransformRecipe, id: Identifier) : HTDisplay<HTTransformRecipe>(recipe, id) {
    constructor(entry: RecipeEntry<HTTransformRecipe>) : this(entry.value, entry.id)

    override fun getInputEntries(): List<EntryIngredient> = buildList {
        add(recipe.target.entryIngredient)
        add(recipe.upgrades.getOrNull(0)?.entryIngredient ?: EntryIngredient.empty())
        add(recipe.upgrades.getOrNull(1)?.entryIngredient ?: EntryIngredient.empty())
        add(recipe.upgrades.getOrNull(2)?.entryIngredient ?: EntryIngredient.empty())
    }

    override fun getOutputEntries(): List<EntryIngredient> = listOf(EntryIngredients.of(recipe.result.toStack()))

    override fun getCategoryIdentifier(): CategoryIdentifier<*> = RagiumREIClient.ALCHEMY
}
