package hiiragi283.lib.integration.jei

import hiiragi283.lib.item.HTItemInstanceLike
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.resource.HTIdOrValue
import hiiragi283.lib.text.HTHasText
import hiiragi283.lib.util.Either
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack

/**
 * [HTRecipeHolder]向けの[HTJeiRecipeType]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias HTHolderJeiRecipeType<T> = HTJeiRecipeType<HTRecipeHolder<T>>

/**
 * Hiiragi Seriesで使用される[IRecipeType]の実装クラスです。
 * @param T 対象のレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@Suppress("NonExtendableApiUsage")
class HTJeiRecipeType<T : Any>(
    private val id: Identifier,
    hasText: HTHasText,
    val icon: Either<Identifier, ItemStack>,
    private val recipeClass: Class<out T>
) : IRecipeType<T>,
    HTHasText by hasText {
    override fun getUid(): Identifier = id

    override fun getRecipeClass(): Class<out T> = recipeClass

    override fun toString(): String = "HTJeiRecipeType(uid=$uid)"
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <reified T : Any> HTJeiRecipeType(
    id: Identifier,
    hasText: HTHasText,
    icon: Either<Identifier, ItemStack>
): HTJeiRecipeType<T> = HTJeiRecipeType(id, hasText, icon, T::class.java)

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <reified T : Any, U> HTJeiRecipeType(
    id: U,
    icon: ItemStack
): HTJeiRecipeType<T> where U : HTIdOrValue<*>, U : HTHasText = HTJeiRecipeType(id.idOrThrow, id, Either.Right(icon))

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <reified T : Any, U> HTJeiRecipeType(
    id: U,
    icon: HTItemInstanceLike
): HTJeiRecipeType<T> where U : HTIdOrValue<*>, U : HTHasText = HTJeiRecipeType(id, icon.toStack())
