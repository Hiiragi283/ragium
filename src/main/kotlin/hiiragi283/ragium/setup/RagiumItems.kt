package hiiragi283.ragium.setup

import com.mojang.logging.LogUtils
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.capability.HTEnergyCapabilities
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.core.common.registry.register.HTDeferredItemRegister
import hiiragi283.core.common.storage.component.HTComponentHandler
import hiiragi283.core.common.storage.energy.HTComponentEnergyBattery
import hiiragi283.core.common.storage.fluid.HTComponentFluidTank
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.capability.RagiumCapabilities
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.item.HTFoodCanType
import hiiragi283.ragium.common.item.HTLocationTicketItem
import hiiragi283.ragium.common.item.HTLootTicketItem
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.common.item.HTPotionDropItem
import hiiragi283.ragium.common.item.HTUpgradeItem
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.upgrade.HTComponentUpgradeHandler
import hiiragi283.ragium.common.upgrade.RagiumUpgradeType
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import org.slf4j.Logger
import java.util.function.UnaryOperator

/**
 * @see hiiragi283.core.setup.HCItems
 */
object RagiumItems {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        eventBus.addListener(::registerItemCapabilities)

        REGISTER.register(eventBus)
    }

    //    Materials    //

    @JvmStatic
    val MATERIALS: HTMaterialTable<HTMaterialPrefix, HTSimpleDeferredItem> = buildTable {
        fun register(
            prefix: HTPrefixLike,
            key: HTMaterialKey,
            path: String = prefix.createPath(key),
            operator: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
        ) {
            this[prefix.asMaterialPrefix(), key] = REGISTER.registerSimpleItem(path, operator)
        }

        // Dusts
        arrayOf(
            RagiumMaterialKeys.RAGINITE,
            RagiumMaterialKeys.RAGI_CRYSTAL,
            RagiumMaterialKeys.RAGI_ALLOY,
            RagiumMaterialKeys.ADVANCED_RAGI_ALLOY,
        ).forEach { register(HCMaterialPrefixes.DUST, it) }
        // Gems
        arrayOf(
            RagiumMaterialKeys.RAGI_CRYSTAL,
        ).forEach { register(HCMaterialPrefixes.GEM, it) }
        // Ingots, Nuggets, Gears, Plates, Rods, Wires
        arrayOf(
            HCMaterialPrefixes.INGOT,
            HCMaterialPrefixes.NUGGET,
            HCMaterialPrefixes.GEAR,
            HCMaterialPrefixes.PLATE,
            HCMaterialPrefixes.ROD,
            HCMaterialPrefixes.WIRE,
        ).forEach {
            register(it, RagiumMaterialKeys.RAGI_ALLOY)
            register(it, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)
        }

        // Foods
        register(HCMaterialPrefixes.DUST, RagiumMaterialKeys.MEAT)
        register(HCMaterialPrefixes.INGOT, RagiumMaterialKeys.MEAT) { it.food(Foods.BEEF) }
        register(HCMaterialPrefixes.INGOT, RagiumMaterialKeys.COOKED_MEAT) { it.food(Foods.COOKED_BEEF) }
    }.let(::HTMaterialTable)

    @JvmField
    val RAGI_ALLOY_COMPOUND: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ragi_alloy_compound")

    @JvmField
    val TAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("tar")

    //    Foods    //

    @JvmField
    val EMPTY_CAN: HTSimpleDeferredItem = REGISTER.registerSimpleItem("empty_can")

    @JvmField
    val FOOD_CANS: Map<HTFoodCanType, HTSimpleDeferredItem> = HTFoodCanType.entries.associateWith { canType ->
        val nutrition: Int = when (canType) {
            HTFoodCanType.FISH -> 5
            HTFoodCanType.FRUIT -> 4
            HTFoodCanType.MEAT -> 8
            HTFoodCanType.SOUP -> 6
            HTFoodCanType.VEGETABLE -> 5
        }
        val saturation: Float = when (canType) {
            HTFoodCanType.FISH -> 0.6f
            HTFoodCanType.FRUIT -> 0.3f
            HTFoodCanType.MEAT -> 0.8f
            HTFoodCanType.SOUP -> 0.6f
            HTFoodCanType.VEGETABLE -> 0.6f
        }
        REGISTER.registerSimpleItem("${canType.serializedName}_can") {
            it.food(
                FoodProperties
                    .Builder()
                    .nutrition(nutrition)
                    .saturationModifier(saturation)
                    .fast()
                    .usingConvertsTo(EMPTY_CAN)
                    .build(),
            )
        }
    }

    //    Molds    //

    @JvmField
    val MOLDS: Map<HTMoldType, HTSimpleDeferredItem> = HTMoldType.entries.associateWith { moldType: HTMoldType ->
        REGISTER.registerSimpleItem("${moldType.serializedName}_mold")
    }

    //    Utilities    //

    @JvmField
    val BLANK_DISC: HTSimpleDeferredItem = REGISTER.registerSimpleItem("blank_disc")

    @JvmField
    val LOCATION_TICKET: HTSimpleDeferredItem = REGISTER.registerItem("location_ticket", ::HTLocationTicketItem)

    @JvmField
    val LOOT_TICKET: HTSimpleDeferredItem = REGISTER.registerItem("ragi_ticket", ::HTLootTicketItem)

    @JvmField
    val POTION_DROP: HTSimpleDeferredItem = REGISTER.registerItem("potion_drop", ::HTPotionDropItem)

    //   Upgrades    //

    @JvmField
    val UPGRADES: Map<RagiumUpgradeType, HTSimpleDeferredItem> = RagiumUpgradeType.entries.associateWith { type: RagiumUpgradeType ->
        val color: HTDefaultColor = when (type.group) {
            RagiumUpgradeType.Group.CREATIVE -> HTDefaultColor.RED
            RagiumUpgradeType.Group.GENERATOR -> HTDefaultColor.PURPLE
            RagiumUpgradeType.Group.PROCESSOR -> HTDefaultColor.LIGHT_BLUE
            RagiumUpgradeType.Group.DEVICE -> HTDefaultColor.YELLOW
            RagiumUpgradeType.Group.STORAGE -> HTDefaultColor.GREEN
        }
        REGISTER.registerItemWith("${type.serializedName}_upgrade", color, ::HTUpgradeItem)
    }

    //    Event    //

    @JvmStatic
    private fun registerItemCapabilities(event: RegisterCapabilitiesEvent) {
        // Fluid
        HTFluidCapabilities.registerItem(
            event,
            { context: HTComponentHandler.ContainerContext ->
                val capacity: Int = HTUpgradeHelper.getFluidCapacity(context.attachedTo, RagiumConfig.COMMON.tankCapacity.asInt)
                HTComponentFluidTank.create(capacity, context)
            },
            RagiumBlocks.TANK,
        )

        // Energy
        HTEnergyCapabilities.registerItem(
            event,
            { context: HTComponentHandler.ContainerContext ->
                val capacity: Int = HTUpgradeHelper.getEnergyCapacity(context.attachedTo, RagiumConfig.COMMON.batteryCapacity.asInt)
                HTComponentEnergyBattery.create(context, capacity)
            },
            RagiumBlocks.BATTERY,
        )

        // Upgrade
        for (item: Item in BuiltInRegistries.ITEM) {
            // Data-Based for all items
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

        LOGGER.info("Registered Item Capabilities!")
    }

    //    Extensions    //

    private fun Item.Properties.description(translation: HTTranslation): Item.Properties =
        this.component(RagiumDataComponents.DESCRIPTION, translation)
}
