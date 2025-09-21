package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.recipe.HTRecipeGetter
import hiiragi283.ragium.api.recipe.HTRecipeHolder
import hiiragi283.ragium.api.registry.HTDeferredHolder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

class HTDeferredRecipeType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> : HTDeferredHolder<RecipeType<*>, RecipeType<RECIPE>> {
    constructor(key: ResourceKey<RecipeType<*>>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.RECIPE_TYPE, id)

    fun getAllRecipes(getter: HTRecipeGetter): Sequence<HTRecipeHolder<RECIPE>> = getter.getAllRecipesFor(get())

    inline fun forEach(getter: HTRecipeGetter, action: (ResourceLocation, RECIPE) -> Unit) {
        for (holder: HTRecipeHolder<RECIPE> in getAllRecipes(getter)) {
            action(holder.id, holder.recipe)
        }
    }
}
