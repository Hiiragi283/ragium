package hiiragi283.lib.item.alchemy

import com.mojang.serialization.Codec
import hiiragi283.lib.item.HTSimpleItemLike
import hiiragi283.lib.item.ItemStack
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.core.TypedInstance
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items

/**
 * ポーション瓶の種類を管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
enum class HTBottleType :
    StringRepresentable,
    HTSimpleItemLike {
    DEFAULT,
    SPLASH,
    LINGERING,
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTBottleType> = HTCodecs.stringEnum(HTBottleType::getSerializedName)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTBottleType> = HTStreamCodecs.enum()

        @JvmStatic
        fun getBottleType(instance: TypedInstance<Item>): HTBottleType? = entries.firstOrNull { instance.`is`(it.asItem()) }
    }

    override fun asItem(): Item = when (this) {
        DEFAULT -> Items.POTION
        SPLASH -> Items.SPLASH_POTION
        LINGERING -> Items.LINGERING_POTION
    }

    override fun toTemplate(count: Int, patch: DataComponentPatch): ItemStackTemplate = ItemStackTemplate(asItem(), count, patch)

    override fun toStack(count: Int, patch: DataComponentPatch): ItemStack = ItemStack(this, count, patch)

    override fun getSerializedName(): String = name.lowercase()
}
