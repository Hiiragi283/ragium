@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.item

import hiiragi283.lib.data.HolderAcceptor
import hiiragi283.lib.data.buildDataPatch
import hiiragi283.lib.registry.isAir
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate

/**
 * [ItemStackTemplate]や[ItemStack]向けのビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@HTBuilderMarker
class ItemInstanceBuilder : HolderAcceptor.ItemAcceptor {
    companion object {
        /**
         * [ItemStackTemplate]を作成します。
         * @throws IllegalStateException アイテムが空の場合，または量個数0`以下の場合
         */
        @JvmStatic
        inline fun buildTemplate(builderAction: ItemInstanceBuilder.() -> Unit): ItemStackTemplate {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return ItemInstanceBuilder().apply(builderAction).run { ItemStackTemplate(item, count, patch) }
        }

        /**
         * [ItemStackTemplate]を作成します。
         */
        @Suppress("DEPRECATION")
        @JvmStatic
        inline fun buildSafeTemplate(builderAction: ItemInstanceBuilder.() -> Unit): ItemStackTemplate? {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return ItemInstanceBuilder().apply(builderAction).run {
                when {
                    item.isAir -> null
                    count <= 0 -> null
                    else -> ItemStackTemplate(item, count, patch)
                }
            }
        }

        /**
         * [ItemStack]を作成します。
         */
        @JvmStatic
        inline fun buildStack(builderAction: ItemInstanceBuilder.() -> Unit): ItemStack {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return ItemInstanceBuilder().apply(builderAction).run { ItemStack(item, count, patch) }
        }
    }

    @PublishedApi internal var item: Holder<Item> by HTDelegates.onceInitialize()
    var count: Int = 1

    @PublishedApi internal var patch: DataComponentPatch = DataComponentPatch.EMPTY

    override operator fun Holder<Item>.unaryPlus() {
        item = this
    }

    operator fun DataComponentPatch.unaryPlus() {
        patch = this
    }

    inline fun components(builderAction: DataComponentPatch.Builder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        patch = buildDataPatch(builderAction)
    }
}
