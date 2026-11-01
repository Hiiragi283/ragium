package hiiragi283.lib.recipe.ingredient

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.data.buildDataPatch
import hiiragi283.lib.recipe.display.HTDisplayableContent
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.lib.text.toText
import hiiragi283.lib.util.Ior
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponents
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.display.SlotDisplay
import java.util.function.Predicate

private typealias MinValue = Int

private typealias MaxValue = Int

/**
 * @see net.minecraft.advancements.criterion.MinMaxBounds.Ints
 */
@JvmRecord
data class HTEnchantmentPowerCondition(val range: Ior<MinValue, MaxValue>) :
    Predicate<Int>,
    HTDisplayableContent {
    companion object {
        @JvmField
        val MAP_CODEC: MapCodec<HTEnchantmentPowerCondition> = HTCodecs.ior(
            HTCodecs.NON_NEGATIVE_INT.fieldOf("min"),
            HTCodecs.NON_NEGATIVE_INT.fieldOf("max"),
        ).xmap(::HTEnchantmentPowerCondition, HTEnchantmentPowerCondition::range)

        @JvmField
        val CODEC: Codec<HTEnchantmentPowerCondition> = Codec.lazyInitialized(MAP_CODEC::codec)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTEnchantmentPowerCondition> =
            HTStreamCodecs.ior(ByteBufCodecs.VAR_INT, ByteBufCodecs.VAR_INT).map(::HTEnchantmentPowerCondition, HTEnchantmentPowerCondition::range)

        @JvmStatic
        fun atLeast(min: Int): HTEnchantmentPowerCondition = HTEnchantmentPowerCondition(Ior.Left(min))

        @JvmStatic
        fun atMost(max: Int): HTEnchantmentPowerCondition = HTEnchantmentPowerCondition(Ior.Right(max))

        @JvmStatic
        fun contains(range: IntRange): HTEnchantmentPowerCondition = contains(range.first, range.last)

        @JvmStatic
        fun contains(min: Int, max: Int): HTEnchantmentPowerCondition = HTEnchantmentPowerCondition(Ior.Both(min, max))
    }

    override fun test(power: Int): Boolean = range.fold(
        { power >= it },
        { power <= it },
        { min: MinValue, max: MaxValue -> power in (min..max) },
    )

    override fun display(): SlotDisplay = SlotDisplay.ItemStackSlotDisplay(
        ItemStackTemplate(
            Items.ENCHANTED_BOOK,
            1,
            buildDataPatch {
                set(
                    DataComponents.ITEM_NAME,
                    HTCommonTranslation.ENCHANTMENT_POWER.translate(
                        range.fold(
                            { "[$it, )" },
                            { "(, $it]" },
                            { min: MinValue, max: MaxValue -> "[$min, $max]" },
                        ).toText(),
                    ),
                )
            },
        ),
    )
}
