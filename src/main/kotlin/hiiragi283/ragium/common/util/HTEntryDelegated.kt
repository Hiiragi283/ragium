package hiiragi283.ragium.common.util

import net.minecraft.util.Identifier

interface HTEntryDelegated<T : Any> {
    val entry: HTRegistryEntry<T>

    val value: T get() = entry.value()
    val id: Identifier get() = entry.id
}
