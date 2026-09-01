package hiiragi283.lib.registry

import hiiragi283.lib.util.Option
import hiiragi283.lib.util.kotlin
import kotlin.jvm.optionals.getOrNull
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey

//    HolderGetter    //

fun <T : Any> HolderGetter<T>.getOrNull(key: ResourceKey<T>): Holder.Reference<T>? = this.get(key).getOrNull()

fun <T : Any> HolderGetter<T>.getOrNull(key: TagKey<T>): HolderSet.Named<T>? = this.get(key).getOrNull()

fun <T : Any> HolderGetter<T>.getOption(key: ResourceKey<T>): Option<Holder.Reference<T>> = this.get(key).kotlin

fun <T : Any> HolderGetter<T>.getOption(key: TagKey<T>): Option<HolderSet.Named<T>> = this.get(key).kotlin

//    HolderGetter.Provider    //

fun <T : Any> HolderGetter.Provider.getOrNull(key: ResourceKey<T>): Holder.Reference<T>? = this.get(key).getOrNull()

fun <T : Any> HolderGetter.Provider.getOrNull(key: TagKey<T>): HolderSet.Named<T>? = this.get(key).getOrNull()

fun <T : Any> HolderGetter.Provider.lookupOption(key: RegistryKey<T>): Option<HolderGetter<T>> = this.lookup(key).kotlin

fun <T : Any> HolderGetter.Provider.getOption(key: ResourceKey<T>): Option<Holder.Reference<T>> = this.get(key).kotlin

fun <T : Any> HolderGetter.Provider.getOption(key: TagKey<T>): Option<HolderSet.Named<T>> = this.get(key).kotlin
