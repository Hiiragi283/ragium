package hiiragi283.ragium.impl.collection

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import java.util.stream.Stream

internal class HTHolderSetList<T : Any>(private val holderSet: HolderSet<T>) : AbstractList<Holder<T>>() {
    override val size: Int = holderSet.size()

    override fun isEmpty(): Boolean = holderSet.stream().findAny().isEmpty

    override fun contains(element: Holder<T>): Boolean = holderSet.contains(element)

    override fun iterator(): Iterator<Holder<T>> = holderSet.iterator()

    override fun get(index: Int): Holder<T> = holderSet.get(index)

    override fun stream(): Stream<Holder<T>> = holderSet.stream()
}
