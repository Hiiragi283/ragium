package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.extension.sendPacket
import hiiragi283.ragium.api.inventory.HTSidedStorageBuilder
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.inventory.HTStorageIO
import hiiragi283.ragium.api.inventory.HTStorageSide
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineEntity
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipeProcessorNew
import hiiragi283.ragium.common.init.RagiumNetworks
import hiiragi283.ragium.common.screen.HTProcessorScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.component.ComponentMap
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

open class HTProcessorMachineEntity(type: HTMachineConvertible, tier: HTMachineTier) : HTMachineEntity(type, tier) {
    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        nbt.putInt("recipe_index", currentIndex)
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        currentIndex = nbt.getInt("recipe_index")
    }

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        HTMachineRecipeProcessorNew.process(
            world,
            pos,
            currentRecipe,
            HTMachineRecipe.Input.create(
                machineType,
                tier,
                getStack(0),
                getStack(1),
                getStack(2),
                getStack(3),
                ComponentMap.builder().apply { getCustomData(world, pos, state, this) }.build(),
            ),
            parent,
        )
        /*HTMachineRecipeProcessor(
            parent,
            RagiumRecipeTypes.MACHINE,
        ) { machineType: HTMachineType, tier: HTMachineTier, inventory: HTSimpleInventory ->
            HTMachineRecipe.Input.create(
                machineType,
                tier,
                inventory.getStack(0),
                inventory.getStack(1),
                inventory.getStack(2),
                inventory.getStack(3),
                ComponentMap.builder().apply { getCustomData(world, pos, state, this) }.build(),
            )
        }.process(world, pos, machineType, tier)*/
    }

    open fun getCustomData(
        world: World,
        pos: BlockPos,
        state: BlockState,
        builder: ComponentMap.Builder,
    ) {}

    final override val parent: HTSimpleInventory = HTSidedStorageBuilder(7)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(2, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(3, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(5, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(6, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSimple()

    final override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTProcessorScreenHandler(
            syncId,
            playerInventory,
            packet,
            ScreenHandlerContext.create(parentBE.world, parentBE.pos),
        )

    var currentIndex: Int = 0
        private set
    var maxIndex: Int = -1
        private set
    var currentRecipe: HTMachineRecipe? = null
        set(value) {
            field = value
            if (value != null) {
                world?.sendPacket { player: ServerPlayerEntity ->
                    RagiumNetworks.sendMachineRecipes(
                        player,
                        pos,
                        value,
                    )
                }
            }
        }

    override fun getPropertyDelegate(): PropertyDelegate = object : PropertyDelegate {
        override fun get(index: Int): Int = when (index) {
            0 -> ticks
            1 -> tickRate
            2 -> currentIndex
            3 -> maxIndex
            else -> -1
        }

        override fun set(index: Int, value: Int) {
            when (index) {
                2 -> currentIndex = value
                3 -> maxIndex = value
                else -> Unit
            }
        }

        override fun size(): Int = MAX_PROPERTIES
    }
}
