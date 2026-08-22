package hiiragi283.ragium.block.entity

import hiiragi283.lib.registry.HTDeferredBlock
import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.lib.registry.HTDeferredBlockEntityTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.block.HTBasicEntityBlock
import hiiragi283.ragium.block.RagiumBlocks
import hiiragi283.ragium.block.entity.machine.HTCrusherBlockEntity
import hiiragi283.ragium.block.entity.machine.HTCuttingMachineBlockEntity
import hiiragi283.ragium.block.entity.machine.HTFreezerBlockEntity
import hiiragi283.ragium.block.entity.machine.HTMelterBlockEntity
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent

data object RagiumBlockEntityTypes {
    @JvmField
    val REGISTER = HTDeferredBlockEntityTypeRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(event: IEventBus) {
        event.addListener(::addSupportedBlocks)

        REGISTER.register(event)
    }

    @JvmStatic
    private fun <BE : HTBlockEntity> registerTick(name: String, factory: BlockEntityType.BlockEntitySupplier<BE>): HTDeferredBlockEntityType<BE> = REGISTER.registerType(name, factory, HTBlockEntity::tickServer, HTBlockEntity::tickClient)

    // Mechanical
    @JvmField
    val CRUSHER: HTDeferredBlockEntityType<HTCrusherBlockEntity> = registerTick(RagiumConstants.CRUSHER, ::HTCrusherBlockEntity)

    @JvmField
    val CUTTING_MACHINE: HTDeferredBlockEntityType<HTCuttingMachineBlockEntity> = registerTick(RagiumConstants.CUTTING_MACHINE, ::HTCuttingMachineBlockEntity)

    // Heat
    @JvmField
    val FREEZER: HTDeferredBlockEntityType<HTFreezerBlockEntity> = registerTick(RagiumConstants.FREEZER, ::HTFreezerBlockEntity)

    @JvmField
    val MELTER: HTDeferredBlockEntityType<HTMelterBlockEntity> = registerTick(RagiumConstants.MELTER, ::HTMelterBlockEntity)

    //    Event    //

    // Supported Blocks
    @JvmStatic
    private fun addSupportedBlocks(event: BlockEntityTypeAddBlocksEvent) {
        for (holder: HTDeferredBlock<*> in RagiumBlocks.REGISTER.asBlockSequence()) {
            val block: Block = holder.get()
            if (block is HTBasicEntityBlock) {
                event.modify(block.type.get(), block)
            }
        }
    }
}
