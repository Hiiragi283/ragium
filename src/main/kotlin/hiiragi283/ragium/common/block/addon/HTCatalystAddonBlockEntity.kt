package hiiragi283.ragium.common.block.addon

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTBlockEntityHandlerProvider
import hiiragi283.ragium.api.capability.LimitedItemHandler
import hiiragi283.ragium.api.extension.dropStacks
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.items.ItemHandlerHelper
import net.neoforged.neoforge.items.ItemStackHandler

class HTCatalystAddonBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntity(RagiumBlockEntityTypes.CATALYST_ADDON, pos, state),
    HTBlockEntityHandlerProvider {
    private val itemHandler: ItemStackHandler = object : ItemStackHandler(1) {
        override fun getSlotLimit(slot: Int): Int = 1
    }

    val catalystStack: ItemStack
        get() = itemHandler.getStackInSlot(0)

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.put(ITEM_KEY, itemHandler.serializeNBT(registries))
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        itemHandler.deserializeNBT(registries, tag.getCompound(ITEM_KEY))
    }

    override fun onRightClickedWithItem(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult {
        if (stack.isEmpty) {
            // drop catalyst
            ItemHandlerHelper.giveItemToPlayer(player, itemHandler.getStackInSlot(0))
            itemHandler.setStackInSlot(0, ItemStack.EMPTY)
        } else {
            // insert catalyst
            val stackIn: ItemStack = itemHandler.getStackInSlot(0)
            if (stackIn.isEmpty) {
                itemHandler.setStackInSlot(0, stack.copy())
                stack.count = 0
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        itemHandler.dropStacks(level, pos)
    }

    //    HTBlockEntityHandlerProvider    //

    override fun getItemHandler(direction: Direction?): LimitedItemHandler = LimitedItemHandler.dummy(itemHandler)
}
