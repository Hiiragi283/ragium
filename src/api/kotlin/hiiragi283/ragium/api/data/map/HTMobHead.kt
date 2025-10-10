package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.idOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

@JvmInline
value class HTMobHead(val head: Holder<Item>) : HTItemHolderLike {
    companion object {
        @JvmField
        val CODEC: Codec<HTMobHead> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    RegistryFixedCodec.create(Registries.ITEM).fieldOf("head").forGetter(HTMobHead::head),
                ).apply(instance, ::HTMobHead)
        }
    }

    constructor(item: ItemLike) : this(ItemStack(item).itemHolder)

    override fun asItem(): Item = head.value()

    override fun getId(): ResourceLocation = head.idOrThrow
}
