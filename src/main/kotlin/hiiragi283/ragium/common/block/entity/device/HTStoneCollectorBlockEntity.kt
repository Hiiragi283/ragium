package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemSlot
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.math.Fraction

class HTStoneCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity.Tickable(RagiumBlocks.STONE_COLLECTOR, pos, state) {
    lateinit var outputSlot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        outputSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemSlot.create(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1)),
        )
    }

    //    Ticking    //

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        if (outputSlot.getNeeded() == 0) return false
        // 周囲に1つ以上の水と溶岩があることを確認
        val directions: List<Direction> = Direction.entries.filterNot { it.axis == Direction.Axis.Y }
        val waterPos: BlockPos = directions
            .map(pos::relative)
            .firstOrNull { posIn: BlockPos -> level.getBlockState(posIn).`is`(Blocks.WATER) }
            ?: return false
        val lavaPos: BlockPos = directions
            .map(pos::relative)
            .firstOrNull { posIn: BlockPos -> level.getBlockState(posIn).`is`(Blocks.LAVA) }
            ?: return false
        // 岩石生成のデータを取得する
        val belowState: BlockState = level.getBlockState(pos.below())
        val (waterChance: Fraction, lavaChance: Fraction) = RagiumDataMapTypes.getRockData(belowState) ?: return false
        val random: RandomSource = level.random
        if (random.nextFloat() <= waterChance.toFloat()) {
            level.setBlockAndUpdate(waterPos, Blocks.AIR.defaultBlockState())
        }
        if (random.nextFloat() <= lavaChance.toFloat()) {
            level.setBlockAndUpdate(lavaPos, Blocks.AIR.defaultBlockState())
        }
        // 実際にアウトプットに搬出する
        val resultStack: ImmutableItemStack = ImmutableItemStack.ofNullable(belowState.block) ?: return false
        outputSlot.insert(resultStack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 0.5f, 1f)
        return true
    }
}
