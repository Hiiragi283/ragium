package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.variant.HTToolVariant
import hiiragi283.ragium.common.material.VanillaMaterialKeys
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

enum class HTVanillaToolVariant(
    private val enPattern: String,
    private val jaPattern: String,
    override val tagKeys: Iterable<TagKey<Item>>,
) : HTToolVariant {
    SHOVEL("%s Shovel", "%sのシャベル", ItemTags.SHOVELS) {
        override fun registerItem(register: HTDeferredItemRegister, key: HTMaterialKey, tier: Tier): HTDeferredItem<*> =
            register.registerItemWith("${key.name}_shovel", tier, ::ShovelItem) {
                it.attributes(DiggerItem.createAttributes(tier, 1.5f, -3f))
            }
    },
    PICKAXE("%s Pickaxe", "%sのツルハシ", ItemTags.PICKAXES) {
        override fun registerItem(register: HTDeferredItemRegister, key: HTMaterialKey, tier: Tier): HTDeferredItem<*> =
            register.registerItemWith("${key.name}_pickaxe", tier, ::PickaxeItem) {
                it.attributes(DiggerItem.createAttributes(tier, 1f, -2.8f))
            }
    },
    AXE("%s Axe", "%sの斧", ItemTags.AXES) {
        override fun registerItem(register: HTDeferredItemRegister, key: HTMaterialKey, tier: Tier): HTDeferredItem<*> =
            register.registerItemWith("${key.name}_axe", tier, ::AxeItem) {
                it.attributes(DiggerItem.createAttributes(tier, 6f, -3.1f))
            }
    },
    HOE("%s Hoe", "%sのクワ", ItemTags.HOES) {
        override fun registerItem(register: HTDeferredItemRegister, key: HTMaterialKey, tier: Tier): HTDeferredItem<*> =
            register.registerItemWith("${key.name}_hoe", tier, ::HoeItem) {
                it.attributes(DiggerItem.createAttributes(tier, -2f, -1f))
            }
    },
    SWORD("%s Sword", "%sの剣", ItemTags.SWORDS) {
        override fun registerItem(register: HTDeferredItemRegister, key: HTMaterialKey, tier: Tier): HTDeferredItem<*> =
            register.registerItemWith("${key.name}_sword", tier, ::SwordItem) {
                it.attributes(SwordItem.createAttributes(tier, 3f, -2.4f))
            }
    }, ;

    constructor(enPattern: String, jaPattern: String, tagKey: TagKey<Item>) : this(enPattern, jaPattern, listOf(tagKey))

    companion object {
        val TOOL_TABLE: ImmutableTable<HTVanillaToolVariant, HTMaterialKey, Item> = buildTable {
            // Wooden
            this[SHOVEL, VanillaMaterialKeys.WOOD] = Items.WOODEN_SHOVEL
            this[PICKAXE, VanillaMaterialKeys.WOOD] = Items.WOODEN_PICKAXE
            this[AXE, VanillaMaterialKeys.WOOD] = Items.WOODEN_AXE
            this[HOE, VanillaMaterialKeys.WOOD] = Items.WOODEN_HOE
            this[SWORD, VanillaMaterialKeys.WOOD] = Items.WOODEN_SWORD
            // Stone
            // Iron
            this[SHOVEL, VanillaMaterialKeys.IRON] = Items.IRON_SHOVEL
            this[PICKAXE, VanillaMaterialKeys.IRON] = Items.IRON_PICKAXE
            this[AXE, VanillaMaterialKeys.IRON] = Items.IRON_AXE
            this[HOE, VanillaMaterialKeys.IRON] = Items.IRON_HOE
            this[SWORD, VanillaMaterialKeys.IRON] = Items.IRON_SWORD
            // Golden
            this[SHOVEL, VanillaMaterialKeys.GOLD] = Items.GOLDEN_SHOVEL
            this[PICKAXE, VanillaMaterialKeys.GOLD] = Items.GOLDEN_PICKAXE
            this[AXE, VanillaMaterialKeys.GOLD] = Items.GOLDEN_AXE
            this[HOE, VanillaMaterialKeys.GOLD] = Items.GOLDEN_HOE
            this[SWORD, VanillaMaterialKeys.GOLD] = Items.GOLDEN_SWORD
            // Diamond
            this[SHOVEL, VanillaMaterialKeys.DIAMOND] = Items.DIAMOND_SHOVEL
            this[PICKAXE, VanillaMaterialKeys.DIAMOND] = Items.DIAMOND_PICKAXE
            this[AXE, VanillaMaterialKeys.DIAMOND] = Items.DIAMOND_AXE
            this[HOE, VanillaMaterialKeys.DIAMOND] = Items.DIAMOND_HOE
            this[SWORD, VanillaMaterialKeys.DIAMOND] = Items.DIAMOND_SWORD
            // Netherite
            this[SHOVEL, VanillaMaterialKeys.NETHERITE] = Items.NETHERITE_SHOVEL
            this[PICKAXE, VanillaMaterialKeys.NETHERITE] = Items.NETHERITE_PICKAXE
            this[AXE, VanillaMaterialKeys.NETHERITE] = Items.NETHERITE_AXE
            this[HOE, VanillaMaterialKeys.NETHERITE] = Items.NETHERITE_HOE
            this[SWORD, VanillaMaterialKeys.NETHERITE] = Items.NETHERITE_SWORD
        }
    }

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }.replace("%s", value)

    override fun variantName(): String = name.lowercase()
}
