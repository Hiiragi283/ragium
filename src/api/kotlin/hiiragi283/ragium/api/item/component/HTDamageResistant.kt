package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType

data class HTDamageResistant(val types: TagKey<DamageType>) {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTDamageResistant> = BiCodecs
            .tagKey(Registries.DAMAGE_TYPE)
            .xmap(::HTDamageResistant, HTDamageResistant::types)
    }

    fun isResistantTo(source: DamageSource): Boolean = source.`is`(types)
}
