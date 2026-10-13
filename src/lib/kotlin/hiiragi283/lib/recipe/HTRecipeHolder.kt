package hiiragi283.lib.recipe

import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

/**
 * [RecipeHolder]を任意のクラスに使えるようにするためのクラスです。
 * @param RECIPE 保持するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTRecipeHolder<out RECIPE : Any>(val key: RecipeKey, val recipe: RECIPE) {
    constructor(id: Identifier, recipe: RECIPE) : this(RecipeKey(id), recipe)

    val id: Identifier get() = key.identifier()

    inline fun <U : Any> map(transform: (RECIPE) -> U): HTRecipeHolder<U> =
        HTRecipeHolder(this.key, transform(this.recipe))

    inline fun <U : Any> mapNotNull(transform: (RECIPE) -> U?): HTRecipeHolder<U>? = transform(this.recipe)?.let {
        HTRecipeHolder(this.key, it)
    }
}

//    Extensions    //

/**
 * [RecipeHolder]を[HTRecipeHolder]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val <RECIPE : Recipe<*>> RecipeHolder<RECIPE>.kotlin: HTRecipeHolder<RECIPE>
    get() = HTRecipeHolder(this.id(), this.value())

/**
 * [HTRecipeHolder]を[RecipeHolder]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val <RECIPE : Recipe<*>> HTRecipeHolder<RECIPE>.java: RecipeHolder<RECIPE>
    get() = RecipeHolder(this.key, this.recipe)
