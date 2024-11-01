package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.extension.modifyBlockState
import hiiragi283.ragium.api.inventory.*
import hiiragi283.ragium.api.util.HTDynamicPropertyDelegate
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.screen.HTFireboxMachineScreenHandler
import net.fabricmc.fabric.api.registry.FuelRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTFireboxBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.FIREBOX, pos, state),
    HTDelegatedInventory.Simple,
    NamedScreenHandlerFactory {
    var burningTime: Int = 0
    val isBurning: Boolean
        get() = burningTime > 0
    private var burningCache: Boolean = false
        set(value) {
            field = value
            world?.modifyBlockState(pos) { it.with(RagiumBlockProperties.ACTIVE, value) }
        }
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
                val fuelStack: ItemStack = getStack(fuelSlot)
                val ashStack: ItemStack = getStack(ashSlot)
                if (ashStack.count == ashStack.maxCount) return
                if (!fuelStack.isEmpty) {
                    // add ash
                    when {
                        ashStack.isEmpty -> setStack(
                            ashSlot,
                            RagiumContents.Dusts.ASH
                                .asItem()
                                .defaultStack,
                        )

                        else -> ashStack.increment(1)
                    }
                    // consume fuel
                    val fuel: Item = fuelStack.item
                    val fuelTime: Int = FuelRegistry.INSTANCE.get(fuel) ?: 0
                    this.fuelTime = fuelTime
                    burningTime = fuelTime
                    burningCache = true
                    fuelStack.decrement(1)
                } else {
                    fuelTime = 0
                    burningCache = false
                }
            }
        }
    }

    val property = HTDynamicPropertyDelegate(
        2,
        { index: Int ->
            when (index) {
                0 -> fuelTime - burningTime
                1 -> fuelTime
                else -> -1
            }
        },
    )

    //    HTDelegatedInventory    //

    override val parent: HTSimpleInventory = HTStorageBuilder(2)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .filter { slot: Int, stack: ItemStack ->
            if (slot == 0) AbstractFurnaceBlockEntity.canUseAsFuel(stack) else true
        }.buildSimple()

    override fun markDirty() {
        super<HTBlockEntityBase>.markDirty()
    }

    //    NamedScreenHandlerFactory    //

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTFireboxMachineScreenHandler(syncId, playerInventory, parent, property)

    override fun getDisplayName(): Text = RagiumBlocks.FIREBOX.name
}
