package hiiragi283.ragium.impl.data.recipe.ingredient

import hiiragi283.ragium.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.impl.recipe.ingredient.HTFluidIngredientImpl
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.level.material.Fluid

@Suppress("DEPRECATION")
class HTFluidIngredientCreatorImpl(getter: HolderGetter<Fluid>) :
    HTIngredientCreatorBase<Fluid, HTFluidIngredient>(getter, Fluid::builtInRegistryHolder),
    HTFluidIngredientCreator {
    override fun fromSet(holderSet: HolderSet<Fluid>, amount: Int): HTFluidIngredient = HTFluidIngredientImpl(holderSet, amount)

    override fun codec(): BiCodec<RegistryFriendlyByteBuf, HTFluidIngredient> = HTFluidIngredientImpl.CODEC
}
