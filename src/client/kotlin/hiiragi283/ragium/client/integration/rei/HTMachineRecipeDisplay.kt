package hiiragi283.ragium.client.integration.rei

import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.display.Display
import me.shedaniel.rei.api.common.entry.EntryIngredient
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeEntry
import net.minecraft.util.Identifier
import java.util.*

@Environment(EnvType.CLIENT)
class HTMachineRecipeDisplay(val recipe: HTMachineRecipe, val id: Identifier) : Display {
    constructor(entry: RecipeEntry<HTMachineRecipe>) : this(entry.value, entry.id)

    val catalyst: Ingredient = recipe.catalyst

    override fun getInputEntries(): List<EntryIngredient> = recipe.type.getInputEntries(recipe)

    override fun getOutputEntries(): List<EntryIngredient> = recipe.type.getOutputEntries(recipe)

    override fun getCategoryIdentifier(): CategoryIdentifier<*> = recipe.type.categoryId

    override fun getDisplayLocation(): Optional<Identifier> = Optional.of(id)
}
