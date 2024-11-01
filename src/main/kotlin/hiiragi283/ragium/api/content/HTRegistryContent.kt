package hiiragi283.ragium.api.content

import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier

@JvmDefaultWithCompatibility
interface HTRegistryContent<T : Any> {
    val registry: Registry<T>
    val key: RegistryKey<T>

    val id: Identifier get() = key.value
    val entry: RegistryEntry<T> get() = registry.entryOf(key)
    val value: T get() = entry.value()
}
