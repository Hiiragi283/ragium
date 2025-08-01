package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.extension.getRangedAABB
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.inventory.HTItemCollectorMenu
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper

class HTItemBufferBlockEntity(pos: BlockPos, state: BlockState) : HTDeviceBlockEntity(RagiumBlockEntityTypes.ITEM_BUFFER, pos, state) {
    private val inventory = HTItemStackHandler(9, this::setChanged)

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

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        ItemHandlerHelper.calcRedstoneFromInventory(inventory)

    //    Ticking    //

    override fun serverTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        // アップグレードにマグネットが入っている場合のみ機能する
        if (!upgrades.hasStack { stack: ItemStack -> stack.`is`(RagiumItems.RAGI_MAGNET) }) return TriState.FALSE
        // 範囲内のItem Entityを取得する
        val itemEntities: List<ItemEntity> = level.getEntitiesOfClass(
            ItemEntity::class.java,
            blockPos.getRangedAABB(RagiumConfig.COMMON.entityCollectorRange.get()),
        )
        if (itemEntities.isEmpty()) return TriState.DEFAULT
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
        return TriState.TRUE
    }

    override fun getItemHandler(direction: Direction?): IItemHandler? = inventory

    //    Menu    //

    override val containerData: ContainerData = createData()

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTItemCollectorMenu =
        HTItemCollectorMenu(containerId, playerInventory, blockPos, createDefinition(inventory))
}
