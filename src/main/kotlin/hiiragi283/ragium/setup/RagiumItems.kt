package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.item.component.HTItemSoundEvent
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.api.storage.capability.HTEnergyCapabilities
import hiiragi283.ragium.api.storage.capability.HTFluidCapabilities
import hiiragi283.ragium.api.storage.capability.HTItemCapabilities
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.text.HTTranslation
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.api.variant.HTToolVariant
import hiiragi283.ragium.common.inventory.container.HTPotionBundleContainerMenu
import hiiragi283.ragium.common.item.HTCatalystItem
import hiiragi283.ragium.common.item.HTDrumUpgradeItem
import hiiragi283.ragium.common.item.HTDrumWithMinecartItem
import hiiragi283.ragium.common.item.HTLootTicketItem
import hiiragi283.ragium.common.item.HTPotionDropItem
import hiiragi283.ragium.common.item.HTTierBasedItem
import hiiragi283.ragium.common.item.base.HTSmithingTemplateItem
import hiiragi283.ragium.common.item.food.HTAmbrosiaItem
import hiiragi283.ragium.common.item.food.HTIceCreamItem
import hiiragi283.ragium.common.item.food.HTPotionBundleItem
import hiiragi283.ragium.common.item.food.HTPotionSodaItem
import hiiragi283.ragium.common.item.tool.HTBlastChargeItem
import hiiragi283.ragium.common.item.tool.HTBottledBeeItem
import hiiragi283.ragium.common.item.tool.HTCaptureEggItem
import hiiragi283.ragium.common.item.tool.HTDestructionHammerItem
import hiiragi283.ragium.common.item.tool.HTDrillItem
import hiiragi283.ragium.common.item.tool.HTDynamicLanternItem
import hiiragi283.ragium.common.item.tool.HTMagnetItem
import hiiragi283.ragium.common.item.tool.HTTeleportKeyItem
import hiiragi283.ragium.common.item.tool.HTTraderCatalogItem
import hiiragi283.ragium.common.item.tool.HTUniversalBundleItem
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.storage.energy.HTComponentEnergyHandler
import hiiragi283.ragium.common.storage.energy.battery.HTComponentEnergyBattery
import hiiragi283.ragium.common.storage.fluid.HTComponentFluidHandler
import hiiragi283.ragium.common.storage.fluid.tank.HTComponentFluidTank
import hiiragi283.ragium.common.storage.item.HTComponentItemHandler
import hiiragi283.ragium.common.storage.item.slot.HTComponentItemSlot
import hiiragi283.ragium.common.text.HTSmithingTranslation
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import hiiragi283.ragium.common.tier.HTCircuitTier
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.common.util.HTItemHelper
import hiiragi283.ragium.common.variant.HTArmorVariant
import hiiragi283.ragium.common.variant.HTHammerToolVariant
import hiiragi283.ragium.common.variant.VanillaToolVariant
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
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
        REGISTER.addAlias("meat_ingot", "raw_meat_ingot")
        REGISTER.addAlias("wrench", "ragi_alloy_hammer")
        REGISTER.addAlias("advanced_circuit_board", "circuit_board")

        REGISTER.register(eventBus)

        eventBus.addListener(::registerItemCapabilities)
        eventBus.addListener(::modifyComponents)
    }

    //    Materials    //

    // Raginite
    @JvmField
    val RAGI_ALLOY_COMPOUND: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ragi_alloy_compound")

    @JvmField
    val RAGI_COKE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ragi_coke")

    // Wood
    @JvmField
    val COMPRESSED_SAWDUST: HTSimpleDeferredItem = REGISTER.registerSimpleItem("compressed_sawdust")

    @JvmField
    val RESIN: HTSimpleDeferredItem = REGISTER.registerSimpleItem("resin")

    // Diamond
    @JvmField
    val COAL_CHIP: HTSimpleDeferredItem = REGISTER.registerSimpleItem("coal_chip")

    @JvmField
    val COAL_CHUNK: HTSimpleDeferredItem = REGISTER.registerSimpleItem("coal_chunk")

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
    val ECHO_STAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("echo_star") { it.rarity(Rarity.UNCOMMON) }

    @JvmField
    val ELDER_HEART: HTSimpleDeferredItem = REGISTER.registerSimpleItem("elder_heart") { it.rarity(Rarity.UNCOMMON) }

    @JvmField
    val POTION_DROP: HTSimpleDeferredItem = REGISTER.registerItem("potion_drop", ::HTPotionDropItem)

    @JvmField
    val WITHER_DOLl: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wither_doll")

    @JvmStatic
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
            VanillaMaterialKeys.COAL,
            VanillaMaterialKeys.BLACKSTONE,
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
        register(CommonMaterialPrefixes.DUST, FoodMaterialKeys.RAW_MEAT, "minced_meat")
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
            RagiumMaterialKeys.NIGHT_METAL,
            RagiumMaterialKeys.IRIDESCENTIUM,
            // Foods
            FoodMaterialKeys.CHOCOLATE,
            FoodMaterialKeys.RAW_MEAT,
            FoodMaterialKeys.COOKED_MEAT,
        ).forEach { register(CommonMaterialPrefixes.INGOT, it, "${it.name}_ingot") }
        // Nuggets
        arrayOf(
            RagiumMaterialKeys.RAGI_ALLOY,
            RagiumMaterialKeys.ADVANCED_RAGI_ALLOY,
            RagiumMaterialKeys.AZURE_STEEL,
            RagiumMaterialKeys.DEEP_STEEL,
            RagiumMaterialKeys.NIGHT_METAL,
            RagiumMaterialKeys.IRIDESCENTIUM,
        ).forEach { register(CommonMaterialPrefixes.NUGGET, it, "${it.name}_nugget") }

        // Doughs
        register(CommonMaterialPrefixes.DOUGH, FoodMaterialKeys.WHEAT, "wheat_dough")
        // Flours
        register(CommonMaterialPrefixes.FLOUR, FoodMaterialKeys.WHEAT, "wheat_flour")
        // Fuels
        register(CommonMaterialPrefixes.FUEL, RagiumMaterialKeys.BAMBOO_CHARCOAL, "bamboo_charcoal")
        // Plates
        register(CommonMaterialPrefixes.PLATE, CommonMaterialKeys.PLASTIC, "plastic_plate")
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
    fun getScrap(material: HTMaterialLike): HTSimpleDeferredItem = getMaterial(CommonMaterialPrefixes.SCRAP, material)

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

    @JvmField
    val NIGHT_VISION_GOGGLES: HTDeferredItem<*> =
        HTArmorVariant.HELMET.registerItem(REGISTER, RagiumEquipmentMaterials.RAGI_CRYSTAL, "night_vision_goggles")

    @JvmStatic
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

    @JvmStatic
    fun getArmor(variant: HTArmorVariant, material: HTMaterialLike): HTDeferredItem<*> = ARMORS[variant, material.asMaterialKey()]
        ?: error("Unknown ${variant.variantName()} armor item for ${material.asMaterialName()}")

    @JvmStatic
    fun getArmorMap(material: HTMaterialLike): Map<HTArmorVariant, HTDeferredItem<*>> = ARMORS.column(material.asMaterialKey())

    //    Tools    //

    // Raginite
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

    @JvmField
    val DRILL: HTSimpleDeferredItem = REGISTER.registerItem("drill", ::HTDrillItem)

    // Azure
    @JvmField
    val BLUE_KNOWLEDGE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("blue_knowledge") { it.stacksTo(1) }

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

    @JvmStatic
    val TOOLS: ImmutableTable<HTToolVariant, HTMaterialKey, HTDeferredItem<*>> = buildTable {
        fun register(variant: HTToolVariant, material: HTEquipmentMaterial) {
            this[variant, material.asMaterialKey()] = variant.registerItem(REGISTER, material)
        }
        // Hammer
        register(HTHammerToolVariant, RagiumEquipmentMaterials.RAGI_ALLOY)
        this[HTHammerToolVariant, RagiumMaterialKeys.RAGI_CRYSTAL] = REGISTER.registerItemWith(
            "ragi_crystal_hammer",
            RagiumEquipmentMaterials.RAGI_CRYSTAL,
            HTDestructionHammerItem::create,
        )
        // Tools
        for (variant: VanillaToolVariant in VanillaToolVariant.entries) {
            // Azure Iron
            register(variant, RagiumEquipmentMaterials.AZURE_STEEL)
            // Deep Steel
            register(variant, RagiumEquipmentMaterials.DEEP_STEEL)
            // Night Metal
            register(variant, RagiumEquipmentMaterials.NIGHT_METAL)
        }
    }

    @JvmStatic
    fun getTool(variant: HTToolVariant, material: HTMaterialLike): HTDeferredItem<*> = TOOLS[variant, material.asMaterialKey()]
        ?: error("Unknown ${variant.variantName()} item for ${material.asMaterialName()}")

    @JvmStatic
    fun getHammer(material: HTMaterialLike): HTDeferredItem<*> = getTool(HTHammerToolVariant, material)

    @JvmStatic
    fun getToolMap(material: HTMaterialLike): Map<HTToolVariant, HTDeferredItem<*>> = TOOLS.column(material.asMaterialKey())

    @JvmField
    val SMITHING_TEMPLATES: Map<HTMaterialKey, HTSimpleDeferredItem> = listOf(
        RagiumMaterialKeys.AZURE_STEEL,
        RagiumMaterialKeys.DEEP_STEEL,
        RagiumMaterialKeys.NIGHT_METAL,
    ).associateWith { key: HTMaterialKey ->
        REGISTER.register("${key.name}_upgrade_smithing_template") { _ ->
            HTSmithingTemplateItem(HTSmithingTranslation(RagiumAPI.MOD_ID, key))
        }
    }

    @JvmStatic
    fun getSmithingTemplate(material: HTMaterialLike): HTSimpleDeferredItem =
        SMITHING_TEMPLATES[material.asMaterialKey()] ?: error("Unknown smithing template for ${material.asMaterialName()}")

    //    Foods    //

    @JvmStatic
    private fun registerFood(name: String, foodProperties: FoodProperties): HTSimpleDeferredItem =
        REGISTER.registerSimpleItem(name) { it.food(foodProperties) }

    // Ice Cream
    @JvmField
    val ICE_CREAM: HTSimpleDeferredItem = REGISTER.registerItem("ice_cream", ::HTIceCreamItem) { it.food(RagiumFoods.ICE_CREAM) }

    @JvmField
    val ICE_CREAM_SODA: HTSimpleDeferredItem = REGISTER.registerItem("ice_cream_soda", ::HTPotionSodaItem)

    // Meat
    @JvmField
    val CANNED_COOKED_MEAT: HTSimpleDeferredItem = registerFood("canned_cooked_meat", RagiumFoods.CANNED_COOKED_MEAT)

    // Sponge
    @JvmField
    val MELON_PIE: HTSimpleDeferredItem = registerFood("melon_pie", RagiumFoods.MELON_PIE)

    @JvmField
    val SWEET_BERRIES_CAKE_SLICE: HTSimpleDeferredItem = registerFood("sweet_berries_cake_slice", RagiumFoods.SWEET_BERRIES_CAKE)

    // Cherry
    @JvmField
    val RAGI_CHERRY: HTSimpleDeferredItem = registerFood("ragi_cherry", RagiumFoods.RAGI_CHERRY)

    @JvmField
    val RAGI_CHERRY_PULP: HTSimpleDeferredItem = registerFood("ragi_cherry_pulp", RagiumFoods.RAGI_CHERRY_PULP)

    @JvmField
    val RAGI_CHERRY_JAM: HTSimpleDeferredItem = registerFood("ragi_cherry_jam", RagiumFoods.RAGI_CHERRY_JAM)

    @JvmField
    val RAGI_CHERRY_TOAST: HTSimpleDeferredItem = registerFood("ragi_cherry_toast", RagiumFoods.RAGI_CHERRY_JAM)

    @JvmField
    val FEVER_CHERRY: HTSimpleDeferredItem = REGISTER.registerSimpleItem("fever_cherry") {
        it.food(RagiumFoods.FEVER_CHERRY).rarity(Rarity.EPIC)
    }

    // Other
    @JvmField
    val BOTTLED_BEE: HTSimpleDeferredItem = REGISTER.registerItem("bottled_bee", ::HTBottledBeeItem)

    @JvmField
    val AMBROSIA: HTSimpleDeferredItem = REGISTER.registerItem("ambrosia", ::HTAmbrosiaItem) { it.food(RagiumFoods.AMBROSIA) }

    //    Machine Parts    //

    @JvmField
    val GRAVITATIONAL_UNIT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("gravitational_unit")

    // Catalyst
    @JvmField
    val MOLDS: Map<HTMaterialPrefix, HTSimpleDeferredItem> = listOf(
        CommonMaterialPrefixes.STORAGE_BLOCK,
        CommonMaterialPrefixes.GEM,
        CommonMaterialPrefixes.INGOT,
    ).associate { prefixes: CommonMaterialPrefixes ->
        prefixes.asMaterialPrefix() to REGISTER.registerItem("${prefixes.asPrefixName()}_mold", ::HTCatalystItem)
    }

    @JvmStatic
    fun getMold(prefix: HTPrefixLike): HTSimpleDeferredItem =
        MOLDS[prefix.asMaterialPrefix()] ?: error("Unknown mold for ${prefix.asPrefixName()}")

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
    private fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        fun <T : Any> modify(item: ItemLike, type: DataComponentType<T>, value: T) {
            event.modify(item) { builder: DataComponentPatch.Builder -> builder.set(type, value) }
        }

        fun setDesc(item: ItemLike, translation: HTTranslation) {
            modify(item, RagiumDataComponents.DESCRIPTION, translation)
        }

        fun setEnch(item: ItemLike, ench: ResourceKey<Enchantment>, level: Int = 1) {
            modify(item, RagiumDataComponents.INTRINSIC_ENCHANTMENT, HTIntrinsicEnchantment(ench, level))
        }

        // Materials
        setDesc(ELDER_HEART, RagiumCommonTranslation.ELDER_HEART)

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

        modify(getIngot(FoodMaterialKeys.CHOCOLATE), DataComponents.FOOD, RagiumFoods.CHOCOLATE)
        modify(getIngot(FoodMaterialKeys.RAW_MEAT), DataComponents.FOOD, Foods.BEEF)
        modify(getIngot(FoodMaterialKeys.COOKED_MEAT), DataComponents.FOOD, Foods.COOKED_BEEF)
        // Tools
        for (item: HTDeferredItem<*> in getToolMap(RagiumMaterialKeys.AZURE_STEEL).values) {
            setEnch(item, Enchantments.SILK_TOUCH)
        }

        setEnch(getTool(VanillaToolVariant.PICKAXE, RagiumMaterialKeys.DEEP_STEEL), Enchantments.FORTUNE, 5)
        setEnch(getTool(VanillaToolVariant.AXE, RagiumMaterialKeys.DEEP_STEEL), RagiumEnchantments.STRIKE)
        setEnch(getTool(VanillaToolVariant.SWORD, RagiumMaterialKeys.DEEP_STEEL), RagiumEnchantments.NOISE_CANCELING, 5)

        setEnch(ECHO_STAR, RagiumEnchantments.SONIC_PROTECTION)
        modify(UNIVERSAL_BUNDLE, RagiumDataComponents.COLOR, DyeColor.WHITE)

        setDesc(BLAST_CHARGE, RagiumCommonTranslation.BLAST_CHARGE)
        setDesc(DYNAMIC_LANTERN, RagiumCommonTranslation.DYNAMIC_LANTERN)
        setDesc(ELDRITCH_EGG, RagiumCommonTranslation.ELDRITCH_EGG)
        setDesc(MAGNET, RagiumCommonTranslation.MAGNET)
        setDesc(SLOT_COVER, RagiumCommonTranslation.SLOT_COVER)
        setDesc(TRADER_CATALOG, RagiumCommonTranslation.TRADER_CATALOG)
        // Foods
        event.modify(ICE_CREAM_SODA) { builder: DataComponentPatch.Builder ->
            builder.set(RagiumDataComponents.DRINK_SOUND, HTItemSoundEvent.create(SoundEvents.GENERIC_DRINK))
            builder.set(RagiumDataComponents.EAT_SOUND, HTItemSoundEvent.create(SoundEvents.GENERIC_DRINK))
        }

        event.modify(RAGI_CHERRY_JAM) { builder: DataComponentPatch.Builder ->
            builder.set(RagiumDataComponents.DRINK_SOUND, HTItemSoundEvent.create(SoundEvents.HONEY_DRINK))
            builder.set(RagiumDataComponents.EAT_SOUND, HTItemSoundEvent.create(SoundEvents.HONEY_DRINK))
        }

        setDesc(AMBROSIA, RagiumCommonTranslation.AMBROSIA)
        setDesc(ICE_CREAM, RagiumCommonTranslation.ICE_CREAM)
        setDesc(RAGI_CHERRY, RagiumCommonTranslation.RAGI_CHERRY)

        RagiumAPI.LOGGER.info("Modified default item components!")
    }
}
