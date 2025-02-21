package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*

//    Recipe    //

fun Recipe<*>.getResultItem(): ItemStack = RagiumAPI.getInstance().getCurrentLookup()?.let(this::getResultItem) ?: ItemStack.EMPTY

//    RecipeManager    //

fun <T : RecipeInput, R : Recipe<T>> RecipeManager.getAllRecipes(type: RecipeType<R>): List<R> =
    getAllRecipesFor(type).map(RecipeHolder<R>::value)

fun <R : HTMachineRecipeBase> RecipeManager.getValidRecipes(type: RecipeType<R>): List<R> =
    getAllRecipes(type).filter(HTMachineRecipeBase::isValidOutput)
