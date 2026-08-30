@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.map

import hiiragi283.lib.data.pack.HTDynamicDataRegister
import hiiragi283.lib.resource.HTKeyLike
import hiiragi283.lib.util.DFUEither
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.common.conditions.WithConditions
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.DataMapLoader
import net.neoforged.neoforge.registries.datamaps.DataMapEntry
import net.neoforged.neoforge.registries.datamaps.DataMapFile
import net.neoforged.neoforge.registries.datamaps.DataMapType

/**
 * 動的データパック向けの[DataMapProvider]の代替クラスです。
 * @param T データマップの要素のクラス
 * @param R レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
class HTDynamicDataMap<T : Any, R : Any>(private val type: DataMapType<R, T>) {
    companion object {
        @JvmStatic
        inline operator fun <T : Any, R : Any> invoke(type: DataMapType<R, T>, action: HTDynamicDataMap<T, R>.() -> Unit) {
            contract {
                callsInPlace(action, InvocationKind.EXACTLY_ONCE)
            }
            HTDynamicDataMap(type).apply(action).gather()
        }
    }

    private val registryKey: ResourceKey<Registry<R>> = type.registryKey()
    private val tagValues: MutableMap<TagKey<R>, DataMapEntry<T>> = hashMapOf()
    private val keyValues: MutableMap<ResourceKey<R>, DataMapEntry<T>> = hashMapOf()

    fun gather() {
        HTDynamicDataRegister.addToData(
            "${DataMapLoader.PATH}/${DataMapLoader.getFolderLocation(registryKey.identifier())}/",
            type.id(),
            DataMapFile.codec(registryKey, type),
            DataMapFile(
                false,
                buildMap {
                    for ((tagKey: TagKey<R>, value: DataMapEntry<T>) in tagValues) {
                        this[DFUEither.left(tagKey)] = Optional.of(WithConditions(value))
                    }
                    for ((key: ResourceKey<R>, value: DataMapEntry<T>) in keyValues) {
                        this[DFUEither.right(key)] = Optional.of(WithConditions(value))
                    }
                },
                emptyList(),
            ),
        )
    }

    //    Builder    //

    fun add(holder: HTKeyLike<R>, value: T, replace: Boolean = false) {
        this.add(holder.getKey(), value, replace)
    }

    fun add(key: ResourceKey<R>, value: T, replace: Boolean = false) {
        keyValues[key] = DataMapEntry(value, replace)
    }

    fun add(tagKey: TagKey<R>, value: T, replace: Boolean = false) {
        tagValues[tagKey] = DataMapEntry(value, replace)
    }
}
