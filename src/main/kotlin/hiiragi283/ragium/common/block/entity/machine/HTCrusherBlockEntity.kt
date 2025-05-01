package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.block.entity.HTSimpleItemProcessBlockEntity
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) :
    HTSimpleItemProcessBlockEntity(
        RagiumRecipeTypes.CRUSHING.get(),
        SoundEvents.STONE_BREAK,
        RagiumBlockEntityTypes.CRUSHER,
        pos,
        state,
    )
