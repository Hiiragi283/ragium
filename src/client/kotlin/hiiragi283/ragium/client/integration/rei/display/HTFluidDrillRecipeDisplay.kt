package hiiragi283.ragium.client.integration.rei.display

import hiiragi283.ragium.api.recipe.machine.HTFluidDrillRecipe
import hiiragi283.ragium.client.integration.rei.RagiumREIClient
import hiiragi283.ragium.client.integration.rei.entryIngredient
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.entry.EntryIngredient
import net.minecraft.recipe.RecipeEntry
import net.minecraft.util.Identifier

class HTFluidDrillRecipeDisplay(recipe: HTFluidDrillRecipe, id: Identifier) : HTDisplay<HTFluidDrillRecipe>(recipe, id) {
    constructor(entry: RecipeEntry<HTFluidDrillRecipe>) : this(entry.value, entry.id)

    override fun getInputEntries(): List<EntryIngredient> = listOf()

    override fun getOutputEntries(): List<EntryIngredient> = listOf(recipe.result.entryIngredient)

    override fun getCategoryIdentifier(): CategoryIdentifier<*> = RagiumREIClient.FLUID_DRILL
}
