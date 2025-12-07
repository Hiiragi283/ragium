package hiiragi283.ragium.api.data.map

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.entity.isOf
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.util.unwrapEither
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.function.Function
import java.util.stream.Stream

interface HTSubEntityTypeIngredient {
    companion object {
        private val DISPATCH_CODEC: Codec<HTSubEntityTypeIngredient> = RagiumAPI.SUB_ENTITY_INGREDIENT_TYPE_REGISTRY
            .byNameCodec()
            .dispatch(HTSubEntityTypeIngredient::type, Function.identity())

        @JvmStatic
        val CODEC: Codec<HTSubEntityTypeIngredient> = Codec
            .either(Simple.CODEC.codec, DISPATCH_CODEC)
            .xmap(::unwrapEither) { ingredient: HTSubEntityTypeIngredient ->
                when (ingredient) {
                    is Simple -> Either.left(ingredient)
                    else -> Either.right(ingredient)
                }
            }

        @JvmStatic
        fun simple(entityType: EntityType<*>): HTSubEntityTypeIngredient = Simple(entityType)
    }

    fun type(): MapCodec<out HTSubEntityTypeIngredient>

    fun getEntityType(stack: ItemStack): EntityType<*>?

    fun getPreviewStack(baseItem: Holder<Item>, entityType: Holder<EntityType<*>>): Stream<ItemStack>

    @JvmRecord
    private data class Simple(private val entityType: EntityType<*>) : HTSubEntityTypeIngredient {
        companion object {
            @JvmField
            val CODEC: BiCodec<RegistryFriendlyByteBuf, Simple> = BiCodec.composite(
                VanillaBiCodecs.registryBased(BuiltInRegistries.ENTITY_TYPE).fieldOf("entity_type").forGetter(Simple::entityType),
                ::Simple,
            )
        }

        override fun type(): MapCodec<Simple> = MapCodec.assumeMapUnsafe(CODEC.codec)

        override fun getEntityType(stack: ItemStack): EntityType<*> = entityType

        override fun getPreviewStack(baseItem: Holder<Item>, entityType: Holder<EntityType<*>>): Stream<ItemStack> = when {
            this.entityType.isOf(entityType) -> Stream.of(ItemStack(baseItem))
            else -> Stream.empty()
        }
    }
}
