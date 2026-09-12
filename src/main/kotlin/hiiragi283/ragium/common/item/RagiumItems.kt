package hiiragi283.ragium.common.item

import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildSortedSetMultiMap
import hiiragi283.lib.collection.flatMapTable
import hiiragi283.lib.collection.mutableEnumMapOf
import hiiragi283.lib.item.component.HTToolCollection
import hiiragi283.lib.item.component.HTToolType
import hiiragi283.lib.registry.HTDeferredItemRegister
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.common.item.component.RagiumToolMaterials
import net.minecraft.world.item.HoneycombItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.neoforged.bus.api.IEventBus

data object RagiumItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.addAlias("steel_dust", "sooty_iron_dust")
        REGISTER.addAlias("steel_ingot", "sooty_iron_ingot")
        REGISTER.addAlias("steel_nugget", "sooty_iron_nugget")

        REGISTER.register(eventBus)
    }

    //    Ingredient    //

    @JvmField
    val MATERIAL_ITEMS: Table<HTItemPart, RagiumMaterial, HTSimpleDeferredItem> =
        buildSortedSetMultiMap<RagiumMaterial, HTItemPart>(sortedMapOf(RagiumMaterial.COMPARATOR)) {
            // Fuel
            putAll(RagiumMaterial.Fuel.COAL, HTItemPart.DUST, HTItemPart.TINY)
            putAll(RagiumMaterial.Fuel.CHARCOAL, HTItemPart.DUST, HTItemPart.TINY)
            putAll(RagiumMaterial.Fuel.COAL_COKE, HTItemPart.DUST, HTItemPart.TINY)
            // Mineral
            for (mineral: RagiumMaterial.Mineral in RagiumMaterial.Mineral.entries) {
                if (!mineral.isVanilla) {
                    putAll(mineral, HTItemPart.DUST)
                }
            }
            // Gem
            putAll(RagiumMaterial.Gem.LAPIS, HTItemPart.DUST)
            putAll(RagiumMaterial.Gem.QUARTZ, HTItemPart.DUST)
            putAll(RagiumMaterial.Gem.AMETHYST, HTItemPart.DUST)
            putAll(RagiumMaterial.Gem.DIAMOND, HTItemPart.DUST, HTItemPart.GEAR)
            putAll(RagiumMaterial.Gem.EMERALD, HTItemPart.DUST, HTItemPart.GEAR)
            putAll(RagiumMaterial.Gem.ECHO, HTItemPart.DUST)
            putAll(RagiumMaterial.Gem.PRISMARINE, HTItemPart.DUST)
            // Metal
            putAll(RagiumMaterial.Metal.COPPER, HTItemPart.DUST, HTItemPart.GEAR)
            putAll(RagiumMaterial.Metal.IRON, HTItemPart.DUST, HTItemPart.GEAR)
            putAll(RagiumMaterial.Metal.GOLD, HTItemPart.DUST, HTItemPart.GEAR)
            putAll(RagiumMaterial.Metal.NETHERITE, HTItemPart.DUST, HTItemPart.GEAR, HTItemPart.NUGGET)
            putAll(RagiumMaterial.Metal.SOOTY_IRON, HTItemPart.INGOT, HTItemPart.NUGGET)
            putAll(RagiumMaterial.Metal.BLACK_STEEL, HTItemPart.INGOT, HTItemPart.NUGGET)
            putAll(RagiumMaterial.Metal.VOID_METAL, HTItemPart.INGOT, HTItemPart.NUGGET)
            // Other
            putAll(RagiumMaterial.Other.WOOD, HTItemPart.DUST, HTItemPart.GEAR)
            putAll(RagiumMaterial.Other.GLASS, HTItemPart.DUST)
            putAll(RagiumMaterial.Other.OBSIDIAN, HTItemPart.DUST)
            putAll(RagiumMaterial.Other.PAPER, HTItemPart.DUST)
        }.flatMapTable { (material: RagiumMaterial, parts: Collection<HTItemPart>) ->
            parts.map { part: HTItemPart ->
                Triple(
                    part,
                    material,
                    REGISTER.registerSimpleItem(part.createName(material)) { properties: Item.Properties ->
                        when (material) {
                            RagiumMaterial.Mineral.RAGINITE -> Rarity.EPIC
                            RagiumMaterial.Metal.BLACK_STEEL -> Rarity.UNCOMMON
                            RagiumMaterial.Metal.VOID_METAL -> Rarity.RARE
                            else -> null
                        }?.let(properties::rarity)
                        if (material == RagiumMaterial.Metal.NETHERITE) {
                            properties.fireResistant()
                        }
                        properties
                    }
                )
            }
        }

    @JvmStatic
    fun getOrThrow(part: HTItemPart, material: RagiumMaterial): HTSimpleDeferredItem =
        MATERIAL_ITEMS[part, material] ?: error("Unregistered item: ${part.createName(material)}")

    // Mechanical
    @JvmField
    val BAMBOO_CHARCOAL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("bamboo_charcoal")

    // Heat
    @JvmField
    val COAL_COKE: HTSimpleDeferredItem = REGISTER.registerSimpleItem(RagiumMaterial.Fuel.COAL_COKE.materialName)

    @JvmField
    val TAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("tar")

    @JvmField
    val PITCH_COKE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("pitch_coke")

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
    @JvmField
    val BEESWAX: HTSimpleDeferredItem = REGISTER.registerItem("beeswax", ::HoneycombItem)

    // Electronics

    // Arcane
    @JvmField
    val ELDER_HEART: HTSimpleDeferredItem = REGISTER.registerSimpleItem("elder_heart") { it.rarity(Rarity.UNCOMMON) }

    @JvmField
    val WITHER_DOLL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wither_doll")

    @JvmField
    val WITHER_STAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wither_star") { it.rarity(Rarity.UNCOMMON) }

    //    Parts    //

    @JvmField
    val MACHINE_PARTS: Map<HTMachineType, HTSimpleDeferredItem> = HTMachineType.entries
        .associateWithTo(mutableEnumMapOf()) { machineType: HTMachineType ->
            REGISTER.registerSimpleItem("${machineType.materialName}_machine_parts")
        }

    @JvmStatic
    fun getParts(machineType: HTMachineType): HTSimpleDeferredItem = MACHINE_PARTS[machineType]!!

    // Mechanical

    // Heat

    // Chemical

    // Bio

    // Electronics
    @JvmField
    val MEMORY_DISC: HTSimpleDeferredItem = REGISTER.registerItem("memory_disc", ::HTMemoryDiscItem)

    // Arcane

    //    Tool    //

    @JvmField
    val SOOTY_IRON_TOOLS: HTToolCollection<HTSimpleDeferredItem> = HTToolCollection { toolType: HTToolType ->
        REGISTER.registerItem(
            toolType.createPath(RagiumMaterial.Metal.SOOTY_IRON),
            { prop: Item.Properties -> toolType.createItem(prop, RagiumToolMaterials.SOOTY_IRON, 6f, -3.1f, -2f, -1f) }
        )
    }
}
