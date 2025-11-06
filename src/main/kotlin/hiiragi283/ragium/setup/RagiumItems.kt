package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.item.component.HTItemSoundEvent
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.storage.capability.HTEnergyCapabilities
import hiiragi283.ragium.api.storage.capability.HTExperienceCapabilities
import hiiragi283.ragium.api.storage.capability.HTFluidCapabilities
import hiiragi283.ragium.api.storage.capability.HTItemCapabilities
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.experience.HTExperienceTank
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.api.variant.HTToolVariant
import hiiragi283.ragium.common.inventory.container.HTPotionBundleContainerMenu
import hiiragi283.ragium.common.item.HTBlastChargeItem
import hiiragi283.ragium.common.item.HTCaptureEggItem
import hiiragi283.ragium.common.item.HTCatalystItem
import hiiragi283.ragium.common.item.HTDrumUpgradeItem
import hiiragi283.ragium.common.item.HTDrumWithMinecartItem
import hiiragi283.ragium.common.item.HTDynamicLanternItem
import hiiragi283.ragium.common.item.HTLootTicketItem
import hiiragi283.ragium.common.item.HTMagnetItem
import hiiragi283.ragium.common.item.HTPotionBundleItem
import hiiragi283.ragium.common.item.HTPotionSodaItem
import hiiragi283.ragium.common.item.HTTeleportKeyItem
import hiiragi283.ragium.common.item.HTTierBasedItem
import hiiragi283.ragium.common.item.HTTraderCatalogItem
import hiiragi283.ragium.common.item.HTUniversalBundleItem
import hiiragi283.ragium.common.item.base.HTSmithingTemplateItem
import hiiragi283.ragium.common.item.tool.HTDestructionHammerItem
import hiiragi283.ragium.common.item.tool.HTDrillItem
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.storage.energy.HTComponentEnergyHandler
import hiiragi283.ragium.common.storage.energy.battery.HTComponentEnergyBattery
import hiiragi283.ragium.common.storage.experience.HTBottleExperienceHandler
import hiiragi283.ragium.common.storage.experience.HTComponentExperienceHandler
import hiiragi283.ragium.common.storage.fluid.HTComponentFluidHandler
import hiiragi283.ragium.common.storage.fluid.tank.HTComponentFluidTank
import hiiragi283.ragium.common.storage.item.HTComponentItemHandler
import hiiragi283.ragium.common.storage.item.slot.HTComponentItemSlot
import hiiragi283.ragium.common.tier.HTCircuitTier
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.common.util.HTItemHelper
import hiiragi283.ragium.common.variant.HTArmorVariant
import hiiragi283.ragium.common.variant.HTHammerToolVariant
import hiiragi283.ragium.common.variant.HTVanillaToolVariant
import hiiragi283.ragium.common.variant.VanillaEquipmentMaterial
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent

object RagiumItems {
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
    val RAGI_ALLOY_COMPOUND: HTSimpleDeferredItem = REGISTER.registerSimpleItem("${RagiumConst.RAGI_ALLOY}_compound")

    @JvmField
    val RAGI_COKE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ragi_coke")

    // Wood
    @JvmField
    val COMPRESSED_SAWDUST: HTSimpleDeferredItem = REGISTER.registerSimpleItem("compressed_sawdust")

    @JvmField
    val RESIN: HTSimpleDeferredItem = REGISTER.registerSimpleItem("resin")

    // Oil
    @JvmField
    val TAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("tar")

    // Nuclear Fuel
    @JvmField
    val POTATO_SPROUTS: HTSimpleDeferredItem = REGISTER.registerSimpleItem("potato_sprouts")

    @JvmField
    val GREEN_CAKE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("green_cake")

    @JvmField
    val GREEN_CAKE_DUST: HTSimpleDeferredItem = REGISTER.registerSimpleItem("green_cake_dust")

    @JvmField
    val GREEN_PELLET: HTSimpleDeferredItem = REGISTER.registerSimpleItem("green_pellet")

