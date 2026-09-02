package hiiragi283.lib.registry

import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import kotlin.jvm.optionals.getOrNull

//    HolderGetter    //

fun <T : Any> HolderGetter<T>.getOrNull(key: ResourceKey<T>): Holder.Reference<T>? = this.get(key).getOrNull()

fun <T : Any> HolderGetter<T>.getOrNull(key: TagKey<T>): HolderSet.Named<T>? = this.get(key).getOrNull()

//    HolderGetter.Provider    //

fun <T : Any> HolderGetter.Provider.getOrNull(key: ResourceKey<T>): Holder.Reference<T>? = this.get(key).getOrNull()

fun <T : Any> HolderGetter.Provider.getOrNull(key: TagKey<T>): HolderSet.Named<T>? = this.get(key).getOrNull()
