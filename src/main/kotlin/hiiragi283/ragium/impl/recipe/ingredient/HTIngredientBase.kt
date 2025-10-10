package hiiragi283.ragium.impl.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.recipe.ingredient.HTIngredient
import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.HolderSet
import net.minecraft.network.RegistryFriendlyByteBuf

abstract class HTIngredientBase<ENTRY : Any, STACK : Any, CUSTOM : Any>(
    protected val either: Either<HolderSet<ENTRY>, CUSTOM>,
    protected val amount: Int,
) : HTIngredient<STACK> {
    companion object {
        @JvmStatic
        fun <ENTRY : Any, CUSTOM : Any> eitherCodec(
            registryKey: RegistryKey<ENTRY>,
            ingredientCodec: BiCodec<in RegistryFriendlyByteBuf, CUSTOM>,
        ): BiCodec<RegistryFriendlyByteBuf, Either<HolderSet<ENTRY>, CUSTOM>> =
            BiCodecs.xor(VanillaBiCodecs.holderSet(registryKey), ingredientCodec)
    }
}
