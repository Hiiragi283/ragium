package hiiragi283.ragium.api.extension

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.minecraft.component.ComponentMap
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible

//    DefaultItemComponentEvents    //

fun DefaultItemComponentEvents.ModifyContext.modify(item: ItemConvertible, builderAction: ComponentMap.Builder.() -> Unit) {
    modify(item.asItem(), builderAction)
}

fun DefaultItemComponentEvents.ModifyContext.modify(
    items: Collection<ItemConvertible>,
    builderAction: ComponentMap.Builder.(Item) -> Unit,
) {
    modify(items.map(ItemConvertible::asItem), builderAction)
}
