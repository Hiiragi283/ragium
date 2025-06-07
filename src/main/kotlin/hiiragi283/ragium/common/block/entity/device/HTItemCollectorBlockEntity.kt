package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.block.entity.HTTickAwareBlockEntity
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.forEachStacks
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.util.RagiumConstantValues
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
import net.neoforged.neoforge.items.ItemStackHandler

class HTItemCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTTickAwareBlockEntity(RagiumBlockEntityTypes.ITEM_COLLECTOR, pos, state) {
    private val outputs = ItemStackHandler(9)

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConstantValues.OUTPUT_SLOT, outputs)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConstantValues.OUTPUT_SLOT, outputs)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        super.onRemove(state, level, pos, newState, movedByPiston)
        outputs.forEachStacks { stack: ItemStack -> dropStackAt(level, pos, stack) }
    }

    //    Ticking    //

    override fun onServerTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        // 20 tickごとに実行する
        if (!canProcess()) return TriState.DEFAULT
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
            val stackIn: ItemStack = entity.item.copy()
            val remainStack: ItemStack = ItemHandlerHelper.insertItem(outputs, stackIn, false)
            if (!remainStack.isEmpty) {
                entity.item.count == remainStack.count
            }
        }
        return TriState.TRUE
    }

    override val maxTicks: Int = 20

    override fun getItemHandler(direction: Direction?): IItemHandler? = outputs
}
