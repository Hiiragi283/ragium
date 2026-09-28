@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data

import hiiragi283.lib.registry.HTDeferredBlockAndItem
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.util.HTBuilderMarker
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * [Holder]を受け取る処理を表すインターフェースです。
 * @param T レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@HTBuilderMarker
interface HolderAcceptor<T : Any> {
    /**
     * [Holder]を追加します。
     */
    operator fun Holder<T>.unaryPlus()

    /**
     * [Block]向けの[HolderAcceptor]の拡張インターフェースです。
     * @since 26.1.8
     */
    interface BlockAcceptor : HolderAcceptor<Block> {
        @Suppress("DEPRECATION")
        operator fun Block.unaryPlus() {
            +this.builtInRegistryHolder()
        }

        operator fun HTDeferredBlockAndItem<*, *>.unaryPlus() {
            +this.block
        }
    }

    /**
     * [Fluid]向けの[HolderAcceptor]の拡張インターフェースです。
     */
    interface FluidAcceptor : HolderAcceptor<Fluid> {
        @Suppress("DEPRECATION")
        operator fun Fluid.unaryPlus() {
            +this.builtInRegistryHolder()
        }

        /**
         * @since 26.1.8
         */
        operator fun HTFluidContent.unaryPlus() {
            +this.sourceHolder
        }
    }

    /**
     * [Item]向けの[HolderAcceptor]の拡張インターフェースです。
     */
    interface ItemAcceptor : HolderAcceptor<Item> {
        @Suppress("DEPRECATION")
        operator fun Item.unaryPlus() {
            +this.builtInRegistryHolder()
        }

        operator fun HTDeferredBlockAndItem<*, *>.unaryPlus() {
            +this.item
        }
    }

    //    SetBuilder    //

    /**
     * [HolderSet]を作成する[HolderAcceptor]の実装クラスです。
     */
    open class SetBuilder<T : Any> : HolderAcceptor<T> {
        private var holders: MutableList<Holder<T>> = ObjectArrayList()

        override fun Holder<T>.unaryPlus() {
            check(this.delegate is Holder.Reference<T>) { "Cannot serialize given holder $this" }
            holders += this
        }

        fun build(): HolderSet<T> = HolderSet.direct(holders)
    }

    /**
     * [Block]向けの[SetBuilder]の拡張クラスです。
     */
    class BlockSetBuilder :
        SetBuilder<Block>(),
        BlockAcceptor

    /**
     * [Fluid]向けの[SetBuilder]の拡張クラスです。
     */
    class FluidSetBuilder :
        SetBuilder<Fluid>(),
        FluidAcceptor

    /**
     * [Item]向けの[SetBuilder]の拡張クラスです。
     */
    class ItemSetBuilder :
        SetBuilder<Item>(),
        ItemAcceptor

    companion object {
        /**
         * @since 26.1.8
         */
        @JvmStatic
        inline fun buildBlockSet(builderAction: BlockSetBuilder.() -> Unit): HolderSet<Block> {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return BlockSetBuilder().apply(builderAction).build()
        }

        /**
         * @since 26.1.7
         */
        @JvmStatic
        inline fun buildFluidSet(builderAction: FluidSetBuilder.() -> Unit): HolderSet<Fluid> {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return FluidSetBuilder().apply(builderAction).build()
        }

        /**
         * @since 26.1.7
         */
        @JvmStatic
        inline fun buildItemSet(builderAction: ItemSetBuilder.() -> Unit): HolderSet<Item> {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return ItemSetBuilder().apply(builderAction).build()
        }
    }
}
