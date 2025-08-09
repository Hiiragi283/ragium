package hiiragi283.ragium.setup

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getEnchantmentLevel
import hiiragi283.ragium.api.item.HTConsumableItem
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.item.component.HTPotionBundle
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.registry.HTTaggedHolder
import hiiragi283.ragium.api.util.HTMaterialType
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.item.HTAzureSteelTemplateItem
import hiiragi283.ragium.common.item.HTBlastChargeItem
import hiiragi283.ragium.common.item.HTCaptureEggItem
import hiiragi283.ragium.common.item.HTDeepSteelTemplateItem
import hiiragi283.ragium.common.item.HTDrillItem
import hiiragi283.ragium.common.item.HTDynamicLanternItem
import hiiragi283.ragium.common.item.HTExpMagnetItem
import hiiragi283.ragium.common.item.HTLootTicketItem
import hiiragi283.ragium.common.item.HTPotionBundleItem
import hiiragi283.ragium.common.item.HTSimpleMagnetItem
import hiiragi283.ragium.common.item.HTTeleportTicketItem
import hiiragi283.ragium.common.item.HTWarpedWartItem
import hiiragi283.ragium.common.storage.energy.HTComponentEnergyStorage
import hiiragi283.ragium.common.storage.fluid.HTComponentFluidHandler
import hiiragi283.ragium.util.variant.HTArmorVariant
import hiiragi283.ragium.util.variant.HTForgeHammerVariant
import hiiragi283.ragium.util.variant.HTToolVariant
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
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
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

        AzureSteelArmors.entries
        DeepSteelArmors.entries

        ForgeHammers.entries
        AzureSteelTools.entries
        DeepSteelTools.entries
        Tickets.entries

        Circuits.entries

        REGISTER.register(eventBus)
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
    enum class Gems(override val material: HTMaterialType) :
        HTItemHolderLike.Materialized,
        HTTaggedHolder<Item> {
        AZURE_SHARD(HTMaterialType.AZURE),
        RAGI_CRYSTAL(HTMaterialType.RAGI_CRYSTAL),
        CRIMSON_CRYSTAL(HTMaterialType.CRIMSON_CRYSTAL),
        WARPED_CRYSTAL(HTMaterialType.WARPED_CRYSTAL),
        ELDRITCH_PEARL(HTMaterialType.ELDRITCH_PEARL),
        ;

        override val holder: DeferredItem<*> = register(name.lowercase())
        override val tagKey: TagKey<Item> = material.itemTag(RagiumConst.GEMS)
    }

    // Ingots
    enum class Compounds(override val material: HTMaterialType) : HTItemHolderLike.Materialized {
        RAGI_ALLOY(HTMaterialType.RAGI_ALLOY),
        ADVANCED_RAGI_ALLOY(HTMaterialType.ADVANCED_RAGI_ALLOY),
        AZURE_STEEL(HTMaterialType.AZURE_STEEL),
        ;

        override val holder: DeferredItem<*> = register(name.lowercase(), "compound")
    }

    enum class Ingots(override val material: HTMaterialType) :
        HTItemHolderLike.Materialized,
        HTTaggedHolder<Item> {
        // Metal
        RAGI_ALLOY(HTMaterialType.RAGI_ALLOY),
        ADVANCED_RAGI_ALLOY(HTMaterialType.ADVANCED_RAGI_ALLOY),
        AZURE_STEEL(HTMaterialType.AZURE_STEEL),
        DEEP_STEEL(HTMaterialType.DEEP_STEEL),

        // Food
        CHOCOLATE(HTMaterialType.CHOCOLATE),
        MEAT(HTMaterialType.MEAT),
        COOKED_MEAT(HTMaterialType.COOKED_MEAT),
        ;

        override val holder: DeferredItem<*> = register(name.lowercase(), "ingot")
        override val tagKey: TagKey<Item> = material.itemTag(RagiumConst.INGOTS)
    }

    // Nuggets
    enum class Nuggets(override val material: HTMaterialType) :
        HTItemHolderLike.Materialized,
        HTTaggedHolder<Item> {
        RAGI_ALLOY(HTMaterialType.RAGI_ALLOY),
        ADVANCED_RAGI_ALLOY(HTMaterialType.ADVANCED_RAGI_ALLOY),
        AZURE_STEEL(HTMaterialType.AZURE_STEEL),
        DEEP_STEEL(HTMaterialType.DEEP_STEEL),
        ;

        override val holder: DeferredItem<*> = register(name.lowercase(), "nugget")
        override val tagKey: TagKey<Item> = material.itemTag(RagiumConst.NUGGETS)
    }

    // Dusts
    enum class Dusts(override val material: HTMaterialType, path: String? = null) :
        HTItemHolderLike.Materialized,
        HTTaggedHolder<Item> {
        SAW(HTMaterialType.WOOD, "sawdust"),
        ASH(HTMaterialType.ASH),
        RAGINITE(HTMaterialType.RAGINITE),
        OBSIDIAN(HTMaterialType.OBSIDIAN),
        CINNABAR(HTMaterialType.CINNABAR),
        SALTPETER(HTMaterialType.SALTPETER),
        SULFUR(HTMaterialType.SULFUR),
        ;

        override val holder: DeferredItem<*> = register(path ?: "${name.lowercase()}_dust")
        override val tagKey: TagKey<Item> = material.itemTag(RagiumConst.DUSTS)
    }

    //    Armors    //

    enum class AzureSteelArmors(override val variant: HTArmorVariant) : HTItemHolderLike.Typed<HTArmorVariant> {
        HELMET(HTArmorVariant.HELMET),
        CHESTPLATE(HTArmorVariant.CHESTPLATE),
        LEGGINGS(HTArmorVariant.LEGGINGS),
        BOOTS(HTArmorVariant.BOOTS),
        ;

        override val holder: DeferredItem<*> = variant.registerItem(
            REGISTER,
            HTMaterialType.AZURE_STEEL,
            RagiumArmorMaterials.AZURE_STEEL,
            20,
        )
    }

    enum class DeepSteelArmors(override val variant: HTArmorVariant) : HTItemHolderLike.Typed<HTArmorVariant> {
        HELMET(HTArmorVariant.HELMET),
        CHESTPLATE(HTArmorVariant.CHESTPLATE),
        LEGGINGS(HTArmorVariant.LEGGINGS),
        BOOTS(HTArmorVariant.BOOTS),
        ;

        override val holder: DeferredItem<*> = variant.registerItem(
            REGISTER,
            HTMaterialType.DEEP_STEEL,
            RagiumArmorMaterials.DEEP_STEEL,
            20,
        )
    }

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
    val DRILL: DeferredItem<Item> = register("drill", ::HTDrillItem)

    enum class ForgeHammers(override val variant: HTForgeHammerVariant) : HTItemHolderLike.Typed<HTForgeHammerVariant> {
        IRON(HTForgeHammerVariant.IRON),
        DIAMOND(HTForgeHammerVariant.DIAMOND),
        NETHERITE(HTForgeHammerVariant.NETHERITE),
        RAGI_ALLOY(HTForgeHammerVariant.RAGI_ALLOY),
        AZURE_STEEL(HTForgeHammerVariant.AZURE_STEEL),
        DEEP_STEEL(HTForgeHammerVariant.DEEP_STEEL),
        ;

        override val holder: DeferredItem<*> = register("${variant.serializedName}_hammer", variant.factory)
    }

    enum class AzureSteelTools(override val variant: HTToolVariant) : HTItemHolderLike.Typed<HTToolVariant> {
        SHOVEL(HTToolVariant.SHOVEL),
        PICKAXE(HTToolVariant.PICKAXE),
        AXE(HTToolVariant.AXE),
        HOE(HTToolVariant.HOE),
        SWORD(HTToolVariant.SWORD),
        ;

        override val holder: DeferredItem<*> =
            variant.registerItem(REGISTER, HTMaterialType.AZURE_STEEL, RagiumToolTiers.AZURE_STEEL)
    }

    enum class DeepSteelTools(override val variant: HTToolVariant) : HTItemHolderLike.Typed<HTToolVariant> {
        SHOVEL(HTToolVariant.SHOVEL),
        PICKAXE(HTToolVariant.PICKAXE),
        AXE(HTToolVariant.AXE),
        HOE(HTToolVariant.HOE),
        SWORD(HTToolVariant.SWORD),
        ;

        override val holder: DeferredItem<*> =
            variant.registerItem(REGISTER, HTMaterialType.DEEP_STEEL, RagiumToolTiers.DEEP_STEEL)
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

    enum class Circuits(override val material: HTMaterialType) :
        HTItemHolderLike.Materialized,
        HTTaggedHolder<Item> {
        BASIC(HTMaterialType.BASIC),
        ADVANCED(HTMaterialType.ADVANCED),
        ELITE(HTMaterialType.ELITE),
        ULTIMATE(HTMaterialType.ULTIMATE),
        ;

        override val holder: DeferredItem<*> = register(name.lowercase(), "circuit")
        override val tagKey: TagKey<Item> = material.itemTag(RagiumConst.CIRCUITS)
    }

    //    Event    //

    @SubscribeEvent
    fun registerItemCapabilities(event: RegisterCapabilitiesEvent) {
        registerEnergy(event, DRILL, 160000)

        registerDrums(event)

        LOGGER.info("Registered item capabilities!")
    }

    @JvmStatic
    fun registerEnergy(event: RegisterCapabilitiesEvent, item: ItemLike, capacity: Int) {
        event.registerItem(
            Capabilities.EnergyStorage.ITEM,
            { stack: ItemStack, _: Void? -> HTComponentEnergyStorage(stack, capacity) },
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
                    HTComponentFluidHandler(stack, capacity * modifier)
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
        fun setEnch(item: ItemLike, ench: ResourceKey<Enchantment>, level: Int = 1) {
            event.modify(item) { builder: DataComponentPatch.Builder ->
                builder.set(RagiumDataComponents.INTRINSIC_ENCHANTMENT.get(), HTIntrinsicEnchantment(ench, level))
            }
        }
        setEnch(AzureSteelTools.AXE, Enchantments.SILK_TOUCH)
        setEnch(AzureSteelTools.HOE, Enchantments.SILK_TOUCH)
        setEnch(AzureSteelTools.PICKAXE, Enchantments.SILK_TOUCH)
        setEnch(AzureSteelTools.SHOVEL, Enchantments.SILK_TOUCH)

        setEnch(DeepSteelTools.PICKAXE, Enchantments.FORTUNE, 5)
        setEnch(DeepSteelTools.SWORD, RagiumEnchantments.NOISE_CANCELING, 5)
        setEnch(DeepSteelArmors.CHESTPLATE, RagiumEnchantments.SONIC_PROTECTION)
        // Other
        event.modify(POTION_BUNDLE) { builder: DataComponentPatch.Builder ->
            builder.set(RagiumDataComponents.POTION_BUNDLE.get(), HTPotionBundle.EMPTY)
        }

        event.modify(Ingots.CHOCOLATE) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, RagiumFoods.CHOCOLATE)
        }
        event.modify(Ingots.MEAT) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, Foods.BEEF)
        }
        event.modify(Ingots.COOKED_MEAT) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, Foods.COOKED_BEEF)
        }

        event.modify(RagiumBlocks.Devices.CEU) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.RARITY, Rarity.EPIC)
        }

        LOGGER.info("Modified default item components!")
    }
}
