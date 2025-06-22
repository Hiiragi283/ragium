package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import kotlin.jvm.optionals.getOrNull

object HTTagUtil {
    /**
     * 指定した[tagKey]に含まれる[net.minecraft.core.Holder]を返します。
     * @return 名前空間が`ragium`, `minecraft`の順に検索し，見つからない場合は最初の値を返す
     */
    @JvmStatic
    fun getFirstHolder(lookup: HolderGetter<Item>, tagKey: TagKey<Item>): Holder<Item>? {
        val holderSet: HolderSet.Named<Item> = lookup.get(tagKey).getOrNull() ?: return null
        // Find item from Ragium
        var firstHolder: Holder<Item>? =
            holderSet.firstOrNull { holder: Holder<Item> -> holder.idOrThrow.namespace == RagiumAPI.MOD_ID }
        // Find item from Vanilla
        if (firstHolder == null) {
            firstHolder = holderSet.firstOrNull { holder: Holder<Item> -> holder.idOrThrow.namespace == RagiumConstantValues.MINECRAFT }
        }
        // Return found item or first item
        return firstHolder ?: holderSet.firstOrNull()
    }

    /**
     * 指定した[tagKey]に含まれる[Item]を返します。
     * @return 名前空間が`ragium`, `minecraft`の順に検索し，見つからない場合は最初の値を返す
     */
    @JvmStatic
    fun getFirstItem(lookup: HolderGetter<Item>, tagKey: TagKey<Item>): Item? = getFirstHolder(lookup, tagKey)?.value()
}
