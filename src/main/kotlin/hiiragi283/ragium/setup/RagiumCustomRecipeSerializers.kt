package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.recipe.HTBlastChargeRecipe
import hiiragi283.ragium.common.recipe.HTEternalTicketRecipe
import hiiragi283.ragium.common.recipe.HTIceCreamSodaRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object RagiumCustomRecipeSerializers {
    @JvmField
    val REGISTER: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : Recipe<*>, S : RecipeSerializer<T>> register(name: String, serializer: S): Supplier<S> =
        REGISTER.register(name) { _: ResourceLocation -> serializer }

    //    Custom    //

    @JvmField
    val BLAST_CHARGE: Supplier<RecipeSerializer<HTBlastChargeRecipe>> =
        register("blast_charge", SimpleCraftingRecipeSerializer(::HTBlastChargeRecipe))

    @JvmField
    val ETERNAL_TICKET: Supplier<SimpleCraftingRecipeSerializer<HTEternalTicketRecipe>> =
        register("eternal_ticket", SimpleCraftingRecipeSerializer(::HTEternalTicketRecipe))

    @JvmField
    val ICE_CREAM_SODA: Supplier<SimpleCraftingRecipeSerializer<HTIceCreamSodaRecipe>> =
        register("ice_cream_soda", SimpleCraftingRecipeSerializer(::HTIceCreamSodaRecipe))
}
