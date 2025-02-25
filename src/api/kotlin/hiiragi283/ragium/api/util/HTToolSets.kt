package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.extension.itemProperty
import hiiragi283.ragium.api.material.HTMaterialKey
import net.minecraft.core.Holder
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.*
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

class HTToolSets(register: DeferredRegister.Items, material: Tier, val key: HTMaterialKey) {
    val axeItem: DeferredItem<AxeItem> = register.registerItem(
        "${key.name}_axe",
        { AxeItem(material, it) },
        itemProperty().attributes(DiggerItem.createAttributes(material, 6f, -3.1f)),
    )

    val hoeItem: DeferredItem<HoeItem> = register.registerItem(
        "${key.name}_hoe",
        { HoeItem(material, it) },
        itemProperty().attributes(DiggerItem.createAttributes(material, -2f, -1f)),
    )

    val pickaxeItem: DeferredItem<PickaxeItem> = register.registerItem(
        "${key.name}_pickaxe",
        { PickaxeItem(material, it) },
        itemProperty().attributes(DiggerItem.createAttributes(material, 1f, -2.8f)),
    )

    val shovelItem: DeferredItem<ShovelItem> = register.registerItem(
        "${key.name}_shovel",
        { ShovelItem(material, it) },
        itemProperty().attributes(DiggerItem.createAttributes(material, 1.5f, -3f)),
    )

    val swordItem: DeferredItem<SwordItem> = register.registerItem(
        "${key.name}_sword",
        { SwordItem(material, it) },
        itemProperty().attributes(DiggerItem.createAttributes(material, 3f, -2.4f)),
    )

    val tools: List<DeferredItem<out TieredItem>> = listOf(
        axeItem,
        hoeItem,
        pickaxeItem,
        shovelItem,
        swordItem,
    )

    //    Data Gen    //

    fun appendTags(action: (TagKey<Item>, Holder<Item>) -> Unit) {
        action(ItemTags.AXES, axeItem)
        action(ItemTags.HOES, hoeItem)
        action(ItemTags.PICKAXES, pickaxeItem)
        action(ItemTags.SHOVELS, shovelItem)
        action(ItemTags.SWORDS, swordItem)
    }
}