    // Misc
    @JvmField
    val BASALT_MESH: HTSimpleDeferredItem = REGISTER.registerSimpleItem("basalt_mesh")

    @JvmField
    val ECHO_STAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("echo_star") { it.rarity(Rarity.UNCOMMON) }

    @JvmField
    val ELDER_HEART: HTSimpleDeferredItem = REGISTER.registerSimpleItem("elder_heart") { it.rarity(Rarity.UNCOMMON) }

    @JvmField
    val WITHER_DOLl: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wither_doll")

    val MATERIALS: ImmutableTable<HTMaterialPrefix, HTMaterialKey, HTSimpleDeferredItem> = buildTable {
        fun register(prefix: HTPrefixLike, material: HTMaterialLike, name: String) {
            this[prefix.asMaterialPrefix(), material.asMaterialKey()] = REGISTER.registerSimpleItem(name)
        }

        // Dusts
        arrayOf(
            // Vanilla - Metal
            VanillaMaterialKeys.COPPER,
            VanillaMaterialKeys.IRON,
            VanillaMaterialKeys.GOLD,
            // Vanilla - Gem
            VanillaMaterialKeys.LAPIS,
            VanillaMaterialKeys.AMETHYST,
            VanillaMaterialKeys.ECHO,
            // Vanilla - Other
            VanillaMaterialKeys.OBSIDIAN,
            // Common
            CommonMaterialKeys.Gems.CINNABAR,
            CommonMaterialKeys.Gems.SALTPETER,
            CommonMaterialKeys.Gems.SULFUR,
            // Ragium
            RagiumMaterialKeys.RAGINITE,
            RagiumMaterialKeys.AZURE,
            RagiumMaterialKeys.RAGI_CRYSTAL,
            RagiumMaterialKeys.ELDRITCH_PEARL,
        ).forEach { register(CommonMaterialPrefixes.DUST, it.asMaterialKey(), "${it.asMaterialName()}_dust") }
        register(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.WOOD, "sawdust")
        register(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.MEAT, "minced_meat")
        // Gems
        register(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.AZURE, "azure_shard")
        arrayOf(
            RagiumMaterialKeys.RAGI_CRYSTAL,
            RagiumMaterialKeys.CRIMSON_CRYSTAL,
            RagiumMaterialKeys.WARPED_CRYSTAL,
            RagiumMaterialKeys.ELDRITCH_PEARL,
        ).forEach { register(CommonMaterialPrefixes.GEM, it, it.name) }
        // Ingots
        arrayOf(
            // Metals
            RagiumMaterialKeys.RAGI_ALLOY,
            RagiumMaterialKeys.ADVANCED_RAGI_ALLOY,
            RagiumMaterialKeys.AZURE_STEEL,
            RagiumMaterialKeys.DEEP_STEEL,
            RagiumMaterialKeys.GILDIUM,
            RagiumMaterialKeys.IRIDESCENTIUM,
            // Foods
            RagiumMaterialKeys.CHOCOLATE,
            RagiumMaterialKeys.MEAT,
            RagiumMaterialKeys.COOKED_MEAT,
        ).forEach { register(CommonMaterialPrefixes.INGOT, it, "${it.name}_ingot") }
        // Nuggets
        arrayOf(
            RagiumMaterialKeys.RAGI_ALLOY,
            RagiumMaterialKeys.ADVANCED_RAGI_ALLOY,
            RagiumMaterialKeys.AZURE_STEEL,
            RagiumMaterialKeys.DEEP_STEEL,
            RagiumMaterialKeys.GILDIUM,
            RagiumMaterialKeys.IRIDESCENTIUM,
        ).forEach { register(CommonMaterialPrefixes.NUGGET, it, "${it.name}_nugget") }
        // Plates
        register(CommonMaterialPrefixes.PLATE, CommonMaterialKeys.PLASTIC, "plastic_plate")

        // Fuels
        register(CommonMaterialPrefixes.FUEL, RagiumMaterialKeys.BAMBOO_CHARCOAL, "bamboo_charcoal")
        // Scraps
        register(CommonMaterialPrefixes.SCRAP, RagiumMaterialKeys.DEEP_STEEL, "deep_scrap")
    }

