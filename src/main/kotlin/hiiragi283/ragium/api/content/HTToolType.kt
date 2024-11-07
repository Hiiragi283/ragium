package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.data.HTLangType
import hiiragi283.ragium.api.extension.createAttributeComponent
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
        override fun getPattern(type: HTLangType): String = when (type) {
            HTLangType.EN_US -> "%s Axe"
            HTLangType.JA_JP -> "%sの斧"
        }
    },
    HOE(::HoeItem, ItemTags.HOES, -4.0, 0.0) {
        override fun getPattern(type: HTLangType): String = when (type) {
            HTLangType.EN_US -> "%s Hoe"
            HTLangType.JA_JP -> "%sのクワ"
        }
    },
    PICKAXE(::PickaxeItem, ItemTags.PICKAXES, -2.0, -2.8) {
        override fun getPattern(type: HTLangType): String = when (type) {
            HTLangType.EN_US -> "%s Pickaxe"
            HTLangType.JA_JP -> "%sのツルハシ"
        }
    },
    SHOVEL(::ShovelItem, ItemTags.SHOVELS, 2.0, -3.0) {
        override fun getPattern(type: HTLangType): String = when (type) {
            HTLangType.EN_US -> "%s Shovel"
            HTLangType.JA_JP -> "%sのシャベル"
        }
    },
    SWORD(::SwordItem, ItemTags.SWORDS, 3.0, -2.0) {
        override fun getPattern(type: HTLangType): String = when (type) {
            HTLangType.EN_US -> "%s Sword"
            HTLangType.JA_JP -> "%sの剣"
        }
    },
    ;

    fun createToolItem(material: ToolMaterial, settings: Item.Settings): ToolItem = factory(
        material,
        settings.attributeModifiers(createAttributeComponent(material, baseAttack, attackSpeed)),
    )
}
