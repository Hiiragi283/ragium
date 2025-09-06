package hiiragi283.ragium.api.registry

import net.minecraft.resources.ResourceLocation

/**
 * @see [mekanism.common.registration.INamedEntry]
 */
fun interface HTHolderLike {
    fun getId(): ResourceLocation

    fun getPath(): String = getId().path
}
