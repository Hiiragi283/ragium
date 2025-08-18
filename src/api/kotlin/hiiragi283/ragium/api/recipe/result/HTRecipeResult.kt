package hiiragi283.ragium.api.recipe.result

import com.mojang.serialization.DataResult
import net.minecraft.resources.ResourceLocation

interface HTRecipeResult<S : Any> {
    val id: ResourceLocation

    fun getStackResult(): DataResult<S>

    fun getOrEmpty(): S

    fun hasNoMatchingStack(): Boolean
}
