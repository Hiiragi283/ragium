package hiiragi283.ragium.item

import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.registry.HTDeferredItemRegister
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.util.Identity
import hiiragi283.lib.util.identity
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTItemParts
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
    val TABLE_COMPARATOR: Comparator<Pair<HTItemParts, HTMaterial>> = compareBy<Pair<HTItemParts, HTMaterial>> { it.first }.thenBy { it.second.materialName }

    @JvmField
    val MATERIAL_ITEMS: Table<HTItemParts, HTMaterial, HTSimpleDeferredItem> = buildTable(sortedMapOf(TABLE_COMPARATOR)) {
        fun register(part: HTItemParts, material: HTMaterial, operator: Identity<Item.Properties> = identity()) {
            this[part, material] = REGISTER.registerSimpleItem(part.createName(material), operator)
        }

        // Dust
        setOf(
            // Fuel
            HTMaterial.Fuels.COAL,
            HTMaterial.Fuels.CHARCOAL,
            // Gem
            HTMaterial.Gems.LAPIS,
            HTMaterial.Gems.QUARTZ,
            HTMaterial.Gems.AMETHYST,
            HTMaterial.Gems.DIAMOND,
            HTMaterial.Gems.EMERALD,
            HTMaterial.Gems.ECHO,
            HTMaterial.Gems.PRISMARINE,
            // Metal
            HTMaterial.Metal.COPPER,
            HTMaterial.Metal.IRON,
            HTMaterial.Metal.GOLD,
            // Other
            HTMaterial.Other.WOOD,
            HTMaterial.Other.GLASS,
            HTMaterial.Other.OBSIDIAN,
        ).forEach { register(HTItemParts.DUST, it) }
        // Gear
        setOf(
            // Metal
            HTMaterial.Metal.COPPER,
            HTMaterial.Metal.IRON,
            HTMaterial.Metal.GOLD,
        ).forEach {
            register(HTItemParts.GEAR, it)
        }
        // Alloy
        setOf(
            HTItemParts.DUST,
            HTItemParts.GEAR,
            HTItemParts.NUGGET,
        ).forEach { register(it, HTMaterial.Metal.NETHERITE) { it.fireResistant() } }
    }
}
