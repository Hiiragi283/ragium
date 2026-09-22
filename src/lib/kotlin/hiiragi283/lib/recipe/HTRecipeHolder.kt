package hiiragi283.lib.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.HTValueWithId
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.util.Ior
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
data class HTRecipeHolder<out RECIPE : Any>(val key: RecipeKey, val recipe: RECIPE) : HTValueWithId<RECIPE> {
    companion object {
        @JvmStatic
        fun <RECIPE : Any> codec(recipeCodec: MapCodec<RECIPE>): Codec<HTRecipeHolder<RECIPE>> =
            HTCodecs.record { instance ->
                instance.group(
                    Recipe.KEY_CODEC.fieldOf(HTConstants.ID).forGetter(HTRecipeHolder<RECIPE>::key),
                    recipeCodec.forGetter(HTRecipeHolder<RECIPE>::recipe)
                ).apply(instance, ::HTRecipeHolder)
            }
    }

    constructor(id: Identifier, recipe: RECIPE) : this(RecipeKey(id), recipe)

    constructor(entry: Map.Entry<RecipeKey, RECIPE>) : this(entry.key, entry.value)

    constructor(pair: Pair<RecipeKey, RECIPE>) : this(pair.first, pair.second)

    val id: Identifier get() = key.identifier()

    inline fun <U : Any> map(transform: (RECIPE) -> U): HTRecipeHolder<U> =
        HTRecipeHolder(this.key, transform(this.recipe))

    inline fun <U : Any> mapNotNull(transform: (RECIPE) -> U?): HTRecipeHolder<U>? = transform(this.recipe)?.let {
        HTRecipeHolder(this.key, it)
    }

    inline fun <reified U : Any> castAs(): HTRecipeHolder<U>? = mapNotNull { it as? U }

    override fun unwrapWithId(): Ior<Identifier, RECIPE> = Ior.Both(this.id, this.recipe)
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
