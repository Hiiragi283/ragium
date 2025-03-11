package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.HTBlockStateProperties
import hiiragi283.ragium.api.block.HTHorizontalMachineBlock
import hiiragi283.ragium.api.extension.toCenterVec3
import hiiragi283.ragium.api.machine.HTMachineType
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3

open class HTFrontParticleMachineBlock(
    val particle: List<ParticleOptions>,
    val factory: (BlockPos, BlockState) -> BlockEntity?,
    machineType: HTMachineType,
    properties: Properties,
) : HTHorizontalMachineBlock(machineType, properties) {
    override fun animateTick(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        random: RandomSource,
    ) {
        if (!state.getValue(HTBlockStateProperties.IS_ACTIVE)) return
        val centerPos: Vec3 = pos.toCenterVec3()
        val front: Direction = getFront(state)
        val axis: Direction.Axis = front.axis
        val d4: Double = random.nextDouble() * 0.6 - 0.3
        val d5: Double = if (axis === Direction.Axis.X) front.stepX * 0.52 else d4
        val d6: Double = random.nextDouble() * 6.0 / 16.0
        val d7: Double = if (axis === Direction.Axis.Z) front.stepZ * 0.52 else d4
        for (particle: ParticleOptions in particle) {
            level.addParticle(particle, centerPos.x + d5, centerPos.y + d6, centerPos.z + d7, 0.0, 0.0, 0.0)
        }
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? = factory(pos, state)
}
