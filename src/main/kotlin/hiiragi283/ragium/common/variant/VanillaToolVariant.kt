package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.api.variant.HTToolVariant
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.HoeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.PickaxeItem
import net.minecraft.world.item.ShovelItem
import net.minecraft.world.item.SwordItem

enum class VanillaToolVariant(private val enPattern: String, private val jaPattern: String, override val tagKeys: Iterable<TagKey<Item>>) :
    HTToolVariant {
    SHOVEL("%s Shovel", "%sのシャベル", ItemTags.SHOVELS) {
        override fun registerItem(register: HTDeferredItemRegister, material: HTEquipmentMaterial): HTDeferredItem<*> =
            register.registerItemWith("${material.asMaterialName()}_shovel", material, ::ShovelItem) {
                it.attributes(DiggerItem.createAttributes(material, material.getShovelDamage(), material.getShovelAttackSpeed()))
            }
    },
    PICKAXE("%s Pickaxe", "%sのツルハシ", ItemTags.PICKAXES) {
        override fun registerItem(register: HTDeferredItemRegister, material: HTEquipmentMaterial): HTDeferredItem<*> =
            register.registerItemWith("${material.asMaterialName()}_pickaxe", material, ::PickaxeItem) {
                it.attributes(DiggerItem.createAttributes(material, material.getPickaxeDamage(), material.getPickaxeAttackSpeed()))
            }
    },
    AXE("%s Axe", "%sの斧", ItemTags.AXES) {
        override fun registerItem(register: HTDeferredItemRegister, material: HTEquipmentMaterial): HTDeferredItem<*> =
            register.registerItemWith("${material.asMaterialName()}_axe", material, ::AxeItem) {
                it.attributes(DiggerItem.createAttributes(material, material.getAxeDamage(), material.getAxeAttackSpeed()))
            }
    },
    HOE("%s Hoe", "%sのクワ", ItemTags.HOES) {
        override fun registerItem(register: HTDeferredItemRegister, material: HTEquipmentMaterial): HTDeferredItem<*> =
            register.registerItemWith("${material.asMaterialName()}_hoe", material, ::HoeItem) {
                it.attributes(DiggerItem.createAttributes(material, material.getHoeDamage(), material.getHoeAttackSpeed()))
            }
    },
    SWORD("%s Sword", "%sの剣", ItemTags.SWORDS) {
        override fun registerItem(register: HTDeferredItemRegister, material: HTEquipmentMaterial): HTDeferredItem<*> =
            register.registerItemWith("${material.asMaterialName()}_sword", material, ::SwordItem) {
                it.attributes(SwordItem.createAttributes(material, material.getSwordDamage(), material.getSwordAttackSpeed()))
            }
    }, ;

    constructor(enPattern: String, jaPattern: String, tagKey: TagKey<Item>) : this(enPattern, jaPattern, listOf(tagKey))

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }.replace("%s", value)

    override fun variantName(): String = name.lowercase()
}
