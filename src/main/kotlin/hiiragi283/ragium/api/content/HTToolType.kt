package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.extension.createAttributeComponent
import hiiragi283.ragium.api.extension.itemSettings
import net.minecraft.item.*
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey

enum class HTToolType(
    private val factory: (ToolMaterial, Item.Settings) -> ToolItem,
    val toolTag: TagKey<Item>,
    private val baseAttack: Double,
    private val attackSpeed: Double,
) {
    AXE(::AxeItem, ItemTags.AXES, 3.0, -2.9),
    HOE(::HoeItem, ItemTags.HOES, -4.0, 0.0),
    PICKAXE(::PickaxeItem, ItemTags.PICKAXES, -2.0, -2.8),
    SHOVEL(::ShovelItem, ItemTags.SHOVELS, 2.0, -3.0),
    SWORD(::SwordItem, ItemTags.SWORDS, 3.0, -2.0),
    ;

    fun createToolItem(material: ToolMaterial, settings: Item.Settings = itemSettings()): ToolItem = factory(
        material,
        settings.attributeModifiers(createAttributeComponent(material, baseAttack, attackSpeed)),
    )
}
