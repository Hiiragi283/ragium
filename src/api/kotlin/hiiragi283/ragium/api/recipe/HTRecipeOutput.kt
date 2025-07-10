package hiiragi283.ragium.api.recipe

import com.mojang.datafixers.util.Either
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.toDataResult
import hiiragi283.ragium.api.tag.HTTagHelper
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import java.util.function.Function
import java.util.function.Supplier

abstract class HTRecipeOutput<T : Any, S : Any>(
    protected val entry: Either<ResourceLocation, TagKey<T>>,
    protected val amount: Int,
    protected val components: DataComponentPatch,
) : Supplier<S> {
    val id: ResourceLocation = entry.map(Function.identity(), TagKey<*>::location)

    protected fun getFirstHolder(): DataResult<out Holder<T>> = entry.map(::getFirstHolderFromId, ::getFirstHolderFromTag)

    protected open fun getFirstHolderFromId(id: ResourceLocation): DataResult<out Holder<T>> =
        registry.getHolder(id).toDataResult { "Missing id in ${registry.key()}: $id" }

    protected open fun getFirstHolderFromTag(tagKey: TagKey<T>): DataResult<out Holder<T>> = registry
        .getTag(tagKey)
        .flatMap(HTTagHelper::getFirstHolder)
        .toDataResult { "Missing tag in ${registry.key()}: ${tagKey.location}" }

    protected abstract val registry: Registry<T>

    fun getStackResult(): DataResult<S> = getFirstHolder().map { holder: Holder<T> -> createStack(holder, amount, components) }

    val isValid: Boolean get() = getStackResult().isSuccess

    protected abstract fun createStack(holder: Holder<T>, amount: Int, components: DataComponentPatch): S

    override fun get(): S = getStackResult().orThrow
}
