package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.extension.getRangedAABB
import hiiragi283.ragium.common.storage.fluid.tank.HTExpOrbTank
import hiiragi283.ragium.common.util.HTExperienceHelper
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTExpCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFluidCollectorBlockEntity(RagiumBlocks.EXP_COLLECTOR, pos, state) {
    override fun onRemove(level: Level, pos: BlockPos) {
        super.onRemove(level, pos)
        ExperienceOrb(
            level,
            pos.x.toDouble(),
            pos.y.toDouble(),
            pos.z.toDouble(),
            HTExperienceHelper.expAmountFromFluid(tank.getAmount()),
        ).let(level::addFreshEntity)
    }

    //    Ticking    //

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 範囲内のExp Orbを取得する
        val expOrbs: List<ExperienceOrb> = level.getEntities(
            EntityType.EXPERIENCE_ORB,
            pos.center.getRangedAABB(RagiumConfig.COMMON.deviceCollectorEntityRange.asDouble),
            EntitySelector.NO_SPECTATORS,
        )
        if (expOrbs.isEmpty()) return false
        // それぞれのExp Orbに対して回収を行う
        expOrbs
            .asSequence()
            .filter(ExperienceOrb::isAlive)
            .map(::HTExpOrbTank)
            .forEach { tank: HTExpOrbTank -> HTStackSlotHelper.moveStack(tank, this.tank) }
        return true
    }
}
