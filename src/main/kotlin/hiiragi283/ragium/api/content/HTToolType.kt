package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.util.createAttributeComponent
import net.minecraft.item.*
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey

enum class HTToolType(
    private val factory: (ToolMaterial, Item.Settings) -> ToolItem,
    val toolTag: TagKey<Item>,
    private val baseAttack: Double,
    private val attackSpeed: Double,
) : HTTranslationFormatter {
    AXE(::AxeItem, ItemTags.AXES, 3.0, -2.9) {
        override val enPattern: String = "%s Axe"
        override val jaPattern: String = "%sの斧"
    },
    HOE(::HoeItem, ItemTags.HOES, -4.0, 0.0) {
        override val enPattern: String = "%s Hoe"
        override val jaPattern: String = "%sのクワ"
    },
    PICKAXE(::PickaxeItem, ItemTags.PICKAXES, -2.0, -2.8) {
        override val enPattern: String = "%s Pickaxe"
        override val jaPattern: String = "%sのツルハシ"
    },
    SHOVEL(::ShovelItem, ItemTags.SHOVELS, 2.0, -3.0) {
        override val enPattern: String = "%s Shovel"
        override val jaPattern: String = "%sのシャベル"
    },
    SWORD(::SwordItem, ItemTags.SWORDS, 3.0, -2.0) {
        override val enPattern: String = "%s Sword"
        override val jaPattern: String = "%sの剣"
    },
    ;

    fun createToolItem(material: ToolMaterial, settings: Item.Settings): ToolItem = factory(
        material,
        settings.attributeModifiers(createAttributeComponent(material, baseAttack, attackSpeed)),
    )
}
