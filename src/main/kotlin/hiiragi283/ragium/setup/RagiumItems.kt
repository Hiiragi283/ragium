package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.item.component.HTItemSoundEvent
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.experience.IExperienceStorageItem
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.variant.HTMaterialVariant
import hiiragi283.ragium.api.variant.HTToolVariant
import hiiragi283.ragium.common.entity.vehicle.HTDrumMinecart
import hiiragi283.ragium.common.entity.vehicle.HTMinecart
import hiiragi283.ragium.common.inventory.container.HTPotionBundleContainerMenu
import hiiragi283.ragium.common.item.HTBlastChargeItem
import hiiragi283.ragium.common.item.HTCaptureEggItem
import hiiragi283.ragium.common.item.HTCatalystItem
import hiiragi283.ragium.common.item.HTDrumUpgradeItem
import hiiragi283.ragium.common.item.HTLootTicketItem
import hiiragi283.ragium.common.item.HTMinecartItem
import hiiragi283.ragium.common.item.HTPotionBundleItem
import hiiragi283.ragium.common.item.HTPotionSodaItem
import hiiragi283.ragium.common.item.HTSmithingTemplateItem
import hiiragi283.ragium.common.item.HTTeleportKeyItem
import hiiragi283.ragium.common.item.HTTierBasedItem
import hiiragi283.ragium.common.item.HTTraderCatalogItem
import hiiragi283.ragium.common.item.HTUniversalBundleItem
import hiiragi283.ragium.common.item.tool.HTDestructionHammerItem
import hiiragi283.ragium.common.item.tool.HTDrillItem
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.storage.energy.HTComponentEnergyStorage
import hiiragi283.ragium.common.storage.experience.HTBottleExperienceStorage
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
import hiiragi283.ragium.common.variant.HTItemMaterialVariant
import hiiragi283.ragium.common.variant.HTVanillaToolVariant
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import net.neoforged.neoforge.items.IItemHandler
import java.util.function.UnaryOperator

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
    val RAGI_ALLOY_COMPOUND: HTSimpleDeferredItem = register("${RagiumConst.RAGI_ALLOY}_compound")

    @JvmField
    val RAGI_COKE: HTSimpleDeferredItem = register("ragi_coke")

    // Wood
    @JvmField
    val COMPRESSED_SAWDUST: HTSimpleDeferredItem = register("compressed_sawdust")

    @JvmField
    val RESIN: HTSimpleDeferredItem = register("resin")

    // Oil
    @JvmField
    val TAR: HTSimpleDeferredItem = register("tar")

    // Nuclear Fuel
    @JvmField
    val POTATO_SPROUTS: HTSimpleDeferredItem = register("potato_sprouts")

    @JvmField
    val GREEN_CAKE: HTSimpleDeferredItem = register("green_cake")

    @JvmField
    val GREEN_CAKE_DUST: HTSimpleDeferredItem = register("green_cake_dust")

    @JvmField
    val GREEN_PELLET: HTSimpleDeferredItem = register("green_pellet")

    // Misc
    @JvmField
    val BASALT_MESH: HTSimpleDeferredItem = register("basalt_mesh")

    @JvmField
    val ECHO_STAR: HTSimpleDeferredItem = register("echo_star") { it.rarity(Rarity.UNCOMMON) }

    @JvmField
    val ELDER_HEART: HTSimpleDeferredItem = register("elder_heart") { it.rarity(Rarity.UNCOMMON) }

    @JvmField
    val WITHER_DOLl: HTSimpleDeferredItem = register("wither_doll")

    val MATERIALS: ImmutableTable<HTMaterialVariant.ItemTag, HTMaterialType, HTDeferredItem<*>> = buildTable {
        // Dusts
        arrayOf<HTMaterialType>(
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
        ).forEach { this[HTItemMaterialVariant.DUST, it] = register("${it.materialName()}_dust") }
        this[HTItemMaterialVariant.DUST, HTVanillaMaterialType.WOOD] = register("sawdust")
        this[HTItemMaterialVariant.DUST, RagiumMaterialType.MEAT] = register("minced_meat")
        // Gems
        this[HTItemMaterialVariant.GEM, RagiumMaterialType.AZURE] = register("azure_shard")
        arrayOf(
            RagiumMaterialType.RAGI_CRYSTAL,
            RagiumMaterialType.CRIMSON_CRYSTAL,
            RagiumMaterialType.WARPED_CRYSTAL,
            RagiumMaterialType.ELDRITCH_PEARL,
        ).forEach { this[HTItemMaterialVariant.GEM, it] = register(it.materialName()) }
        // Chips
        mapOf(
            RagiumMaterialType.RAGI_CRYSTAL to RagiumConst.RAGI_CRYSTAL + "_chip",
            RagiumMaterialType.AZURE to "azure_shard_chip",
            RagiumMaterialType.ELDRITCH_PEARL to RagiumConst.ELDRITCH_PEARL + "_chip",
            HTVanillaMaterialType.ECHO to "echo_shard_chip",
        ).forEach { this[HTItemMaterialVariant.CHIP, it.key] = register(it.value) }
        // Ingots
        arrayOf(
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
        ).forEach { this[HTItemMaterialVariant.INGOT, it] = register("${it.materialName()}_ingot") }
        // Nuggets
        arrayOf(
            RagiumMaterialType.RAGI_ALLOY,
            RagiumMaterialType.ADVANCED_RAGI_ALLOY,
            RagiumMaterialType.AZURE_STEEL,
            RagiumMaterialType.DEEP_STEEL,
            RagiumMaterialType.GILDIUM,
            RagiumMaterialType.IRIDESCENTIUM,
        ).forEach { this[HTItemMaterialVariant.NUGGET, it] = register("${it.materialName()}_nugget") }
        // Plates
        this[HTItemMaterialVariant.PLATE, RagiumMaterialType.PLASTIC] = register("plastic_plate")

        // Fuels
        this[HTItemMaterialVariant.FUEL, RagiumMaterialType.BAMBOO_CHARCOAL] = register("bamboo_charcoal")
        // Scraps
        this[HTItemMaterialVariant.SCRAP, RagiumMaterialType.DEEP_STEEL] = register("deep_scrap")
    }

    @JvmStatic
    fun getMaterial(variant: HTMaterialVariant.ItemTag, material: HTMaterialType): HTDeferredItem<*> = MATERIALS[variant, material]
        ?: error("Unknown ${variant.variantName()} item for ${material.materialName()}")

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

    @JvmStatic
    fun getScrap(material: HTMaterialType): HTItemHolderLike = when (material) {
        HTVanillaMaterialType.NETHERITE -> Items.NETHERITE_SCRAP.toHolderLike()
        else -> getMaterial(HTItemMaterialVariant.SCRAP, material)
    }

    @JvmField
    val COILS: Map<HTMaterialType, HTDeferredItem<*>> = arrayOf(RagiumMaterialType.RAGI_ALLOY, RagiumMaterialType.ADVANCED_RAGI_ALLOY)
        .associateWith { material: HTMaterialType -> register("${material.materialName()}_coil") }

    @JvmField
    val CIRCUITS: Map<HTCircuitTier, HTDeferredItem<*>> = HTCircuitTier.entries.associateWith { tier: HTCircuitTier ->
        REGISTER.registerItemWith("${tier.materialName()}_circuit", tier, ::HTTierBasedItem)
    }

    @JvmField
    val COMPONENTS: Map<HTComponentTier, HTDeferredItem<*>> = HTComponentTier.entries.associateWith { tier: HTComponentTier ->
        REGISTER.registerItemWith("${tier.materialName()}_component", tier, ::HTTierBasedItem)
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
    val WRENCH: HTSimpleDeferredItem = register("wrench") { it.stacksTo(1) }

    @JvmField
    val MAGNET: HTSimpleDeferredItem = register("ragi_magnet")

    @JvmField
    val ADVANCED_MAGNET: HTSimpleDeferredItem = register("advanced_ragi_magnet")

    @JvmField
    val DYNAMIC_LANTERN: HTSimpleDeferredItem = register("ragi_lantern")

    @JvmField
    val LOOT_TICKET: HTSimpleDeferredItem = REGISTER.registerItem("ragi_ticket", ::HTLootTicketItem)

    @JvmField
    val NIGHT_VISION_GOGGLES: HTSimpleDeferredItem = register("night_vision_goggles") { it.stacksTo(1) }

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
    val SLOT_COVER: HTSimpleDeferredItem = register("slot_cover")

    @JvmField
    val TRADER_CATALOG: HTSimpleDeferredItem = REGISTER.registerItem("trader_catalog", ::HTTraderCatalogItem)

    @JvmField
    val MEDIUM_DRUM_UPGRADE: HTSimpleDeferredItem =
        REGISTER.registerItem("medium_drum_upgrade", HTDrumUpgradeItem::Medium)

    @JvmField
    val LARGE_DRUM_UPGRADE: HTSimpleDeferredItem = REGISTER.registerItem("large_drum_upgrade", HTDrumUpgradeItem::Large)

    @JvmField
    val HUGE_DRUM_UPGRADE: HTSimpleDeferredItem = REGISTER.registerItem("huge_drum_upgrade", HTDrumUpgradeItem::Huge)

    val TOOLS: ImmutableTable<HTToolVariant, HTMaterialType, HTDeferredItem<*>> = buildTable {
        val consumer: (HTToolVariant, HTMaterialType, Tier) -> Unit = { variant: HTToolVariant, material: HTMaterialType, tier: Tier ->
            this[variant, material] = variant.registerItem(REGISTER, material, tier)
        }

        // Hammer
        consumer(HTHammerToolVariant, HTVanillaMaterialType.IRON, Tiers.IRON)
        consumer(HTHammerToolVariant, HTVanillaMaterialType.DIAMOND, Tiers.DIAMOND)
        consumer(HTHammerToolVariant, HTVanillaMaterialType.NETHERITE, Tiers.NETHERITE)

        consumer(HTHammerToolVariant, RagiumMaterialType.RAGI_ALLOY, RagiumToolTiers.RAGI_ALLOY)
        consumer(HTHammerToolVariant, RagiumMaterialType.AZURE_STEEL, RagiumToolTiers.AZURE_STEEL)
        consumer(HTHammerToolVariant, RagiumMaterialType.DEEP_STEEL, RagiumToolTiers.DEEP_STEEL)

        this[HTHammerToolVariant, RagiumMaterialType.RAGI_CRYSTAL] =
            REGISTER.registerItem("ragi_crystal_hammer", ::HTDestructionHammerItem)
        // Tools
        for (variant: HTToolVariant in HTVanillaToolVariant.entries) {
            // Azure
            consumer(variant, RagiumMaterialType.AZURE_STEEL, RagiumToolTiers.AZURE_STEEL)
            // Deep
            consumer(variant, RagiumMaterialType.DEEP_STEEL, RagiumToolTiers.DEEP_STEEL)
        }
    }

    @JvmStatic
    fun getTool(variant: HTToolVariant, material: HTMaterialType): HTDeferredItem<*> = TOOLS[variant, material]
        ?: error("Unknown ${variant.variantName()} item for ${material.materialName()}")

    @JvmStatic
    private fun getAzureTool(variant: HTVanillaToolVariant): HTDeferredItem<*> = getTool(variant, RagiumMaterialType.AZURE_STEEL)

    @JvmStatic
    private fun getDeepTool(variant: HTVanillaToolVariant): HTDeferredItem<*> = getTool(variant, RagiumMaterialType.DEEP_STEEL)

    //    Foods    //

    @JvmStatic
    private fun registerFood(name: String, foodProperties: FoodProperties): HTSimpleDeferredItem =
        register(name) { it.food(foodProperties) }

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
    val BOTTLED_BEE: HTSimpleDeferredItem = register("bottled_bee")

    @JvmField
    val AMBROSIA: HTSimpleDeferredItem = registerFood("ambrosia", RagiumFoods.AMBROSIA)

    //    Machine Parts    //

    @JvmField
    val GRAVITATIONAL_UNIT: HTSimpleDeferredItem = register("gravitational_unit")

    // Catalyst
    @JvmField
    val PLATING_CATALYST: HTSimpleDeferredItem = REGISTER.registerItem("plating_catalyst", ::HTCatalystItem)

    @JvmField
    val POLYMER_CATALYST: HTSimpleDeferredItem = REGISTER.registerItem("polymer_catalyst", ::HTCatalystItem)

    // LED
    @JvmField
    val LUMINOUS_PASTE: HTSimpleDeferredItem = register("luminous_paste")

    @JvmField
    val LED: HTSimpleDeferredItem = register("led")

    @JvmField
    val SOLAR_PANEL: HTSimpleDeferredItem = register("solar_panel")

    // Redstone
    @JvmField
    val REDSTONE_BOARD: HTSimpleDeferredItem = register("redstone_board")

    // Plastics
    @JvmField
    val POLYMER_RESIN: HTSimpleDeferredItem = register("polymer_resin")

    @JvmField
    val SYNTHETIC_FIBER: HTSimpleDeferredItem = register("synthetic_fiber")

    @JvmField
    val SYNTHETIC_LEATHER: HTSimpleDeferredItem = register("synthetic_leather")

    @JvmField
    val CIRCUIT_BOARD: HTSimpleDeferredItem = register("circuit_board")

    @JvmField
    val ADVANCED_CIRCUIT_BOARD: HTSimpleDeferredItem = register("advanced_circuit_board")

    //    Vehicles    //

    @JvmField
    val DRUM_MINECARTS: Map<HTDrumTier, HTSimpleDeferredItem> = HTDrumTier.entries.associateWith { tier: HTDrumTier ->
        val factory: HTMinecart.Factory = when (tier) {
            HTDrumTier.SMALL -> HTMinecart.Factory(HTDrumMinecart::Small)
            HTDrumTier.MEDIUM -> HTMinecart.Factory(HTDrumMinecart::Medium)
            HTDrumTier.LARGE -> HTMinecart.Factory(HTDrumMinecart::Large)
            HTDrumTier.HUGE -> HTMinecart.Factory(HTDrumMinecart::Huge)
            HTDrumTier.CREATIVE -> HTMinecart.Factory(HTDrumMinecart::Creative)
        }
        REGISTER.registerItemWith(tier.entityPath, factory, ::HTMinecartItem)
    }

    //    Extensions    //

    @JvmStatic
    private fun register(name: String, operator: UnaryOperator<Item.Properties> = UnaryOperator.identity()): HTSimpleDeferredItem =
        REGISTER.registerSimpleItem(name, operator)

    //    Event    //

    @JvmStatic
    private fun registerItemCapabilities(event: RegisterCapabilitiesEvent) {
        // Item
        for ((_, block: ItemLike) in RagiumBlocks.CRATES) {
            registerItem(
                event,
                { stack: ItemStack -> HTComponentItemHandler(HTComponentItemSlot.create(stack, 1)) },
                block,
            )
        }
        registerItem(
            event,
            { stack: ItemStack ->
                HTComponentItemHandler(9) { slot: Int ->
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
                    HTComponentFluidHandler(stack, HTComponentFluidTank.create(stack, capacity))
                },
                block,
            )
        }
        registerFluid(
            event,
            { stack: ItemStack ->
                val capacity: Int = HTItemHelper.processStorageCapacity(null, stack, 8000)
                HTComponentFluidHandler(
                    stack,
                    HTComponentFluidTank.create(stack, capacity, filter = RagiumFluidContents.DEW_OF_THE_WARP::isOf),
                )
            },
            TELEPORT_KEY,
        )

        // Energy
        registerEnergy(
            event,
            { stack: ItemStack ->
                HTComponentEnergyStorage(stack, HTItemHelper.processStorageCapacity(null, stack, 160000))
            },
            DRILL,
        )

        // Exp
        registerExp(event, ::HTBottleExperienceStorage, Items.GLASS_BOTTLE, Items.EXPERIENCE_BOTTLE)

        RagiumAPI.LOGGER.info("Registered item capabilities!")
    }

    @JvmStatic
    fun registerItem(event: RegisterCapabilitiesEvent, getter: (ItemStack) -> IItemHandler?, vararg items: ItemLike) {
        event.registerItem(
            RagiumCapabilities.ITEM.itemCapability(),
            { stack: ItemStack, _: Void? -> getter(stack) },
            *items,
        )
    }

    @JvmStatic
    fun registerFluid(event: RegisterCapabilitiesEvent, getter: (ItemStack) -> IFluidHandlerItem?, vararg items: ItemLike) {
        event.registerItem(
            RagiumCapabilities.FLUID.itemCapability(),
            { stack: ItemStack, _: Void? -> getter(stack) },
            *items,
        )
    }

    @JvmStatic
    fun registerEnergy(event: RegisterCapabilitiesEvent, getter: (ItemStack) -> IEnergyStorage?, vararg items: ItemLike) {
        event.registerItem(
            RagiumCapabilities.ENERGY.itemCapability(),
            { stack: ItemStack, _: Void? -> getter(stack) },
            *items,
        )
    }

    @JvmStatic
    fun registerExp(event: RegisterCapabilitiesEvent, getter: (ItemStack) -> IExperienceStorageItem?, vararg items: ItemLike) {
        event.registerItem(
            RagiumCapabilities.EXPERIENCE.itemCapability(),
            { stack: ItemStack, _: Void? -> getter(stack) },
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
        setEnch(getAzureTool(HTVanillaToolVariant.AXE), Enchantments.SILK_TOUCH)
        setEnch(getAzureTool(HTVanillaToolVariant.HOE), Enchantments.SILK_TOUCH)
        setEnch(getAzureTool(HTVanillaToolVariant.PICKAXE), Enchantments.SILK_TOUCH)
        setEnch(getAzureTool(HTVanillaToolVariant.SHOVEL), Enchantments.SILK_TOUCH)

        setEnch(getDeepTool(HTVanillaToolVariant.PICKAXE), Enchantments.FORTUNE, 5)
        setEnch(getDeepTool(HTVanillaToolVariant.AXE), RagiumEnchantments.STRIKE)
        setEnch(getDeepTool(HTVanillaToolVariant.SWORD), RagiumEnchantments.NOISE_CANCELING, 5)
        // Foods
        event.modify(FEVER_CHERRY) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.RARITY, Rarity.EPIC)
        }
        event.modify(AMBROSIA) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.RARITY, Rarity.EPIC)
        }
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

        RagiumAPI.LOGGER.info("Modified default item components!")
    }
}
