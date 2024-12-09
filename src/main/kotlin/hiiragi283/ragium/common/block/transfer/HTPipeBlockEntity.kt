package hiiragi283.ragium.common.block.transfer

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.util.HTPipeType
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class HTPipeBlockEntity(pos: BlockPos, state: BlockState) : HTPipeBlockEntityBase(RagiumBlockEntityTypes.PIPE, pos, state) {
    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier, type: HTPipeType) : this(pos, state) {
        this.tier = tier
        this.type = type
    }
}
