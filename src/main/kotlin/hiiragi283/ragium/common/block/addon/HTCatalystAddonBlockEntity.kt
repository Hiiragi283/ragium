package hiiragi283.ragium.common.block.addon

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTBlockEntityHandlerProvider
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.extension.dropStacks
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.ItemHandlerHelper

class HTCatalystAddonBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntity(RagiumBlockEntityTypes.CATALYST_ADDON, pos, state),
    HTBlockEntityHandlerProvider {
    private val itemHandler: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)

    val catalystStack: ItemStack
        get() = itemHandler.getStackInSlot(0)

    private val serializer: HTHandlerSerializer = HTHandlerSerializer.ofItem(listOf(itemHandler.createSlot(0)))

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        serializer.writeNbt(nbt, dynamicOps)
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        serializer.readNbt(nbt, dynamicOps)
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

    override fun getItemHandler(direction: Direction?): IItemHandlerModifiable = HTStorageIO.INTERNAL.wrapItemHandler(itemHandler)
}
