package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.RagiumAPI
import mezz.jei.api.gui.builder.IIngredientAcceptor
import net.minecraft.core.RegistryAccess
import net.minecraft.world.item.crafting.Recipe

//    IRecipeLayoutBuilder    //

fun <T : IIngredientAcceptor<T>> IIngredientAcceptor<T>.addResult(recipe: Recipe<*>): IIngredientAcceptor<T> {
    val access: RegistryAccess = RagiumAPI.getInstance().getRegistryAccess() ?: return this
    return this.addItemStack(recipe.getResultItem(access))
}
