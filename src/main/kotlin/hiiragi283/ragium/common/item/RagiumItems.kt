package hiiragi283.ragium.common.item

import hiiragi283.lib.capability.HTFluidCapabilities
import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildSetMultiMap
import hiiragi283.lib.collection.flatMapTable
import hiiragi283.lib.item.component.HTToolCollection
import hiiragi283.lib.item.component.HTToolType
import hiiragi283.lib.registry.HTDeferredItemRegister
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.text.HTHasTranslationKey
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.HTMachineUpgrade
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.text.HTItemTooltip
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.component.RagiumToolMaterials
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.HoneycombItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.transfer.access.ItemAccess

data object RagiumItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.addAlias("steel_dust", "sooty_iron_dust")
        REGISTER.addAlias("steel_ingot", "sooty_iron_ingot")
        REGISTER.addAlias("steel_nugget", "sooty_iron_nugget")
        REGISTER.addAlias("coal_coke_dust", "carbon_dust")
        REGISTER.addAlias("circuit_chip", "silicon_chip")
        REGISTER.addAlias("bio_machine_parts", "resource_machine_parts")
        REGISTER.addAlias("void_metal_ingot", "shadow_steel_ingot")
        REGISTER.addAlias("void_metal_nugget", "shadow_steel_nugget")
        REGISTER.addAlias("carbon_fiber", "synthetic_fiber")
        REGISTER.addAlias("cfrp_plate", "plastic_plate")
        REGISTER.addAlias("quartz_dust", "silica_dust")
        REGISTER.addAlias("amethyst_dust", "silica_dust")
        REGISTER.addAlias("blank_mold", "mold/blank")
        REGISTER.addAlias("ball_mold", "mold/ball")
        REGISTER.addAlias("block_mold", "mold/block")
        REGISTER.addAlias("plate_mold", "mold/plate")
        HTMachineType.entries.forEach { machineType: HTMachineType ->
            val name: String = machineType.materialName
            REGISTER.addAlias("${name}_machine_parts", "machine_parts/$name")
        }
        REGISTER.addAlias("sodium_hydroxide", "alkali_hydroxide")
        REGISTER.addAlias("soda_ash_dust", "alkali_carbonate")

        eventBus.addListener(::registerCapabilities)
        eventBus.addListener(::modifyDefaultComponents)

        REGISTER.register(eventBus)
    }

    //    Ingredient    //

    @JvmField
    val MATERIAL_ITEMS: Table<HTItemPart, RagiumMaterial, HTSimpleDeferredItem> =
        buildSetMultiMap<RagiumMaterial, HTItemPart>(
            sortedMapOf(RagiumMaterial.COMPARATOR),
            {
                // Fuel
                putAll(RagiumMaterial.Fuel.COAL, HTItemPart.DUST, HTItemPart.TINY)
                putAll(RagiumMaterial.Fuel.CHARCOAL, HTItemPart.DUST, HTItemPart.TINY)
                putAll(RagiumMaterial.Fuel.COAL_COKE, HTItemPart.TINY)
                putAll(RagiumMaterial.Fuel.PITCH_COKE, HTItemPart.TINY)
                // Gem
                putAll(RagiumMaterial.Gem.LAPIS, HTItemPart.DUST)
                putAll(RagiumMaterial.Gem.DIAMOND, HTItemPart.DUST, HTItemPart.GEAR)
                putAll(RagiumMaterial.Gem.EMERALD, HTItemPart.DUST, HTItemPart.GEAR)
                putAll(RagiumMaterial.Gem.ECHO, HTItemPart.DUST)
                // putAll(RagiumMaterial.Gem.PRISMARINE, HTItemPart.DUST)
                putAll(RagiumMaterial.Gem.CINNABAR, HTItemPart.DUST, HTItemPart.GEM)
                putAll(RagiumMaterial.Gem.FLUORITE, HTItemPart.DUST, HTItemPart.GEM)
                putAll(RagiumMaterial.Gem.APATITE, HTItemPart.DUST, HTItemPart.GEM)
                // putAll(RagiumMaterial.Gem.CRYOLITE, HTItemPart.DUST, HTItemPart.GEM)
                // Metal
                putAll(RagiumMaterial.Metal.COPPER, HTItemPart.DUST, HTItemPart.GEAR)
                putAll(RagiumMaterial.Metal.IRON, HTItemPart.DUST, HTItemPart.GEAR)
                putAll(RagiumMaterial.Metal.GOLD, HTItemPart.DUST, HTItemPart.GEAR)
                putAll(RagiumMaterial.Metal.ALUMINUM, HTItemPart.INGOT, HTItemPart.NUGGET)
                putAll(RagiumMaterial.Metal.NICKEL, HTItemPart.DUST)
                putAll(RagiumMaterial.Metal.SILVER, HTItemPart.DUST, HTItemPart.INGOT, HTItemPart.NUGGET)
                // Alloy
                putAll(RagiumMaterial.Alloy.NETHERITE, HTItemPart.DUST, HTItemPart.GEAR, HTItemPart.NUGGET)
                putAll(RagiumMaterial.Alloy.CONSTANTAN, HTItemPart.DUST, HTItemPart.INGOT)
                putAll(RagiumMaterial.Alloy.SOOTY_IRON, HTItemPart.INGOT, HTItemPart.NUGGET)
                putAll(RagiumMaterial.Alloy.BLACK_STEEL, HTItemPart.INGOT, HTItemPart.NUGGET)
                putAll(RagiumMaterial.Alloy.SHADOW_STEEL, HTItemPart.INGOT, HTItemPart.NUGGET)
                // Other
                for (dustLike: RagiumMaterial.DustLike in RagiumMaterial.DustLike.entries) {
                    if (dustLike is RagiumMaterial.Other && dustLike.hasExistingDust) continue
                    putAll(dustLike, HTItemPart.DUST)
                }
                putAll(RagiumMaterial.Other.WOOD, HTItemPart.GEAR)
            },
            ::sortedSetOf,
        ).flatMapTable { (material: RagiumMaterial, parts: Collection<HTItemPart>) ->
            parts.map { part: HTItemPart ->
                Triple(
                    part,
                    material,
                    REGISTER.registerSimpleItem(part.createName(material)) { properties: Item.Properties ->
                        when (material) {
                            RagiumMaterial.Alloy.BLACK_STEEL -> Rarity.UNCOMMON
                            RagiumMaterial.Alloy.SHADOW_STEEL -> Rarity.RARE
                            RagiumMaterial.Other.RAGINITE -> Rarity.EPIC
                            else -> null
                        }?.let(properties::rarity)
                        if (material == RagiumMaterial.Alloy.NETHERITE) {
                            properties.fireResistant()
                        }
                        properties
                    },
                )
            }
        }

    @JvmStatic
    fun getOrThrow(part: HTItemPart, material: RagiumMaterial): HTSimpleDeferredItem =
        MATERIAL_ITEMS[part, material] ?: error("Unregistered item: ${part.createName(material)}")

    // Mechanical
    @JvmField
    val BAMBOO_CHARCOAL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("bamboo_charcoal")

    @JvmField
    val PARTICLE_BOARD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("particle_board")

    @JvmField
    val CEMENT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("cement")

    @JvmField
    val MORTAR: HTSimpleDeferredItem = REGISTER.registerItem("mortar", ::HTMortarItem)

    // Heat
    @JvmField
    val COAL_COKE: HTSimpleDeferredItem = REGISTER.registerSimpleItem(RagiumMaterial.Fuel.COAL_COKE.materialName)

    @JvmField
    val TAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("tar")

    @JvmField
    val PITCH_COKE: HTSimpleDeferredItem = REGISTER.registerSimpleItem(RagiumMaterial.Fuel.PITCH_COKE.materialName)

    @JvmField
    val STICKY_BALL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("sticky_ball")

    // Chemical
    @JvmField
    val ALKALI_HYDROXIDE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("alkali_hydroxide")

    @JvmField
    val ALKALI_CARBONATE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("alkali_carbonate")

    @JvmField
    val ALKALI_SULFATE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("alkali_sulfate")

    @JvmField
    val PLASTIC_PLATE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("plastic_plate")

    @JvmField
    val SYNTHETIC_FEATHER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_feather")

    @JvmField
    val SYNTHETIC_LEATHER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_leather")

    @JvmField
    val SYNTHETIC_FIBER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_fiber")

    // val ALCLAD_PLATE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("alclad_plate")

    // Resource
    @JvmField
    val BEESWAX: HTSimpleDeferredItem = REGISTER.registerItem("beeswax", ::HoneycombItem)

    @JvmField
    val BIO_CHAFF: HTSimpleDeferredItem = REGISTER.registerSimpleItem("bio_chaff")

    @JvmField
    val SPLASH_BOTTLE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("splash_bottle")

    @JvmField
    val LINGERING_BOTTLE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("lingering_bottle")

    // Electronics
    @JvmField
    val CRUDE_SILICON: HTSimpleDeferredItem = REGISTER.registerSimpleItem("crude_silicon")

    @JvmField
    val SILICON_WAFER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("silicon_wafer")

    @JvmField
    val SILICON_CHIP: HTSimpleDeferredItem = REGISTER.registerSimpleItem("silicon_chip")

    @JvmField
    val CIRCUIT_BOARD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("circuit_board")

    @JvmField
    val ELECTRIC_CIRCUIT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("electric_circuit")

    @JvmField
    val DIAMOND_WAFER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("diamond_wafer")

    @JvmField
    val DIAMOND_CHIP: HTSimpleDeferredItem = REGISTER.registerSimpleItem("diamond_chip")

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
        .associateWith { machineType: HTMachineType ->
            REGISTER.registerSimpleItem("machine_parts/${machineType.materialName}")
        }

    @JvmStatic
    fun getParts(machineType: HTMachineType): HTSimpleDeferredItem = MACHINE_PARTS[machineType]!!

    // Mechanical
    @JvmField
    val BLANK_DIE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("press_die/blank")

    @JvmField
    val GEAR_DIE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("press_die/gear")

    @JvmField
    val PACKING_DIE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("press_die/packing")

    @JvmField
    val UNPACKING_DIE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("press_die/unpacking")

    @JvmField
    val PRESS_DIES: Set<HTSimpleDeferredItem> = setOf(BLANK_DIE, GEAR_DIE, PACKING_DIE, UNPACKING_DIE)

    @JvmField
    val HEATING_COIL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("heating_coil")

    // Heat
    @JvmField
    val BLANK_MOLD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("mold/blank") { it.stacksTo(1) }

    @JvmField
    val BALL_MOLD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("mold/ball") { it.stacksTo(1) }

    @JvmField
    val BLOCK_MOLD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("mold/block") { it.stacksTo(1) }

    @JvmField
    val PLATE_MOLD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("mold/plate") { it.stacksTo(1) }

    @JvmField
    val MOLDS: Set<HTSimpleDeferredItem> = setOf(BLANK_MOLD, BALL_MOLD, BLOCK_MOLD, PLATE_MOLD)

    // Chemical

    // Resource

    // Electronics
    @JvmField
    val QUARTZ_LENS: HTSimpleDeferredItem = REGISTER.registerSimpleItem("quartz_lens") {
        it.stacksTo(1)
    }

    @JvmField
    val DIAMOND_LENS: HTSimpleDeferredItem = REGISTER.registerSimpleItem("diamond_lens") {
        it.stacksTo(1).rarity(Rarity.UNCOMMON)
    }

    @JvmField
    val SHADOW_LENS: HTSimpleDeferredItem = REGISTER.registerSimpleItem("shadow_lens") {
        it.stacksTo(1).rarity(Rarity.RARE)
    }

    @JvmField
    val NETHER_STAR_LENS: HTSimpleDeferredItem = REGISTER.registerSimpleItem("nether_star_lens") {
        it.stacksTo(1).rarity(Rarity.EPIC)
    }

    @JvmField
    val MEMORY_DISC: HTSimpleDeferredItem = REGISTER.registerItem("memory_disc", ::HTMemoryDiscItem)

    // Arcane

    //    Tool    //

    @JvmField
    val SOOTY_IRON_TOOLS: HTToolCollection<HTSimpleDeferredItem> = HTToolCollection { toolType: HTToolType ->
        REGISTER.registerItem(
            toolType.createPath(RagiumMaterial.Alloy.SOOTY_IRON),
            { prop: Item.Properties -> toolType.createItem(prop, RagiumToolMaterials.SOOTY_IRON, 6f, -3.1f, -2f, -1f) },
        )
    }

    //    Upgrades    //

    @JvmField
    val BASIC_UPGRADE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("basic_upgrade") {
        it.component(RagiumDataComponents.UPGRADE, HTMachineUpgrade(speed = 25, efficiency = 25, capacity = 25))
    }

    @JvmField
    val ADVANCED_UPGRADE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("advanced_upgrade") {
        it.component(RagiumDataComponents.UPGRADE, HTMachineUpgrade(speed = 50, efficiency = 50, capacity = 50))
    }

    @JvmField
    val ELITE_UPGRADE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("elite_upgrade") {
        it.component(RagiumDataComponents.UPGRADE, HTMachineUpgrade(speed = 75, efficiency = 75, capacity = 75))
    }

    @JvmField
    val ULTIMATE_UPGRADE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ultimate_upgrade") {
        it.component(RagiumDataComponents.UPGRADE, HTMachineUpgrade(speed = 100, efficiency = 100, capacity = 100))
    }

    //    Events    //

    @JvmStatic
    private fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        // Fluid
        event.registerItem(
            HTFluidCapabilities.item,
            { _, access: ItemAccess -> HTPotionBucketItem.BucketHandler(access) },
            RagiumFluids.POTION.bucketHolder,
        )
        // Item
    }

    @JvmStatic
    private fun modifyDefaultComponents(event: ModifyDefaultComponentsEvent) {
        fun <T : Any> set(item: ItemLike, type: DataComponentType<T>, value: T) {
            event.modify(item) { builder: DataComponentMap.Builder, _, _ ->
                builder.set(type, value)
            }
        }

        // Tooltip
        fun setTooltip(item: ItemLike, delegate: HTHasTranslationKey) {
            set(item, RagiumDataComponents.ITEM_TOOLTIP, HTItemTooltip.wrap(delegate))
        }

        setTooltip(getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.SULFUR), RagiumTranslation.TOOLTIPS_SULFUR)
        setTooltip(getOrThrow(HTItemPart.GEM, RagiumMaterial.Gem.CINNABAR), RagiumTranslation.TOOLTIPS_CINNABAR)
        setTooltip(getOrThrow(HTItemPart.GEM, RagiumMaterial.Gem.FLUORITE), RagiumTranslation.TOOLTIPS_FLUORITE)
        setTooltip(getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.CARBON), RagiumTranslation.TOOLTIPS_CARBON)
    }
}