    @JvmStatic
    fun getMaterial(prefix: HTPrefixLike, material: HTMaterialLike): HTSimpleDeferredItem =
        MATERIALS[prefix.asMaterialPrefix(), material.asMaterialKey()]
            ?: error("Unknown $prefix item for ${material.asMaterialName()}")

    @JvmStatic
    fun getDust(material: HTMaterialLike): HTSimpleDeferredItem = getMaterial(CommonMaterialPrefixes.DUST, material)

    @JvmStatic
    fun getGem(material: HTMaterialLike): HTSimpleDeferredItem = getMaterial(CommonMaterialPrefixes.GEM, material)

    @JvmStatic
    fun getIngot(material: HTMaterialLike): HTSimpleDeferredItem = getMaterial(CommonMaterialPrefixes.INGOT, material)

    @JvmStatic
    fun getNugget(material: HTMaterialLike): HTSimpleDeferredItem = getMaterial(CommonMaterialPrefixes.NUGGET, material)

    @JvmStatic
    fun getPlate(material: HTMaterialLike): HTSimpleDeferredItem = getMaterial(CommonMaterialPrefixes.PLATE, material)

    @JvmStatic
    fun getScrap(material: HTMaterialLike): HTItemHolderLike = when (material.asMaterialKey()) {
        VanillaMaterialKeys.NETHERITE -> Items.NETHERITE_SCRAP.toHolderLike()
        else -> getMaterial(CommonMaterialPrefixes.SCRAP, material)
    }

    @JvmStatic
    fun getMaterialMap(prefix: HTPrefixLike): Map<HTMaterialKey, HTSimpleDeferredItem> = MATERIALS.row(prefix.asMaterialPrefix())

    @JvmField
    val COILS: Map<HTMaterialKey, HTSimpleDeferredItem> = arrayOf(RagiumMaterialKeys.RAGI_ALLOY, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)
        .associateWith { key: HTMaterialKey -> REGISTER.registerSimpleItem("${key.name}_coil") }

    @JvmField
    val CIRCUITS: Map<HTCircuitTier, HTDeferredItem<*>> = HTCircuitTier.entries.associateWith { tier: HTCircuitTier ->
        REGISTER.registerItemWith("${tier.asMaterialName()}_circuit", tier, ::HTTierBasedItem)
    }

    @JvmField
    val COMPONENTS: Map<HTComponentTier, HTDeferredItem<*>> = HTComponentTier.entries.associateWith { tier: HTComponentTier ->
        REGISTER.registerItemWith("${tier.asMaterialName()}_component", tier, ::HTTierBasedItem)
    }

    @JvmStatic
    fun getCoil(key: HTMaterialKey): HTDeferredItem<*> = COILS[key]!!

    @JvmStatic
    fun getCircuit(tier: HTCircuitTier): HTDeferredItem<*> = CIRCUITS[tier]!!

    @JvmStatic
    fun getComponent(tier: HTComponentTier): HTDeferredItem<*> = COMPONENTS[tier]!!

    //    Armors    //

    val ARMORS: ImmutableTable<HTArmorVariant, HTMaterialKey, HTDeferredItem<*>> = buildTable {
        fun register(variant: HTArmorVariant, material: HTEquipmentMaterial) {
            this[variant, material.asMaterialKey()] = variant.registerItem(REGISTER, material)
        }
        // Azure, Deep
        for (variant: HTArmorVariant in HTArmorVariant.entries) {
            register(variant, RagiumEquipmentMaterials.AZURE_STEEL)
            register(variant, RagiumEquipmentMaterials.DEEP_STEEL)
        }
    }

    @JvmField
    val NIGHT_VISION_GOGGLES: HTDeferredItem<*> =
        HTArmorVariant.HELMET.registerItem(REGISTER, RagiumEquipmentMaterials.RAGI_CRYSTAL, "night_vision_goggles")

