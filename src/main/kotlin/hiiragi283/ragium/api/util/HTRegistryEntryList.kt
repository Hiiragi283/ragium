package hiiragi283.ragium.api.util

import com.mojang.datafixers.util.Either
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryEntryLookup
import net.minecraft.registry.tag.TagKey
import kotlin.random.Random

/**
 * [T]値の[List]または[TagKey]を持つオブジェクトです。
 */
sealed interface HTRegistryEntryList<T : Any> : Iterable<T> {
    companion object {
        /**
         * 空の[HTRegistryEntryList]を返します。
         */
        @JvmStatic
        fun <T : Any> empty(): HTRegistryEntryList<T> = Empty()

        /**
         * [value]を持つ[HTRegistryEntryList]を返します。
         */
        @JvmStatic
        fun <T : Any> direct(value: T): HTRegistryEntryList<T> = direct(listOf(value))

        /**
         * [values]を持つ[HTRegistryEntryList]を返します。
         */
        @JvmStatic
        fun <T : Any> direct(vararg values: T): HTRegistryEntryList<T> = direct(values.toList())

        /**
         * [values]を持つ[HTRegistryEntryList]を返します。
         */
        @JvmStatic
        fun <T : Any> direct(values: List<T>): HTRegistryEntryList<T> = Direct(values)

        /**
         * [tagKey]を持つ[HTRegistryEntryList]を返します。
         */
        @JvmStatic
        fun <T : Any> fromTag(tagKey: TagKey<T>, lookup: RegistryEntryLookup<T>): HTRegistryEntryList<T> =
            fromTag(tagKey, HTTagValueGetter.ofLookup(lookup))

        /**
         * [tagKey]を持つ[HTRegistryEntryList]を返します。
         */
        @JvmStatic
        fun <T : Any> fromTag(tagKey: TagKey<T>, registry: Registry<T>): HTRegistryEntryList<T> = fromTag(tagKey, registry::iterateEntries)

        /**
         * [tagKey]を持つ[HTRegistryEntryList]を返します。
         */
        @JvmStatic
        fun <T : Any> fromTag(tagKey: TagKey<T>, valueGetter: HTTagValueGetter<T>): HTRegistryEntryList<T> = Tagged(tagKey, valueGetter)
    }

    /**
     * [TagKey]または[List]を持つ[Either]を返します。
     */
    val storage: Either<TagKey<T>, List<T>>

    /**
     * この[HTRegistryEntryList]の要素が空か判定します。
     */
    val isEmpty: Boolean
        get() = entries.isEmpty()

    /**
     * この[HTRegistryEntryList]の要素の個数を返します。
     */
    val size: Int
        get() = entries.size

    /**
     * この[HTRegistryEntryList]の要素のリストを返します。
     */
    val entries: List<T>

    /**
     * この[HTRegistryEntryList]からランダムな要素を返します。
     */
    fun getRandom(random: Random): T? = entries.randomOrNull(random)

    /**
     * 指定された[index]に対応する要素を返します。
     */
    operator fun get(index: Int): T? = entries.getOrNull(index)

    /**
     * 指定された要素[entry]が含まれるか判定します。
     */
    operator fun contains(entry: T): Boolean = entries.contains(entry)

    override fun iterator(): Iterator<T> = entries.iterator()

    private class Empty<T : Any> : HTRegistryEntryList<T> {
        override val storage: Either<TagKey<T>, List<T>> = Either.right(listOf())

        override val entries: List<T> = listOf()
    }

    private class Direct<T : Any>(values: List<T>) : HTRegistryEntryList<T> {
        override val storage: Either<TagKey<T>, List<T>> = Either.right(values)

        override val entries: List<T> = values
    }

    private class Tagged<T : Any>(private val tagKey: TagKey<T>, private val valueGetter: HTTagValueGetter<T>) :
        HTRegistryEntryList<T> {
        override val storage: Either<TagKey<T>, List<T>> = Either.left(tagKey)

        override val entries: List<T>
            get() = valueGetter.getValues(tagKey)
    }
}
