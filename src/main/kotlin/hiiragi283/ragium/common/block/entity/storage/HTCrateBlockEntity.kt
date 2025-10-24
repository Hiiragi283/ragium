package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.block.entity.HTBlockInteractContext
import hiiragi283.ragium.api.extension.giveStackTo
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.maxStackSize
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
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
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Consumer

abstract class HTCrateBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(type, pos, state) {
    constructor(variant: HTCrateVariant, pos: BlockPos, state: BlockState) : this(variant.blockEntityHolder, pos, state)

    private lateinit var slot: HTItemSlot.Mutable

    final override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        slot = builder.addSlot(
            HTAccessConfig.BOTH,
            HTVariableItemStackSlot.create(listener, { stack: ImmutableItemStack ->
                val capacity: Int = HTItemSlot.getMaxStackSize(stack) * getDefaultSlotMultiplier()
                HTItemHelper.processStorageCapacity(level?.random, this, capacity)
            }, 0, 0),
        )
        return builder.build()
    }

    protected abstract fun getDefaultSlotMultiplier(): Int

    override fun onRightClickedWithItem(context: HTBlockInteractContext, stack: ItemStack, hand: InteractionHand): ItemInteractionResult {
        val result: ItemInteractionResult = super.onRightClickedWithItem(context, stack, hand)
        if (result.consumesAction()) return result
        val level: Level = context.level
        val handSlot = HTPlayerHandSlot(context.player, hand)
        // プレイヤーがアイテムを持っている場合
        if (handSlot.isNotEmpty()) {
            if (!level.isClientSide) {
                val stackInHand: ImmutableItemStack = handSlot.getStack()
                var remainder: ImmutableItemStack = slot.insert(stackInHand, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
                if (remainder != stackInHand) {
                    remainder = slot.insert(stackInHand, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                    handSlot.setStack(remainder)
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    override fun onLeftClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
    ) {
        super.onLeftClicked(state, level, pos, player)
        val toExtract: Int = if (player.isShiftKeyDown) slot.getStack().maxStackSize() else 1
        val extracted: ImmutableItemStack = slot.extract(toExtract, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        giveStackTo(player, extracted.stack)
    }

    override fun dropInventory(consumer: Consumer<ItemStack>) {}

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
        override fun getDefaultSlotMultiplier(): Int = HTCrateVariant.SMALL.multiplier
    }

    class Medium(pos: BlockPos, state: BlockState) : HTCrateBlockEntity(HTCrateVariant.MEDIUM, pos, state) {
        override fun getDefaultSlotMultiplier(): Int = HTCrateVariant.MEDIUM.multiplier
    }

    class Large(pos: BlockPos, state: BlockState) : HTCrateBlockEntity(HTCrateVariant.LARGE, pos, state) {
        override fun getDefaultSlotMultiplier(): Int = HTCrateVariant.LARGE.multiplier
    }

    class Huge(pos: BlockPos, state: BlockState) : HTCrateBlockEntity(HTCrateVariant.HUGE, pos, state) {
        override fun getDefaultSlotMultiplier(): Int = HTCrateVariant.HUGE.multiplier
    }
}
