package hiiragi283.lib.integration.jei

import hiiragi283.lib.item.HTItemInstanceLike
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.resource.HTIdLike
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
    private val recipeClass: Class<out T>,
) : IRecipeType<T>,
    HTIdLike,
    HTHasText by hasText {
    override fun getUid(): Identifier = id

    override fun getRecipeClass(): Class<out T> = recipeClass

    override fun getId(): Identifier = uid

    override fun toString(): String = "HTJeiRecipeType(uid=$uid)"
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <reified T : Any> HTJeiRecipeType(id: Identifier, hasText: HTHasText, icon: Either<Identifier, ItemStack>): HTJeiRecipeType<T> = HTJeiRecipeType(id, hasText, icon, T::class.java)

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <reified T : Any> HTJeiRecipeType(id: HTIdLike.Translatable, icon: ItemStack): HTJeiRecipeType<T> = HTJeiRecipeType(id.getId(), id, Either.Right(icon))

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <reified T : Any> HTJeiRecipeType(id: HTIdLike.Translatable, icon: HTItemInstanceLike): HTJeiRecipeType<T> = HTJeiRecipeType(id, icon.toStack())
