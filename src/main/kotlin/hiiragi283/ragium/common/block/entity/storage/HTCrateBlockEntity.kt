package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.block.attribute.getAttributeTier
import hiiragi283.ragium.api.block.entity.HTBlockInteractContext
import hiiragi283.ragium.api.extension.giveStackTo
import hiiragi283.ragium.api.item.component.HTItemContents
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
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.util.HTItemHelper
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage
import java.util.function.Consumer

class HTCrateBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(blockHolder, pos, state) {
    private lateinit var tier: HTCrateTier

    override fun initializeVariables() {
        tier = blockHolder.getAttributeTier()
    }

    private lateinit var slot: HTItemSlot.Mutable

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        slot = builder.addSlot(
            HTAccessConfig.BOTH,
            HTVariableItemStackSlot.create(listener, { stack: ImmutableItemStack ->
                val capacity: Int = HTItemSlot.getMaxStackSize(stack) * tier.getMultiplier()
                HTItemHelper.processStorageCapacity(level?.random, this, capacity)
            }, 0, 0),
        )
        return builder.build()
    }

    override fun onRightClickedWithItem(context: HTBlockInteractContext, stack: ItemStack, hand: InteractionHand): ItemInteractionResult {
        val result: ItemInteractionResult = super.onRightClickedWithItem(context, stack, hand)
        if (result.consumesAction()) return result
        val level: Level = context.level
        val handSlot = HTPlayerHandSlot(context.player, hand)
        // プレイヤーがアイテムを持っている場合
        val stackInHand: ImmutableItemStack? = handSlot.getStack()
        if (stackInHand != null) {
            if (!level.isClientSide) {
                var remainder: ImmutableItemStack? = slot.insert(stackInHand, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
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
        val toExtract: Int = if (player.isShiftKeyDown) {
            slot.getStack()?.maxStackSize() ?: return
        } else {
            1
        }
        val extracted: ImmutableItemStack? = slot.extract(toExtract, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        giveStackTo(player, extracted?.stack)
    }

    override fun dropInventory(consumer: Consumer<ItemStack>) {}

    //    Save & Read    //

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        componentInput.get(RagiumDataComponents.ITEM_CONTENT)?.getOrNull(0)?.let(slot::setStack)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(RagiumDataComponents.ITEM_CONTENT, HTItemContents.of(slot.getStack()))
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = false

    override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = null
}
