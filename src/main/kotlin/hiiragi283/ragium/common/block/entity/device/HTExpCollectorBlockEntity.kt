package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.extension.getRangedAABB
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.capability.wrapStorage
import hiiragi283.ragium.api.storage.experience.HTExperienceStorage
import hiiragi283.ragium.api.storage.experience.IExperienceStorage
import hiiragi283.ragium.common.storage.experience.HTBasicExperienceStorage
import hiiragi283.ragium.common.util.HTExperienceHelper
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTExpCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity.Tickable(RagiumBlocks.EXP_COLLECTOR, pos, state) {
    private val expStorage: HTExperienceStorage = HTBasicExperienceStorage.output(::setOnlySave, Long.MAX_VALUE)

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        expStorage.serialize(output)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        expStorage.deserialize(input)
    }

    override fun getExperienceStorage(direction: Direction?): HTExperienceStorage = expStorage

    override fun onRemove(level: Level, pos: BlockPos) {
        super.onRemove(level, pos)
        ExperienceOrb(
            level,
            pos.x.toDouble(),
            pos.y.toDouble(),
            pos.z.toDouble(),
            expStorage.getAmountAsInt(),
        )
    }

    //    Ticking    //

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 範囲内のExp Orbを取得する
        val expOrbs: List<ExperienceOrb> = level.getEntitiesOfClass(
            ExperienceOrb::class.java,
            pos.center.getRangedAABB(RagiumConfig.COMMON.deviceCollectorEntityRange.asDouble),
        )
        if (expOrbs.isEmpty()) return false
        // それぞれのExp Orbに対して回収を行う
        expOrbs
            .asSequence()
            .filter(ExperienceOrb::isAlive)
            .mapNotNull { RagiumCapabilities.EXPERIENCE.getCapability(it, null) }
            .forEach { storage: IExperienceStorage -> HTExperienceHelper.moveExp(wrapStorage(storage), expStorage) }
        return true
    }
}
