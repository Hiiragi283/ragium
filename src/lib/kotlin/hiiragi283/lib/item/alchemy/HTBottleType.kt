package hiiragi283.lib.item.alchemy

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.util.StringRepresentable

/**
 * ポーション瓶の種類を管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
enum class HTBottleType(val filledItem: HTSimpleDeferredItem) : StringRepresentable {
    DEFAULT(vanillaId("potion")),
    SPLASH(vanillaId("splash_potion")),
    LINGERING(vanillaId("lingering_potion"))
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTBottleType> = HTCodecs.stringEnum(HTBottleType::getSerializedName)

        @JvmField
        val FIELD_CODEC: MapCodec<HTBottleType> = HTBottleType.CODEC.optionalFieldOf(HTConstants.BOTTLE_TYPE, DEFAULT)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTBottleType> = HTStreamCodecs.enum()
    }

    constructor(filledItem: Identifier) : this(HTSimpleDeferredItem(filledItem))

    val emptyItem: HTSimpleDeferredItem
        get() = when (this) {
            DEFAULT -> HTPotionFluidAccess.INSTANCE.glassBottle
            SPLASH -> HTPotionFluidAccess.INSTANCE.splashBottle
            LINGERING -> HTPotionFluidAccess.INSTANCE.lingeringBottle
        }

    override fun getSerializedName(): String = name.lowercase()
}
