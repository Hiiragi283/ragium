package hiiragi283.lib.recipe

import hiiragi283.lib.registry.createKey
import hiiragi283.lib.resource.toId
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Recipe

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

//    HTRecipeHolder    //

typealias HTRecipeHolder<R> = Pair<RecipeKey, R>

fun <R> HTRecipeHolder(id: Identifier, recipe: R): HTRecipeHolder<R> = RecipeKey(id) to recipe

val <R> HTRecipeHolder<R>.key: RecipeKey get() = this.first

val <R> HTRecipeHolder<R>.id: Identifier get() = this.key.identifier()

val <R> HTRecipeHolder<R>.recipe: R get() = this.second
