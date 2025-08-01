package hiiragi283.ragium.api.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredHolder

class HTDeferredRecipeType<I : RecipeInput, R : Recipe<I>>(key: ResourceKey<RecipeType<*>>) :
    DeferredHolder<RecipeType<*>, RecipeType<R>>(key) {
    companion object {
        @JvmStatic
        fun <I : RecipeInput, R : Recipe<I>> createType(key: ResourceLocation): HTDeferredRecipeType<I, R> =
            createType(ResourceKey.create(Registries.RECIPE_TYPE, key))

        @JvmStatic
        fun <I : RecipeInput, R : Recipe<I>> createType(key: ResourceKey<RecipeType<*>>): HTDeferredRecipeType<I, R> =
            HTDeferredRecipeType(key)
    }

    fun getAllRecipes(recipeManager: RecipeManager): Iterable<RecipeHolder<R>> = recipeManager.getAllRecipesFor(get())

    inline fun forEach(recipeManager: RecipeManager, action: (ResourceLocation, R) -> Unit) {
        for (holder: RecipeHolder<R> in getAllRecipes(recipeManager)) {
            val id: ResourceLocation = holder.id
            val recipe: R = holder.value
            action(id, recipe)
        }
    }
}
