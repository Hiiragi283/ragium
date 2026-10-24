package hiiragi283.ragium.api.data

import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.transfer.item.ItemResource

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
@ConsistentCopyVisibility
@JvmRecord
data class HTMemoryDiscData private constructor(val item: Holder<Item>, val components: DataComponentPatch) {
    companion object {
        @JvmField
        val CODEC: Codec<HTMemoryDiscData> = HTCodecs.record { instance ->
            instance.group(
                Item.CODEC.fieldOf(ItemInstance.FIELD_ID).forGetter(HTMemoryDiscData::item),
                DataComponentPatch.CODEC
                    .optionalFieldOf(ItemInstance.FIELD_COMPONENTS, DataComponentPatch.EMPTY)
                    .forGetter(HTMemoryDiscData::components),
            ).apply(instance, ::HTMemoryDiscData)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMemoryDiscData> = StreamCodec.composite(
            Item.STREAM_CODEC,
            HTMemoryDiscData::item,
            DataComponentPatch.STREAM_CODEC,
            HTMemoryDiscData::components,
            ::HTMemoryDiscData,
        )

        @JvmStatic
        fun from(template: ItemStackTemplate): HTMemoryDiscData = HTMemoryDiscData(template.item(), template.components())

        @JvmStatic
        fun from(stack: ItemStack): HTMemoryDiscData? = when {
            stack.isEmpty -> null
            else -> HTMemoryDiscData(stack.typeHolder(), stack.componentsPatch)
        }

        @JvmStatic
        fun from(resource: ItemResource): HTMemoryDiscData? = when {
            resource.isEmpty -> null
            else -> HTMemoryDiscData(resource.typeHolder(), resource.componentsPatch)
        }
    }

    init {
        if (item.`is`(HTConstants.Keys.AIR)) {
            throw IllegalArgumentException("item must be non-empty")
        }
    }

    @JvmOverloads
    fun toStack(count: Int = 1): ItemStack = ItemStack(item, count, components)
}
