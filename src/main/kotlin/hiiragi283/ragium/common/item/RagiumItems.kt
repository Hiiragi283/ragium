package hiiragi283.ragium.common.item

import hiiragi283.lib.capability.HTFluidCapabilities
import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildSortedSetMultiMap
import hiiragi283.lib.collection.flatMapTable
import hiiragi283.lib.collection.mutableEnumMapOf
import hiiragi283.lib.item.component.HTToolCollection
import hiiragi283.lib.item.component.HTToolType
import hiiragi283.lib.registry.HTDeferredItemRegister
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.lib.text.Text
import hiiragi283.lib.transfer.fluid.HTFluidView
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.data.oreSlurry.RagiumOreSlurryData
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.component.RagiumToolMaterials
import net.minecraft.ChatFormatting
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.item.HoneycombItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.common.tooltip.TooltipLocation
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.event.RegisterTooltipAppendersEvent
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.fluid.FluidResource
import java.util.function.Consumer

data object RagiumItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.addAlias("steel_dust", "sooty_iron_dust")
        REGISTER.addAlias("steel_ingot", "sooty_iron_ingot")
        REGISTER.addAlias("steel_nugget", "sooty_iron_nugget")
        REGISTER.addAlias("coal_coke_dust", "carbon_dust")

        eventBus.addListener(::registerTooltipAppenders)
        eventBus.addListener(::registerCapabilities)
        eventBus.addListener(::modifyDefaultComponents)

        REGISTER.register(eventBus)
    }

    //    Ingredient    //

    @JvmField
    val MATERIAL_ITEMS: Table<HTItemPart, RagiumMaterial, HTSimpleDeferredItem> =
        buildSortedSetMultiMap<RagiumMaterial, HTItemPart>(sortedMapOf(RagiumMaterial.COMPARATOR)) {
            // Fuel
            putAll(RagiumMaterial.Fuel.COAL, HTItemPart.DUST, HTItemPart.TINY)
            putAll(RagiumMaterial.Fuel.CHARCOAL, HTItemPart.DUST, HTItemPart.TINY)
            putAll(RagiumMaterial.Fuel.COAL_COKE, HTItemPart.TINY)
            putAll(RagiumMaterial.Fuel.PITCH_COKE, HTItemPart.TINY)
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
            putAll(RagiumMaterial.Gem.FLUORITE, HTItemPart.DUST, HTItemPart.GEM)
            putAll(RagiumMaterial.Gem.CRYOLITE, HTItemPart.DUST, HTItemPart.GEM)
            // Metal
            putAll(RagiumMaterial.Metal.COPPER, HTItemPart.DUST, HTItemPart.GEAR)
            putAll(RagiumMaterial.Metal.IRON, HTItemPart.DUST, HTItemPart.GEAR)
            putAll(RagiumMaterial.Metal.GOLD, HTItemPart.DUST, HTItemPart.GEAR)
            putAll(RagiumMaterial.Metal.NETHERITE, HTItemPart.DUST, HTItemPart.GEAR, HTItemPart.NUGGET)
            putAll(RagiumMaterial.Metal.ALUMINUM, HTItemPart.INGOT, HTItemPart.NUGGET)
            putAll(RagiumMaterial.Metal.SOOTY_IRON, HTItemPart.INGOT, HTItemPart.NUGGET)
            putAll(RagiumMaterial.Metal.BLACK_STEEL, HTItemPart.INGOT, HTItemPart.NUGGET)
            putAll(RagiumMaterial.Metal.VOID_METAL, HTItemPart.INGOT, HTItemPart.NUGGET)
            // Other
            putAll(RagiumMaterial.Other.WOOD, HTItemPart.DUST, HTItemPart.GEAR)
            putAll(RagiumMaterial.Other.GLASS, HTItemPart.DUST)
            putAll(RagiumMaterial.Other.OBSIDIAN, HTItemPart.DUST)
            putAll(RagiumMaterial.Other.PAPER, HTItemPart.DUST)
            putAll(RagiumMaterial.Other.CARBON, HTItemPart.DUST)
            putAll(RagiumMaterial.Other.SILICON, HTItemPart.DUST)
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
    val PITCH_COKE: HTSimpleDeferredItem = REGISTER.registerSimpleItem(RagiumMaterial.Fuel.PITCH_COKE.materialName)

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
    val CIRCUIT_CHIP: HTSimpleDeferredItem = REGISTER.registerSimpleItem("circuit_chip")

    @JvmField
    val CIRCUIT_BOARD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("circuit_board")

    @JvmField
    val ELECTRIC_CIRCUIT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("electric_circuit")

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

    //    Events    //

    @JvmStatic
    private fun registerTooltipAppenders(event: RegisterTooltipAppendersEvent) {
        event.registerAppender(
            TooltipLocation.HEAD
        ) { stack: ItemStack, _, _, _, _, builder: Consumer<Text> ->
            if (!stack.`is`(RagiumTags.Items.SHOW_FLUID_TOOLTIPS)) return@registerAppender
            val view: HTFluidView = HTFluidCapabilities.getSlot(stack, 0) ?: return@registerAppender
            val isCreative: Boolean = stack.`is`(RagiumTags.BlockItem.STORAGES_CREATIVE.item)
            // Fluid Name
            val resource: FluidResource = view.resource
            when {
                resource.isEmpty -> HTCommonTranslation.EMPTY.translateColored(ChatFormatting.RED)

                isCreative -> HTCommonTranslation.STORED.translateColored(
                    ChatFormatting.LIGHT_PURPLE,
                    resource.hoverName,
                    ChatFormatting.GRAY,
                    HTCommonTranslation.INFINITE
                )

                else -> HTCommonTranslation.STORED_MB.translateColored(
                    ChatFormatting.LIGHT_PURPLE,
                    resource.hoverName,
                    ChatFormatting.GRAY,
                    view.amount
                )
            }.let(builder::accept)
            // Tank Capacity
            when (isCreative) {
                true -> HTCommonTranslation.CAPACITY.translateColored(
                    ChatFormatting.BLUE,
                    ChatFormatting.GRAY,
                    HTCommonTranslation.INFINITE
                )

                false -> HTCommonTranslation.CAPACITY_MB.translateColored(
                    ChatFormatting.BLUE,
                    ChatFormatting.GRAY,
                    view.currentCapacity
                )
            }.let(builder::accept)
        }
    }

    @JvmStatic
    private fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        // Fluid
        event.registerItem(
            HTFluidCapabilities.item,
            { _, access: ItemAccess -> HTPotionBucketItem.BucketHandler(access) },
            RagiumFluids.POTION.bucketHolder
        )
        event.registerItem(
            HTFluidCapabilities.item,
            { _, access: ItemAccess -> HTOreSlurryBucketItem.BucketHandler(access) },
            RagiumFluids.ORE_SLURRY.bucketHolder
        )
        // Item
    }

    @JvmStatic
    private fun modifyDefaultComponents(event: ModifyDefaultComponentsEvent) {
        // Item
        event.modify(Items.RAW_COPPER) { builder: DataComponentMap.Builder, provider: HolderLookup.Provider, _ ->
            builder.set(RagiumDataComponents.ORE_SLURRY_DATA, provider.getOrThrow(RagiumOreSlurryData.COPPER))
        }
        event.modify(Items.RAW_IRON) { builder: DataComponentMap.Builder, provider: HolderLookup.Provider, _ ->
            builder.set(RagiumDataComponents.ORE_SLURRY_DATA, provider.getOrThrow(RagiumOreSlurryData.IRON))
        }
        event.modify(Items.RAW_GOLD) { builder: DataComponentMap.Builder, provider: HolderLookup.Provider, _ ->
            builder.set(RagiumDataComponents.ORE_SLURRY_DATA, provider.getOrThrow(RagiumOreSlurryData.GOLD))
        }
        event.modify(Items.ANCIENT_DEBRIS) { builder: DataComponentMap.Builder, provider: HolderLookup.Provider, _ ->
            builder.set(RagiumDataComponents.ORE_SLURRY_DATA, provider.getOrThrow(RagiumOreSlurryData.NETHERITE_SCRAP))
        }
    }
}
