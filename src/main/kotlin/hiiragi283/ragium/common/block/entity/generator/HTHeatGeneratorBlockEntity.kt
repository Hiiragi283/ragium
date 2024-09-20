package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.common.block.entity.HTBaseBlockEntity
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.inventory.*
import hiiragi283.ragium.common.screen.HTBurningBoxScreenHandler
import net.fabricmc.fabric.api.registry.FuelRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTHeatGeneratorBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBaseBlockEntity(type, pos, state),
    HTDelegatedInventory,
    NamedScreenHandlerFactory {
    companion object {
        @JvmField
        val TICKER: BlockEntityTicker<HTHeatGeneratorBlockEntity> =
            BlockEntityTicker { _: World, _: BlockPos, _: BlockState, blockEntity: HTHeatGeneratorBlockEntity ->
                when {
                    blockEntity.isBurning -> blockEntity.burningTime--
                    else -> {
                        val ashStack: ItemStack = blockEntity.getStack(1)
                        if (ashStack.count == ashStack.maxCount) return@BlockEntityTicker
                        val fuelStack: ItemStack = blockEntity.getStack(0)
                        if (!fuelStack.isEmpty) {
                            val fuel: Item = fuelStack.item
                            val fuelTime: Int = FuelRegistry.INSTANCE.get(fuel)
                            blockEntity.burningTime = fuelTime
                            fuelStack.decrement(1)
                            when {
                                ashStack.isEmpty -> blockEntity.setStack(1, RagiumItems.ASH_DUST.defaultStack)
                                else -> ashStack.increment(1)
                            }
                        }
                    }
                }
            }
    }

    var burningTime: Int = 0
        private set
    val isBurning: Boolean
        get() = burningTime > 0

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        parent.writeNbt(nbt, registryLookup)
        nbt.putInt("BurningTime", burningTime)
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        parent.readNbt(nbt, registryLookup)
        burningTime = nbt.getInt("BurningTime")
    }

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int = when (isBurning) {
        true -> 15
        false -> 0
    }

    //    HTDelegatedInventory    //

    override val parent: HTSidedInventory =
        HTSidedStorageBuilder(2)
            .set(0, HTStorageIO.INPUT, HTStorageSides.ANY)
            .set(1, HTStorageIO.OUTPUT, HTStorageSides.DOWN)
            .buildSided()

    override fun markDirty() {
        super<HTBaseBlockEntity>.markDirty()
    }

    //    NamedScreenHandlerFactory    //

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTBurningBoxScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos))
}
