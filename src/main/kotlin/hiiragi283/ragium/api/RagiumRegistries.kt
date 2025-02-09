package hiiragi283.ragium.api

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeCondition
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.RegistryBuilder

/**
 * Ragiumが追加する[Registry]
 */
object RagiumRegistries {
    /**
     * [HTMultiblockComponent.Type]の[Registry]
     */
    @JvmField
    val MULTIBLOCK_COMPONENT_TYPE: Registry<HTMultiblockComponent.Type<*>> =
        RegistryBuilder(Keys.MULTIBLOCK_COMPONENT_TYPE).sync(true).create()

    /**
     * [HTMachineRecipeCondition]の[MapCodec]の[Registry]
     */
    @JvmField
    val RECIPE_CONDITION: Registry<MapCodec<out HTMachineRecipeCondition>> =
        RegistryBuilder(Keys.RECIPE_CONDITION).sync(true).create()

    /**
     * Ragiumが追加する[Registry]の[ResourceKey]
     */
    object Keys {
        @JvmField
        val MULTIBLOCK_COMPONENT_TYPE: ResourceKey<Registry<HTMultiblockComponent.Type<*>>> =
            ResourceKey.createRegistryKey<HTMultiblockComponent.Type<*>>(RagiumAPI.id("multiblock_component_type"))

        @JvmField
        val RECIPE_CONDITION: ResourceKey<Registry<MapCodec<out HTMachineRecipeCondition>>> =
            ResourceKey.createRegistryKey<MapCodec<out HTMachineRecipeCondition>>(RagiumAPI.id("recipe_condition"))
    }
}
