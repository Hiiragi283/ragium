package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.world.getRangedAABB
import hiiragi283.ragium.common.entity.HTThrownCaptureEgg
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.block.state.BlockState

class HTMobCapturerBlockEntity(pos: BlockPos, state: BlockState) : HTCapturerBlockEntity(RagiumBlocks.MOB_CAPTURER, pos, state) {
    override fun inputFilter(stack: ImmutableItemStack): Boolean = stack.isOf(RagiumItems.ELDRITCH_EGG)

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
            val eggStack: ImmutableItemStack = HTThrownCaptureEgg.getCapturedStack(entity) ?: continue
            for (slot: HTItemStackSlot in outputSlots) {
                if (slot.insert(eggStack, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) == null) {
                    // スポーンエッグをスロットに入れる
                    slot.insert(eggStack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                    // 対象を消す
                    entity.discard()
                    // Capture Eggを減らす
                    inputSlot.extract(1, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                    return true
                }
            }
        }
        return false
    }
}