    @JvmStatic
    fun getArmor(variant: HTArmorVariant, material: HTMaterialLike): HTDeferredItem<*> = ARMORS[variant, material.asMaterialKey()]
        ?: error("Unknown ${variant.variantName()} armor item for ${material.asMaterialName()}")

    @JvmStatic
    fun getArmorMap(material: HTMaterialLike): Map<HTArmorVariant, HTDeferredItem<*>> = ARMORS.column(material.asMaterialKey())

    //    Tools    //

    // Raginite
    @JvmField
    val WRENCH: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wrench") { it.stacksTo(1) }

    @JvmField
    val MAGNET: HTSimpleDeferredItem =
        REGISTER.registerItemWith("ragi_magnet", RagiumConfig.COMMON.basicMagnetRange, ::HTMagnetItem)

    @JvmField
    val ADVANCED_MAGNET: HTSimpleDeferredItem =
        REGISTER.registerItemWith("advanced_ragi_magnet", RagiumConfig.COMMON.advancedMagnetRange, ::HTMagnetItem)

    @JvmField
    val DYNAMIC_LANTERN: HTSimpleDeferredItem = REGISTER.registerItem("ragi_lantern", ::HTDynamicLanternItem)

    @JvmField
    val LOOT_TICKET: HTSimpleDeferredItem = REGISTER.registerItem("ragi_ticket", ::HTLootTicketItem)

    // Azure
    @JvmField
    val AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE: HTSimpleDeferredItem = REGISTER.register(
        "${RagiumConst.AZURE_STEEL}_upgrade_smithing_template",
    ) { _ ->
        HTSmithingTemplateItem(
            RagiumTranslation.AZURE_STEEL_UPGRADE_APPLIES_TO,
            RagiumTranslation.AZURE_STEEL_UPGRADE_INGREDIENTS,
            RagiumTranslation.AZURE_STEEL_UPGRADE,
            RagiumTranslation.AZURE_STEEL_UPGRADE_BASE_SLOT_DESCRIPTION,
            RagiumTranslation.AZURE_STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION,
        )
    }

    @JvmField
    val DRILL: HTSimpleDeferredItem = REGISTER.registerItem("drill", ::HTDrillItem)

    // Crimson
    @JvmField
    val BLAST_CHARGE: HTSimpleDeferredItem = REGISTER.registerItem("blast_charge", ::HTBlastChargeItem)

    // Warped
    @JvmField
    val TELEPORT_KEY: HTSimpleDeferredItem = REGISTER.registerItem("teleport_key", ::HTTeleportKeyItem)

    // Eldritch
    @JvmField
    val ELDRITCH_EGG: HTSimpleDeferredItem = REGISTER.registerItem("eldritch_egg", ::HTCaptureEggItem)

    @JvmField
    val UNIVERSAL_BUNDLE: HTSimpleDeferredItem = REGISTER.registerItem("universal_bundle", ::HTUniversalBundleItem)

    // Deep
    @JvmField
    val DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE: HTSimpleDeferredItem = REGISTER.register(
        "${RagiumConst.DEEP_STEEL}_upgrade_smithing_template",
    ) { _ ->
        HTSmithingTemplateItem(
            RagiumTranslation.DEEP_STEEL_UPGRADE_APPLIES_TO,
            RagiumTranslation.DEEP_STEEL_UPGRADE_INGREDIENTS,
            RagiumTranslation.DEEP_STEEL_UPGRADE,
            RagiumTranslation.DEEP_STEEL_UPGRADE_BASE_SLOT_DESCRIPTION,
            RagiumTranslation.DEEP_STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION,
        )
    }

    // Other
    @JvmField
    val POTION_BUNDLE: HTSimpleDeferredItem = REGISTER.registerItem("potion_bundle", ::HTPotionBundleItem)

    @JvmField
    val SLOT_COVER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("slot_cover")

    @JvmField
    val TRADER_CATALOG: HTSimpleDeferredItem = REGISTER.registerItem("trader_catalog", ::HTTraderCatalogItem)

