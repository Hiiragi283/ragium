package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.data.RagiumDataComponents
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import java.util.function.Consumer

/**
 * 機械のアップグレードを束ねるクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.9
 */
class HTMachineUpgradeList private constructor(private val stacks: List<ItemStack>) : TooltipProvider {
    companion object {
        @JvmField
        val EMPTY = HTMachineUpgradeList(List(4) { ItemStack.EMPTY })

        @JvmField
        val CODEC: Codec<HTMachineUpgradeList> = ItemStack.OPTIONAL_CODEC
            .listOf(4, 4)
            .xmap(::HTMachineUpgradeList, HTMachineUpgradeList::stacks)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMachineUpgradeList> =
            ItemStack.OPTIONAL_LIST_STREAM_CODEC.map(::HTMachineUpgradeList, HTMachineUpgradeList::stacks)
    }

    val isEmpty: Boolean get() = stacks.all(ItemStack::isEmpty)

    operator fun get(index: Int): ItemStack = stacks[index].copy()

    val combinedUpgrade: HTMachineUpgrade = stacks.mapNotNull { it.get(RagiumDataComponents.UPGRADE) }.let {
        HTMachineUpgrade(
            it.sumOf(HTMachineUpgrade::speed),
            it.sumOf(HTMachineUpgrade::efficiency),
            it.sumOf(HTMachineUpgrade::capacity),
            it.flatMap(HTMachineUpgrade::customKeys).toSet()
        )
    }

    override fun addToTooltip(
        context: Item.TooltipContext,
        consumer: Consumer<Component>,
        flag: TooltipFlag,
        components: DataComponentGetter
    ) {
        combinedUpgrade.addToTooltip(context, consumer, flag, components)
    }

    fun addToTooltip(consumer: Consumer<Component>) {
        combinedUpgrade.addToTooltip(consumer)
    }

    @Suppress("DEPRECATION")
    override fun equals(other: Any?): Boolean =
        other is HTMachineUpgradeList && ItemStack.listMatches(this.stacks, other.stacks)

    @Suppress("DEPRECATION")
    override fun hashCode(): Int = ItemStack.hashStackList(stacks)

    override fun toString(): String = "HTMachineUpgradeList(stacks=$stacks)"

    //    Mutable    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.9
     */
    class Mutable {
        private val stacks: MutableList<ItemStack> = MutableList(4) { ItemStack.EMPTY }

        constructor(parent: HTMachineUpgradeList) {
            parent.stacks.forEachIndexed(stacks::set)
        }

        operator fun set(index: Int, stack: ItemStack) {
            stacks[index] = stack.copyWithCount(1)
        }

        fun toImmutable(): HTMachineUpgradeList = HTMachineUpgradeList(stacks)
    }
}
