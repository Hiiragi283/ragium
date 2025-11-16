package hiiragi283.ragium.api.data.map

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.entity.isOf
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.function.Function

interface HTSubEntityTypeIngredient {
    companion object {
        private val DISPATCH_CODEC: Codec<HTSubEntityTypeIngredient> = RagiumAPI.SUB_ENTITY_INGREDIENT_TYPE_REGISTRY
            .byNameCodec()
            .dispatch(HTSubEntityTypeIngredient::type, Function.identity())

        @JvmStatic
        val CODEC: Codec<HTSubEntityTypeIngredient> = Codec
            .either(Simple.CODEC, DISPATCH_CODEC)
            .xmap(
                Either<Simple, HTSubEntityTypeIngredient>::unwrap,
            ) { ingredient: HTSubEntityTypeIngredient ->
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

    fun getPreviewStack(baseItem: Holder<Item>, entityType: Holder<EntityType<*>>): ItemStack

    @JvmRecord
    private data class Simple(private val entityType: EntityType<*>) : HTSubEntityTypeIngredient {
        companion object {
            @JvmField
            val CODEC: Codec<Simple> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        BuiltInRegistries.ENTITY_TYPE
                            .byNameCodec()
                            .fieldOf("entity_type")
                            .forGetter(Simple::entityType),
                    ).apply(instance, ::Simple)
            }
        }

        override fun type(): MapCodec<Simple> = MapCodec.assumeMapUnsafe(CODEC)

        override fun getEntityType(stack: ItemStack): EntityType<*> = entityType

        override fun getPreviewStack(baseItem: Holder<Item>, entityType: Holder<EntityType<*>>): ItemStack = when {
            this.entityType.isOf(entityType) -> ItemStack(baseItem)
            else -> ItemStack.EMPTY
        }
    }
}
