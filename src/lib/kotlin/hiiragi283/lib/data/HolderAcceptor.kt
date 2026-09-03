package hiiragi283.lib.data

import hiiragi283.lib.registry.HTDeferredBlockAndItem
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid

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
     * [Fluid]向けの[HolderAcceptor]の拡張インターフェースです。
     */
    interface FluidAcceptor : HolderAcceptor<Fluid> {
        @Suppress("DEPRECATION")
        operator fun Fluid.unaryPlus() {
            +this.builtInRegistryHolder()
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

    //    ValueBuilder    //

    /**
     * 単一の[Holder]のみを保持する[HolderAcceptor]の実装クラスです。
     */
    open class ValueBuilder<T : Any> : HolderAcceptor<T> {
        private var holder: Holder<T> by HTDelegates.onceInitialize()

        override fun Holder<T>.unaryPlus() {
            check(this.delegate is Holder.Reference<T>) { "Cannot serialize given holder $this" }
            holder = this
        }

        fun build(): Holder<T> = holder
    }

    /**
     * [Fluid]向けの[ValueBuilder]の拡張クラスです。
     */
    class FluidValueBuilder :
        ValueBuilder<Fluid>(),
        FluidAcceptor

    /**
     * [Item]向けの[ValueBuilder]の拡張クラスです。
     */
    class ItemValueBuilder :
        ValueBuilder<Item>(),
        ItemAcceptor

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
}
