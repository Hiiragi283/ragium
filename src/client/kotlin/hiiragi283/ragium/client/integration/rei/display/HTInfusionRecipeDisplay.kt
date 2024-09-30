package hiiragi283.ragium.client.integration.rei.display

import hiiragi283.ragium.client.integration.rei.RagiumREIClient
import hiiragi283.ragium.client.integration.rei.entryIngredient
import hiiragi283.ragium.common.recipe.alchemy.HTInfusionRecipe
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.entry.EntryIngredient
import net.minecraft.recipe.RecipeEntry
import net.minecraft.util.Identifier

class HTInfusionRecipeDisplay(recipe: HTInfusionRecipe, id: Identifier) : HTDisplay<HTInfusionRecipe>(recipe, id) {
    constructor(entry: RecipeEntry<HTInfusionRecipe>) : this(entry.value, entry.id)

    override fun getInputEntries(): List<EntryIngredient> = buildList {
        add(recipe.getInput(0)?.entryIngredient ?: EntryIngredient.empty())
        add(recipe.getInput(1)?.entryIngredient ?: EntryIngredient.empty())
        add(recipe.getInput(2)?.entryIngredient ?: EntryIngredient.empty())
        add(recipe.getInput(3)?.entryIngredient ?: EntryIngredient.empty())
    }

    override fun getOutputEntries(): List<EntryIngredient> = listOf(recipe.result.entryIngredient)

    override fun getCategoryIdentifier(): CategoryIdentifier<*> = RagiumREIClient.ALCHEMY
}
