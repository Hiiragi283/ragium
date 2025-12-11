package hiiragi283.ragium.api.capability

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.items.IItemHandler

object HTItemCapabilities : HTMultiCapability.Simple<IItemHandler> {
    override val block: BlockCapability<IItemHandler, Direction?> = Capabilities.ItemHandler.BLOCK
    override val entity: EntityCapability<IItemHandler, Direction?> = Capabilities.ItemHandler.ENTITY_AUTOMATION
    override val item: ItemCapability<IItemHandler, Void?> = Capabilities.ItemHandler.ITEM

    override fun getCapability(entity: Entity, side: Direction?): IItemHandler? {
        if (side == null) {
            val handler: IItemHandler? = entity.getCapability(entityAlt)
            if (handler != null) return handler
        }
        return super.getCapability(entity, side)
    }

    val entityAlt: EntityCapability<IItemHandler, Void?> = Capabilities.ItemHandler.ENTITY

    fun wrapHandler(handler: IItemHandler): HTItemHandler = when (handler) {
        is HTItemHandler -> handler
        else -> HTItemHandler {
            handler.slotRange.map { slot: Int ->
                object : HTItemSlot, HTContentListener.Empty, HTValueSerializable.Empty {
                    override fun isValid(stack: ImmutableItemStack): Boolean = handler.isItemValid(slot, stack.unwrap())

                    override fun insert(
                        stack: ImmutableItemStack?,
                        action: HTStorageAction,
                        access: HTStorageAccess,
                    ): ImmutableItemStack? = handler.insertItem(slot, stack?.unwrap() ?: ItemStack.EMPTY, action.simulate).toImmutable()

                    override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): ImmutableItemStack? =
                        handler.extractItem(slot, amount, action.simulate).toImmutable()

                    override fun getStack(): ImmutableItemStack? = handler.getStackInSlot(slot).toImmutable()

                    override fun getCapacity(stack: ImmutableItemStack?): Int = handler.getSlotLimit(slot)
                }
            }
        }
    }

    //    Block    //

    fun getItemHandler(level: Level, pos: BlockPos, side: Direction?): HTItemHandler? = getCapability(level, pos, side)?.let(::wrapHandler)

    /**
     * 指定した引数から[HTItemSlot]の一覧を返します。
     * @return [HTItemSlot]の[List]
     */
    fun getItemSlots(level: Level, pos: BlockPos, side: Direction?): List<HTItemSlot> =
        getItemHandler(level, pos, side)?.getItemSlots(side) ?: listOf()

    fun getItemSlot(
        level: Level,
        pos: BlockPos,
        side: Direction?,
        slot: Int,
    ): HTItemSlot? = getItemSlots(level, pos, side).getOrNull(slot)

    //    Entity    //

    fun getItemHandler(entity: Entity, side: Direction?): HTItemHandler? = getCapability(entity, side)?.let(::wrapHandler)

    fun getItemSlots(entity: Entity, side: Direction?): List<HTItemSlot> = getItemHandler(entity, side)?.getItemSlots(side) ?: listOf()

    fun getItemSlot(entity: Entity, side: Direction?, slot: Int): HTItemSlot? = getItemSlots(entity, side).getOrNull(slot)

    //    Item    //

    fun getItemHandler(stack: ItemStack): HTItemHandler? = getCapability(stack)?.let(::wrapHandler)

    /**
     * 指定した引数から[HTItemSlot]の一覧を返します。
     * @return [HTItemSlot]の[List]
     */
    fun getItemSlots(stack: ItemStack): List<HTItemSlot> = getItemHandler(stack)?.getItemSlots(null) ?: listOf()

    /**
     * 指定した引数から[index]に対応する[HTItemSlot]を返します。
     * @return 見つからない場合は`null`
     */
    fun getItemSlot(stack: ItemStack, index: Int): HTItemSlot? = getItemSlots(stack).getOrNull(index)

    // HTItemStorageStack

    fun getItemHandler(stack: ImmutableItemStack?): HTItemHandler? = getCapability(stack)?.let(::wrapHandler)

    fun getItemSlots(stack: ImmutableItemStack?): List<HTItemSlot> = getItemHandler(stack)?.getItemSlots(null) ?: listOf()

    fun getItemSlot(stack: ImmutableItemStack?, index: Int): HTItemSlot? = getItemSlots(stack).getOrNull(index)
}
