package hiiragi283.ragium.api.recipe.result

import com.mojang.serialization.DataResult
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation

interface HTRecipeResult<STACK : Any> {
    val id: ResourceLocation

    fun getStackResult(provider: HolderLookup.Provider?): DataResult<STACK>

    fun getOrEmpty(provider: HolderLookup.Provider?): STACK

    fun hasNoMatchingStack(): Boolean = getStackResult(null).isError
}
