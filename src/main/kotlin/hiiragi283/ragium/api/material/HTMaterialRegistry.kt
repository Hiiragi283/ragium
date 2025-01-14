package hiiragi283.ragium.api.material

import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Keyable
import hiiragi283.ragium.api.property.HTPropertyHolder
import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import java.util.stream.Stream

/**
 * [HTMaterialKey]のレジストリ
 * @see hiiragi283.ragium.api.RagiumAPI.materialRegistry
 */
interface HTMaterialRegistry : Keyable {
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
     *
     * @return 値がない場合は[emptySet]
     */
    fun getItems(prefix: HTTagPrefix, key: HTMaterialKey): List<Holder<Item>>

    /**
     * 指定された[key]に紐づいた[Entry]を返します。
     * @throws IllegalStateException [key]が登録されていない場合
     */
    fun getEntry(key: HTMaterialKey): Entry = getEntryOrNull(key) ?: error("Unknown material key: $key")

    /**
     * 指定された[key]に紐づいた[Entry]を返します。
     * @return [key]が登録されていない場合はnull
     */
    fun getEntryOrNull(key: HTMaterialKey): Entry?

    //    Keyable    //

    override fun <T : Any> keys(ops: DynamicOps<T>): Stream<T> = keys
        .stream()
        .map(HTMaterialKey::name)
        .map(ops::createString)

    //    Entry    //

    interface Entry : HTPropertyHolder {
        val type: HTMaterialType

        fun getItems(prefix: HTTagPrefix): List<Holder<Item>>

        fun getFirstItemOrNull(prefix: HTTagPrefix): Holder<Item>? = getItems(prefix).firstOrNull()

        fun getFirstItem(prefix: HTTagPrefix): Holder<Item> = getItems(prefix).first()
    }
}