    @JvmField
    val MEDIUM_DRUM_UPGRADE: HTSimpleDeferredItem =
        REGISTER.registerItem("medium_drum_upgrade", HTDrumUpgradeItem::Medium)

    @JvmField
    val LARGE_DRUM_UPGRADE: HTSimpleDeferredItem = REGISTER.registerItem("large_drum_upgrade", HTDrumUpgradeItem::Large)

    @JvmField
    val HUGE_DRUM_UPGRADE: HTSimpleDeferredItem = REGISTER.registerItem("huge_drum_upgrade", HTDrumUpgradeItem::Huge)

    val TOOLS: ImmutableTable<HTToolVariant, HTMaterialKey, HTDeferredItem<*>> = buildTable {
        val consumer: (HTToolVariant, HTEquipmentMaterial) -> Unit = { variant: HTToolVariant, material: HTEquipmentMaterial ->
            this[variant, material.asMaterialKey()] = variant.registerItem(REGISTER, material)
        }

        // Hammer
        consumer(HTHammerToolVariant, VanillaEquipmentMaterial.IRON)
        consumer(HTHammerToolVariant, VanillaEquipmentMaterial.DIAMOND)
        consumer(HTHammerToolVariant, VanillaEquipmentMaterial.NETHERITE)

        consumer(HTHammerToolVariant, RagiumEquipmentMaterials.RAGI_ALLOY)
        consumer(HTHammerToolVariant, RagiumEquipmentMaterials.AZURE_STEEL)
        consumer(HTHammerToolVariant, RagiumEquipmentMaterials.DEEP_STEEL)

        this[HTHammerToolVariant, RagiumMaterialKeys.RAGI_CRYSTAL] = REGISTER.registerItemWith(
            "ragi_crystal_hammer",
            RagiumEquipmentMaterials.RAGI_CRYSTAL,
            HTDestructionHammerItem::create,
        )
        // Tools
        for (variant: HTToolVariant in HTVanillaToolVariant.entries) {
            // Azure
            consumer(variant, RagiumEquipmentMaterials.AZURE_STEEL)
            // Deep
            consumer(variant, RagiumEquipmentMaterials.DEEP_STEEL)
        }
    }

    @JvmStatic
    fun getTool(variant: HTToolVariant, material: HTMaterialLike): HTDeferredItem<*> = TOOLS[variant, material.asMaterialKey()]
        ?: error("Unknown ${variant.variantName()} item for ${material.asMaterialName()}")

    @JvmStatic
    fun getToolMap(material: HTMaterialLike): Map<HTToolVariant, HTDeferredItem<*>> = TOOLS.column(material.asMaterialKey())

    //    Foods    //

    @JvmStatic
    private fun registerFood(name: String, foodProperties: FoodProperties): HTSimpleDeferredItem =
        REGISTER.registerSimpleItem(name) { it.food(foodProperties) }

    @JvmField
    val ICE_CREAM: HTSimpleDeferredItem = registerFood("ice_cream", RagiumFoods.ICE_CREAM)

    @JvmField
    val ICE_CREAM_SODA: HTSimpleDeferredItem = REGISTER.registerItem("ice_cream_soda", ::HTPotionSodaItem)

    // Meat
    @JvmField
    val CANNED_COOKED_MEAT: HTSimpleDeferredItem = registerFood("canned_${RagiumConst.COOKED_MEAT}", RagiumFoods.CANNED_COOKED_MEAT)

    // Sponge
    @JvmField
    val MELON_PIE: HTSimpleDeferredItem = registerFood("melon_pie", RagiumFoods.MELON_PIE)

    @JvmField
    val SWEET_BERRIES_CAKE_SLICE: HTSimpleDeferredItem = registerFood("sweet_berries_cake_slice", RagiumFoods.SWEET_BERRIES_CAKE)

    // Cherry
    @JvmField
    val RAGI_CHERRY: HTSimpleDeferredItem = registerFood(RagiumConst.RAGI_CHERRY, RagiumFoods.RAGI_CHERRY)

