package hiiragi283.ragium.api.machine

import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Keyable
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.property.HTPropertyHolder
import net.minecraft.world.item.ItemStack
import java.util.stream.Stream

interface HTMachineRegistry : Keyable {
    /**
     * 登録された[HTMachineKey]の一覧
     */
    val keys: Set<HTMachineKey>

    /**
     * 登録された[HTMachineKey]とその[Entry]のマップ
     */
    val entryMap: Map<HTMachineKey, Entry>

    /**
     * 登録された[HTMachineKey]に紐づいたブロックの一覧
     */
    val blocks: Collection<HTBlockContent>
        get() = entryMap.values

    /**
     * 指定された[key]が登録されているか判定します。
     */
    operator fun contains(key: HTMachineKey): Boolean = key in keys

    /**
     * 指定された[key]に紐づいたブロックを返します。
     *
     * @return 値がない場合はnull
     */
    fun getBlock(key: HTMachineKey): HTBlockContent?

    /**
     * 指定された[key]に紐づいた[Entry]を返します。
     * @return [key]が登録されていない場合はnull
     */
    fun getEntryData(key: HTMachineKey): DataResult<Entry>

    /**
     * 指定された[key]に紐づいた[Entry]を返します。
     * @throws IllegalStateException [key]が登録されていない場合
     */
    fun getEntry(key: HTMachineKey): Entry = getEntryData(key).orThrow

    /**
     * 指定された[key]に紐づいた[Entry]を返します。
     * @return [key]が登録されていない場合はnull
     */
    fun getEntryOrNull(key: HTMachineKey): Entry? = getEntryData(key).getOrNull()

    //    Keyable    //

    override fun <T : Any> keys(ops: DynamicOps<T>): Stream<T> = keys
        .stream()
        .map(HTMachineKey::name)
        .map(ops::createString)

    //    Entry    //

    /**
     * 機械の情報をまとめたクラス
     */
    interface Entry :
        HTPropertyHolder,
        HTBlockContent {
        val type: HTMachineType

        fun createItemStack(tier: HTMachineTier): ItemStack?
    }
}
