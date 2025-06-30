package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.block.entity.HTTickAwareBlockEntity
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper

class HTItemCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTTickAwareBlockEntity(RagiumBlockEntityTypes.ITEM_COLLECTOR, pos, state) {
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

    override fun onServerTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        // 20 tickごとに実行する
        if (!canProcess()) return TriState.DEFAULT
        // 自動搬出する
        exportItems(level, pos)
        // 範囲内のItem Entityを取得する
        val range: Int = RagiumConfig.COMMON.entityCollectorRange.get()
        val itemEntities: List<ItemEntity> = level.getEntitiesOfClass(
            ItemEntity::class.java,
            AABB.of(
                BoundingBox(
                    blockPos.x - range,
                    blockPos.y - range,
                    blockPos.z - range,
                    blockPos.x + range,
                    blockPos.y + range,
                    blockPos.z + range,
                ),
            ),
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

    override val maxTicks: Int = 20

    override fun getItemHandler(direction: Direction?): IItemHandler? = HTFilteredItemHandler(inventory, HTItemFilter.EXTRACT_ONLY)
}
