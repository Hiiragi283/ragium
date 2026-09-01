package hiiragi283.lib.recipe

import hiiragi283.lib.registry.createKey
import hiiragi283.lib.resource.HTKeyOrValue
import hiiragi283.lib.resource.toLanguageKey
import hiiragi283.lib.text.HTHasText
import hiiragi283.lib.util.Ior
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType

@JvmRecord
data class HTRecipeType<T : Recipe<*>>(private val id: Identifier) :
    RecipeType<T>,
    HTKeyOrValue<RecipeType<*>, HTRecipeType<T>>,
    HTHasText.Translatable {

    override fun unwrapWithKey(): Ior<ResourceKey<RecipeType<*>>, HTRecipeType<T>> = Ior.Both(Registries.RECIPE_TYPE.createKey(id), this)

    override val translationKey: String get() = keyOrThrow.toLanguageKey()
}
