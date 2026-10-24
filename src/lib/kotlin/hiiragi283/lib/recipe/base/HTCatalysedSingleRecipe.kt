package hiiragi283.lib.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

/**
 * 1種類の材料と触媒から1種類の完成品を作成するレシピを表す抽象クラスです。
 * @param ING レシピの材料を判定するクラス
 * @param RES レシピの完成品を提供するクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
open class HTCatalysedSingleRecipe<out ING : Any, out RES : Any>(
    val ingredient: ING,
    val catalyst: HTItemCatalyst,
    val result: RES,
    override val progressData: HTProgressData,
) : HTProgressRecipe.Holder {
    companion object {
        @JvmStatic
        fun <ING : Any, RES : Any, R : HTCatalysedSingleRecipe<ING, RES>> codec(
            ingredient: Codec<ING>,
            result: Codec<RES>,
            factory: Factory<ING, RES, R>,
        ): MapCodec<R> = HTCodecs.recordMap { instance ->
            instance.group(
                ingredient.fieldOf(HTConstants.INGREDIENT).forGetter { it.ingredient },
                HTItemCatalyst.CODEC.forGetter { it.catalyst },
                result.fieldOf(HTConstants.RESULT).forGetter { it.result },
                HTProgressData.CODEC.forGetter { it.progressData },
            ).apply(instance, factory::create)
        }

        @JvmStatic
        fun <ING : Any, RES : Any, R : HTCatalysedSingleRecipe<ING, RES>> streamCodec(
            ingredient: StreamCodec<in RegistryFriendlyByteBuf, ING>,
            result: StreamCodec<in RegistryFriendlyByteBuf, RES>,
            factory: Factory<ING, RES, R>,
        ): StreamCodec<RegistryFriendlyByteBuf, R> = StreamCodec.composite(
            ingredient,
            { it.ingredient },
            HTItemCatalyst.STREAM_CODEC,
            { it.catalyst },
            result,
            { it.result },
            HTProgressData.STREAM_CODEC,
            { it.progressData },
            factory::create,
        )
    }

    //    Factory    //

    /**
     * @param ING レシピの材料を判定するクラス
     * @param RES レシピの完成品を提供するクラス
     * @param RECIPE 出力するレシピのクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    fun interface Factory<ING : Any, RES : Any, out RECIPE : Any> {
        fun create(ingredient: ING, catalyst: HTItemCatalyst, result: RES, progressData: HTProgressData): RECIPE
    }
}
