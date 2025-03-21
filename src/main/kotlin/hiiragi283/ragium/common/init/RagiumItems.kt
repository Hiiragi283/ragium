package hiiragi283.ragium.common.init

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.itemProperty
import hiiragi283.ragium.api.material.HTMaterialItemLike
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.common.item.HTAmbrosiaItem
import hiiragi283.ragium.common.item.HTMaterialItem
import hiiragi283.ragium.common.item.HTWarpedWartItem
import hiiragi283.ragium.common.util.HTArmorSets
import hiiragi283.ragium.common.util.HTToolSets
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStackSimple
import net.neoforged.neoforge.registries.DeferredItem
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.neoforge.kotlin.supply
import java.util.function.Supplier

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
        MekResources.entries

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
    private fun registerMaterial(prefix: HTTagPrefix, key: HTMaterialKey): DeferredItem<HTMaterialItem> =
        REGISTER.registerItem(prefix.createPath(key)) { prop: Item.Properties ->
            HTMaterialItem(prefix, key, prop)
        }

    //    Materials    //

    enum class Dusts(override val key: HTMaterialKey) : HTMaterialItemLike {
        // Vanilla
        WOOD(VanillaMaterials.WOOD),
        COAL(VanillaMaterials.COAL),
        COPPER(VanillaMaterials.COPPER),
        IRON(VanillaMaterials.IRON),
        LAPIS(VanillaMaterials.LAPIS),
        QUARTZ(VanillaMaterials.QUARTZ),
        GOLD(VanillaMaterials.GOLD),
        DIAMOND(VanillaMaterials.DIAMOND),
        EMERALD(VanillaMaterials.EMERALD),
        AMETHYST(VanillaMaterials.AMETHYST),
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

        override val prefix: HTTagPrefix = HTTagPrefix.DUST
        private val holder: DeferredItem<HTMaterialItem> = registerMaterial(prefix, key)
        override val id: ResourceLocation = holder.id

        override fun asItem(): Item = holder.asItem()
    }

    enum class Ingots(override val key: HTMaterialKey) : HTMaterialItemLike {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        ADVANCED_RAGI_ALLOY(RagiumMaterials.ADVANCED_RAGI_ALLOY),
        AZURE_STEEL(RagiumMaterials.AZURE_STEEL),
        DEEP_STEEL(RagiumMaterials.DEEP_STEEL),
        ;

        override val prefix: HTTagPrefix = HTTagPrefix.INGOT
        private val holder: DeferredItem<HTMaterialItem> = registerMaterial(prefix, key)
        override val id: ResourceLocation = holder.id

        override fun asItem(): Item = holder.asItem()
    }

    enum class RawResources(override val prefix: HTTagPrefix, override val key: HTMaterialKey) : HTMaterialItemLike {
        // Raw
        RAGINITE(HTTagPrefix.RAW_MATERIAL, RagiumMaterials.RAGINITE),

        // Gem
        RAGI_CRYSTAL(HTTagPrefix.GEM, RagiumMaterials.RAGI_CRYSTAL),
        CRIMSON_CRYSTAL(HTTagPrefix.GEM, RagiumMaterials.CRIMSON_CRYSTAL),
        WARPED_CRYSTAL(HTTagPrefix.GEM, RagiumMaterials.WARPED_CRYSTAL),
        ;

        private val holder: DeferredItem<HTMaterialItem> = registerMaterial(prefix, key)
        override val id: ResourceLocation = holder.id

        override fun asItem(): Item = holder.asItem()
    }

    enum class MekResources(override val prefix: HTTagPrefix) : HTMaterialItemLike {
        DIRTY_DUST(HTTagPrefix.DIRTY_DUST),
        CLUMP(HTTagPrefix.CLUMP),
        SHARD(HTTagPrefix.SHARD),
        CRYSTAL(HTTagPrefix.CRYSTAL),
        ;

        override val key: HTMaterialKey = RagiumMaterials.RAGINITE
        private val holder: DeferredItem<HTMaterialItem> = registerMaterial(prefix, key)
        override val id: ResourceLocation = holder.id

        override fun asItem(): Item = holder.asItem()
    }

    @JvmField
    val RAGI_ALLOY_COMPOUND: DeferredItem<Item> = register("ragi_alloy_compound")

    @JvmField
    val AZURE_STEEL_COMPOUND: DeferredItem<Item> = register("azure_steel_compound")

    //    Armors    //

    @JvmField
    val AZURE_STEEL_ARMORS = HTArmorSets(RagiumArmorMaterials.AZURE_STEEL, RagiumMaterials.AZURE_STEEL)

    //    Tools    //

    @JvmField
    val RAGI_ALLOY_TOOLS = HTToolSets(RagiumToolMaterials.RAGI_ALLOY, RagiumMaterials.RAGI_ALLOY)

    @JvmField
    val AZURE_STEEL_TOOLS = HTToolSets(RagiumToolMaterials.STEEL, RagiumMaterials.AZURE_STEEL)

    //    Fluid Cube    //

    @JvmField
    val WATER_CUBE: DeferredItem<Item> = register("water_cube")

    @JvmField
    val LAVA_CUBE: DeferredItem<Item> = register("lava_cube")

    @JvmField
    val MILK_CUBE: DeferredItem<Item> = register("milk_cube")

    @JvmField
    val FLUID_MAP: Map<DeferredItem<Item>, Supplier<out Fluid>> = mapOf(
        WATER_CUBE to supply(Fluids.WATER),
        LAVA_CUBE to supply(Fluids.LAVA),
        MILK_CUBE to NeoForgeMod.MILK,
    )

    @JvmField
    val FLUID_CUBES: Array<DeferredItem<*>> = FLUID_MAP.keys.toTypedArray()

    //    Foods    //

    @JvmStatic
    private fun registerFood(name: String, foodProperties: FoodProperties): DeferredItem<Item> =
        register(name, itemProperty().food(foodProperties))

    @JvmField
    val SWEET_BERRIES_CAKE_PIECE: DeferredItem<Item> =
        registerFood("sweet_berries_cake_piece", RagiumFoods.SWEET_BERRIES_CAKE)

    @JvmField
    val MELON_PIE: DeferredItem<Item> = registerFood("melon_pie", RagiumFoods.MELON_PIE)

    @JvmField
    val BUTTER: DeferredItem<Item> = registerFood("butter", Foods.APPLE)

    @JvmField
    val CHEESE: DeferredItem<Item> = registerFood("cheese", Foods.APPLE)

    @JvmField
    val FLOUR: DeferredItem<Item> = register("flour")

    @JvmField
    val DOUGH: DeferredItem<Item> = register("dough")

    @JvmField
    val CHOCOLATE: DeferredItem<Item> = registerFood("chocolate", RagiumFoods.CHOCOLATE)

    @JvmField
    val CHOCOLATE_APPLE: DeferredItem<Item> = registerFood("chocolate_apple", Foods.COOKED_CHICKEN)

    @JvmField
    val CHOCOLATE_BREAD: DeferredItem<Item> = registerFood("chocolate_bread", Foods.COOKED_BEEF)

    @JvmField
    val CHOCOLATE_COOKIE: DeferredItem<Item> = registerFood("chocolate_cookie", Foods.COOKIE)

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

    @JvmField
    val WARPED_WART: DeferredItem<HTWarpedWartItem> = register(
        "warped_wart",
        ::HTWarpedWartItem,
        itemProperty().food(RagiumFoods.WARPED_WART),
    )

    @JvmField
    val AMBROSIA: DeferredItem<HTAmbrosiaItem> = register(
        "ambrosia",
        ::HTAmbrosiaItem,
        itemProperty().food(RagiumFoods.AMBROSIA).rarity(Rarity.EPIC),
    )

    @JvmField
    val FOODS: List<DeferredItem<*>> = listOf(
        // cake
        SWEET_BERRIES_CAKE_PIECE,
        MELON_PIE,
        // ingredient
        BUTTER,
        CHEESE,
        FLOUR,
        DOUGH,
        // chocolate
        CHOCOLATE,
        CHOCOLATE_APPLE,
        CHOCOLATE_BREAD,
        CHOCOLATE_COOKIE,
        // meat
        MINCED_MEAT,
        MEAT_INGOT,
        COOKED_MEAT_INGOT,
        CANNED_COOKED_MEAT,
        MEAT_SANDWICH,
        // wart
        WARPED_WART,
        // end-contents
        AMBROSIA,
    )

    //    Machine Parts    //

    @JvmField
    val POLYMER_RESIN: DeferredItem<Item> = register("polymer_resin")

    @JvmField
    val PLASTIC_PLATE: DeferredItem<Item> = register("plastic_plate")

    @JvmField
    val ENGINE: DeferredItem<Item> = register("engine")

    @JvmField
    val LED: DeferredItem<Item> = register("led")

    @JvmField
    val SOLAR_PANEL: DeferredItem<Item> = register("solar_panel")

    @JvmField
    val STONE_BOARD: DeferredItem<Item> = register("stone_board")

    //    Misc    //

    @JvmField
    val SOAP: DeferredItem<Item> = register("soap")

    @JvmField
    val TAR: DeferredItem<Item> = register("tar")

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
            { stack: ItemStack, _: Void? ->
                FluidHandlerItemStackSimple.Consumable(
                    RagiumComponentTypes.FLUID_CONTENT,
                    stack,
                    1000,
                )
            },
            *FLUID_CUBES,
        )

        LOGGER.info("Registered item capabilities!")
    }

    @SubscribeEvent
    fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        fun fluidCube(cube: ItemLike, fluid: Fluid) {
            event.modify(cube) { builder: DataComponentPatch.Builder ->
                builder.set(
                    RagiumComponentTypes.FLUID_CONTENT.get(),
                    SimpleFluidContent.copyOf(FluidStack(fluid, 1000)),
                )
            }
        }

        for ((cube: DeferredItem<Item>, fluid: Supplier<out Fluid>) in FLUID_MAP) {
            fluidCube(cube, fluid.get())
        }

        LOGGER.info("Modified default item components!")
    }
}
