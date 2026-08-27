package hiiragi283.lib.registry

import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.flatMap
import hiiragi283.lib.util.right
import kotlin.jvm.optionals.getOrElse
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey

fun <T : Any> HolderGetter<T>.getResult(key: ResourceKey<T>): HTTextResult<Holder<T>> = this.get(key).map(Holder<T>::right).getOrElse { HTTextResult("Missing element $key") }

fun <T : Any> HolderGetter<T>.getResult(key: TagKey<T>): HTTextResult<HolderSet<T>> = this.get(key).map(HolderSet<T>::right).getOrElse { HTTextResult("Missing tag $key") }

//    Provider    //

fun <T : Any> HolderGetter.Provider.lookupResult(key: RegistryKey<T>): HTTextResult<HolderGetter<T>> = this.lookup(key).map(HolderGetter<T>::right).getOrElse { HTTextResult("Registry ${key.identifier()} not found") }

fun <T : Any> HolderGetter.Provider.getResult(key: ResourceKey<T>): HTTextResult<Holder<T>> = this.lookupResult(key.registryKey()).flatMap { it.getResult(key) }

fun <T : Any> HolderGetter.Provider.getResult(key: TagKey<T>): HTTextResult<HolderSet<T>> = this.lookupResult(key.registry()).flatMap { it.getResult(key) }

fun <T : Any> HolderLookup.Provider.lookupResult(key: RegistryKey<T>): HTTextResult<HolderLookup.RegistryLookup<T>> = this.lookup(key).map(HolderLookup.RegistryLookup<T>::right).getOrElse { HTTextResult("Registry ${key.identifier()} not found") }

fun <T : Any> RegistryAccess.lookupResult(key: RegistryKey<T>): HTTextResult<Registry<T>> = this.lookup(key).map(Registry<T>::right).getOrElse { HTTextResult("Registry ${key.identifier()} not found") }
