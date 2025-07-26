package hiiragi283.ragium.setup

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.extension.getEnchantmentLevel
import hiiragi283.ragium.api.item.HTConsumableItem
import hiiragi283.ragium.api.item.HTForgeHammerItem
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.upgrade.HTUpgrade
import hiiragi283.ragium.api.util.HTIntrinsicEnchantment
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.item.HTAzureSteelTemplateItem
import hiiragi283.ragium.common.item.HTCaptureEggItem
import hiiragi283.ragium.common.item.HTDeepSteelTemplateItem
import hiiragi283.ragium.common.item.HTDynamicLanternItem
import hiiragi283.ragium.common.item.HTExpMagnetItem
import hiiragi283.ragium.common.item.HTLootTicketItem
import hiiragi283.ragium.common.item.HTSimpleMagnetItem
import hiiragi283.ragium.common.item.HTTeleportTicketItem
import hiiragi283.ragium.common.item.HTWarpedWartItem
import hiiragi283.ragium.util.HTArmorSets
import hiiragi283.ragium.util.HTToolSets
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemNameBlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper
import net.neoforged.neoforge.registries.DeferredItem
import org.slf4j.Logger

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumItems {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val REGISTER = HTItemRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        REGISTER.addAlias(RagiumAPI.id("item_magnet"), RagiumAPI.id("ragi_magnet"))
        REGISTER.addAlias(RagiumAPI.id("exp_magnet"), RagiumAPI.id("advanced_ragi_magnet"))

        REGISTER.register(eventBus)

        AZURE_STEEL_ARMORS.init(eventBus)
        DEEP_STEEL_ARMORS.init(eventBus)

        AZURE_STEEL_TOOLS.init(eventBus)
        DEEP_STEEL_TOOLS.init(eventBus)
    }

    @JvmStatic
    private fun register(prefix: String, suffix: String, properties: Item.Properties = Item.Properties()): DeferredItem<Item> =
        register("${prefix}_$suffix", properties)

    @JvmStatic
    private fun register(name: String, properties: Item.Properties = Item.Properties()): DeferredItem<Item> =
        REGISTER.registerSimpleItem(name, properties)

    @JvmStatic
    private fun <T : Item> register(
        name: String,
        factory: (Item.Properties) -> T,
        properties: Item.Properties = Item.Properties(),
    ): DeferredItem<T> = REGISTER.registerItem(name, factory, properties)

    //    Tickets    //

    @JvmField
    val BLANK_TICKET: DeferredItem<Item> = register("blank_ticket")

    @JvmField
    val RAGI_TICKET: DeferredItem<HTLootTicketItem> = register("ragi_ticket", ::HTLootTicketItem)

    @JvmField
    val AZURE_TICKET: DeferredItem<Item> = register("azure_ticket")

    @JvmField
    val BLOODY_TICKET: DeferredItem<Item> = register("bloody_ticket")

    @JvmField
    val TELEPORT_TICKET: DeferredItem<HTTeleportTicketItem> = register("teleport_ticket", ::HTTeleportTicketItem)

    @JvmField
    val ELDRITCH_TICKET: DeferredItem<Item> = register("eldritch_ticket")

    @JvmField
    val DAYBREAK_TICKET: DeferredItem<Item> = register("daybreak_ticket")

    @JvmField
    val ETERNAL_TICKET: DeferredItem<Item> = register("eternal_ticket")

    //    Materials    //

    @JvmField
    val RAGI_COKE: DeferredItem<Item> = register("ragi_coke")

    @JvmField
    val COMPRESSED_SAWDUST: DeferredItem<Item> = register("compressed_sawdust")

    @JvmField
    val TAR: DeferredItem<Item> = register("tar")

    @JvmField
    val DEEP_SCRAP: DeferredItem<Item> = register("deep_scrap")

    // Gems
    @JvmField
    val RAGI_CRYSTAL: DeferredItem<Item> = register(RagiumConstantValues.RAGI_CRYSTAL)

    @JvmField
    val AZURE_SHARD: DeferredItem<Item> = register("azure_shard")

    @JvmField
    val CRIMSON_CRYSTAL: DeferredItem<Item> = register(RagiumConstantValues.CRIMSON_CRYSTAL)

    @JvmField
    val WARPED_CRYSTAL: DeferredItem<Item> = register(RagiumConstantValues.WARPED_CRYSTAL)

    @JvmField
    val ELDRITCH_ORB: DeferredItem<Item> = register("eldritch_orb")

    @JvmField
    val ELDRITCH_PEARL: DeferredItem<Item> = register(RagiumConstantValues.ELDRITCH_PEARL)

    // Ingots
    @JvmField
    val RAGI_ALLOY_COMPOUND: DeferredItem<Item> = register(RagiumConstantValues.RAGI_ALLOY, "compound")

    @JvmField
    val RAGI_ALLOY_INGOT: DeferredItem<Item> = register(RagiumConstantValues.RAGI_ALLOY, "ingot")

    @JvmField
    val ADVANCED_RAGI_ALLOY_COMPOUND: DeferredItem<Item> = register(RagiumConstantValues.ADVANCED_RAGI_ALLOY, "compound")

    @JvmField
    val ADVANCED_RAGI_ALLOY_INGOT: DeferredItem<Item> = register(RagiumConstantValues.ADVANCED_RAGI_ALLOY, "ingot")

    @JvmField
    val AZURE_STEEL_COMPOUND: DeferredItem<Item> = register(RagiumConstantValues.AZURE_STEEL, "compound")

    @JvmField
    val AZURE_STEEL_INGOT: DeferredItem<Item> = register(RagiumConstantValues.AZURE_STEEL, "ingot")

    @JvmField
    val DEEP_STEEL_INGOT: DeferredItem<Item> = register(RagiumConstantValues.DEEP_STEEL, "ingot")

    // Nuggets
    @JvmField
    val RAGI_ALLOY_NUGGET: DeferredItem<Item> = register(RagiumConstantValues.RAGI_ALLOY, "nugget")

    @JvmField
    val ADVANCED_RAGI_ALLOY_NUGGET: DeferredItem<Item> = register(RagiumConstantValues.ADVANCED_RAGI_ALLOY, "nugget")

    @JvmField
    val AZURE_STEEL_NUGGET: DeferredItem<Item> = register(RagiumConstantValues.AZURE_STEEL, "nugget")

    // Dusts
    @JvmField
    val SAWDUST: DeferredItem<Item> = register("sawdust")

    @JvmField
    val ASH_DUST: DeferredItem<Item> = register("ash", "dust")

    @JvmField
    val RAGINITE_DUST: DeferredItem<Item> = register(RagiumConstantValues.RAGINITE, "dust")

    @JvmField
    val OBSIDIAN_DUST: DeferredItem<Item> = register("obsidian", "dust")

    @JvmField
    val CINNABAR_DUST: DeferredItem<Item> = register("cinnabar", "dust")

    @JvmField
    val QUARTZ_DUST: DeferredItem<Item> = register("quartz", "dust")

    @JvmField
    val SALTPETER_DUST: DeferredItem<Item> = register("saltpeter", "dust")

    @JvmField
    val SULFUR_DUST: DeferredItem<Item> = register("sulfur", "dust")

    //    Armors    //

    @JvmField
    val AZURE_STEEL_ARMORS = HTArmorSets(RagiumArmorMaterials.AZURE_STEEL, RagiumConstantValues.AZURE_STEEL, 20)

    @JvmField
    val DEEP_STEEL_ARMORS = HTArmorSets(RagiumArmorMaterials.DEEP_STEEL, RagiumConstantValues.DEEP_STEEL, 20)

    //    Tools    //

    @JvmField
    val TRADER_CATALOG: DeferredItem<Item> = register("trader_catalog", Item.Properties().stacksTo(1))

    @JvmField
    val RAGI_MAGNET: DeferredItem<HTSimpleMagnetItem> = register("ragi_magnet", ::HTSimpleMagnetItem)

    @JvmField
    val RAGI_LANTERN: DeferredItem<HTDynamicLanternItem> = register("ragi_lantern", ::HTDynamicLanternItem)

    @JvmField
    val ADVANCED_RAGI_MAGNET: DeferredItem<HTExpMagnetItem> = register("advanced_ragi_magnet", ::HTExpMagnetItem)

    @JvmField
    val ENDER_BUNDLE: DeferredItem<Item> = register("ender_bundle", Item.Properties().stacksTo(1))

    @JvmField
    val ELDRITCH_EGG: DeferredItem<HTCaptureEggItem> = register("eldritch_egg", ::HTCaptureEggItem)

    @JvmField
    val ADVANCED_RAGI_ALLOY_UPGRADE_SMITHING_TEMPLATE: DeferredItem<Item> =
        register("${RagiumConstantValues.ADVANCED_RAGI_ALLOY}_upgrade_smithing_template")

    @JvmField
    val AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE: DeferredItem<HTAzureSteelTemplateItem> =
        register("${RagiumConstantValues.AZURE_STEEL}_upgrade_smithing_template", { _: Item.Properties -> HTAzureSteelTemplateItem() })

    @JvmField
    val DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE: DeferredItem<HTDeepSteelTemplateItem> =
        register("${RagiumConstantValues.DEEP_STEEL}_upgrade_smithing_template", { _: Item.Properties -> HTDeepSteelTemplateItem() })

    @JvmField
    val AZURE_STEEL_TOOLS = HTToolSets(RagiumToolTiers.AZURE_STEEL, RagiumConstantValues.AZURE_STEEL)

    @JvmField
    val DEEP_STEEL_TOOLS = HTToolSets(RagiumToolTiers.DEEP_STEEL, RagiumConstantValues.DEEP_STEEL)

    @JvmField
    val FORGE_HAMMERS: Map<Tier, DeferredItem<HTForgeHammerItem>> = listOf(
        Tiers.IRON,
        Tiers.DIAMOND,
        Tiers.NETHERITE,
        RagiumToolTiers.RAGI_ALLOY,
        RagiumToolTiers.AZURE_STEEL,
        RagiumToolTiers.DEEP_STEEL,
    ).associateWith { tier: Tier ->
        val prefix: String = when (tier) {
            Tiers.IRON -> "iron"
            Tiers.DIAMOND -> "diamond"
            Tiers.NETHERITE -> "netherite"
            RagiumToolTiers.RAGI_ALLOY -> RagiumConstantValues.RAGI_ALLOY
            RagiumToolTiers.AZURE_STEEL -> RagiumConstantValues.AZURE_STEEL
            else -> RagiumConstantValues.DEEP_STEEL
        }
        register(
            "${prefix}_hammer",
            { prop: Item.Properties -> HTForgeHammerItem(tier, prop) },
        )
    }

    @JvmStatic
    fun getForgeHammer(tier: Tier): DeferredItem<HTForgeHammerItem> = FORGE_HAMMERS[tier]!!

    //    Foods    //

    @JvmStatic
    private fun registerFood(
        name: String,
        foodProperties: FoodProperties,
        properties: Item.Properties = Item.Properties(),
    ): DeferredItem<HTConsumableItem> = register(name, HTConsumableItem.create(), properties.food(foodProperties))

    @JvmField
    val ICE_CREAM: DeferredItem<HTConsumableItem> = registerFood("ice_cream", RagiumFoods.ICE_CREAM)

    @JvmField
    val ICE_CREAM_SODA: DeferredItem<HTConsumableItem> = register(
        "ice_cream_soda",
        HTConsumableItem.create(sound = SoundEvents.GENERIC_DRINK),
    )

    @JvmField
    val CHOCOLATE_INGOT: DeferredItem<HTConsumableItem> = registerFood("${RagiumConstantValues.CHOCOLATE}_ingot", RagiumFoods.CHOCOLATE)

    // Meat
    @JvmField
    val MINCED_MEAT: DeferredItem<Item> = register("minced_meat")

    @JvmField
    val MEAT_INGOT: DeferredItem<HTConsumableItem> = registerFood("${RagiumConstantValues.MEAT}_ingot", Foods.BEEF)

    @JvmField
    val COOKED_MEAT_INGOT: DeferredItem<HTConsumableItem> = registerFood("${RagiumConstantValues.COOKED_MEAT}_ingot", Foods.COOKED_BEEF)

    @JvmField
    val CANNED_COOKED_MEAT: DeferredItem<HTConsumableItem> = registerFood(
        "canned_${RagiumConstantValues.COOKED_MEAT}",
        RagiumFoods.CANNED_COOKED_MEAT,
    )

    // Sponge
    @JvmField
    val MELON_PIE: DeferredItem<HTConsumableItem> = registerFood("melon_pie", RagiumFoods.MELON_PIE)

    @JvmField
    val SWEET_BERRIES_CAKE_SLICE: DeferredItem<HTConsumableItem> = registerFood("sweet_berries_cake_slice", RagiumFoods.SWEET_BERRIES_CAKE)

    // Cherry
    @JvmField
    val RAGI_CHERRY: DeferredItem<HTConsumableItem> = registerFood("ragi_cherry", RagiumFoods.RAGI_CHERRY)

    @JvmField
    val RAGI_CHERRY_JAM: DeferredItem<HTConsumableItem> = register(
        "ragi_cherry_jam",
        HTConsumableItem.create(sound = SoundEvents.HONEY_DRINK),
        Item.Properties().food(RagiumFoods.RAGI_CHERRY_JAM),
    )

    @JvmField
    val FEVER_CHERRY: DeferredItem<HTConsumableItem> = registerFood(
        "fever_cherry",
        RagiumFoods.FEVER_CHERRY,
        Item.Properties().rarity(Rarity.EPIC),
    )

    // Other
    @JvmField
    val BOTTLED_BEE: DeferredItem<Item> = register("bottled_bee")

    @JvmField
    val EXP_BERRIES: DeferredItem<ItemNameBlockItem> = register(
        "exp_berries",
        { prop: Item.Properties -> ItemNameBlockItem(RagiumBlocks.EXP_BERRY_BUSH.get(), prop) },
    )

    @JvmField
    val WARPED_WART: DeferredItem<HTWarpedWartItem> = register(
        "warped_wart",
        ::HTWarpedWartItem,
        Item.Properties().food(RagiumFoods.WARPED_WART),
    )

    @JvmField
    val AMBROSIA: DeferredItem<HTConsumableItem> =
        registerFood("ambrosia", RagiumFoods.AMBROSIA, Item.Properties().rarity(Rarity.EPIC))

    //    Molds    //

    /*enum class Molds(val tagKey: TagKey<Item>) : ItemLike {
        BLANK(RagiumItemTags.MOLDS_BLANK),
        BALL(RagiumItemTags.MOLDS_BALL),
        BLOCK(RagiumItemTags.MOLDS_BLOCK),
        GEAR(RagiumItemTags.MOLDS_GEAR),
        INGOT(RagiumItemTags.MOLDS_INGOT),
        PLATE(RagiumItemTags.MOLDS_PLATE),
        ROD(RagiumItemTags.MOLDS_ROD),
        WIRE(RagiumItemTags.MOLDS_WIRE),
        ;

        val path = "${name.lowercase()}_mold"
        private val holder: DeferredItem<Item> = register(path)

        override fun asItem(): Item = holder.asItem()
    }*/

    //    Machine Parts    //

    @JvmField
    val ELDER_HEART: DeferredItem<Item> = register("elder_heart")

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
    val PLASTIC_PLATE: DeferredItem<Item> = register("plastic_plate")

    @JvmField
    val SYNTHETIC_FIBER: DeferredItem<Item> = register("synthetic_fiber")

    @JvmField
    val SYNTHETIC_LEATHER: DeferredItem<Item> = register("synthetic_leather")

    @JvmField
    val CIRCUIT_BOARD: DeferredItem<Item> = register("circuit_board")

    @JvmField
    val BASIC_CIRCUIT: DeferredItem<Item> = register(RagiumConstantValues.BASIC, "circuit")

    @JvmField
    val ADVANCED_CIRCUIT: DeferredItem<Item> = register(RagiumConstantValues.ADVANCED, "circuit")

    @JvmField
    val ELITE_CIRCUIT: DeferredItem<Item> = register(RagiumConstantValues.ELITE, "circuit")

    @JvmField
    val ULTIMATE_CIRCUIT: DeferredItem<Item> = register(RagiumConstantValues.ULTIMATE, "circuit")

    //    Event    //

    @SubscribeEvent
    fun registerItemCapabilities(event: RegisterCapabilitiesEvent) {
        event.registerItem(
            Capabilities.FluidHandler.ITEM,
            { stack: ItemStack, _: Void? -> FluidBucketWrapper(stack) },
            *RagiumFluidContents.REGISTER.itemEntries.toTypedArray(),
        )

        registerDrums(event)

        LOGGER.info("Registered item capabilities!")
    }

    @JvmStatic
    private fun registerDrums(event: RegisterCapabilitiesEvent) {
        fun register(capacity: Int, item: ItemLike) {
            event.registerItem(
                Capabilities.FluidHandler.ITEM,
                { stack: ItemStack, _: Void? ->
                    val modifier: Int = stack.getEnchantmentLevel(RagiumEnchantments.CAPACITY) + 1
                    FluidHandlerItemStack(RagiumDataComponents.FLUID_CONTENT, stack, capacity * modifier)
                },
                item,
            )
        }

        register(RagiumConfig.COMMON.smallDrumCapacity.get(), RagiumBlocks.SMALL_DRUM)
        register(RagiumConfig.COMMON.mediumDrumCapacity.get(), RagiumBlocks.MEDIUM_DRUM)
        register(RagiumConfig.COMMON.largeDrumCapacity.get(), RagiumBlocks.LARGE_DRUM)
        register(RagiumConfig.COMMON.hugeDrumCapacity.get(), RagiumBlocks.HUGE_DRUM)
    }

    @SubscribeEvent
    fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        // Tickets
        fun setColor(item: DeferredItem<*>, color: ChatFormatting) {
            event.modify(item) { builder: DataComponentPatch.Builder ->
                builder.set(
                    DataComponents.ITEM_NAME,
                    item
                        .get()
                        .description
                        .copy()
                        .withStyle(color),
                )
            }
        }

        setColor(BLANK_TICKET, ChatFormatting.DARK_GRAY)

        setColor(RAGI_TICKET, ChatFormatting.RED)
        setColor(AZURE_TICKET, ChatFormatting.BLUE)
        setColor(BLOODY_TICKET, ChatFormatting.DARK_RED)
        setColor(TELEPORT_TICKET, ChatFormatting.DARK_AQUA)
        setColor(ELDRITCH_TICKET, ChatFormatting.LIGHT_PURPLE)

        setColor(DAYBREAK_TICKET, ChatFormatting.GOLD)
        setColor(ETERNAL_TICKET, ChatFormatting.YELLOW)

        // Tools
        fun setEnch(item: DeferredItem<*>, ench: ResourceKey<Enchantment>, level: Int = 1) {
            event.modify(item) { builder: DataComponentPatch.Builder ->
                builder.set(RagiumDataComponents.INTRINSIC_ENCHANTMENT.get(), HTIntrinsicEnchantment(ench, level))
            }
        }
        setEnch(AZURE_STEEL_TOOLS.shovelItem, Enchantments.SILK_TOUCH)
        setEnch(AZURE_STEEL_TOOLS.pickaxeItem, Enchantments.SILK_TOUCH)
        setEnch(AZURE_STEEL_TOOLS.axeItem, Enchantments.SILK_TOUCH)
        setEnch(AZURE_STEEL_TOOLS.hoeItem, Enchantments.SILK_TOUCH)

        setEnch(DEEP_STEEL_TOOLS.pickaxeItem, Enchantments.FORTUNE, 5)
        setEnch(DEEP_STEEL_TOOLS.swordItem, RagiumEnchantments.NOISE_CANCELING, 5)
        setEnch(DEEP_STEEL_ARMORS.chestplateItem, RagiumEnchantments.SONIC_PROTECTION)

        // Circuits
        fun setUpgrade(item: DeferredItem<*>, base: Int) {
            event.modify(item) { builder: DataComponentPatch.Builder ->
                builder.set(RagiumDataComponents.UPGRADE.get(), HTUpgrade(base.toFloat()))
            }
        }

        setUpgrade(BASIC_CIRCUIT, 1)
        setUpgrade(ADVANCED_CIRCUIT, 2)
        setUpgrade(ELITE_CIRCUIT, 3)
        setUpgrade(ULTIMATE_CIRCUIT, 4)
        // Creative Item
        event.modify(RagiumBlocks.CEU) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.RARITY, Rarity.EPIC)
        }

        LOGGER.info("Modified default item components!")
    }
}
