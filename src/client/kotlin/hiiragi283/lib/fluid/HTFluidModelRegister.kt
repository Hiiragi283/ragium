@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.fluid

import hiiragi283.lib.registry.HTFluidContent
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.client.renderer.block.FluidModel
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent

/**
 * Hiiragi Seriesで使用される[RegisterFluidModelsEvent]の補助クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmInline
value class HTFluidModelRegister(private val event: RegisterFluidModelsEvent) {
    /**
     * 指定した[content]に対して液体モデルを登録します。
     * @param builderAction [HTFluidModelBuilder]を初期化するブロック
     */
    inline fun register(content: HTFluidContent, builderAction: HTFluidModelBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        register(content, HTFluidModelBuilder().apply(builderAction).build())
    }

    /**
     * 指定した[content]に対して[液体モデル][model]を登録します。
     */
    fun register(content: HTFluidContent, model: FluidModel.Unbaked) {
        when (content) {
            is HTFluidContent.Flowing -> event.register(model, content.get(), content.flowingHolder.get())
            is HTFluidContent.Virtual -> event.register(model, content.get())
        }
    }
}
