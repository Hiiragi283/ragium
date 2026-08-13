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
import net.minecraft.world.item.Rarity
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

    //    Ingredient    //

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
            // Mineral
            HTMaterial.Mineral.SALT,
            HTMaterial.Mineral.NITER,
            HTMaterial.Mineral.BORAX,
            HTMaterial.Mineral.RAGINITE,
            // Gem
            HTMaterial.Gem.LAPIS,
            HTMaterial.Gem.QUARTZ,
            HTMaterial.Gem.AMETHYST,
            HTMaterial.Gem.DIAMOND,
            HTMaterial.Gem.EMERALD,
            HTMaterial.Gem.ECHO,
            HTMaterial.Gem.PRISMARINE,
            HTMaterial.Gem.RAGI_CRYSTAL,
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
        // Gem
        register(HTItemPart.GEM, HTMaterial.Gem.RAGI_CRYSTAL)

        // Fuel
        HTMaterial.Fuel.entries.forEach { register(HTItemPart.TINY, it) }
        // Alloy
        setOf(
            HTItemPart.DUST,
            HTItemPart.GEAR,
            HTItemPart.NUGGET,
        ).forEach { register(it, HTMaterial.Metal.NETHERITE) { properties: Item.Properties -> properties.fireResistant() } }
        setOf(
            HTItemPart.DUST,
            HTItemPart.INGOT,
            HTItemPart.NUGGET,
        ).forEach {
            register(it, HTMaterial.Metal.STEEL)
            register(it, HTMaterial.Metal.RAGI_ALLOY)
            register(it, HTMaterial.Metal.ADVANCED_RAGI_ALLOY)
        }
    }

    @JvmStatic
    fun getOrThrow(part: HTItemPart, material: HTMaterial): HTSimpleDeferredItem = MATERIAL_ITEMS[part, material] ?: error("Unregistered item: ${part.createName(material)}")

    @JvmField
    val COAL_COKE: HTSimpleDeferredItem = REGISTER.registerSimpleItem(HTMaterial.Fuel.COAL_COKE.materialName)

    @JvmField
    val BAMBOO_CHARCOAL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("bamboo_charcoal")

    @JvmField
    val PARTICLE_BOARD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("particle_board")

    @JvmField
    val SYNTHETIC_FEATHER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_feather")

    @JvmField
    val SYNTHETIC_LEATHER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_leather")

    @JvmField
    val SYNTHETIC_FIBER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_fiber")

    @JvmField
    val ELDER_HEART: HTSimpleDeferredItem = REGISTER.registerSimpleItem("elder_heart") { it.rarity(Rarity.UNCOMMON) }

    @JvmField
    val WITHER_DOLL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wither_doll")

    @JvmField
    val WITHER_STAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wither_star") { it.rarity(Rarity.UNCOMMON) }
}
