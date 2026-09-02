package hiiragi283.lib.recipe.result

import com.mojang.serialization.MapCodec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

/**
 * [HTItemResultType]のコーデックを保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTItemResultType<E : HTItemResult.Entry>(
    val codec: MapCodec<E>,
    val streamCodec: StreamCodec<RegistryFriendlyByteBuf, E>
) {
    constructor(codec: MapCodec<E>) : this(codec, ByteBufCodecs.fromCodecWithRegistries(codec.codec()))
}
