package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.variant.HTToolVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.HoeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.PickaxeItem
import net.minecraft.world.item.ShovelItem
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier

enum class HTVanillaToolVariant(private val enPattern: String, private val jaPattern: String, override val tagKey: TagKey<Item>) :
    HTToolVariant {
    SHOVEL("%s Shovel", "%sのシャベル", ItemTags.SHOVELS) {
        override fun registerItem(register: HTDeferredItemRegister, material: HTMaterialType, tier: Tier): HTDeferredItem<*> =
            register.registerItem(
                "${material.materialName()}_shovel",
                { prop: Item.Properties -> ShovelItem(tier, prop) },
                Item.Properties().attributes(DiggerItem.createAttributes(tier, 1.5f, -3f)),
            )
    },
    PICKAXE("%s Pickaxe", "%sのツルハシ", ItemTags.PICKAXES) {
        override fun registerItem(register: HTDeferredItemRegister, material: HTMaterialType, tier: Tier): HTDeferredItem<*> =
            register.registerItem(
                "${material.materialName()}_pickaxe",
                { prop: Item.Properties -> PickaxeItem(tier, prop) },
                Item.Properties().attributes(DiggerItem.createAttributes(tier, 1f, -2.8f)),
            )
    },
    AXE("%s Axe", "%sの斧", ItemTags.AXES) {
        override fun registerItem(register: HTDeferredItemRegister, material: HTMaterialType, tier: Tier): HTDeferredItem<*> =
            register.registerItem(
                "${material.materialName()}_axe",
                { prop: Item.Properties -> AxeItem(tier, prop) },
                Item.Properties().attributes(DiggerItem.createAttributes(tier, 6f, -3.1f)),
            )
    },
    HOE("%s Hoe", "%sのクワ", ItemTags.HOES) {
        override fun registerItem(register: HTDeferredItemRegister, material: HTMaterialType, tier: Tier): HTDeferredItem<*> =
            register.registerItem(
                "${material.materialName()}_hoe",
                { prop: Item.Properties -> HoeItem(tier, prop) },
                Item.Properties().attributes(DiggerItem.createAttributes(tier, -2f, -1f)),
            )
    },
    SWORD("%s Sword", "%sの剣", ItemTags.SWORDS) {
        override fun registerItem(register: HTDeferredItemRegister, material: HTMaterialType, tier: Tier): HTDeferredItem<*> =
            register.registerItem(
                "${material.materialName()}_sword",
                { prop: Item.Properties -> SwordItem(tier, prop) },
                Item.Properties().attributes(SwordItem.createAttributes(tier, 3f, -2.4f)),
            )
    }, ;

    companion object {
        val TOOL_TABLE: ImmutableTable<HTVanillaToolVariant, HTVanillaMaterialType, Item> = buildTable {
            // Wooden
            this[SHOVEL, HTVanillaMaterialType.WOOD] = Items.WOODEN_SHOVEL
            this[PICKAXE, HTVanillaMaterialType.WOOD] = Items.WOODEN_PICKAXE
            this[AXE, HTVanillaMaterialType.WOOD] = Items.WOODEN_AXE
            this[HOE, HTVanillaMaterialType.WOOD] = Items.WOODEN_HOE
            this[SWORD, HTVanillaMaterialType.WOOD] = Items.WOODEN_SWORD
            // Stone
            // Iron
            this[SHOVEL, HTVanillaMaterialType.IRON] = Items.IRON_SHOVEL
            this[PICKAXE, HTVanillaMaterialType.IRON] = Items.IRON_PICKAXE
            this[AXE, HTVanillaMaterialType.IRON] = Items.IRON_AXE
            this[HOE, HTVanillaMaterialType.IRON] = Items.IRON_HOE
            this[SWORD, HTVanillaMaterialType.IRON] = Items.IRON_SWORD
            // Golden
            this[SHOVEL, HTVanillaMaterialType.GOLD] = Items.GOLDEN_SHOVEL
            this[PICKAXE, HTVanillaMaterialType.GOLD] = Items.GOLDEN_PICKAXE
            this[AXE, HTVanillaMaterialType.GOLD] = Items.GOLDEN_AXE
            this[HOE, HTVanillaMaterialType.GOLD] = Items.GOLDEN_HOE
            this[SWORD, HTVanillaMaterialType.GOLD] = Items.GOLDEN_SWORD
            // Diamond
            this[SHOVEL, HTVanillaMaterialType.DIAMOND] = Items.DIAMOND_SHOVEL
            this[PICKAXE, HTVanillaMaterialType.DIAMOND] = Items.DIAMOND_PICKAXE
            this[AXE, HTVanillaMaterialType.DIAMOND] = Items.DIAMOND_AXE
            this[HOE, HTVanillaMaterialType.DIAMOND] = Items.DIAMOND_HOE
            this[SWORD, HTVanillaMaterialType.DIAMOND] = Items.DIAMOND_SWORD
            // Netherite
            this[SHOVEL, HTVanillaMaterialType.NETHERITE] = Items.NETHERITE_SHOVEL
            this[PICKAXE, HTVanillaMaterialType.NETHERITE] = Items.NETHERITE_PICKAXE
            this[AXE, HTVanillaMaterialType.NETHERITE] = Items.NETHERITE_AXE
            this[HOE, HTVanillaMaterialType.NETHERITE] = Items.NETHERITE_HOE
            this[SWORD, HTVanillaMaterialType.NETHERITE] = Items.NETHERITE_SWORD
        }
    }

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }.replace("%s", value)

    override fun variantName(): String = name.lowercase()
}
