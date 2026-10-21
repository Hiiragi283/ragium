package hiiragi283.lib.data

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.data.worldgen.BootstrapContextAccess
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey

/**
 * @author Hiiragi Tsubasa
 * @since 26.3.0
 */
fun <T : Any> BootstrapContextAccess.getOrThrow(key: ResourceKey<T>): Holder.Reference<T> =
    this.lookup(key.registryKey()).getOrThrow(key)

/**
 * @author Hiiragi Tsubasa
 * @since 26.3.0
 */
fun <T : Any> BootstrapContextAccess.getOrThrow(key: TagKey<T>): HolderSet.Named<T> =
    this.lookup(key.registry()).getOrThrow(key)
