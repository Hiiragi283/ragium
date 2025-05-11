package hiiragi283.ragium.setup

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.component.HTConsumableData
import hiiragi283.ragium.api.item.HTConsumableItem
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.item.*
import hiiragi283.ragium.util.HTArmorSets
import hiiragi283.ragium.util.HTToolSets
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.TagKey
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemNameBlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper
import net.neoforged.neoforge.registries.DeferredItem
import org.slf4j.Logger

@EventBusSubscriber(modid = RagiumAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object RagiumItems {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val REGISTER = HTItemRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        Molds.entries

        REGISTER.register(eventBus)

        AZURE_STEEL_ARMORS.init(eventBus)

        RAGI_ALLOY_TOOLS.init(eventBus)
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

    //    Materials    //

    // Raw Materials

    @JvmField
    val RAW_RAGINITE: DeferredItem<Item> = register("raw_raginite")

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
    val RAGI_CRYSTAL: DeferredItem<Item> = register("ragi_crystal")

    @JvmField
    val CRIMSON_CRYSTAL: DeferredItem<Item> = register("crimson_crystal")

    @JvmField
    val WARPED_CRYSTAL: DeferredItem<Item> = register("warped_crystal")

    // Ingots
    @JvmField
    val RAGI_ALLOY_COMPOUND: DeferredItem<Item> = register("ragi_alloy_compound")

    @JvmField
    val RAGI_ALLOY_INGOT: DeferredItem<Item> = register("ragi_alloy_ingot")

    @JvmField
    val ADVANCED_RAGI_ALLOY_COMPOUND: DeferredItem<Item> = register("advanced_ragi_alloy_compound")

    @JvmField
    val ADVANCED_RAGI_ALLOY_INGOT: DeferredItem<Item> = register("advanced_ragi_alloy_ingot")

    @JvmField
    val AZURE_STEEL_COMPOUND: DeferredItem<Item> = register("azure_steel_compound")

    @JvmField
    val AZURE_STEEL_INGOT: DeferredItem<Item> = register("azure_steel_ingot")

    @JvmField
    val DEEP_STEEL_INGOT: DeferredItem<Item> = register("deep_steel_ingot")

    // Dusts
    @JvmField
    val SAWDUST: DeferredItem<Item> = register("sawdust")

    @JvmField
    val ASH_DUST: DeferredItem<Item> = register("ash_dust")

    @JvmField
    val RAGINITE_DUST: DeferredItem<Item> = register("raginite_dust")

    @JvmField
    val OBSIDIAN_DUST: DeferredItem<Item> = register("obsidian_dust")

    @JvmField
    val SALTPETER_DUST: DeferredItem<Item> = register("saltpeter_dust")

    @JvmField
    val SULFUR_DUST: DeferredItem<Item> = register("sulfur_dust")

    // Other
    @JvmField
    val INACTIVE_RAGIUM_ESSENCE: DeferredItem<Item> = register("inactive_ragium_essence")

    @JvmField
    val RAGIUM_ESSENCE: DeferredItem<Item> = register("ragium_essence")

    @JvmField
    val CHIPPED_RAGIUM_ESSENCE: DeferredItem<Item> = register("chipped_ragium_essence")

    @JvmField
    val AQUATIC_RAGIUM_ESSENCE: DeferredItem<Item> = register("aquatic_ragium_essence")

    @JvmField
    val ELDER_HEART: DeferredItem<Item> = register("elder_heart")

    //    Armors    //

    @JvmField
    val AZURE_STEEL_ARMORS = HTArmorSets(RagiumArmorMaterials.AZURE_STEEL, "azure_steel", RagiumItemTags.INGOTS_AZURE_STEEL)

    //    Tools    //

    @JvmField
    val RAGI_ALLOY_TOOLS = HTToolSets(RagiumToolMaterials.RAGI_ALLOY, "ragi_alloy", RagiumItemTags.INGOTS_RAGI_ALLOY)

    @JvmField
    val AZURE_STEEL_TOOLS = HTToolSets(RagiumToolMaterials.STEEL, "azure_steel", RagiumItemTags.INGOTS_AZURE_STEEL)

    @JvmField
    val ENDER_BUNDLE: DeferredItem<Item> = register("ender_bundle", Item.Properties().stacksTo(1))

    @JvmField
    val ITEM_MAGNET: DeferredItem<HTSimpleMagnetItem> = register("item_magnet", ::HTSimpleMagnetItem)

    @JvmField
    val EXP_MAGNET: DeferredItem<HTExpMagnetItem> = register("exp_magnet", ::HTExpMagnetItem)

    @JvmField
    val TRADER_CATALOG: DeferredItem<Item> = register("trader_catalog", Item.Properties().stacksTo(1))

    @JvmField
    val TELEPORT_TICKET: DeferredItem<HTTeleportTicketItem> =
        register("teleport_ticket", ::HTTeleportTicketItem, Item.Properties().rarity(Rarity.RARE))

    @JvmField
    val RAGI_LANTERN: DeferredItem<HTDynamicLanternItem> =
        register("ragi_lantern", ::HTDynamicLanternItem)

    @JvmField
    val RAGI_TICKET: DeferredItem<HTRagiTicketItem> =
        register("ragi_ticket", ::HTRagiTicketItem, Item.Properties().rarity(Rarity.EPIC))

    //    Foods    //

    @JvmStatic
    private fun registerFood(
        name: String,
        foodProperties: FoodProperties,
        properties: Item.Properties = Item.Properties(),
    ): DeferredItem<HTConsumableItem> = register(name, ::HTConsumableItem, properties.food(foodProperties))

    @JvmField
    val ICE_CREAM: DeferredItem<HTConsumableItem> = registerFood("ice_cream", RagiumFoods.ICE_CREAM)

    @JvmField
    val ICE_CREAM_SODA: DeferredItem<HTConsumableItem> = register("ice_cream_soda", factory = ::HTConsumableItem)

    @JvmField
    val CHEESE_INGOT: DeferredItem<HTConsumableItem> = registerFood("cheese_ingot", Foods.APPLE)

    @JvmField
    val CHOCOLATE_INGOT: DeferredItem<HTConsumableItem> = registerFood("chocolate_ingot", RagiumFoods.CHOCOLATE)

    // Meat
    @JvmField
    val MINCED_MEAT: DeferredItem<Item> = register("minced_meat")

    @JvmField
    val MEAT_INGOT: DeferredItem<HTConsumableItem> = registerFood("meat_ingot", Foods.BEEF)

    @JvmField
    val COOKED_MEAT_INGOT: DeferredItem<HTConsumableItem> = registerFood("cooked_meat_ingot", Foods.COOKED_BEEF)

    @JvmField
    val CANNED_COOKED_MEAT: DeferredItem<HTConsumableItem> = registerFood("canned_cooked_meat", RagiumFoods.CANNED_COOKED_MEAT)

    // Sponge
    @JvmField
    val MELON_PIE: DeferredItem<HTConsumableItem> = registerFood("melon_pie", RagiumFoods.MELON_PIE)

    @JvmField
    val SWEET_BERRIES_CAKE_PIECE: DeferredItem<HTConsumableItem> = registerFood("sweet_berries_cake_piece", RagiumFoods.SWEET_BERRIES_CAKE)

    // Cherry
    @JvmField
    val RAGI_CHERRY: DeferredItem<HTConsumableItem> = registerFood("ragi_cherry", RagiumFoods.RAGI_CHERRY)

    @JvmField
    val RAGI_CHERRY_JAM: DeferredItem<HTConsumableItem> = registerFood("ragi_cherry_jam", RagiumFoods.RAGI_CHERRY_JAM)

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
    val WARPED_WART: DeferredItem<HTConsumableItem> = registerFood("warped_wart", RagiumFoods.WARPED_WART)

    @JvmField
    val AMBROSIA: DeferredItem<HTConsumableItem> = registerFood("ambrosia", RagiumFoods.AMBROSIA, Item.Properties().rarity(Rarity.EPIC))

    //    Molds    //

    enum class Molds(val tagKey: TagKey<Item>) : ItemLike {
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
    }

    //    Machine Parts    //

    @JvmField
    val ENGINE: DeferredItem<Item> = register("engine")

    @JvmField
    val LED: DeferredItem<Item> = register("led")

    @JvmField
    val SOLAR_PANEL: DeferredItem<Item> = register("solar_panel")

    @JvmField
    val STONE_BOARD: DeferredItem<Item> = register("stone_board")

    @JvmField
    val POLYMER_RESIN: DeferredItem<Item> = register("polymer_resin")

    @JvmField
    val PLASTIC_PLATE: DeferredItem<Item> = register("plastic_plate")

    @JvmField
    val BASIC_CIRCUIT: DeferredItem<Item> = register("basic_circuit")

    @JvmField
    val ADVANCED_CIRCUIT: DeferredItem<Item> = register("advanced_circuit")

    @JvmField
    val CRYSTAL_PROCESSOR: DeferredItem<Item> = register("crystal_processor")

    //    Misc    //

    @JvmField
    val SOAP: DeferredItem<Item> = register("soap")

    //    Event    //

    @SubscribeEvent
    fun registerItemCapabilities(event: RegisterCapabilitiesEvent) {
        event.registerItem(
            Capabilities.FluidHandler.ITEM,
            { stack: ItemStack, _: Void? -> FluidBucketWrapper(stack) },
            *RagiumFluidContents.REGISTER.itemEntries.toTypedArray(),
        )

        LOGGER.info("Registered item capabilities!")
    }

    @SubscribeEvent
    fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        event.modify(ICE_CREAM_SODA) { builder: DataComponentPatch.Builder ->
            builder.set(RagiumComponentTypes.CONSUMABLE.get(), HTConsumableData(sound = SoundEvents.GENERIC_DRINK))
        }
        event.modify(RAGI_CHERRY_JAM) { builder: DataComponentPatch.Builder ->
            builder.set(RagiumComponentTypes.CONSUMABLE.get(), HTConsumableData(sound = SoundEvents.HONEY_DRINK))
        }
        // Ingot
        /*event.modify(Ingots.CHEESE) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, Foods.APPLE)
        }
        event.modify(Ingots.CHOCOLATE) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, RagiumFoods.CHOCOLATE)
        }*/

        // Ragium Essence
        event.modify(RAGIUM_ESSENCE) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
            builder.set(DataComponents.RARITY, Rarity.EPIC)
        }
        event.modify(CHIPPED_RAGIUM_ESSENCE) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
            builder.set(DataComponents.RARITY, Rarity.EPIC)
        }

        LOGGER.info("Modified default item components!")
    }
}
