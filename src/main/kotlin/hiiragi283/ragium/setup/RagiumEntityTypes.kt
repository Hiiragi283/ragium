package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.capability.HTEnergyCapabilities
import hiiragi283.ragium.api.capability.HTFluidCapabilities
import hiiragi283.ragium.api.capability.HTItemCapabilities
import hiiragi283.ragium.api.registry.impl.HTDeferredEntityType
import hiiragi283.ragium.api.registry.impl.HTDeferredEntityTypeRegister
import hiiragi283.ragium.api.storage.HTHandlerProvider
import hiiragi283.ragium.common.HTChargeType
import hiiragi283.ragium.common.entity.HTThrownCaptureEgg
import hiiragi283.ragium.common.entity.charge.HTAbstractCharge
import hiiragi283.ragium.common.entity.charge.HTBlastCharge
import hiiragi283.ragium.common.entity.charge.HTConfusionCharge
import hiiragi283.ragium.common.entity.charge.HTFishingCharge
import hiiragi283.ragium.common.entity.charge.HTNeutralCharge
import hiiragi283.ragium.common.entity.charge.HTStrikeCharge
import hiiragi283.ragium.common.entity.charge.HTTeleportCharge
import hiiragi283.ragium.common.entity.vehicle.HTTankMinecart
import net.minecraft.SharedConstants
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.Level
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent

object RagiumEntityTypes {
    @JvmField
    val REGISTER = HTDeferredEntityTypeRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        listOf(
            "small",
            "medium",
            "large",
            "huge",
        ).forEach { REGISTER.addAlias("${it}_drum_minecart", "tank_minecart") }

        REGISTER.register(eventBus)

        eventBus.addListener(::registerEntityCapabilities)
    }

    @JvmField
    val ELDRITCH_EGG: HTDeferredEntityType<HTThrownCaptureEgg> = registerThrowable("eldritch_egg", ::HTThrownCaptureEgg)

    @JvmField
    val CHARGES: Map<HTChargeType, HTDeferredEntityType<out HTAbstractCharge>> =
        HTChargeType.entries.associateWith { chargeType: HTChargeType ->
            val factory: (EntityType<out HTAbstractCharge>, Level) -> HTAbstractCharge = when (chargeType) {
                HTChargeType.BLAST -> ::HTBlastCharge
                HTChargeType.STRIKE -> ::HTStrikeCharge
                HTChargeType.NEUTRAL -> ::HTNeutralCharge
                HTChargeType.FISHING -> ::HTFishingCharge
                HTChargeType.TELEPORT -> ::HTTeleportCharge
                HTChargeType.CONFUSION -> ::HTConfusionCharge
            }
            registerThrowable("${chargeType.serializedName}_charge", factory)
        }

    @JvmStatic
    private fun <T : Entity> registerThrowable(name: String, factory: EntityType.EntityFactory<T>): HTDeferredEntityType<T> =
        REGISTER.registerType(name, factory, MobCategory.MISC) { builder: EntityType.Builder<T> ->
            builder
                .sized(0.25f, 0.25f)
                .clientTrackingRange(4)
                .updateInterval(10)
        }

    //    TNT    //

    @JvmStatic
    private fun <T : Entity> registerTnt(name: String, factory: EntityType.EntityFactory<T>): HTDeferredEntityType<T> =
        REGISTER.registerType(name, factory, MobCategory.MISC) { builder: EntityType.Builder<T> ->
            builder
                .fireImmune()
                .sized(0.98f, 0.98f)
                .eyeHeight(0.15f)
                .clientTrackingRange(10)
                .updateInterval(SharedConstants.TICKS_PER_SECOND / 2)
        }

    //    Minecart    //

    @JvmField
    val TANK_MINECART: HTDeferredEntityType<HTTankMinecart> = REGISTER.registerType(
        "tank_minecart",
        ::HTTankMinecart,
        MobCategory.MISC,
    ) { builder: EntityType.Builder<HTTankMinecart> -> builder.sized(0.98f, 0.7f) }

    //    Event    //

    // Capabilities
    @JvmStatic
    private fun registerEntityCapabilities(event: RegisterCapabilitiesEvent) {
        registerCapability(event, TANK_MINECART)
    }

    @JvmStatic
    private fun <ENTITY> registerCapability(
        event: RegisterCapabilitiesEvent,
        type: HTDeferredEntityType<ENTITY>,
    ) where ENTITY : Entity, ENTITY : HTHandlerProvider {
        val type1: EntityType<ENTITY> = type.get()
        event.registerEntity(HTItemCapabilities.entityAlt, type1) { entity: ENTITY, _ -> entity.getItemHandler(null) }
        event.registerEntity(HTItemCapabilities.entity, type1, HTHandlerProvider::getItemHandler)
        event.registerEntity(HTFluidCapabilities.entity, type1, HTHandlerProvider::getFluidHandler)
        event.registerEntity(HTEnergyCapabilities.entity, type1, HTHandlerProvider::getEnergyStorage)
    }
}
