package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.storage.item.HTItemStorageStack
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.common.extensions.IItemStackExtension
import net.neoforged.neoforge.common.extensions.ILevelExtension

/**
 * @see [mekanism.common.capabilities.IMultiTypeCapability]
 */
interface HTMultiCapability<HANDLER : Any, ITEM_HANDLER : HANDLER, SLOTTED_HANDLER : HANDLER, SLOT : Any> {
    fun blockCapability(): BlockCapability<HANDLER, Direction?>

    fun entityCapability(): EntityCapability<HANDLER, Direction?>

    fun itemCapability(): ItemCapability<ITEM_HANDLER, Void?>

    //    Block    //

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getCapability(level: ILevelExtension, pos: BlockPos, side: Direction?): HANDLER? = level.getCapability(blockCapability(), pos, side)

    fun getSlottedCapability(level: ILevelExtension, pos: BlockPos, side: Direction?): SLOTTED_HANDLER?

    fun getCapabilitySlots(level: ILevelExtension, pos: BlockPos, side: Direction?): List<SLOT>

    fun getCapabilitySlot(
        level: ILevelExtension,
        pos: BlockPos,
        side: Direction?,
        index: Int,
    ): SLOT? = getCapabilitySlots(level, pos, side).getOrNull(index)

    //    Entity    //

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getCapability(entity: Entity, side: Direction?): HANDLER? = entity.getCapability(entityCapability(), side)

    //    Item    //

    fun getCapability(stack: IItemStackExtension): ITEM_HANDLER? = stack.getCapability(itemCapability())

    fun getSlottedCapability(stack: IItemStackExtension): SLOTTED_HANDLER?

    fun getCapabilitySlots(stack: IItemStackExtension): List<SLOT>

    fun getCapabilitySlot(stack: IItemStackExtension, index: Int): SLOT? = getCapabilitySlots(stack).getOrNull(index)

    fun hasCapability(stack: IItemStackExtension): Boolean = getCapability(stack) != null

    // HTItemStorageStack
    fun getCapability(stack: HTItemStorageStack): ITEM_HANDLER? = getCapability(stack.stack)

    fun getSlottedCapability(stack: HTItemStorageStack): SLOTTED_HANDLER? = getSlottedCapability(stack.stack)

    fun getCapabilitySlots(stack: HTItemStorageStack): List<SLOT> = getCapabilitySlots(stack.stack)

    fun getCapabilitySlot(stack: HTItemStorageStack, index: Int): SLOT? = getCapabilitySlot(stack.stack, index)
}
