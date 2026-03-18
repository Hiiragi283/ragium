package hiiragi283.ragium.setup

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.capability.HTEnergyCapabilities
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.common.registry.register.HTDeferredItemRegister
import hiiragi283.core.common.storage.component.HTComponentHandler
import hiiragi283.core.common.storage.energy.HTComponentEnergyBattery
import hiiragi283.core.common.storage.fluid.HTComponentFluidTank
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.capability.RagiumCapabilities
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.upgrade.HTUpgradeHandler
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.item.HTBlueprintItem
import hiiragi283.ragium.common.item.HTFoodCanType
import hiiragi283.ragium.common.item.HTLocationTicketItem
import hiiragi283.ragium.common.item.HTLootTicketItem
import hiiragi283.ragium.common.item.HTUpgradeItem
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.upgrade.HTComponentUpgradeHandler
import hiiragi283.ragium.common.upgrade.RagiumUpgradeType
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent

/**
 * @see hiiragi283.core.setup.HCItems
 */
object RagiumItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)

        eventBus.addListener(::modifyComponents)
        eventBus.addListener(::registerItemCapabilities)
    }

    //    Materials    //

    // Overworld
    @JvmField
    val RAGI_ALLOY_COMPOUND: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("ragi_alloy_compound")

    @JvmField
    val CARBON_COMPOUND: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("carbon_compound")

    @JvmField
    val CRYO_CHARGE: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("cryo_charge")

    // Nether
    @JvmField
    val CRUDE_SILICON: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("crude_silicon")

    @JvmField
    val GLYCEROL_DROP: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("glycerol_drop")

    @JvmField
    val NITROGLYCERIN: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("nitroglycerin")

    @JvmField
    val NITROCELLULOSE: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("nitrocellulose")

    @JvmField
    val SMOKELESS_POWDER: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("smokeless_powder")

    //    Parts    //

    // Basic

    // Advanced
    @JvmField
    val MERCURY_BOTTLE: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("mercury_bottle")

    @JvmField
    val THERMOMETER: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("thermometer")

    // Elite
    @JvmField
    val CIRCUIT_BOARD: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("circuit_board")

    @JvmField
    val PLATED_CIRCUIT_BOARD: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("plated_circuit_board")

    @JvmField
    val PRINTED_CIRCUIT_BOARD: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("printed_circuit_board")

    @JvmField
    val ELECTRIC_CIRCUIT: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("electric_circuit")

    // Ultimate
    @JvmField
    val ARTIFICIAL_ARTIFACT: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("artificial_artifact")

    //    Foods    //

    @JvmField
    val EMPTY_CAN: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("empty_can")

    @JvmField
    val FOOD_CANS: Map<HTFoodCanType, HTSimpleItemHolderLike> = HTFoodCanType.entries.associateWith { canType ->
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

    //    Utilities    //

    @JvmField
    val BLANK_DISC: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("blank_disc")

    @JvmField
    val BLUEPRINT: HTSimpleItemHolderLike = REGISTER.registerItem("blueprint", ::HTBlueprintItem) {
        it.stacksTo(1).component(RagiumDataComponents.BLUEPRINT_NUMBER, 0).description(RagiumTranslation.BLUEPRINT)
    }

    @JvmField
    val LOCATION_TICKET: HTSimpleItemHolderLike = REGISTER.registerItem("location_ticket", ::HTLocationTicketItem)

    @JvmField
    val LOOT_TICKET: HTSimpleItemHolderLike = REGISTER.registerItem("ragi_ticket", ::HTLootTicketItem)

    //   Upgrades    //

    @JvmField
    val UPGRADES: Map<RagiumUpgradeType, HTSimpleItemHolderLike> = RagiumUpgradeType.entries.associateWith { type: RagiumUpgradeType ->
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
    private fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        fun <T : Any> modify(item: ItemLike, type: DataComponentType<T>, value: T) {
            event.modify(item) { builder: DataComponentPatch.Builder -> builder.set(type, value) }
        }

        with(HiiragiCoreAccess.INSTANCE.registeredContents.items) {
            modify(getOrThrow(CommonParts.INGOT, RagiumMaterialKeys.MEAT), DataComponents.FOOD, Foods.BEEF)
            modify(getOrThrow(CommonParts.INGOT, RagiumMaterialKeys.COOKED_MEAT), DataComponents.FOOD, Foods.COOKED_BEEF)
        }
    }

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
            RagiumBlocks.CREATIVE_TANK,
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

        event.registerItem(
            RagiumCapabilities.UPGRADABLE_ITEM,
            { _, _ ->
                object : HTUpgradeHandler {
                    override fun getUpgrades(): List<HTItemResourceType> = listOf()

                    override fun isValidUpgrade(upgrade: HTItemResourceType): Boolean = false

                    override fun isCreative(): Boolean = true
                }
            },
            RagiumBlocks.CREATIVE_BATTERY,
            RagiumBlocks.CREATIVE_CRATE,
            RagiumBlocks.CREATIVE_TANK,
        )
    }

    //    Extensions    //

    private fun Item.Properties.description(translation: HTTranslation): Item.Properties =
        this.component(HCDataComponents.DESCRIPTION, translation)
}
