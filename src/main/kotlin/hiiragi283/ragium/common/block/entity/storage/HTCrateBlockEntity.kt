package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.block.entity.HTBlockInteractContext
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.maxStackSize
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTPlayerHandSlot
import hiiragi283.ragium.common.storage.item.slot.HTVariableItemStackSlot
import hiiragi283.ragium.common.util.HTItemHelper
import hiiragi283.ragium.common.variant.HTCrateVariant
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentMap
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

abstract class HTCrateBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(type, pos, state) {
    constructor(variant: HTCrateVariant, pos: BlockPos, state: BlockState) : this(variant.blockEntityHolder, pos, state)

    private lateinit var slot: HTItemSlot.Mutable

    final override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        slot = HTVariableItemStackSlot.create(listener, { stack: ImmutableItemStack ->
            val capacity: Long = stack.maxStackSize() * getDefaultSlotMultiplier()
            HTItemHelper.processStorageCapacity(level?.random, this, capacity)
        }, 0, 0)
        return HTSimpleItemSlotHolder(null, listOf(), listOf(), slot)
    }

    protected abstract fun getDefaultSlotMultiplier(): Long

    override fun onRightClickedWithItem(context: HTBlockInteractContext, stack: ItemStack, hand: InteractionHand): ItemInteractionResult {
        val result: ItemInteractionResult = super.onRightClickedWithItem(context, stack, hand)
        if (result.consumesAction()) return result
        val level: Level = context.level
        val handSlot = HTPlayerHandSlot(context.player, hand)
        // プレイヤーがアイテムを持っている場合
        if (!handSlot.isEmpty()) {
            val stackInHand: ImmutableItemStack = handSlot.getStack()
            val remainder: ImmutableItemStack = slot.insert(stackInHand, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
            if (remainder != stackInHand) {
                slot.insert(stackInHand, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                return ItemInteractionResult.sidedSuccess(level.isClientSide)
            }
        } else {
            // プレイヤーがアイテムを持っていない場合
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    //    Save & Read    //

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        componentInput.get(RagiumDataComponents.ITEM_CONTENT)?.let(slot::setStack)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(RagiumDataComponents.ITEM_CONTENT, slot.getStack())
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = false

    //    Impl    //

    class Small(pos: BlockPos, state: BlockState) : HTCrateBlockEntity(HTCrateVariant.SMALL, pos, state) {
        override fun getDefaultSlotMultiplier(): Long = HTCrateVariant.SMALL.multiplier
    }

    class Medium(pos: BlockPos, state: BlockState) : HTCrateBlockEntity(HTCrateVariant.MEDIUM, pos, state) {
        override fun getDefaultSlotMultiplier(): Long = HTCrateVariant.MEDIUM.multiplier
    }

    class Large(pos: BlockPos, state: BlockState) : HTCrateBlockEntity(HTCrateVariant.LARGE, pos, state) {
        override fun getDefaultSlotMultiplier(): Long = HTCrateVariant.LARGE.multiplier
    }

    class Huge(pos: BlockPos, state: BlockState) : HTCrateBlockEntity(HTCrateVariant.HUGE, pos, state) {
        override fun getDefaultSlotMultiplier(): Long = HTCrateVariant.HUGE.multiplier
    }
}
