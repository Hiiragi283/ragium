package hiiragi283.ragium.api.recipe.result

import com.mojang.serialization.DataResult
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation

interface HTRecipeResult<S : Any> {
    val id: ResourceLocation

    fun getStackResult(provider: HolderLookup.Provider?): DataResult<S>

    fun getOrEmpty(provider: HolderLookup.Provider?): S

    fun hasNoMatchingStack(): Boolean = getStackResult(null).isError
}
