package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.impl.HTBasicDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockRegister
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.IEventBus
import vectorwing.farmersdelight.common.block.FeastBlock
import vectorwing.farmersdelight.common.block.PieBlock

object RagiumDelightContents {
    @JvmStatic
    fun register(eventBus: IEventBus) {
        BLOCK_REGISTER.register(eventBus)
    }

    //    Block    //

    @JvmField
    val BLOCK_REGISTER = HTDeferredBlockRegister(RagiumAPI.MOD_ID)

    @JvmField
    val RAGI_CHERRY_PIE: HTBasicDeferredBlock<PieBlock> = BLOCK_REGISTER.registerSimple(
        "ragi_cherry_pie",
        BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE),
        { prop: BlockBehaviour.Properties -> PieBlock(prop, RAGI_CHERRY_PIE_SLICE) },
    )

    @JvmField
    val RAGI_CHERRY_TOAST_BLOCK: HTBasicDeferredBlock<FeastBlock> = BLOCK_REGISTER.registerSimple(
        "ragi_cherry_toast_block",
        BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE),
        { prop: BlockBehaviour.Properties -> FeastBlock(prop, RagiumItems.RAGI_CHERRY_TOAST, true) },
    )

    //    Item    //

    // Food
    @JvmField
    val RAGI_CHERRY_PIE_SLICE: HTSimpleDeferredItem =
        RagiumIntegrationItems.REGISTER.registerSimpleItem("ragi_cherry_pie_slice") { it.food(RagiumFoods.RAGI_CHERRY_PIE_SLICE) }
}
