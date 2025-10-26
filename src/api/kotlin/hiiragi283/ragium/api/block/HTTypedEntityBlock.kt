package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.block.type.HTEntityBlockType
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import java.util.function.UnaryOperator

typealias HTSimpleTypedEntityBlock = HTTypedEntityBlock<HTEntityBlockType>

/**
 * @see mekanism.common.block.prefab.BlockTile
 */
open class HTTypedEntityBlock<TYPE : HTEntityBlockType>(type: TYPE, properties: Properties) :
    HTTypedBlock<TYPE>(type, properties),
    HTBlockWithEntity {
    constructor(
        type: TYPE,
        operator: UnaryOperator<Properties>,
    ) : this(type, operator.apply(Properties.of().strength(3.5f, 16f).requiresCorrectToolForDrops()))

    final override fun getBlockEntityType(): HTDeferredBlockEntityType<*> = type.getBlockEntityType()
}
