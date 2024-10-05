package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineTypes
import net.fabricmc.fabric.api.registry.FuelRegistry
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.PropertyDelegate
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTHeatGeneratorBlockEntity(pos: BlockPos, state: BlockState, tier: HTMachineTier = HTMachineTier.PRIMITIVE) :
    HTMachineBlockEntityBase(
        RagiumBlockEntityTypes.HEAT_GENERATOR,
        pos,
        state,
        RagiumMachineTypes.HEAT_GENERATOR,
        tier,
    ) {
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

    override fun validateMachineType(machineType: HTMachineType) {
        check(machineType == RagiumMachineTypes.HEAT_GENERATOR)
    }

    override fun tickEach(
        world: World,
        pos: BlockPos,
        state: BlockState,
        ticks: Int,
    ) {
        val fuelSlot = 0
        val ashSlot = 4
        when {
            isBurning -> burningTime--
            else -> {
                val ashStack: ItemStack = getStack(ashSlot)
                if (ashStack.count == ashStack.maxCount) return
                val fuelStack: ItemStack = getStack(fuelSlot)
                if (!fuelStack.isEmpty) {
                    val fuel: Item = fuelStack.item
                    val fuelTime: Int = FuelRegistry.INSTANCE.get(fuel)
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

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        machineType.asGenerator()?.process(world, pos, tier)
    }

    override fun getPropertyDelegate(): PropertyDelegate = object : PropertyDelegate {
        override fun get(index: Int): Int = when (index) {
            0 -> fuelTime - burningTime
            1 -> fuelTime
            else -> throw IndexOutOfBoundsException(index)
        }

        override fun set(index: Int, value: Int) {
        }

        override fun size(): Int = 2
    }
}
