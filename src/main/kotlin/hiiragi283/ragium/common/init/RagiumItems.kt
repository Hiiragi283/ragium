package hiiragi283.ragium.common.init

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.itemProperty
import hiiragi283.ragium.api.material.HTMaterialItemLike
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.item.*
import hiiragi283.ragium.common.util.HTArmorSets
import hiiragi283.ragium.common.util.HTToolSets
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
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
        Dusts.entries
        Ingots.entries
        RawResources.entries

        Molds.entries

        REGISTER.register(eventBus)

        AZURE_STEEL_ARMORS.init(eventBus)

        RAGI_ALLOY_TOOLS.init(eventBus)
        AZURE_STEEL_TOOLS.init(eventBus)
    }

    @JvmStatic
    private fun register(name: String, properties: Item.Properties = itemProperty()): DeferredItem<Item> =
        REGISTER.registerSimpleItem(name, properties)

    @JvmStatic
    private fun <T : Item> register(
        name: String,
        factory: (Item.Properties) -> T,
        properties: Item.Properties = itemProperty(),
    ): DeferredItem<T> = REGISTER.registerItem(name, factory, properties)

    @JvmStatic
    private fun registerMaterial(
        prefix: HTTagPrefix,
        key: HTMaterialKey,
        path: String = prefix.createPath(key),
    ): DeferredItem<HTMaterialItem> = REGISTER.registerItem(path) { prop: Item.Properties ->
        HTMaterialItem(prefix, key, prop)
    }

    //    Materials    //

    enum class Dusts(override val key: HTMaterialKey, path: String) : HTMaterialItemLike {
        // Vanilla
        WOOD(VanillaMaterials.WOOD, "sawdust"),
        COAL(VanillaMaterials.COAL),
        COPPER(VanillaMaterials.COPPER),
        IRON(VanillaMaterials.IRON),
        LAPIS(VanillaMaterials.LAPIS),
        QUARTZ(VanillaMaterials.QUARTZ),
        GOLD(VanillaMaterials.GOLD),
        DIAMOND(VanillaMaterials.DIAMOND),
        EMERALD(VanillaMaterials.EMERALD),
        AMETHYST(VanillaMaterials.AMETHYST),
        ENDER_PEARL(VanillaMaterials.ENDER_PEARL),
        OBSIDIAN(VanillaMaterials.OBSIDIAN),

        // Ragium
        RAGINITE(RagiumMaterials.RAGINITE),
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        ADVANCED_RAGI_ALLOY(RagiumMaterials.ADVANCED_RAGI_ALLOY),
        RAGI_CRYSTAL(RagiumMaterials.RAGI_CRYSTAL),
        AZURE_STEEL(RagiumMaterials.AZURE_STEEL),
        DEEP_STEEL(RagiumMaterials.DEEP_STEEL),

        // Common
        ASH(CommonMaterials.ASH),
        SALTPETER(CommonMaterials.SALTPETER),
        SULFUR(CommonMaterials.SULFUR),
        ;

        constructor(key: HTMaterialKey) : this(key, HTTagPrefixes.DUST.createPath(key))

        override val prefix: HTTagPrefix = HTTagPrefixes.DUST
        private val holder: DeferredItem<HTMaterialItem> = registerMaterial(prefix, key, path)
        override val id: ResourceLocation = holder.id

        override fun asItem(): Item = holder.asItem()
    }

    enum class Ingots(override val key: HTMaterialKey) : HTMaterialItemLike {
        // Ragium
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        ADVANCED_RAGI_ALLOY(RagiumMaterials.ADVANCED_RAGI_ALLOY),
        AZURE_STEEL(RagiumMaterials.AZURE_STEEL),
        DEEP_STEEL(RagiumMaterials.DEEP_STEEL),

        // Food
        CHEESE(CommonMaterials.CHEESE),
        CHOCOLATE(CommonMaterials.CHOCOLATE),
        ;

        override val prefix: HTTagPrefix = HTTagPrefixes.INGOT
        private val holder: DeferredItem<HTMaterialItem> = registerMaterial(prefix, key)
        override val id: ResourceLocation = holder.id

        override fun asItem(): Item = holder.asItem()
    }

    enum class RawResources(override val prefix: HTTagPrefix, override val key: HTMaterialKey) : HTMaterialItemLike {
        // Raw
        RAGINITE(HTTagPrefixes.RAW_MATERIAL, RagiumMaterials.RAGINITE),

        // Gem
        RAGI_CRYSTAL(HTTagPrefixes.GEM, RagiumMaterials.RAGI_CRYSTAL),
        CRIMSON_CRYSTAL(HTTagPrefixes.GEM, RagiumMaterials.CRIMSON_CRYSTAL),
        WARPED_CRYSTAL(HTTagPrefixes.GEM, RagiumMaterials.WARPED_CRYSTAL),
        ;

        private val holder: DeferredItem<HTMaterialItem> = registerMaterial(prefix, key)
        override val id: ResourceLocation = holder.id

        override fun asItem(): Item = holder.asItem()
    }

    @JvmField
    val RAGI_ALLOY_COMPOUND: DeferredItem<Item> = register("ragi_alloy_compound")

    @JvmField
    val ADVANCED_RAGI_ALLOY_COMPOUND: DeferredItem<Item> = register("advanced_ragi_alloy_compound")

    @JvmField
    val AZURE_STEEL_COMPOUND: DeferredItem<Item> = register("azure_steel_compound")

    @JvmField
    val SAWDUST_PELLET: DeferredItem<Item> = register("sawdust_pellet")

    @JvmField
    val TAR: DeferredItem<Item> = register("tar")

    //    Armors    //

    @JvmField
    val AZURE_STEEL_ARMORS = HTArmorSets(RagiumArmorMaterials.AZURE_STEEL, RagiumMaterials.AZURE_STEEL)

    //    Tools    //

    @JvmField
    val RAGI_ALLOY_TOOLS = HTToolSets(RagiumToolMaterials.RAGI_ALLOY, RagiumMaterials.RAGI_ALLOY)

    @JvmField
    val AZURE_STEEL_TOOLS = HTToolSets(RagiumToolMaterials.STEEL, RagiumMaterials.AZURE_STEEL)

    @JvmField
    val ENDER_BUNDLE: DeferredItem<Item> = register("ender_bundle", itemProperty().stacksTo(1))

    @JvmField
    val ITEM_MAGNET: DeferredItem<HTSimpleMagnetItem> =
        register("item_magnet", ::HTSimpleMagnetItem, itemProperty().stacksTo(1))

    @JvmField
    val TRADER_CATALOG: DeferredItem<Item> = register("trader_catalog", itemProperty().stacksTo(1))

    @JvmField
    val TELEPORT_TICKET: DeferredItem<HTTeleportTicketItem> =
        register("teleport_ticket", ::HTTeleportTicketItem, itemProperty().rarity(Rarity.RARE))

    @JvmField
    val RAGI_TICKET: DeferredItem<HTRagiTicketItem> =
        register("ragi_ticket", ::HTRagiTicketItem, itemProperty().rarity(Rarity.EPIC))

    //    Foods    //

    @JvmStatic
    private fun registerFood(name: String, foodProperties: FoodProperties): DeferredItem<Item> =
        register(name, itemProperty().food(foodProperties))

    // Meat
    @JvmField
    val MINCED_MEAT: DeferredItem<Item> = register("minced_meat")

    @JvmField
    val MEAT_INGOT: DeferredItem<Item> = registerFood("meat_ingot", Foods.BEEF)

    @JvmField
    val COOKED_MEAT_INGOT: DeferredItem<Item> = registerFood("cooked_meat_ingot", Foods.COOKED_BEEF)

    @JvmField
    val CANNED_COOKED_MEAT: DeferredItem<Item> = registerFood("canned_cooked_meat", RagiumFoods.CANNED_COOKED_MEAT)

    @JvmField
    val MEAT_SANDWICH: DeferredItem<Item> = registerFood("meat_sandwich", RagiumFoods.MEAT_SANDWICH)

    // Wheat
    @JvmField
    val FLOUR: DeferredItem<Item> = register("flour")

    @JvmField
    val DOUGH: DeferredItem<Item> = register("dough")

    // Milk
    @JvmField
    val BUTTER: DeferredItem<Item> = registerFood("butter", Foods.APPLE)

    @JvmField
    val ICE_CREAM: DeferredItem<Item> = register("ice_cream", itemProperty().food(RagiumFoods.ICE_CREAM))

    // Honey
    @JvmField
    val BOTTLED_BEE: DeferredItem<HTBottledBeeItem> = register("bottled_bee", ::HTBottledBeeItem)

    // Chocolate
    @JvmField
    val CHOCOLATE_APPLE: DeferredItem<Item> = registerFood("chocolate_apple", Foods.COOKED_CHICKEN)

    @JvmField
    val CHOCOLATE_BREAD: DeferredItem<Item> = registerFood("chocolate_bread", Foods.COOKED_BEEF)

    @JvmField
    val CHOCOLATE_COOKIE: DeferredItem<Item> = registerFood("chocolate_cookie", Foods.COOKIE)

    // Sponge
    @JvmField
    val MELON_PIE: DeferredItem<Item> = registerFood("melon_pie", RagiumFoods.MELON_PIE)

    @JvmField
    val SWEET_BERRIES_CAKE_PIECE: DeferredItem<Item> =
        registerFood("sweet_berries_cake_piece", RagiumFoods.SWEET_BERRIES_CAKE)

    // Other
    @JvmField
    val EXP_BERRIES: DeferredItem<ItemNameBlockItem> = register(
        "exp_berries",
        { prop: Item.Properties -> ItemNameBlockItem(RagiumBlocks.EXP_BERRY_BUSH.get(), prop) },
    )

    @JvmField
    val WARPED_WART: DeferredItem<Item> = registerFood("warped_wart", RagiumFoods.WARPED_WART)

    @JvmField
    val AMBROSIA: DeferredItem<Item> =
        register("ambrosia", itemProperty().food(RagiumFoods.AMBROSIA).rarity(Rarity.EPIC))

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

        val holder: DeferredItem<Item> = register("${name.lowercase()}_mold")

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

    //    Misc    //

    @JvmField
    val SOAP: DeferredItem<Item> = register("soap")

    @JvmField
    val YELLOW_CAKE: DeferredItem<Item> =
        register("yellow_cake")

    @JvmField
    val YELLOW_CAKE_PIECE: DeferredItem<Item> =
        registerFood("yellow_cake_piece", RagiumFoods.YELLOW_CAKE_PIECE)

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
        // Storage Block
        for (block: RagiumBlocks.StorageBlocks in RagiumBlocks.StorageBlocks.entries) {
            event.modify(block) { builder: DataComponentPatch.Builder ->
                builder.set(DataComponents.ITEM_NAME, block.prefix.createText(block.key))
            }
        }

        // Cheese
        event.modify(Ingots.CHOCOLATE) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, Foods.APPLE)
        }
        // Chocolate
        event.modify(Ingots.CHOCOLATE) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, RagiumFoods.CHOCOLATE)
        }

        LOGGER.info("Modified default item components!")
    }
}
