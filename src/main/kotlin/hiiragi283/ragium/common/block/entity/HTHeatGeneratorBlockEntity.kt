package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.inventory.*
import hiiragi283.ragium.common.screen.HTBurningBoxScreenHandler
import net.fabricmc.fabric.api.registry.FuelRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.AbstractFurnaceBlockEntity
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
    HTBlockEntityBase(type, pos, state),
    HTDelegatedInventory,
    NamedScreenHandlerFactory {
    var burningTime: Int = 0
        private set
    val isBurning: Boolean
        get() = burningTime > 0

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, registryLookup)
        nbt.putInt("BurningTime", burningTime)
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, registryLookup)
        burningTime = nbt.getInt("BurningTime")
    }

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int = when (isBurning) {
        true -> 15
        false -> 0
    }

    override fun tickEach(
        world: World,
        pos: BlockPos,
        state: BlockState,
        ticks: Int,
    ) {
        when {
            isBurning -> burningTime--
            else -> {
                val ashStack: ItemStack = getStack(1)
                if (ashStack.count == ashStack.maxCount) return
                val fuelStack: ItemStack = getStack(0)
                if (!fuelStack.isEmpty) {
                    val fuel: Item = fuelStack.item
                    val fuelTime: Int = FuelRegistry.INSTANCE.get(fuel)
                    burningTime = fuelTime
                    fuelStack.decrement(1)
                    when {
                        ashStack.isEmpty -> setStack(
                            1,
                            RagiumContents.Dusts.ASH
                                .asItem()
                                .defaultStack,
                        )
                        else -> ashStack.increment(1)
                    }
                }
            }
        }
    }

    //    HTDelegatedInventory    //

    override val parent: HTSidedInventory =
        HTSidedStorageBuilder(2)
            .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
            .set(1, HTStorageIO.OUTPUT, HTStorageSide.DOWN)
            .filter { slot: Int, stack: ItemStack ->
                when (slot) {
                    0 -> AbstractFurnaceBlockEntity.canUseAsFuel(stack)
                    else -> true
                }
            }.buildSided()

    override fun markDirty() {
        super<HTBlockEntityBase>.markDirty()
    }

    //    NamedScreenHandlerFactory    //

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTBurningBoxScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos))
}
