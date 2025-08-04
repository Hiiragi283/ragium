package hiiragi283.ragium.setup

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.getEnchantmentLevel
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.item.HTConsumableItem
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.registry.HTTaggedHolder
import hiiragi283.ragium.api.util.HTIntrinsicEnchantment
import hiiragi283.ragium.api.util.HTPotionBundle
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.item.HTAzureSteelTemplateItem
import hiiragi283.ragium.common.item.HTBlastChargeItem
import hiiragi283.ragium.common.item.HTCaptureEggItem
import hiiragi283.ragium.common.item.HTDeepSteelTemplateItem
import hiiragi283.ragium.common.item.HTDrillItem
import hiiragi283.ragium.common.item.HTDynamicLanternItem
import hiiragi283.ragium.common.item.HTExpMagnetItem
import hiiragi283.ragium.common.item.HTForgeHammerItem
import hiiragi283.ragium.common.item.HTLootTicketItem
import hiiragi283.ragium.common.item.HTPotionBundleItem
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
import net.minecraft.tags.TagKey
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
import net.neoforged.neoforge.energy.ComponentEnergyStorage
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
        REGISTER.addAlias(RagiumAPI.id("item_collector"), RagiumAPI.id("item_buffer"))

        Gems.entries
        Compounds.entries
        Ingots.entries
        Nuggets.entries
        Dusts.entries

        ForgeHammers.entries
        Tickets.entries

        Circuits.entries

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

    //    Materials    //

    @JvmField
    val RAGI_COKE: DeferredItem<Item> = register("ragi_coke")

    @JvmField
    val COMPRESSED_SAWDUST: DeferredItem<Item> = register("compressed_sawdust")

    @JvmField
    val TAR: DeferredItem<Item> = register("tar")

    @JvmField
    val ELDRITCH_ORB: DeferredItem<Item> = register("eldritch_orb")

    @JvmField
    val DEEP_SCRAP: DeferredItem<Item> = register("deep_scrap")

    // Gems
    enum class Gems(tagPath: String? = null) :
        HTItemHolderLike,
        HTTaggedHolder<Item> {
        AZURE_SHARD("azure"),
        RAGI_CRYSTAL,
        CRIMSON_CRYSTAL,
        WARPED_CRYSTAL,
        ELDRITCH_PEARL,
        ;

        override val holder: DeferredItem<*> = register(name.lowercase())
        override val tagKey: TagKey<Item> = itemTagKey(commonId(RagiumConst.GEMS, tagPath ?: name.lowercase()))
    }

    // Ingots
    enum class Compounds : HTItemHolderLike {
        RAGI_ALLOY,
        ADVANCED_RAGI_ALLOY,
        AZURE_STEEL,
        ;

        override val holder: DeferredItem<*> = register(name.lowercase(), "compound")
    }

    enum class Ingots :
        HTItemHolderLike,
        HTTaggedHolder<Item> {
        RAGI_ALLOY,
        ADVANCED_RAGI_ALLOY,
        AZURE_STEEL,
        DEEP_STEEL,
        ;

        override val holder: DeferredItem<*> = register(name.lowercase(), "ingot")
        override val tagKey: TagKey<Item> = itemTagKey(commonId(RagiumConst.INGOTS, name.lowercase()))
    }

    // Nuggets
    enum class Nuggets :
        HTItemHolderLike,
        HTTaggedHolder<Item> {
        RAGI_ALLOY,
        ADVANCED_RAGI_ALLOY,
        AZURE_STEEL,
        DEEP_STEEL,
        ;

        override val holder: DeferredItem<*> = register(name.lowercase(), "nugget")
        override val tagKey: TagKey<Item> = itemTagKey(commonId(RagiumConst.NUGGETS, name.lowercase()))
    }

    // Dusts
    enum class Dusts(path: String? = null, tagPath: String? = null) :
        HTItemHolderLike,
        HTTaggedHolder<Item> {
        SAW("sawdust", "wood"),
        ASH,
        RAGINITE,
        OBSIDIAN,
        CINNABAR,
        SALTPETER,
        SULFUR,
        ;

        override val holder: DeferredItem<*> = register(path ?: "${name.lowercase()}_dust")
        override val tagKey: TagKey<Item> = itemTagKey(commonId(RagiumConst.DUSTS, tagPath ?: name.lowercase()))
    }

    //    Armors    //

    @JvmField
    val AZURE_STEEL_ARMORS = HTArmorSets(RagiumArmorMaterials.AZURE_STEEL, RagiumConst.AZURE_STEEL, 20)

    @JvmField
    val DEEP_STEEL_ARMORS = HTArmorSets(RagiumArmorMaterials.DEEP_STEEL, RagiumConst.DEEP_STEEL, 20)

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
    val ADVANCED_RAGI_ALLOY_UPGRADE_SMITHING_TEMPLATE: DeferredItem<Item> =
        register("${RagiumConst.ADVANCED_RAGI_ALLOY}_upgrade_smithing_template")

    @JvmField
    val AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE: DeferredItem<Item> =
        register("${RagiumConst.AZURE_STEEL}_upgrade_smithing_template", { _: Item.Properties -> HTAzureSteelTemplateItem() })

    @JvmField
    val DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE: DeferredItem<Item> =
        register("${RagiumConst.DEEP_STEEL}_upgrade_smithing_template", { _: Item.Properties -> HTDeepSteelTemplateItem() })

    @JvmField
    val AZURE_STEEL_TOOLS = HTToolSets(RagiumToolTiers.AZURE_STEEL, RagiumConst.AZURE_STEEL)

    @JvmField
    val DEEP_STEEL_TOOLS = HTToolSets(RagiumToolTiers.DEEP_STEEL, RagiumConst.DEEP_STEEL)

    @JvmField
    val DRILL: DeferredItem<Item> = register("drill", ::HTDrillItem)

    enum class ForgeHammers(tier: Tier) : HTItemHolderLike {
        IRON(Tiers.IRON),
        DIAMOND(Tiers.DIAMOND),
        NETHERITE(Tiers.NETHERITE),
        RAGI_ALLOY(RagiumToolTiers.RAGI_ALLOY),
        AZURE_STEEL(RagiumToolTiers.AZURE_STEEL),
        DEEP_STEEL(RagiumToolTiers.DEEP_STEEL),
        ;

        override val holder: DeferredItem<*> = register(
            "${name.lowercase()}_hammer",
            { prop: Item.Properties -> HTForgeHammerItem(tier, prop) },
        )
    }

    //    Tickets    //

    enum class Tickets(factory: (Item.Properties) -> Item = ::Item, properties: Item.Properties = Item.Properties()) : HTItemHolderLike {
        BLANK,
        RAGI(::HTLootTicketItem),
        AZURE,
        BLOODY,
        TELEPORT(::HTTeleportTicketItem, Item.Properties().durability(63)),
        ELDRITCH,
        DAYBREAK,
        ETERNAL,
        ;

        override val holder: DeferredItem<*> = register("${name.lowercase()}_ticket", factory, properties)
    }

    //    Foods    //

    @JvmStatic
    private fun registerFood(
        name: String,
        foodProperties: FoodProperties,
        properties: Item.Properties = Item.Properties(),
    ): DeferredItem<Item> = register(name, HTConsumableItem.create(), properties.food(foodProperties))

    @JvmField
    val ICE_CREAM: DeferredItem<Item> = registerFood("ice_cream", RagiumFoods.ICE_CREAM)

    @JvmField
    val ICE_CREAM_SODA: DeferredItem<Item> = register("ice_cream_soda", HTConsumableItem.create(sound = SoundEvents.GENERIC_DRINK))

    @JvmField
    val CHOCOLATE_INGOT: DeferredItem<Item> = registerFood("${RagiumConst.CHOCOLATE}_ingot", RagiumFoods.CHOCOLATE)

    // Meat
    @JvmField
    val MINCED_MEAT: DeferredItem<Item> = register("minced_meat")

    @JvmField
    val MEAT_INGOT: DeferredItem<Item> = registerFood("${RagiumConst.MEAT}_ingot", Foods.BEEF)

    @JvmField
    val COOKED_MEAT_INGOT: DeferredItem<Item> = registerFood("${RagiumConst.COOKED_MEAT}_ingot", Foods.COOKED_BEEF)

    @JvmField
    val CANNED_COOKED_MEAT: DeferredItem<Item> = registerFood("canned_${RagiumConst.COOKED_MEAT}", RagiumFoods.CANNED_COOKED_MEAT)

    // Sponge
    @JvmField
    val MELON_PIE: DeferredItem<Item> = registerFood("melon_pie", RagiumFoods.MELON_PIE)

    @JvmField
    val SWEET_BERRIES_CAKE_SLICE: DeferredItem<Item> = registerFood("sweet_berries_cake_slice", RagiumFoods.SWEET_BERRIES_CAKE)

    // Cherry
    @JvmField
    val RAGI_CHERRY: DeferredItem<Item> = registerFood("ragi_cherry", RagiumFoods.RAGI_CHERRY)

    @JvmField
    val RAGI_CHERRY_JAM: DeferredItem<Item> = register(
        "ragi_cherry_jam",
        HTConsumableItem.create(sound = SoundEvents.HONEY_DRINK),
        Item.Properties().food(RagiumFoods.RAGI_CHERRY_JAM),
    )

    @JvmField
    val FEVER_CHERRY: DeferredItem<Item> = registerFood("fever_cherry", RagiumFoods.FEVER_CHERRY, Item.Properties().rarity(Rarity.EPIC))

    // Other
    @JvmField
    val BOTTLED_BEE: DeferredItem<Item> = register("bottled_bee")

    @JvmField
    val EXP_BERRIES: DeferredItem<Item> = register(
        "exp_berries",
        { prop: Item.Properties -> ItemNameBlockItem(RagiumBlocks.EXP_BERRY_BUSH.get(), prop) },
    )

    @JvmField
    val WARPED_WART: DeferredItem<Item> = register(
        "warped_wart",
        ::HTWarpedWartItem,
        Item.Properties().food(RagiumFoods.WARPED_WART),
    )

    @JvmField
    val AMBROSIA: DeferredItem<Item> = registerFood("ambrosia", RagiumFoods.AMBROSIA, Item.Properties().rarity(Rarity.EPIC))

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

    enum class Circuits :
        HTItemHolderLike,
        HTTaggedHolder<Item> {
        BASIC,
        ADVANCED,
        ELITE,
        ULTIMATE,
        ;

        override val holder: DeferredItem<*> = register(name.lowercase(), "circuit")
        override val tagKey: TagKey<Item> = itemTagKey(commonId(RagiumConst.CIRCUITS, name.lowercase()))
    }

    //    Event    //

    @SubscribeEvent
    fun registerItemCapabilities(event: RegisterCapabilitiesEvent) {
        event.registerItem(
            Capabilities.FluidHandler.ITEM,
            { stack: ItemStack, _: Void? -> FluidBucketWrapper(stack) },
            *RagiumFluidContents.REGISTER.itemEntries.toTypedArray(),
        )

        registerEnergy(event, DRILL, 160000)

        registerDrums(event)

        LOGGER.info("Registered item capabilities!")
    }

    @JvmStatic
    fun registerEnergy(event: RegisterCapabilitiesEvent, item: ItemLike, capacity: Int) {
        event.registerItem(
            Capabilities.EnergyStorage.ITEM,
            { stack: ItemStack, _: Void? -> ComponentEnergyStorage(stack, RagiumDataComponents.ENERGY.get(), capacity) },
            item,
        )
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

        register(RagiumAPI.getConfig().getSmallDrumCapacity(), RagiumBlocks.Drums.SMALL)
        register(RagiumAPI.getConfig().getMediumDrumCapacity(), RagiumBlocks.Drums.MEDIUM)
        register(RagiumAPI.getConfig().getLargeDrumCapacity(), RagiumBlocks.Drums.LARGE)
        register(RagiumAPI.getConfig().getHugeDrumCapacity(), RagiumBlocks.Drums.HUGE)
    }

    @SubscribeEvent
    fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        // Tickets
        fun setColor(item: HTItemHolderLike, color: ChatFormatting) {
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

        setColor(Tickets.BLANK, ChatFormatting.DARK_GRAY)

        setColor(Tickets.RAGI, ChatFormatting.RED)
        setColor(Tickets.AZURE, ChatFormatting.BLUE)
        setColor(Tickets.BLOODY, ChatFormatting.DARK_RED)
        setColor(Tickets.TELEPORT, ChatFormatting.DARK_AQUA)
        setColor(Tickets.ELDRITCH, ChatFormatting.LIGHT_PURPLE)

        setColor(Tickets.DAYBREAK, ChatFormatting.GOLD)
        setColor(Tickets.ETERNAL, ChatFormatting.YELLOW)

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
        // Other
        event.modify(POTION_BUNDLE) { builder: DataComponentPatch.Builder ->
            builder.set(RagiumDataComponents.POTION_BUNDLE.get(), HTPotionBundle.EMPTY)
        }

        event.modify(RagiumBlocks.Devices.CEU) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.RARITY, Rarity.EPIC)
        }

        LOGGER.info("Modified default item components!")
    }
}
