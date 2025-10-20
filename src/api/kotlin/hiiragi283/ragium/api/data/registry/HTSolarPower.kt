package hiiragi283.ragium.api.data.registry

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.advancements.critereon.LocationPredicate
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.ExtraCodecs
import kotlin.jvm.optionals.getOrNull

@JvmRecord
data class HTSolarPower(val predicate: LocationPredicate, val power: Float) {
    companion object {
        @JvmField
        val DIRECT_CODEC: Codec<HTSolarPower> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    LocationPredicate.CODEC.fieldOf("location").forGetter(HTSolarPower::predicate),
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("power").forGetter(HTSolarPower::power),
                ).apply(instance, ::HTSolarPower)
        }

        @JvmField
        val HOLDER_CODEC: Codec<Holder<HTSolarPower>> = RegistryFixedCodec.create(RagiumAPI.SOLAR_POWER_KEY)

        @JvmStatic
        fun getSolarPower(lookup: HolderLookup<HTSolarPower>, level: ServerLevel, pos: BlockPos): Float? = lookup
            .listElements()
            .filter { holder: Holder.Reference<HTSolarPower> ->
                holder.value().predicate.matches(level, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
            }.map(Holder.Reference<HTSolarPower>::value)
            .findFirst()
            .map(HTSolarPower::power)
            .getOrNull()
    }
}
