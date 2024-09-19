package hiiragi283.ragium.data

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class RagiumDynamicRegistryProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricDynamicRegistryProvider(output, registriesFuture) {
    override fun getName(): String = "Dynamic Registry"

    override fun configure(registries: RegistryWrapper.WrapperLookup, entries: Entries) {
    }
}