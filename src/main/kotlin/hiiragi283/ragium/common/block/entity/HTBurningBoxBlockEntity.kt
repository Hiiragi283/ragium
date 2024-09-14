package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.inventory.*
import hiiragi283.ragium.common.screen.HTBurningBoxScreenHandler
import net.fabricmc.fabric.api.registry.FuelRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTBurningBoxBlockEntity(
    pos: BlockPos,
    state: BlockState,
) : BlockEntity(RagiumBlockEntityTypes.BURNING_BOX, pos, state), HTDelegatedInventory, NamedScreenHandlerFactory {

    companion object {
        @JvmField
        val TICKER: BlockEntityTicker<HTBurningBoxBlockEntity> =
            BlockEntityTicker { _: World, _: BlockPos, _: BlockState, blockEntity: HTBurningBoxBlockEntity ->
                when {
                    blockEntity.isBurning -> blockEntity.burningTime--
                    else -> {
                        val fuelStack: ItemStack = blockEntity.getStack(0)
                        if (!fuelStack.isEmpty) {
                            val fuel: Item = fuelStack.item
                            val fuelTime: Int = FuelRegistry.INSTANCE.get(fuel)
                            blockEntity.burningTime = fuelTime
                            fuelStack.decrement(1)
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

    //    HTDelegatedInventory    //

    override val parent: HTSidedInventory = HTSidedStorageBuilder(1)
        .set(0, HTStorageIO.GENERIC, HTStorageSides.ANY)
        .buildInventory()

    override fun markDirty() {
        super<BlockEntity>.markDirty()
    }

    //    NamedScreenHandlerFactory    //

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTBurningBoxScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos))

    override fun getDisplayName(): Text = RagiumBlocks.BURNING_BOX.name

}