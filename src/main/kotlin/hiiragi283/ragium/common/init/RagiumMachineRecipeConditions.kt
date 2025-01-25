package hiiragi283.ragium.common.init

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import hiiragi283.ragium.common.recipe.condition.HTCoolingCondition
import hiiragi283.ragium.common.recipe.condition.HTHeatingCondition
import hiiragi283.ragium.common.recipe.condition.HTProcessorCatalystCondition
import hiiragi283.ragium.common.recipe.condition.HTRockGeneratorCondition
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumMachineRecipeConditions {
    @JvmField
    val REGISTER: DeferredRegister<MapCodec<out HTMachineRecipeCondition>> =
        DeferredRegister.create(RagiumAPI.RegistryKeys.RECIPE_CONDITION, RagiumAPI.MOD_ID)

    @JvmField
    val COOLING: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTCoolingCondition>> =
        REGISTER.register("cooling", HTCoolingCondition::CODEC)

    @JvmField
    val HEATING: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTHeatingCondition>> =
        REGISTER.register("heating", HTHeatingCondition::CODEC)

    @JvmField
    val PROCESSOR_CATALYST: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTProcessorCatalystCondition>> =
        REGISTER.register("processor_catalyst", HTProcessorCatalystCondition::CODEC)

    @JvmField
    val ROCK_GENERATOR: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTRockGeneratorCondition>> =
        REGISTER.register("rock_generator", HTRockGeneratorCondition::codec)
}
