package hiiragi283.ragium.api.machine

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.extension.toDataResult
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.common.internal.HTMachineRegistryImpl
import net.neoforged.neoforge.registries.DeferredBlock

interface HTMachineRegistry {
    fun forEachEntries(action: (HTMachineKey, DeferredBlock<*>?, HTPropertyHolder) -> Unit) {
        HTMachineKey.allKeys.forEach { key: HTMachineKey ->
            action(key, getBlockOrNull(key), HTMachineRegistryImpl.getProperty(key))
        }
    }

    //    Block    //

    /**
     * 登録された[HTMachineKey]とその[DeferredBlock]のマップ
     */
    val blockMap: Map<HTMachineKey, DeferredBlock<*>>

    val blocks: Collection<DeferredBlock<*>>
        get() = blockMap.values

    /**
     * 指定された[key]に紐づいたブロックを返します。
     */
    fun getBlockData(key: HTMachineKey): DataResult<DeferredBlock<*>> =
        blockMap[key].toDataResult { "Machine key: $key is not bound to any block!" }

    /**
     * 指定された[key]に紐づいたブロックを返します。
     * @throws IllegalStateException 指定した[key]にブロックが登録されていない場合
     */
    fun getBlock(key: HTMachineKey): DeferredBlock<*> = getBlockData(key).orThrow

    /**
     * 指定された[key]に紐づいたブロックを返します。
     * @return 値がない場合はnull
     */
    fun getBlockOrNull(key: HTMachineKey): DeferredBlock<*>? = getBlockData(key).getOrNull()

    //    Property    //

    /**
     * 指定した[key]に基づいて[HTPropertyHolder]を返します。
     */
    fun getProperty(key: HTMachineKey): HTPropertyHolder
}
