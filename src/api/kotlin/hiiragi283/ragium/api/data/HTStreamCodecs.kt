package hiiragi283.ragium.api.data

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.UseAnim
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs

object HTStreamCodecs {
    @JvmField
    val USE_ANIM: StreamCodec<FriendlyByteBuf, UseAnim> = NeoForgeStreamCodecs.enumCodec(UseAnim::class.java)
}
