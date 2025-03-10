package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.extension.getMachineAccess
import hiiragi283.ragium.api.machine.HTMachineAccess
import hiiragi283.ragium.api.machine.HTMachineType
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class HTFurnaceMachineBlock(
    val sound: SoundEvent,
    particle: List<ParticleOptions>,
    factory: (BlockPos, BlockState) -> BlockEntity?,
    machineType: HTMachineType,
    properties: Properties,
) : HTFrontParticleMachineBlock(particle, factory, machineType, properties) {
    override fun animateTick(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        random: RandomSource,
    ) {
        val machine: HTMachineAccess = level.getMachineAccess(pos) ?: return
        if (!machine.isActive) return
        if (random.nextDouble() < 0.1) {
            level.playLocalSound(pos, sound, SoundSource.BLOCKS, 0.6f, 1f, false)
        }
        super.animateTick(state, level, pos, random)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? = factory(pos, state)
}
