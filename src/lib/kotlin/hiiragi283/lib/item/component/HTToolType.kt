package hiiragi283.lib.item.component

import hiiragi283.lib.data.lang.HTLangPatternProvider
import hiiragi283.lib.tag.HTMaterialLike
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.HoeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ShovelItem
import net.minecraft.world.item.ToolMaterial

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.4
 */
enum class HTToolType(val toolTag: TagKey<Item>, provider: HTLangPatternProvider) : HTLangPatternProvider by provider {
    SWORD(ItemTags.SWORDS, "%s Sword", "%sの剣"),
    SHOVEL(ItemTags.SHOVELS, "%s Shovel", "%sのショベル"),
    PICKAXE(ItemTags.PICKAXES, "%s Pickaxe", "%sのツルハシ"),
    AXE(ItemTags.AXES, "%s Axe", "%sの斧"),
    HOE(ItemTags.HOES, "%s Hoe", "%sのクワ")
    ;

    constructor(toolTag: TagKey<Item>, enPattern: String, jaPattern: String) : this(
        toolTag,
        HTLangPatternProvider(enPattern, jaPattern)
    )

    fun createPath(name: String): String = "${name}_${this.name.lowercase()}"

    fun createPath(material: HTMaterialLike): String = createPath(material.materialName)

    fun createItem(
        properties: Item.Properties,
        material: ToolMaterial,
        axeDamage: Float,
        axeSpeed: Float,
        hoeDamage: Float,
        hoeSpeed: Float
    ): Item = when (this) {
        SWORD -> Item(properties.sword(material, 3f, -2.4f))
        SHOVEL -> ShovelItem(material, 1.5f, -3f, properties)
        PICKAXE -> Item(properties.pickaxe(material, 1f, -2.8f))
        AXE -> AxeItem(material, axeDamage, axeSpeed, properties)
        HOE -> HoeItem(material, hoeDamage, hoeSpeed, properties)
    }
}
