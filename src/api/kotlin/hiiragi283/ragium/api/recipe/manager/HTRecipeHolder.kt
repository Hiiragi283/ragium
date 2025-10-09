package hiiragi283.ragium.api.recipe.manager

import hiiragi283.ragium.api.registry.HTHolderLike
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

/**
 * @see [RecipeHolder]
 */
@JvmRecord
data class HTRecipeHolder<R : Recipe<*>>(
    @JvmField val id: ResourceLocation,
    @JvmField val recipe: R,
) : HTHolderLike {
    constructor(holder: RecipeHolder<R>) : this(holder.id, holder.value)

    override fun getId(): ResourceLocation = id

    fun toVanilla(): RecipeHolder<R> = RecipeHolder(id, recipe)

    inline fun <reified R2 : R> castRecipe(): HTRecipeHolder<R2> = HTRecipeHolder(id, recipe as R2)

    fun withPrefix(prefix: String): HTRecipeHolder<R> = HTRecipeHolder(id.withPrefix(prefix), recipe)

    fun withSuffix(suffix: String): HTRecipeHolder<R> = HTRecipeHolder(id.withSuffix(suffix), recipe)

    inline fun <R2 : Recipe<*>> mapRecipe(action: (R) -> R2): HTRecipeHolder<R2> = HTRecipeHolder(id, action(recipe))
}
