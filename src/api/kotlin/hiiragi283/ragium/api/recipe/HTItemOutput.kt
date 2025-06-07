package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier

class HTItemOutput(private val stack: ItemStack, val chance: Float) : Supplier<ItemStack> {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemOutput> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    MapCodec.assumeMapUnsafe(ItemStack.CODEC).forGetter(HTItemOutput::stack),
                    Codec.floatRange(0f, 1f).optionalFieldOf("chance", 1f).forGetter(HTItemOutput::chance),
                ).apply(instance, ::HTItemOutput)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemOutput> = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            HTItemOutput::stack,
            ByteBufCodecs.FLOAT,
            HTItemOutput::chance,
            ::HTItemOutput,
        )

        @JvmStatic
        fun of(item: ItemLike, count: Int = 1, chance: Float = 1f): HTItemOutput = of(ItemStack(item, count), chance)

        @JvmStatic
        fun of(stack: ItemStack, chance: Float = 1f): HTItemOutput {
            if (stack.isEmpty) {
                error("Empty ItemStack is not allowed for HTItemOutput!")
            }
            if (chance !in (0f..1f)) {
                error("Chance must be in 0f to 1f!")
            }
            return HTItemOutput(stack, chance)
        }
    }

    val id: ResourceLocation = get().itemHolder.idOrThrow

    override fun get(): ItemStack = stack.copy()

    fun getChancedStack(random: RandomSource): ItemStack = when {
        random.nextFloat() <= chance -> get()
        else -> ItemStack.EMPTY
    }
}
