package hiiragi283.ragium.common.init

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeCondition
import hiiragi283.ragium.common.recipe.HTEnchantmentCondition
import hiiragi283.ragium.common.recipe.HTSourceCondition
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumMachineRecipeConditions {
    @JvmField
    val REGISTER: DeferredRegister<MapCodec<out HTMachineRecipeCondition>> =
        DeferredRegister.create(RagiumRegistries.Keys.RECIPE_CONDITION, RagiumAPI.MOD_ID)

    @JvmField
    val ENCHANTMENT: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTEnchantmentCondition>> =
        REGISTER.register("enchantment", HTEnchantmentCondition::CODEC)

    @JvmField
    val SOURCE: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTSourceCondition>> =
        REGISTER.register("source", HTSourceCondition::CODEC)
}
