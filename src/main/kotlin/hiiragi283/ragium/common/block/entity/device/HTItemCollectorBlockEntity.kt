package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.extension.getRangedAABB
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.inventory.HTItemCollectorMenu
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
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

class HTItemCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity(RagiumBlockEntityTypes.ITEM_COLLECTOR, pos, state) {
    private val inventory = HTItemStackHandler(9, this::setChanged)

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConstantValues.INVENTORY, inventory)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConstantValues.INVENTORY, inventory)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        super.onRemove(state, level, pos, newState, movedByPiston)
        inventory.dropStacksAt(level, pos)
    }

    //    Ticking    //

    override fun serverTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
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

    override fun getItemHandler(direction: Direction?): IItemHandler? = HTFilteredItemHandler(inventory, HTItemFilter.EXTRACT_ONLY)

    //    Menu    //

    override val containerData: ContainerData = createData()

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTItemCollectorMenu =
        HTItemCollectorMenu(containerId, playerInventory, blockPos, createDefinition(inventory))
}
