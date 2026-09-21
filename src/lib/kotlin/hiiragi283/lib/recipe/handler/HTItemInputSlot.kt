package hiiragi283.lib.recipe.handler

import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.transfer.HTResourceSlot
import hiiragi283.lib.transfer.item.getItemStack
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.transfer.item.ItemResource

/**
 * [Item]向けの[HTResourceInputSlot]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
class HTItemInputSlot(slot: HTResourceSlot<ItemResource>) :
    HTResourceInputSlot<Item, ItemResource, ItemInstance>(slot) {
    override fun getEmptyStack(): ItemStack = ItemStack.EMPTY

    override fun getAmount(instance: ItemInstance): Int = instance.count()

    override fun asResource(instance: ItemInstance): ItemResource = when (instance) {
        is ItemStack -> ItemResource.of(instance)
        is ItemStackTemplate -> ItemResource.of(instance)
        else -> ItemResource.of(instance.typeHolder())
    }

    override fun getStoredInput(): ItemStack = slot.getItemStack()

    override fun isEmpty(instance: ItemInstance): Boolean = HTIngredientHelper.isEmpty(instance)
}
