package hiiragi283.ragium.integration.rei

import hiiragi283.ragium.common.recipe.HTMachineType
import hiiragi283.ragium.common.recipe.HTRecipeResult
import hiiragi283.ragium.common.recipe.WeightedIngredient
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.entry.EntryStack
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.item.ItemStack

//    CategoryIdentifier    //

val HTMachineType.categoryId: CategoryIdentifier<HTMachineRecipeDisplay>
    get() = RagiumREIClient.getCategoryId(this)

//    WeightedIngredient    //

val WeightedIngredient.entryStacks: List<EntryStack<ItemStack>>
    get() = matchingStacks.map(EntryStacks::of)

val WeightedIngredient.entryIngredient: EntryIngredient
    get() = EntryIngredient.of(entryStacks)

//    HTRecipeResult    //

val HTRecipeResult.entryStack: EntryStack<ItemStack>
    get() = EntryStacks.of(toStack())

val HTRecipeResult.entryIngredient: EntryIngredient
    get() = EntryIngredient.of(entryStack)
