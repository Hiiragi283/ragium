package hiiragi283.ragium.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class RagiumEnchantmentProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>,
) : FabricDynamicRegistryProvider(output, registriesFuture) {
    override fun getName(): String = "Enchantment"

    override fun configure(registries: RegistryWrapper.WrapperLookup, entries: Entries) {

    }
}