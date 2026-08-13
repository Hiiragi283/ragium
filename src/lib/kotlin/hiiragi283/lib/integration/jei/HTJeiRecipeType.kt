package hiiragi283.lib.integration.jei

import hiiragi283.lib.item.HTSimpleItemLike
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.text.HTHasText
import hiiragi283.lib.util.Either
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack

class HTJeiRecipeType<T : Any>(
    private val id: Identifier,
    hasText: HTHasText,
    val icon: Either<Identifier, ItemStack>,
    val width: Int,
    val height: Int,
    private val recipeClass: Class<out T>,
) : IRecipeType<T>,
    HTIdLike,
    HTHasText by hasText {
    override fun getUid(): Identifier = id

    override fun getRecipeClass(): Class<out T> = recipeClass

    override fun getId(): Identifier = uid

    override fun toString(): String = "HTJeiRecipeType(uid=$uid)"
}

inline fun <reified T : Any> HTJeiRecipeType(id: Identifier, hasText: HTHasText, icon: Either<Identifier, ItemStack>, width: Int, height: Int): HTJeiRecipeType<T> = HTJeiRecipeType(id, hasText, icon, width, height, T::class.java)

inline fun <reified T : Any> HTJeiRecipeType(id: HTIdLike.Translatable, icon: HTSimpleItemLike, width: Int, height: Int): HTJeiRecipeType<T> = HTJeiRecipeType(id.getId(), id, Either.Right(icon.toStack()), width, height)
