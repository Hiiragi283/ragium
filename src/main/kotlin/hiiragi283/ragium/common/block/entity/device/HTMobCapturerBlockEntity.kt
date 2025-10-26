package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.extension.getRangedAABB
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.isOf
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.insertItem
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.common.entity.HTThrownCaptureEgg
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState

class HTMobCapturerBlockEntity(pos: BlockPos, state: BlockState) : HTDeviceBlockEntity.Tickable(RagiumBlocks.MOB_CAPTURER, pos, state) {
    private lateinit var inputSlot: HTItemSlot.Mutable
    private lateinit var outputSlots: List<HTItemSlot>

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(null)
        inputSlot = builder.addSlot(
            HTAccessConfig.INPUT_ONLY,
            HTItemStackSlot.input(
                listener,
                HTSlotHelper.getSlotPosX(2),
                HTSlotHelper.getSlotPosY(1),
                filter = { stack: ImmutableItemStack -> stack.isOf(RagiumItems.ELDRITCH_EGG) },
            ),
        )
        outputSlots = (0..<9).map { i: Int ->
            builder.addSlot(
                HTAccessConfig.OUTPUT_ONLY,
                HTOutputItemStackSlot.create(
                    listener,
                    HTSlotHelper.getSlotPosX(4 + i % 3),
                    HTSlotHelper.getSlotPosY(i / 3),
                ),
            )
        }
        return builder.build()
    }

    //    Ticking    //

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 範囲内のエンティティを取得する
        val entities: List<LivingEntity> = level.getEntitiesOfClass(
            LivingEntity::class.java,
            pos.center.getRangedAABB(RagiumConfig.COMMON.deviceCollectorEntityRange.asDouble),
        )
        if (entities.isEmpty()) return false
        // それぞれのエンティティについて捕獲を行う
        for (entity: LivingEntity in entities) {
            val eggStack: ItemStack = HTThrownCaptureEgg.getCapturedStack(entity) ?: continue
            for (slot: HTItemSlot in outputSlots) {
                if (slot.insertItem(eggStack, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL).isEmpty) {
                    // スポーンエッグをスロットに入れる
                    slot.insertItem(eggStack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                    // 対象を消す
                    entity.discard()
                    // Capture Eggを減らす
                    inputSlot.shrinkStack(1, HTStorageAction.EXECUTE)
                    return true
                }
            }
        }
        return false
    }
}
