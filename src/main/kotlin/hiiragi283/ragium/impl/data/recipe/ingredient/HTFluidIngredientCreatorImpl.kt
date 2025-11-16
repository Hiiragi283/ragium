package hiiragi283.ragium.impl.data.recipe.ingredient

import hiiragi283.ragium.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import net.minecraft.core.HolderGetter
import net.minecraft.world.level.material.Fluid

@Suppress("DEPRECATION")
internal class HTFluidIngredientCreatorImpl(getter: HolderGetter<Fluid>) :
    HTIngredientCreatorBase<Fluid, HTFluidIngredient>(getter, Fluid::builtInRegistryHolder),
    HTFluidIngredientCreator
