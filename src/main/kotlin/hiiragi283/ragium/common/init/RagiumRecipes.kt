package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.recipe.HTMachineRecipeType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumRecipes {
    @JvmField
    val SERIALIZER: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, RagiumAPI.MOD_ID)

    @JvmField
    val TYPE: DeferredRegister<RecipeType<*>> =
        DeferredRegister.create(Registries.RECIPE_TYPE, RagiumAPI.MOD_ID)

    @JvmField
    val CHEMICAL_REACTOR: HTMachineRecipeType = create(RagiumMachineKeys.CHEMICAL_REACTOR)

    @JvmStatic
    private fun create(machine: HTMachineKey): HTMachineRecipeType {
        val type = HTMachineRecipeType(machine)
        SERIALIZER.register(machine.name) { _: ResourceLocation -> type }
        TYPE.register(machine.name) { _: ResourceLocation -> type }
        return type
    }
}
