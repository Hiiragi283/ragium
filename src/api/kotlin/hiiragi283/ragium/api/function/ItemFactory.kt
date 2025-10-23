package hiiragi283.ragium.api.function

import net.minecraft.world.item.Item
import java.util.function.BiFunction
import java.util.function.Function

fun interface ItemFactory<ITEM : Item> :
    Function<Item.Properties, ITEM>,
    (Item.Properties) -> ITEM {
    override fun apply(properties: Item.Properties): ITEM = invoke(properties)

    fun interface WithContext<C, ITEM : Item> :
        BiFunction<C, Item.Properties, ITEM>,
        (C, Item.Properties) -> ITEM {
        override fun apply(context: C, properties: Item.Properties): ITEM = invoke(context, properties)
    }
}
