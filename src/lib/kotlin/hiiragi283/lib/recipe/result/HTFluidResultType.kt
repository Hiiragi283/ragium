package hiiragi283.lib.recipe.result

import com.mojang.serialization.MapCodec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

@JvmRecord
data class HTFluidResultType<E : HTFluidResult.Entry>(val codec: MapCodec<E>, val streamCodec: StreamCodec<RegistryFriendlyByteBuf, E>) {
    constructor(codec: MapCodec<E>) : this(codec, ByteBufCodecs.fromCodecWithRegistries(codec.codec()))
}
