package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier

class HTItemOutput private constructor(private val holder: Holder<Item>, val count: Int, val components: DataComponentPatch) :
    Supplier<ItemStack> {
        companion object {
            @JvmField
            val CODEC: Codec<HTItemOutput> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        ItemStack.ITEM_NON_AIR_CODEC
                            .fieldOf("item")
                            .forGetter(HTItemOutput::holder),
                        Codec.intRange(1, 99).optionalFieldOf("count", 1).forGetter(HTItemOutput::count),
                        DataComponentPatch.CODEC
                            .optionalFieldOf("components", DataComponentPatch.EMPTY)
                            .forGetter(HTItemOutput::components),
                    ).apply(instance, ::HTItemOutput)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemOutput> = StreamCodec.composite(
                ByteBufCodecs.holderRegistry(Registries.ITEM),
                HTItemOutput::holder,
                ByteBufCodecs.VAR_INT,
                HTItemOutput::count,
                DataComponentPatch.STREAM_CODEC,
                HTItemOutput::components,
                ::HTItemOutput,
            )

            @JvmStatic
            fun of(item: ItemLike, count: Int): HTItemOutput = of(ItemStack(item, count))

            @JvmStatic
            fun of(stack: ItemStack): HTItemOutput {
                check(!stack.isEmpty)
                return HTItemOutput(stack.itemHolder, stack.count, stack.componentsPatch)
            }
        }

        val id: ResourceLocation = holder.idOrThrow

        override fun get(): ItemStack = ItemStack(holder, count, components)
    }