    @JvmField
    val FEVER_CHERRY: HTSimpleDeferredItem = registerFood("fever_cherry", RagiumFoods.FEVER_CHERRY)

    // Other
    @JvmField
    val BOTTLED_BEE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("bottled_bee")

    @JvmField
    val AMBROSIA: HTSimpleDeferredItem = registerFood("ambrosia", RagiumFoods.AMBROSIA)

    //    Machine Parts    //

    @JvmField
    val GRAVITATIONAL_UNIT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("gravitational_unit")

    // Catalyst
    @JvmField
    val PLATING_CATALYST: HTSimpleDeferredItem = REGISTER.registerItem("plating_catalyst", ::HTCatalystItem)

    @JvmField
    val POLYMER_CATALYST: HTSimpleDeferredItem = REGISTER.registerItem("polymer_catalyst", ::HTCatalystItem)

    // LED
    @JvmField
    val LUMINOUS_PASTE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("luminous_paste")

    @JvmField
    val LED: HTSimpleDeferredItem = REGISTER.registerSimpleItem("led")

    @JvmField
    val SOLAR_PANEL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("solar_panel")

    // Redstone
    @JvmField
    val REDSTONE_BOARD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("redstone_board")

    // Plastics
    @JvmField
    val POLYMER_RESIN: HTSimpleDeferredItem = REGISTER.registerSimpleItem("polymer_resin")

    @JvmField
    val SYNTHETIC_FIBER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_fiber")

    @JvmField
    val SYNTHETIC_LEATHER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_leather")

    @JvmField
    val CIRCUIT_BOARD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("circuit_board")

    @JvmField
    val ADVANCED_CIRCUIT_BOARD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("advanced_circuit_board")

    //    Vehicles    //

    @JvmField
    val DRUM_MINECARTS: Map<HTDrumTier, HTSimpleDeferredItem> = HTDrumTier.entries.associateWith { tier: HTDrumTier ->
        REGISTER.registerItemWith(tier.entityPath, tier, ::HTDrumWithMinecartItem)
    }

    //    Event    //

    @JvmStatic
    private fun registerItemCapabilities(event: RegisterCapabilitiesEvent) {
        // Item
        for ((_, block: ItemLike) in RagiumBlocks.CRATES) {
            registerItem(
                event,
                { stack: ItemStack -> listOf(HTComponentItemSlot.create(stack, 1)) },
                block,
            )
        }
        registerItem(
            event,
            { stack: ItemStack ->
                (0..<9).map { slot: Int ->
                    HTComponentItemSlot.create(stack, slot, filter = HTPotionBundleContainerMenu::filterPotion)
                }
            },
            POTION_BUNDLE,
        )

        // Fluid
        for ((tier: HTDrumTier, block: ItemLike) in RagiumBlocks.DRUMS) {
            registerFluid(
                event,
                { stack: ItemStack ->
                    val capacity: Int = HTItemHelper.processStorageCapacity(null, stack, tier.getDefaultCapacity())
                    HTComponentFluidTank.create(stack, capacity)
                },
                block,
            )
        }
        registerFluid(
            event,
            { stack: ItemStack ->
                HTComponentFluidTank.create(stack, Int.MAX_VALUE, filter = RagiumFluidContents.EXPERIENCE::isOf)
            },
            RagiumBlocks.EXP_DRUM,
        )

        registerFluid(
            event,
            { stack: ItemStack ->
                val capacity: Int = HTItemHelper.processStorageCapacity(null, stack, 8000)
                HTComponentFluidTank.create(stack, capacity, filter = RagiumFluidContents.DEW_OF_THE_WARP::isOf)
            },
            TELEPORT_KEY,
        )

        // Energy
        registerEnergy(
            event,
            { stack: ItemStack ->
                HTComponentEnergyBattery.create(stack, HTItemHelper.processStorageCapacity(null, stack, 160000))
            },
            DRILL,
        )

        // Exp
        event.registerItem(
            HTExperienceCapabilities.item,
            { stack: ItemStack, _: Void? -> HTBottleExperienceHandler(stack) },
            Items.GLASS_BOTTLE,
            Items.EXPERIENCE_BOTTLE,
        )

        RagiumAPI.LOGGER.info("Registered item capabilities!")
    }

