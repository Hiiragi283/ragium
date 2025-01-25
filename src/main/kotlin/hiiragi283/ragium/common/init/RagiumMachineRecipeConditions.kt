package hiiragi283.ragium.common.init

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import hiiragi283.ragium.common.recipe.condition.*
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumMachineRecipeConditions {
    @JvmField
    val REGISTER: DeferredRegister<MapCodec<out HTMachineRecipeCondition>> =
        DeferredRegister.create(RagiumAPI.RegistryKeys.RECIPE_CONDITION, RagiumAPI.MOD_ID)

    @JvmField
    val BIOME: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTBiomeCondition>> =
        REGISTER.register("biome", HTBiomeCondition::CODEC)

    @JvmField
    val PROCESSOR_CATALYST: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTProcessorCatalystCondition>> =
        REGISTER.register("processor_catalyst", HTProcessorCatalystCondition::CODEC)

    @JvmField
    val ROCK_GENERATOR: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTRockGeneratorCondition>> =
        REGISTER.register("rock_generator", HTRockGeneratorCondition::codec)

    @JvmField
    val TEMPERATURE: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTTemperatureCondition>> =
        REGISTER.register("temperature", HTTemperatureCondition::CODEC)

    @JvmField
    val TIER: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTTierCondition>> =
        REGISTER.register("min_tier", HTTierCondition::CODEC)
}
