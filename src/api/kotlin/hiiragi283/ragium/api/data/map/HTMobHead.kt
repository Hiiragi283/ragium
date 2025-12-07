package hiiragi283.ragium.api.data.map

import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.builtInRegistryHolder
import hiiragi283.ragium.api.registry.idOrThrow
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

@JvmInline
value class HTMobHead(val head: Holder<Item>) : HTItemHolderLike {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTMobHead> = BiCodec.composite(
            VanillaBiCodecs.holder(Registries.ITEM).fieldOf("head").forGetter(HTMobHead::head),
            ::HTMobHead,
        )
    }

    constructor(item: ItemLike) : this(item.builtInRegistryHolder())

    override fun asItem(): Item = head.value()

    override fun getId(): ResourceLocation = head.idOrThrow
}
