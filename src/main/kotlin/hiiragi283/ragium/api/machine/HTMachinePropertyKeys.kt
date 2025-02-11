package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.client.renderer.HTMachineRenderer
import hiiragi283.ragium.api.machine.property.HTMachineParticleHandler
import hiiragi283.ragium.api.property.HTPropertyKey
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.block.state.BlockState

object HTMachinePropertyKeys {
    @JvmField
    val MACHINE_FACTORY: HTPropertyKey<(BlockPos, BlockState) -> HTMachineBlockEntity> =
        HTPropertyKey.simple(RagiumAPI.id("machine_factory"))

    @JvmField
    val PARTICLE: HTPropertyKey<HTMachineParticleHandler> =
        HTPropertyKey.simple(RagiumAPI.id("particle"))

    @JvmField
    val SOUND: HTPropertyKey<SoundEvent> =
        HTPropertyKey.simple(RagiumAPI.id("sound"))

    @JvmField
    val RENDERER_PRE: HTPropertyKey<HTMachineRenderer> =
        HTPropertyKey.withDefault(RagiumAPI.id("renderer_pre"), HTMachineRenderer::EMPTY)

    @JvmField
    val RENDERER_POST: HTPropertyKey<HTMachineRenderer> =
        HTPropertyKey.withDefault(RagiumAPI.id("renderer_post"), HTMachineRenderer::EMPTY)
}
