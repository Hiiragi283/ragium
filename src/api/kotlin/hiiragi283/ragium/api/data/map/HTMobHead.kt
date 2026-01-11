package hiiragi283.ragium.api.data.map

import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

@JvmInline
value class HTMobHead(private val head: Holder<Item>) : HTItemHolderLike<Item> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTMobHead> = BiCodec.composite(
            VanillaBiCodecs.holder(Registries.ITEM).fieldOf("head").forGetter(HTMobHead::getItemHolder),
            ::HTMobHead,
        )
    }

    @Suppress("DEPRECATION")
    constructor(item: ItemLike) : this(item.asItem().builtInRegistryHolder())

    override fun getItemHolder(): Holder<Item> = head

    override fun asItem(): Item = head.value()
}
