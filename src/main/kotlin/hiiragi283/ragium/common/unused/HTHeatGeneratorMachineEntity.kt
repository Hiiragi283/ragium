package hiiragi283.ragium.common.unused

import hiiragi283.ragium.api.machine.HTMachineTier

class HTHeatGeneratorMachineEntity(tier: HTMachineTier)
/* : HTGeneratorMachineEntity(TODO(), tier) {
    var burningTime: Int = 0
    val isBurning: Boolean
        get() = burningTime > 0
    private var fuelTime: Int = 0

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        nbt.putInt("BurningTime", burningTime)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
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
        // RagiumAPI.log { info("Burning Time; $burningTime") }
    }

    override fun getProperty(index: Int): Int = when (index) {
        0 -> fuelTime - burningTime
        1 -> fuelTime
        else -> -1
    }

    override val parent: HTSidedInventory = HTStorageBuilder(2)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .filter { slot: Int, stack: ItemStack ->
            if (slot == 0) {
                AbstractFurnaceBlockEntity.canUseAsFuel(stack)
            }
            true
        }.buildSided()
}*/
