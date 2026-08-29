@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.common.recipe.ingredient

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.ragium.setup.RagiumDataComponents
import java.util.stream.Stream
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.streams.asStream
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType

class HTMemoryDiscIngredient(val dataIngredient: Ingredient) : ICustomIngredient {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMemoryDiscIngredient> = Codec.lazyInitialized { HTCodecs.INGREDIENT }.fieldOf("data").xmap(::HTMemoryDiscIngredient, HTMemoryDiscIngredient::dataIngredient)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMemoryDiscIngredient> = Ingredient.CONTENTS_STREAM_CODEC.map(::HTMemoryDiscIngredient, HTMemoryDiscIngredient::dataIngredient)

        @JvmField
        val TYPE: IngredientType<HTMemoryDiscIngredient> = IngredientType(CODEC, STREAM_CODEC)

        @JvmStatic
        inline fun create(builderAction: IngredientBuilder.() -> Unit): HTMemoryDiscIngredient {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return IngredientBuilder().apply(builderAction).build().let(::HTMemoryDiscIngredient)
        }
    }

    override fun test(stack: ItemStack): Boolean = stack.get(RagiumDataComponents.MEMORY_DISC_DATA)
        ?.let { dataIngredient.test(it.toStack()) }
        ?: false

    override fun getItems(): Stream<ItemStack> = dataIngredient.items
        .asSequence()
        .mapNotNull { it.toResource() }
        .map(RagiumDataComponents::createMemoryDisc)
        .asStream()

    override fun isSimple(): Boolean = false

    override fun getType(): IngredientType<*> = TYPE
}
