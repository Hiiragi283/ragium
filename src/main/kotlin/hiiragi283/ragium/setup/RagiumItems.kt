package hiiragi283.ragium.setup

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.extension.getEnchantmentLevel
import hiiragi283.ragium.api.item.HTConsumableItem
import hiiragi283.ragium.api.item.HTForgeHammerItem
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.item.HTAzureSteelTemplateItem
import hiiragi283.ragium.common.item.HTCaptureEggItem
import hiiragi283.ragium.common.item.HTDynamicLanternItem
import hiiragi283.ragium.common.item.HTExpMagnetItem
import hiiragi283.ragium.common.item.HTSimpleMagnetItem
import hiiragi283.ragium.common.item.HTTeleportTicketItem
import hiiragi283.ragium.common.item.HTWarpedWartItem
import hiiragi283.ragium.common.util.HTToolSets
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemNameBlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
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
        REGISTER.register(eventBus)

        AZURE_STEEL_TOOLS.init(eventBus)
    }

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
    val RAGI_TICKET_FAKE: DeferredItem<Item> = register("ragi_ticket_fake")

    @JvmField
    val RAGI_TICKET: DeferredItem<Item> = register("ragi_ticket")

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
    val AZURE_SHARD: DeferredItem<Item> = register("azure_shard")

    @JvmField
    val COMPRESSED_SAWDUST: DeferredItem<Item> = register("compressed_sawdust")

    @JvmField
    val TAR: DeferredItem<Item> = register("tar")

    // Gems
    @JvmField
    val RAGI_CRYSTAL: DeferredItem<Item> = register(RagiumConstantValues.RAGI_CRYSTAL)

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
    val RAGI_ALLOY_COMPOUND: DeferredItem<Item> = register("${RagiumConstantValues.RAGI_ALLOY}_compound")

    @JvmField
    val RAGI_ALLOY_INGOT: DeferredItem<Item> = register("${RagiumConstantValues.RAGI_ALLOY}_ingot")

    @JvmField
    val ADVANCED_RAGI_ALLOY_COMPOUND: DeferredItem<Item> = register("${RagiumConstantValues.ADVANCED_RAGI_ALLOY}_compound")

    @JvmField
    val ADVANCED_RAGI_ALLOY_INGOT: DeferredItem<Item> = register("${RagiumConstantValues.ADVANCED_RAGI_ALLOY}_ingot")

    @JvmField
    val AZURE_STEEL_COMPOUND: DeferredItem<Item> = register("${RagiumConstantValues.AZURE_STEEL}_compound")

    @JvmField
    val AZURE_STEEL_INGOT: DeferredItem<Item> = register("${RagiumConstantValues.AZURE_STEEL}_ingot")

    @JvmField
    val DEEP_STEEL_INGOT: DeferredItem<Item> = register("${RagiumConstantValues.DEEP_STEEL}_ingot")

    // Nuggets
    @JvmField
    val RAGI_ALLOY_NUGGET: DeferredItem<Item> = register("${RagiumConstantValues.RAGI_ALLOY}_nugget")

    @JvmField
    val ADVANCED_RAGI_ALLOY_NUGGET: DeferredItem<Item> = register("${RagiumConstantValues.ADVANCED_RAGI_ALLOY}_nugget")

    @JvmField
    val AZURE_STEEL_NUGGET: DeferredItem<Item> = register("${RagiumConstantValues.AZURE_STEEL}_nugget")

    // Dusts
    @JvmField
    val SAWDUST: DeferredItem<Item> = register("sawdust")

    @JvmField
    val ASH_DUST: DeferredItem<Item> = register("ash_dust")

    @JvmField
    val RAGINITE_DUST: DeferredItem<Item> = register("${RagiumConstantValues.RAGINITE}_dust")

    @JvmField
    val OBSIDIAN_DUST: DeferredItem<Item> = register("obsidian_dust")

    @JvmField
    val CINNABAR_DUST: DeferredItem<Item> = register("cinnabar_dust")

    @JvmField
    val SALTPETER_DUST: DeferredItem<Item> = register("saltpeter_dust")

    @JvmField
    val SULFUR_DUST: DeferredItem<Item> = register("sulfur_dust")

    // Other
    @JvmField
    val ELDER_HEART: DeferredItem<Item> = register("elder_heart")

    //    Armors    //

    @JvmStatic
    private fun registerArmor(
        name: String,
        armorType: ArmorItem.Type,
        material: Holder<ArmorMaterial>,
        multiplier: Int,
    ) = register(
        "${name}_${armorType.serializedName}",
        { properties: Item.Properties -> ArmorItem(material, armorType, properties) },
        Item.Properties().durability(armorType.getDurability(multiplier)),
    )

    @JvmField
    val AZURE_STEEL_HELMET: DeferredItem<ArmorItem> =
        registerArmor(RagiumConstantValues.AZURE_STEEL, ArmorItem.Type.HELMET, RagiumArmorMaterials.AZURE_STEEL, 20)

    @JvmField
    val AZURE_STEEL_CHESTPLATE: DeferredItem<ArmorItem> =
        registerArmor(RagiumConstantValues.AZURE_STEEL, ArmorItem.Type.CHESTPLATE, RagiumArmorMaterials.AZURE_STEEL, 20)

    @JvmField
    val AZURE_STEEL_LEGGINGS: DeferredItem<ArmorItem> =
        registerArmor(RagiumConstantValues.AZURE_STEEL, ArmorItem.Type.LEGGINGS, RagiumArmorMaterials.AZURE_STEEL, 20)

    @JvmField
    val AZURE_STEEL_BOOTS: DeferredItem<ArmorItem> =
        registerArmor(RagiumConstantValues.AZURE_STEEL, ArmorItem.Type.BOOTS, RagiumArmorMaterials.AZURE_STEEL, 20)

    @JvmField
    val AZURE_ARMORS: List<DeferredItem<ArmorItem>> = listOf(
        AZURE_STEEL_HELMET,
        AZURE_STEEL_CHESTPLATE,
        AZURE_STEEL_LEGGINGS,
        AZURE_STEEL_BOOTS,
    )

    //    Tools    //

    @JvmField
    val TRADER_CATALOG: DeferredItem<Item> = register("trader_catalog", Item.Properties().stacksTo(1))

    @JvmField
    val RAGI_ALLOY_HAMMER: DeferredItem<HTForgeHammerItem> = register(
        "${RagiumConstantValues.RAGI_ALLOY}_hammer",
        { prop: Item.Properties -> HTForgeHammerItem(RagiumToolTiers.RAGI_ALLOY, prop) },
    )

    @JvmField
    val ITEM_MAGNET: DeferredItem<HTSimpleMagnetItem> = register("item_magnet", ::HTSimpleMagnetItem)

    @JvmField
    val RAGI_LANTERN: DeferredItem<HTDynamicLanternItem> = register("ragi_lantern", ::HTDynamicLanternItem)

    @JvmField
    val EXP_MAGNET: DeferredItem<HTExpMagnetItem> = register("exp_magnet", ::HTExpMagnetItem)

    @JvmField
    val AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE: DeferredItem<HTAzureSteelTemplateItem> =
        register("${RagiumConstantValues.AZURE_STEEL}_upgrade_smithing_template", { _: Item.Properties -> HTAzureSteelTemplateItem() })

    @JvmField
    val AZURE_STEEL_TOOLS = HTToolSets(RagiumToolTiers.AZURE_STEEL, RagiumConstantValues.AZURE_STEEL, RagiumItemTags.INGOTS_AZURE_STEEL)

    @JvmField
    val ENDER_BUNDLE: DeferredItem<Item> = register("ender_bundle", Item.Properties().stacksTo(1))

    @JvmField
    val ELDRITCH_EGG: DeferredItem<HTCaptureEggItem> = register("eldritch_egg", ::HTCaptureEggItem)

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
    val LED: DeferredItem<Item> = register("led")

    @JvmField
    val SOLAR_PANEL: DeferredItem<Item> = register("solar_panel")

    @JvmField
    val STONE_BOARD: DeferredItem<Item> = register("stone_board")

    @JvmField
    val POLYMER_RESIN: DeferredItem<Item> = register("polymer_resin")

    // val PLASTIC_PLATE: DeferredItem<Item> = register("plastic_plate")

    @JvmField
    val BASIC_CIRCUIT: DeferredItem<Item> = register("basic_circuit")

    @JvmField
    val ADVANCED_CIRCUIT: DeferredItem<Item> = register("advanced_circuit")

    @JvmField
    val CRYSTAL_PROCESSOR: DeferredItem<Item> = register("crystal_processor")

    //    Event    //

    @SubscribeEvent
    fun registerItemCapabilities(event: RegisterCapabilitiesEvent) {
        event.registerItem(
            Capabilities.FluidHandler.ITEM,
            { stack: ItemStack, _: Void? -> FluidBucketWrapper(stack) },
            *RagiumFluidContents.REGISTER.itemEntries.toTypedArray(),
        )

        fun createDrumHandler(capacity: Int): (ItemStack, Void?) -> FluidHandlerItemStack? = { stack: ItemStack, _: Void? ->
            val modifier: Int = stack.getEnchantmentLevel(RagiumEnchantments.CAPACITY) + 1
            FluidHandlerItemStack(RagiumComponentTypes.FLUID_CONTENT, stack, capacity * modifier)
        }

        event.registerItem(
            Capabilities.FluidHandler.ITEM,
            createDrumHandler(RagiumConfig.COMMON.smallDrumCapacity.get()),
            RagiumBlocks.SMALL_DRUM,
        )
        event.registerItem(
            Capabilities.FluidHandler.ITEM,
            createDrumHandler(RagiumConfig.COMMON.mediumDrumCapacity.get()),
            RagiumBlocks.MEDIUM_DRUM,
        )
        event.registerItem(
            Capabilities.FluidHandler.ITEM,
            createDrumHandler(RagiumConfig.COMMON.largeDrumCapacity.get()),
            RagiumBlocks.LARGE_DRUM,
        )
        event.registerItem(
            Capabilities.FluidHandler.ITEM,
            createDrumHandler(RagiumConfig.COMMON.hugeDrumCapacity.get()),
            RagiumBlocks.HUGE_DRUM,
        )

        LOGGER.info("Registered item capabilities!")
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

        setColor(RAGI_TICKET_FAKE, ChatFormatting.RED)
        setColor(RAGI_TICKET, ChatFormatting.RED)
        setColor(AZURE_TICKET, ChatFormatting.BLUE)
        setColor(BLOODY_TICKET, ChatFormatting.DARK_RED)
        setColor(TELEPORT_TICKET, ChatFormatting.DARK_AQUA)
        setColor(ELDRITCH_TICKET, ChatFormatting.LIGHT_PURPLE)

        setColor(DAYBREAK_TICKET, ChatFormatting.GOLD)
        setColor(ETERNAL_TICKET, ChatFormatting.YELLOW)
        // Creative Item
        event.modify(RagiumBlocks.CEU) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.RARITY, Rarity.EPIC)
        }

        LOGGER.info("Modified default item components!")
    }
}
