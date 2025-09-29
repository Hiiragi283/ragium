package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.collection.HTTable
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.extension.buildTable
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.variant.HTToolVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import net.minecraft.resources.ResourceLocation
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
                "${material.serializedName}_shovel",
                { prop: Item.Properties -> ShovelItem(tier, prop) },
                Item.Properties().attributes(DiggerItem.createAttributes(tier, 1.5f, -3f)),
            )
    },
    PICKAXE("%s Pickaxe", "%sのツルハシ", ItemTags.PICKAXES) {
        override fun registerItem(register: HTDeferredItemRegister, material: HTMaterialType, tier: Tier): HTDeferredItem<*> =
            register.registerItem(
                "${material.serializedName}_pickaxe",
                { prop: Item.Properties -> PickaxeItem(tier, prop) },
                Item.Properties().attributes(DiggerItem.createAttributes(tier, 1f, -2.8f)),
            )
    },
    AXE("%s Axe", "%sの斧", ItemTags.AXES) {
        override fun registerItem(register: HTDeferredItemRegister, material: HTMaterialType, tier: Tier): HTDeferredItem<*> =
            register.registerItem(
                "${material.serializedName}_axe",
                { prop: Item.Properties -> AxeItem(tier, prop) },
                Item.Properties().attributes(DiggerItem.createAttributes(tier, 6f, -3.1f)),
            )
    },
    HOE("%s Hoe", "%sのクワ", ItemTags.HOES) {
        override fun registerItem(register: HTDeferredItemRegister, material: HTMaterialType, tier: Tier): HTDeferredItem<*> =
            register.registerItem(
                "${material.serializedName}_hoe",
                { prop: Item.Properties -> HoeItem(tier, prop) },
                Item.Properties().attributes(DiggerItem.createAttributes(tier, -2f, -1f)),
            )
    },
    SWORD("%s Sword", "%sの剣", ItemTags.SWORDS) {
        override fun registerItem(register: HTDeferredItemRegister, material: HTMaterialType, tier: Tier): HTDeferredItem<*> =
            register.registerItem(
                "${material.serializedName}_sword",
                { prop: Item.Properties -> SwordItem(tier, prop) },
                Item.Properties().attributes(SwordItem.createAttributes(tier, 3f, -2.4f)),
            )
    }, ;

    companion object {
        @JvmField
        val TOOL_TABLE: HTTable<HTVanillaToolVariant, HTVanillaMaterialType, Item> = buildTable {
            // Wooden
            put(SHOVEL, HTVanillaMaterialType.WOOD, Items.WOODEN_SHOVEL)
            put(PICKAXE, HTVanillaMaterialType.WOOD, Items.WOODEN_PICKAXE)
            put(AXE, HTVanillaMaterialType.WOOD, Items.WOODEN_AXE)
            put(HOE, HTVanillaMaterialType.WOOD, Items.WOODEN_HOE)
            put(SWORD, HTVanillaMaterialType.WOOD, Items.WOODEN_SWORD)
            // Stone
            // Iron
            put(SHOVEL, HTVanillaMaterialType.IRON, Items.IRON_SHOVEL)
            put(PICKAXE, HTVanillaMaterialType.IRON, Items.IRON_PICKAXE)
            put(AXE, HTVanillaMaterialType.IRON, Items.IRON_AXE)
            put(HOE, HTVanillaMaterialType.IRON, Items.IRON_HOE)
            put(SWORD, HTVanillaMaterialType.IRON, Items.IRON_SWORD)
            // Golden
            put(SHOVEL, HTVanillaMaterialType.GOLD, Items.GOLDEN_SHOVEL)
            put(PICKAXE, HTVanillaMaterialType.GOLD, Items.GOLDEN_PICKAXE)
            put(AXE, HTVanillaMaterialType.GOLD, Items.GOLDEN_AXE)
            put(HOE, HTVanillaMaterialType.GOLD, Items.GOLDEN_HOE)
            put(SWORD, HTVanillaMaterialType.GOLD, Items.GOLDEN_SWORD)
            // Diamond
            put(SHOVEL, HTVanillaMaterialType.DIAMOND, Items.DIAMOND_SHOVEL)
            put(PICKAXE, HTVanillaMaterialType.DIAMOND, Items.DIAMOND_PICKAXE)
            put(AXE, HTVanillaMaterialType.DIAMOND, Items.DIAMOND_AXE)
            put(HOE, HTVanillaMaterialType.DIAMOND, Items.DIAMOND_HOE)
            put(SWORD, HTVanillaMaterialType.DIAMOND, Items.DIAMOND_SWORD)
            // Netherite
            put(SHOVEL, HTVanillaMaterialType.NETHERITE, Items.NETHERITE_SHOVEL)
            put(PICKAXE, HTVanillaMaterialType.NETHERITE, Items.NETHERITE_PICKAXE)
            put(AXE, HTVanillaMaterialType.NETHERITE, Items.NETHERITE_AXE)
            put(HOE, HTVanillaMaterialType.NETHERITE, Items.NETHERITE_HOE)
            put(SWORD, HTVanillaMaterialType.NETHERITE, Items.NETHERITE_SWORD)
        }
    }

    override fun getParentId(path: String): ResourceLocation = vanillaId(path)

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }.replace("%s", value)

    override fun getSerializedName(): String = name.lowercase()
}
