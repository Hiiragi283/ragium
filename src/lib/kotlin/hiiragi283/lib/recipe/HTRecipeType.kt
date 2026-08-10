package hiiragi283.lib.recipe

import hiiragi283.lib.registry.createKey
import hiiragi283.lib.resource.HTKeyLike
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType

@JvmRecord
data class HTRecipeType<T : Recipe<*>>(private val id: Identifier) :
    RecipeType<T>,
    HTKeyLike.SimpleTranslatable<RecipeType<*>> {
    override fun getKey(): ResourceKey<RecipeType<*>> = Registries.RECIPE_TYPE.createKey(id)
}
