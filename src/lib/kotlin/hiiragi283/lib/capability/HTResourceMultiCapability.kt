package hiiragi283.lib.capability

import hiiragi283.lib.transfer.HTResourceHandler
import hiiragi283.lib.transfer.HTResourceSlot
import hiiragi283.lib.transfer.HTTransferAccess
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * [HTResourceSlot]向けの[HTMultiCapability]の拡張インターフェースです。
 * @param T 保持するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTResourceMultiCapability<T : Resource> : HTMultiCapability<ResourceHandler<T>> {
    /**
     * 指定された[handler]を[HTResourceSlot]の一覧に展開します。
     */
    fun unwrapSlots(handler: ResourceHandler<T>): List<HTResourceSlot<T>> = when (handler) {
        is HTResourceHandler<T> -> handler.slots

        else -> List(handler.size()) { index: Int ->
            object : HTResourceSlot<T> {
                override fun isValid(resource: T): Boolean = handler.isValid(index, resource)

                override fun insert(
                    resource: T,
                    amount: Int,
                    transaction: TransactionContext,
                    access: HTTransferAccess
                ): Int = handler.insert(index, resource, amount, transaction)

                override fun extract(
                    resource: T,
                    amount: Int,
                    transaction: TransactionContext,
                    access: HTTransferAccess
                ): Int = handler.extract(index, resource, amount, transaction)

                override val resource: T get() = handler.getResource(index)
                override val amount: Int get() = handler.getAmountAsInt(index)

                override fun getCapacity(resource: T): Int = handler.getCapacityAsInt(index, resource)

                override fun serialize(output: ValueOutput): Unit = Unit

                override fun deserialize(input: ValueInput): Unit = Unit
            }
        }
    }

    //    Block    //

    /**
     * 指定した引数から[HTResourceSlot]の一覧を返します。
     */
    fun getSlots(level: Level, pos: BlockPos, side: Direction?): List<HTResourceSlot<T>> =
        getCapability(level, pos, side)?.let(::unwrapSlots) ?: listOf()

    /**
     * 指定した引数から[HTResourceSlot]を返します。
     * @return [index]に対応するスロットがない場合は`null`
     */
    fun getSlot(level: Level, pos: BlockPos, side: Direction?, index: Int): HTResourceSlot<T>? =
        getSlots(level, pos, side).getOrNull(index)

    //    Entity    //

    /**
     * 指定した引数から[HTResourceSlot]の一覧を返します。
     */
    fun getSlots(entity: Entity, side: Direction?): List<HTResourceSlot<T>> =
        getCapability(entity, side)?.let(::unwrapSlots) ?: listOf()

    /**
     * 指定した引数から[HTResourceSlot]を返します。
     * @return [index]に対応するスロットがない場合は`null`
     */
    fun getSlot(entity: Entity, side: Direction?, index: Int): HTResourceSlot<T>? =
        getSlots(entity, side).getOrNull(index)

    //    Item    //

    /**
     * 指定した引数から[HTResourceSlot]の一覧を返します。
     */
    fun getSlots(access: ItemAccess): List<HTResourceSlot<T>> = getCapability(access)?.let(::unwrapSlots) ?: listOf()

    /**
     * 指定した引数から[HTResourceSlot]を返します。
     * @return [index]に対応するスロットがない場合は`null`
     */
    fun getSlot(access: ItemAccess, index: Int): HTResourceSlot<T>? = getSlots(access).getOrNull(index)

    /**
     * 指定した引数から[HTResourceSlot]の一覧を返します。
     */
    fun getSlots(stack: ItemStack): List<HTResourceSlot<T>> = getCapability(stack)?.let(::unwrapSlots) ?: listOf()

    /**
     * 指定した引数から[HTResourceSlot]を返します。
     * @return [index]に対応するスロットがない場合は`null`
     */
    fun getSlot(stack: ItemStack, index: Int): HTResourceSlot<T>? = getSlots(stack).getOrNull(index)
}
