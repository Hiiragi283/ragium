package hiiragi283.ragium.common.util

import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import com.mojang.datafixers.util.Either
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryEntryOwner
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.*
import java.util.function.Predicate
import java.util.stream.Stream

class HTRegistryEntry<T : Any>(registry: Registry<T>, key: RegistryKey<T>) : RegistryEntry<T> {
    constructor(registry: Registry<T>, id: Identifier) : this(registry, RegistryKey.of(registry.key, id))

    val id: Identifier = key.value

    private val supplier: Supplier<RegistryEntry.Reference<T>> = Suppliers.memoize { registry.entryOf(key) }

    override fun value(): T = supplier.get().value()

    override fun hasKeyAndValue(): Boolean = supplier.get().hasKeyAndValue()

    override fun matchesId(id: Identifier): Boolean = supplier.get().matchesId(id)

    override fun streamTags(): Stream<TagKey<T>> = supplier.get().streamTags()

    override fun getKeyOrValue(): Either<RegistryKey<T>, T> = supplier.get().keyOrValue

    override fun getKey(): Optional<RegistryKey<T>> = supplier.get().key

    override fun getType(): RegistryEntry.Type = supplier.get().type

    override fun ownerEquals(owner: RegistryEntryOwner<T>): Boolean = supplier.get().ownerEquals(owner)

    override fun isIn(tag: TagKey<T>): Boolean = supplier.get().isIn(tag)

    @Deprecated("Deprecated in Java", ReplaceWith("supplier.get().matches(entry)"))
    override fun matches(entry: RegistryEntry<T>): Boolean = supplier.get().matches(entry)

    override fun matches(predicate: Predicate<RegistryKey<T>>): Boolean = supplier.get().matches(predicate)

    override fun matchesKey(key: RegistryKey<T>): Boolean = supplier.get().matchesKey(key)
}
