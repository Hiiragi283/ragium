package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.isNotEmpty
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.function.Supplier

class HTItemOutput(private val holderSet: HolderSet<Item>, val count: Int, val components: DataComponentPatch) : Supplier<ItemStack> {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemOutput> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    RegistryCodecs
                        .homogeneousList(Registries.ITEM)
                        .fieldOf("items")
                        .forGetter(HTItemOutput::holderSet),
                    Codec.intRange(1, 99).optionalFieldOf("count", 1).forGetter(HTItemOutput::count),
                    DataComponentPatch.CODEC
                        .optionalFieldOf("components", DataComponentPatch.EMPTY)
                        .forGetter(HTItemOutput::components),
                ).apply(instance, ::HTItemOutput)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemOutput> = StreamCodec.composite(
            ByteBufCodecs.holderSet(Registries.ITEM),
            HTItemOutput::holderSet,
            ByteBufCodecs.VAR_INT,
            HTItemOutput::count,
            DataComponentPatch.STREAM_CODEC,
            HTItemOutput::components,
            ::HTItemOutput,
        )
    }

    val id: ResourceLocation =
        holderSet.unwrap().map(
            { tagKey: TagKey<Item> -> RagiumAPI.id(tagKey.location().path.replace(oldChar = '/', newChar = '_')) },
            { items: List<Holder<Item>> -> items.first().idOrThrow },
        )

    val isValid: Boolean
        get() = holderSet.isNotEmpty

    override fun get(): ItemStack = holderSet
        .stream()
        .findFirst()
        .map { ItemStack(it, count, components) }
        .orElse(ItemStack.EMPTY)
}
