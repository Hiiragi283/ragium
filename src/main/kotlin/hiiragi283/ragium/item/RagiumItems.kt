package hiiragi283.ragium.item

import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildSetMultiMap
import hiiragi283.lib.collection.flatMapTable
import hiiragi283.lib.registry.HTDeferredItemRegister
import hiiragi283.lib.registry.HTSimpleDeferredItem
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

    //    Ingredient    //

    @JvmField
    val MATERIAL_ITEMS: Table<HTItemPart, HTMaterial, HTSimpleDeferredItem> = buildSetMultiMap<HTItemPart, HTMaterial>(sortedMapOf()) {
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
        ).forEach { put(HTItemPart.DUST, it) }
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
            put(HTItemPart.GEAR, it)
        }
        // Gem
        put(HTItemPart.GEM, HTMaterial.Gem.RAGI_CRYSTAL)

        // Fuel
        HTMaterial.Fuel.entries.forEach { put(HTItemPart.TINY, it) }
        // Alloy
        setOf(
            HTItemPart.DUST,
            HTItemPart.GEAR,
            HTItemPart.NUGGET,
        ).forEach { put(it, HTMaterial.Metal.NETHERITE) }
        setOf(
            HTItemPart.DUST,
            HTItemPart.INGOT,
            HTItemPart.NUGGET,
        ).forEach {
            put(it, HTMaterial.Metal.STEEL)
            put(it, HTMaterial.Metal.RAGI_ALLOY)
            put(it, HTMaterial.Metal.ADVANCED_RAGI_ALLOY)
        }
    }.flatMapTable { (part: HTItemPart, materials: Collection<HTMaterial>) ->
        materials
            .sortedBy(HTMaterial::materialName)
            .map { material: HTMaterial ->
                val item: HTSimpleDeferredItem = if (material == HTMaterial.Metal.NETHERITE) {
                    REGISTER.registerSimpleItem(part.createName(material)) { properties: Item.Properties -> properties.fireResistant() }
                } else {
                    REGISTER.registerSimpleItem(part.createName(material))
                }
                Triple(part, material, item)
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

    //    Tool    //

    @JvmStatic
    private fun registerShapePattern(name: String): HTSimpleDeferredItem = REGISTER.registerSimpleItem("${name}_shape_pattern") { it.stacksTo(1) }

    @JvmField
    val BLOCK_SHAPE_PATTERN: HTSimpleDeferredItem = registerShapePattern("block")

    @JvmField
    val INGOT_SHAPE_PATTERN: HTSimpleDeferredItem = registerShapePattern("ingot")

    @JvmField
    val BALL_SHAPE_PATTERN: HTSimpleDeferredItem = registerShapePattern("ball")
}
