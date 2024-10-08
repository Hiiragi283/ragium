package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.inventory.HTSidedInventory
import hiiragi283.ragium.api.inventory.HTSidedStorageBuilder
import hiiragi283.ragium.api.inventory.HTStorageIO
import hiiragi283.ragium.api.inventory.HTStorageSide
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumMachineTypes
import net.fabricmc.fabric.api.registry.FuelRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTHeatGeneratorMachineEntity(tier: HTMachineTier) : HTGeneratorMachineEntity(RagiumMachineTypes.HEAT_GENERATOR, tier) {
    var burningTime: Int = 0
        private set
    val isBurning: Boolean
        get() = burningTime > 0
    private var fuelTime: Int = 0

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, registryLookup)
        nbt.putInt("BurningTime", burningTime)
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, registryLookup)
        burningTime = nbt.getInt("BurningTime")
    }

    override fun tickEach(
        world: World,
        pos: BlockPos,
        state: BlockState,
        ticks: Int,
    ) {
        val fuelSlot = 0
        val ashSlot = 1
        when {
            isBurning -> burningTime--
            else -> {
                val ashStack: ItemStack = getStack(ashSlot)
                if (ashStack.count == ashStack.maxCount) return
                val fuelStack: ItemStack = getStack(fuelSlot)
                if (!fuelStack.isEmpty) {
                    val fuel: Item = fuelStack.item
                    val fuelTime: Int = FuelRegistry.INSTANCE.get(fuel) ?: 0
                    this.fuelTime = fuelTime
                    burningTime = fuelTime
                    fuelStack.decrement(1)
                    when {
                        ashStack.isEmpty -> setStack(
                            ashSlot,
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

    override fun getProperty(index: Int): Int = when (index) {
        0 -> fuelTime - burningTime
        1 -> fuelTime
        else -> throw IndexOutOfBoundsException(index)
    }

    override val parent: HTSidedInventory = HTSidedStorageBuilder(2)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .filter { slot: Int, stack: ItemStack ->
            if (slot == 0) {
                AbstractFurnaceBlockEntity.canUseAsFuel(stack)
            }
            true
        }.buildSided()
}
