package hiiragi283.ragium.api.recipe.machine

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.item.HTFluidCubeItem
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.biome.Biome

data class HTFluidPumpEntry(val biomeKey: RegistryKey<Biome>, val fluidName: String) {
    companion object {
        const val KEY: String = "fluid_pump"

        @JvmField
        val REGISTRY_KEY: RegistryKey<Registry<HTFluidPumpEntry>> = RegistryKey.ofRegistry(RagiumAPI.id(KEY))

        @JvmField
        val CODEC: Codec<HTFluidPumpEntry> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    RegistryKey.createCodec(RegistryKeys.BIOME).fieldOf("biome").forGetter(HTFluidPumpEntry::biomeKey),
                    Codec.STRING.fieldOf("fluid").forGetter(HTFluidPumpEntry::fluidName),
                ).apply(instance, ::HTFluidPumpEntry)
        }
    }

    val fluidCube: HTFluidCubeItem? = HTFluidCubeItem[fluidName]
}
