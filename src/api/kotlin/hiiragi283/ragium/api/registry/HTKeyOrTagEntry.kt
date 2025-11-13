package hiiragi283.ragium.api.registry

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.text.HTTextResult
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import java.util.function.Function

/**
 * [ResourceKey]または[TagKey]を取得できるインターフェース
 * @param T 種類のクラス
 */
interface HTKeyOrTagEntry<T : Any> : HTHolderLike {
    /**
     * 指定した引数から，このオブジェクトを変換します。
     * @param U 変換後のクラス
     * @param fromKey [ResourceKey]から変換するブロック
     * @param fromTag [TagKey]から変換するブロック
     * @return 変換された値
     */
    fun <U> map(fromKey: Function<ResourceKey<T>, U>, fromTag: Function<TagKey<T>, U>): U

    fun toEither(): Either<ResourceKey<T>, TagKey<T>> =
        map(Either<ResourceKey<T>, TagKey<T>>::left, Either<ResourceKey<T>, TagKey<T>>::right)

    /**
     * 指定した[HolderLookup.Provider]から[Holder]の[HTTextResult]を返します。
     */
    fun getFirstHolder(provider: HolderLookup.Provider?): HTTextResult<Holder<T>>

    /**
     * 指定した[HolderGetter]から[Holder]の[HTTextResult]を返します。
     */
    fun getFirstHolder(getter: HolderGetter<T>): HTTextResult<Holder<T>>
}
