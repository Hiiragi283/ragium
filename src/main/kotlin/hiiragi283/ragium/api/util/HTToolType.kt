package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.extension.createToolAttribute
import net.minecraft.item.*
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey

enum class HTToolType(
    private val factory: (ToolMaterial, Item.Settings) -> ToolItem,
    val toolTag: TagKey<Item>,
    private val baseAttack: Double,
    private val attackSpeed: Double,
) {
    AXE(::AxeItem, ItemTags.AXES, 3.0, -2.9),
    HOE(::HoeItem, ItemTags.HOES, -4.0, 0.0),
    PICKAXE(::PickaxeItem, ItemTags.PICKAXES, -2.0, -2.8),
    SHOVEL(::ShovelItem, ItemTags.SHOVELS, 2.0, -3.0),
    SWORD(::SwordItem, ItemTags.SWORDS, 3.0, -2.0),
    ;

    fun createToolItem(material: ToolMaterial, settings: Item.Settings): ToolItem = factory(
        material,
        settings.attributeModifiers(createToolAttribute(material, baseAttack, attackSpeed).build()),
    )

    fun getShapedPattern(): List<String> = when (this) {
        AXE -> listOf("B ", "BA", "AA")
        HOE -> listOf("B ", "B ", "AA")
        PICKAXE -> listOf(" B ", " B ", "AAA")
        SHOVEL -> listOf("B", "B", "A")
        SWORD -> listOf("B", "A", "A")
    }
}
