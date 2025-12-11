package hiiragi283.ragium.common.block.entity.component

import hiiragi283.ragium.api.serialization.component.HTComponentSerializable
import hiiragi283.ragium.api.serialization.value.HTValueSerializable

/**
 * @see mekanism.common.tile.component.ITileComponent
 */
interface HTBlockEntityComponent :
    HTValueSerializable,
    HTComponentSerializable
