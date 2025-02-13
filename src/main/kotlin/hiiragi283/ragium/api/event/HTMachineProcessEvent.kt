package hiiragi283.ragium.api.event

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.machine.HTMachineAccess

/**
 * 機械が処理を行う度に呼び出されるイベント
 * [HTMachineBlockEntity.tickOnServer]中にフックされています。
 */
sealed class HTMachineProcessEvent(machine: HTMachineAccess) : HTMachineEvent(machine) {
    /**
     * 機械が処理に成功した場合のイベント
     */
    class Success(machine: HTMachineAccess) : HTMachineProcessEvent(machine)

    /**
     * 機械が処理に失敗した場合のイベント
     * @param throwable 失敗時のエラーメッセージ
     */
    class Failed(machine: HTMachineAccess, val throwable: Throwable) : HTMachineProcessEvent(machine)
}
