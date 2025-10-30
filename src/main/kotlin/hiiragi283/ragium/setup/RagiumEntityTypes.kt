package hiiragi283.ragium.setup

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.impl.HTDeferredEntityType
import hiiragi283.ragium.api.registry.impl.HTDeferredEntityTypeRegister
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTHandlerProvider
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.HTEnergyCapabilities
import hiiragi283.ragium.api.storage.capability.HTExperienceCapabilities
import hiiragi283.ragium.api.storage.capability.HTFluidCapabilities
import hiiragi283.ragium.api.storage.capability.HTItemCapabilities
import hiiragi283.ragium.api.storage.experience.HTExperienceStorage
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.entity.HTBlastCharge
import hiiragi283.ragium.common.entity.HTThrownCaptureEgg
import hiiragi283.ragium.common.entity.vehicle.HTDrumMinecart
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.common.util.HTExperienceHelper
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent

object RagiumEntityTypes {
    @JvmField
    val REGISTER = HTDeferredEntityTypeRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        REGISTER.register(eventBus)

        eventBus.addListener(::registerEntityCapabilities)
    }

    @JvmField
    val BLAST_CHARGE: HTDeferredEntityType<HTBlastCharge> =
        REGISTER.registerType("blast_charge", ::HTBlastCharge, MobCategory.MISC) { builder: EntityType.Builder<HTBlastCharge> ->
            builder
                .sized(0.25f, 0.25f)
                .clientTrackingRange(4)
                .updateInterval(10)
        }

    @JvmField
    val ELDRITCH_EGG: HTDeferredEntityType<HTThrownCaptureEgg> =
        REGISTER.registerType("eldritch_egg", ::HTThrownCaptureEgg, MobCategory.MISC) { builder: EntityType.Builder<HTThrownCaptureEgg> ->
            builder
                .sized(0.25f, 0.25f)
                .clientTrackingRange(4)
                .updateInterval(10)
        }

    //    Minecart    //

    @JvmField
    val DRUMS: Map<HTDrumTier, HTDeferredEntityType<HTDrumMinecart>> = HTDrumTier.entries.associateWith { tier: HTDrumTier ->
        val factory: (EntityType<*>, Level) -> HTDrumMinecart = when (tier) {
            HTDrumTier.SMALL -> HTDrumMinecart::Small
            HTDrumTier.MEDIUM -> HTDrumMinecart::Medium
            HTDrumTier.LARGE -> HTDrumMinecart::Large
            HTDrumTier.HUGE -> HTDrumMinecart::Huge
            HTDrumTier.CREATIVE -> HTDrumMinecart::Creative
        }
        REGISTER.registerType(
            tier.entityPath,
            factory,
            MobCategory.MISC,
        ) { builder: EntityType.Builder<HTDrumMinecart> -> builder.sized(0.98f, 0.7f) }
    }

    //    Event    //

    // Capabilities
    @JvmStatic
    private fun registerEntityCapabilities(event: RegisterCapabilitiesEvent) {
        for (type: HTDeferredEntityType<HTDrumMinecart> in DRUMS.values) {
            registerCapability(event, type)
        }

        // Exp Storage for Exp Orb
        event.registerEntity(HTExperienceCapabilities.entity, EntityType.EXPERIENCE_ORB) { orb: ExperienceOrb, _ ->
            object :
                HTExperienceStorage.Basic(),
                HTValueSerializable.Empty {
                override fun setAmount(amount: Long, action: HTStorageAction) {
                    if (action.execute) {
                        orb.value = Ints.saturatedCast(amount)
                        onContentsChanged()
                    }
                }

                override fun getAmount(): Long = orb.value.toLong()

                override fun getCapacity(): Long = Long.MAX_VALUE

                override fun onContentsChanged() {
                    if (orb.value <= 0) {
                        orb.discard()
                    }
                }
            }
        }
        // Exp Storage for Player
        event.registerEntity(HTExperienceCapabilities.entity, EntityType.PLAYER) { player: Player, _ ->
            object :
                HTExperienceStorage.Basic(),
                HTContentListener.Empty,
                HTValueSerializable.Empty {
                override fun setAmount(amount: Long, action: HTStorageAction) {
                    if (action.execute) {
                        HTExperienceHelper.setPlayerExp(player, amount)
                    }
                }

                override fun getAmount(): Long = HTExperienceHelper.getPlayerExp(player)

                override fun getCapacity(): Long = Long.MAX_VALUE
            }
        }
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
        event.registerEntity(HTExperienceCapabilities.entity, type1, HTHandlerProvider::getExperienceStorage)
    }
}
