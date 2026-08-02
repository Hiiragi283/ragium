package hiiragi283.ragium.setup

import com.google.common.primitives.Ints
import hiiragi283.core.api.data.DataComponentType
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.ragium.api.item.component.HTLootTicketTargets
import hiiragi283.ragium.api.item.component.HTSpawnerMob
import java.util.function.IntSupplier
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import org.apache.commons.lang3.math.Fraction

data object RagiumDataComponents {
    @JvmField
    val CAPACITY_SCALE: DataComponentType<Int> = DataComponentType(HTCodecs.POSITIVE_INT, ByteBufCodecs.VAR_INT)

    @JvmStatic
    fun getCapacity(base: IntSupplier, scale: Int): Int = Ints.saturatedCast(base.asInt * scale.toLong())

    @JvmField
    val CHARGE_POWER: DataComponentType<Fraction> = DataComponentType(HTCodecs.NON_NEGATIVE_FRACTION, HTStreamCodecs.FRACTION)

    @JvmField
    val LOOT_TICKET: DataComponentType<HTLootTicketTargets> = DataComponentType(HTLootTicketTargets.CODEC, HTLootTicketTargets.STREAM_CODEC)

    @JvmField
    val MEMORY_DISC_DATA: DataComponentType<HTItemResourceType> = DataComponentType(HTItemResourceType.CODEC, HTItemResourceType.STREAM_CODEC)

    @JvmField
    val SPAWNER_MOB: DataComponentType<HTSpawnerMob> = DataComponentType(HTSpawnerMob.CODEC, HTSpawnerMob.STREAM_CODEC)
}
