package hiiragi283.ragium.setup

import hiiragi283.core.api.registry.HTDeferredItemRegister
import hiiragi283.core.api.registry.HTSimpleDeferredItem
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.capability.HTEnergyCapabilities
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.common.item.HTCreativeItem
import hiiragi283.core.common.storage.energy.HTBasicItemEnergyBattery
import hiiragi283.core.common.storage.fluid.HTBasicItemFluidTank
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.item.HTBatteryItem
import hiiragi283.ragium.common.item.HTElectricIgniterItem
import hiiragi283.ragium.common.item.HTLocationTicketItem
import hiiragi283.ragium.common.item.HTLootTicketItem
import hiiragi283.ragium.common.storge.energy.HTInfiniteEnergyBattery
import hiiragi283.ragium.common.storge.fluid.HTInfiniteItemFluidTank
import hiiragi283.ragium.common.storge.fluid.HTVoidItemFluidTank
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.food.Foods
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import java.util.function.IntSupplier

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
    val RAGI_ALLOY_COMPOUND: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ragi_alloy_compound")

    @JvmField
    val CRYO_CHARGE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("cryo_charge")

    // Nether
    @JvmField
    val CRUDE_SILICON: HTSimpleDeferredItem = REGISTER.registerSimpleItem("crude_silicon")

    @JvmField
    val GLYCEROL_DROP: HTSimpleDeferredItem = REGISTER.registerSimpleItem("glycerol_drop")

    @JvmField
    val NITROGLYCERIN: HTSimpleDeferredItem = REGISTER.registerSimpleItem("nitroglycerin")

    @JvmField
    val NITROCELLULOSE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("nitrocellulose")

    @JvmField
    val SMOKELESS_POWDER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("smokeless_powder")

    //    Parts    //

    // Basic

    // Advanced
    @JvmField
    val MERCURY_BOTTLE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("mercury_bottle")

    @JvmField
    val THERMOMETER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("thermometer") { it.rarity(Rarity.UNCOMMON) }

    // Elite
    @JvmField
    val SILICON_WAFER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("silicon_wafer")

    @JvmField
    val CIRCUIT_CHIP: HTSimpleDeferredItem = REGISTER.registerSimpleItem("circuit_chip")

    @JvmField
    val CIRCUIT_BOARD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("circuit_board")

    @JvmField
    val ELECTRIC_CIRCUIT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("electric_circuit") { it.rarity(Rarity.RARE) }

    // Ultimate
    @JvmField
    val ARTIFICIAL_ARTIFACT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("artificial_artifact") { it.rarity(Rarity.EPIC) }

    //    Foods    //

    @JvmField
    val MINCED_MEAT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("meat_dust")

    @JvmField
    val MEAT_INGOT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("meat_ingot") { it.food(Foods.BEEF) }

    @JvmField
    val COOKED_MEAT_INGOT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("cooked_meat_ingot") { it.food(Foods.COOKED_BEEF) }

    @JvmField
    val CANNED_COOKED_MEAT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("canned_cooked_meat") { it.food(RagiumFoods.CANNED_COOKED_MEAT) }

    //    Utilities    //

    // Basic
    @JvmField
    val BLANK_DISC: HTSimpleDeferredItem = REGISTER.registerSimpleItem("blank_disc")

    @JvmField
    val ELECTRIC_IGNITER: HTSimpleDeferredItem = REGISTER.registerItem("electric_igniter", ::HTElectricIgniterItem)

    // Advanced
    @JvmField
    val LOCATION_TICKET: HTSimpleDeferredItem = REGISTER.registerItem("location_ticket", ::HTLocationTicketItem)

    // Elite
    @JvmField
    val CRYSTAL_BATTERY: HTSimpleDeferredItem = REGISTER.registerItem("crystal_battery", ::HTBatteryItem)

    @JvmField
    val DYNAMITE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("dynamite")

    // Ultimate

    //    End Game    //

    @JvmField
    val RAGI_MATTER: HTSimpleDeferredItem = REGISTER.registerItem("ragi_matter", ::HTCreativeItem)

    @JvmField
    val RAGI_TICKET: HTSimpleDeferredItem = REGISTER.registerItem("ragi_ticket", ::HTLootTicketItem)

    //    Event    //

    @JvmStatic
    private fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        fun <T : Any> modify(item: ItemLike, type: DataComponentType<T>, value: T) {
            event.modify(item) { builder: DataComponentPatch.Builder -> builder.set(type, value) }
        }
    }

    @JvmStatic
    private fun registerItemCapabilities(event: RegisterCapabilitiesEvent) {
        // Fluid
        HTFluidCapabilities.registerItemTank(
            event,
            { container: ItemStack -> HTBasicItemFluidTank.create(container, getCapacity(container, RagiumConfig.COMMON.tankCapacity)) },
            RagiumBlocks.TANK,
        )
        HTFluidCapabilities.registerItemTank(event, ::HTVoidItemFluidTank, RagiumBlocks.VOID_TANK)
        HTFluidCapabilities.registerItemTank(event, ::HTInfiniteItemFluidTank, RagiumBlocks.CREATIVE_TANK)

        // Energy
        HTEnergyCapabilities.registerItemEnergy(
            event,
            { container: ItemStack -> HTBasicItemEnergyBattery.create(container, getCapacity(container, RagiumConfig.COMMON.batteryCapacity)) },
            RagiumBlocks.BATTERY,
        )
        HTEnergyCapabilities.registerItemEnergy(event, { HTInfiniteEnergyBattery }, RagiumBlocks.CREATIVE_BATTERY)

        HTEnergyCapabilities.registerItemEnergy(
            event,
            { container: ItemStack -> HTBasicItemEnergyBattery.create(container, RagiumConfig.COMMON.electricIgniter.getCapacity()) },
            ELECTRIC_IGNITER,
        )
        HTEnergyCapabilities.registerItemEnergy(
            event,
            { container: ItemStack -> HTBasicItemEnergyBattery.create(container, 8000) },
            CRYSTAL_BATTERY,
        )
    }

    @JvmStatic
    private fun getCapacity(context: ItemStack, base: IntSupplier): Int = RagiumDataComponents.getCapacity(base, context.getOrDefault(RagiumDataComponents.CAPACITY_SCALE, 1))

    //    Extensions    //

    private fun Item.Properties.description(translation: HTTranslation): Item.Properties = this.component(HCDataComponents.DESCRIPTION, translation)
}
