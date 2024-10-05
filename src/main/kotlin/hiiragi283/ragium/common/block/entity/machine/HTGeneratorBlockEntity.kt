package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTGeneratorBlockEntity : HTMachineBlockEntityBase {
    constructor(pos: BlockPos, state: BlockState) : super(RagiumBlockEntityTypes.GENERATOR_MACHINE, pos, state)

    constructor(
        pos: BlockPos,
        state: BlockState,
        machineType: HTMachineType,
        tier: HTMachineTier,
    ) : super(
        RagiumBlockEntityTypes.GENERATOR_MACHINE,
        pos,
        state,
        machineType,
        tier,
    )

    override fun validateMachineType(machineType: HTMachineType) {
        check(machineType.isGenerator())
    }

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        machineType.asGenerator()?.process(world, pos, tier)
    }
}
