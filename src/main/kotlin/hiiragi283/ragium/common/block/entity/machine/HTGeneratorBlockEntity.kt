package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTGeneratorBlockEntity(pos: BlockPos, state: BlockState, tier: HTMachineTier = HTMachineTier.PRIMITIVE) :
    HTMachineBlockEntityBase(
        RagiumBlockEntityTypes.GENERATOR_MACHINE,
        pos,
        state,
        RagiumMachineTypes.Generator.WATER,
        tier,
    ) {
    override fun validateMachineType(machineType: HTMachineType) {
        check(machineType.isGenerator())
    }

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        machineType.asGenerator()?.process(world, pos, machineType, tier)
    }
}
