package hiiragi283.ragium.api.data

import com.mojang.serialization.Codec
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block

data class HTTreeTap(val holderSet: HolderSet<Block>) {
    companion object {
        @JvmField
        val CODEC: Codec<HTTreeTap> =
            RegistryCodecs.homogeneousList(Registries.BLOCK).xmap(::HTTreeTap, HTTreeTap::holderSet)
    }
}
