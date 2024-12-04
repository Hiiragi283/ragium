package hiiragi283.ragium.common.block.transfer

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class HTCreativeExporterBlockEntity(pos: BlockPos, state: BlockState) :
    HTExporterBlockEntityBase(RagiumBlockEntityTypes.CREATIVE_EXPORTER, pos, state) {
    init {
        tier = HTMachineTier.ADVANCED
    }

    override val itemSpeed: Long = Long.MAX_VALUE
    override val fluidSpeed: Long = Long.MAX_VALUE
}
