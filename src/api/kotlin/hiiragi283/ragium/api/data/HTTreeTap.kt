package hiiragi283.ragium.api.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block

data class HTTreeTap(val holderSet: HolderSet<Block>) {
    companion object {
        @JvmField
        val CODEC: Codec<HTTreeTap> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(HTTreeTap::holderSet),
                ).apply(instance, ::HTTreeTap)
        }
    }
}
