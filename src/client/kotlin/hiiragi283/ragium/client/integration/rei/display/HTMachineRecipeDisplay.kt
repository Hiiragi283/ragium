package hiiragi283.ragium.client.integration.rei.display

import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.client.integration.rei.categoryId
import hiiragi283.ragium.client.integration.rei.getInputEntries
import hiiragi283.ragium.client.integration.rei.getOutputEntries
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.entry.EntryIngredient
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeEntry
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
class HTMachineRecipeDisplay(recipe: HTMachineRecipe, id: Identifier) : HTDisplay<HTMachineRecipe>(recipe, id) {
    constructor(entry: RecipeEntry<HTMachineRecipe>) : this(entry.value, entry.id)

    val catalyst: Ingredient = recipe.catalyst

    override fun getInputEntries(): List<EntryIngredient> = recipe.type.getInputEntries(recipe)

    override fun getOutputEntries(): List<EntryIngredient> = recipe.type.getOutputEntries(recipe)

    override fun getCategoryIdentifier(): CategoryIdentifier<*> = recipe.type.categoryId
}
