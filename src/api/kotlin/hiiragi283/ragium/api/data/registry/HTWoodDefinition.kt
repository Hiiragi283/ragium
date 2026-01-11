package hiiragi283.ragium.api.data.registry

import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toHolderLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item

data class HTWoodDefinition(private val map: Map<Variant, HTItemHolderLike<*>>, val logTag: TagKey<Item>) {
    companion object {
        @JvmStatic
        private val ITEM_CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemHolderLike<*>> = VanillaBiCodecs
            .holder(Registries.ITEM)
            .xmap(
                Holder<Item>::value.andThen(Item::toHolderLike),
                HTItemHolderLike<*>::getItemHolder,
            )

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTWoodDefinition> =
            BiCodec.composite(
                BiCodecs.mapOf(Variant.CODEC, ITEM_CODEC).fieldOf("variants").forGetter(HTWoodDefinition::map),
                VanillaBiCodecs.tagKey(Registries.ITEM, true).fieldOf("log_tag").forGetter(HTWoodDefinition::logTag),
                ::HTWoodDefinition,
            )
    }

    operator fun get(variant: Variant): HTItemHolderLike<*>? = map[variant]

    enum class Variant : StringRepresentable {
        BOAT,
        BUTTON,
        CHEST_BOAT,
        DOOR,
        FENCE,
        FENCE_GATE,
        HANGING_SIGN,
        LOG,
        PLANKS,
        PRESSURE_PLATE,
        SIGN,
        SLAB,
        STAIRS,
        TRAPDOOR,
        WOOD,
        ;

        companion object {
            @JvmField
            val CODEC: BiCodec<ByteBuf, Variant> = BiCodecs.stringEnum(Variant::getSerializedName)
        }

        override fun getSerializedName(): String = name.lowercase()
    }
}
