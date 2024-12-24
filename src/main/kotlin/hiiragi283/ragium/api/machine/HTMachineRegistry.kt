package hiiragi283.ragium.api.machine

import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Keyable
import hiiragi283.ragium.api.block.HTMachineBlock
import hiiragi283.ragium.api.property.HTPropertyHolder
import net.minecraft.item.ItemConvertible
import net.minecraft.util.Identifier
import java.util.stream.Stream

/**
 * [HTMachineKey]のレジストリ
 * @see hiiragi283.ragium.api.RagiumAPI.machineRegistry
 */
class HTMachineRegistry(
    private val types: Map<HTMachineKey, HTMachineType>,
    private val blockMap: Map<HTMachineKey, HTMachineBlock>,
    private val properties: Map<HTMachineKey, HTPropertyHolder>,
) : Keyable {
    /**
     * 登録された[HTMachineKey]の一覧
     */
    val keys: Set<HTMachineKey>
        get() = types.keys

    /**
     * 登録された[HTMachineKey]とその[Entry]のマップ
     */
    val entryMap: Map<HTMachineKey, Entry>
        get() = types.keys.associateWith(::createEntry)

    /**
     * 登録された[HTMachineKey]に紐づいたブロックの一覧
     */
    val blocks: Collection<HTMachineBlock>
        get() = blockMap.values

    /**
     * 指定された[key]が登録されているか判定します。
     */
    operator fun contains(key: HTMachineKey): Boolean = key in types

    /**
     * 指定された[key]に紐づいたブロックを返します。
     *
     * @return 値がない場合はnull
     */
    fun getBlock(key: HTMachineKey): HTMachineBlock? = blockMap[key]

    private fun createEntry(key: HTMachineKey): Entry = Entry(
        types[key] ?: error("Unknown machine key: $key"),
        blockMap[key] ?: error("Unknown machine key: $key"),
        properties.getOrDefault(key, HTPropertyHolder.Empty),
    )

    /**
     * 指定された[key]に紐づいた[Entry]を返します。
     * @throws IllegalStateException [key]が登録されていない場合
     */
    fun getEntry(key: HTMachineKey): Entry = getEntryOrNull(key) ?: error("Unknown machine key: $key")

    /**
     * 指定された[key]に紐づいた[Entry]を返します。
     * @return [key]が登録されていない場合はnull
     */
    fun getEntryOrNull(key: HTMachineKey): Entry? = entryMap[key]

    //    Keyable    //

    override fun <T : Any> keys(ops: DynamicOps<T>): Stream<T> = keys
        .stream()
        .map(HTMachineKey::id)
        .map(Identifier::toString)
        .map(ops::createString)

    //    Entry    //

    /**
     * 機械の情報をまとめたクラス
     */
    data class Entry(val type: HTMachineType, val block: HTMachineBlock, val property: HTPropertyHolder) :
        HTPropertyHolder by property,
        ItemConvertible by block
}
