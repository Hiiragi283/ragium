package hiiragi283.ragium.api.machine.property

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import hiiragi283.ragium.api.machine.HTMachineKey
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.DataMapValueMerger
import net.neoforged.neoforge.registries.datamaps.DataMapValueRemover
import java.util.*

object HTMachineFuel {
    @JvmField
    val CODEC: Codec<Map<HTMachineKey, Int>> =
        Codec.unboundedMap(HTMachineKey.CODEC, Codec.intRange(1, Int.MAX_VALUE))

    object Merger : DataMapValueMerger<Fluid, Map<HTMachineKey, Int>> {
        override fun merge(
            registry: Registry<Fluid>,
            first: Either<TagKey<Fluid>, ResourceKey<Fluid>>,
            firstValue: Map<HTMachineKey, Int>,
            second: Either<TagKey<Fluid>, ResourceKey<Fluid>>,
            secondValue: Map<HTMachineKey, Int>,
        ): Map<HTMachineKey, Int> = buildMap {
            putAll(firstValue)
            putAll(secondValue)
        }
    }

    data class Remover(val machine: HTMachineKey) : DataMapValueRemover<Fluid, Map<HTMachineKey, Int>> {
        companion object {
            @JvmField
            val CODEC: Codec<Remover> = HTMachineKey.CODEC.xmap(::Remover, Remover::machine)
        }

        override fun remove(
            value: Map<HTMachineKey, Int>,
            registry: Registry<Fluid>,
            source: Either<TagKey<Fluid>, ResourceKey<Fluid>>,
            `object`: Fluid,
        ): Optional<Map<HTMachineKey, Int>> = Optional.of(
            buildMap {
                putAll(value)
                remove(machine)
            },
        )
    }
}
