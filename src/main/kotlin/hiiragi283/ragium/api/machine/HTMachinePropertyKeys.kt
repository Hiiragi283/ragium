package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.client.renderer.HTMachineRenderer
import hiiragi283.ragium.api.extension.constFunction3
import hiiragi283.ragium.api.extension.identifyFunction
import hiiragi283.ragium.api.machine.property.HTMachineParticleHandler
import hiiragi283.ragium.api.machine.property.HTMachineRecipeProxy
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.api.property.HTPropertyKey
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

typealias HTMachineEntityFactory = (BlockPos, BlockState, HTMachineKey) -> HTMachineBlockEntity?

object HTMachinePropertyKeys {
    @JvmField
    val MACHINE_FACTORY: HTPropertyKey<HTMachineEntityFactory> =
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

    //    Data Gen    //

    @JvmField
    val MODEL_MAPPER: HTPropertyKey<(HTMachineKey) -> ResourceLocation> =
        HTPropertyKey.withDefault(RagiumAPI.id("model_mapper")) { key: HTMachineKey -> RagiumAPI.id("block/${key.name}") }

    @JvmField
    val ROTATION_MAPPER: HTPropertyKey<(Direction) -> Direction> =
        HTPropertyKey.withDefault(RagiumAPI.id("rotation_mapper"), ::identifyFunction)

    //    Generator    //

    @JvmField
    val GENERATOR_PREDICATE: HTPropertyKey<(Level, BlockPos) -> Boolean> =
        HTPropertyKey.withDefault(RagiumAPI.id("generator_predicate"), constFunction3(false))

    //    Processor    //

    @JvmField
    val RECIPE_PROXY: HTPropertyKey<HTMachineRecipeProxy> =
        HTPropertyKey.simple(RagiumAPI.id("recipe_proxy"))

    //    Multiblock    //

    @JvmField
    val MULTIBLOCK_MAP: HTPropertyKey<HTMultiblockMap.Relative> =
        HTPropertyKey.simple(RagiumAPI.id("multiblock_map"))
}
