package hiiragi283.lib.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.ingredient.HTIngredient
import hiiragi283.lib.recipe.result.HTRecipeResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 1種類の材料から1種類の完成品を作成するレシピを表す抽象クラスです。
 * @param INPUT レシピの入力となるクラス
 * @param ING レシピの材料を判定するクラス
 * @param RES レシピの完成品を提供するクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
open class HTBasicSingleRecipe<INPUT : RecipeInput, ING : HTIngredient<*, *>, RES : HTRecipeResult<*>>(
    val ingredient: ING,
    val result: RES,
    override val progressData: HTProgressData
) : HTProgressRecipe.Simple<INPUT> {
    companion object {
        @JvmStatic
        fun <ING : HTIngredient<*, *>, RES : HTRecipeResult<*>, RECIPE : HTBasicSingleRecipe<*, ING, RES>> codec(
            ingredient: Codec<ING>,
            result: Codec<RES>,
            factory: Factory<ING, RES, RECIPE>
        ): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
            instance.group(
                ingredient.fieldOf(HTConstants.INGREDIENT).forGetter { it.ingredient },
                result.fieldOf(HTConstants.RESULT).forGetter { it.result },
                HTProgressData.CODEC.forGetter { it.progressData }
            ).apply(instance, factory::create)
        }

        @JvmStatic
        fun <ING : HTIngredient<*, *>, RES : HTRecipeResult<*>, RECIPE : HTBasicSingleRecipe<*, ING, RES>> streamCodec(
            ingredient: StreamCodec<in RegistryFriendlyByteBuf, ING>,
            result: StreamCodec<in RegistryFriendlyByteBuf, RES>,
            factory: Factory<ING, RES, RECIPE>
        ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
            ingredient,
            { it.ingredient },
            result,
            { it.result },
            HTProgressData.STREAM_CODEC,
            { it.progressData },
            factory::create
        )
    }

    //    Factory    //

    /**
     * @param ING レシピの材料を判定するクラス
     * @param RES レシピの完成品を提供するクラス
     * @param RECIPE 出力するレシピのクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.8
     */
    fun interface Factory<ING : HTIngredient<*, *>, RES : HTRecipeResult<*>, out RECIPE : Any> {
        fun create(ingredient: ING, result: RES, progressData: HTProgressData): RECIPE
    }
}
