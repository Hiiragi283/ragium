package hiiragi283.ragium.api.recipe.manager

import hiiragi283.ragium.api.registry.HTHolderLike
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

@JvmInline
value class HTRecipeHolder<R : Recipe<*>>(val holder: RecipeHolder<R>) : HTHolderLike {
    constructor(id: ResourceLocation, recipe: R) : this(RecipeHolder(id, recipe))

    override fun getId(): ResourceLocation = holder.id

    fun recipe(): R = holder.value

    inline fun <reified R2 : R> castRecipe(): HTRecipeHolder<R2> = HTRecipeHolder(getId(), recipe() as R2)

    fun withPrefix(prefix: String): HTRecipeHolder<R> = HTRecipeHolder(getId().withPrefix(prefix), recipe())

    fun withSuffix(suffix: String): HTRecipeHolder<R> = HTRecipeHolder(getId().withSuffix(suffix), recipe())

    inline fun <R2 : Recipe<*>> mapRecipe(action: (R) -> R2): HTRecipeHolder<R2> = HTRecipeHolder(getId(), action(recipe()))
}
