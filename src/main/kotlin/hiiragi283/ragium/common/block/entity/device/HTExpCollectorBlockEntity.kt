package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.extension.getRangedAABB
import hiiragi283.ragium.api.storage.experience.HTExperienceTank
import hiiragi283.ragium.api.storage.holder.HTExperienceTankHolder
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.experience.tank.HTBasicExperienceTank
import hiiragi283.ragium.common.storage.experience.tank.HTOrbExperienceTank
import hiiragi283.ragium.common.storage.holder.HTBasicExperienceTankHolder
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
    HTDeviceBlockEntity.Tickable(RagiumBlocks.EXP_COLLECTOR, pos, state) {
    lateinit var tank: HTBasicExperienceTank
        private set

    override fun initializeExperienceHandler(listener: HTContentListener): HTExperienceTankHolder {
        val builder: HTBasicExperienceTankHolder.Builder = HTBasicExperienceTankHolder.builder(this)
        tank = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicExperienceTank.output(listener, Long.MAX_VALUE))
        return builder.build()
    }

    override fun onRemove(level: Level, pos: BlockPos) {
        super.onRemove(level, pos)
        ExperienceOrb(
            level,
            pos.x.toDouble(),
            pos.y.toDouble(),
            pos.z.toDouble(),
            tank.getAmountAsInt(),
        )
    }

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = HTStackSlotHelper.calculateRedstoneLevel(tank)

    //    Ticking    //

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
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
            .map(::HTOrbExperienceTank)
            .forEach { tank: HTExperienceTank -> HTExperienceHelper.moveExperience(tank, this.tank) }
        return true
    }
}
