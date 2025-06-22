package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import kotlin.jvm.optionals.getOrNull

object HTTagUtil {
    /**
     * 指定した[tagKey]に含まれる[Holder]を返します。
     * @return 名前空間が`ragium`, `minecraft`の順に検索し，見つからない場合は最初の値を返す
     */
    @JvmStatic
    fun getFirstHolder(lookup: HolderGetter<Item>, tagKey: TagKey<Item>): Holder<Item>? {
        val holderSet: HolderSet.Named<Item> = lookup.get(tagKey).getOrNull() ?: return null
        for (modId: String in RagiumConfig.COMMON.tagOutputModIds.get()) {
            val foundHolder: Holder<Item>? = getFirstHolder(holderSet, modId)
            if (foundHolder != null) return foundHolder
        }
        return holderSet.firstOrNull()
    }

    @JvmStatic
    private fun getFirstHolder(holderSet: HolderSet<Item>, namespace: String): Holder<Item>? =
        holderSet.firstOrNull { holder: Holder<Item> -> holder.idOrThrow.namespace == namespace }

    /**
     * 指定した[tagKey]に含まれる[Item]を返します。
     * @return 名前空間が`ragium`, `minecraft`の順に検索し，見つからない場合は最初の値を返す
     */
    @JvmStatic
    fun getFirstItem(lookup: HolderGetter<Item>, tagKey: TagKey<Item>): Item? = getFirstHolder(lookup, tagKey)?.value()
}
