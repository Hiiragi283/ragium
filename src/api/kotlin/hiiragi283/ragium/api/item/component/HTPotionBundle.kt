package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.data.BiCodec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.BundleContents

/**
 * @see [BundleContents]
 */
class HTPotionBundle private constructor(private val items: List<ItemStack>) : TooltipComponent {
    companion object {
        @JvmField
        val EMPTY = HTPotionBundle(listOf())

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTPotionBundle> =
            BiCodec
                .of(ItemStack.CODEC, ItemStack.STREAM_CODEC)
                .listOf()
                .xmap(::HTPotionBundle, HTPotionBundle::items)
    }

    fun getItemUnsafe(index: Int): ItemStack = items.getOrNull(index) ?: ItemStack.EMPTY

    fun itemsCopy(): List<ItemStack> = items.map(ItemStack::copy)

    val size: Int get() = items.size
    val isEmpty: Boolean get() = items.isEmpty()

    @Suppress("DEPRECATION")
    override fun hashCode(): Int = ItemStack.hashStackList(items)

    @Suppress("DEPRECATION")
    override fun equals(other: Any?): Boolean {
        val bundle: HTPotionBundle = (other as? HTPotionBundle) ?: return false
        return ItemStack.listMatches(this.items, bundle.items)
    }

    //    Mutable    //

    class Mutable(bundle: HTPotionBundle) {
        private val items: MutableList<ItemStack> = bundle.items.toMutableList()

        fun tryInsert(stack: ItemStack): Boolean {
            if (stack.isEmpty) return false
            if (!stack.`is`(Items.POTION)) return false
            if (items.size >= 9) return false
            val stack1: ItemStack = stack.copy()
            stack.shrink(1)
            return items.add(stack1)
        }

        fun removeOne(): ItemStack? = items.removeFirstOrNull()?.copy()

        fun toImmutable(): HTPotionBundle = HTPotionBundle(items)
    }
}
