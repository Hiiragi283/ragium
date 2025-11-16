package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import hiiragi283.ragium.api.serialization.codec.BiCodec
import io.netty.buffer.ByteBuf
import net.minecraft.core.registries.Registries
import net.minecraft.world.damagesource.DamageType

@JvmInline
value class HTDamageResistant(val entry: HTKeyOrTagEntry<DamageType>) {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTDamageResistant> = HTKeyOrTagHelper.INSTANCE
            .codec(Registries.DAMAGE_TYPE)
            .xmap(::HTDamageResistant, HTDamageResistant::entry)
    }
}
