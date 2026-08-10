package hiiragi283.lib.recipe.result

import com.mojang.serialization.Codec
import hiiragi283.lib.fluid.transmuteCopy
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.resource.HTIdLike
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate

/**
 * 液体の完成品を提供するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmInline
value class HTFluidResult private constructor(@PublishedApi internal val template: FluidStackTemplate) : HTIdLike {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidResult> = FluidStackTemplate.CODEC.xmap(::create, HTFluidResult::template)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidResult> = FluidStackTemplate.STREAM_CODEC.map(::create, HTFluidResult::template)

        /**
         * 液体流が指定されている場合，液体源に置き換えます。
         */
        @JvmStatic
        private fun validate(template: FluidStackTemplate): FluidStackTemplate {
            val fluid: Fluid = template.typeHolder().value()
            return when {
                !fluid.isSource(fluid.defaultFluidState()) && fluid is FlowingFluid -> template.transmuteCopy(fluid.source)!!
                else -> template
            }
        }

        /**
         * 新しい[HTFluidResult]のインスタンスを作成します。
         */
        @JvmStatic
        fun create(stack: FluidStack): HTFluidResult = stack.let(FluidStackTemplate::fromNonEmptyStack).let(::create)

        /**
         * 新しい[HTFluidResult]のインスタンスを作成します。
         */
        @JvmStatic
        fun create(template: FluidStackTemplate): HTFluidResult = template.let(::validate).let(::HTFluidResult)
    }

    /**
     * 完成品の液体量
     */
    inline val amount: Int get() = template.amount()

    /**
     * このインスタンスのコピーを作成します。
     * @param newAmount 新しい液体量
     */
    fun copyWithAmount(newAmount: Int): HTFluidResult = create(template.withAmount(newAmount))

    /**
     * 液体の完成品を取得します。
     */
    fun create(): FluidStack = template.create()

    override fun getId(): Identifier = template.typeHolder().getKeyOrThrow().identifier()
}
