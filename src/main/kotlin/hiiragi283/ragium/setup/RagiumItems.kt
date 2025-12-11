package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.capability.HTEnergyCapabilities
import hiiragi283.ragium.api.capability.HTFluidCapabilities
import hiiragi283.ragium.api.capability.HTItemCapabilities
import hiiragi283.ragium.api.capability.RagiumCapabilities
import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.item.component.HTComponentUpgrade
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.item.component.HTItemSoundEvent
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.math.fraction
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.text.HTTranslation
import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.api.upgrade.HTUpgradeKey
import hiiragi283.ragium.api.upgrade.HTUpgradeProvider
import hiiragi283.ragium.api.upgrade.RagiumUpgradeKeys
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.api.variant.HTToolVariant
import hiiragi283.ragium.common.HTChargeType
import hiiragi283.ragium.common.HTMoldType
import hiiragi283.ragium.common.inventory.container.HTPotionBundleContainerMenu
import hiiragi283.ragium.common.item.HTIridescentPowderItem
import hiiragi283.ragium.common.item.HTLootTicketItem
import hiiragi283.ragium.common.item.HTPotionDropItem
import hiiragi283.ragium.common.item.HTTankMinecartItem
import hiiragi283.ragium.common.item.HTTierBasedItem
import hiiragi283.ragium.common.item.base.HTSmithingTemplateItem
import hiiragi283.ragium.common.item.food.HTAmbrosiaItem
import hiiragi283.ragium.common.item.food.HTIceCreamItem
import hiiragi283.ragium.common.item.food.HTPotionBundleItem
import hiiragi283.ragium.common.item.food.HTPotionSodaItem
import hiiragi283.ragium.common.item.tool.HTBottledBeeItem
import hiiragi283.ragium.common.item.tool.HTCaptureEggItem
import hiiragi283.ragium.common.item.tool.HTChargeItem
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
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.upgrade.HTComponentUpgradeHandler
import hiiragi283.ragium.common.variant.HTArmorVariant
import hiiragi283.ragium.common.variant.HTHammerToolVariant
import hiiragi283.ragium.common.variant.HTUpgradeVariant
import hiiragi283.ragium.common.variant.VanillaToolVariant
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.TagKey
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import org.apache.commons.lang3.math.Fraction
import java.util.function.UnaryOperator

object RagiumItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        REGISTER.addAlias("meat_ingot", "raw_meat_ingot")

        REGISTER.addAlias("wrench", "ragi_alloy_hammer")

        REGISTER.addAlias("advanced_circuit_board", "circuit_board")

        REGISTER.addAlias(RagiumAPI.id("blackstone_dust"), vanillaId("blackstone"))
        REGISTER.addAlias(RagiumAPI.id("obsidian_dust"), vanillaId("obsidian"))

        REGISTER.addAlias("elite_circuit", "ragi_crystal")
        REGISTER.addAlias("ultimate_circuit", "eldritch_pearl")

        arrayOf(
            // Gems
            RagiumMaterialKeys.RAGI_CRYSTAL,
            RagiumMaterialKeys.CRIMSON_CRYSTAL,
            RagiumMaterialKeys.WARPED_CRYSTAL,
            RagiumMaterialKeys.ELDRITCH_PEARL,
            // Metals
            RagiumMaterialKeys.RAGI_ALLOY,
            RagiumMaterialKeys.ADVANCED_RAGI_ALLOY,
            RagiumMaterialKeys.AZURE_STEEL,
            RagiumMaterialKeys.DEEP_STEEL,
            RagiumMaterialKeys.NIGHT_METAL,
        ).forEach { key: HTMaterialKey ->
            REGISTER.addAlias("${key.name}_dust", "${key.name}_ingot")
        }

        REGISTER.addAlias("iridescentium_dust", "iridescent_powder")
        REGISTER.addAlias("iridescentium_ingot", "iridescent_powder")

        REGISTER.addAlias("cinnabar_dust", "magma_shard")
        REGISTER.addAlias(RagiumAPI.id("saltpeter_dust"), vanillaId("bone_meal"))

        REGISTER.addAlias("resin", "rosin")

        REGISTER.addAlias("confusing_charge", "confusion_charge")
        REGISTER.addAlias("ragi_coke", "coal_coke")

        listOf(
            "small",
            "medium",
            "large",
            "huge",
        ).forEach { REGISTER.addAlias("${it}_drum_minecart", "tank_minecart") }

        REGISTER.register(eventBus)

        eventBus.addListener(::registerItemCapabilities)
        eventBus.addListener(::modifyComponents)
    }

    //    Materials    //

    // Raginite
    @JvmField
    val RAGI_ALLOY_COMPOUND: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ragi_alloy_compound")

    @JvmField
    val RAGIUM_POWDER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ragium_powder")

    // Wood
    @JvmField
    val COMPRESSED_SAWDUST: HTSimpleDeferredItem = REGISTER.registerSimpleItem("compressed_sawdust")

    @JvmField
    val ROSIN: HTSimpleDeferredItem = REGISTER.registerSimpleItem("rosin")

    // Diamond
    @JvmField
    val COAL_CHIP: HTSimpleDeferredItem = REGISTER.registerSimpleItem("coal_chip")

    @JvmField
    val COAL_CHUNK: HTSimpleDeferredItem = REGISTER.registerSimpleItem("coal_chunk")

    // Oil
    @JvmField
    val TAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("tar")

    @JvmField
    val MAGMA_SHARD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("magma_shard")

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
    val ECHO_STAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("echo_star") {
        it.rarity(Rarity.UNCOMMON).enchantment(RagiumEnchantments.SONIC_PROTECTION)
    }

    @JvmField
    val ELDER_HEART: HTSimpleDeferredItem = REGISTER.registerSimpleItem("elder_heart") {
        it.rarity(Rarity.UNCOMMON).description(RagiumCommonTranslation.ELDER_HEART)
    }

    @JvmField
    val WITHER_DOLl: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wither_doll")

    @JvmField
    val WITHER_STAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wither_star")

    @JvmField
    val IRIDESCENT_POWDER: HTSimpleDeferredItem = REGISTER.registerItem("iridescent_powder", ::HTIridescentPowderItem) {
        it.description(RagiumCommonTranslation.IRIDESCENT_POWDER)
    }

    @JvmStatic
    val MATERIALS: ImmutableTable<HTMaterialPrefix, HTMaterialKey, HTSimpleDeferredItem> = buildTable {
        fun register(
            prefix: HTPrefixLike,
            material: HTMaterialLike,
            name: String,
            operator: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
        ) {
            this[prefix.asMaterialPrefix(), material.asMaterialKey()] = REGISTER.registerSimpleItem(name, operator)
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
            // Common
            CommonMaterialKeys.Gems.SALT,
            CommonMaterialKeys.Gems.SULFUR,
            // Ragium - Gem
            RagiumMaterialKeys.RAGINITE,
            RagiumMaterialKeys.AZURE,
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
            RagiumMaterialKeys.RAGI_ALLOY,
            RagiumMaterialKeys.ADVANCED_RAGI_ALLOY,
            RagiumMaterialKeys.AZURE_STEEL,
            RagiumMaterialKeys.DEEP_STEEL,
            RagiumMaterialKeys.NIGHT_METAL,
        ).forEach { register(CommonMaterialPrefixes.INGOT, it, "${it.asMaterialName()}_ingot") }

        // Foods
        fun food(key: HTMaterialKey) {
            register(CommonMaterialPrefixes.FOOD, key, "${key.name}_ingot")
        }

        fun food(key: HTMaterialKey, food: FoodProperties) {
            register(CommonMaterialPrefixes.FOOD, key, "${key.name}_ingot") { it.food(food) }
        }

        food(FoodMaterialKeys.BUTTER)
        food(FoodMaterialKeys.CHOCOLATE, RagiumFoods.CHOCOLATE)
        food(FoodMaterialKeys.RAW_MEAT, Foods.BEEF)
        food(FoodMaterialKeys.COOKED_MEAT, Foods.COOKED_BEEF)
        // Nuggets
        arrayOf(
            RagiumMaterialKeys.RAGI_ALLOY,
            RagiumMaterialKeys.ADVANCED_RAGI_ALLOY,
            RagiumMaterialKeys.AZURE_STEEL,
            RagiumMaterialKeys.DEEP_STEEL,
            RagiumMaterialKeys.NIGHT_METAL,
        ).forEach { register(CommonMaterialPrefixes.NUGGET, it, "${it.name}_nugget") }
        // Gears
        arrayOf(
            RagiumMaterialKeys.AZURE_STEEL,
            RagiumMaterialKeys.DEEP_STEEL,
            RagiumMaterialKeys.NIGHT_METAL,
        ).forEach { register(CommonMaterialPrefixes.GEAR, it, "${it.name}_gear") }

        // Doughs
        register(CommonMaterialPrefixes.DOUGH, FoodMaterialKeys.WHEAT, "wheat_dough")
        // Flours
        register(CommonMaterialPrefixes.FLOUR, FoodMaterialKeys.WHEAT, "wheat_flour")
        // Fuels
        register(CommonMaterialPrefixes.FUEL, RagiumMaterialKeys.BAMBOO_CHARCOAL, "bamboo_charcoal")
        register(CommonMaterialPrefixes.FUEL, CommonMaterialKeys.COAL_COKE, "coal_coke")
        // Plates
        register(CommonMaterialPrefixes.PLATE, VanillaMaterialKeys.WOOD, "wood_plate")
        register(CommonMaterialPrefixes.PLATE, CommonMaterialKeys.PLASTIC, "plastic_plate")
        register(CommonMaterialPrefixes.PLATE, CommonMaterialKeys.RAW_RUBBER, "raw_rubber_sheet")
        register(CommonMaterialPrefixes.PLATE, CommonMaterialKeys.RUBBER, "rubber_sheet")
        // Scraps
        register(CommonMaterialPrefixes.SCRAP, RagiumMaterialKeys.DEEP_STEEL, "deep_scrap")
    }

    @JvmStatic
    fun getMaterial(prefix: HTPrefixLike, material: HTMaterialLike): HTSimpleDeferredItem =
        MATERIALS[prefix.asMaterialPrefix(), material.asMaterialKey()]
            ?: error("Unknown ${prefix.asPrefixName()} item for ${material.asMaterialName()}")

    @JvmStatic
    fun getDust(material: HTMaterialLike): HTSimpleDeferredItem = getMaterial(CommonMaterialPrefixes.DUST, material)

    @JvmStatic
    fun getGem(material: HTMaterialLike): HTSimpleDeferredItem = getMaterial(CommonMaterialPrefixes.GEM, material)

    @JvmStatic
    fun getFood(material: HTMaterialLike): HTSimpleDeferredItem = getMaterial(CommonMaterialPrefixes.FOOD, material)

    @JvmStatic
    fun getGear(material: HTMaterialLike): HTSimpleDeferredItem = getMaterial(CommonMaterialPrefixes.GEAR, material)

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

    @JvmStatic
    fun getMaterialMap(material: HTMaterialLike): Map<HTMaterialPrefix, HTSimpleDeferredItem> = MATERIALS.column(material.asMaterialKey())

    //    Armors    //

    @JvmField
    val NIGHT_VISION_GOGGLES: HTDeferredItem<*> =
        HTArmorVariant.HELMET.registerItem(REGISTER, RagiumEquipmentMaterials.RAGI_CRYSTAL, "night_vision_goggles")

    @JvmStatic
    val ARMORS: ImmutableTable<HTArmorVariant, HTMaterialKey, HTDeferredItem<*>> = buildTable {
        val materials: List<HTEquipmentMaterial> = listOf(
            RagiumEquipmentMaterials.AZURE_STEEL,
            RagiumEquipmentMaterials.DEEP_STEEL,
        )
        for (material: HTEquipmentMaterial in materials) {
            for (variant: HTArmorVariant in HTArmorVariant.entries) {
                this[variant, material.asMaterialKey()] = variant.registerItem(REGISTER, material)
            }
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
    val MAGNET: HTSimpleDeferredItem = REGISTER.registerItemWith(
        "ragi_magnet",
        RagiumConfig.COMMON.basicMagnetRange,
        ::HTMagnetItem,
    ) { it.description(RagiumCommonTranslation.MAGNET) }

    @JvmField
    val ADVANCED_MAGNET: HTSimpleDeferredItem =
        REGISTER.registerItemWith("advanced_ragi_magnet", RagiumConfig.COMMON.advancedMagnetRange, ::HTMagnetItem)

    @JvmField
    val DYNAMIC_LANTERN: HTSimpleDeferredItem = REGISTER.registerItem("ragi_lantern", ::HTDynamicLanternItem) {
        it.description(RagiumCommonTranslation.DYNAMIC_LANTERN)
    }

    @JvmField
    val LOOT_TICKET: HTSimpleDeferredItem = REGISTER.registerItem("ragi_ticket", ::HTLootTicketItem)

    @JvmField
    val DRILL: HTSimpleDeferredItem = REGISTER.registerItem("drill", ::HTDrillItem) {
        it.requiredFeatures(RagiumAPI.WORK_IN_PROGRESS)
    }

    // Azure
    // Crimson
    @JvmField
    val CHARGES: Map<HTChargeType, HTSimpleDeferredItem> = HTChargeType.entries.associateWith { chargeType: HTChargeType ->
        REGISTER.registerItemWith(
            "${chargeType.serializedName}_charge",
            chargeType,
            ::HTChargeItem,
        ) { it.component(RagiumDataComponents.CHARGE_POWER, HTChargeType.DEFAULT_POWER) }
    }

    // Warped
    @JvmField
    val TELEPORT_KEY: HTSimpleDeferredItem = REGISTER.registerItem("teleport_key", ::HTTeleportKeyItem)

    // Eldritch
    @JvmField
    val ELDRITCH_EGG: HTSimpleDeferredItem = REGISTER.registerItem("eldritch_egg", ::HTCaptureEggItem) {
        it.description(RagiumCommonTranslation.ELDRITCH_EGG)
    }

    @JvmField
    val UNIVERSAL_BUNDLE: HTSimpleDeferredItem = REGISTER.registerItem("universal_bundle", ::HTUniversalBundleItem) {
        it.component(RagiumDataComponents.COLOR, DyeColor.WHITE)
    }

    // Other
    @JvmField
    val POTION_BUNDLE: HTSimpleDeferredItem = REGISTER.registerItem("potion_bundle", ::HTPotionBundleItem)

    @JvmField
    val SLOT_COVER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("slot_cover") {
        it.description(RagiumCommonTranslation.SLOT_COVER)
    }

    @JvmField
    val TRADER_CATALOG: HTSimpleDeferredItem = REGISTER.registerItem("trader_catalog", ::HTTraderCatalogItem) {
        it.description(RagiumCommonTranslation.TRADER_CATALOG)
    }

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
        listOf(
            RagiumEquipmentMaterials.AZURE_STEEL,
            RagiumEquipmentMaterials.DEEP_STEEL,
            RagiumEquipmentMaterials.NIGHT_METAL,
        ).forEach { material: HTEquipmentMaterial ->
            for (variant: HTToolVariant in VanillaToolVariant.entries) {
                register(variant, material)
            }
        }
    }

    @JvmStatic
    fun getTool(variant: HTToolVariant, material: HTMaterialLike): HTDeferredItem<*> = TOOLS[variant, material.asMaterialKey()]
        ?: error("Unknown ${variant.variantName()} item for ${material.asMaterialName()}")

    @JvmStatic
    fun getHammer(material: HTMaterialLike): HTDeferredItem<*> = getTool(HTHammerToolVariant, material)

    @JvmStatic
    fun getToolMap(material: HTMaterialLike): Map<HTToolVariant, HTDeferredItem<*>> = TOOLS.column(material.asMaterialKey())

    //    Foods    //

    @JvmField
    val CREAM_BOWL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("cream_bowl")

    // Ice Cream
    @JvmField
    val ICE_CREAM: HTSimpleDeferredItem = REGISTER.registerItem("ice_cream", ::HTIceCreamItem) {
        it.food(RagiumFoods.ICE_CREAM).description(RagiumCommonTranslation.ICE_CREAM)
    }

    @JvmField
    val ICE_CREAM_SODA: HTSimpleDeferredItem = REGISTER.registerItem("ice_cream_soda", ::HTPotionSodaItem) {
        it.foodSound(SoundEvents.GENERIC_DRINK)
    }

    @JvmField
    val POTION_DROP: HTSimpleDeferredItem = REGISTER.registerItem("potion_drop", ::HTPotionDropItem)

    // Meat
    @JvmField
    val CANNED_COOKED_MEAT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("canned_cooked_meat") {
        it.food(RagiumFoods.CANNED_COOKED_MEAT)
    }

    // Sponge
    @JvmField
    val MELON_PIE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("melon_pie") {
        it.food(RagiumFoods.MELON_PIE)
    }

    @JvmField
    val SWEET_BERRIES_CAKE_SLICE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("sweet_berries_cake_slice") {
        it.food(RagiumFoods.SWEET_BERRIES_CAKE)
    }

    // Cherry
    @JvmField
    val RAGI_CHERRY: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ragi_cherry") {
        it.food(RagiumFoods.RAGI_CHERRY).description(RagiumCommonTranslation.RAGI_CHERRY)
    }

    @JvmField
    val RAGI_CHERRY_PULP: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ragi_cherry_pulp") {
        it.food(RagiumFoods.RAGI_CHERRY_PULP)
    }

    @JvmField
    val RAGI_CHERRY_JUICE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ragi_cherry_juice") {
        it.food(RagiumFoods.RAGI_CHERRY_JAM).foodSound(SoundEvents.GENERIC_DRINK)
    }

    @JvmField
    val RAGI_CHERRY_JAM: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ragi_cherry_jam") {
        it.food(RagiumFoods.RAGI_CHERRY_JAM).foodSound(SoundEvents.HONEY_DRINK)
    }

    @JvmField
    val RAGI_CHERRY_PIE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ragi_cherry_pie")

    @JvmField
    val RAGI_CHERRY_PIE_SLICE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ragi_cherry_pie_slice") {
        it.food(RagiumFoods.RAGI_CHERRY_PIE_SLICE)
    }

    @JvmField
    val RAGI_CHERRY_TOAST: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ragi_cherry_toast") {
        it.food(RagiumFoods.RAGI_CHERRY_JAM)
    }

    @JvmField
    val FEVER_CHERRY: HTSimpleDeferredItem = REGISTER.registerSimpleItem("fever_cherry") {
        it.food(RagiumFoods.FEVER_CHERRY).rarity(Rarity.EPIC)
    }

    // Other
    @JvmField
    val BOTTLED_BEE: HTSimpleDeferredItem = REGISTER.registerItem("bottled_bee", ::HTBottledBeeItem) {
        it.requiredFeatures(RagiumAPI.WORK_IN_PROGRESS)
    }

    @JvmField
    val AMBROSIA: HTSimpleDeferredItem = REGISTER.registerItem(
        "ambrosia",
        ::HTAmbrosiaItem,
    ) { it.food(RagiumFoods.AMBROSIA).description(RagiumCommonTranslation.AMBROSIA) }

    //    Machine Parts    //

    @JvmField
    val COILS: Map<HTMaterialKey, HTSimpleDeferredItem> = arrayOf(RagiumMaterialKeys.RAGI_ALLOY, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)
        .associateWith { key: HTMaterialKey -> REGISTER.registerSimpleItem("${key.name}_coil") }

    @JvmStatic
    fun getCoil(key: HTMaterialKey): HTDeferredItem<*> = COILS[key]!!

    // Catalyst
    @JvmField
    val MOLDS: Map<HTMoldType, HTSimpleDeferredItem> = HTMoldType.entries.associateWith { type: HTMoldType ->
        REGISTER.registerSimpleItem("${type.serializedName}_mold")
    }

    @JvmField
    val POLYMER_CATALYST: HTSimpleDeferredItem = REGISTER.registerSimpleItem("polymer_catalyst")

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
    val BASIC_CIRCUIT: HTSimpleDeferredItem =
        REGISTER.registerItemWith("basic_circuit", HTBaseTier.BASIC, ::HTTierBasedItem)

    @JvmField
    val ADVANCED_CIRCUIT: HTSimpleDeferredItem =
        REGISTER.registerItemWith("advanced_circuit", HTBaseTier.ADVANCED, ::HTTierBasedItem)

    //    Vehicles    //

    @JvmField
    val TANK_MINECART: HTSimpleDeferredItem = REGISTER.registerItem("tank_minecart", ::HTTankMinecartItem)

    //    Upgrades    //

    @JvmField
    val COMPONENTS: Map<HTComponentTier, HTSimpleDeferredItem> = HTComponentTier.entries.associateWith { tier: HTComponentTier ->
        REGISTER.registerItemWith("${tier.asMaterialName()}_component", tier, ::HTTierBasedItem) { it.stacksTo(1) }
    }

    @JvmStatic
    fun getComponent(tier: HTComponentTier): HTSimpleDeferredItem = COMPONENTS[tier]!!

    @JvmField
    val GRAVITATIONAL_UNIT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("gravitational_unit")

    @JvmField
    val ETERNAL_COMPONENT: HTSimpleDeferredItem =
        REGISTER.registerItemWith("eternal_component", HTBaseTier.CREATIVE, ::HTTierBasedItem)

    // Machine
    @JvmStatic
    val MACHINE_UPGRADES: ImmutableTable<HTUpgradeVariant, HTBaseTier, HTSimpleDeferredItem> = buildTable {
        fun register(variant: HTUpgradeVariant, tier: HTBaseTier, vararg pairs: Pair<HTUpgradeKey, Fraction>) {
            this[variant, tier] = REGISTER.registerSimpleItem("${tier.serializedName}_${variant.variantName()}_upgrade") {
                it
                    .stacksTo(1)
                    .component(RagiumDataComponents.MACHINE_UPGRADE, HTComponentUpgrade.create(*pairs))
                    .component(
                        RagiumDataComponents.MACHINE_UPGRADE_FILTER,
                        HTKeyOrTagHelper.INSTANCE.create(RagiumModTags.BlockEntityTypes.MACHINES_ELECTRIC),
                    )
            }
        }

        // Efficiency
        register(
            HTUpgradeVariant.EFFICIENCY,
            HTBaseTier.BASIC,
            RagiumUpgradeKeys.ENERGY_EFFICIENCY to fraction(5, 4),
        )
        register(
            HTUpgradeVariant.EFFICIENCY,
            HTBaseTier.ADVANCED,
            RagiumUpgradeKeys.ENERGY_EFFICIENCY to fraction(3, 2),
        )
        // Energy Capacity
        register(
            HTUpgradeVariant.ENERGY_CAPACITY,
            HTBaseTier.BASIC,
            RagiumUpgradeKeys.ENERGY_CAPACITY to fraction(4),
        )
        register(
            HTUpgradeVariant.ENERGY_CAPACITY,
            HTBaseTier.ADVANCED,
            RagiumUpgradeKeys.ENERGY_CAPACITY to fraction(8),
        )
        // Speed
        register(
            HTUpgradeVariant.SPEED,
            HTBaseTier.BASIC,
            RagiumUpgradeKeys.ENERGY_EFFICIENCY to fraction(4, 5),
            RagiumUpgradeKeys.ENERGY_GENERATION to fraction(5, 4),
            RagiumUpgradeKeys.SPEED to fraction(5, 4),
        )
        register(
            HTUpgradeVariant.SPEED,
            HTBaseTier.ADVANCED,
            RagiumUpgradeKeys.ENERGY_EFFICIENCY to fraction(2, 3),
            RagiumUpgradeKeys.ENERGY_GENERATION to fraction(3, 2),
            RagiumUpgradeKeys.SPEED to fraction(3, 2),
        )
    }

    @JvmStatic
    fun getUpgrade(variant: HTUpgradeVariant, tier: HTBaseTier): HTSimpleDeferredItem = MACHINE_UPGRADES[variant, tier]
        ?: error("Unknown ${tier.serializedName} ${variant.variantName()} upgrade")

    // Processor
    @JvmField
    val EFFICIENT_CRUSH_UPGRADE: HTSimpleDeferredItem =
        registerUpgrade("efficient_crush", RagiumUpgradeKeys.USE_LUBRICANT, RagiumCommonTranslation.EFFICIENT_CRUSH_UPGRADE)

    @JvmField
    val PRIMARY_ONLY_UPGRADE: HTSimpleDeferredItem =
        registerUpgrade("primary_only", RagiumUpgradeKeys.DISABLE_EXTRA, RagiumCommonTranslation.PRIMARY_ONLY_UPGRADE)

    // Device
    @JvmField
    val EXP_COLLECTOR_UPGRADE: HTSimpleDeferredItem =
        registerUpgrade("exp_collector", RagiumUpgradeKeys.EXP_COLLECTING, RagiumCommonTranslation.EXP_COLLECTOR_UPGRADE)

    @JvmField
    val FISHING_UPGRADE: HTSimpleDeferredItem =
        registerUpgrade("fishing", RagiumUpgradeKeys.FISHING, RagiumCommonTranslation.FISHING_UPGRADE)

    @JvmField
    val MOB_CAPTURE_UPGRADE: HTSimpleDeferredItem =
        registerUpgrade("mob_capture", RagiumUpgradeKeys.MOB_CAPTURE, RagiumCommonTranslation.MOB_CAPTURE_UPGRADE)

    @JvmStatic
    private fun registerUpgrade(name: String, key: HTUpgradeKey, translation: HTTranslation): HTSimpleDeferredItem =
        REGISTER.registerSimpleItem("${name}_upgrade") {
            it
                .stacksTo(1)
                .description(translation)
                .component(
                    RagiumDataComponents.MACHINE_UPGRADE,
                    HTComponentUpgrade.create(key to fraction(1)),
                )
        }

    @JvmField
    val CREATIVE_UPGRADE: HTSimpleDeferredItem = REGISTER.registerItemWith("creative_upgrade", HTBaseTier.CREATIVE, ::HTTierBasedItem) {
        it.stacksTo(1).component(
            RagiumDataComponents.MACHINE_UPGRADE,
            HTComponentUpgrade.create(RagiumUpgradeKeys.IS_CREATIVE to fraction(1)),
        )
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
        registerFluid(
            event,
            { stack: ItemStack ->
                val capacity: Int = HTUpgradeHelper.getTankCapacity(stack, RagiumConfig.COMMON.tankCapacity.asInt)
                HTComponentFluidTank.create(stack, 0, capacity)
            },
            RagiumBlocks.TANK,
        )

        registerFluid(
            event,
            { stack: ItemStack ->
                val capacity: Int = HTUpgradeHelper.getTankCapacity(stack, 8000)
                HTComponentFluidTank.create(stack, 0, capacity, filter = RagiumFluidContents.DEW_OF_THE_WARP::isOf)
            },
            TELEPORT_KEY,
        )

        // Energy
        registerEnergy(
            event,
            { stack: ItemStack ->
                HTComponentEnergyBattery.create(stack, HTUpgradeHelper.getEnergyCapacity(stack, 160000))
            },
            DRILL,
        )

        // Upgrade
        for (item: Item in BuiltInRegistries.ITEM) {
            // Component-Based for all items
            event.registerItem(
                RagiumCapabilities.UPGRADE_ITEM,
                { stack: ItemStack, _: Void? ->
                    if (stack.has(RagiumDataComponents.MACHINE_UPGRADE)) {
                        HTUpgradeProvider { key: HTUpgradeKey ->
                            stack.get(RagiumDataComponents.MACHINE_UPGRADE)?.get(key) ?: Fraction.ZERO
                        }
                    } else {
                        null
                    }
                },
                item,
            )

            event.registerItem(
                RagiumCapabilities.UPGRADABLE_ITEM,
                { stack: ItemStack, _: Void? ->
                    if (stack.has(RagiumDataComponents.MACHINE_UPGRADES)) {
                        HTComponentUpgradeHandler(stack)
                    } else {
                        null
                    }
                },
                item,
            )
        }

        for ((tier: HTComponentTier, item: ItemLike) in COMPONENTS) {
            event.registerItem(
                RagiumCapabilities.UPGRADE_ITEM,
                { _: ItemStack, _: Void? ->
                    HTUpgradeProvider { key: HTUpgradeKey ->
                        if (key == RagiumUpgradeKeys.BASE_MULTIPLIER) {
                            fraction(tier.ordinal + 2)
                        } else {
                            Fraction.ZERO
                        }
                    }
                },
                item,
            )
        }

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

        fun setEnch(item: ItemLike, ench: ResourceKey<Enchantment>, level: Int = 1) {
            modify(item, RagiumDataComponents.INTRINSIC_ENCHANTMENT, HTIntrinsicEnchantment(ench, level))
        }

        fun setFilter(item: ItemLike, type: HTDeferredBlockEntityType<*>) {
            modify(item, RagiumDataComponents.MACHINE_UPGRADE_FILTER, HTKeyOrTagHelper.INSTANCE.create(type.key))
        }

        fun setFilter(item: ItemLike, tagKey: TagKey<BlockEntityType<*>>) {
            modify(item, RagiumDataComponents.MACHINE_UPGRADE_FILTER, HTKeyOrTagHelper.INSTANCE.create(tagKey))
        }

        // Tools
        for (item: HTDeferredItem<*> in getToolMap(RagiumMaterialKeys.AZURE_STEEL).values) {
            setEnch(item, Enchantments.SILK_TOUCH)
        }

        setEnch(getTool(VanillaToolVariant.PICKAXE, RagiumMaterialKeys.DEEP_STEEL), Enchantments.FORTUNE, 5)
        setEnch(getTool(VanillaToolVariant.AXE, RagiumMaterialKeys.DEEP_STEEL), RagiumEnchantments.STRIKE)
        setEnch(getTool(VanillaToolVariant.SWORD, RagiumMaterialKeys.DEEP_STEEL), RagiumEnchantments.NOISE_CANCELING, 5)

        // Upgrades
        setFilter(EFFICIENT_CRUSH_UPGRADE, RagiumModTags.BlockEntityTypes.EFFICIENT_CRUSH_UPGRADABLE)
        setFilter(PRIMARY_ONLY_UPGRADE, RagiumModTags.BlockEntityTypes.EXTRA_OUTPUT_UPGRADABLE)

        setFilter(EXP_COLLECTOR_UPGRADE, RagiumBlockEntityTypes.FLUID_COLLECTOR)
        setFilter(FISHING_UPGRADE, RagiumBlockEntityTypes.ITEM_COLLECTOR)
        setFilter(MOB_CAPTURE_UPGRADE, RagiumBlockEntityTypes.ITEM_COLLECTOR)

        RagiumAPI.LOGGER.info("Modified default item components!")
    }

    private fun Item.Properties.description(translation: HTTranslation): Item.Properties =
        this.component(RagiumDataComponents.DESCRIPTION, translation)

    private fun Item.Properties.enchantment(ench: ResourceKey<Enchantment>, level: Int = 1): Item.Properties =
        this.component(RagiumDataComponents.INTRINSIC_ENCHANTMENT, HTIntrinsicEnchantment(ench, level))

    private fun Item.Properties.foodSound(sound: SoundEvent): Item.Properties = apply {
        val itemSound: HTItemSoundEvent = HTItemSoundEvent.create(sound)
        component(RagiumDataComponents.DRINK_SOUND, itemSound)
        component(RagiumDataComponents.EAT_SOUND, itemSound)
    }
}
