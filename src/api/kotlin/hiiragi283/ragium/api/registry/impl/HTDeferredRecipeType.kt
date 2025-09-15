package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.registry.HTDeferredHolder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType

class HTDeferredRecipeType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> : HTDeferredHolder<RecipeType<*>, RecipeType<RECIPE>> {
    constructor(key: ResourceKey<RecipeType<*>>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.RECIPE_TYPE, id)

    fun getAllRecipes(recipeManager: RecipeManager): Iterable<RecipeHolder<RECIPE>> = recipeManager.getAllRecipesFor(get())

    inline fun forEach(recipeManager: RecipeManager, action: (ResourceLocation, RECIPE) -> Unit) {
        for (holder: RecipeHolder<RECIPE> in getAllRecipes(recipeManager)) {
            val id: ResourceLocation = holder.id
            val recipe: RECIPE = holder.value
            action(id, recipe)
        }
    }
}
