package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.property.HTPropertyHolder
import net.neoforged.neoforge.registries.DeferredBlock

interface HTMachineRegistry {
    fun forEachEntries(action: (HTMachineKey, DeferredBlock<*>?, HTPropertyHolder) -> Unit) {
        HTMachineKey.allKeys.forEach { key: HTMachineKey ->
            action(key, getBlockOrNull(key), getProperty(key))
        }
    }

    //    Block    //

    /**
     * 登録された[DeferredBlock]の一覧を返します。
     */
    val blocks: Collection<DeferredBlock<*>>

    /**
     * 指定された[key]に紐づいたブロックを返します。
     * @throws IllegalStateException 指定した[key]にブロックが登録されていない場合
     */
    fun getBlock(key: HTMachineKey): DeferredBlock<*> = getBlockOrNull(key) ?: error("Machine key: $key is not bound to any block!")

    /**
     * 指定された[key]に紐づいたブロックを返します。
     * @return 値がない場合はnull
     */
    fun getBlockOrNull(key: HTMachineKey): DeferredBlock<*>?

    //    Property    //

    /**
     * 指定した[key]に基づいて[HTPropertyHolder]を返します。
     */
    fun getProperty(key: HTMachineKey): HTPropertyHolder
}
