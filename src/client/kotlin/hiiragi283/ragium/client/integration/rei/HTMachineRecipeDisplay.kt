package hiiragi283.ragium.client.integration.rei

import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.recipe.HTRecipeResult
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.display.Display
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.util.EntryIngredients
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.recipe.RecipeEntry
import net.minecraft.util.Identifier
import java.util.*

@Environment(EnvType.CLIENT)
class HTMachineRecipeDisplay(val recipe: HTMachineRecipe, val id: Identifier) : Display {
    constructor(entry: RecipeEntry<HTMachineRecipe>) : this(entry.value, entry.id)

    override fun getInputEntries(): List<EntryIngredient> = buildList {
        add(recipe.getInput(0)?.entryIngredient ?: EntryIngredient.empty())
        add(recipe.getInput(1)?.entryIngredient ?: EntryIngredient.empty())
        add(recipe.getInput(2)?.entryIngredient ?: EntryIngredient.empty())
        add(EntryIngredients.ofIngredient(recipe.catalyst))
    }

    override fun getOutputEntries(): List<EntryIngredient> = recipe.outputs.map(HTRecipeResult::entryIngredient)

    override fun getCategoryIdentifier(): CategoryIdentifier<*> = recipe.type.categoryId

    override fun getDisplayLocation(): Optional<Identifier> = Optional.of(id)
}
