package hiiragi283.ragium.api

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.recipe.base.HTItemResult
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.RegistryBuilder

/**
 * Ragiumが追加する[Registry]
 */
object RagiumRegistries {
    /**
     * [HTItemResult]の[MapCodec]の[Registry]
     */
    @JvmField
    val ITEM_RESULT: Registry<MapCodec<out HTItemResult>> =
        RegistryBuilder(Keys.ITEM_RESULT).sync(true).create()

    /**
     * [HTMultiblockComponent.Type]の[Registry]
     */
    @JvmField
    val MULTIBLOCK_COMPONENT_TYPE: Registry<HTMultiblockComponent.Type<*>> =
        RegistryBuilder(Keys.MULTIBLOCK_COMPONENT_TYPE).sync(true).create()

    /**
     * Ragiumが追加する[Registry]の[ResourceKey]
     */
    object Keys {
        @JvmField
        val ITEM_RESULT: ResourceKey<Registry<MapCodec<out HTItemResult>>> =
            ResourceKey.createRegistryKey(RagiumAPI.id("item_result"))

        @JvmField
        val MULTIBLOCK_COMPONENT_TYPE: ResourceKey<Registry<HTMultiblockComponent.Type<*>>> =
            ResourceKey.createRegistryKey(RagiumAPI.id("multiblock_component_type"))
    }
}
