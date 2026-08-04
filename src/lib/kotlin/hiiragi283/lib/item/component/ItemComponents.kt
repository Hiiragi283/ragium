@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.item.component

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.Consumable
import net.minecraft.world.item.component.ItemAttributeModifiers

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T : Any, U, V : Any> Item.Properties.delayedComponent(type: DataComponentType<V>, key: ResourceKey<T>, value: U, factory: (Holder<T>, U) -> V): Item.Properties = this.delayedComponent(type) { provider: HolderLookup.Provider -> factory(provider.getOrThrow(key), value) }

/**
 * [Consumable]を追加します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun Item.Properties.consumables(consumable: Consumable): Item.Properties = this.component(DataComponents.CONSUMABLE, consumable)

//    ItemAttributeModifiers    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun buildItemAttributeModifiers(builderAction: ItemAttributeModifiers.Builder.() -> Unit): ItemAttributeModifiers {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return ItemAttributeModifiers.builder().apply(builderAction).build()
}
