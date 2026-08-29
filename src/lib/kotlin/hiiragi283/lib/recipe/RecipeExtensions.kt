package hiiragi283.lib.recipe

import hiiragi283.lib.registry.createKey
import hiiragi283.lib.resource.toId
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

//    RecipeKey    //

/**
 * [Recipe]に対する[ResourceKey]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias RecipeKey = ResourceKey<Recipe<*>>

/**
 * 新しい[RecipeKey]のインスタンスを作成します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun RecipeKey(namespace: String, path: String): RecipeKey = RecipeKey(namespace.toId(path))

/**
 * 新しい[RecipeKey]のインスタンスを作成します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun RecipeKey(namespace: String, vararg path: String): RecipeKey = RecipeKey(namespace.toId(*path))

/**
 * 新しい[RecipeKey]のインスタンスを作成します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun RecipeKey(id: Identifier): RecipeKey = Registries.RECIPE.createKey(id)

//    RecipeHolder    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
operator fun RecipeHolder<*>.component1(): RecipeKey = this.id()

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
operator fun <R : Recipe<*>> RecipeHolder<R>.component2(): R = this.value()

//    HTRecipeHolder    //

/**
 * [RecipeHolder]を任意のクラスに使えるようにするためのエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias HTRecipeHolder<R> = Pair<RecipeKey, R>

/**
 * 新しい[HTRecipeHolder]のインスタンスを作成します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <R> HTRecipeHolder(id: Identifier, recipe: R): HTRecipeHolder<R> = RecipeKey(id) to recipe

/**
 * [RecipeHolder]を[HTRecipeHolder]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <R : Recipe<*>> HTRecipeHolder(holder: RecipeHolder<R>): HTRecipeHolder<R> = holder.id() to holder.value()

/**
 * [HTRecipeHolder]を[RecipeHolder]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <R : Recipe<*>> RecipeHolder(holder: HTRecipeHolder<R>): RecipeHolder<R> = RecipeHolder(holder.key, holder.recipe)

/**
 * [HTRecipeHolder]が保持するレシピのID
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val <R> HTRecipeHolder<R>.key: RecipeKey get() = this.first

/**
 * [HTRecipeHolder]が保持するレシピのID
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val <R> HTRecipeHolder<R>.id: Identifier get() = this.key.identifier()

/**
 * [HTRecipeHolder]が保持するレシピ
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val <R> HTRecipeHolder<R>.recipe: R get() = this.second
