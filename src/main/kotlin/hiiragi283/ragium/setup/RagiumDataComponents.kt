package hiiragi283.ragium.setup

import com.google.common.primitives.Ints
import hiiragi283.core.api.registry.HTDeferredDataComponentRegister
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.item.component.HTLootTicketTargets
import hiiragi283.ragium.api.item.component.HTSpawnerMob
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import org.apache.commons.lang3.math.Fraction
import java.util.function.IntSupplier

object RagiumDataComponents {
    @JvmField
    val REGISTER = HTDeferredDataComponentRegister(Registries.DATA_COMPONENT_TYPE, RagiumAPI.MOD_ID)

    @JvmField
    val CAPACITY_SCALE: DataComponentType<Int> =
        REGISTER.registerType("capacity_scale", HTCodecs.POSITIVE_INT, ByteBufCodecs.VAR_INT)

    @JvmStatic
    fun getCapacity(base: IntSupplier, scale: Int): Int = Ints.saturatedCast(base.asInt * scale.toLong())

    @JvmField
    val CHARGE_POWER: DataComponentType<Fraction> =
        REGISTER.registerType("charge_power", HTCodecs.NON_NEGATIVE_FRACTION, HTStreamCodecs.FRACTION)

    @JvmField
    val LOOT_TICKET: DataComponentType<HTLootTicketTargets> =
        REGISTER.registerType("loot_ticket", HTLootTicketTargets.CODEC, HTLootTicketTargets.STREAM_CODEC)

    @JvmField
    val SPAWNER_MOB: DataComponentType<HTSpawnerMob> =
        REGISTER.registerType("spawner_mob", HTSpawnerMob.CODEC, HTSpawnerMob.STREAM_CODEC)
}
