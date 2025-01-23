package hiiragi283.ragium.api.material

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Keyable
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.property.HTPropertyHolder
import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import java.util.stream.Stream

/**
 * [HTMaterialKey]のレジストリ
 * @see hiiragi283.ragium.api.RagiumAPI.materialRegistry
 */
interface HTMaterialRegistry : Keyable {
    companion object {
        @JvmField
        val ENTRY_CODEC: Codec<Entry> = HTMaterialKey.CODEC.comapFlatMap(
            RagiumAPI.materialRegistry::getEntryData,
            Entry::key,
        )
    }

    /**
     * 登録された[HTMaterialKey]の一覧
     */
    val keys: Set<HTMaterialKey>

    /**
     * 登録された[HTMaterialKey]とその[Entry]のマップ
     */
    val entryMap: Map<HTMaterialKey, Entry>

    /**
     * 指定された[key]が登録されているか判定します。
     */
    operator fun contains(key: HTMaterialKey): Boolean = key in keys

    /**
     * 指定された[prefix]と[key]に紐づいたアイテムの一覧を返します。
     * @return 値がない場合は[emptyList]
     */
    fun getItems(prefix: HTTagPrefix, key: HTMaterialKey): List<Holder<Item>>

    /**
     * 指定された[item]に紐づいた素材のデータを返します。
     * @return 値がない場合は[emptyList]
     */
    fun getDefinitions(item: ItemLike): List<HTMaterialDefinition>

    /**
     * 指定された[key]に紐づいた[Entry]を返します。
     * @return [key]が登録されていない場合はnull
     */
    fun getEntryData(key: HTMaterialKey): DataResult<Entry>

    /**
     * 指定された[key]に紐づいた[Entry]を返します。
     * @throws IllegalStateException [key]が登録されていない場合
     */
    fun getEntry(key: HTMaterialKey): Entry = getEntryData(key).orThrow

    /**
     * 指定された[key]に紐づいた[Entry]を返します。
     * @return [key]が登録されていない場合はnull
     */
    fun getEntryOrNull(key: HTMaterialKey): Entry? = getEntryData(key).getOrNull()

    //    Keyable    //

    override fun <T : Any> keys(ops: DynamicOps<T>): Stream<T> = keys
        .stream()
        .map(HTMaterialKey::name)
        .map(ops::createString)

    //    Entry    //

    interface Entry : HTPropertyHolder {
        val key: HTMaterialKey
        val type: HTMaterialType

        fun getItems(prefix: HTTagPrefix): List<Holder<Item>>

        fun getFirstItemOrNull(prefix: HTTagPrefix): Holder<Item>? = getItems(prefix).firstOrNull()

        fun getFirstItem(prefix: HTTagPrefix): Holder<Item> = getItems(prefix).first()
    }
}
