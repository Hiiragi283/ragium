package hiiragi283.ragium.setup

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.buildTable
import hiiragi283.ragium.api.extension.columnValues
import hiiragi283.ragium.api.extension.getEnchantmentLevel
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.item.component.HTPotionBundle
import hiiragi283.ragium.api.registry.HTDeferredItemRegister
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.material.HTItemMaterialVariant
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.api.util.material.HTVanillaMaterialType
import hiiragi283.ragium.api.util.tool.HTArmorVariant
import hiiragi283.ragium.api.util.tool.HTToolVariant
import hiiragi283.ragium.api.util.tool.HTVanillaToolVariant
import hiiragi283.ragium.common.item.HTAzureSteelTemplateItem
import hiiragi283.ragium.common.item.HTBlastChargeItem
import hiiragi283.ragium.common.item.HTCaptureEggItem
import hiiragi283.ragium.common.item.HTDeepSteelTemplateItem
import hiiragi283.ragium.common.item.HTDestructionHammerItem
import hiiragi283.ragium.common.item.HTDrillItem
import hiiragi283.ragium.common.item.HTDrumUpgradeItem
import hiiragi283.ragium.common.item.HTDynamicLanternItem
import hiiragi283.ragium.common.item.HTExpMagnetItem
import hiiragi283.ragium.common.item.HTLootTicketItem
import hiiragi283.ragium.common.item.HTPotionBundleItem
import hiiragi283.ragium.common.item.HTSimpleMagnetItem
import hiiragi283.ragium.common.item.HTTeleportKeyItem
import hiiragi283.ragium.common.storage.energy.HTComponentEnergyStorage
import hiiragi283.ragium.common.storage.fluid.HTComponentFluidHandler
import hiiragi283.ragium.util.material.RagiumMaterialType
import hiiragi283.ragium.util.material.RagiumTierType
import hiiragi283.ragium.util.variant.HTDeviceVariant
import hiiragi283.ragium.util.variant.HTDrumVariant
import hiiragi283.ragium.util.variant.HTHammerToolVariant
import hiiragi283.ragium.util.variant.RagiumMaterialVariants
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import net.neoforged.neoforge.registries.DeferredItem
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

    @JvmField
    val RAGI_COKE: DeferredItem<Item> = register("ragi_coke")

    @JvmField
    val COMPRESSED_SAWDUST: DeferredItem<Item> = register("compressed_sawdust")

    @JvmField
    val RESIN: DeferredItem<Item> = register("resin")

    @JvmField
    val SILICON: DeferredItem<Item> = register("silicon")

    @JvmField
    val TAR: DeferredItem<Item> = register("tar")

    @JvmField
    val DEEP_SCRAP: DeferredItem<Item> = register("deep_scrap")

    @JvmField
    val MATERIALS: HTTable<HTMaterialVariant, HTMaterialType, DeferredItem<*>> = buildTable {
        // Dusts
        listOf(
            RagiumMaterialType.RAGINITE,
            HTVanillaMaterialType.OBSIDIAN,
            RagiumMaterialType.CINNABAR,
            RagiumMaterialType.SALTPETER,
            RagiumMaterialType.SULFUR,
            RagiumMaterialType.ASH,
        ).forEach { put(HTItemMaterialVariant.DUST, it, register("${it.serializedName}_dust")) }
        put(HTItemMaterialVariant.DUST, HTVanillaMaterialType.WOOD, register("sawdust"))
        // Gems
        put(HTItemMaterialVariant.GEM, RagiumMaterialType.AZURE, register("azure_shard"))
        listOf(
            RagiumMaterialType.RAGI_CRYSTAL,
            RagiumMaterialType.CRIMSON_CRYSTAL,
            RagiumMaterialType.WARPED_CRYSTAL,
            RagiumMaterialType.ELDRITCH_PEARL,
        ).forEach { put(HTItemMaterialVariant.GEM, it, register(it.serializedName)) }
        // Ingots
        listOf(
            RagiumMaterialType.RAGI_ALLOY,
            RagiumMaterialType.ADVANCED_RAGI_ALLOY,
            RagiumMaterialType.AZURE_STEEL,
        ).forEach { put(RagiumMaterialVariants.COMPOUND, it, register("${it.serializedName}_compound")) }

        listOf(
            // Metals
            RagiumMaterialType.RAGI_ALLOY,
            RagiumMaterialType.ADVANCED_RAGI_ALLOY,
            RagiumMaterialType.AZURE_STEEL,
            RagiumMaterialType.DEEP_STEEL,
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
            RagiumMaterialType.IRIDESCENTIUM,
        ).forEach { put(HTItemMaterialVariant.NUGGET, it, register("${it.serializedName}_nugget")) }
        // Plates
        put(HTItemMaterialVariant.PLATE, RagiumMaterialType.PLASTIC, register("plastic_plate"))

        // Circuits, Components
        for (tier: RagiumTierType in RagiumTierType.entries) {
            put(HTItemMaterialVariant.CIRCUIT, tier, register("${tier.serializedName}_circuit"))
            put(RagiumMaterialVariants.COMPONENT, tier, register("${tier.serializedName}_component"))
        }

        // Coils
        fun addCoil(material: RagiumMaterialType) {
            put(RagiumMaterialVariants.COIL, material, register("${material.serializedName}_coil"))
        }
        addCoil(RagiumMaterialType.RAGI_ALLOY)
        addCoil(RagiumMaterialType.ADVANCED_RAGI_ALLOY)
    }

    @JvmStatic
    fun getMaterial(variant: HTMaterialVariant, material: HTMaterialType): DeferredItem<*> = MATERIALS.get(variant, material)
        ?: error("Unregistered ${variant.serializedName} item for ${material.serializedName}")

    @JvmStatic
    fun getDust(material: HTMaterialType): DeferredItem<*> = getMaterial(HTItemMaterialVariant.DUST, material)

    @JvmStatic
    fun getGem(material: HTMaterialType): DeferredItem<*> = getMaterial(HTItemMaterialVariant.GEM, material)

    @JvmStatic
    fun getIngot(material: HTMaterialType): DeferredItem<*> = getMaterial(HTItemMaterialVariant.INGOT, material)

    @JvmStatic
    fun getNugget(material: HTMaterialType): DeferredItem<*> = getMaterial(HTItemMaterialVariant.NUGGET, material)

    @JvmStatic
    fun getPlate(material: HTMaterialType): DeferredItem<*> = getMaterial(HTItemMaterialVariant.PLATE, material)

    //    Armors    //

    @JvmField
    val ARMORS: HTTable<HTArmorVariant, HTMaterialType, DeferredItem<*>> = buildTable {
        for (variant: HTArmorVariant in HTArmorVariant.entries) {
            // Azure
            put(
                variant,
                RagiumMaterialType.AZURE_STEEL,
                variant.registerItem(REGISTER, RagiumMaterialType.AZURE_STEEL, RagiumArmorMaterials.AZURE_STEEL, 20),
            )
            // Deep
            put(
                variant,
                RagiumMaterialType.DEEP_STEEL,
                variant.registerItem(REGISTER, RagiumMaterialType.DEEP_STEEL, RagiumArmorMaterials.DEEP_STEEL, 20),
            )
        }
    }

    @JvmStatic
    fun getAzureArmor(variant: HTArmorVariant): DeferredItem<*> = ARMORS.get(variant, RagiumMaterialType.AZURE_STEEL)
        ?: error("Unregistered azure steel ${variant.serializedName} item")

    @JvmStatic
    fun getDeepArmor(variant: HTArmorVariant): DeferredItem<*> = ARMORS.get(variant, RagiumMaterialType.DEEP_STEEL)
        ?: error("Unregistered deep steel ${variant.serializedName} item")

    //    Tools    //

    @JvmField
    val POTION_BUNDLE: DeferredItem<Item> = register("potion_bundle", ::HTPotionBundleItem, Item.Properties().stacksTo(1))

    @JvmField
    val SLOT_COVER: DeferredItem<Item> = register("slot_cover")

    @JvmField
    val TRADER_CATALOG: DeferredItem<Item> = register("trader_catalog", Item.Properties().stacksTo(1))

    @JvmField
    val RAGI_MAGNET: DeferredItem<Item> = register("ragi_magnet", ::HTSimpleMagnetItem)

    @JvmField
    val RAGI_LANTERN: DeferredItem<Item> = register("ragi_lantern", ::HTDynamicLanternItem)

    @JvmField
    val ADVANCED_RAGI_MAGNET: DeferredItem<Item> = register("advanced_ragi_magnet", ::HTExpMagnetItem)

    @JvmField
    val BLAST_CHARGE: DeferredItem<Item> = register("blast_charge", ::HTBlastChargeItem)

    @JvmField
    val ENDER_BUNDLE: DeferredItem<Item> = register("ender_bundle", Item.Properties().stacksTo(1))

    @JvmField
    val ELDRITCH_EGG: DeferredItem<Item> = register("eldritch_egg", ::HTCaptureEggItem)

    @JvmField
    val AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE: DeferredItem<Item> =
        REGISTER.register("${RagiumConst.AZURE_STEEL}_upgrade_smithing_template", ::HTAzureSteelTemplateItem)

    @JvmField
    val DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE: DeferredItem<Item> =
        REGISTER.register("${RagiumConst.DEEP_STEEL}_upgrade_smithing_template", ::HTDeepSteelTemplateItem)

    @JvmField
    val RAGI_TICKET: DeferredItem<HTLootTicketItem> = register("ragi_ticket", ::HTLootTicketItem)

    @JvmField
    val TELEPORT_KEY: DeferredItem<HTTeleportKeyItem> = register("teleport_key", ::HTTeleportKeyItem)

    @JvmField
    val ETERNAL_COMPONENT: DeferredItem<Item> = register("eternal_component", Item.Properties().rarity(Rarity.EPIC))

    @JvmField
    val MEDIUM_DRUM_UPGRADE: DeferredItem<HTDrumUpgradeItem> =
        register("medium_drum_upgrade", HTDrumUpgradeItem::Medium)

    @JvmField
    val LARGE_DRUM_UPGRADE: DeferredItem<HTDrumUpgradeItem> = register("large_drum_upgrade", HTDrumUpgradeItem::Large)

    @JvmField
    val HUGE_DRUM_UPGRADE: DeferredItem<HTDrumUpgradeItem> = register("huge_drum_upgrade", HTDrumUpgradeItem::Huge)

    @JvmField
    val DRILL: DeferredItem<Item> = register("drill", ::HTDrillItem)

    @JvmField
    val TOOLS: HTTable<HTToolVariant, HTMaterialType, DeferredItem<*>> = buildTable {
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
    fun getTool(variant: HTToolVariant, material: HTMaterialType): DeferredItem<*> = TOOLS.get(variant, material)
        ?: error("Unregistered ${variant.serializedName} item for ${material.serializedName}")

    @JvmStatic
    private fun getAzureTool(variant: HTVanillaToolVariant): DeferredItem<*> = getTool(variant, RagiumMaterialType.AZURE_STEEL)

    @JvmStatic
    private fun getDeepTool(variant: HTVanillaToolVariant): DeferredItem<*> = getTool(variant, RagiumMaterialType.DEEP_STEEL)

    //    Foods    //

    @JvmStatic
    private fun registerFood(
        name: String,
        foodProperties: FoodProperties,
        properties: Item.Properties = Item.Properties(),
    ): DeferredItem<Item> = register(name, properties.food(foodProperties))

    @JvmField
    val ICE_CREAM: DeferredItem<Item> = registerFood("ice_cream", RagiumFoods.ICE_CREAM)

    @JvmField
    val ICE_CREAM_SODA: DeferredItem<Item> = register("ice_cream_soda")

    // Meat
    @JvmField
    val MINCED_MEAT: DeferredItem<Item> = register("minced_meat")

    @JvmField
    val CANNED_COOKED_MEAT: DeferredItem<Item> = registerFood("canned_${RagiumConst.COOKED_MEAT}", RagiumFoods.CANNED_COOKED_MEAT)

    // Sponge
    @JvmField
    val MELON_PIE: DeferredItem<Item> = registerFood("melon_pie", RagiumFoods.MELON_PIE)

    @JvmField
    val SWEET_BERRIES_CAKE_SLICE: DeferredItem<Item> = registerFood("sweet_berries_cake_slice", RagiumFoods.SWEET_BERRIES_CAKE)

    // Cherry
    @JvmField
    val RAGI_CHERRY: DeferredItem<Item> = registerFood(RagiumConst.RAGI_CHERRY, RagiumFoods.RAGI_CHERRY)

    @JvmField
    val FEVER_CHERRY: DeferredItem<Item> =
        registerFood("fever_cherry", RagiumFoods.FEVER_CHERRY, properties = Item.Properties().rarity(Rarity.EPIC))

    // Other
    @JvmField
    val BOTTLED_BEE: DeferredItem<Item> = register("bottled_bee")

    @JvmField
    val AMBROSIA: DeferredItem<Item> =
        registerFood("ambrosia", RagiumFoods.AMBROSIA, properties = Item.Properties().rarity(Rarity.EPIC))

    //    Machine Parts    //

    @JvmField
    val BASALT_MESH: DeferredItem<Item> = register("basalt_mesh")

    @JvmField
    val ELDER_HEART: DeferredItem<Item> = register("elder_heart", Item.Properties().rarity(Rarity.UNCOMMON))

    @JvmField
    val ELDRITCH_GEAR: DeferredItem<Item> = register("eldritch_gear", Item.Properties().rarity(Rarity.RARE))

    // LED
    @JvmField
    val LUMINOUS_PASTE: DeferredItem<Item> = register("luminous_paste")

    @JvmField
    val LED: DeferredItem<Item> = register("led")

    @JvmField
    val SOLAR_PANEL: DeferredItem<Item> = register("solar_panel")

    // Redstone
    @JvmField
    val REDSTONE_BOARD: DeferredItem<Item> = register("redstone_board")

    // Plastics
    @JvmField
    val POLYMER_RESIN: DeferredItem<Item> = register("polymer_resin")

    @JvmField
    val SYNTHETIC_FIBER: DeferredItem<Item> = register("synthetic_fiber")

    @JvmField
    val SYNTHETIC_LEATHER: DeferredItem<Item> = register("synthetic_leather")

    @JvmField
    val CIRCUIT_BOARD: DeferredItem<Item> = register("circuit_board")

    @JvmField
    val ADVANCED_CIRCUIT_BOARD: DeferredItem<Item> = register("advanced_circuit_board")

    //    Extensions    //

    @JvmStatic
    private fun register(name: String, properties: Item.Properties = Item.Properties()): DeferredItem<Item> =
        REGISTER.registerSimpleItem(name, properties)

    @JvmStatic
    private fun <T : Item> register(
        name: String,
        factory: (Item.Properties) -> T,
        properties: Item.Properties = Item.Properties(),
    ): DeferredItem<T> = REGISTER.registerItem(name, factory, properties)

    //    Event    //

    @JvmStatic
    private fun registerItemCapabilities(event: RegisterCapabilitiesEvent) {
        // Fluid
        for (variant: HTDrumVariant in HTDrumVariant.entries) {
            registerFluid(event, variant, variant.capacity)
        }
        registerFluid(event, TELEPORT_KEY, 8000) { stack: ItemStack, capacity: Int ->
            object : HTComponentFluidHandler(stack, capacity) {
                override fun isFluidValid(tank: Int, stack: FluidStack): Boolean =
                    super.isFluidValid(tank, stack) && RagiumFluidContents.DEW_OF_THE_WARP.isOf(stack)
            }
        }

        // Energy
        registerEnergy(event, DRILL, 160000)

        LOGGER.info("Registered item capabilities!")
    }

    @JvmStatic
    fun registerFluid(
        event: RegisterCapabilitiesEvent,
        item: ItemLike,
        capacity: Int,
        factory: (ItemStack, Int) -> IFluidHandlerItem = ::HTComponentFluidHandler,
    ) {
        event.registerItem(
            Capabilities.FluidHandler.ITEM,
            { stack: ItemStack, _: Void? ->
                val modifier: Int = stack.getEnchantmentLevel(RagiumEnchantments.CAPACITY) + 1
                factory(stack, capacity * modifier)
            },
            item,
        )
    }

    @JvmStatic
    fun registerEnergy(event: RegisterCapabilitiesEvent, item: ItemLike, capacity: Int) {
        event.registerItem(
            Capabilities.EnergyStorage.ITEM,
            { stack: ItemStack, _: Void? ->
                val modifier: Int = stack.getEnchantmentLevel(RagiumEnchantments.CAPACITY) + 1
                HTComponentEnergyStorage(stack, capacity * modifier)
            },
            item,
        )
    }

    @JvmStatic
    private fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        // Tools
        fun setEnch(item: ItemLike, ench: ResourceKey<Enchantment>, level: Int = 1) {
            event.modify(item) { builder: DataComponentPatch.Builder ->
                builder.set(RagiumDataComponents.INTRINSIC_ENCHANTMENT.get(), HTIntrinsicEnchantment(ench, level))
            }
        }
        setEnch(getAzureTool(HTVanillaToolVariant.AXE), Enchantments.SILK_TOUCH)
        setEnch(getAzureTool(HTVanillaToolVariant.HOE), Enchantments.SILK_TOUCH)
        setEnch(getAzureTool(HTVanillaToolVariant.PICKAXE), Enchantments.SILK_TOUCH)
        setEnch(getAzureTool(HTVanillaToolVariant.SHOVEL), Enchantments.SILK_TOUCH)

        setEnch(getDeepTool(HTVanillaToolVariant.PICKAXE), Enchantments.FORTUNE, 5)
        setEnch(getDeepTool(HTVanillaToolVariant.SWORD), RagiumEnchantments.NOISE_CANCELING, 5)
        setEnch(getDeepArmor(HTArmorVariant.CHESTPLATE), RagiumEnchantments.SONIC_PROTECTION)
        // Other
        event.modify(POTION_BUNDLE) { builder: DataComponentPatch.Builder ->
            builder.set(RagiumDataComponents.POTION_BUNDLE.get(), HTPotionBundle.EMPTY)
        }

        event.modify(ICE_CREAM_SODA) { builder: DataComponentPatch.Builder ->
            builder.set(RagiumDataComponents.DRINK_SOUND.get(), SoundEvents.GENERIC_DRINK)
            builder.set(RagiumDataComponents.EAT_SOUND.get(), SoundEvents.GENERIC_DRINK)
        }

        for (block: ItemLike in RagiumBlocks.MATERIALS.columnValues(RagiumMaterialType.IRIDESCENTIUM)) {
            event.modify(block) { builder: DataComponentPatch.Builder ->
                builder.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                builder.set(DataComponents.RARITY, Rarity.RARE)
            }
        }
        for (item: ItemLike in MATERIALS.columnValues(RagiumMaterialType.IRIDESCENTIUM)) {
            event.modify(item) { builder: DataComponentPatch.Builder ->
                builder.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                builder.set(DataComponents.RARITY, Rarity.RARE)
            }
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
