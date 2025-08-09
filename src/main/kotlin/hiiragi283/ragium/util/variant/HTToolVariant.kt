package hiiragi283.ragium.util.variant

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.api.util.HTMaterialType
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.HoeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.PickaxeItem
import net.minecraft.world.item.ShovelItem
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

enum class HTToolVariant(private val enUsPattern: String, private val jaJpPattern: String, override val tagKey: TagKey<Item>) :
    HTVariantKey.Tagged<Item> {
    SHOVEL("%s Shovel", "%sのシャベル", ItemTags.SHOVELS) {
        override fun registerItem(register: DeferredRegister.Items, material: HTMaterialType, tier: Tier): DeferredItem<*> =
            register.registerItem(
                "${material.serializedName}_shovel",
                { prop: Item.Properties -> ShovelItem(tier, prop) },
                Item.Properties().attributes(DiggerItem.createAttributes(tier, 1.5f, -3f)),
            )
    },
    PICKAXE("%s Pickaxe", "%sのツルハシ", ItemTags.PICKAXES) {
        override fun registerItem(register: DeferredRegister.Items, material: HTMaterialType, tier: Tier): DeferredItem<*> =
            register.registerItem(
                "${material.serializedName}_pickaxe",
                { prop: Item.Properties -> PickaxeItem(tier, prop) },
                Item.Properties().attributes(DiggerItem.createAttributes(tier, 1f, -2.8f)),
            )
    },
    AXE("%s Axe", "%sの斧", ItemTags.AXES) {
        override fun registerItem(register: DeferredRegister.Items, material: HTMaterialType, tier: Tier): DeferredItem<*> =
            register.registerItem(
                "${material.serializedName}_axe",
                { prop: Item.Properties -> AxeItem(tier, prop) },
                Item.Properties().attributes(DiggerItem.createAttributes(tier, 6f, -3.1f)),
            )
    },
    HOE("%s Hoe", "%sのクワ", ItemTags.HOES) {
        override fun registerItem(register: DeferredRegister.Items, material: HTMaterialType, tier: Tier): DeferredItem<*> =
            register.registerItem(
                "${material.serializedName}_hoe",
                { prop: Item.Properties -> HoeItem(tier, prop) },
                Item.Properties().attributes(DiggerItem.createAttributes(tier, -2f, -1f)),
            )
    },
    SWORD("%s Sword", "%sの剣", ItemTags.SWORDS) {
        override fun registerItem(register: DeferredRegister.Items, material: HTMaterialType, tier: Tier): DeferredItem<*> =
            register.registerItem(
                "${material.serializedName}_sword",
                { prop: Item.Properties -> SwordItem(tier, prop) },
                Item.Properties().attributes(SwordItem.createAttributes(tier, 3f, -2.4f)),
            )
    },
    ;

    abstract fun registerItem(register: DeferredRegister.Items, material: HTMaterialType, tier: Tier): DeferredItem<*>

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }.replace("%s", value)

    override fun getSerializedName(): String = name.lowercase()
}
