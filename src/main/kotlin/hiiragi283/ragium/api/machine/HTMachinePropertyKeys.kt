package hiiragi283.ragium.api.machine

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.api.property.HTPropertyKey
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTMachineRecipeValidator
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.Level

object HTMachinePropertyKeys {
    @JvmField
    val MACHINE_FACTORY: HTPropertyKey.Simple<HTMachineEntityFactory> =
        HTPropertyKey.ofSimple(RagiumAPI.id("machine_factory"))

    @JvmField
    val VALID_TIERS: HTPropertyKey.Defaulted<List<HTMachineTier>> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("valid_tiers")) {
            listOf(HTMachineTier.SIMPLE, HTMachineTier.BASIC, HTMachineTier.ADVANCED, HTMachineTier.ELITE)
        }

    @JvmField
    val MODEL_ID: HTPropertyKey.Defaulted<ResourceLocation> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("model_id")) { RagiumAPI.id("block/dynamic_processor") }

    @JvmField
    val ACTIVE_MODEL_ID: HTPropertyKey.Defaulted<ResourceLocation> =
        HTPropertyKey.ofDefaulted(
            RagiumAPI.id("active_model_id"),
        ) { RagiumAPI.id("block/active_dynamic_processor") }

    @JvmField
    val PARTICLE: HTPropertyKey.Simple<SimpleParticleType> =
        HTPropertyKey.ofSimple(RagiumAPI.id("particle"))

    /*val SCREEN_FACTORY: HTPropertyKey.Simple<HTMachineScreenHandler.Factory> =
        HTPropertyKey.ofSimple(RagiumAPI.id("screen_factory"))*/

    @JvmField
    val SOUND: HTPropertyKey.Simple<SoundEvent> =
        HTPropertyKey.ofSimple(RagiumAPI.id("sound"))

    //    Generator    //

    @JvmField
    val GENERATOR_PREDICATE: HTPropertyKey.Defaulted<(Level, BlockPos) -> Boolean> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("generator_predicate")) { _: Level, _: BlockPos -> false }

    //    Processor    //

    @JvmField
    val FRONT_TEX: HTPropertyKey.Defaulted<(HTMachineKey) -> ResourceLocation> =
        HTPropertyKey.ofDefaulted(
            RagiumAPI.id("front_tex"),
        ) { key: HTMachineKey -> RagiumAPI.id("block/machine/${key.name}") }

    @JvmField
    val ACTIVE_FRONT_TEX: HTPropertyKey.Defaulted<(HTMachineKey) -> ResourceLocation> =
        HTPropertyKey.ofDefaulted(
            RagiumAPI.id("active_front_tex"),
        ) { key: HTMachineKey -> RagiumAPI.id("block/machine/${key.name}_active") }

    @JvmField
    val FRONT_MAPPER: HTPropertyKey.Defaulted<(Direction) -> Direction> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("front_mapper"), value = { it })

    @JvmField
    val RECIPE_VALIDATOR: HTPropertyKey.Defaulted<HTMachineRecipeValidator> =
        HTPropertyKey.ofDefaulted(
            RagiumAPI.id("recipe_validator"),
            HTMachineRecipeValidator(DataResult<HTMachineRecipe>::success),
        )

    //    Multiblock    //

    @JvmField
    val MULTIBLOCK_MAP: HTPropertyKey.Simple<HTMultiblockMap.Relative> =
        HTPropertyKey.ofSimple(RagiumAPI.id("multiblock_map"))
}
