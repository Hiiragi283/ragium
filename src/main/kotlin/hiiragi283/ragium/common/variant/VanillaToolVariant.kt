package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.api.variant.HTToolVariant
import net.minecraft.core.component.DataComponents
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.HoeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.PickaxeItem
import net.minecraft.world.item.ShearsItem
import net.minecraft.world.item.ShovelItem
import net.minecraft.world.item.SwordItem
import net.neoforged.neoforge.common.Tags

enum class VanillaToolVariant(private val enPattern: String, private val jaPattern: String, override val tagKeys: Iterable<TagKey<Item>>) :
    HTToolVariant {
    SHOVEL("%s Shovel", "%sのシャベル", ItemTags.SHOVELS),
    PICKAXE("%s Pickaxe", "%sのツルハシ", ItemTags.PICKAXES),
    AXE("%s Axe", "%sの斧", ItemTags.AXES),
    HOE("%s Hoe", "%sのクワ", ItemTags.HOES),
    SWORD("%s Sword", "%sの剣", ItemTags.SWORDS),

    // Misc
    SHEARS("%s Shears", "%sのハサミ", Tags.Items.TOOLS_SHEAR),
    ;

    constructor(enPattern: String, jaPattern: String, tagKey: TagKey<Item>) : this(enPattern, jaPattern, listOf(tagKey))

    override fun registerItem(register: HTDeferredItemRegister, material: HTEquipmentMaterial, name: String): HTDeferredItem<*> =
        when (this) {
            SHOVEL -> register.registerItemWith(name, material, ::ShovelItem) {
                it.attributes(DiggerItem.createAttributes(material, material.getShovelDamage(), material.getShovelAttackSpeed()))
            }

            PICKAXE -> register.registerItemWith(name, material, ::PickaxeItem) {
                it.attributes(DiggerItem.createAttributes(material, material.getPickaxeDamage(), material.getPickaxeAttackSpeed()))
            }

            AXE -> register.registerItemWith(name, material, ::AxeItem) {
                it.attributes(DiggerItem.createAttributes(material, material.getAxeDamage(), material.getAxeAttackSpeed()))
            }

            HOE -> register.registerItemWith(name, material, ::HoeItem) {
                it.attributes(DiggerItem.createAttributes(material, material.getHoeDamage(), material.getHoeAttackSpeed()))
            }

            SWORD -> register.registerItemWith(name, material, ::SwordItem) {
                it.attributes(SwordItem.createAttributes(material, material.getSwordDamage(), material.getSwordAttackSpeed()))
            }

            SHEARS -> register.registerItem(name, ::ShearsItem) {
                it.durability(material.uses).component(DataComponents.TOOL, ShearsItem.createToolProperties())
            }
        }

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }.replace("%s", value)

    override fun variantName(): String = name.lowercase()
}
