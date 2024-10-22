package hiiragi283.ragium.client.integration.rei.display

import hiiragi283.ragium.api.recipe.machines.HTMachineRecipeBase
import hiiragi283.ragium.client.integration.rei.categoryIdNew
import hiiragi283.ragium.client.integration.rei.entryIngredient
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.display.Display
import me.shedaniel.rei.api.common.entry.EntryIngredient
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.recipe.RecipeEntry
import net.minecraft.util.Identifier
import java.util.*

@Environment(EnvType.CLIENT)
class HTMachineRecipeDisplayNew(val recipe: HTMachineRecipeBase<*>, val id: Identifier) : Display {
    constructor(entry: RecipeEntry<out HTMachineRecipeBase<*>>) : this(entry.value, entry.id)

    override fun getInputEntries(): List<EntryIngredient> = buildList {
        add(recipe.itemInputs.getOrNull(0)?.entryIngredient ?: EntryIngredient.empty())
        add(recipe.itemInputs.getOrNull(1)?.entryIngredient ?: EntryIngredient.empty())
        add(recipe.fluidInputs.getOrNull(0)?.entryIngredient ?: EntryIngredient.empty())
        add(recipe.fluidInputs.getOrNull(1)?.entryIngredient ?: EntryIngredient.empty())
    }

    override fun getOutputEntries(): List<EntryIngredient> = buildList {
        add(recipe.itemOutputs.getOrNull(0)?.entryIngredient ?: EntryIngredient.empty())
        add(recipe.itemOutputs.getOrNull(1)?.entryIngredient ?: EntryIngredient.empty())
        add(recipe.fluidOutputs.getOrNull(0)?.entryIngredient ?: EntryIngredient.empty())
        add(recipe.fluidOutputs.getOrNull(1)?.entryIngredient ?: EntryIngredient.empty())
    }

    override fun getCategoryIdentifier(): CategoryIdentifier<*> = recipe.machineType.categoryIdNew

    override fun getDisplayLocation(): Optional<Identifier> = Optional.of(id)
}
