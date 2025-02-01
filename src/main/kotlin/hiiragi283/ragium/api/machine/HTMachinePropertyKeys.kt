package hiiragi283.ragium.api.machine

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.client.renderer.HTMachineRenderer
import hiiragi283.ragium.api.extension.constFunction3
import hiiragi283.ragium.api.extension.identifyFunction
import hiiragi283.ragium.api.machine.property.HTMachineParticleHandler
import hiiragi283.ragium.api.machine.property.HTMachineRecipeProxy
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.api.property.HTPropertyKey
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.util.DataFunction
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
    val TICK_RATE: HTPropertyKey<(HTMachineTier) -> Int> =
        HTPropertyKey
            .builder<(HTMachineTier) -> Int>(RagiumAPI.id("tick_rate"))
            .setDefaultValue { HTMachineTier::tickRate }
            .build()

    @JvmField
    val RENDERER_PRE: HTPropertyKey<HTMachineRenderer> =
        HTPropertyKey
            .builder<HTMachineRenderer>(RagiumAPI.id("renderer_pre"))
            .setDefaultValue(HTMachineRenderer::EMPTY)
            .build()

    @JvmField
    val RENDERER_POST: HTPropertyKey<HTMachineRenderer> =
        HTPropertyKey
            .builder<HTMachineRenderer>(RagiumAPI.id("renderer_post"))
            .setDefaultValue(HTMachineRenderer::EMPTY)
            .build()

    //    Data Gen    //

    @JvmField
    val MODEL_MAPPER: HTPropertyKey<(HTMachineKey) -> ResourceLocation> =
        HTPropertyKey
            .builder<(HTMachineKey) -> ResourceLocation>(RagiumAPI.id("block_model_mapper"))
            .setDefaultValue { { key: HTMachineKey -> RagiumAPI.id("block/${key.name}") } }
            .build()

    @JvmField
    val ROTATION_MAPPER: HTPropertyKey<(Direction) -> Direction> =
        HTPropertyKey
            .builder<(Direction) -> Direction>(RagiumAPI.id("rotation_mapper"))
            .setDefaultValue(::identifyFunction)
            .build()

    //    Generator    //

    @JvmField
    val GENERATOR_PREDICATE: HTPropertyKey<(Level, BlockPos) -> Boolean> =
        HTPropertyKey
            .builder<(Level, BlockPos) -> Boolean>(RagiumAPI.id("generator_predicate"))
            .setDefaultValue { constFunction3(false) }
            .build()

    //    Processor    //

    @JvmField
    val RECIPE_VALIDATOR: HTPropertyKey<DataFunction<HTMachineRecipe>> =
        HTPropertyKey
            .builder<DataFunction<HTMachineRecipe>>(RagiumAPI.id("recipe_validator"))
            .setDefaultValue {
                DataFunction<HTMachineRecipe> { recipe: HTMachineRecipe ->
                    when {
                        recipe.itemInputs.size > 3 -> DataResult.error { "Machine recipe accepts 3 or less item input!" }
                        recipe.fluidInputs.size > 3 -> DataResult.error { "Machine recipe accepts 3 or less fluid input!" }
                        recipe.getItemOutput(3) != null -> DataResult.error { "Machine recipe accepts 3 or less item output!" }
                        recipe.getFluidOutput(3) != null -> DataResult.error { "Machine recipe accepts 3 or less fluid output!" }
                        else -> DataResult.success(recipe)
                    }
                }
            }.build()

    @JvmField
    val RECIPE_PROXY: HTPropertyKey<HTMachineRecipeProxy> =
        HTPropertyKey
            .builder<HTMachineRecipeProxy>(RagiumAPI.id("recipe_proxy"))
            .build()

    //    Multiblock    //

    @JvmField
    val MULTIBLOCK_MAP: HTPropertyKey<HTMultiblockMap.Relative> =
        HTPropertyKey.simple(RagiumAPI.id("multiblock_map"))
}
