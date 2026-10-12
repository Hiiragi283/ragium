package hiiragi283.lib.integration.jei

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.RecipeHolder
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * [RecipeHolder]を任意のクラスに使えるようにするためのクラスです。
 * @param RECIPE 保持するレシピのクラス
 * @see hiiragi283.lib.recipe.HTRecipeHolder
 * @author Hiiragi Tsubasa
 * @since 26.1.9
 */
@JvmRecord
data class HTJeiRecipeHolder<out RECIPE : Any>(val id: Identifier?, val recipe: RECIPE) {
    companion object {
        @JvmStatic
        fun <RECIPE : Any> codec(recipeCodec: MapCodec<RECIPE>): Codec<HTJeiRecipeHolder<RECIPE>> =
            HTCodecs.record { instance ->
                instance.group(
                    Identifier.CODEC.optionalFieldOf(HTConstants.ID).forGetter { Optional.ofNullable(it.id) },
                    recipeCodec.forGetter(HTJeiRecipeHolder<RECIPE>::recipe)
                ).apply(instance, ::HTJeiRecipeHolder)
            }
    }

    constructor(id: Optional<Identifier>, recipe: RECIPE) : this(id.getOrNull(), recipe)

    constructor(recipe: RECIPE) : this(null, recipe)

    constructor(parent: HTRecipeHolder<RECIPE>) : this(parent.id, parent.recipe)

    inline fun <U : Any> map(transform: (RECIPE) -> U): HTJeiRecipeHolder<U> =
        HTJeiRecipeHolder(this.id, transform(this.recipe))

    inline fun <U : Any> mapNotNull(transform: (RECIPE) -> U?): HTJeiRecipeHolder<U>? = transform(this.recipe)?.let {
        HTJeiRecipeHolder(this.id, it)
    }

    inline fun <reified U : Any> castAs(): HTJeiRecipeHolder<U>? = mapNotNull { it as? U }
}
