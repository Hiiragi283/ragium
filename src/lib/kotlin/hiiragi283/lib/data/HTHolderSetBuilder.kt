package hiiragi283.lib.data

import hiiragi283.lib.HTComparators
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.collection.toNelOrNull
import hiiragi283.lib.util.Ior
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.neoforged.neoforge.registries.holdersets.AndHolderSet
import net.neoforged.neoforge.registries.holdersets.OrHolderSet

open class HTHolderSetBuilder<T : Any> {
    private val holders: MutableList<Holder<T>> = ObjectArrayList()
    private val tags: MutableList<HolderSet<T>> = ObjectArrayList()

    operator fun Holder<T>.unaryPlus() {
        holders += this
    }

    operator fun HolderSet<T>.unaryPlus() {
        tags += this
    }

    private fun resolve(): Ior<Nel<Holder<T>>, Nel<HolderSet<T>>>? =
        Ior.fromNullable(holders.toNelOrNull(), tags.toNelOrNull())

    fun buildOr(): HolderSet<T> = resolve()?.fold(
        { HolderSet.direct(it) },
        { HTComparators.resolveHolderSet(it).let(::OrHolderSet) },
        { holders: Nel<Holder<T>>, tags: Nel<HolderSet<T>> ->
            OrHolderSet(HTComparators.resolveHolderSet(tags) + listOf(HolderSet.direct(holders)))
        }
    ) ?: HolderSet.empty()

    fun buildAnd(): HolderSet<T> = resolve()?.fold(
        { HolderSet.direct(it) },
        { HTComparators.resolveHolderSet(it).let(::AndHolderSet) },
        { holders: Nel<Holder<T>>, tags: Nel<HolderSet<T>> ->
            AndHolderSet(HTComparators.resolveHolderSet(tags) + listOf(HolderSet.direct(holders)))
        }
    ) ?: HolderSet.empty()
}
