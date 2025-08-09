package hiiragi283.ragium.util.variant

import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.api.util.HTLanguageType
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.HoeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.PickaxeItem
import net.minecraft.world.item.ShovelItem
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier
import net.minecraft.world.item.TieredItem

enum class HTToolVariant(
    private val factory: (Tier, Item.Properties) -> TieredItem,
    private val enUsPattern: String,
    private val jaJpPattern: String,
    override val tagKey: TagKey<Item>,
) : HTVariantKey.Tagged<Item> {
    SHOVEL(::ShovelItem, "%s Shovel", "%sのシャベル", ItemTags.SHOVELS),
    PICKAXE(::PickaxeItem, "%s Pickaxe", "%sのツルハシ", ItemTags.PICKAXES),
    AXE(::AxeItem, "%s Axe", "%sの斧", ItemTags.AXES),
    HOE(::HoeItem, "%s Hoe", "%sのクワ", ItemTags.HOES),
    SWORD(::SwordItem, "%s Sword", "%sの剣", ItemTags.SWORDS),
    ;

    fun createFactory(tier: Tier): (Item.Properties) -> Item = { prop: Item.Properties -> factory(tier, prop) }

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }.replace("%s", value)

    override fun getSerializedName(): String = name.lowercase()
}
