package hiiragi283.ragium.setup

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.collection.HTTable
import hiiragi283.ragium.api.extension.buildTable
import hiiragi283.ragium.api.extension.columnValues
import hiiragi283.ragium.api.extension.partially1
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.item.component.HTItemSoundEvent
import hiiragi283.ragium.api.item.component.RagiumEnchantmentHelper
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.variant.HTToolVariant
import hiiragi283.ragium.common.item.HTAzureSteelTemplateItem
import hiiragi283.ragium.common.item.HTBlastChargeItem
import hiiragi283.ragium.common.item.HTCaptureEggItem
import hiiragi283.ragium.common.item.HTCatalystItem
import hiiragi283.ragium.common.item.HTDeepSteelTemplateItem
import hiiragi283.ragium.common.item.HTDrumUpgradeItem
import hiiragi283.ragium.common.item.HTLootTicketItem
import hiiragi283.ragium.common.item.HTPotionBundleItem
import hiiragi283.ragium.common.item.HTPotionSodaItem
import hiiragi283.ragium.common.item.HTTeleportKeyItem
import hiiragi283.ragium.common.item.HTTierBasedItem
import hiiragi283.ragium.common.item.HTTraderCatalogItem
import hiiragi283.ragium.common.item.HTUniversalBundleItem
import hiiragi283.ragium.common.item.tool.HTDestructionHammerItem
import hiiragi283.ragium.common.item.tool.HTDrillItem
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storage.energy.HTComponentEnergyStorage
import hiiragi283.ragium.common.storage.fluid.HTComponentFluidHandler
import hiiragi283.ragium.common.storage.fluid.HTTeleportKeyFluidHandler
import hiiragi283.ragium.common.storage.item.HTPotionBundleItemHandler
import hiiragi283.ragium.common.tier.HTCircuitTier
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.variant.HTArmorVariant
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.common.variant.HTDrumVariant
import hiiragi283.ragium.common.variant.HTHammerToolVariant
import hiiragi283.ragium.common.variant.HTVanillaToolVariant
import hiiragi283.ragium.config.RagiumCommonConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import org.slf4j.Logger

object RagiumItems {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        REGISTER.register(eventBus)

