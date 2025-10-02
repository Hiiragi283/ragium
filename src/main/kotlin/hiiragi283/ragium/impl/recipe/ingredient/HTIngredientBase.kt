package hiiragi283.ragium.impl.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.extension.RegistryKey
import hiiragi283.ragium.api.recipe.ingredient.HTIngredient
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
            BiCodecs.either(BiCodecs.holderSet(registryKey), ingredientCodec)
    }
}
