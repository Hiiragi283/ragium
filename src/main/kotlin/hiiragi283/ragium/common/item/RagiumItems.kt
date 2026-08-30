package hiiragi283.ragium.common.item

import hiiragi283.lib.HTComparators
import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildSetMultiMap
import hiiragi283.lib.collection.flatMapTable
import hiiragi283.lib.material.CommonMaterials
import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.VanillaMaterials
import hiiragi283.lib.registry.HTDeferredItemRegister
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTItemPart
import hiiragi283.ragium.common.material.RagiumMaterials
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
            VanillaMaterials.COAL,
            VanillaMaterials.CHARCOAL,
            CommonMaterials.COAL_COKE,
            // Mineral
            CommonMaterials.BORAX,
            CommonMaterials.NITER,
            CommonMaterials.SALT,
            CommonMaterials.SULFUR,
            RagiumMaterials.RAGINITE,
            // Gem
            VanillaMaterials.LAPIS,
            VanillaMaterials.QUARTZ,
            VanillaMaterials.AMETHYST,
            VanillaMaterials.DIAMOND,
            VanillaMaterials.EMERALD,
            VanillaMaterials.ECHO,
            VanillaMaterials.PRISMARINE,
            // Metal
            VanillaMaterials.COPPER,
            VanillaMaterials.IRON,
            VanillaMaterials.GOLD,
            // Other
            VanillaMaterials.WOOD,
            VanillaMaterials.GLASS,
            VanillaMaterials.OBSIDIAN,
            VanillaMaterials.PAPER,
        ).forEach { put(HTItemPart.DUST, it) }
        // Gear
        setOf(
            // Gem
            VanillaMaterials.DIAMOND,
            VanillaMaterials.EMERALD,
            // Metal
            VanillaMaterials.COPPER,
            VanillaMaterials.IRON,
            VanillaMaterials.GOLD,
            // Other
            VanillaMaterials.WOOD,
        ).forEach {
            put(HTItemPart.GEAR, it)
        }
        // Fuel
        setOf(
            VanillaMaterials.COAL,
            VanillaMaterials.CHARCOAL,
            CommonMaterials.COAL_COKE,
        ).forEach { put(HTItemPart.TINY, it) }
        // Alloy
        setOf(
            HTItemPart.DUST,
            HTItemPart.GEAR,
            HTItemPart.NUGGET,
        ).forEach { put(it, VanillaMaterials.NETHERITE) }
        setOf(
            HTItemPart.DUST,
            HTItemPart.INGOT,
            HTItemPart.NUGGET,
        ).forEach {
            put(it, CommonMaterials.STEEL)
        }
    }.flatMapTable { (part: HTItemPart, materials: Collection<HTMaterial>) ->
        materials
            .toSortedSet(compareBy(HTComparators.ID, HTMaterial::getId))
            .map { material: HTMaterial ->
                val item: HTSimpleDeferredItem = if (material == VanillaMaterials.NETHERITE) {
                    REGISTER.registerSimpleItem(part.createName(material)) { properties: Item.Properties -> properties.fireResistant() }
                } else {
                    REGISTER.registerSimpleItem(part.createName(material))
                }
                Triple(part, material, item)
            }
    }

    @JvmStatic
    fun getOrThrow(part: HTItemPart, material: HTMaterial): HTSimpleDeferredItem = MATERIAL_ITEMS[part, material] ?: error("Unregistered item: ${part.createName(material)}")

    // Mechanical
    @JvmField
    val BAMBOO_CHARCOAL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("bamboo_charcoal")

    @JvmField
    val COAL_COKE: HTSimpleDeferredItem = REGISTER.registerSimpleItem(CommonMaterials.COAL_COKE.path)

    // Heat
    @JvmField
    val TAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("tar")

    // Chemical
    @JvmField
    val PARTICLE_BOARD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("particle_board")

    @JvmField
    val PLASTIC_PLATE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("plastic_plate")

    @JvmField
    val SYNTHETIC_FEATHER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_feather")

    @JvmField
    val SYNTHETIC_LEATHER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_leather")

    @JvmField
    val SYNTHETIC_FIBER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_fiber")

    // Bio

    // Electronics

    // Arcane
    @JvmField
    val ELDER_HEART: HTSimpleDeferredItem = REGISTER.registerSimpleItem("elder_heart") { it.rarity(Rarity.UNCOMMON) }

    @JvmField
    val WITHER_DOLL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wither_doll")

    @JvmField
    val WITHER_STAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wither_star") { it.rarity(Rarity.UNCOMMON) }

    //    Parts    //

    // Mechanical

    // Heat

    // Chemical

    // Bio

    // Electronics
    @JvmField
    val MEMORY_DISC: HTSimpleDeferredItem = REGISTER.registerItem("memory_disc", ::HTMemoryDiscItem)

    // Arcane

    //    Tool    //

    @JvmStatic
    private fun registerShapePattern(name: String): HTSimpleDeferredItem = REGISTER.registerSimpleItem("${name}_shape_pattern") { it.stacksTo(1) }

    @JvmField
    val BLANK_SHAPE_PATTERN: HTSimpleDeferredItem = registerShapePattern("blank")

    @JvmField
    val BLOCK_SHAPE_PATTERN: HTSimpleDeferredItem = registerShapePattern("block")

    @JvmField
    val INGOT_SHAPE_PATTERN: HTSimpleDeferredItem = registerShapePattern("ingot")

    @JvmField
    val BALL_SHAPE_PATTERN: HTSimpleDeferredItem = registerShapePattern("ball")

    @JvmField
    val SHAPE_PATTERNS: Set<HTSimpleDeferredItem> = setOf(
        BLANK_SHAPE_PATTERN,
        BLOCK_SHAPE_PATTERN,
        INGOT_SHAPE_PATTERN,
        BALL_SHAPE_PATTERN,
    )
}
