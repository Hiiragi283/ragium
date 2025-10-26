package hiiragi283.ragium.api.block.type

import hiiragi283.ragium.api.block.type.HTEntityBlockType.Builder.Impl
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import java.util.function.BiFunction
import java.util.function.Supplier

/**
 * @see mekanism.common.content.blocktype.BlockTypeTile
 */
open class HTEntityBlockType(private val blockEntityTypeGetter: Supplier<HTDeferredBlockEntityType<*>>, attributeMap: BlockAttributeMap) :
    HTBlockType(attributeMap) {
    companion object {
        @JvmStatic
        fun builder(blockEntityTypeGetter: Supplier<HTDeferredBlockEntityType<*>>): Impl<HTEntityBlockType> =
            Impl(blockEntityTypeGetter, ::HTEntityBlockType)
    }

    fun getBlockEntityType(): HTDeferredBlockEntityType<*> = blockEntityTypeGetter.get()

    //    Builder    //

    abstract class Builder<TYPE : HTEntityBlockType, BUILDER : Builder<TYPE, BUILDER>>(
        private val blockEntityTypeGetter: Supplier<HTDeferredBlockEntityType<*>>,
        factory: BiFunction<Supplier<HTDeferredBlockEntityType<*>>, BlockAttributeMap, TYPE>,
    ) : HTBlockType.Builder<TYPE, BUILDER>({ map: BlockAttributeMap -> factory.apply(blockEntityTypeGetter, map) }) {
        //    Impl    //

        class Impl<TYPE : HTEntityBlockType>(
            blockEntityTypeGetter: Supplier<HTDeferredBlockEntityType<*>>,
            factory: BiFunction<Supplier<HTDeferredBlockEntityType<*>>, BlockAttributeMap, TYPE>,
        ) : Builder<TYPE, Impl<TYPE>>(blockEntityTypeGetter, factory)
    }
}
