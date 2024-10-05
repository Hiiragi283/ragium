package hiiragi283.ragium.client.integration.rei.display

import hiiragi283.ragium.api.recipe.HTRecipeResult
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.client.integration.rei.categoryId
import hiiragi283.ragium.client.integration.rei.entryIngredient
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.util.EntryIngredients
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeEntry
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
class HTMachineRecipeDisplay(recipe: HTMachineRecipe, id: Identifier) : HTDisplay<HTMachineRecipe>(recipe, id) {
    constructor(entry: RecipeEntry<HTMachineRecipe>) : this(entry.value, entry.id)

    val catalyst: Ingredient = recipe.catalyst

    override fun getInputEntries(): List<EntryIngredient> = buildList {
        add(recipe.getInput(0)?.entryIngredient ?: EntryIngredient.empty())
        add(recipe.getInput(1)?.entryIngredient ?: EntryIngredient.empty())
        add(recipe.getInput(2)?.entryIngredient ?: EntryIngredient.empty())
        add(EntryIngredients.ofIngredient(recipe.catalyst))
    }

    override fun getOutputEntries(): List<EntryIngredient> = recipe.outputs.map(HTRecipeResult::entryIngredient)

    override fun getCategoryIdentifier(): CategoryIdentifier<*> = recipe.type.categoryId
}
