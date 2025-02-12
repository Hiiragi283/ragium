package hiiragi283.ragium.integration.jei.entry

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.machine.HTMachineKey
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid

data class HTGeneratorFuelEntry(val machine: HTMachineKey, val fuelTag: TagKey<Fluid>, val amount: Int) {
    companion object {
        @JvmField
        val CODEC: Codec<HTGeneratorFuelEntry> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTMachineKey.FIELD_CODEC.forGetter(HTGeneratorFuelEntry::machine),
                    TagKey.codec(Registries.FLUID).fieldOf("fuel").forGetter(HTGeneratorFuelEntry::fuelTag),
                    Codec.INT.fieldOf("amount").forGetter(HTGeneratorFuelEntry::amount),
                ).apply(instance, ::HTGeneratorFuelEntry)
        }
    }
}
