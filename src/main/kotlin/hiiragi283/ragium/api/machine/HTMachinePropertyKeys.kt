package hiiragi283.ragium.api.machine

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.client.renderer.HTMachineRenderer
import hiiragi283.ragium.api.machine.property.HTGeneratorFuel
import hiiragi283.ragium.api.machine.property.HTMachineEntityFactory
import hiiragi283.ragium.api.machine.property.HTMachineParticleHandler
import hiiragi283.ragium.api.machine.property.HTMachineRecipeProxy
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.api.property.HTPropertyKey
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTMachineRecipeValidator
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.Level
import java.util.function.BiPredicate
import java.util.function.Function
import java.util.function.UnaryOperator

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
    val BLOCK_MODEL_MAPPER: HTPropertyKey<Function<HTMachineKey, ResourceLocation>> =
        HTPropertyKey
            .builder<Function<HTMachineKey, ResourceLocation>>(RagiumAPI.id("block_model_mapper"))
            .setDefaultValue { Function { key: HTMachineKey -> RagiumAPI.id("block/${key.name}") } }
            .build()

    @JvmField
    val ITEM_MODEL_MAPPER: HTPropertyKey<Function<HTMachineKey, ResourceLocation>> =
        HTPropertyKey
            .builder<Function<HTMachineKey, ResourceLocation>>(RagiumAPI.id("item_model_mapper"))
            .setDefaultValue { Function { key: HTMachineKey -> RagiumAPI.id("block/${key.name}") } }
            .build()

    @JvmField
    val ROTATION_MAPPER: HTPropertyKey<UnaryOperator<Direction>> =
        HTPropertyKey
            .builder<UnaryOperator<Direction>>(RagiumAPI.id("rotation_mapper"))
            .setDefaultValue(UnaryOperator<Direction>::identity)
            .build()

    //    Generator    //

    @JvmField
    val GENERATOR_PREDICATE: HTPropertyKey<BiPredicate<Level, BlockPos>> =
        HTPropertyKey
            .builder<BiPredicate<Level, BlockPos>>(RagiumAPI.id("generator_predicate"))
            .setDefaultValue { BiPredicate { _: Level, _: BlockPos -> false } }
            .build()

    @JvmField
    val GENERATOR_FUEL: HTPropertyKey<Set<HTGeneratorFuel>> =
        HTPropertyKey
            .builder<Set<HTGeneratorFuel>>(RagiumAPI.id("fuel_tag"))
            .setValidation { fuels: Set<HTGeneratorFuel> ->
                val nonEmpty: Set<HTGeneratorFuel> = fuels.filterNot(HTGeneratorFuel::isEmpty).toSet()
                if (nonEmpty.isEmpty()) {
                    DataResult.error { "There is no valid fuel data!" }
                } else {
                    DataResult.success(nonEmpty)
                }
            }.build()

    //    Processor    //

    @JvmField
    val RECIPE_VALIDATOR: HTPropertyKey<HTMachineRecipeValidator> =
        HTPropertyKey
            .builder<HTMachineRecipeValidator>(RagiumAPI.id("recipe_validator"))
            .setDefaultValue {
                HTMachineRecipeValidator { recipe: HTMachineRecipe ->
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
            .setDefaultValue(HTMachineRecipeProxy::DEFAULT)
            .build()

    //    Multiblock    //

    @JvmField
    val MULTIBLOCK_MAP: HTPropertyKey<HTMultiblockMap.Relative> =
        HTPropertyKey.simple(RagiumAPI.id("multiblock_map"))
}
