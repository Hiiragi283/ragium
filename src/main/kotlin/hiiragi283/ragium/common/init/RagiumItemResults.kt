package hiiragi283.ragium.common.init

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.recipe.HTBreweryRecipe
import hiiragi283.ragium.api.recipe.base.HTItemResult
import hiiragi283.ragium.common.recipe.HTApplyFortuneItemResult
import hiiragi283.ragium.common.recipe.HTSimpleItemResult
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumItemResults {
    @JvmField
    val REGISTER: DeferredRegister<MapCodec<out HTItemResult>> =
        DeferredRegister.create(RagiumRegistries.Keys.ITEM_RESULT, RagiumAPI.MOD_ID)

    @JvmField
    val APPLY_FORTUNE: DeferredHolder<MapCodec<out HTItemResult>, MapCodec<HTApplyFortuneItemResult>> =
        REGISTER.register("apply_fortune", HTApplyFortuneItemResult::CODEC)

    @JvmField
    val POTION: DeferredHolder<MapCodec<out HTItemResult>, MapCodec<HTBreweryRecipe.PotionResult>> =
        REGISTER.register("potion", HTBreweryRecipe.PotionResult::CODEC)

    @JvmField
    val SIMPLE: DeferredHolder<MapCodec<out HTItemResult>, MapCodec<HTSimpleItemResult>> =
        REGISTER.register("simple", HTSimpleItemResult::CODEC)
}