        eventBus.addListener(::registerItemCapabilities)
        eventBus.addListener(::modifyComponents)
    }

    //    Materials    //

    // Raginite
    @JvmField
    val RAGI_ALLOY_COMPOUND: HTDeferredItem<Item> = register("${RagiumConst.RAGI_ALLOY}_compound")

    @JvmField
    val RAGI_COKE: HTDeferredItem<Item> = register("ragi_coke")

    // Deep Steel
    @JvmField
    val DEEP_SCRAP: HTDeferredItem<Item> = register("deep_scrap")

    // Wood
    @JvmField
    val COMPRESSED_SAWDUST: HTDeferredItem<Item> = register("compressed_sawdust")

    @JvmField
    val RESIN: HTDeferredItem<Item> = register("resin")

    // Oil
    @JvmField
    val TAR: HTDeferredItem<Item> = register("tar")

    // Nuclear Fuel
    @JvmField
    val POTATO_SPROUTS: HTDeferredItem<Item> = register("potato_sprouts")

    @JvmField
    val GREEN_CAKE: HTDeferredItem<Item> = register("green_cake")

    @JvmField
    val GREEN_CAKE_DUST: HTDeferredItem<Item> = register("green_cake_dust")

    @JvmField
    val GREEN_PELLET: HTDeferredItem<Item> = register("green_pellet")

    // Misc
    @JvmField
    val BASALT_MESH: HTDeferredItem<Item> = register("basalt_mesh")

    @JvmField
    val ECHO_STAR: HTDeferredItem<Item> = register("echo_star", Item.Properties().rarity(Rarity.UNCOMMON))

    @JvmField
    val ELDER_HEART: HTDeferredItem<Item> = register("elder_heart", Item.Properties().rarity(Rarity.UNCOMMON))

    @JvmField
    val WITHER_DOLl: HTDeferredItem<Item> = register("wither_doll")

    @JvmField
    val MATERIALS: HTTable<HTMaterialVariant.ItemTag, HTMaterialType, HTDeferredItem<*>> = buildTable {
        // Dusts
        listOf(
            // Vanilla - Metal
            HTVanillaMaterialType.COPPER,
            HTVanillaMaterialType.IRON,
            HTVanillaMaterialType.GOLD,
            // Vanilla - Gem
            HTVanillaMaterialType.LAPIS,
            HTVanillaMaterialType.AMETHYST,
            HTVanillaMaterialType.ECHO,
            // Vanilla - Other
            HTVanillaMaterialType.OBSIDIAN,
            // Common
            RagiumMaterialType.CINNABAR,
            RagiumMaterialType.SALTPETER,
            RagiumMaterialType.SULFUR,
            // Ragium
            RagiumMaterialType.RAGINITE,
            RagiumMaterialType.AZURE,
            RagiumMaterialType.RAGI_CRYSTAL,
            RagiumMaterialType.ELDRITCH_PEARL,
        ).forEach { put(HTItemMaterialVariant.DUST, it, register("${it.serializedName}_dust")) }
        put(HTItemMaterialVariant.DUST, HTVanillaMaterialType.WOOD, register("sawdust"))
        put(HTItemMaterialVariant.DUST, RagiumMaterialType.MEAT, register("minced_meat"))
        // Gems
        put(HTItemMaterialVariant.GEM, RagiumMaterialType.AZURE, register("azure_shard"))
        listOf(
            RagiumMaterialType.RAGI_CRYSTAL,
            RagiumMaterialType.CRIMSON_CRYSTAL,
            RagiumMaterialType.WARPED_CRYSTAL,
            RagiumMaterialType.ELDRITCH_PEARL,
        ).forEach { put(HTItemMaterialVariant.GEM, it, register(it.serializedName)) }
        // Chips
        mapOf(
            RagiumMaterialType.RAGI_CRYSTAL to RagiumConst.RAGI_CRYSTAL + "_chip",
            RagiumMaterialType.AZURE to "azure_shard_chip",
            RagiumMaterialType.ELDRITCH_PEARL to RagiumConst.ELDRITCH_PEARL + "_chip",
            HTVanillaMaterialType.ECHO to "echo_shard_chip",
        ).forEach { put(HTItemMaterialVariant.CHIP, it.key, register(it.value)) }
        // Ingots
        listOf(
            // Metals
            RagiumMaterialType.RAGI_ALLOY,
            RagiumMaterialType.ADVANCED_RAGI_ALLOY,
            RagiumMaterialType.AZURE_STEEL,
            RagiumMaterialType.DEEP_STEEL,
            RagiumMaterialType.GILDIUM,
            RagiumMaterialType.IRIDESCENTIUM,
            // Foods
            RagiumMaterialType.CHOCOLATE,
            RagiumMaterialType.MEAT,
            RagiumMaterialType.COOKED_MEAT,
        ).forEach { put(HTItemMaterialVariant.INGOT, it, register("${it.serializedName}_ingot")) }
        // Nuggets
        listOf(
            RagiumMaterialType.RAGI_ALLOY,
            RagiumMaterialType.ADVANCED_RAGI_ALLOY,
            RagiumMaterialType.AZURE_STEEL,
            RagiumMaterialType.DEEP_STEEL,
            RagiumMaterialType.GILDIUM,
            RagiumMaterialType.IRIDESCENTIUM,
        ).forEach { put(HTItemMaterialVariant.NUGGET, it, register("${it.serializedName}_nugget")) }
        // Plates
        put(HTItemMaterialVariant.PLATE, RagiumMaterialType.PLASTIC, register("plastic_plate"))

        // Fuels
        put(HTItemMaterialVariant.FUEL, RagiumMaterialType.BAMBOO_CHARCOAL, register("bamboo_charcoal"))
    }

    @JvmStatic
    fun getMaterial(variant: HTMaterialVariant.ItemTag, material: HTMaterialType): HTDeferredItem<*> = MATERIALS.get(variant, material)
        ?: error("Unknown ${variant.serializedName} item for ${material.serializedName}")

    @JvmStatic
    fun getDust(material: HTMaterialType): HTDeferredItem<*> = getMaterial(HTItemMaterialVariant.DUST, material)

    @JvmStatic
    fun getGem(material: HTMaterialType): HTDeferredItem<*> = getMaterial(HTItemMaterialVariant.GEM, material)

    @JvmStatic
    fun getIngot(material: HTMaterialType): HTDeferredItem<*> = getMaterial(HTItemMaterialVariant.INGOT, material)

    @JvmStatic
    fun getNugget(material: HTMaterialType): HTDeferredItem<*> = getMaterial(HTItemMaterialVariant.NUGGET, material)

    @JvmStatic
    fun getPlate(material: HTMaterialType): HTDeferredItem<*> = getMaterial(HTItemMaterialVariant.PLATE, material)

    @JvmField
    val COILS: Map<HTMaterialType, HTDeferredItem<*>> = listOf(RagiumMaterialType.RAGI_ALLOY, RagiumMaterialType.ADVANCED_RAGI_ALLOY)
        .associateWith { material: HTMaterialType -> register("${material.serializedName}_coil") }

    @JvmField
    val CIRCUITS: Map<HTCircuitTier, HTDeferredItem<*>> = HTCircuitTier.entries.associateWith { tier: HTCircuitTier ->
        register("${tier.serializedName}_circuit", ::HTTierBasedItem.partially1(tier))
    }

    @JvmField
    val COMPONENTS: Map<HTComponentTier, HTDeferredItem<*>> = HTComponentTier.entries.associateWith { tier: HTComponentTier ->
        register("${tier.serializedName}_component", ::HTTierBasedItem.partially1(tier))
    }

    @JvmStatic
    fun getCoil(material: HTMaterialType): HTDeferredItem<*> = COILS[material]!!

    @JvmStatic
    fun getCircuit(tier: HTCircuitTier): HTDeferredItem<*> = CIRCUITS[tier]!!

    @JvmStatic
    fun getComponent(tier: HTComponentTier): HTDeferredItem<*> = COMPONENTS[tier]!!

    //    Armors    //

    @JvmField
    val AZURE_ARMORS: Map<HTArmorVariant, HTDeferredItem<*>> = HTArmorVariant.entries.associateWith { variant: HTArmorVariant ->
        variant.registerItem(REGISTER, RagiumMaterialType.AZURE_STEEL, RagiumArmorMaterials.AZURE_STEEL, 20)
    }

    @JvmField
    val DEEP_ARMORS: Map<HTArmorVariant, HTDeferredItem<*>> = HTArmorVariant.entries.associateWith { variant: HTArmorVariant ->
        variant.registerItem(REGISTER, RagiumMaterialType.DEEP_STEEL, RagiumArmorMaterials.DEEP_STEEL, 20)
    }

    @JvmStatic
    fun getAzureArmor(variant: HTArmorVariant): HTDeferredItem<*> = AZURE_ARMORS[variant]!!

    @JvmStatic
    fun getDeepArmor(variant: HTArmorVariant): HTDeferredItem<*> = DEEP_ARMORS[variant]!!

    //    Tools    //

    // Raginite
    @JvmField
    val WRENCH: HTDeferredItem<Item> = register("wrench", Item.Properties().stacksTo(1))

    @JvmField
    val MAGNET: HTDeferredItem<Item> = register("ragi_magnet")

    @JvmField
    val ADVANCED_MAGNET: HTDeferredItem<Item> = register("advanced_ragi_magnet")

    @JvmField
    val DYNAMIC_LANTERN: HTDeferredItem<Item> = register("ragi_lantern")

    @JvmField
    val LOOT_TICKET: HTDeferredItem<Item> = register("ragi_ticket", ::HTLootTicketItem)

    @JvmField
    val NIGHT_VISION_GOGGLES: HTDeferredItem<Item> = register("night_vision_goggles", Item.Properties().stacksTo(1))

    // Azure
    @JvmField
    val AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE: HTDeferredItem<Item> =
        REGISTER.register("${RagiumConst.AZURE_STEEL}_upgrade_smithing_template", ::HTAzureSteelTemplateItem)

    @JvmField
    val DRILL: HTDeferredItem<Item> = register("drill", ::HTDrillItem)

    // Crimson
    @JvmField
    val BLAST_CHARGE: HTDeferredItem<Item> = register("blast_charge", ::HTBlastChargeItem)

    // Warped
    @JvmField
    val TELEPORT_KEY: HTDeferredItem<Item> = register("teleport_key", ::HTTeleportKeyItem)

    // Eldritch
    @JvmField
    val ELDRITCH_EGG: HTDeferredItem<Item> = register("eldritch_egg", ::HTCaptureEggItem)

    @JvmField
    val UNIVERSAL_BUNDLE: HTDeferredItem<Item> = register("universal_bundle", ::HTUniversalBundleItem)

    // Deep
    @JvmField
    val DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE: HTDeferredItem<Item> =
        REGISTER.register("${RagiumConst.DEEP_STEEL}_upgrade_smithing_template", ::HTDeepSteelTemplateItem)

    // Other
    @JvmField
    val POTION_BUNDLE: HTDeferredItem<Item> = register("potion_bundle", ::HTPotionBundleItem)

    @JvmField
    val SLOT_COVER: HTDeferredItem<Item> = register("slot_cover")

    @JvmField
    val TRADER_CATALOG: HTDeferredItem<Item> = register("trader_catalog", ::HTTraderCatalogItem)

    @JvmField
    val MEDIUM_DRUM_UPGRADE: HTDeferredItem<Item> = register("medium_drum_upgrade", HTDrumUpgradeItem::Medium)

    @JvmField
    val LARGE_DRUM_UPGRADE: HTDeferredItem<Item> = register("large_drum_upgrade", HTDrumUpgradeItem::Large)

    @JvmField
    val HUGE_DRUM_UPGRADE: HTDeferredItem<Item> = register("huge_drum_upgrade", HTDrumUpgradeItem::Huge)

    @JvmField
    val TOOLS: HTTable<HTToolVariant, HTMaterialType, HTDeferredItem<*>> = buildTable {
        val consumer: (HTToolVariant, HTMaterialType, Tier) -> Unit = { variant: HTToolVariant, material: HTMaterialType, tier: Tier ->
            put(
                variant,
                material,
                variant.registerItem(REGISTER, material, tier),
            )
        }

        // Hammer
        consumer(HTHammerToolVariant, HTVanillaMaterialType.IRON, Tiers.IRON)
        consumer(HTHammerToolVariant, HTVanillaMaterialType.DIAMOND, Tiers.DIAMOND)
        consumer(HTHammerToolVariant, HTVanillaMaterialType.NETHERITE, Tiers.NETHERITE)

        consumer(HTHammerToolVariant, RagiumMaterialType.RAGI_ALLOY, RagiumToolTiers.RAGI_ALLOY)
        consumer(HTHammerToolVariant, RagiumMaterialType.AZURE_STEEL, RagiumToolTiers.AZURE_STEEL)
        consumer(HTHammerToolVariant, RagiumMaterialType.DEEP_STEEL, RagiumToolTiers.DEEP_STEEL)

        put(
            HTHammerToolVariant,
            RagiumMaterialType.RAGI_CRYSTAL,
            register("ragi_crystal_hammer", ::HTDestructionHammerItem),
        )
        // Tools
        for (variant: HTToolVariant in HTVanillaToolVariant.entries) {
            // Azure
            consumer(variant, RagiumMaterialType.AZURE_STEEL, RagiumToolTiers.AZURE_STEEL)
            // Deep
            consumer(variant, RagiumMaterialType.DEEP_STEEL, RagiumToolTiers.DEEP_STEEL)
        }
    }

    @JvmStatic
    fun getTool(variant: HTToolVariant, material: HTMaterialType): HTDeferredItem<*> = TOOLS.get(variant, material)
        ?: error("Unknown ${variant.serializedName} item for ${material.serializedName}")

    @JvmStatic
    private fun getAzureTool(variant: HTVanillaToolVariant): HTDeferredItem<*> = getTool(variant, RagiumMaterialType.AZURE_STEEL)

    @JvmStatic
    private fun getDeepTool(variant: HTVanillaToolVariant): HTDeferredItem<*> = getTool(variant, RagiumMaterialType.DEEP_STEEL)

    //    Foods    //

    @JvmStatic
    private fun registerFood(
        name: String,
        foodProperties: FoodProperties,
        properties: Item.Properties = Item.Properties(),
    ): HTDeferredItem<Item> = register(name, properties.food(foodProperties))

    @JvmField
    val ICE_CREAM: HTDeferredItem<Item> = registerFood("ice_cream", RagiumFoods.ICE_CREAM)

    @JvmField
    val ICE_CREAM_SODA: HTDeferredItem<Item> = register("ice_cream_soda", ::HTPotionSodaItem)

    // Meat
    @JvmField
    val CANNED_COOKED_MEAT: HTDeferredItem<Item> = registerFood("canned_${RagiumConst.COOKED_MEAT}", RagiumFoods.CANNED_COOKED_MEAT)

    // Sponge
    @JvmField
    val MELON_PIE: HTDeferredItem<Item> = registerFood("melon_pie", RagiumFoods.MELON_PIE)

    @JvmField
    val SWEET_BERRIES_CAKE_SLICE: HTDeferredItem<Item> = registerFood("sweet_berries_cake_slice", RagiumFoods.SWEET_BERRIES_CAKE)

    // Cherry
    @JvmField
    val RAGI_CHERRY: HTDeferredItem<Item> = registerFood(RagiumConst.RAGI_CHERRY, RagiumFoods.RAGI_CHERRY)

    @JvmField
    val FEVER_CHERRY: HTDeferredItem<Item> =
        registerFood("fever_cherry", RagiumFoods.FEVER_CHERRY, properties = Item.Properties().rarity(Rarity.RARE))

    // Other
    @JvmField
    val BOTTLED_BEE: HTDeferredItem<Item> = register("bottled_bee")

    @JvmField
    val AMBROSIA: HTDeferredItem<Item> =
        registerFood("ambrosia", RagiumFoods.AMBROSIA, properties = Item.Properties().rarity(Rarity.EPIC))

    //    Machine Parts    //

    @JvmField
    val GRAVITATIONAL_UNIT: HTDeferredItem<Item> = register("gravitational_unit")

    // Catalyst
    @JvmField
    val PLATING_CATALYST: HTDeferredItem<Item> = register("plating_catalyst", ::HTCatalystItem)

    @JvmField
    val POLYMER_CATALYST: HTDeferredItem<Item> = register("polymer_catalyst", ::HTCatalystItem)

    // LED
    @JvmField
    val LUMINOUS_PASTE: HTDeferredItem<Item> = register("luminous_paste")

    @JvmField
    val LED: HTDeferredItem<Item> = register("led")

    @JvmField
    val SOLAR_PANEL: HTDeferredItem<Item> = register("solar_panel")

    // Redstone
    @JvmField
    val REDSTONE_BOARD: HTDeferredItem<Item> = register("redstone_board")

    // Plastics
    @JvmField
    val POLYMER_RESIN: HTDeferredItem<Item> = register("polymer_resin")

    @JvmField
    val SYNTHETIC_FIBER: HTDeferredItem<Item> = register("synthetic_fiber")

    @JvmField
    val SYNTHETIC_LEATHER: HTDeferredItem<Item> = register("synthetic_leather")

    @JvmField
    val CIRCUIT_BOARD: HTDeferredItem<Item> = register("circuit_board")

    @JvmField
    val ADVANCED_CIRCUIT_BOARD: HTDeferredItem<Item> = register("advanced_circuit_board")

    //    Extensions    //

    @JvmStatic
    private fun register(name: String, properties: Item.Properties = Item.Properties()): HTDeferredItem<Item> =
        REGISTER.registerSimpleItem(name, properties)

    @JvmStatic
    private fun <T : Item> register(
        name: String,
        factory: (Item.Properties) -> T,
        properties: Item.Properties = Item.Properties(),
    ): HTDeferredItem<T> = REGISTER.registerItem(name, factory, properties)

    //    Event    //

    @JvmStatic
    private fun registerItemCapabilities(event: RegisterCapabilitiesEvent) {
        // Item
        HTCapabilityCodec.registerItem(event, provider(9, ::HTPotionBundleItemHandler), POTION_BUNDLE)

        // Fluid
        for (variant: HTDrumVariant in HTDrumVariant.entries) {
            val capacity: Int = when (variant) {
                HTDrumVariant.SMALL -> RagiumCommonConfig::smallDrumCapacity
                HTDrumVariant.MEDIUM -> RagiumCommonConfig::mediumDrumCapacity
                HTDrumVariant.LARGE -> RagiumCommonConfig::largeDrumCapacity
                HTDrumVariant.HUGE -> RagiumCommonConfig::hugeDrumCapacity
            }(RagiumConfig.COMMON).asInt
            HTCapabilityCodec.registerFluid(event, providerEnch(capacity, ::HTComponentFluidHandler), variant)
        }
        HTCapabilityCodec.registerFluid(event, providerEnch(8000, ::HTTeleportKeyFluidHandler), TELEPORT_KEY)

        // Energy
        HTCapabilityCodec.registerEnergy(event, providerEnch(160000, ::HTComponentEnergyStorage), DRILL)

        LOGGER.info("Registered item capabilities!")
    }

    @JvmStatic
    private fun <T : Any> provider(capacity: Int, factory: (ItemStack, Int) -> T): (ItemStack) -> T? = { stack: ItemStack ->
        factory(stack, capacity)
    }

    @JvmStatic
    private fun <T : Any> providerEnch(capacity: Int, factory: (ItemStack, Int) -> T): (ItemStack) -> T? = { stack: ItemStack ->
        factory(stack, RagiumEnchantmentHelper.INSTANCE.processStorageCapacity(null, stack, capacity))
    }

    @JvmStatic
    private fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        // Tools
        fun setEnch(item: ItemLike, ench: ResourceKey<Enchantment>, level: Int = 1) {
            event.modify(item) { builder: DataComponentPatch.Builder ->
                builder.set(RagiumDataComponents.INTRINSIC_ENCHANTMENT, HTIntrinsicEnchantment(ench, level))
            }
        }
        setEnch(getAzureTool(HTVanillaToolVariant.AXE), Enchantments.SILK_TOUCH)
        setEnch(getAzureTool(HTVanillaToolVariant.HOE), Enchantments.SILK_TOUCH)
        setEnch(getAzureTool(HTVanillaToolVariant.PICKAXE), Enchantments.SILK_TOUCH)
        setEnch(getAzureTool(HTVanillaToolVariant.SHOVEL), Enchantments.SILK_TOUCH)

        setEnch(getDeepTool(HTVanillaToolVariant.PICKAXE), Enchantments.FORTUNE, 5)
        setEnch(getDeepTool(HTVanillaToolVariant.SWORD), RagiumEnchantments.NOISE_CANCELING, 5)
        // Other
        event.modify(ECHO_STAR) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
            builder.set(
                RagiumDataComponents.IMMUNE_DAMAGE_TYPES,
                HTKeyOrTagHelper.INSTANCE.create(RagiumModTags.DamageTypes.IS_SONIC),
            )
        }

        event.modify(UNIVERSAL_BUNDLE) { builder: DataComponentPatch.Builder ->
            builder.set(RagiumDataComponents.COLOR, DyeColor.WHITE)
        }

        event.modify(ICE_CREAM_SODA) { builder: DataComponentPatch.Builder ->
            builder.set(RagiumDataComponents.DRINK_SOUND, HTItemSoundEvent.create(SoundEvents.GENERIC_DRINK))
            builder.set(RagiumDataComponents.EAT_SOUND, HTItemSoundEvent.create(SoundEvents.GENERIC_DRINK))
        }

        val iridescent: (DataComponentPatch.Builder) -> Unit = { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
            builder.set(DataComponents.RARITY, Rarity.RARE)
        }
        for (block: ItemLike in RagiumBlocks.MATERIALS.columnValues(RagiumMaterialType.IRIDESCENTIUM)) {
            event.modify(block, iridescent)
        }
        for (item: ItemLike in MATERIALS.columnValues(RagiumMaterialType.IRIDESCENTIUM)) {
            event.modify(item, iridescent)
        }

        event.modify(getIngot(RagiumMaterialType.CHOCOLATE)) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, RagiumFoods.CHOCOLATE)
        }
        event.modify(getIngot(RagiumMaterialType.MEAT)) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, Foods.BEEF)
        }
        event.modify(getIngot(RagiumMaterialType.COOKED_MEAT)) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, Foods.COOKED_BEEF)
        }

        event.modify(HTDeviceVariant.CEU) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.RARITY, Rarity.EPIC)
        }

        LOGGER.info("Modified default item components!")
    }
}
