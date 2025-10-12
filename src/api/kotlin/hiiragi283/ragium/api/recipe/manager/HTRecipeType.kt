package hiiragi283.ragium.api.recipe.manager

import hiiragi283.ragium.api.text.HTHasText
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager

interface HTRecipeType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> : HTHasText {
    fun getAllHolders(manager: RecipeManager): Sequence<RecipeHolder<RECIPE>>

    fun getAllRecipes(manager: RecipeManager): Sequence<RECIPE> = getAllHolders(manager).map(RecipeHolder<RECIPE>::value)
}
