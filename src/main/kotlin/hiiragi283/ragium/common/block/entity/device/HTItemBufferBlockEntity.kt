package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getRangedAABB
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTDeviceVariant
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper

class HTItemBufferBlockEntity(pos: BlockPos, state: BlockState) : HTDeviceBlockEntity(HTDeviceVariant.ITEM_BUFFER, pos, state) {
    private val inventory: HTItemStackHandler = HTItemStackHandler.Builder(9).addInput(0..8).build(this)

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConst.INVENTORY, inventory)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConst.INVENTORY, inventory)
    }

    override fun dropInventory(consumer: (ItemStack) -> Unit) {
        super.dropInventory(consumer)
        inventory.getStackView().forEach(consumer)
    }

    override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult {
        if (isRemote) {
            RagiumMenuTypes.ITEM_BUFFER.openMenu(
                player,
                state.block.name,
                this,
                ::writeExtraContainerData,
            )
        }

        return InteractionResult.sidedSuccess(isRemote)
    }

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        ItemHandlerHelper.calcRedstoneFromInventory(inventory)

    //    Ticking    //

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // アップグレードにマグネットが入っている場合のみ機能する
        // if (!upgrades.hasStack { stack: ItemStack -> stack.`is`(RagiumItems.RAGI_MAGNET) }) return TriState.FALSE
        // 範囲内のItem Entityを取得する
        val itemEntities: List<ItemEntity> = level.getEntitiesOfClass(
            ItemEntity::class.java,
            blockPos.getRangedAABB(RagiumAPI.getConfig().getEntityCollectorRange()),
        )
        if (itemEntities.isEmpty()) return false
        // それぞれのItem Entityに対して回収を行う
        for (entity: ItemEntity in itemEntities) {
            // IEのコンベヤ上にいるアイテムは無視する
            if (entity.persistentData.getBoolean("PreventRemoteMovement")) continue
            // 回収までのディレイが残っている場合はスキップ
            if (entity.hasPickUpDelay()) continue
            // 各スロットに対して搬入操作を行う
            val stackIn: ItemStack = entity.item.copy()
            val remainStack: ItemStack = ItemHandlerHelper.insertItem(inventory, stackIn, false)
            if (remainStack.isEmpty) {
                entity.discard()
            } else {
                entity.item.count = remainStack.count
            }
        }
        return true
    }

    override fun getItemHandler(direction: Direction?): HTItemStackHandler = inventory

    //    HTSlotProvider    //

    override fun addInputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        for (index: Int in (0..8)) {
            consumer(
                inventory,
                index,
                HTSlotHelper.getSlotPosX(3 + index % 3),
                HTSlotHelper.getSlotPosY(index / 3),
            )
        }
    }

    override fun addOutputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
    }
}
