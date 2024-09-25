package hiiragi283.ragium.common.block.entity.machine.heat

import hiiragi283.ragium.common.block.entity.machine.HTSingleMachineBlockEntity
import hiiragi283.ragium.common.block.entity.machine.RagiumMachineConditions
import hiiragi283.ragium.common.machine.HTMachineType
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTRockGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleMachineBlockEntity(HTMachineType.Single.ROCK_GENERATOR, pos, state) {
    override val condition: (World, BlockPos) -> Boolean = RagiumMachineConditions.ROCK_GENERATOR
}
