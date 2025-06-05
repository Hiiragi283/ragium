package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.block.entity.HTTickAwareBlockEntity
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.common.util.TriState

class HTItemCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTTickAwareBlockEntity(RagiumBlockEntityTypes.ITEM_COLLECTOR, pos, state),
    HTItemSlotHandler {
    private val itemSlots: List<HTItemSlot> = (0..8).map { HTItemSlot.create("output_slot_$it", this) }

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        for (slot: HTItemSlot in itemSlots) {
            slot.writeNbt(nbt, registryOps)
        }
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        for (slot: HTItemSlot in itemSlots) {
            slot.readNbt(nbt, registryOps)
        }
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        super.onRemove(state, level, pos, newState, movedByPiston)
        for (slot: HTItemSlot in itemSlots) {
            slot.dropStack(level, pos)
        }
    }

    //    Ticking    //

    override fun onServerTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        // 20 tickごとに実行する
        if (!canProcess(20)) return TriState.DEFAULT
        // 範囲内のItem Entityを取得する
        val range = 5
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
            for (slot: HTItemSlot in itemSlots) {
                val stackIn: ItemStack = entity.item.copy()
                slot.insert(stackIn, false)
                // すべて搬入出来たらItem Entityを無効化する
                if (stackIn.isEmpty) {
                    entity.discard()
                    break
                } else {
                    // そうでなければ個数を反映させる
                    entity.item.count == stackIn.count
                }
            }
        }
        return TriState.TRUE
    }

    //    Item    //

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = if (slot in itemSlots.indices) HTStorageIO.GENERIC else HTStorageIO.EMPTY

    override fun getItemSlot(slot: Int): HTItemSlot? = itemSlots.getOrNull(slot)

    override fun getSlots(): Int = itemSlots.size
}
