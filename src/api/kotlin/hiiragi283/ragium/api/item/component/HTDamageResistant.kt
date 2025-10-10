package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageType

@JvmInline
value class HTDamageResistant(val types: TagKey<DamageType>) {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTDamageResistant> = VanillaBiCodecs
            .tagKey(Registries.DAMAGE_TYPE)
            .xmap(::HTDamageResistant, HTDamageResistant::types)
    }
}
