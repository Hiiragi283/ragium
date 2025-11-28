package hiiragi283.ragium.api.registry

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.text.HTTextResult
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey

/**
 * [ResourceKey]または[TagKey]を取得できるインターフェース
 * @param T 種類のクラス
 */
interface HTKeyOrTagEntry<T : Any> : HTHolderLike {
    fun unwrap(): Either<ResourceKey<T>, TagKey<T>>

    fun isOf(holder: Holder<T>): Boolean = unwrap().map(holder::`is`, holder::`is`)

    fun getAllHolders(provider: HolderLookup.Provider?): HTTextResult<HolderSet<T>>

    fun getAllHolders(getter: HolderGetter<T>): HTTextResult<HolderSet<T>>

    /**
     * 指定した[HolderLookup.Provider]から[Holder]の[HTTextResult]を返します。
     */
    fun getFirstHolder(provider: HolderLookup.Provider?): HTTextResult<Holder<T>>

    /**
     * 指定した[HolderGetter]から[Holder]の[HTTextResult]を返します。
     */
    fun getFirstHolder(getter: HolderGetter<T>): HTTextResult<Holder<T>>
}
