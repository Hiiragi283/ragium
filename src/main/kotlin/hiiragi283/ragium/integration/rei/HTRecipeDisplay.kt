package hiiragi283.ragium.integration.rei

import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.recipe.HTRecipeResult
import hiiragi283.ragium.common.recipe.WeightedIngredient
import hiiragi283.ragium.integration.entryIngredient
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.display.Display
import me.shedaniel.rei.api.common.entry.EntryIngredient
import net.minecraft.recipe.RecipeEntry
import net.minecraft.util.Identifier
import java.util.*

class HTRecipeDisplay(val recipe: HTMachineRecipe, val id: Identifier) : Display {

    constructor(entry: RecipeEntry<HTMachineRecipe>) : this(entry.value, entry.id)

    override fun getInputEntries(): List<EntryIngredient> = recipe.inputs.map(WeightedIngredient::entryIngredient)

    override fun getOutputEntries(): List<EntryIngredient> = recipe.outputs.map(HTRecipeResult::entryIngredient)

    override fun getCategoryIdentifier(): CategoryIdentifier<*> = RagiumREIClient.getCategoryId(recipe.type)

    override fun getDisplayLocation(): Optional<Identifier> = Optional.of(id)

}