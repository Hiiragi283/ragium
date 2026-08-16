package hiiragi283.lib.transfer.proxy

import hiiragi283.lib.transfer.HTResourceSlot
import hiiragi283.lib.transfer.holder.HTCapabilityHolder
import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * [HTResourceSlot]向けの[HTProxyHandler]の拡張クラスです。
 *
 * 参照 : [Mekanism - ProxyResourceHandler](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/capabilities/proxy/ProxyResourceHandler.java)
 * @param RESOURCE 保持するリソースのクラス
 * @param side 現在の向き
 * @param holder 搬入出の制御
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTProxyResourceHandler<RESOURCE : Resource>(private val handler: ResourceHandler<RESOURCE>, side: Direction?, holder: HTCapabilityHolder?) :
    HTProxyHandler(side, holder),
    ResourceHandler<RESOURCE> {
    override fun size(): Int = handler.size()

    override fun getResource(index: Int): RESOURCE = handler.getResource(index)

    override fun getAmountAsLong(index: Int): Long = handler.getAmountAsLong(index)

    override fun getCapacityAsLong(index: Int, resource: RESOURCE): Long = handler.getCapacityAsLong(index, resource)

    override fun isValid(index: Int, resource: RESOURCE): Boolean = handler.isValid(index, resource)

    override fun insert(index: Int, resource: RESOURCE, amount: Int, transaction: TransactionContext): Int = when {
        readOnlyInsert -> 0
        else -> handler.insert(index, resource, amount, transaction)
    }

    override fun insert(resource: RESOURCE, amount: Int, transaction: TransactionContext): Int = when {
        readOnlyInsert -> 0
        else -> handler.insert(resource, amount, transaction)
    }

    override fun extract(index: Int, resource: RESOURCE, amount: Int, transaction: TransactionContext): Int = when {
        readOnlyExtract -> 0
        else -> handler.extract(index, resource, amount, transaction)
    }

    override fun extract(resource: RESOURCE, amount: Int, transaction: TransactionContext): Int = when {
        readOnlyExtract -> 0
        else -> handler.extract(resource, amount, transaction)
    }
}
