package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.Item

@JvmInline
value class HTRepairable(val items: HolderSet<Item>) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTRepairable> = VanillaBiCodecs
            .holderSet(Registries.ITEM)
            .xmap(::HTRepairable, HTRepairable::items)
    }
}