    @JvmStatic
    fun registerItem(event: RegisterCapabilitiesEvent, getter: (ItemStack) -> List<HTItemSlot>, vararg items: ItemLike) {
        event.registerItem(
            HTItemCapabilities.item,
            { stack: ItemStack, _: Void? -> HTComponentItemHandler(getter(stack)) },
            *items,
        )
    }

    @JvmStatic
    fun registerFluid(event: RegisterCapabilitiesEvent, getter: (ItemStack) -> HTFluidTank, vararg items: ItemLike) {
        event.registerItem(
            HTFluidCapabilities.item,
            { stack: ItemStack, _: Void? -> HTComponentFluidHandler(stack, getter(stack)) },
            *items,
        )
    }

    @JvmStatic
    fun registerEnergy(event: RegisterCapabilitiesEvent, getter: (ItemStack) -> HTEnergyBattery, vararg items: ItemLike) {
        event.registerItem(
            HTEnergyCapabilities.item,
            { stack: ItemStack, _: Void? -> HTComponentEnergyHandler(stack, getter(stack)) },
            *items,
        )
    }

    @JvmStatic
    fun registerExp(event: RegisterCapabilitiesEvent, getter: (ItemStack) -> HTExperienceTank, vararg items: ItemLike) {
        event.registerItem(
            HTExperienceCapabilities.item,
            { stack: ItemStack, _: Void? -> HTComponentExperienceHandler(stack, getter(stack)) },
            *items,
        )
    }

    @JvmStatic
    private fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        // Tools
        fun setEnch(item: ItemLike, ench: ResourceKey<Enchantment>, level: Int = 1) {
            event.modify(item) { builder: DataComponentPatch.Builder ->
                builder.set(RagiumDataComponents.INTRINSIC_ENCHANTMENT, HTIntrinsicEnchantment(ench, level))
            }
        }

        for (item: HTDeferredItem<*> in getToolMap(RagiumMaterialKeys.AZURE_STEEL).values) {
            setEnch(item, Enchantments.SILK_TOUCH)
        }

        setEnch(getTool(HTVanillaToolVariant.PICKAXE, RagiumMaterialKeys.DEEP_STEEL), Enchantments.FORTUNE, 5)
        setEnch(getTool(HTVanillaToolVariant.AXE, RagiumMaterialKeys.DEEP_STEEL), RagiumEnchantments.STRIKE)
        setEnch(getTool(HTVanillaToolVariant.SWORD, RagiumMaterialKeys.DEEP_STEEL), RagiumEnchantments.NOISE_CANCELING, 5)
        // Foods
        event.modify(FEVER_CHERRY) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.RARITY, Rarity.EPIC)
        }
        event.modify(AMBROSIA) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.RARITY, Rarity.EPIC)
        }
        // Other
        setEnch(ECHO_STAR, RagiumEnchantments.SONIC_PROTECTION)
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
        for (block: ItemLike in RagiumBlocks.MATERIALS.columnValues(RagiumMaterialKeys.IRIDESCENTIUM)) {
            event.modify(block, iridescent)
        }
        for (item: ItemLike in MATERIALS.columnValues(RagiumMaterialKeys.IRIDESCENTIUM)) {
            event.modify(item, iridescent)
        }

        event.modify(getIngot(RagiumMaterialKeys.CHOCOLATE)) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, RagiumFoods.CHOCOLATE)
        }
        event.modify(getIngot(RagiumMaterialKeys.MEAT)) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, Foods.BEEF)
        }
        event.modify(getIngot(RagiumMaterialKeys.COOKED_MEAT)) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, Foods.COOKED_BEEF)
        }

        RagiumAPI.LOGGER.info("Modified default item components!")
    }
}
