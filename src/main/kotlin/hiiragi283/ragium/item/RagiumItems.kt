package hiiragi283.ragium.item

import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.registry.HTDeferredItemRegister
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.util.Identity
import hiiragi283.lib.util.identity
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTItemPart
import hiiragi283.ragium.api.tag.HTMaterial
import net.minecraft.world.item.Item
import net.neoforged.bus.api.IEventBus

data object RagiumItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    @JvmField
    val TABLE_COMPARATOR: Comparator<Pair<HTItemPart, HTMaterial>> = compareBy<Pair<HTItemPart, HTMaterial>> { it.first }.thenBy { it.second.materialName }

    @JvmField
    val MATERIAL_ITEMS: Table<HTItemPart, HTMaterial, HTSimpleDeferredItem> = buildTable(sortedMapOf(TABLE_COMPARATOR)) {
        fun register(part: HTItemPart, material: HTMaterial, operator: Identity<Item.Properties> = identity()) {
            this[part, material] = REGISTER.registerSimpleItem(part.createName(material), operator)
        }

        // Dust
        setOf(
            // Fuel
            HTMaterial.Fuel.COAL,
            HTMaterial.Fuel.CHARCOAL,
            HTMaterial.Fuel.COAL_COKE,
            // Gem
            HTMaterial.Gem.LAPIS,
            HTMaterial.Gem.QUARTZ,
            HTMaterial.Gem.AMETHYST,
            HTMaterial.Gem.DIAMOND,
            HTMaterial.Gem.EMERALD,
            HTMaterial.Gem.ECHO,
            HTMaterial.Gem.PRISMARINE,
            // Metal
            HTMaterial.Metal.COPPER,
            HTMaterial.Metal.IRON,
            HTMaterial.Metal.GOLD,
            // Other
            HTMaterial.Other.WOOD,
            HTMaterial.Other.GLASS,
            HTMaterial.Other.OBSIDIAN,
        ).forEach { register(HTItemPart.DUST, it) }
        // Gear
        setOf(
            // Gem
            HTMaterial.Gem.DIAMOND,
            HTMaterial.Gem.EMERALD,
            // Metal
            HTMaterial.Metal.COPPER,
            HTMaterial.Metal.IRON,
            HTMaterial.Metal.GOLD,
            // Other
            HTMaterial.Other.WOOD,
        ).forEach {
            register(HTItemPart.GEAR, it)
        }

        // Fuel
        HTMaterial.Fuel.entries.forEach { register(HTItemPart.TINY, it) }
        // Alloy
        setOf(
            HTItemPart.DUST,
            HTItemPart.GEAR,
            HTItemPart.NUGGET,
        ).forEach { register(it, HTMaterial.Metal.NETHERITE) { properties: Item.Properties -> properties.fireResistant() } }
    }

    @JvmField
    val COAL_COKE: HTSimpleDeferredItem = REGISTER.registerSimpleItem(HTMaterial.Fuel.COAL_COKE.materialName)

    @JvmStatic
    fun getOrThrow(part: HTItemPart, material: HTMaterial): HTSimpleDeferredItem = MATERIAL_ITEMS[part, material] ?: error("Unregistered item: ${part.createName(material)}")
}
