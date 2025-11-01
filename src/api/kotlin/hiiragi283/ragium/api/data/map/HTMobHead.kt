package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.toHolderLike
import net.minecraft.world.level.ItemLike

@JvmInline
value class HTMobHead(val head: HTItemHolderLike) : HTItemHolderLike by head {
    companion object {
        @JvmField
        val CODEC: Codec<HTMobHead> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTItemHolderLike.CODEC.codec
                        .fieldOf("head")
                        .forGetter(HTMobHead::head),
                ).apply(instance, ::HTMobHead)
        }
    }

    constructor(item: ItemLike) : this(item.toHolderLike())
}
