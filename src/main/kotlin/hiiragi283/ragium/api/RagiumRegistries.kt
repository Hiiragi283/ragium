package hiiragi283.ragium.api

import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.RegistryBuilder

object RagiumRegistries {
    @JvmField
    val MULTIBLOCK_COMPONENT_TYPE: Registry<HTMultiblockComponent.Type<*>> =
        RegistryBuilder(Keys.MULTIBLOCK_COMPONENT_TYPE).sync(true).create()

    object Keys {
        @JvmField
        val MULTIBLOCK_COMPONENT_TYPE: ResourceKey<Registry<HTMultiblockComponent.Type<*>>> =
            ResourceKey.createRegistryKey<HTMultiblockComponent.Type<*>>(RagiumAPI.id("multiblock_component_type"))
    }
}
