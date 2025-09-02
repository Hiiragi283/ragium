package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

object HTTagHelper {
    /**
     * 指定した[tagKey]に含まれる[net.minecraft.core.Holder]を返します。
     * @return 名前空間が`ragium`, `minecraft`の順に検索し，見つからない場合は最初の値を返す
     */
    @JvmStatic
    fun <T : Any> getFirstHolder(lookup: HolderGetter<T>, tagKey: TagKey<T>): Holder<T>? =
        lookup.get(tagKey).flatMap(::getFirstHolder).getOrNull()

    @JvmStatic
    fun <T : Any> getFirstHolder(holderSet: HolderSet<T>): Optional<Holder<T>> {
        for (modId: String in RagiumConfig.CONFIG.tagOutputPriority.get()) {
            val foundHolder: Holder<T>? = getFirstHolder(holderSet, modId)
            if (foundHolder != null) return Optional.of(foundHolder)
        }
        return Optional.ofNullable(holderSet.firstOrNull())
    }

    @JvmStatic
    private fun <T : Any> getFirstHolder(holderSet: HolderSet<T>, namespace: String): Holder<T>? =
        holderSet.firstOrNull { holder: Holder<T> -> holder.idOrThrow.namespace == namespace }
}
